package com.sim.landlord.controller;

import com.sim.landlord.DemoApplication;
import com.sim.landlord.landLordJavaFx.LandLordsJavaFx;
import com.sim.landlord.landLordJavaFx.LoginFXML;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @Autowired
    private LandLordsJavaFx fx;


    @GetMapping("test2")
    public void test1(){
//        LandLordsJavaFx.launch(LandLordsJavaFx.class);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                DemoApplication.launch(DemoApplication.class, LoginFXML.class, new String[]{});
            }
        });


    }

}
