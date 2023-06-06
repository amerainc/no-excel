package com.rainc.noexcel.annotation.process;


import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.UUID;

/**
 * @author rainc
 * @date 2023/5/9
 */
public class ErrorTranslator extends TreeTranslator {
    /**
     * 构造JCTree的工具类
     */
    private final TreeMaker treeMaker;

    /**
     * 名字处理工具类
     */
    private final Names names;

    /**
     * 编译期的日志打印工具类
     */
    private final Messager messager;
    /**
     * sb的定义
     */
    private JCTree.JCFieldAccess stringBuilder;

    public ErrorTranslator(TreeMaker treeMaker, Names names, Messager messager, TreePath treePath) {
        this.treeMaker = treeMaker;
        this.names = names;
        this.messager = messager;
        JCTree.JCIdent ident = treeMaker.Ident(names.fromString("java.lang"));
        stringBuilder = treeMaker.Select(ident, names.fromString("StringBuilder"));
    }

    /**
     * 访问到类定义时的处理
     *
     * @param jcClassDecl 类定义的抽象语法树节点
     */
    @Override
    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
        super.visitClassDef(jcClassDecl);
        //实现ErrMsg类
        createErrField(jcClassDecl);
        implErrMsg(jcClassDecl);
        implErrMsgMethod(jcClassDecl);

    }

    private void implErrMsgMethod(JCTree.JCClassDecl jcClassDecl) {
        JCTree.JCMethodDecl hasErrMsgMethod = createHasErrMsgMethod();
        messager.printMessage(Diagnostic.Kind.NOTE, "createHasErrMsgMethod");
        jcClassDecl.defs = jcClassDecl.defs.append(hasErrMsgMethod);
        messager.printMessage(Diagnostic.Kind.NOTE, "createHasNotErrMsgMethod");
        JCTree.JCMethodDecl hasNotErrMsgMethod = createHasNotErrMsgMethod();
        jcClassDecl.defs = jcClassDecl.defs.append(hasNotErrMsgMethod);
        messager.printMessage(Diagnostic.Kind.NOTE, "createAppendMethod");
        JCTree.JCMethodDecl appendMethod = createAppendMethod();
        jcClassDecl.defs = jcClassDecl.defs.append(appendMethod);
        messager.printMessage(Diagnostic.Kind.NOTE, "createSetErrMsgMethod");
        jcClassDecl.defs=jcClassDecl.defs.append(createSetErrMsgMethod());
        messager.printMessage(Diagnostic.Kind.NOTE, "createGetErrMsgMethod");
        jcClassDecl.defs=jcClassDecl.defs.append(createGetErrMsgMethod());
    }

    private JCTree.JCMethodDecl createSetErrMsgMethod() {
        JCTree.JCVariableDecl param = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), names.fromString("errMsg"),stringBuilder, null);
          return treeMaker.MethodDef(
                // 访问修饰符
                treeMaker.Modifiers(Flags.PUBLIC),
                // 方法名
                names.fromString("setErrMsg"),
                // 方法返回类型
               treeMaker.TypeIdent(TypeTag.VOID),
                // 泛型参数
                List.nil(),
                // 方法参数列表
                List.of(param),
                // throw表达式
                List.nil(),
                // 方法体
                treeMaker.Block(0,List.nil()),
                // 默认值
                null
        );
    }

    private JCTree.JCMethodDecl createGetErrMsgMethod() {
        JCTree.JCFieldAccess errMsg = treeMaker.Select(treeMaker.Ident(names.fromString("this")), names.fromString("errMsg"));
        JCTree.JCReturn ret = treeMaker.Return(errMsg);
        JCTree.JCBlock methodBody = treeMaker.Block(0L, List.of(ret));
        return treeMaker.MethodDef(
                // 访问修饰符
                treeMaker.Modifiers(Flags.PUBLIC),
                // 方法名
                names.fromString("getErrMsg"),
                // 方法返回类型
                stringBuilder,
                // 泛型参数
                List.nil(),
                // 方法参数列表
                List.nil(),
                // throw表达式
                List.nil(),
                // 方法体
                methodBody,
                // 默认值
                null
        );
    }


    private JCTree.JCMethodDecl createAppendMethod() {
        JCTree.JCVariableDecl param = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), names.fromString("msg"),treeMaker.Ident(names.fromString("String")), null);
        messager.printMessage(Diagnostic.Kind.NOTE, "msg参数:" + param);
        //hasNotErrMsg()
        JCTree.JCFieldAccess hasNotErrMsgDef = treeMaker.Select(treeMaker.Ident(names.fromString("this")), names.fromString("hasNotErrMsg"));
        messager.printMessage(Diagnostic.Kind.NOTE, "hasNotErrMsg:" + hasNotErrMsgDef);
        //if的表达式
        JCTree.JCMethodInvocation hasNotErrMsgInvoke = treeMaker.Apply(List.nil(), hasNotErrMsgDef, List.nil()).setType(treeMaker.TypeIdent(TypeTag.BOOLEAN).type);
        // if为true执行内容
        // errMsg=new StringBuilder();
        messager.printMessage(Diagnostic.Kind.NOTE, "hasNotErrMsgInvoke:" + hasNotErrMsgInvoke);
        JCTree.JCFieldAccess errMsgDef = treeMaker.Select(treeMaker.Ident(names.fromString("this")), names.fromString("errMsg"));
        messager.printMessage(Diagnostic.Kind.NOTE, "errMsgDef:" + errMsgDef);
        JCTree.JCNewClass newSb = treeMaker.NewClass(null, List.nil(), stringBuilder, List.nil(), null);
        messager.printMessage(Diagnostic.Kind.NOTE, "newSb:" + newSb);
        JCTree.JCAssign assign = treeMaker.Assign(errMsgDef, newSb);
        //构建if
        JCTree.JCIf jcIf = treeMaker.If(hasNotErrMsgInvoke, treeMaker.Exec(assign), null);
        //构建接下来的内容
        //errMsg.append(msg)
        JCTree.JCFieldAccess errMsgAppend = treeMaker.Select(errMsgDef, names.fromString("append"));
        messager.printMessage(Diagnostic.Kind.NOTE, "errMsgAppend:" + errMsgAppend);
        messager.printMessage(Diagnostic.Kind.NOTE, "param.getName:" + param.getName());
        //入参类型
        List<JCTree.JCExpression> typeArgs = List.of(treeMaker.Ident(names.fromString("String")));
        //入参
        List<JCTree.JCExpression> args = List.of(treeMaker.Ident(param.getName()));
        JCTree.JCMethodInvocation appendMethod = treeMaker.Apply(typeArgs, errMsgAppend, args);
        messager.printMessage(Diagnostic.Kind.NOTE, "appendMethod:" + appendMethod);
        JCTree.JCBlock methodBody = treeMaker.Block(0, List.of(jcIf, treeMaker.Exec(appendMethod)));
        return treeMaker.MethodDef(
                // 访问修饰符
                treeMaker.Modifiers(Flags.PUBLIC),
                // 方法名
                names.fromString("append"),
                // 方法返回类型
                treeMaker.TypeIdent(TypeTag.VOID),
                // 泛型参数
                List.nil(),
                // 方法参数列表
                List.of(param),
                // throw表达式
                List.nil(),
                // 方法体
                methodBody,
                // 默认值
                null
        );
    }

    private JCTree.JCMethodDecl createHasNotErrMsgMethod() {
        JCTree.JCFieldAccess hasErrMsg = treeMaker.Select(treeMaker.Ident(names.fromString("this")), names.fromString("hasErrMsg"));
        JCTree.JCMethodInvocation appMethod = treeMaker.Apply(List.nil(), hasErrMsg, List.nil()).setType(treeMaker.TypeIdent(TypeTag.BOOLEAN).type);
        messager.printMessage(Diagnostic.Kind.NOTE, "appMethod:" + appMethod);
        JCTree.JCUnary unary = treeMaker.Unary(JCTree.Tag.NOT, appMethod);
        JCTree.JCReturn jcReturn = treeMaker.Return(unary);
        JCTree.JCBlock methodBody = treeMaker.Block(0L, List.of(jcReturn));
        return treeMaker.MethodDef(
                // 访问修饰符
                treeMaker.Modifiers(Flags.PUBLIC),
                // 方法名
                names.fromString("hasNotErrMsg"),
                // 方法返回类型
                treeMaker.TypeIdent(TypeTag.BOOLEAN),
                // 泛型参数
                List.nil(),
                // 方法参数列表
                List.nil(),
                // throw表达式
                List.nil(),
                // 方法体
                methodBody,
                // 默认值
                null
        );
    }

    private JCTree.JCMethodDecl createHasErrMsgMethod() {
        // 构造方法入参
        // 构造 this.errMsg!=null
        JCTree.JCFieldAccess errMsg = treeMaker.Select(treeMaker.Ident(names.fromString("this")), names.fromString("errMsg"));
        JCTree.JCBinary binary = treeMaker.Binary(JCTree.Tag.NE, errMsg, treeMaker.Literal(TypeTag.BOT, null));
        // 构造方法返回类型
        JCTree.JCReturn aReturn = treeMaker.Return(binary);
        // 构造方法体
        JCTree.JCBlock methodBody = treeMaker.Block(0L, List.of(aReturn));
        return treeMaker.MethodDef(
                // 访问修饰符
                treeMaker.Modifiers(Flags.PUBLIC),
                // 方法名
                names.fromString("hasErrMsg"),
                // 方法返回类型
                treeMaker.TypeIdent(TypeTag.BOOLEAN),
                // 泛型参数
                List.nil(),
                // 方法参数列表
                List.nil(),
                // throw表达式
                List.nil(),
                // 方法体
                methodBody,
                // 默认值
                null
        );
    }

    private void createErrField(JCTree.JCClassDecl jcClassDecl) {
        JCTree.JCFieldAccess excelField = treeMaker.Select(treeMaker.Ident(names.fromString("com.rainc.noexcel.annotation")), names.fromString("ExcelField"));
        treeMaker.Import(excelField,false);
        JCTree.JCIdent excelFieldAnnotation = treeMaker.Ident(names.fromString("ExcelField"));
        JCTree.JCAssign name = treeMaker.Assign(
                treeMaker.Ident(names.fromString("name")),
                treeMaker.Literal("错误信息")
        );
        JCTree.JCAssign sort = treeMaker.Assign(
                treeMaker.Ident(names.fromString("sort")),
                treeMaker.Literal(Integer.MAX_VALUE)
        );
        JCTree.JCVariableDecl def = treeMaker.VarDef(treeMaker.Modifiers(Flags.PRIVATE),
                names.fromString("errMsg"),
                stringBuilder,
                null
        );
        def.mods.annotations=List.of(treeMaker.Annotation(excelFieldAnnotation, List.of(name, sort)));
        jcClassDecl.defs=jcClassDecl.defs.prepend(def);
    }

    private void implErrMsg(JCTree.JCClassDecl jcClassDecl) {
        Name className = names.fromString("ErrMsg");
        JCTree.JCIdent jcIdent = treeMaker.Ident(names.fromString("com.rainc.noexcel.meta"));
        JCTree.JCFieldAccess fieldAccess = treeMaker.Select(jcIdent, className);
        messager.printMessage(Diagnostic.Kind.NOTE, "jcClassDecl.implementing:" + jcClassDecl.implementing);
        if (jcClassDecl.implementing != null) {
            jcClassDecl.implementing = jcClassDecl.implementing.append(fieldAccess);
        } else {
            jcClassDecl.implementing = List.of(fieldAccess);
        }
        messager.printMessage(Diagnostic.Kind.NOTE, "jcClassDecl.append:" + jcClassDecl.implementing);
    }
}
