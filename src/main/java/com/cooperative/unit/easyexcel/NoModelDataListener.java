package com.cooperative.unit.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellExtra;
import com.cooperative.unit.JacksonUtil;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 不创建对象的读
 *
 * @ClassName NoModelDataListener
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/8/19 14:21
 * @Version 1.0
 **/
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoModelDataListener.class);

    List<Map<Integer, String>> list = Lists.newArrayList();

    @SneakyThrows
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        LOGGER.info("解析到一条头数据:{}", JacksonUtil.objToJson(headMap));
    }

    @SneakyThrows
    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext analysisContext) {
        LOGGER.info("解析到一条数据:{}", JacksonUtil.objToJson(data));
        list.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        LOGGER.info("read {} rows", list.size());
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) {
        LOGGER.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            LOGGER.error("第{}行，第{}列解析异常,异常数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
        }
    }

    @SneakyThrows
    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        LOGGER.info("读取到了一条额外信息:{}", JacksonUtil.objToJson(extra));
        switch (extra.getType()) {
            case COMMENT:
                LOGGER.info("额外信息是批注,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(), extra.getColumnIndex(), extra.getText());
                break;
            case HYPERLINK:
                if ("Sheet1!A1".equals(extra.getText())) {
                    LOGGER.info("额外信息是超链接,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(),
                            extra.getColumnIndex(), extra.getText());
                } else if ("Sheet2!A1".equals(extra.getText())) {
                    LOGGER.info(
                            "额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{},"
                                    + "内容是:{}",
                            extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
                            extra.getLastColumnIndex(), extra.getText());
                } else {
                    LOGGER.error("Unknown hyperlink!");
                }
                break;
            case MERGE:
                LOGGER.info(
                        "额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{}",
                        extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
                        extra.getLastColumnIndex());
                break;
            default:
        }
    }

    public List<Map<Integer, String>> getRows() {
        return list;
    }
}
