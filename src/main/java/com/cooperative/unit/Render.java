package com.cooperative.unit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public class Render<T> implements Serializable {

    private static final long serialVersionUID = -3635638020581376807L;
    private String msg;

    private T data;

    private int code;

    private Boolean error;

    private Long timestamp;

    private static Map map;


    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public Boolean getError() {
        return error;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public static <T> Render<T> fail(String message) {
        Render<T> msg = new Render<>();
        msg.msg = message;
        msg.code(ResultCode.EXCEPTION.val());
        msg.error(true);
        return msg.putTimeStamp();
    }

    public static <T> Render<T> fail(ResultCode resultCode) {
        Render<T> msg = new Render<>();
        msg.msg = resultCode.msg();
        msg.code(resultCode.val());
        msg.error(true);
        return msg.putTimeStamp();
    }

    public static <T> Render<T> ok() {
        return ok(null);
    }

    private Render<T> putTimeStamp() {
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    public static <T> Render<T> ok(T data) {
        return new Render<T>()
                .data(data)
                .putTimeStamp()
                .error(false)
                .msg(ResultCode.SUCCESS.msg())
                .code(ResultCode.SUCCESS.val());
    }

    public static <T> Render<T> ok(T data, String msg) {
        return new Render<T>()
                .data(data)
                .putTimeStamp()
                .error(false)
                .msg(msg)
                .code(ResultCode.SUCCESS.val());
    }

    public static Map okMap(Object data) {
        map = new HashMap();
        map.put("data", data);
        map.put("error", false);
        map.put("code", ResultCode.SUCCESS.val());
        map.put("msg", ResultCode.SUCCESS.msg());
        map.put("timestamp", System.currentTimeMillis());
        return map;
    }

    public static Map failMap(String msg) {
        map = new HashMap();
        map.put("data", null);
        map.put("error", true);
        map.put("code", ResultCode.EXCEPTION.val());
        map.put("msg", msg);
        map.put("timestamp", System.currentTimeMillis());
        return map;
    }


    public static Map failMap(ResultCode resultCode) {
        map = new HashMap();
        map.put("data", null);
        map.put("error", true);
        map.put("code", resultCode.val());
        map.put("msg", resultCode.msg());
        map.put("timestamp", System.currentTimeMillis());
        return map;
    }

    public Render<T> data(T data) {
        this.data = data;
        return this;
    }

    public Render<T> code(int code) {
        this.code = code;
        return this;
    }

    public Render<T> error(Boolean error) {
        this.error = error;
        return this;
    }

    public Render<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 过滤字段：指定需要序列化的字段
     */
    @JsonIgnore
    private transient Map<Class<?>, Set<String>> includes;

    /**
     * 过滤字段：指定不需要序列化的字段
     */
    @JsonIgnore
    private transient Map<Class<?>, Set<String>> excludes;

    public Render() {

    }

    public Render<T> include(Class<?> type, String... fields) {
        return include(type, Arrays.asList(fields));
    }

    public Render<T> include(Class<?> type, Collection<String> fields) {
        if (includes == null)
            includes = new HashMap<>();
        if (fields == null || fields.isEmpty()) return this;
        fields.forEach(field -> {
            if (field.contains(".")) {
                String tmp[] = field.split("[.]", 2);
                try {
                    Field field1 = type.getDeclaredField(tmp[0]);
                    if (field1 != null) {
                        include(field1.getType(), tmp[1]);
                    }
                } catch (Throwable e) {
                }
            } else {
                getStringListFromMap(includes, type).add(field);
            }
        });
        return this;
    }

    public Render<T> exclude(Class type, Collection<String> fields) {
        if (excludes == null)
            excludes = new HashMap<>();
        if (fields == null || fields.isEmpty()) return this;
        fields.forEach(field -> {
            if (field.contains(".")) {
                String tmp[] = field.split("[.]", 2);
                try {
                    Field field1 = type.getDeclaredField(tmp[0]);
                    if (field1 != null) {
                        exclude(field1.getType(), tmp[1]);
                    }
                } catch (Throwable e) {
                }
            } else {
                getStringListFromMap(excludes, type).add(field);
            }
        });
        return this;
    }

    public Render<T> exclude(Collection<String> fields) {
        if (excludes == null)
            excludes = new HashMap<>();
        if (fields == null || fields.isEmpty()) return this;
        Class type;
        if (getData() != null) type = getData().getClass();
        else return this;
        exclude(type, fields);
        return this;
    }

    public Render<T> include(Collection<String> fields) {
        if (includes == null)
            includes = new HashMap<>();
        if (fields == null || fields.isEmpty()) return this;
        Class type;
        if (getData() != null) type = getData().getClass();
        else return this;
        include(type, fields);
        return this;
    }

    public Render<T> exclude(Class type, String... fields) {
        return exclude(type, Arrays.asList(fields));
    }

    public Render<T> exclude(String... fields) {
        return exclude(Arrays.asList(fields));
    }

    public Render<T> include(String... fields) {
        return include(Arrays.asList(fields));
    }

    protected Set<String> getStringListFromMap(Map<Class<?>, Set<String>> map, Class type) {
        return map.computeIfAbsent(type, k -> new HashSet<>());
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(this);
//        return JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss");
    }

    public Map<Class<?>, Set<String>> getExcludes() {
        return excludes;
    }

    public Map<Class<?>, Set<String>> getIncludes() {
        return includes;
    }

}

