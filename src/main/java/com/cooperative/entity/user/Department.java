package com.cooperative.entity.user;

import com.cooperative.entity.BaseMode;
import lombok.Data;

import java.io.Serializable;

@Data
public class Department extends BaseMode implements Serializable {

    private static final long serialVersionUID = -2417615809602780657L;
    private String name;
}
