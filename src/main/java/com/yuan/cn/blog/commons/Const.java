package com.yuan.cn.blog.commons;

import com.yuan.cn.blog.utils.PropertiesUtil;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public final class Const {

    public static final CachingMap<String, String> USER_CACHING_MAP = new CachingMap<>();

    // 请求缓存
    public static final CachingMap<String, String> EMAIL_CODE_CACHING_MAP = new CachingMap<>();

    public static final int USER_CACHING_TIMEOUT = 1000 * 60 * 60;

    // 验证码有效时间 10分钟
    public static final int CODE_EXPIRED = 1000 * 60 * 10;
    /**
     * 最大允许上传 50M 大小的文件
     */
    public static final int MAX_UPLOAD_SIZE = 1024 * 1024 * 50;

    public static final String BLOG_IMAGES_SAVING_PATH = PropertiesUtil.getProperty("blog-save-dir");

    public static int sendEmailCode(String code, String targetEmail, JavaMailSender javaMailSender){
        MimeMessage mMessage = javaMailSender.createMimeMessage();//创建邮件对象
        MimeMessageHelper mMessageHelper;
        String from;
        try {
            //从配置文件中拿到发件人邮箱地址
            from = PropertiesUtil.getProperty("mail.smtp.username");
            mMessageHelper = new MimeMessageHelper(mMessage,false);
            mMessageHelper.setFrom(from);//发件人邮箱
            mMessageHelper.setTo(targetEmail);//收件人邮箱
            mMessageHelper.setSubject("邮件验证码");//邮件的主题
            mMessageHelper.setText("<p><strong>[Jack Yuan博客验证码]</strong></p><br/>" +
                    "<span>验证码：<b>"+code+"</b> (有效期<b>10分钟</b>)</span>",true);//邮件的文本内容，true表示文本以html格式打开
            javaMailSender.send(mMessage);//发送邮件
            return 0;
        } catch (MessagingException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int sendMessage(String message, String targetEmail, JavaMailSender javaMailSender){
        MimeMessage mMessage = javaMailSender.createMimeMessage();//创建邮件对象
        MimeMessageHelper mMessageHelper;
        String from;
        try {
            //从配置文件中拿到发件人邮箱地址
            from = PropertiesUtil.getProperty("mail.smtp.username");
            mMessageHelper = new MimeMessageHelper(mMessage,false);
            mMessageHelper.setFrom(from);//发件人邮箱
            mMessageHelper.setTo(targetEmail);//收件人邮箱
            mMessageHelper.setSubject("服务器内部错误通知");//邮件的主题
            mMessageHelper.setText(message,false);//邮件的文本内容，true表示文本以html格式打开
            javaMailSender.send(mMessage);//发送邮件
            return 0;
        } catch (MessagingException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
