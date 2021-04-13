package com.tommy.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Tommy
 * 2021/4/6
 */
//@Controller
//@ResponseBody //将java对象转为json格式的数据。
@RestController
public class MyUser {

    @GetMapping("/hello")
    public Map<String,String> hello(){
        Map<String ,String> map = new HashMap<>();
        map.put("value1","值1");
        map.put("value2","值2");
        return map;
    }

}
