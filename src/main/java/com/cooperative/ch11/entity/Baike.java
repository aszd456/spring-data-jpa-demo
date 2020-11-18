package com.cooperative.ch11.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: zhouliansheng
 * @Date: 2020/11/19 0:42
 */
@Data
public class Baike {
    private String id;
    private String desc;
    private List<String> tag = new ArrayList<String>();
    private Comment comment = null;
    private Date crateDate =null;
    private Date updateDate = null;
    private int status = 0;
}
