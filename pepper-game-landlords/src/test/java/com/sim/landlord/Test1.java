package com.sim.landlord;

import com.alibaba.fastjson.JSONObject;
import org.jfree.ui.ApplicationFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Huang Yujiao
 * @Date: 2021/9/16 13:49
 * @Desc:
 */
public class Test1 extends ApplicationFrame {
    public Test1(String title) {
        super(title);
    }

    public static void main(String[] args)
    {
        List li = new ArrayList();
        li.add(1);
        li.add(2);
        List li1 = new ArrayList();
        li1.add(3);
        li1.add(2);
        System.out.println(li1.contains(2));
//        String js =  JSONObject.toJSONString("dssddf");
//        System.out.println(js);
    }
}
