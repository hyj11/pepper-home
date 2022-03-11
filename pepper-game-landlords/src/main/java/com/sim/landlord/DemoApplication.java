package com.sim.landlord;

import com.sim.landlord.landLordJavaFx.LoginFXML;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DemoApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
//        launch(DemoApplication.class, LoginFXML.class,new SplashScreen(), args);
        launch(DemoApplication.class, LoginFXML.class, args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        stage.setTitle("（〜^㉨^)〜斗地主");
    }


    public void relaunch(){
        Platform.runLater(() -> {
            getStage().close();
            try {
                this.stop();
                this.init();
                this.start(new Stage());
            } catch (Exception e) {
                log.error("重启失败",e);
            }
        });
    }
}
