package com.example.myplayer.utils;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2020/4/6
 * TODO:热修复辅助类
 */
public class ShareReflectUtils {

        public static Field findField(Object object , String name) throws NoSuchFieldException {
            Class<?> cls = object.getClass();
            while(cls != null){  //或cls != Object.class
                try {
                    Field field = cls.getDeclaredField(name);
                    if(field != null){
                        Log.i(name, " -> findFiled: OK!");
                        field.setAccessible(true);
                        return field;
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                cls = cls.getSuperclass();
            }
            throw new NoSuchFieldException(object.getClass().getSimpleName() + " not found "+name);
        }

        public static Method findMethod(Object object,String name, Class<?>... parameterType) throws NoSuchMethodException {
            Class<?> cls = object.getClass();
            while(cls != null){
                try {
                    Method method = cls.getDeclaredMethod(name,parameterType);
                    if(method != null){
                        Log.i(name, " -> findMethod: OK!");
                        method.setAccessible(true);
                        return method;
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                cls = cls.getSuperclass();
            }
            throw new NoSuchMethodException(object.getClass().getSimpleName()+" not found "+name);
        }
}

