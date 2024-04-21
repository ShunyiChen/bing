package com.simeon.bing;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;
//import javafx.scene.web.WebEngine;
//import javafx.scene.web.WebView;
////import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;
//import java.awt.*;
//import java.io.File;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.UUID;

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
