package com.cooperative.ch4;

import com.cooperative.entity.user.User;
import com.cooperative.service.user.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/stream")
public class JacksonStreamController {

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/user/{id}.json")
    @ResponseBody
    public User showUserInfo(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return user;
    }

    @GetMapping("/readTree.json")
    public @ResponseBody
    String readTree() throws JsonProcessingException {
        String json = "{\"name\":\"lijz\",\"id\":10}";
        JsonNode node = objectMapper.readTree(json);
        String name = node.get("name").asText();
        int id = node.get("id").asInt();
        return "name:" + name + ",id:" + id;
    }

    @GetMapping("/dataBind.json")
    public @ResponseBody
    String dataBind() throws JsonProcessingException {
        String json = "{\"name\":\"lijz\",\"id\":10}";
        User user = objectMapper.readValue(json, User.class);
        String jsonStr = objectMapper.writeValueAsString(user);
        return "name:" + user.getName() + ",id:" + user.getId();
    }

    //    @JsonView(User.IdView.class)
    @RequestMapping("/id.json")
    public User queryIds() {
        return null;
    }

    public String customize() throws Exception {
        /**
         * 如果为Map类型
         * mapper.getTypeFactory().constructParametricType(Map.class,String.class,Student.class);
         * // 第二个参数是Map的key，第三个参数是Map的value
         * 如果为List类型
         * personList  =  mapper.readValue(mapper.writeValueAsString(personList),mapper.getTypeFactory().constructParametricType(List.class,Person.class));
         *
         */

        String json = "[{\"id\":2,\"name\":\"leo\"}]";
        List<User> list = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });
        return null;
    }

    @RequestMapping("/parser.html")
    public @ResponseBody
    String parser() throws JsonParseException, IOException {
        String json = "{\"name\":\"lijz\",\"id\":10}";
        JsonFactory f = objectMapper.getFactory();
        String key = null, value = null;
        JsonParser parser = f.createParser(json);
        // {
        JsonToken token = parser.nextToken();
        //"name"
        token = parser.nextToken();
        if (token == JsonToken.FIELD_NAME) {
            key = parser.getCurrentName();

        }

        token = parser.nextToken();
        //"lijz"
        value = parser.getValueAsString();
        parser.close();
        return key + "," + value;

    }

    @RequestMapping("/generator.html")
    public @ResponseBody
    String generator() throws JsonParseException, IOException {
        JsonFactory f = objectMapper.getFactory();
        //输出到stringWriter
        StringWriter sw = new StringWriter();
        JsonGenerator g = f.createGenerator(sw);
        // {
        g.writeStartObject();

        // "message", "Hello world!"
        g.writeStringField("name", "lijiazhi");
        // }
        g.writeEndObject();
        g.close();
        return sw.toString();
    }
}
