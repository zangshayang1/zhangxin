package com.zhangxin.javacore.rbtree;

import java.util.Scanner;

/******************************
 *
 * 码炫课堂技术交流Q群：963060292
 * 主讲：smart哥
 *
 ******************************/
public class RBTreeTest {
    public static void main(String[] args) {
        //新增节点
//        insertOpt();
        //删除节点
        deleteOpt();
    }

    /**
     * 插入操作
     */
    public static void insertOpt(){
        Scanner scanner=new Scanner(System.in);
        RBTree<String,Object> rbt=new RBTree<>();
        while (true){
            System.out.println("请输入你要插入的节点:");
            String key=scanner.next();
            System.out.println();
            //这里代码最多支持3位数，3位以上的话红黑树显示太错位了，这里就不重构代码了,大家可自行重构
            if(key.length()==1){
                key="00"+key;
            }else if(key.length()==2){
                key="0"+key;
            }
            rbt.put(key, key);
            TreeOperation.show(rbt.getRoot());
        }
    }

    /**
     * 删除操作
     */
    public static void deleteOpt(){
        RBTree<String,Object> rbt=new RBTree<>();

        //测试2：包含2位数和3位数的测试代码 1 2 3 4 5 66 77 88 99 100 101
        rbt.put("001", "001");
        rbt.put("002", "002");
        rbt.put("003", "003");
        rbt.put("004", "004");
        rbt.put("005", "005");
        rbt.put("066", "066");
        rbt.put("077", "077");
        rbt.put("088", "088");
        rbt.put("099", "099");
        rbt.put("100", "100");
        rbt.put("101", "101");

        TreeOperation.show(rbt.getRoot());
        //以下开始删除
        Scanner scanner=new Scanner(System.in);
        while (true){
            System.out.println("请输入你要删除的节点:");
            String key=scanner.next();
            System.out.println();
            //这里代码最多支持3位数，3位以上的话红黑树显示太错位了，这里就不重构代码了,大家可自行重构
            if(key.length()==1){
                key="00"+key;
            }else if(key.length()==2){
                key="0"+key;
            }
            //1 2 3 88 66 77 100 5 4 101
            rbt.remove(key);
            TreeOperation.show(rbt.getRoot());
        }
    }
}
