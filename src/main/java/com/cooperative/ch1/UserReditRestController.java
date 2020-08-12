package com.cooperative.ch1;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注解RestController相当于注解Controller和ResponseBody的合体。
 * 试试诡异制造如下问题:
 * 1 更改getCreditLevel名字为getCreditLevelTest
 * 2 /usercredit/{id} 更改为 /usercredit/{id1}
 *
 * @author Administrator
 */
@RestController
public class UserReditRestController {
    @RequestMapping(value = "/usercredit/{id}")
    public Integer getCreditLevel(@PathVariable String id) {
        return Integer.parseInt(id);
    }
}
