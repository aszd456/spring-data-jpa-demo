package com.cooperative.annotation.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * @ClassName DynamicDataSource
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/8/1 9:57
 * @Version 1.0
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 数据源标识，保存在线程变量中，避免多线程操作数据源时互相干扰
     */
    private static final ThreadLocal<String> key = new ThreadLocal<String>();

    @Override
    protected Object determineCurrentLookupKey() {
        return key.get();
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        //重点
        super.afterPropertiesSet();
    }


    /**
     * 设置数据源
     *
     * @param dataSource 数据源名称
     */
    public static void setDataSource(String dataSource) {
        key.set(dataSource);
    }


    /**
     * 获取数据源
     *
     * @return
     */
    public static String getDataSource() {
        return key.get();
    }

    /**
     * 清除数据源
     */
    public static void clearDataSource() {
        key.remove();
    }
}
