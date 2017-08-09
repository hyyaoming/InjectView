package com.example.inject_model;

import com.example.BindLayout;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * 获取布局注入的Id
 *
 * @author yaoming.li
 * @since 2017-08-08 16:09
 */
public class InjectLayoutClass {

    private int mLayoutId;

    public InjectLayoutClass(Element element) throws IllegalArgumentException{
        if (element.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException("this annotation must use type");
        }
        TypeElement typeElement = (TypeElement) element;
        mLayoutId = typeElement.getAnnotation(BindLayout.class).value();
        if(mLayoutId < 0){
            throw new IllegalArgumentException("resources id is error");
        }
    }

    public int getLayoutId(){
        return mLayoutId;
    }

}
