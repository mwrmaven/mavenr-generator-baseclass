package com.mavenr.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;

/**
 * @author mavenr
 * @Classname NodeCreateUtil
 * @Description 创建节点的工具类
 * @Date 2022/8/17 15:19
 */
public class NodeCreateUtil {

    public HBox createLabelAndTextField(String labelName) {
        // 标签
        Label label = new Label(labelName);
        // 输入框
        TextField tf = new TextField();
        tf.setPrefWidth(200);

        // 添加到行
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(label, tf);
        return hbox;
    }

    /**
     * 生成标签、文本框、下载按钮、选择按钮、复选框
     * @param labelName 标签名称
     * @param filePath resources下文件的路径
     * @return
     */
    public HBox createLabelAndFileChooser(String labelName, String filePath) {
        // 标签
        Label label = new Label(labelName);
        // 输入框
        TextField tf = new TextField();
        tf.setPrefWidth(400);
        tf.setDisable(true);
        tf.setPromptText("不勾选则不生成数据；不选择，则按默认格式生成数据");

        // 下载模板文件
        Button downloadTemplateButton = new Button("下载模板文件");
        downloadTemplateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setTitle("选择下载文件的存储路径");
                File file = dc.showDialog(new Stage());
                if (file != null) {
                    // 将文件存储到指定路径下
                    String dirPath = file.getPath();
                    // 获取配置文件
                    InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream(filePath);
                }
            }
        });

        // 按钮
        Button button = new Button("选择文件");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                File file = fc.showOpenDialog(new Stage());
                if (file != null) {
                    tf.setText(file.getPath());
                }
            }
        });
        // 复选框
        CheckBox cb = new CheckBox();
        cb.setSelected(true);
        // 添加到行
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(label, tf, downloadTemplateButton, button, cb);
        return hbox;
    }
}
