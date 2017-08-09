package com.example;

import com.example.inject_model.InjectClass;
import com.example.inject_model.InjectLayoutClass;
import com.example.inject_model.InjectOnClickMethodClass;
import com.example.inject_model.InjectViewField;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

//这个注解用来生成注解处理器所需要的文件
//生成 META-INF/services/javax.annotation.processing.Processor 文件
@AutoService(Processor.class)
public class InjectViewProcessor extends AbstractProcessor {

    private Elements mElement;
    private Filer mField;
    private Messager mMessage;
    private Map<String, InjectClass> mInjectClassMap = new HashMap<>();

    //初始化处理器，在这里可以获取到一些相应的工具类
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElement = processingEnvironment.getElementUtils();
        mField = processingEnvironment.getFiler();
        mMessage = processingEnvironment.getMessager();
    }

    //告诉当前处理器需要对哪些注解进行处理。
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(OnClick.class.getCanonicalName());
        set.add(BindView.class.getCanonicalName());
        set.add(BindLayout.class.getCanonicalName());
        return set;
    }

    //指定当前使用的java版本 一般默认为SourceVersion.latestSupported()
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mInjectClassMap.clear();
        try {
            parserInjectView(roundEnvironment);
            parserInjectOnClick(roundEnvironment);
            parseInjectLayout(roundEnvironment);
        } catch (IllegalArgumentException error) {
            mMessage.printMessage(Diagnostic.Kind.ERROR, error.getMessage());
            return true;
        }
        try {
            for (InjectClass clazz : mInjectClassMap.values()) {
                clazz.generateClass().writeTo(mField);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mMessage.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return true;
    }

    private void parseInjectLayout(RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(BindLayout.class)) {
            InjectClass injectClass = getInjectClass(element, false);
            InjectLayoutClass injectLayoutClass = new InjectLayoutClass(element);
            injectClass.setInjectLayoutClass(injectLayoutClass);
        }
    }

    private void parserInjectOnClick(RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(OnClick.class)) {
            InjectClass injectClass = getInjectClass(element, true);
            InjectOnClickMethodClass methodClass = new InjectOnClickMethodClass(element);
            injectClass.addInjectOnClickMethodClass(methodClass);
        }
    }

    private void parserInjectView(RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(BindView.class)) {
            InjectClass injectClass = getInjectClass(element, true);
            InjectViewField injectField = new InjectViewField(element);
            injectClass.addInjectViewFiled(injectField);
        }
    }

    private InjectClass getInjectClass(Element element, boolean parent) {
        TypeElement typeElement = parent ? (TypeElement) element.getEnclosingElement() : (TypeElement) element;
        String injectClassName = typeElement.getQualifiedName().toString();
        InjectClass injectClass = mInjectClassMap.get(injectClassName);
        if (injectClass == null) {
            injectClass = new InjectClass(typeElement, mElement);
            mInjectClassMap.put(injectClassName, injectClass);
        }
        return injectClass;
    }
}
