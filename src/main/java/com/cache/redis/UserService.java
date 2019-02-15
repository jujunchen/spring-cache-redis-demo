package com.cache.redis;

import com.cache.redis.model.Person;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author: jujun chen
 * @description:
 * @date: 2019/2/14
 */
@Service
@CacheConfig(cacheNames = "person")
public class UserService {

    private Person person = null;


    @Cacheable(key = "#id")
    public Person getUserById(Long id){
        //如果没走缓存，会打印下面这句话
        System.out.println("=>操作数据库，根据id获取用户信息");
        return this.person;
    }


    @CachePut(key = "#person.id")
    public Person savePerson(Person person){
        System.out.println("=>操作数据库保存用户数据");
        this.person = person;
        return this.person;
    }


    @CacheEvict(key = "#id")
    public void delPerson(Long id){
        System.out.println("=>操作数据库删除用户数据");
        person = null;
    }

}
