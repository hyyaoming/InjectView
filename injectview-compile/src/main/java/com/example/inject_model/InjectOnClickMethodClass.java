package com.example.inject_model;

import com.example.OnClick;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;

/**
 * 获取注入点击事件信息
 *
 * @author yaoming.li
 * @since 2017-08-08 16:01
 */
public class InjectOnClickMethodClass {

    private Name mMethodName;
    private int[] mRes;

    public InjectOnClickMethodClass(Element element) throws IllegalArgumentException{
        if (element.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException("method name is different");
        }
        ExecutableElement executableElement = (ExecutableElement) element;
        mMethodName = executableElement.getSimpleName();
        mRes = executableElement.getAnnotation(OnClick.class).value();
        if (mRes == null || mRes.length == 0) {
            throw new IllegalArgumentException("An array of resources is not null");
        } else {
            for (int id : mRes) {
                if (id < 0) {
                    throw new IllegalArgumentException("resources id is error");
                }
            }
        }
    }


    public Name getMethodName() {
        return mMethodName;
    }

    public int[] getOnClickIds() {
        return mRes;
    }

}
