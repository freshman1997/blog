package com.yuan.cn.blog.config.normal;

import com.yuan.cn.blog.utils.PropertiesUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender(){
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.auth", PropertiesUtil.getProperty("mail.smtp.auth"));
        prop.setProperty("mail.smtp.timeout", PropertiesUtil.getProperty("mail.smtp.timeout"));
        prop.setProperty("mail.smtp.ssl.enable", "true");
        prop.setProperty("mail.smtp.socketFactory.port", "465");
        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setUsername(PropertiesUtil.getProperty("mail.smtp.username"));
        javaMailSender.setPassword(PropertiesUtil.getProperty("mail.smtp.password"));
        javaMailSender.setHost(PropertiesUtil.getProperty("mail.smtp.host"));
        javaMailSender.setDefaultEncoding(PropertiesUtil.getProperty("mail.smtp.defaultEncoding"));
        javaMailSender.setPort(465);
        javaMailSender.setJavaMailProperties(prop);
        return javaMailSender;
    }
}
