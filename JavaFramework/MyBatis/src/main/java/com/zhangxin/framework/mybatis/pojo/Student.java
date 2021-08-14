package com.zhangxin.framework.mybatis.pojo;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {

    public int stuId;

    public Person person;

    public List<Person> list;

    /**
     * ArrayList@fun1:
     * @return
     */
    @Override
    public ArrayList fun1() {
        return new ArrayList();
    }
}
