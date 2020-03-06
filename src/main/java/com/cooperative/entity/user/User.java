package com.cooperative.entity.user;

import com.cooperative.entity.BaseMode;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.collect.Maps;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true,value = {"id","password"})
public class User extends BaseMode implements Serializable {

    public interface IdNameView extends IdView{};

    private static final long serialVersionUID = -501311792025981521L;

    @JsonView(IdNameView.class)
    private String name;

    private Integer departmentId;

    @JsonIgnore
    private String password;

    Map<String,Object> map= Maps.newHashMap();

    @JsonAnySetter
    private void other(String key,Object value){
        map.put(key,value);
    }

    @JsonAnyGetter
    public Map<String,Object> getOtherProperties(){
        return map;
    }

}
