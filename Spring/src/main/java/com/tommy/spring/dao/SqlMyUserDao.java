package com.tommy.spring.dao;

import com.tommy.spring.entity.SqlMyUser;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author Tommy
 * 2021/4/11
 */
public interface SqlMyUserDao extends JpaRepository<SqlMyUser,Integer> {
    //根据用户名查询
    SqlMyUser findByUser(String user);
}
