package com.sim.landlord.landLordJavaFx.context;

import com.sim.landlord.controller.HouseController;
import javafx.collections.ObservableList;
import lombok.Data;

/**
 * @Author: Huang Yujiao
 * @Date: 2021/10/12 9:08
 * @Desc:
 */
@Data
public class Context {

//    private final static Context instance = new Context();
//    public static Context getInstance() {
//        return instance;
//    }

    private String name;
    private Integer num;

    private HouseController tabRough;
    private ObservableList<Integer> pokerIndexs;
}
