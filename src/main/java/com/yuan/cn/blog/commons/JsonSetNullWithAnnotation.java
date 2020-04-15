package com.yuan.cn.blog.commons;

import com.yuan.cn.blog.annotation.JsonIgnoredNotAdminAction;

import java.lang.reflect.Field;

public class JsonSetNullWithAnnotation {

    public static void excludeFieldByAnnotation(Object obj){
        for (Field declaredField : obj.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(JsonIgnoredNotAdminAction.class)){
                declaredField.setAccessible(true);
                try {
                    declaredField.set(obj, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
