package com.simeon.bing;

import com.simeon.bing.dao.SubCategoryDAO;
import com.simeon.bing.dao.UserDAO;
import com.simeon.bing.model.Category;
import com.simeon.bing.model.SubCategory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;


public class EditingSubCategoryController {

    @FXML
    private TextField tfSubCategoryName;
    @FXML
    private CheckBox cbRFE;

    private Stage stage;
    private TableView<SubCategory> subCategoryTableView;
    private Category category;
    private SubCategory subCategory;

    @Setter
    private String mode = "insert";

    public void init(TableView<SubCategory> subCategoryTableView, Stage stage, Category category) {
        this.subCategoryTableView = subCategoryTableView;
        this.stage = stage;
        this.category = category;
    }

    public void willEdit(Stage stage,TableView<SubCategory> subCategoryTableView, SubCategory subCategory) {
        this.stage = stage;
        this.subCategory = subCategory;
        this.subCategoryTableView = subCategoryTableView;
        tfSubCategoryName.setText(subCategory.getName());
        cbRFE.setSelected(subCategory.getRFE());
    }

    public void cleanFields() {
        tfSubCategoryName.setText("");
        cbRFE.setSelected(false);
    }

    @FXML
    protected void onOkAction() {
        if(StringUtils.isEmpty(tfSubCategoryName.getText().trim())) {
            Alert alert = new Alert (Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText("分类名称不能为空");
            alert.setContentText("");
            alert.show();
        } else {
            // insert
            if(mode.equals("insert")) {
                if(addExistCheck(tfSubCategoryName.getText().trim())) {
                    Alert alert = new Alert (Alert.AlertType.WARNING);
                    alert.setTitle("提示");
                    alert.setHeaderText("分类名已存在");
                    alert.setContentText("");
                    alert.show();
                } else {
                    handleAdd();
                    stage.close();
                }
            }
            // update
            else {
                if(updateExistCheck(tfSubCategoryName.getText().trim(), subCategory.getName())) {
                    Alert alert = new Alert (Alert.AlertType.WARNING);
                    alert.setTitle("提示");
                    alert.setHeaderText("分类名已存在");
                    alert.setContentText("");
                    alert.show();
                } else {
                    handleUpdate();
                    stage.close();
                }
            }
        }
    }

    @FXML
    protected void onCancelAction() {
        stage.close();
    }

    private boolean addExistCheck(String name) {
        try {
            return SubCategoryDAO.hasExist(name, null);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean updateExistCheck(String name, String oldName) {
        try {
            return SubCategoryDAO.hasExist(name, oldName);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAdd() {
        try {
            SubCategory subCategory = new SubCategory();
            subCategory.setName(tfSubCategoryName.getText().trim());
            subCategory.setRFE(cbRFE.isSelected());
            subCategory.setCategoryId(category.getId());
            subCategory.setShortcut(null);

            Long id = SubCategoryDAO.insertSubCategory(subCategory);
            subCategory.setId(id);
            subCategoryTableView.getItems().add(subCategory);
            subCategoryTableView.refresh();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleUpdate() {
        try {
            subCategory.setName(tfSubCategoryName.getText().trim());
            subCategory.setRFE(cbRFE.isSelected());
            subCategory.setCategoryId(subCategory.getCategoryId());
            SubCategoryDAO.updateSubCategory(subCategory);
            subCategoryTableView.refresh();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
