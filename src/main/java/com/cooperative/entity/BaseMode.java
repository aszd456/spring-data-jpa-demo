package com.cooperative.entity;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseMode {

    private Integer id;

    private Date createTime;
}
