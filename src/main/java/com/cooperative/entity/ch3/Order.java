package com.cooperative.entity.ch3;

import com.cooperative.entity.BaseMode;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "order_table")
public class Order extends BaseMode implements Serializable {
    private static final long serialVersionUID = -2518790664843401857L;

    @Column
    private String name;
}
