package com.cooperative.controller.ch3;

import com.cooperative.controller.ch3.form.WorkInfoForm;
import com.cooperative.entity.user.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("thymeleaf")
public class ThymeleafController {

    @RequestMapping("/hello/{id}")
    public String helloThymeleaf(Model model, @PathVariable("id") Long id) {
        model.addAttribute("hello", "hello Thymeleaf！");
        String msg = "<h1>我是h1</h1>";
        model.addAttribute("msg", msg);
        model.addAttribute("a", 1);
        model.addAttribute("b", 2);
        User user = new User();
        user.setName("test");
        model.addAttribute("user", user);

        List<User> userList = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            User user2 = new User();
            user2.setId(i);
            user2.setName("jack" + i);
            userList.add(user);
        }
        model.addAttribute("userList", userList);

        Map<String, User> userMap = Maps.newHashMap();
        for (int i = 0; i < 10; i++) {
            User user3 = new User();
            user3.setId(i);
            user3.setName("jack" + i);
            userMap.put(String.valueOf(i), user);
        }
        model.addAttribute("userMap", userMap);

        model.addAttribute("thText", "th:text 设置文本内容 <b>加粗</b>");
        model.addAttribute("thUText", "th:utext 设置文本内容 <b>加粗</b>");
        model.addAttribute("thValue", "thValue 设置当前元素的value值");
        model.addAttribute("thEach", Arrays.asList("th:each", "遍历列表"));
        model.addAttribute("thIf", "msg is not null");


        model.addAttribute("itdragonStr", "itdragonBlog");
        model.addAttribute("itdragonBool", true);
        model.addAttribute("itdragonArray", new Integer[]{1, 2, 3, 4});
        model.addAttribute("itdragonList", Arrays.asList(1, 3, 2, 4, 0));
        Map itdragonMap = new HashMap();
        itdragonMap.put("thName", "${#...}");
        itdragonMap.put("desc", "变量表达式内置方法");
        model.addAttribute("itdragonMap", itdragonMap);
        model.addAttribute("itdragonDate", new Date());
        model.addAttribute("itdragonNum", 888.888D);

        model.addAttribute("flag",true);
        return "thymeleaf/index";
    }
/**
 @RequestMapping(value = "", method = RequestMethod.PUT, params = "", headers = "", consumes = "application/json")
 public String requestMap(Model model) {
 model.addAttribute("requestMap", "d");
 return "thymeleaf/index";
 }
 @GetMapping
 @PostMapping
 @PutMapping
 @DeleteMapping
 @PatchMapping
 **/

    /**
     * 方法参数
     * @PathVariable
     * Model
     * ModelAndView
     * JavaBean
     * MultipartFile
     * @ModelAttribute
     * WebRequest
     * InputStream,Reader
     * OutputStream,Writer
     * HttpMethod
     * @MatrixVariable
     * @RequestParam
     * ....
     */

    /**
     * JavaBean
     */
    public String getUser(@RequestParam(name = "id", required = true) Integer id) {
        return null;
    }

    /**
     * 等价
     */
    public String getUser2(User user) {
        return null;
    }

    /**
     * @RequestBody 接受JSON
     */
    @ResponseBody
    public String json(@RequestBody User user) {
        return null;
    }

    /**
     * 文件上传
     */
    public String handleFormUpload(String name, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
        }
        return "success";
    }

    /**
     * @ModelAttribute
     */
    @ModelAttribute
    public void findUserById(@PathVariable Long id, Model model) {
        User user = new User();
        model.addAttribute("user", user);
    }

    /**
     * mvc使用@Validated
     */
    @RequestMapping("/addWorkInfo")
    @ResponseBody
    public void addWorkInfo(@Validated({WorkInfoForm.Add.class}) WorkInfoForm workInfoForm, BindingResult bindingResult) {
        return;
    }

    /**
     * Redirect Forward
     */
    @RequestMapping(value = "saveOrder")
    public String saveOrder() {
        return "redirect:/order/index";
    }

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        Vector<Integer> vector = new Vector<Integer>();
        long start = System.currentTimeMillis();
        for(int i=0;i<100000;i++)
            list.add(i);
        long end = System.currentTimeMillis();
        System.out.println("ArrayList进行100000次插入操作耗时："+(end-start)+"ms");
        start = System.currentTimeMillis();
        for(int i=0;i<100000;i++)
            vector.add(i);
        end = System.currentTimeMillis();
        System.out.println("Vector进行100000次插入操作耗时："+(end-start)+"ms");
    }
}
