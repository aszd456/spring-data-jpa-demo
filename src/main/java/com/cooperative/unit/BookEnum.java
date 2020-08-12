package com.cooperative.unit;

public enum BookEnum {

    ONE("Spring Boot 2企业应用实战.pdf", "D://学习书籍//spring//Spring Boot 2企业应用实战.pdf"),

    TWO("Spring Data JPA从入门到精通.pdf", "D://学习书籍//spring//Spring Data JPA从入门到精通.pdf"),

    THREE("SpringBoot2精髓.pdf", "D://学习书籍//spring//SpringBoot2精髓.pdf"),

    FOUR("springmybatis企业应用实战第2版.pdf", "D://学习书籍//spring//springmybatis企业应用实战第2版.pdf"),

    FIVE("MySQL必知必会.pdf", "D://学习书籍//spring//MySQL必知必会.pdf"),

    SIX("深入浅出Spring Boot 2.pdf", "D://学习书籍//spring//深入浅出Spring Boot 2.pdf");

    private String name;
    private String path;

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    BookEnum(String name, String path) {
        this.name = name;
        this.path = path;
    }
}
