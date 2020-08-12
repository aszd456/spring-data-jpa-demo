package com.cooperative.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;

@Entity
@Table(name = "user_info")
public class UserInfo extends BaseMode implements Serializable {
    private static final long serialVersionUID = 8740980080305753457L;

    public UserInfo() {
    }

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    @JsonIgnore
    private String realName;

    /**
     * 电话号码
     */
    @Column
    @JsonIgnore
    private String mobile;

    /**
     * 电子邮箱
     */
    @Column
    @JsonIgnore
    private String email;

    /**
     * QQ号码
     */
    @Column
    private String qq;

    /**
     * 联系地址
     */
    @Column
    @JsonIgnore
    private String address;

    /**
     * 邮政编码
     */
    @Column
    private String postcode;

    /**
     * 身份证
     */
    @Column(name = "id_card")
    @JsonIgnore
    private String idCard;

//    @OneToOne(mappedBy = "userInfo")
//    private User user;

    /**
     * 版本号,用于实现乐观锁
     */
    @Version
    @Column(nullable = false)
    private Integer version;
}
