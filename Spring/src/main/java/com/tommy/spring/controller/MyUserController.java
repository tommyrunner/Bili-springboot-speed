package com.tommy.spring.controller;

import com.tommy.spring.entity.SqlMyUser;
import com.tommy.spring.service.MyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Tommy
 * 2021/4/6
 */
//@Controller
//@ResponseBody //将java对象转为json格式的数据。
@RestController
@Slf4j
public class MyUserController {

    @Autowired
    MyUserService myUserService;

    //查询所有用户
    @GetMapping("/getSqlMyUserAll")
    public List<SqlMyUser> getSqlMyUserAll(){
        log.info("------访问了查询所有用户-----");
        log.debug("------测试时候:访问了查询所有用户-----");
       return myUserService.findMyUserAll();
    }
    //根据用户名查询用户
    @GetMapping("/getSqlMyUser")
    public SqlMyUser getSqlMyUser(String user){
        return myUserService.findMyUserByUser(user);
    }
    //添加
    @PostMapping("/saveMyUser")
    public SqlMyUser saveMyUser(@RequestBody  SqlMyUser sqlMyUser){
       return myUserService.saveSqlMyUser(sqlMyUser);
    }
    //删除
    @PostMapping("/deleteMyUser")
    public String deleteMyUser(int id){
        myUserService.deleteByIdMyUser(id);
        return "删除成功!";
    }
}
