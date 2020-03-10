package com.cooperative.entity.user;

import com.cooperative.entity.BaseMode;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Department extends BaseMode implements Serializable {

    private static final long serialVersionUID = -2417615809602780657L;

    @Column
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "department")
    private Set<User> users = new HashSet<User>();
}
