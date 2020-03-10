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
