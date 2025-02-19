package com.simeon.bing;

//7.22
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;


//6.24.3
//import com.teamdev.jxbrowser.chromium.Browser;
//import com.teamdev.jxbrowser.chromium.BrowserContext;
//import com.teamdev.jxbrowser.chromium.BrowserContextParams;
//import com.teamdev.jxbrowser.chromium.BrowserType;
//import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import java.util.UUID;

public class DPController {

    @FXML
    private BorderPane webView;

    @FXML
    private TextField urlBar = new TextField();

    @FXML
    private void initialize() {
        initWebView();
    }

    private void initWebView() {
//        7.22
        Engine engine = Engine.newInstance(
                EngineOptions.newBuilder(HARDWARE_ACCELERATED).build());
        Browser browser = engine.newBrowser();
        BrowserView view = BrowserView.newInstance(browser);

        urlBar.setText("file:///J:/zhangtao/bing/drivers/CZURPlugin_1.0.23.0926_Setup/test.htm");
        urlBar.setOnAction(e -> {
            browser.navigation().loadUrl(urlBar.getText());
        });
        webView.setCenter(view);

//        6.24.3
//        BrowserContext context = new BrowserContext(new BrowserContextParams("/tmp/" + UUID.randomUUID().toString()));
//        Browser browser = new Browser(BrowserType.LIGHTWEIGHT, context);
//        BrowserView browserView = new BrowserView(browser);
//        urlBar.setText("file:///J:/zhangtao/bing/drivers/CZURPlugin_1.0.23.0926_Setup/test.htm");
//        urlBar.setOnAction(e -> {
//            browser.loadURL(urlBar.getText());
//        });
//        webView.setCenter(browserView);
    }
}
