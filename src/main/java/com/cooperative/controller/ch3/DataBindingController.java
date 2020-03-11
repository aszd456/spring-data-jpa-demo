package com.cooperative.controller.ch3;

import com.cooperative.controller.ch3.form.WorkInfoForm;
import com.cooperative.entity.user.User;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/databind")
public class DataBindingController {

    /**
     *  从字面意思可以看出这个的作用是给Binder做初始化的，被此注解的方法可以对WebDataBinder初始化。
     *  webDataBinder是用于表单到方法的数据绑定的！
     * @InitBinder只在@Controller中注解方法来为这个控制器注册一个绑定器初始化方法，方法只对本控制器有效。
     *
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
        binder.addValidators(new Validator() {

            @Override
            public boolean supports(Class<?> clazz) {
                return clazz == WorkInfoForm.class;
            }

            @Override
            public void validate(Object target, Errors errors) {
                WorkInfoForm form = (WorkInfoForm) target;
            }

        });
    }


    @RequestMapping("/date.json")
    @ResponseBody
    public User printDate(Date d) {
        System.out.println(d);
        return new User();
    }

    @ResponseBody
    @RequestMapping("/addworkinfo.html")
    public void addWorkInfo(@Validated({WorkInfoForm.Add.class}) WorkInfoForm workInfo, BindingResult result) {
        if (result.hasErrors()) {

            List<ObjectError> list = result.getAllErrors();

            FieldError error = (FieldError) list.get(0);
            System.out.println(error.getObjectName() + "," + error.getField() + "," + error.getDefaultMessage());
            return;
        }

        return;
    }
}
