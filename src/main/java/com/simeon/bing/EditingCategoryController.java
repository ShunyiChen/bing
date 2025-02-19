package com.simeon.bing;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;

public class EditingCategoryController {

    @FXML
    protected TextField categoryTextField;
    @FXML
    protected Label errMsg;

//    private TreeView<Category> categoryTreeView;
    private Stage editingCategoryStage;
    @Setter
    private String mode = "insert";

//    public void init(TreeView<Category> categoryTreeView, Stage editingCategoryStage) {
//        this.categoryTreeView = categoryTreeView;
//        this.editingCategoryStage = editingCategoryStage;
//        errMsg.setVisible(false);
//    }

//    public void willRename(Category category) {
//        categoryTextField.setText(category.getName());
//        focus();
//    }

    @FXML
    protected void onOkAction() {
//        if(!StringUtils.isEmpty(categoryTextField.getText().trim())) {
//            if(categoryTextField.getText().trim().length() > 100) {
//                showErrMsg("名称长度不能超过100个字符");
//                return;
//            }
//
//            if(mode.equals("insert")) {
//                TreeItem<Category> parent = categoryTreeView.getSelectionModel().getSelectedItem();
//                Category category = new Category();
//                category.setName(categoryTextField.getText().trim());
//                category.setPid(parent.getValue().getId());
//                try {
//                    long id = CategoryDAO.insertCategory(category);
//                    category.setId(id);
//                    TreeItem<Category> node = new TreeItem<>(category);
//                    parent.getChildren().add(node);
//                    categoryTreeView.getSelectionModel().select(node);
//                    editingCategoryStage.close();
//                } catch (SQLException | ClassNotFoundException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            else {
//                TreeItem<Category> item = categoryTreeView.getSelectionModel().getSelectedItem();
//                Platform.runLater(() ->  {
//                    item.getValue().setName(categoryTextField.getText().trim());
//                    try {
//                        CategoryDAO.updateCategory(item.getValue());
//                        categoryTreeView.refresh();
//                        editingCategoryStage.close();
//                    } catch (SQLException | ClassNotFoundException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//            }
//
//        } else {
//            showErrMsg("分类名不能为空");
//        }
    }

    private void showErrMsg(String content) {
        errMsg.setText("提示:"+content);
        errMsg.setVisible(true);
    }

    public void resetFields() {
        categoryTextField.setText("");
        errMsg.setVisible(false);
    }

    @FXML
    protected void onCancelAction() {
        resetFields();
        editingCategoryStage.close();
    }

    public void focus() {
        Platform.runLater(() -> {
            categoryTextField.requestFocus();
            categoryTextField.selectAll();
        });
    }
}
