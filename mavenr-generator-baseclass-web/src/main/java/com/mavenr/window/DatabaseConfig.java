package com.mavenr.window;

import com.mavenr.config.Config;
import com.mavenr.entity.BaseConfig;
import com.mavenr.entity.Table;
import com.mavenr.enums.DatabaseTypeEnum;
import com.mavenr.service.DatabaseBasic;
import com.mavenr.service.MySqlDatabase;
import com.mavenr.service.OracleDatabase;
import com.mavenr.service.OutToFile;
import com.mavenr.systemenum.DatabaseType;
import com.mavenr.util.BatchButton;
import com.mavenr.util.NodeCreateUtil;
import com.mavenr.util.WriteOutUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

/**
 * @author mavenr
 * @Classname DatabaseConfig
 * @Description 数据库表的配置
 * @Date 2022/8/17 13:45
 */
public class DatabaseConfig {

    private NodeCreateUtil nodeCreateUtil = new NodeCreateUtil();
    private BatchButton buttonUtil = new BatchButton();

    /**
     * 获取数据库表配置信息
     * @param width 页面宽
     * @param height 页面高
     * @return
     */
    public AnchorPane getDatabaseInfo(double width, double height) {
        AnchorPane ap = new AnchorPane();
        ap.setPrefWidth(width);
        ap.setPrefHeight(height);

        VBox root = new VBox();
        root.setPrefWidth(width - 20);
        root.setPrefHeight(height - 10);
        root.setLayoutX(10);
        root.setSpacing(10);
        ap.getChildren().add(root);

        // 获取配置文件中的参数
        String paramType = Config.get("databaseType");
        String paramPath = Config.get("databasePath");
        String paramPort = Config.get("databasePort");
        String paramName = Config.get("databaseName");
        String paramUser = Config.get("databaseUser");
        String paramPwd = Config.get("databasePwd");
        String paramTableName = Config.get("databaseTableName");
        String paramPackageBasePath = Config.get("packageBasePath");
        String paramEntity = Config.get("entityChooser");
        String paramVo = Config.get("voChooser");
        String paramMapper = Config.get("mapperChooser");
        String paramMapperXml = Config.get("mapperXmlChooser");
        String paramService = Config.get("serviceChooser");
        String paramServiceImpl = Config.get("serviceImplChooser");
        String paramBo = Config.get("boChooser");
        String booleanEntity = Config.get("isEntity");
        String booleanVo = Config.get("isVo");
        String booleanMapper = Config.get("isMapper");
        String booleanMapperXml = Config.get("isMapperXml");
        String booleanService = Config.get("isService");
        String booleanServiceImpl = Config.get("isServiceImpl");
        String booleanBo = Config.get("isBo");

        HBox databaseType = databaseType(paramType);
        HBox databasePath = nodeCreateUtil.createLabelAndTextField("请输入数据库地址：", paramPath, "databasePath");
        HBox databasePort = nodeCreateUtil.createLabelAndTextField("请输入端口号：", paramPort, "databasePort");
        HBox databaseName = nodeCreateUtil.createLabelAndTextField("请输入数据库名称：", paramName, "databaseName");
        HBox databaseUser = nodeCreateUtil.createLabelAndTextField("请输入数据库账号：", paramUser, "databaseUser");
        HBox databasePwd = nodeCreateUtil.createLabelAndTextField("请输入数据库密码：", paramPwd, "databasePwd");
        HBox databaseTableName = nodeCreateUtil.createLabelAndTextField("请输入表名（不写则遍历所有表；多个表名以,分隔）：", paramTableName, "databaseTableName");
        HBox packageBasePath = nodeCreateUtil.createLabelAndTextField("请输入包的基础路径：", paramPackageBasePath, "packageBasePath");
        HBox entityChooser = nodeCreateUtil.createLabelAndFileChooser("请选择entity类模板文件：", "template/Entity.java", paramEntity, booleanEntity, "entityChooser", "isEntity");
        HBox voChooser = nodeCreateUtil.createLabelAndFileChooser("请选择vo类模板文件：", "template/VO.java", paramVo, booleanVo, "voChooser", "isVo");
        HBox mapperChooser = nodeCreateUtil.createLabelAndFileChooser("请选择mapper类模板文件：", "template/Mapper.java", paramMapper, booleanMapper, "mapperChooser", "isMapper");
        HBox mapperXmlChooser = nodeCreateUtil.createLabelAndFileChooser("请选择mapperXml类模板文件：", "template/Mapper.xml", paramMapperXml, booleanMapperXml, "mapperXmlChooser", "isMapperXml");
        HBox serviceChooser = nodeCreateUtil.createLabelAndFileChooser("请选择service类模板文件：", "template/Service.java", paramService, booleanService, "serviceChooser", "isService");
        HBox serviceImplChooser = nodeCreateUtil.createLabelAndFileChooser("请选择serviceImpl类模板文件：", "template/ServiceImpl.java", paramServiceImpl, booleanServiceImpl, "serviceImplChooser", "isServiceImpl");
        HBox boChooser = nodeCreateUtil.createLabelAndFileChooser("请选择bo类模板文件：", "template/BO.java", paramBo, booleanBo, "boChooser", "isBo");
        // 处理按钮
        Button executeButton = buttonUtil.createInstance();
        TextField resultPath = new TextField();
        resultPath.setDisable(true);
        resultPath.setPrefWidth(400);
        resultPath.setStyle("-fx-background-color: transparent");
        HBox bottomHbox = new HBox();
        bottomHbox.setSpacing(10);
        bottomHbox.setAlignment(Pos.CENTER_LEFT);
        bottomHbox.getChildren().addAll(executeButton, resultPath);

        root.getChildren().addAll(databaseType,
                databasePath,
                databasePort,
                databaseName,
                databaseUser,
                databasePwd,
                databaseTableName,
                packageBasePath,
                entityChooser,
                voChooser,
                mapperChooser,
                mapperXmlChooser,
                serviceChooser,
                serviceImplChooser,
                boChooser,
                bottomHbox);

        // 处理按钮触发
        ObservableList<Node> children = root.getChildren();
        clickButton(children);

        return ap;
    }

    /**
     * 数据库类型
     * @return
     */
    private HBox databaseType(String databaseType) {
        // 标签
        Label typeLabel = new Label("选择数据库类型：");
        // 数据库类型，下拉框
        ObservableList types = FXCollections.observableArrayList(DatabaseType.ORACLE, DatabaseType.MYSQL);
        ComboBox databaseTypeBox = new ComboBox(types);
        databaseTypeBox.setStyle("-fx-background-color: #f9cb9c");
        // 设置默认为第一个
        if (DatabaseType.MYSQL.getType().equals(databaseType)) {
            databaseTypeBox.getSelectionModel().select(1);
        } else {
            databaseTypeBox.getSelectionModel().select(0);
        }
        databaseTypeBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // 获取当前选中的值
                String value = String.valueOf(databaseTypeBox.getSelectionModel().getSelectedItem());
                if (!value.equals(databaseType)) {
                    Config.set("databaseType", value);
                }
            }
        });
        // 添加到行
        HBox typeHbox = new HBox();
        typeHbox.setSpacing(10);
        typeHbox.setAlignment(Pos.CENTER_LEFT);
        typeHbox.getChildren().addAll(typeLabel, databaseTypeBox);
        return typeHbox;
    }

    /**
     * 触发处理按钮
     * @param children
     */
    private void clickButton(ObservableList<Node> children) {
        HBox bottomHbox = (HBox) children.get(children.size() - 1);
        Button executeButton = (Button) bottomHbox.getChildren().get(0);
        executeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // 数据库类型
                HBox databaseTypeHbox = (HBox) children.get(0);
                ComboBox dtCb = ((ComboBox)databaseTypeHbox.getChildren().get(1));
                String type = String.valueOf(dtCb.getSelectionModel().getSelectedItem());
                System.out.println("选取的数据类型为：" + type);
                // 数据库地址
                String path = getTextFieldValue(children.get(1));
                // 端口号
                String port = getTextFieldValue(children.get(2));
                // 数据库名称
                String name = getTextFieldValue(children.get(3));
                // 数据库账号
                String user = getTextFieldValue(children.get(4));
                // 数据库密码
                String pwd = getTextFieldValue(children.get(5));
                // 表名
                String tableNames = getTextFieldValue(children.get(6));
                // 包基础路径
                String packageBasePath = getTextFieldValue(children.get(7));
                // entity模板文件路径
                String entityPath = getTemplatePath(children.get(8));
                // vo模板文件路径
                String voPath = getTemplatePath(children.get(9));
                // mapper模板文件路径
                String mapperPath = getTemplatePath(children.get(10));
                // mapperXml模板文件路径
                String mapperXmlPath = getTemplatePath(children.get(11));
                // service模板文件路径
                String servicePath = getTemplatePath(children.get(12));
                // serviceImpl模板文件路径
                String serviceImplPath = getTemplatePath(children.get(13));
                // bo模板文件路径
                String boPath = getTemplatePath(children.get(14));

                String outPath = System.getProperty("user.dir") + File.separator + "code";
                BaseConfig bc = BaseConfig.builder()
                        .type(type)
                        .path(path)
                        .port(port)
                        .name(name)
                        .user(user)
                        .pwd(pwd)
                        .tableNames(tableNames)
                        .packageBasePath(packageBasePath)
                        .entityPath(entityPath)
                        .voPath(voPath)
                        .mapperPath(mapperPath)
                        .mapperXmlPath(mapperXmlPath)
                        .servicePath(servicePath)
                        .serviceImplPath(serviceImplPath)
                        .boPath(boPath)
                        .outPath(outPath)
                        .build();

                // 获取数据库表的字段信息
                List<Table> tableList = getTableInfos(bc);
                // 根据tableBOList生成code代码并将代码导出到文件
                WriteOutUtil.write(tableList, new OutToFile(), bc);
                System.out.println("程序执行完毕，文件输出路径为：" + outPath);
                // 将输出路径弹窗显示到界面中
                Alert alert = new Alert(Alert.AlertType.INFORMATION, outPath);
                alert.setTitle("操作完成");
                alert.setHeaderText("文件输出路径如下");
                alert.showAndWait();
            }
        });
    }

    /**
     * 解析HBox中有效的值
     * @param node
     * @return
     */
    private String getTextFieldValue(Node node) {
        HBox hbox = (HBox) node;
        String value = ((TextField) hbox.getChildren().get(1)).getText();
        return value;
    }

    /**
     * 解析获取模板文件路径
     * @param node
     * @return
     */
    private String getTemplatePath(Node node) {
        HBox hbox = (HBox) node;
        CheckBox cb = (CheckBox) hbox.getChildren().get(4);
        boolean selected = cb.isSelected();
        if (selected) {
            return ((TextField) hbox.getChildren().get(1)).getText();
        } else {
            return null;
        }
    }

    /**
     * 获取表信息
     * @param baseConfig 数据库表基本配置
     * @return
     */
    private List<Table> getTableInfos(BaseConfig baseConfig) {
        // 数据库类型
        String type = baseConfig.getType();
        DatabaseBasic databaseBasic = null;
        if (DatabaseTypeEnum.ORACLE.getType().equals(type)) {
            databaseBasic = new OracleDatabase();
        } else if (DatabaseTypeEnum.MYSQL.getType().equals(type)) {
            databaseBasic = new MySqlDatabase();
        }
        databaseBasic.init(baseConfig);
        List<Table> result = null;
        try {
            result = databaseBasic.columns(baseConfig);
        } catch (Exception e) {
            System.out.println("获取表信息失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}