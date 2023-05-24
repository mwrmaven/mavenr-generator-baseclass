package com.mavenr.util;

import com.mavenr.config.Config;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author mavenr
 * @Classname NodeCreateUtil
 * @Description 创建节点的工具类
 * @Date 2022/8/17 15:19
 */
public class NodeCreateUtil {

    /**
     * 生成标签、文本
     * @param labelName 标签名称
     * @param text 文本的值
     * @param paramKey 配置文件中的key
     * @return
     */
    public HBox createLabelAndTextField(String labelName, String text, String paramKey) {
        // 标签
        Label label = new Label(labelName);
        // 输入框
        TextField tf = new TextField();
        tf.setStyle("-fx-background-color: none; -fx-border-color: #ff9900; -fx-border-radius: 20");
        tf.setPrefWidth(200);
        if (text != null && !"".equals(text.trim())) {
            tf.setText(text);
        }

        // 失去焦点事件
        tf.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // 判断tf当前的值是否与text相同
                if (!tf.getText().equals(text)) {
                    Config.set(paramKey, tf.getText());
                }
            }
        });

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
     * @param text 文本值
     * @param isSelect 是否选择
     * @param textKey 参数文件中文本值的key
     * @param isSelectKey 参数文件中是否选中的key
     * @return
     */
    public HBox createLabelAndFileChooser(String labelName, String filePath, String text, String isSelect, String textKey, String isSelectKey) {
        // 标签
        Label label = new Label(labelName);
        // 输入框
        TextField tf = new TextField();
        tf.setPrefWidth(400);
        tf.setStyle("-fx-border-color: black; -fx-border-radius: 20; -fx-background-radius: 20");
        tf.setPromptText("不勾选则不生成数据；不选择，则按默认格式生成数据");
        if (text != null && !"".equals(text.trim())) {
            tf.setText(text);
        }
        //监听输入框中值的变化
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Config.set(textKey, newValue);
            }
        });

        // 下载模板文件
        Button downloadTemplateButton = new Button("下载模板文件");
        downloadTemplateButton.setStyle("-fx-background-color: #ffe599;");
        downloadTemplateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setTitle("选择下载文件的存储路径");
                File file = dc.showDialog(new Stage());
                if (file != null) {
                    // 将文件存储到指定路径下
                    String dirPath = file.getPath();
                    String fileName = filePath.replace("template", "");
                    dirPath += fileName;
                    // 获取文件
                    InputStream in = ClassLoader.getSystemResourceAsStream(filePath);
                    // 写出文件
                    FileOptions.writeToFile(in, dirPath);
                }
            }
        });

        // 按钮
        Button button = new Button("选择文件");
        button.setStyle("-fx-background-color: #9fc5e8");
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
        if (isSelect != null && !"".equals(isSelect.trim())) {
            // 判断值是否为false
            if ("FALSE".equals(isSelect.toUpperCase())) {
                cb.setSelected(false);
            }
        }
        // 监听复选框的变化
        cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                Config.set(isSelectKey, String.valueOf(newValue).toUpperCase());
            }
        });
        // 添加到行
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(label, tf, downloadTemplateButton, button, cb);
        return hbox;
    }
}
