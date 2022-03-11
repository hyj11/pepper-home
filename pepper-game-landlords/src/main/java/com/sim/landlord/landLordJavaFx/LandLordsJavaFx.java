package com.sim.landlord.landLordJavaFx;

import com.sim.landlord.service.LoginService;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: Huang Yujiao
 * @Date: 2021/9/29 13:50
 * @Desc:
 */
@Component
public class LandLordsJavaFx extends Application {
    @Resource
    private LoginService loginService;

    @Override
    public void start(Stage stage) {
        Text text3 = new Text("请输入用户名");
        Text text1 = new Text("用户名");
        Text text4 = new Text("请输入房间号");
        Text text2 = new Text("房间号");
        //Creating Text Filed for email
        TextField textField1 = new TextField();
        //Creating Text Filed for password
        PasswordField textField2 = new PasswordField();
        //Creating Buttons
        Button button1 = new Button("进入房间");
        Button button2 = new Button("Clear");

        //Creating a Grid Pane
        GridPane gridPane = new GridPane();
        //Setting size for the pane
        gridPane.setMinSize(400, 200);
        //Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);
        //Arranging all the nodes in the grid
        gridPane.add(text3, 0, 0);
        gridPane.add(text1, 0, 1);
        gridPane.add(textField1, 1, 1);
        gridPane.add(text4, 0, 2);
        gridPane.add(text2, 0, 3);
        gridPane.add(textField2, 1, 3);
        gridPane.add(button1, 0, 4);
        gridPane.add(button2, 1, 4);
        //Styling nodes
        button1.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        text1.setStyle("-fx-font: normal bold 20px 'serif' ");
        text2.setStyle("-fx-font: normal bold 20px 'serif' ");
        text3.setStyle("-fx-font: normal bold 10px 'serif' ");
        text4.setStyle("-fx-font: normal bold 10px 'serif' ");
        text3.setVisible(false);
        text4.setVisible(false);
        gridPane.setStyle("-fx-background-color: BEIGE;");
        //Creating a scene object
        Scene scene = new Scene(gridPane);
        //Setting title to the Stage
        stage.setTitle("LandLords");
        //Adding scene to the stage
        stage.setScene(scene);
        //Displaying the contents of the stage
        stage.show();
        //先登录
        //登录成功关闭登录界面并调用接口传递登录人信息，进入等待界面
        //等待界面可以确认是否开始游戏
        //确认开始后发牌
        EventHandler<MouseEvent> eventHandler1 = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (StringUtils.isEmpty(textField1.getText())) {
                    text3.setVisible(true);
                } else if (StringUtils.isEmpty(textField2.getText())) {
                    text4.setVisible(true);
                } else {
                    text3.setVisible(false);
                    text4.setVisible(false);
                    loginService.login(textField2.getText(), textField1.getText());
                }
            }
        };
        button1.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler1);

        //Creating the mouse event handler
        EventHandler<MouseEvent> eventHandler2 = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                textField1.setText("");
                textField2.setText("");
            }
        };
        button2.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler2);
    }

    public void startLand() {
        launch();
    }

    public static void main(String args[]) {
        launch(args);
    }

}