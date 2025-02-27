package com.simeon.bing;

import com.simeon.bing.model.CallbackParam;
import com.simeon.bing.model.TablePage;
import com.simeon.bing.request.GetRecordsReq;
import com.simeon.bing.response.GetRecordsRes;
import com.simeon.bing.utils.HttpUtil;
import com.simeon.bing.utils.JsonUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.function.UnaryOperator;

@Getter
@Setter
public class TablePageController {
    private Callback<CallbackParam, Void> searchCallBack;
    private GetRecordsReq queryParam;
    private long totalPages;
    @FXML
    private Label totalCountLabel;
    @FXML
    private Label currentPageNumLabel;
    @FXML
    private ComboBox<TablePage> pageSizeBox;
    @FXML
    private TextField pageNumField;
    @FXML
    private Label totalPageLabel;

    @FXML
    private void initialize() {
        TablePage page10 = new TablePage("30条/页", 30);
        TablePage page20 = new TablePage("40条/页", 40);
        TablePage page30 = new TablePage("50条/页", 50);
        TablePage page40 = new TablePage("100条/页", 100);
        pageSizeBox.getItems().addAll(page10, page20, page30, page40);
        pageSizeBox.getSelectionModel().selectFirst();
        pageNumField.setText("1");
        currentPageNumLabel.setText("1");
        totalCountLabel.setText("共"+0+"条");
        totalPageLabel.setText("");

        // 创建一个UnaryOperator用于过滤输入,只允许输入数字
        UnaryOperator<TextFormatter.Change> filter = change -> {
            // 允许没有输入
            if (change.getControlNewText().isEmpty()) {
                return change; // 允许空输入
            }
            // 检查输入的字符是否都是数字
            if (change.getControlNewText().matches("\\d*")) {
                return change; // 允许输入数字
            }
            return null; // 拒绝输入
        };

        // 创建一个TextFormatter并将过滤器应用于它
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        pageNumField.setTextFormatter(textFormatter);
    }

    public void updatePagination(CallbackParam callbackParam) {
        this.queryParam = callbackParam.getQueryParam();
        totalPages = (callbackParam.getTotalCount() + queryParam.getPageSize() - 1) / queryParam.getPageSize();
        queryParam.setPageNum(1);
        currentPageNumLabel.setText("1");
        totalPageLabel.setText("/"+ totalPages +"页");
        totalCountLabel.setText("共"+callbackParam.getTotalCount()+"条");
    }

    private CallbackParam handleSearch() {
        String jsonInputString;
        try {
            jsonInputString = JsonUtil.toJson(queryParam);// 将对象转换为JSON字符串
            String response = HttpUtil.sendPostRequest(APIs.GET_RECORDS, jsonInputString, TokenStore.getToken());
            GetRecordsRes res = JsonUtil.fromJson(response, GetRecordsRes.class);
            totalCountLabel.setText("共"+res.getTotal()+"条");
            totalPages = (res.getTotal() + queryParam.getPageSize() - 1) / queryParam.getPageSize();
            totalPageLabel.setText("/"+ totalPages +"页");
            return new CallbackParam(queryParam, res.getRows(), res.getTotal());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleChangePage() {
        TablePage page = pageSizeBox.getSelectionModel().getSelectedItem();
        currentPageNumLabel.setText("1");
        pageNumField.setText("1");

        int pageNum = Integer.parseInt(currentPageNumLabel.getText());
        int pageSize = page.getPageSize();
        queryParam.setPageNum(pageNum);
        queryParam.setPageSize(pageSize);
        searchCallBack.call(handleSearch());
    }

    @FXML
    public void goToTheHomepage() {
        queryParam.setPageNum(1);
        currentPageNumLabel.setText("1");
        searchCallBack.call(handleSearch());
    }

    @FXML
    protected void goToThePreviousPage() {
        if(queryParam.getPageNum() > 1) {
            queryParam.setPageNum(queryParam.getPageNum() - 1);
            currentPageNumLabel.setText(queryParam.getPageNum()+"");
            searchCallBack.call(handleSearch());
        }
    }

    @FXML
    protected void goToTheNextPage() {
        if(queryParam.getPageNum() < totalPages) {
            queryParam.setPageNum(queryParam.getPageNum() + 1);
            currentPageNumLabel.setText(queryParam.getPageNum()+"");
            searchCallBack.call(handleSearch());
        }
    }

    @FXML
    protected void goToTheLastPage() {
        queryParam.setPageNum((int) totalPages);
        currentPageNumLabel.setText(queryParam.getPageNum()+"");
        searchCallBack.call(handleSearch());
    }

    @FXML
    protected void handleGoToPage(KeyEvent keyEvent) {
        // 检查按下的键是否是 Enter 键
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if(StringUtils.isNotEmpty(pageNumField.getText())) {
                // 执行你想要的任务
                int inputNum = Integer.parseInt(pageNumField.getText());
                if(inputNum > 0 && inputNum <= totalPages) {
                    queryParam.setPageNum(inputNum);
                    currentPageNumLabel.setText(queryParam.getPageNum()+"");
                    searchCallBack.call(handleSearch());
                }
            }
        }
    }
}
