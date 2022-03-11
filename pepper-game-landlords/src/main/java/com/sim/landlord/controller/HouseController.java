package com.sim.landlord.controller;

import com.sim.landlord.DemoApplication;
import com.sim.landlord.cons.RedisCons;
import com.sim.landlord.landLordJavaFx.HouseFXML;
import com.sim.landlord.landLordJavaFx.LandLordFXML;
import com.sim.landlord.landLordJavaFx.LoginFXML;
import com.sim.landlord.landLordJavaFx.context.Context;
import com.sim.landlord.landLordJavaFx.context.ThreadLocalUtil;
import com.sim.landlord.service.LoginService;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: Huang Yujiao
 * @Date: 2021/10/11 16:49
 * @Desc:
 */
@FXMLController
public class HouseController implements Initializable {
    @FXML
    Label username;
    @FXML
    Label wait;
    @FXML
    private HBox box;
    @FXML
    private Button region;
    @FXML
    private Button login;

    private ResourceBundle resourceBundle;
    @Autowired
    LoginService loginService;
    @Resource
    RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //设置定时器定时刷新当前房间玩家

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Set<String> s = redisTemplate.keys(RedisCons.LANDLORDS_LOGIN_KEY + "*");
                        String names = "";
                        for (String str : s) {
                            names += str.replace(RedisCons.LANDLORDS_LOGIN_KEY, "") + " ";
                        }
                        username.setText("当前房间玩家：" + names);
                        Context context = (Context) ThreadLocalUtil.get("1");
                        if(s.size() == 1){
                            context.setNum(1);
                            ThreadLocalUtil.set("1",context);
                        }

                        if (context.getNum() == 1) {
                            login.setVisible(true);
                            wait.setVisible(false);
                        } else {
                            wait.setVisible(true);
                            login.setVisible(false);
                        }
                    }
                });

            }
        }, 100, 500);
        resourceBundle = resources;
    }

    @FXML
    public void inHouse(ActionEvent actionEvent) {

        Set<String> s = redisTemplate.keys(RedisCons.LANDLORDS_LOGIN_KEY + "*");
        for (String str : s) {
            username.setText(str);
            box.getChildren().add(username);
        }
        DemoApplication.showView(HouseFXML.class);
    }

    @FXML
    public void outHouse(ActionEvent actionEvent) {
        Context context = (Context) ThreadLocalUtil.get("1");
        loginService.unlogin(context.getName());
        Stage stage = (Stage) region.getScene().getWindow();
        stage.close();
        DemoApplication.showView(LoginFXML.class);
    }

    @FXML
    public void start(ActionEvent actionEvent) {
        Set<String> s = redisTemplate.keys(RedisCons.LANDLORDS_LOGIN_KEY + "*");
        if (s.size() > 1) {
            DemoApplication.showView(LandLordFXML.class);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "当前人数不足");
            alert.setTitle("不可以开始哦");
            alert.setHeaderText("人太少啦");
            alert.showAndWait();
        }
    }

}
