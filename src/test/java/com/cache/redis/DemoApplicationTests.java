package com.cache.redis;

import com.cache.redis.model.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private  UserService userService;

    @Test
    public void testRedisCache(){
        Person person = new Person(1L,"陈大侠","18","男");

        System.out.println("插入用户，新增缓存");
        userService.savePerson(person);

        System.out.println("第一次获取用户，存在缓存就从缓存返回,不存在就从数据库取");
        Person dbPerson = userService.getUserById(1L);
        System.out.println(dbPerson);

        System.out.println("第二次获取用户");
        dbPerson = userService.getUserById(1L);
        System.out.println(dbPerson);

       /* System.out.println("更新用户,更新缓存");
        person.setAge("88");
        userService.savePerson(person);

        System.out.println("第二次获取用户，看缓存是否有变化");
        dbPerson = userService.getUserById(1L);
        System.out.println(dbPerson);

        System.out.println("删除用户,删除缓存");
        userService.delPerson(1L);

        System.out.println("第三次再次获取用户，看缓存是否有变化");
        dbPerson = userService.getUserById(1L);
        System.out.println(dbPerson);*/
    }


    @Test
    public void addCache(){
        Person person = new Person(1L,"陈大侠","18","男");
        System.out.println("插入用户，新增缓存");
        userService.savePerson(person);
    }

    @Test
    public void getCache(){
        Person dbPerson = userService.getUserById(1L);
        System.out.println(dbPerson);
    }

}

