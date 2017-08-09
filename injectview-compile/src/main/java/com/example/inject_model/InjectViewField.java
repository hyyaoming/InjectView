package com.example.inject_model;

import com.example.BindView;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author yaoming.li
 * @since 2017-08-08 15:52
 */
public class InjectViewField {
    private VariableElement mElement;
    private int mRes;

    public InjectViewField(Element element) throws IllegalArgumentException {
        if (element.getKind() != element.getKind()) {
            throw new IllegalArgumentException("res is not only");
        }
        mElement = (VariableElement) element;
        BindView bindView = mElement.getAnnotation(BindView.class);
        mRes = bindView.value();
        if (mRes < 0) {
            throw new IllegalArgumentException("value() value is error");
        }
    }

    public int getInjectViewRes() {
        return mRes;
    }

    public Name getInjectViewFiledName() {
        return mElement.getSimpleName();
    }

    public TypeMirror getInjectViewType() {
        return mElement.asType();
    }


}
