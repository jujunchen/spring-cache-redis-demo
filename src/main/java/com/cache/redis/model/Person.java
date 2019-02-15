package com.cache.redis.model;

import java.io.Serializable;

/**
 * @author: jujun chen
 * @description:
 * @date: 2019/2/14
 */
public class Person implements Serializable{
    private Long id;

    private String name;

    private String age;

    private String sex;

    public Person() {
    }

    public Person(Long id, String name, String age, String sex) {
        this.id  = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
