package com.yuan.cn.blog.config.basic;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.cn.blog.commons.Const;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan("com.yuan.cn.blog")
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ContextLoaderListener contextLoaderListener() {
        return new ContextLoaderListener();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/js/**", "/static/css/**", "/static/img/**", "/static/image/**", "/static/fonts/**")
                .addResourceLocations("/static/js/", "/static/css/", "/static/img/", "/static/image/", "/static/fonts/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        // 要求DispatcherServlet将静态资源的请求转发到Servlet容器中的默认Servlet上，而不是使用DispatcherServlet本身来处理此类请求
        configurer.enable();
    }

    // spring mvc的拦截器配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        TimeCounterInterceptor timeCounterInterceptor = new TimeCounterInterceptor();
//        registry.addInterceptor(timeCounterInterceptor).addPathPatterns("/usr/*").excludePathPatterns("/usr/login", "/usr/register", "/usr/checkValid",
//                "/usr/forgetAndGetQuestion", "/usr/forget_check_answer", "/usr/forget_reset_password", "/usr/reset_password", "/usr/logout");


    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.TEXT_HTML);
        list.add(MediaType.TEXT_PLAIN);
        list.add(MediaType.TEXT_XML);
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setSupportedMediaTypes(list);
        converters.add(stringHttpMessageConverter);

        List<MediaType> list1 = new ArrayList<>();
        list1.add(MediaType.APPLICATION_JSON_UTF8);
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mappingJackson2HttpMessageConverter.setObjectMapper(mapper);
        converters.add(mappingJackson2HttpMessageConverter);

    }

    // 文件上传配置
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(Const.MAX_UPLOAD_SIZE);
        commonsMultipartResolver.setMaxInMemorySize(4096);
        commonsMultipartResolver.setDefaultEncoding("UTF-8");
        return commonsMultipartResolver;
    }

    // 以下为thymeleaf的配置
    @Bean
    public SpringResourceTemplateResolver resourceTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("/WEB-INF/pages/html/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(1);
        resolver.setName("/*");
        // 开发阶段设置不能缓存
        resolver.setCacheable(false);
        return resolver;
    }

    @Bean
    public ViewResolver htmlViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resourceTemplateResolver());
        return engine;
    }

    // jsp 的配置
//    @Bean
//    public InternalResourceViewResolver viewResolver(){
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/pages/jsp/");
//        viewResolver.setSuffix(".jsp");
//        viewResolver.setOrder(2);
//        viewResolver.setViewClass(JstlView.class);
//        viewResolver.setViewNames("*");
//        return viewResolver;
//    }

}
