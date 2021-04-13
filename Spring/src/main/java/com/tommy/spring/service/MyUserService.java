package com.tommy.spring.service;

import com.tommy.spring.dao.SqlMyUserDao;
import com.tommy.spring.entity.SqlMyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Tommy
 * 2021/4/11
 */
@Service
public class MyUserService {
    @Autowired
    SqlMyUserDao sqlMyUserDao;
    //查询
    public List<SqlMyUser> findMyUserAll(){
       return sqlMyUserDao.findAll();
    }
    //根据用户名查询
    public SqlMyUser findMyUserByUser(String user){
        return sqlMyUserDao.findByUser(user);
    }
    //删除
    public void deleteByIdMyUser(Integer id){
        sqlMyUserDao.deleteById(id);
    }
    //添加/修改
    public SqlMyUser saveSqlMyUser(SqlMyUser sqlMyUser){
        return sqlMyUserDao.save(sqlMyUser);
    }
}
