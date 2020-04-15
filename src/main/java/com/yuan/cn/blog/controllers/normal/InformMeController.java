package com.yuan.cn.blog.controllers.normal;

import com.yuan.cn.blog.commons.BlogResponse;
import com.yuan.cn.blog.commons.Const;
import com.yuan.cn.blog.commons.ErrorType;
import com.yuan.cn.blog.commons.SuccessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/positive")
public class InformMeController {

    @Autowired
    private JavaMailSender sender;

    @PostMapping("/inform")
    @ResponseBody
    public BlogResponse<Integer> informMe(HttpServletRequest request){
        String username = Const.USER_CACHING_MAP.get(request.getSession().getId());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd mm:HH:ss");
        if (username != null){
            int ret = Const.sendMessage(username + "正在通知你：服务器发生错误了，赶紧查看日志并调试！时间： " + format.format(new Date()) , "1905269298@qq.com", sender);
            if (ret == 0)
                return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
        }else {
            int ret = Const.sendMessage("通知你：服务器发生错误了，赶紧查看日志并调试！时间： " + format.format(new Date()) , "1905269298@qq.com", sender);
            if (ret == 0)
                return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "发送邮件失败！", null);
    }
}
