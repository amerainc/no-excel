package com.rainc.noexcel.annotation.process;

import com.google.auto.service.AutoService;
import com.rainc.noexcel.annotation.ExcelEntity;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * @author rainc
 * @date 2023/5/9
 */
@SupportedAnnotationTypes(value = {"com.rainc.noexcel.annotation.ExcelEntity"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ExcelEntityProcess extends AbstractProcessor {
    /**
     * 可将Element转换成JCTree，组成AST语法树。使用Trees.instance(processingEnv);进行初始化。
     */
    private Trees trees;

    /**
     * 构造JCTree的工具类。使用TreeMaker.instance(((JavacProcessingEnvironment) processingEnv).getContext());初始化。
     */
    private TreeMaker treeMaker;

    /**
     * 名字处理工具类。使用Names.instance(context);进行初始化。
     */
    private Names names;

    /**
     * 编译期的日志打印工具类。使用processingEnv.getMessager();进行初始化
     */
    private Messager messager;
    private Elements elementUtils;
    /**
     * init 初始化
     * @param processingEnv 环境：提供一些Javac的执行工具
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = Trees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
        this.elementUtils = processingEnv.getElementUtils();
    }
    /**
     * 具体的执行
     * @param annotations   注解集合
     * @param roundEnv      执行round环境
     * @return 执行结果
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE,"roundEnv --->" + roundEnv);
        if (!roundEnv.processingOver()) {
            // 所有@GetterAndSetter注解标注的类
            Set<? extends Element> annotated = roundEnv.getElementsAnnotatedWith(ExcelEntity.class);
            for (Element element : annotated) {
                ExcelEntity annotation = element.getAnnotation(ExcelEntity.class);
                // 获得当前遍历类的语法树
                JCTree tree = (JCTree) trees.getTree(element);
                TreePath path = trees.getPath(element);
                treeMaker.pos=tree.pos;
                // 使用translator处理
                if (annotation.needErr()){
                    tree.accept(new ErrorTranslator(treeMaker, names, messager,path));
                }
            }
        }
        // 返回true：Javac编译器会从编译期的第二阶段回到第一阶段
        return true;
    }
}
