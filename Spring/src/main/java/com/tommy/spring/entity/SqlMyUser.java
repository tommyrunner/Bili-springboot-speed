package com.tommy.spring.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author Tommy
 * 2021/4/11
 */
@Entity
@Table(name = "tb_user")
@Data
public class SqlMyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column
    String user;
    @Column
    String pwd;
    @Column
    String node;
}
