package com.cooperative.ch1;


import java.lang.annotation.*;

/**
 * @Documented 注解表明这个注解应该被 javadoc工具记录. 默认情况下,javadoc是不包括注解的.
 * 但如果声明注解时指定了 @Documented,则它会被 javadoc 之类的工具处理,
 * 所以注解类型信息也会被包括在生成的文档中，是一个标记注解，没有成员
 *
 * @Retention({RetentionPolicy.Runtime}) 注解
 * RetentionPolicy这个枚举类型的常量描述保留注解的各种策略，它们与元注解(@Retention)一起指定注释要保留多长时间
 *
 *          SOURCE, 注解只在源代码级别保留，编译时被忽略
 *
 *          CLASS, 注解将被编译器在类文件中记录，但在运行时不需要JVM保留。这是默认的行为.
 *
 *          RUNTIME,注解将被编译器记录在类文件中,在运行时保留VM，因此可以反读。
 *
 * @Target({ElementType.TYPE}) 注解
 *
 * ElementType 这个枚举类型的常量提供了一个简单的分类：
 * 注解可能出现在Java程序中的语法位置（这些常量与元注解类型(@Target)一起指定在何处写入注解的合法位置）
 *
 * @author zhouliansheng
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CustomFunction {
    String value() default "";
}
/**
enum ElementType {
    /** 类, 接口 (包括注解类型), 或 枚举 声明 *
    TYPE,

    /** 字段声明（包括枚举常量） *
    FIELD,

    /** 方法声明(Method declaration) *
    METHOD,

    /** 正式的参数声明 *
    PARAMETER,

    /** 构造函数声明 *
    CONSTRUCTOR,

    /** 局部变量声明 *
    LOCAL_VARIABLE,

    /** 注解类型声明 *
    ANNOTATION_TYPE,

    /** 包声明 *
    PACKAGE,

    /**
     * 类型参数声明
     *
     * @since 1.8
     *
    TYPE_PARAMETER,

    /**
     * 使用的类型
     *
     * @since 1.8
     *
    TYPE_USE
}
**/