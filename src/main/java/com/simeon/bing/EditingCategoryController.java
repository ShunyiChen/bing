package com.simeon.bing;

import com.simeon.bing.model.Category;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.Callback;

import java.util.UUID;

public class EditingCategoryController {

    @FXML
    private ListView<String> categoriesListView;

    @FXML
    private void initialize() {
        categoriesListView.setEditable(true);
//        categoriesListView.setCellFactory(TextFieldListCell.forListView());
        categoriesListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                ListCell<String> cell = new ListCell<String>() {
                    @Override public void commitEdit(String newValue) {
                        System.out.println(newValue);
                    }
//                    @Override
//                    protected void updateItem(String t, boolean bln) {
//                        super.updateItem(t, bln);
//                        if (t != null) {
//                            System.out.println(t);
//                            setText(t);
//                        }
//                    }
                };

                return cell;
            }
        });
    }

    @FXML
    protected void onNewCategoryAction() {
        categoriesListView.getItems().add("新分类");
    }

    @FXML
    protected void onRemoveCategoryAction() {
        String category = categoriesListView.getSelectionModel().getSelectedItem();
        if(category != null) {
            categoriesListView.getItems().remove(category);
        }
    }
}
