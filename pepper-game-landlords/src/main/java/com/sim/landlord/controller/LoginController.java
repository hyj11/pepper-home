package com.sim.landlord.controller;

/**
 * @Author: Huang Yujiao
 * @Date: 2021/10/11 14:31
 * @Desc:
 */

import com.sim.landlord.DemoApplication;
import com.sim.landlord.cons.RedisCons;
import com.sim.landlord.landLordJavaFx.HouseFXML;
import com.sim.landlord.landLordJavaFx.context.Context;
import com.sim.landlord.landLordJavaFx.context.ThreadLocalUtil;
import com.sim.landlord.service.LoginService;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

@FXMLController
public class LoginController implements Initializable {

    @FXML
    private TextField userName;
    @FXML
    private TextField passWord;
    @FXML
    private Button login;

    private ResourceBundle resourceBundle;
    @Autowired
    LoginService loginService;
    @Resource
    RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
    }

    @FXML
    public void btnClick(ActionEvent actionEvent) {
        userName.setText("");
        passWord.setText("");
    }

    @FXML
    public void inHouse(ActionEvent actionEvent) {
        if(StringUtils.isEmpty(passWord.getText())){
            passWord.setText("1");
        }
        Context context = new Context();
        redisTemplate.opsForValue().set(RedisCons.LANDLORDS_LOGIN_KEY + userName.getText(), passWord.getText());
        Set<String> s = redisTemplate.keys(RedisCons.LANDLORDS_LOGIN_KEY + "*");
        if(s.size()==1){
            context.setNum(1);
        }else {
            context.setNum(2);
        }
        context.setName(userName.getText());
        ThreadLocalUtil.set("1",context);
        Stage stage = (Stage) login.getScene().getWindow();
        stage.close();
        DemoApplication.showView(HouseFXML.class);
    }

}