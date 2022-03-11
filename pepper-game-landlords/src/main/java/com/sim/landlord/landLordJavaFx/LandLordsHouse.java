package com.sim.landlord.landLordJavaFx;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * @Author: Huang Yujiao
 * @Date: 2021/9/30 13:34
 * @Desc:
 */
public class LandLordsHouse {
    private static boolean res;
    public static boolean display(String title,String msg){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Label label = new Label();
        label.setText(msg);
        Button btn1 = new Button("是");
        btn1.setOnMouseClicked(event -> {
            res=true;
            stage.close();
        });
        VBox vBox = new VBox();
        vBox.getChildren().addAll(label,btn1);
        //设置居中
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox,200,100);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.showAndWait();
        return res;
    }
}
