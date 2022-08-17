package com.mavenr;

import com.mavenr.config.Config;
import com.mavenr.window.DatabaseConfig;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * javafx 启动类
 */
public class App extends Application {
    /**
     * 固定配置
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        // 加载配置文件
        Config.createProperties();

        // 布局
        AnchorPane ap = new AnchorPane();
        ap.setStyle("-fx-background-color: Ivory");

        // 场景
        Scene scene = new Scene(ap);
        // 获取屏幕的宽高
        Screen screen = Screen.getPrimary();
        Rectangle2D visualBounds = screen.getVisualBounds();
        // 宽、高
        double maxWidth = visualBounds.getWidth();
        double maxHeight = visualBounds.getHeight();

        double standardWidth = maxWidth / 2;
        double standardHeight = maxHeight / 2;

        // 配置窗体
        stage.setScene(scene);
        stage.setTitle("根据数据库表生成增删改查类");
        stage.setWidth(standardWidth);
        stage.setHeight(standardHeight);
        stage.show();

        // 此时可以获取到scene的实际宽高
        double sWidth = scene.getWidth();
        double sHeight = scene.getHeight();
        DatabaseConfig dc = new DatabaseConfig();
        ap.getChildren().add(dc.getDatabaseInfo(sWidth, sHeight));
    }
}
