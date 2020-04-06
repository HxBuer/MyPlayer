package com.example.myplayer.utils;

import android.app.Application;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2020/4/6
 * TODO:实现Qzone热修复
 * 步骤：
 *      1. 获取当前应用的PathClassLoader；
 *      2. 反射获取DexPathList属性对象pathList;
 *      3. 反射修改pathList的dexElements
 *          3.1 把补丁包path.dex转换为Elemt[ ] (patch)
 *          3.2 获得pathList的dexElements属性(old)
 *          3.3 patch+old合并，并反射赋值给pathList的dexElements
 */
public class Hotfix {

    public static void installPatch(Application application , File patch){
        if(!patch.exists()){
            return;
        }
        //1. 获取当前应用的PathClassLoader；
        ClassLoader classLoader = application.getClassLoader();
        try {
            //2. 反射获取DexPathList属性对象pathList;
            Field pathListField = ShareReflectUtils.findField(classLoader, "pathList");
            Object pathList = pathListField.get(classLoader);

            /*
                 3.1 把补丁包path.dex转换为Elemt[] (patch)
                     第一个参数：dex文件路径的List
                     第二个参数：odex文件输出目录
                     第三个参数：IOException异常对象的List
             */
            Method makePathElementsMethod = ShareReflectUtils.findMethod(pathList, "makePathElements",List.class,File.class,List.class);
            ArrayList<Object> patchs = new ArrayList<>();
            patchs.add(patch);
            ArrayList<IOException> suppressedExceptions = new ArrayList<>();
            Object[] patchElements = (Object[]) makePathElementsMethod.invoke(null, patchs, application.getCacheDir(), suppressedExceptions);

            /*
               3.2 获得pathList的dexElements属性(old)
             */
            Field dexElementsField = ShareReflectUtils.findField(pathList, "dexElements");
            Object[] dexElements = (Object[]) dexElementsField.get(pathList);
            /*
              3.3 patch+old合并，并反射赋值给pathList的dexElements
             */
            //创建新的Element数组
            Object[] newElements = (Object[]) Array.newInstance(patchElements.getClass().getComponentType(), patchElements.length + dexElements.length);
            System.arraycopy(patchElements,0,newElements,0,patchElements.length);
            System.arraycopy(dexElements,0,newElements,patchElements.length,dexElements.length);
            //反射赋值
            dexElementsField.set(pathList,newElements);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
