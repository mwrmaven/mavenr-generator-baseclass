package com.mavenr.window;

import com.mavenr.config.Config;
import com.mavenr.entity.BaseConfig;
import com.mavenr.entity.Table;
import com.mavenr.entity.TemplateInfo;
import com.mavenr.service.impl.*;
import com.mavenr.systemenum.DatabaseTypeEnum;
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
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
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
     *
     * @param width  页面宽
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
        String paramMapper = Config.get("mapperChooser");
        String paramMapperXml = Config.get("mapperXmlChooser");
        String paramService = Config.get("serviceChooser");
        String paramServiceImpl = Config.get("serviceImplChooser");
        String booleanEntity = Config.get("isEntity");
        String booleanMapper = Config.get("isMapper");
        String booleanMapperXml = Config.get("isMapperXml");
        String booleanService = Config.get("isService");
        String booleanServiceImpl = Config.get("isServiceImpl");

        double threeWidthInRow = (width - 40) / 3;
        HBox databaseType = databaseType(paramType);
        databaseType.setPrefWidth(threeWidthInRow);
        HBox databasePath = nodeCreateUtil.createLabelAndTextField("请输入数据库地址：", paramPath, "databasePath");
        databasePath.setPrefWidth(threeWidthInRow);
        HBox databasePort = nodeCreateUtil.createLabelAndTextField("请输入端口号：", paramPort, "databasePort");
        databasePort.setPrefWidth(threeWidthInRow);

        HBox databaseName = nodeCreateUtil.createLabelAndTextField("请输入数据库名称：", paramName, "databaseName");
        databaseName.setPrefWidth(threeWidthInRow);
        HBox databaseUser = nodeCreateUtil.createLabelAndTextField("请输入数据库账号：", paramUser, "databaseUser");
        databaseUser.setPrefWidth(threeWidthInRow);
        HBox databasePwd = nodeCreateUtil.createLabelAndTextField("请输入数据库密码：", paramPwd, "databasePwd");
        databasePwd.setPrefWidth(threeWidthInRow);

        HBox databaseTableName = nodeCreateUtil.createLabelAndTextField("请输入表名（不写则遍历所有表；多个表名以,分隔）：", paramTableName, "databaseTableName");
        HBox packageBasePath = nodeCreateUtil.createLabelAndTextField("请输入包的基础路径：", paramPackageBasePath, "packageBasePath");
        HBox tipH = new HBox();
        tipH.setAlignment(Pos.CENTER);
        Label tip = new Label("——————注意：不勾选，则不生成数据；不选择文件，则按默认模板文件的格式生成数据!——————");
        tipH.getChildren().add(tip);


        HBox classEdit = new HBox();
        classEdit.setSpacing(10);
        classEdit.setAlignment(Pos.CENTER_LEFT);
        HBox beforeClassName = nodeCreateUtil.createLabelAndTextField("类名前追加：", "", "");
        HBox afterClassName = nodeCreateUtil.createLabelAndTextField("类名后追加：", "", "");
        Label example = new Label("例如VO类的类名，追加后为：{类名前}ClassName{类名后}VO。如果不添加则不会追加内容。");
        classEdit.getChildren().addAll(beforeClassName, afterClassName, example);

        HBox entityChooser = nodeCreateUtil.createLabelAndFileChooser("请选择entity/VO/BO类模板文件：", "template/Entity.java",
                paramEntity, booleanEntity, "entityChooser", "isEntity", true, true);
        HBox entityPackageField = nodeCreateUtil.createLabelAndTextField("包路径：", "", "");

        entityChooser.getChildren().add(entityPackageField);
        HBox mapperChooser = nodeCreateUtil.createLabelAndFileChooser("请选择mapper类模板文件：", "template/Mapper.java",
                paramMapper, booleanMapper, "mapperChooser", "isMapper", false, false);
        HBox mapperPackageField = nodeCreateUtil.createLabelAndTextField("包路径：", "", "");
        mapperChooser.getChildren().add(mapperPackageField);
        HBox mapperXmlChooser = nodeCreateUtil.createLabelAndFileChooser("请选择mapperXml类模板文件：", "template/Mapper.xml",
                paramMapperXml, booleanMapperXml, "mapperXmlChooser", "isMapperXml", false, false);
        HBox serviceChooser = nodeCreateUtil.createLabelAndFileChooser("请选择service类模板文件：", "template/Service.java",
                paramService, booleanService, "serviceChooser", "isService", false, false);
        HBox servicePackageField = nodeCreateUtil.createLabelAndTextField("包路径：", "", "");
        serviceChooser.getChildren().add(servicePackageField);
        HBox serviceImplChooser = nodeCreateUtil.createLabelAndFileChooser("请选择serviceImpl类模板文件：", "template/ServiceImpl.java",
                paramServiceImpl, booleanServiceImpl, "serviceImplChooser", "isServiceImpl", false, false);
        HBox serviceImplPackageField = nodeCreateUtil.createLabelAndTextField("包路径：", "", "");
        serviceImplChooser.getChildren().add(serviceImplPackageField);

        // 处理包基础路径变化
        changePackageBasePath(packageBasePath, entityPackageField, mapperPackageField, servicePackageField, serviceImplPackageField);
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

        VBox left = new VBox();
        left.setSpacing(10);
        left.getChildren().addAll(databaseType,
                databasePath,
                databasePort,
                databaseName,
                databaseUser,
                databasePwd,
                databaseTableName,
                packageBasePath);

        Text tips = new Text("模板文件中可以使用的替代符号以及含义如下：");
        HBox mergeHbox = new HBox();
        mergeHbox.setSpacing(50);
        mergeHbox.getChildren().addAll(left, tips);

        root.getChildren().addAll(mergeHbox,
                tipH,
                classEdit,
                entityChooser,
                mapperChooser,
                mapperXmlChooser,
                serviceChooser,
                serviceImplChooser,
                bottomHbox);

        // 处理按钮触发
        ObservableList<Node> children = root.getChildren();
        clickButton(children);

        return ap;
    }

    /**
     * 监听包基础路径的修改，同步修改
     *
     * @param packageBase
     * @param entityPackage
     * @param mapperPackage
     * @param servicePackage
     * @param serviceImplPackage
     */
    private void changePackageBasePath(HBox packageBase, HBox entityPackage, HBox mapperPackage, HBox servicePackage, HBox serviceImplPackage) {
        TextField base = (TextField) packageBase.getChildren().get(1);
        TextField entity = (TextField) entityPackage.getChildren().get(1);
        TextField mapper = (TextField) mapperPackage.getChildren().get(1);
        TextField service = (TextField) servicePackage.getChildren().get(1);
        TextField serviceImpl = (TextField) serviceImplPackage.getChildren().get(1);

        // 监听包基础路径文本框中的文本变化
        base.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // 失去焦点触发
                if (!newValue) {
                    String basePath = base.getText();
                    // 自动填写类所在包的路径
                    entity.setText(basePath + ".entity");
                    mapper.setText(basePath + ".mapper");
                    service.setText(basePath + ".service");
                    serviceImpl.setText(basePath + ".service.impl");
                }
            }
        });
    }

    /**
     * 数据库类型
     *
     * @return
     */
    private HBox databaseType(String databaseType) {
        // 标签
        Label typeLabel = new Label("选择数据库类型：");
        // 数据库类型，下拉框
        ObservableList types = FXCollections.observableArrayList(DatabaseTypeEnum.ORACLE, DatabaseTypeEnum.MYSQL, DatabaseTypeEnum.DMDB);
        ComboBox databaseTypeBox = new ComboBox(types);
        databaseTypeBox.setStyle("-fx-background-color: #f9cb9c");
        // 设置默认为第一个
        if (DatabaseTypeEnum.MYSQL.getType().equals(databaseType)) {
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
                    try {
                        Config.set("databaseType", value);
                    } catch (Exception e) {
                        System.out.println("数据库类型写入配置文件失败！");
                    }
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
     *
     * @param children
     */
    private void clickButton(ObservableList<Node> children) {
        // 批量处理按钮
        HBox bottomHbox = (HBox) children.get(children.size() - 1);
        Button executeButton = (Button) bottomHbox.getChildren().get(0);
        executeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    // 获取第一个合并行
                    HBox mergeHbox = (HBox) children.get(0);
                    // 获取数据库相关信息列
                    ObservableList<Node> databaseInfos = ((VBox) mergeHbox.getChildren().get(0)).getChildren();
                    // 数据库类型
                    HBox databaseTypeHbox = (HBox) databaseInfos.get(0);
                    ComboBox dtCb = ((ComboBox) databaseTypeHbox.getChildren().get(1));
                    String type = String.valueOf(dtCb.getSelectionModel().getSelectedItem());
                    System.out.println("选取的数据类型为：" + type);
                    // 数据库地址
                    String path = getTextFieldValue(databaseInfos.get(1));
                    // 端口号
                    String port = getTextFieldValue(databaseInfos.get(2));
                    // 数据库名称
                    String name = getTextFieldValue(databaseInfos.get(3));
                    // 数据库账号
                    String user = getTextFieldValue(databaseInfos.get(4));
                    // 数据库密码
                    String pwd = getTextFieldValue(databaseInfos.get(5));
                    // 表名
                    String tableNames = getTextFieldValue(databaseInfos.get(6));

                    // 类名前和类名后追加的文本
                    ObservableList<Node> classEditChildren = ((HBox) children.get(2)).getChildren();
                    String before = getTextFieldValue(classEditChildren.get(0));
                    String after = getTextFieldValue(classEditChildren.get(1));
                    // 解析entity模板控件行的所有信息
                    TemplateInfo entityTemplateInfo = formatTemplateInfo(children.get(3));
                    // 不可为空
                    if (entityTemplateInfo.isSelected() &&
                            (entityTemplateInfo.getPackagePath() == null || "".equals(entityTemplateInfo.getPackagePath().trim()))) {
                        throw new Exception("entity的包路径不可为空");
                    }
                    // mapper模板文件路径
                    TemplateInfo mapperTemplateInfo = formatTemplateInfo(children.get(4));
                    // 不可为空
                    if (mapperTemplateInfo.isSelected() &&
                            (mapperTemplateInfo.getPackagePath() == null || "".equals(mapperTemplateInfo.getPackagePath().trim()))) {
                        throw new Exception("mapper的包路径不可为空");
                    }
                    // mapperXml模板文件路径
                    TemplateInfo mapperXmlTemplateInfo = formatTemplateInfo(children.get(5));
                    // service模板文件路径
                    TemplateInfo serviceTemplateInfo = formatTemplateInfo(children.get(6));
                    // 不可为空
                    if (serviceTemplateInfo.isSelected() &&
                            (serviceTemplateInfo.getPackagePath() == null || "".equals(serviceTemplateInfo.getPackagePath().trim()))) {
                        throw new Exception("service的包路径不可为空");
                    }
                    // serviceImpl模板文件路径
                    TemplateInfo serviceImplTemplateInfo = formatTemplateInfo(children.get(7));
                    // 不可为空
                    if (serviceImplTemplateInfo.isSelected() &&
                            (serviceImplTemplateInfo.getPackagePath() == null || "".equals(serviceImplTemplateInfo.getPackagePath().trim()))) {
                        throw new Exception("serviceImpl的包路径不可为空");
                    }

                    String outPath = System.getProperty("user.dir") + File.separator + "code";
                    BaseConfig bc = BaseConfig.builder()
                            .type(type)
                            .path(path)
                            .port(port)
                            .name(name)
                            .user(user)
                            .pwd(pwd)
                            .tableNames(tableNames)
                            .entity(entityTemplateInfo)
                            .mapper(mapperTemplateInfo)
                            .mapperXml(mapperXmlTemplateInfo)
                            .service(serviceTemplateInfo)
                            .serviceImpl(serviceImplTemplateInfo)
                            .outPath(outPath)
                            .classNameBefore(before)
                            .classNameAfter(after)
                            .build();

                    System.out.println(bc.toString());
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
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = "程序运行报错，请联系技术人员！";
                    // 将输出路径弹窗显示到界面中
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                    alert.setTitle("执行出错");
                    alert.setHeaderText(msg);
                    alert.showAndWait();
                }
            }
        });
    }

    /**
     * 解析HBox中有效的值
     *
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
     *
     * @param node
     * @return
     */
    private TemplateInfo formatTemplateInfo(Node node) throws Exception {
        HBox hbox = (HBox) node;
        CheckBox cb = (CheckBox) hbox.getChildren().get(1);
        // 是否选中
        boolean selected = cb.isSelected();
        // 模板文件路径
        String filePath = ((TextField) hbox.getChildren().get(6)).getText();
        // 过滤字段
        String filterStr = ((TextArea) hbox.getChildren().get(7)).getText();
        // 追加字段
        String appendStr = ((TextArea) hbox.getChildren().get(8)).getText();
        appendStr = appendStr.replaceAll("\n", System.getProperty("line.separator"));
        // 类所在的包路径
        String packagePath = "";
        if (hbox.getChildren().size() > 9) {
            packagePath = ((TextField) ((HBox) hbox.getChildren().get(9)).getChildren().get(1)).getText();
        }

        TemplateInfo info = TemplateInfo.builder()
                .selected(selected)
                .filePath(filePath)
                .filterStr(filterStr)
                .appendStr(appendStr)
                .packagePath(packagePath)
                .build();
        return info;
    }

    /**
     * 获取表信息
     *
     * @param baseConfig 数据库表基本配置
     * @return
     */
    private List<Table> getTableInfos(BaseConfig baseConfig) throws Exception {
        // 数据库类型
        String type = baseConfig.getType();
        DatabaseBasic databaseBasic = null;
        if (DatabaseTypeEnum.ORACLE.getType().equals(type)) {
            databaseBasic = new OracleDatabase();
        } else if (DatabaseTypeEnum.MYSQL.getType().equals(type)) {
            databaseBasic = new MySqlDatabase();
        } else if (DatabaseTypeEnum.DMDB.getType().equals(type)) {
            databaseBasic = new DMDatabase();
        }
        List<Table> result = new ArrayList<>();
        databaseBasic.init(baseConfig);
        result = databaseBasic.columns(baseConfig);
        return result;
    }

}