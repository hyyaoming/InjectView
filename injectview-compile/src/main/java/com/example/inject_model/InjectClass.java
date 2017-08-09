package com.example.inject_model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author yaoming.li
 * @since 2017-08-08 15:38
 */
public class InjectClass {

    private TypeElement mTypeElement;
    private List<InjectViewField> mFieldList;
    private List<InjectOnClickMethodClass> mMethodClassList;
    private InjectLayoutClass mLayoutClass;
    private Elements mElements;
    private static final ClassName PARAMETER_NAME = ClassName.get("injectview.lym.org.injectview_api.finder", "Finder");
    private static final ClassName ANDROID_ONCLICK = ClassName.get("android.view", "View", "OnClickListener");
    private static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    private static final ClassName INJECT_CLASS_INTER_FACE = ClassName.get("injectview.lym.org.injectview_api", "ViewInject");


    private static final ClassName MORE_CLICK = ClassName.get("injectview.lym.org.injectview_api.internal", "MultiOnClckListener");

    public InjectClass(TypeElement typeElement, Elements elements) {
        this.mTypeElement = typeElement;
        this.mElements = elements;
        mFieldList = new ArrayList<>();
        mMethodClassList = new ArrayList<>();
    }

    public void addInjectViewFiled(InjectViewField field) {
        mFieldList.add(field);
    }

    public void addInjectOnClickMethodClass(InjectOnClickMethodClass methodClass) {
        mMethodClassList.add(methodClass);
    }

    public void setInjectLayoutClass(InjectLayoutClass layoutClass) {
        this.mLayoutClass = layoutClass;
    }

    public JavaFile generateClass() {
        MethodSpec.Builder spec = writeMethod();
        writeLayout(spec);
        writeViews(spec);
        writeViewOnClick(spec);
        TypeSpec typeSpec = wroteInjectClass(spec);
        return JavaFile.builder(getPackageName(mTypeElement), typeSpec).build();
    }

    private TypeSpec wroteInjectClass(MethodSpec.Builder spec) {
        String packageName = getPackageName(mTypeElement);
        String clazzName = getClassName(mTypeElement, packageName);
        ClassName name = ClassName.get(packageName, clazzName);
        return TypeSpec.classBuilder(name.simpleName() + "$$Inject")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(INJECT_CLASS_INTER_FACE, TypeName.get(mTypeElement.asType())))
                .addMethod(spec.build())
                .build();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    private String getPackageName(TypeElement mTypeElement) {
        return mElements.getPackageOf(mTypeElement).getQualifiedName().toString();
    }

    private MethodSpec.Builder writeMethod() {
        return MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mTypeElement.asType()), "object", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "resources")
                .addParameter(PARAMETER_NAME, "finder");
    }

    private void writeViewOnClick(MethodSpec.Builder spec) {
        if (!mMethodClassList.isEmpty()) {
            spec.addStatement("$T listener", ANDROID_ONCLICK);
        }
        for (InjectOnClickMethodClass method : mMethodClassList) {
            TypeSpec listener = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(ANDROID_ONCLICK)
                    .addMethod(MethodSpec.methodBuilder("onClick")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.VOID)
                            .addParameter(ANDROID_VIEW, "view")
                            .addStatement("object.$N()", method.getMethodName()).build()).build();
            spec.addStatement("listener = $L ", listener);

            for (int clickId : method.getOnClickIds()) {
                spec.addStatement("finder.findView(resources,$L).setOnClickListener(listener)", clickId);
            }
        }
    }

    private void writeViews(MethodSpec.Builder binder) {
        if (!mFieldList.isEmpty()) {
            for (InjectViewField field : mFieldList) {
                binder.addStatement("object.$N= ($T)(finder.findView(resources,$L))", field.getInjectViewFiledName()
                        , ClassName.get(field.getInjectViewType()), field.getInjectViewRes());
            }
        }
    }

    private void writeLayout(MethodSpec.Builder binder) {
        if (null != mLayoutClass && mLayoutClass.getLayoutId() != 0) {
            binder.addStatement("object.setContentView($L)", mLayoutClass.getLayoutId());
        }
    }

}
