package com.cooperative.entity;

import com.cooperative.unit.wxpay.UserWay;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity(name = "wx_user")
public class User extends BaseMode implements Serializable {

    private static final long serialVersionUID = -2093315030654251243L;

    public User() {
    }

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    /**
     * 账户余额
     */
    @Column(name = "remain_money", precision = 18, scale = 4, columnDefinition = "decimal(18,4) default '0'")
    private BigDecimal remainMoney;

    /**
     * 微信openId
     */
    @Column(name = "open_id")
    @JsonIgnore
    private String openId;

    /**
     * 用户类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_way")
    private UserWay userWay;

    /**
     * 用户信息
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info_id", referencedColumnName = "id")
    private UserInfo userInfo = new UserInfo();

    /**
     * 注册ip
     */
    @Column(name = "reg_ip")
    @JsonIgnore
    private String regIp;

    @Version
    @Column(nullable = false)
    private Integer version;
}
