package com.cooperative.entity.user;

import com.cooperative.entity.BaseMode;
import lombok.Data;

import java.io.Serializable;

@Data
public class User extends BaseMode implements Serializable {

    private static final long serialVersionUID = -501311792025981521L;

    private String name;

    private Integer departmentId;
}
