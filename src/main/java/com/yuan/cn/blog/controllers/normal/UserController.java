package com.yuan.cn.blog.controllers.normal;

import com.yuan.cn.blog.commons.*;
import com.yuan.cn.blog.pojo.Gender;
import com.yuan.cn.blog.pojo.User;
import com.yuan.cn.blog.service.UserService;
import com.yuan.cn.blog.utils.MD5Util;
import com.yuan.cn.blog.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    protected UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/usr/check-unique-username")
    public BlogResponse<Integer> checkUniqueUsername(@RequestParam String username) {
        int ret = userService.countUsername(username);
        if (ret != 0) {
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_DATA_MESSAGE, ResponseCode.ERROR.getCode(), null, ret);
        }
        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
    }

    @GetMapping("/usr/check-unique-email")
    public BlogResponse<Integer> checkUniqueEmail(@RequestParam String email) {
        int ret = userService.countEmail(email.trim());
        if (ret != 0) {
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_DATA_MESSAGE, ResponseCode.ERROR.getCode(), null, ret);
        }
        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
    }

    @PostMapping(value = "/usr/register")
    public BlogResponse<Void> registerUser(String username, String password, String email,
                                           @RequestParam(name = "gender", required = false) String gender,
                                           @RequestParam(name = "birthday", required = false) String birthday,
                                           @RequestParam(name = "headPhoto",required = false) MultipartFile headPhoto,
                                           @RequestParam("token") String token, @RequestParam("code") String code, HttpServletRequest request) {
        if (!token.equals(request.getSession().getAttribute("user-token")) || code.equals("")){
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "参数错误！", null);
        }
        String ecode = Const.EMAIL_CODE_CACHING_MAP.get(request.getSession().getId());
        if (ecode == null)
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "验证码已过期！", null);

        if (!code.equals(ecode))
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "验证码错误！", null);

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password.trim());
        user.setEmail(email.trim());

        user.setCreateTime(new Date());
        user.setStatus(0);
        // 性别，如果没有值，那么就是未定义
        if (gender.trim().equals("男")) {
            user.setGender(Gender.MAN.getGender());
        } else {
            user.setGender(Gender.WOMAN.getGender());
        }

        if (!birthday.trim().equals("")) {
            //todo 传进来的生日是否合理
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(birthday.trim());
                Date today = new Date();
                // 不合理的日期
                if (date.getTime() < today.getTime()) {
                    user.setBirthday(date);

                    // 计算年龄
                    Calendar calendar = Calendar.getInstance();
                    int nowYear = calendar.get(Calendar.YEAR);
                    calendar.setTime(date);
                    int birthYear = calendar.get(Calendar.YEAR);
                    user.setAge(nowYear - birthYear);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (null != headPhoto) {
            saveUserHeadPicture(headPhoto, user);
        }else
            user.setHeadPhotoUrl("default.jpg");

        int ret = userService.insertUserSelective(user);

        if (ret == 1) {
            return BlogResponse.SUCCESS(SuccessType.JUST_SUCCESS, null, null);
        } else {
            return BlogResponse.ERROR(ErrorType.NORMAL_ERROR, ResponseCode.ERROR.getCode(), null, null);
        }
    }

    private void saveUserHeadPicture(MultipartFile headPhoto, User user) {
        // todo 保存头像以及设置头像路径
        String parentPath = PropertiesUtil.getProperty("head-photo-for-save");
        File parent = new File(parentPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }
        String originName = headPhoto.getOriginalFilename();
        String extension = originName.substring(originName.lastIndexOf("."));
        String newName = MD5Util.MD5EncodeUtf8(originName + System.currentTimeMillis() / 1000) + extension;
        String finallyName = parentPath + "/" + newName;
        user.setHeadPhotoUrl(newName);
        ThreadUtil.submitTask(() -> {
            // todo save picture to disk
            File file = new File(finallyName);
            boolean created = false;
            if (!file.exists()) {
                try {
                    created = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!created) {
                throw new IllegalStateException("创建文件失败： " + finallyName);
            }
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                InputStream inputStream = headPhoto.getInputStream();

                int len;
                byte[] buf = new byte[1024];
                while ((len = inputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @PostMapping("/usr/code/generate-user-register-token")
    @ResponseBody
    public BlogResponse<String> generateToken(HttpServletRequest request){
        String userToken = UUID.randomUUID().toString();
        String header = request.getHeader("Generate-User-Register-Token");
        if (header != null && !header.equals("")){
            if (header.equals("true")){
                request.getSession().setAttribute("user-token", userToken);
                return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, userToken);
            }
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "非法请求！", null);
    }
    @GetMapping("/usr/count")
    public int countUser() {
        return userService.countUser();
    }

    @GetMapping("/usr/head-photo")
    public void writeHeadPhoto(HttpServletRequest request, HttpServletResponse response){
        try {
            String url = userService.getUserByUsername(Const.USER_CACHING_MAP.get(request.getSession().getId())).getHeadPhotoUrl();
            userService.fetchHeadPhoto(url, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/usr/head-photo-using-name")
    public void writeHeadPhotoUsingUsername(@RequestParam("u") String username, HttpServletResponse response){
        try {
            int countUsername = userService.countUsername(username);
            if (username == null || username.equals("") || countUsername == 0){
                response.setStatus(404);
                return;
            }
            String url = userService.getUserByUsername(username).getHeadPhotoUrl();
            userService.fetchHeadPhoto(url, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/usr/need-login")
    public BlogResponse<String> needLogin(){
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_DATA_MESSAGE, ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription(), "/");
    }

    @PostMapping("/usr/send-email-code")
    @ResponseBody
    public BlogResponse<Integer> sendEmailCode(@RequestParam("e") String targetEmail, HttpServletRequest request){
        if (targetEmail.equals(""))
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "非法请求！", null);

        Random random = new Random();
        String code = "";
        code += random.nextInt(10);
        code += random.nextInt(10);
        code += random.nextInt(10);
        code += random.nextInt(10);
        try {
            if (Const.sendEmailCode(code, targetEmail, javaMailSender) == 0) {
                Const.EMAIL_CODE_CACHING_MAP.insert(request.getSession().getId(), code, Const.CODE_EXPIRED);
                return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, 1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "发送邮件失败", null);
    }
}
