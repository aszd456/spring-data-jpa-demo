package com.cooperative.controller.ch3.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = { WorkOverTimeValidator.class }) //声明用什么类实现验证
@Documented
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkOverTime {
    String message() default "加班时间过长,不能超过{max}";

    int max() default 4;

    Class<?>[] groups() default {}; //验证规则分组

    Class<? extends Payload>[] payload() default {};//验证的有效负荷
}
