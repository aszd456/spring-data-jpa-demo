package com.cooperative.controller.ch3.form;

import com.cooperative.controller.ch3.validate.WorkOverTime;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
public class WorkInfoForm {

    public interface Update {
    }

    public interface Add {
    }

    @Null(groups = {Add.class})
    @NotNull(message = "id不能为空", groups = {Update.class})
    private Long id;

    @Size(min = 3, max = 20, message = "长度在3~20之间")
    private String name;

    @Email(message = "不是邮箱格式")
    private String email;

    @WorkOverTime(max = 5)
    Integer workTime;
}
