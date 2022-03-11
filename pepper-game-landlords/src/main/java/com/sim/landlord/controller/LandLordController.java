package com.sim.landlord.controller;

import com.alibaba.fastjson.JSONObject;
import com.sim.landlord.cons.RedisCons;
import com.sim.landlord.landLordJavaFx.DragSelectionListView;
import com.sim.landlord.landLordJavaFx.context.Context;
import com.sim.landlord.landLordJavaFx.context.ThreadLocalUtil;
import com.sim.landlord.service.LandService;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.net.URL;
import java.util.*;

/**
 * @Author: Huang Yujiao
 * @Date: 2021/10/14 17:19
 * @Desc:
 */
@FXMLController
public class LandLordController implements Initializable {

    @FXML
    private AnchorPane pane;
    private ResourceBundle resourceBundle;
    @FXML
    private ListView view;

    @Resource
    RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Set<String> s = redisTemplate.keys(RedisCons.LANDLORDS_LOGIN_KEY + "*");
        ArrayList list = new ArrayList();
        for (String str : s) {
            list.add(str.replace(RedisCons.LANDLORDS_LOGIN_KEY, ""));
        }
        //获取每个人的牌
        Map<String, List<String>> players = LandService.deal(list);
        Context context = (Context) ThreadLocalUtil.get("1");
        List<String> board = players.get(context.getName());
        players.forEach((k, v) -> {
            redisTemplate.opsForValue().set(RedisCons.LANDLORDSKEY + k, JSONObject.toJSONString(v));
        });

//使用文本框构造牌页，缺点：不能多选
//        Stream.iterate(0, i -> i + 1).limit(board.size()).forEach(index -> {
//            DragSelectionTextArea agent = new DragSelectionTextArea();
//            agent.setLayoutX(24.0 * index);
//            agent.setLayoutY(148.0);
//            agent.setMinWidth(27);
//            agent.setMaxHeight(200);
//            agent.setText(board.get(index));
//            agent.setDisable(true);
//            pane.getChildren().add(agent);
//        });

        ObservableList<String> strList = FXCollections.observableArrayList(board);
        DragSelectionListView agent = new DragSelectionListView(strList);
        agent.setLayoutX(24.0);
        agent.setLayoutY(200.0);
        agent.setMinWidth(30*strList.size());
        agent.setMaxHeight(250);
        agent.setOrientation(Orientation.HORIZONTAL);

        //选中后该牌修改纵坐标，缺陷：只能修改一张牌
//        agent.setCellFactory(lv -> {
//            ListCell<String> cell = new ListCell<String>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//                    setText(empty ? null : item);
//                }
//            };
//            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
//                if (cell.isEmpty()) {
//                    return ;
//                }
//                cell.setLayoutY(cell.getLayoutY()+10);
//                e.consume();
//            });
//            return cell ;
//        });

        ObservableList<String> items = agent.getSelectionModel().getSelectedItems();

        pane.getChildren().addAll(agent);

        resourceBundle = resources;
    }


}
