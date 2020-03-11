package com.cooperative.entity.user;

import com.cooperative.entity.BaseMode;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.collect.Maps;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @JsonView
 * 1.使用接口来声明多个视图
 * 2.在值对象的get方法上指定视图
 * 3.在Controller的方法上指定视图
 */
@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer"})
public class User extends BaseMode implements Serializable {

    public interface IdNameView extends IdView {
    }

    private static final long serialVersionUID = -501311792025981521L;

    @JsonView(IdNameView.class)
    @Column
    private String name;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column
    private String password;

    @Column
    private BigDecimal salary;


}
