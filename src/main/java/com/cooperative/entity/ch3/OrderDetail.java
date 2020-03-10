package com.cooperative.entity.ch3;

import com.cooperative.entity.BaseMode;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Entity
public class OrderDetail extends BaseMode implements Serializable {
    private static final long serialVersionUID = -8685796628076513810L;

    @Column
    private String name;
}
