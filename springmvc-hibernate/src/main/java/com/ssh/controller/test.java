package com.ssh.controller;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class test {

    @Test
    public void test() throws Exception {
        // 1.初始化ioc容器(装对象的容器)
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");

        //2. 获取jdbcTemplate对象
/*        ServiceA sa=context.getBean("ServiceA", ServiceA.class);
        sa.aaa();*/
        //增删改
//        String sql = "insert into t_user(username,password) values(?,?)";
//        jdbcTemplate.update(sql, "lisi","lisi123");
//        String sql2 = "update user set password=? where username=?";
//        int a = jdbcTemplate.update(sql2, "123","admin");
//        String sql3 = "delete from t_user where id=3";
//        jdbcTemplate.update(sql3);

    }

    public static void main(String[] args) {
        String a = "F:\\w\\springmvc-hibernate\\target\\spring-hibernate\\statics\\uploadfiles";

    }
}