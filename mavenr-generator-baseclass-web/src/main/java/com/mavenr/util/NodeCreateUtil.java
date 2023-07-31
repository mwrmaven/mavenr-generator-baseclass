package com.mavenr.util;

import com.mavenr.config.Config;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;

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

        if (paramKey != null && !"".equals(paramKey.trim())) {
            // 失去焦点事件
            tf.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    // 判断tf当前的值是否与text相同
                    if (!tf.getText().equals(text)) {
                        try {
                            Config.set(paramKey, tf.getText());
                        } catch (Exception e) {
                            System.out.println("写入配置文件失败！");
                        }
                    }
                }
            });
        }

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
        tf.setDisable(true);
        tf.setPrefWidth(300);
        tf.setStyle("-fx-border-color: black; -fx-border-radius: 20; -fx-background-radius: 20");
        tf.setPromptText("模板路径");
        if (text != null && !"".equals(text.trim())) {
            tf.setText(text);
        }
        //监听输入框中值的变化
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    Config.set(textKey, newValue);
                } catch (Exception e) {
                    System.out.println("写入配置文件失败！");
                }
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
                    try {
                        FileOptions.writeToFile(in, dirPath);
                    } catch (Exception e) {
                        String msg = "文件下载失败，请联系技术人员！";
                        // 将输出路径弹窗显示到界面中
                        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                        alert.setTitle("执行出错");
                        alert.setHeaderText(msg);
                        alert.showAndWait();
                    }
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
                try {
                    Config.set(isSelectKey, String.valueOf(newValue).toUpperCase());
                } catch (Exception e) {
                    System.out.println("写入配置文件失败！");
                }
            }
        });

        TextField filter = new TextField();
        // 隐藏控件
        filter.setManaged(false);
        filter.setPrefHeight(0);
        filter.setPrefWidth(0);
        // 过滤字段
        Button filterButton = new Button("过滤字段");
        filterButton.setStyle("-fx-background-color: #ffe599;");
        filterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                buttonDialogOption(filter, filterButton, "过滤字段", "请输入要过滤的字段名，多个以英文逗号分隔");
            }
        });

        TextField append = new TextField();
        // 隐藏控件
        append.setManaged(false);
        append.setPrefHeight(0);
        append.setPrefWidth(0);
        // 类中追加文本
        Button appendButton = new Button("类末尾追加文本");
        appendButton.setStyle("-fx-background-color: #ffe599;");

        appendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                buttonDialogOption(append, appendButton, "类末尾追加文本", "请输入要追加到类末尾（注意：在结束符号}之前）的文本");
            }
        });

        // 添加到行
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(label, cb, filterButton, appendButton, downloadTemplateButton, button, tf, filter, append);
        return hbox;
    }

    private void buttonDialogOption(TextField field, Button button, String title, String promptText) {
        // 弹出一个文本输入框
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        // 添加多个按钮类型（其中一个为 ButtonType.CLOSE，或者一个确认按钮类型）
        ButtonType yes = new ButtonType("确认");
        ButtonType close = ButtonType.CLOSE;
        dialog.getDialogPane().getButtonTypes().addAll(yes, close);

        TextArea area = new TextArea();
        area.setPromptText(promptText);
        String fieldText = field.getText();
        if (fieldText != null && !"".equals(fieldText.trim())) {
            area.setText(fieldText);
        }

        AnchorPane textPane = new AnchorPane();
        textPane.getChildren().add(area);
        dialog.getDialogPane().setContent(textPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == yes) {
                return area.getText();
            }
            return null;
        });
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(field::setText);
        if (field.getText() != null && !"".equals(field.getText().trim())) {
            // 改变按钮的颜色
            button.setStyle("-fx-background-color: #87CEFA;");
        } else {
            button.setStyle("-fx-background-color: #ffe599;");
        }
    }
}
