package com.zhangxin.framework.mybatis.pojo;

import java.util.ArrayList;
import java.util.List;

public class Person {

    public int id;

    public String name;

    public Integer age;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return "name";
    }

    public void setName(String name) {
        System.out.println("name = " + name);
    }


    /**
     * List#fun1:
     * @return
     */
    public List fun1(){
        return new ArrayList();
    }
}
