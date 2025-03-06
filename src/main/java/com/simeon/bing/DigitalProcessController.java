package com.simeon.bing;

import cn.hutool.core.bean.BeanUtil;
import com.simeon.bing.model.BingFiles;
import com.simeon.bing.model.PatientRecord;
import com.simeon.bing.model.SysDictData;
import com.simeon.bing.model.SysDictType;
import com.simeon.bing.request.BatchAddReq;
import com.simeon.bing.response.*;
import com.simeon.bing.utils.HttpUtil;
import com.simeon.bing.utils.JsonUtil;
import com.simeon.bing.utils.StringUtils;
import com.simeon.bing.utils.enums.FileState;
import com.simeon.bing.utils.enums.PatientRecordState;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.js.JsObject;
import com.teamdev.jxbrowser.js.internal.JsMapImpl;
import com.teamdev.jxbrowser.js.internal.JsObjectImpl;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.Encoder;
import org.apache.commons.text.StringEscapeUtils;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;

@Setter
@Getter
public class DigitalProcessController {
    private static final double ZOOM_FACTOR = 1.1; // 缩放因子
    private Stage stage;
    private TreeItem<SysDictData> rootNode = new TreeItem<>(new SysDictData("材料列表"));
    private Browser jxbrowser;
    private double mouseX, mouseY;
    private boolean remainOriginSize;
    private double scale = 0d;
    private GetRecordRes res;

    @FXML
    protected GridPane gridPane;
    @FXML
    protected TextField medicalRecordNumberTxt;
    @FXML
    protected TextField patientInfoTxt;
    @FXML
    protected ComboBox<SysDictType> caseClassificationTxt;
    @FXML
    protected TreeView<SysDictData> caseClassificationTree;
    @FXML
    protected Button btnFetch;
    @FXML
    protected BorderPane webView;
    @FXML
    protected Button btnOpenCamera;
    @FXML
    protected Button btnCloseCamera;
    @FXML
    protected Button btnTakePhoto;
    @FXML
    protected ImageView bigImageView;
    @FXML
    protected ScrollPane imageViewScrollPane;
    @FXML
    protected Label labelPX;
    @FXML
    protected Label labelFileSize;
    @FXML
    protected Button btnZoomInOut;

    @FXML
    private void initialize() {
        // 为TreeView设置鼠标点击事件
        caseClassificationTree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                TreeItem<SysDictData> selected = caseClassificationTree.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    if(selected.getValue().isFile()) {
                        File f = new File(selected.getValue().getFilePath());
                        Image image = new Image(f.toURI().toString());
                        bigImageView.setImage(image);

                        // 获取图片的宽度和高度
                        int width = (int) image.getWidth();
                        int height = (int) image.getHeight();

                        labelPX.setText("图片分辨率: " + width + "x" + height);

                        long fileSize = f.length();

                        // 转换为 KB 或 MB
                        if (fileSize < 1024) {
                            labelFileSize.setText("图片文件大小: " + fileSize + " 字节");
                        } else if (fileSize < 1024 * 1024) {
                            double sizeKB = fileSize / 1024.0;
                            labelFileSize.setText("图片文件大小: " + String.format("%.2f", sizeKB) + " KB");
                        } else {
                            double sizeMB = fileSize / (1024.0 * 1024.0);
                            labelFileSize.setText("图片文件大小: " + String.format("%.2f", sizeMB) + " MB");
                        }
                    }
                }
            }
        });
        SysDictType d1 = new SysDictType(12L,"1.住院病案", "bing_admission case", "0");
        SysDictType d2 = new SysDictType(18L,"2.门诊病案", "bing_outpatient_case", "0");
        SysDictType d3 = new SysDictType(19L,"3.急诊病案", "bing_emergency case", "0");
        caseClassificationTxt.getItems().addAll(d1, d2, d3);

        rootNode.setExpanded(true); // 展开根节点
        caseClassificationTree.setRoot(rootNode);

        FontIcon icon = new FontIcon("mdomz-search");
        icon.setIconSize(12);
        btnFetch.setGraphic(icon);

        // 初始化浏览器
        Engine engine = Engine.newInstance(
                EngineOptions.newBuilder(HARDWARE_ACCELERATED).build());
        jxbrowser = engine.newBrowser();
        BrowserView view = BrowserView.newInstance(jxbrowser);
        webView.setCenter(view);

        // 监听ScrollPane的宽度和高度变化，动态设置ImageView的初始尺寸
        imageViewScrollPane.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            bigImageView.setFitWidth(newVal.getWidth());
            bigImageView.setFitHeight(newVal.getHeight());
        });
        // 设置ImageView的尺寸与图片保持一致
        bigImageView.setPreserveRatio(true);

        // 添加鼠标事件处理程序以实现拖拽
        bigImageView.setOnMousePressed(this::handleMousePressed);
        bigImageView.setOnMouseDragged(this::handleMouseDragged);
        // 添加滚轮事件处理程序以实现缩放
        bigImageView.setOnScroll(this::handleScroll);

        FontIcon cropFreeIcon = new FontIcon("mdal-crop_free");
        cropFreeIcon.setIconSize(14);
        btnZoomInOut.setGraphic(cropFreeIcon);
    }

    private void handleMousePressed(MouseEvent event) {
        // 记录鼠标按下时的位置
        mouseX = event.getX();
        mouseY = event.getY();
    }

    private void handleMouseDragged(MouseEvent event) {
        // 计算鼠标移动的距离
        double deltaX = event.getX() - mouseX;
        double deltaY = event.getY() - mouseY;
        // 获取ImageView的当前位置
        ImageView imageView = (ImageView) event.getSource();
        double newX = imageView.getTranslateX() + deltaX;
        double newY = imageView.getTranslateY() + deltaY;
        // 更新ImageView的位置
        imageView.setTranslateX(newX);
        imageView.setTranslateY(newY);
    }

    private void handleScroll(ScrollEvent event) {
        // 获取ImageView
        ImageView imageView = (ImageView) event.getSource();
        // 获取当前缩放比例
        scale = imageView.getScaleX();
        // 根据滚轮方向调整缩放比例
        if (event.getDeltaY() > 0) {
            // 向上滚动，放大图片
            scale *= ZOOM_FACTOR;
        } else {
            // 向下滚动，缩小图片
            scale /= ZOOM_FACTOR;
        }
        // 设置新的缩放比例
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);
        // 阻止事件继续传播
        event.consume();
    }

    @FXML
    protected void handleZoomInOut() {
        if (remainOriginSize) {
            // 显示当前ScrollPane尺寸
            FontIcon cropFreeIcon = new FontIcon("mdal-crop_free");
            cropFreeIcon.setIconSize(14);
            btnZoomInOut.setGraphic(cropFreeIcon);

            bigImageView.setScaleX(1);
            bigImageView.setScaleY(1);
            bigImageView.setPreserveRatio(true); // 保持比例
        } else {
            // 显示原图尺寸
            FontIcon zoomOutIcon = new FontIcon("mdomz-zoom_out_map");
            zoomOutIcon.setIconSize(14);
            btnZoomInOut.setGraphic(zoomOutIcon);

            bigImageView.setScaleX(scale);
            bigImageView.setScaleY(scale);
            bigImageView.setPreserveRatio(true); // 保持比例
        }
        remainOriginSize = !remainOriginSize;
    }

    @FXML
    protected void handleFetch() {
        String medicalRecordNumber = medicalRecordNumberTxt.getText();
        try {
            String response = HttpUtil.sendGetRequest(APIs.GET_RECORD + medicalRecordNumber, TokenStore.getToken());
            res = JsonUtil.fromJson(response, GetRecordRes.class);
            if(res.getData() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("提示");
                alert.setHeaderText("病案号"+medicalRecordNumber+"不存在");
                alert.setContentText("");
                alert.show();
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String info = String.format("病案号:%s  入院日期:%s  姓名:%s  就诊次数:%d", medicalRecordNumber, dateFormat.format(res.getData().getAdmissionDate()), res.getData().getPatientName(), res.getData().getHospitalizationCount());
                patientInfoTxt.setText(info);
                caseClassificationTxt.setValue(null);
                caseClassificationTree.getRoot().getChildren().clear();
                btnOpenCamera.setDisable(true);
                btnCloseCamera.setDisable(true);
                btnTakePhoto.setDisable(true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 选择病案分类下拉框
     */
    @FXML
    protected void handleChangeClassification() {
        if(StringUtils.isEmpty(patientInfoTxt.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(stage);
            alert.setTitle("提示");
            alert.setHeaderText("请先提取一个病案号");
            alert.setContentText("");
            alert.show();
            return;
        }
        // 清空当前显示的材料图片
        bigImageView.setImage(null);

        //关闭Camera
        handleCloseCamera();

        // 选择病案分类
        SysDictType d = caseClassificationTxt.getSelectionModel().getSelectedItem();
        if(d != null) {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("pageNum", "1");
            queryParams.put("pageSize", "10");
            queryParams.put("dictType", d.getDictType());
            String response;
            try {
                // 取已上传的文件列表
                BingFiles bingFiles = new BingFiles();
                bingFiles.setRecordId(res.getData().getId());
                bingFiles.setClassificationName(d.getDictType());
                String jsonInputString = JsonUtil.toJson(bingFiles);
                String json = HttpUtil.sendPostRequest(APIs.GET_FILES, jsonInputString, TokenStore.getToken());
                List<BingFiles> list = JsonUtil.fromJsonToList(json, BingFiles.class);

                // 取材料分类列表
                response = HttpUtil.sendGetRequest(APIs.GET_DICT_DATA, queryParams, TokenStore.getToken());
                GetDictDataRes res = JsonUtil.fromJson(response, GetDictDataRes.class);
                rootNode.getChildren().clear();
                for(SysDictData data : res.getRows()) {
                    TreeItem<SysDictData> item = new TreeItem<>();
                    item.setValue(data);
                    rootNode.getChildren().add(item);

                    // 加载文件列表
                    List<BingFiles> filteredList =  list.stream().filter(e -> e.getClassificationName().equals(data.getDictLabel())).toList();
                    for(BingFiles f : filteredList) {
                        TreeItem<SysDictData> t = new TreeItem<>();
                        SysDictData page = new SysDictData();
                        page.setFileId(f.getId());
                        page.setFile(true);
                        if(f.getStatus().equals(FileState.UPLOADED.getState())) {
                            page.setFilePath(Settings.REMOTE_STORAGE_PATH + f.getFilePath());
                        } else {
                            page.setFilePath(Settings.LOCAL_STORAGE_PATH + f.getFilePath());
                        }
                        page.setDictLabel(f.getFileName());
                        t.setValue(page);
                        item.getChildren().add(t);
                        item.setExpanded(true);
                    }
                }
                jxbrowser.navigation().loadUrl("file:///"+Settings.CAPTURE_PLUGIN);

                btnOpenCamera.setDisable(false);
                btnCloseCamera.setDisable(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("----------");
        }
    }

    @FXML
    protected void handleOpenCamera() {
        btnOpenCamera.setDisable(true);
        btnCloseCamera.setDisable(false);
        btnTakePhoto.setDisable(false);

        jxbrowser.mainFrame().ifPresent(frame -> {
            CountDownLatch latch1 = new CountDownLatch(1);
            CountDownLatch latch2 = new CountDownLatch(1);
            Thread thread1 = new Thread(() -> {
                frame.executeJavaScript("connect_cmd_svc()" + ";");
                frame.executeJavaScript("connect_mc_svc()" + ";");
                try {
                    Thread.sleep(2000); // 使线程 1 暂停 2 秒（2000 毫秒）
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 如果线程被中断，设置中断状态
                    System.out.println("线程 1 被中断");
                }
                latch1.countDown(); // 完成后释放第一个 Latch
            });
            Thread thread2 = new Thread(() -> {
                try {
                    latch1.await(); // 等待线程 1 完成
                    frame.executeJavaScript("CZUR_ID_Initialize()" + ";");
                    latch2.countDown(); // 完成后释放第二个 Latch
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            Thread thread3 = new Thread(() -> {
                try {
                    latch2.await(); // 等待线程 2 完成
                    frame.executeJavaScript("CZUR_ID_OpenDevice()" + ";");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            thread1.start();
            thread2.start();
            thread3.start();
        });
    }

    @FXML
    protected void handleCloseCamera() {
        btnOpenCamera.setDisable(false);
        btnCloseCamera.setDisable(true);
        btnTakePhoto.setDisable(true);
        jxbrowser.mainFrame().ifPresent(frame -> {
            frame.executeJavaScript("CZUR_ID_CloseDevice()" + ";");
            frame.executeJavaScript("CZUR_ID_Deintialize()" + ";");
        });
    }

    @FXML
    protected void handleTakePhoto() {
        TreeItem<SysDictData> item = caseClassificationTree.getSelectionModel().getSelectedItem();
        if(item == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(stage);
            alert.setTitle("提示");
            alert.setHeaderText("请选择材料分类");
            alert.setContentText("");
            alert.show();
        }
        else if(item.getValue().isFile()) {
            // 更新操作
            // 显示确认对话框
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("提示");
            alert.initOwner(stage);
            alert.setHeaderText("您确定要覆盖原有文件吗？");
            alert.setContentText("");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // 更新处理
                TreeItem<SysDictData> finalItem = item;
                // 清空原有材料文件
                bigImageView.setImage(null);
                File f = new File(finalItem.getValue().getFilePath());
                if(f.exists()) {
                    f.delete();
                }

                jxbrowser.mainFrame().ifPresent(frame -> {
                    String classificationName = finalItem.getValue().getDictLabel();
                    File dir = new File(Settings.LOCAL_STORAGE_PATH +File.separator+ res.getData().getMedicalRecordNumber() +File.separator+ classificationName);
                    if(!dir.exists()) {
                        dir.mkdirs();
                    }
                    finalItem.getValue().setFileStatus(FileState.WAITING_UPLOAD.getState());

                    updateNewFile(finalItem.getValue().getFileId());
                    updatePatientRecord(res.getData());

                    execute(finalItem.getValue().getFilePath(), frame);
                });
            }
        } else {
            // 新增处理
            TreeItem<SysDictData> finalItem = item;
            jxbrowser.mainFrame().ifPresent(frame -> {
                TreeItem<SysDictData> subItem = new TreeItem<>();
                SysDictData newTreeNode = new SysDictData();
                String classificationName = finalItem.getValue().getDictLabel();
                File dir = new File(Settings.LOCAL_STORAGE_PATH +File.separator+ res.getData().getMedicalRecordNumber() +File.separator+ classificationName);
                if(!dir.exists()) {
                    dir.mkdirs();
                }
                int num = finalItem.getChildren().size();
                String fileName = "第"+(num + 1)+"页";
                String filePath = dir.getPath() + File.separator+fileName+".jpg";

                newTreeNode.setDictLabel(fileName);
                newTreeNode.setFilePath(filePath);
                newTreeNode.setFile(true);

                subItem.setValue(newTreeNode);
                finalItem.getChildren().add(subItem);
                finalItem.setExpanded(true);

                SysDictType type = caseClassificationTxt.getSelectionModel().getSelectedItem();
                if(type != null) {
                    // 获取病案分类
                    String recordClassificationName = type.getDictType();
                    String relativePath = File.separator+ res.getData().getMedicalRecordNumber() +File.separator+ classificationName+ File.separator+fileName+".jpg";
                    long id = insertNewFile(res.getData().getId(), recordClassificationName, classificationName, fileName, relativePath, FileState.WAITING_UPLOAD.getState());
                    newTreeNode.setFileId(id);
                    updatePatientRecord(res.getData());

                    execute(filePath, frame);
                }

            });
        }
    }

    private void execute(String filePath, Frame frame) {
        String newFilePath = filePath.replace("\\", "/");
        frame.executeJavaScript("SET_PHOTO_PATH('"+newFilePath+"');");
        frame.executeJavaScript("CZUR_ID_GrabImage();", new Consumer<>() {
            @Override
            public void accept(Object o) {
                try {
                    displayPicture(filePath);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Consumer<Object> andThen(Consumer<? super Object> after) {
                return Consumer.super.andThen(after);
            }
        });
    }

    private Long insertNewFile(long recordId, String recordClassificationName, String classificationName, String fileName, String filePath, String status) {
        BingFiles file = new BingFiles();
        file.setRecordId(recordId);
        file.setRecordClassificationName(recordClassificationName);
        file.setClassificationName(classificationName);
        file.setFileName(fileName);
        file.setFilePath(filePath);
        file.setStatus(status);
        file.setCreateBy(UserInfoStore.getUserName());
        file.setUpdateBy(UserInfoStore.getUserName());
        String jsonInputString; // 将对象转换为JSON字符串
        try {
            jsonInputString = JsonUtil.toJson(file);
            String response = HttpUtil.sendPostRequest(APIs.ADD_FILE, jsonInputString, TokenStore.getToken());
            AddFileRes res = JsonUtil.fromJson(response, AddFileRes.class);
            if(res.getCode() == 200) {
                return res.getId();
            } else {
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.initOwner(stage);
                alert.setTitle("提示");
                alert.setHeaderText("插入材料失败");
                alert.setContentText("");
                alert.show();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0L;
    }

    private void updateNewFile(Long fileId) {
        BingFiles bf = new BingFiles();
        bf.setId(fileId);
        bf.setStatus(FileState.WAITING_UPLOAD.getState());
        bf.setUpdateBy(UserInfoStore.getUserName());
        String jsonInputString = "";
        try {
            jsonInputString = JsonUtil.toJson(bf);
            String response = HttpUtil.sendPostRequest(APIs.UPDATE_FILE, jsonInputString, TokenStore.getToken());
            Response res = JsonUtil.fromJson(response, Response.class);
            if(res.getCode() == 500) {
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.initOwner(stage);
                alert.setTitle("提示");
                alert.setHeaderText("更新材料状态失败");
                alert.setContentText("");
                alert.show();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updatePatientRecord(PatientRecord r) {
        try {
            PatientRecord record = new PatientRecord();
            BeanUtil.copyProperties(r, record);
            record.setStatus(PatientRecordState.MODIFIED.getState());
            record.setUpdateBy(UserInfoStore.getUserName());
            String jsonInputString = JsonUtil.toJson(record);
            String response = HttpUtil.sendPostRequest(APIs.UPDATE_RECORDS, jsonInputString, TokenStore.getToken());
            UpdateRecordRes res = JsonUtil.fromJson(response, UpdateRecordRes.class);
            if(res.getCode() == 500) {
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.initOwner(stage);
                alert.setTitle("提示");
                alert.setHeaderText("更新病案状态失败");
                alert.setContentText("");
                alert.show();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void displayPicture(String path) {
        // 创建一个定时任务调度器
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // 定义要执行的任务
        Runnable task = new Runnable() {
            private int count = 0; // 计数器

            @Override
            public void run() {
                if (count < 3) { // 执行三次
                    if(new File(path).exists()) {
                        Image image = new Image(new File(path).toURI().toString());
                        bigImageView.setImage(image);
                        scheduler.shutdown();
                    }
                    System.out.println("执行任务: " + (count + 1));
                    count++;
                } else {
                    scheduler.shutdown(); // 任务完成后关闭调度器
                }
            }
        };

        // 调度初始任务，定时执行
        int initialDelay = 0; // 第一次执行前的延迟
        int period = 1000; // 隔一秒执行一次

        // scheduleWithFixedDelay 可以实现间隔执行
        scheduler.scheduleWithFixedDelay(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }
}
