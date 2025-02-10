package client.view.panel;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class RankPanel extends JFXPanel {
    private WebView webview;
    private WebEngine webEngine;
    private StackPane stackPane;
    private Button refresh;

    public RankPanel() {
        // 初始化 JavaFX 组件
        Platform.setImplicitExit(false);
        Platform.runLater(() -> init());
    }

    private void init() {
        // 创建 StackPane 和 Button
        stackPane = new StackPane();
        refresh = new Button("刷新");

        // 初始化 WebView
        webview = new WebView();
        webEngine = webview.getEngine();

        // 设置按钮点击事件
        refresh.setOnAction(e -> updateWeb());

        // 将按钮放入一个 BorderPane 顶部，WebView 位于中心
        BorderPane layout = new BorderPane();
        layout.setTop(refresh);
        layout.setCenter(webview);

        // 设置场景
        Scene scene = new Scene(layout);
        this.setScene(scene);
    }

    public void changeHTML(String url) {
        Platform.runLater(() -> {
            if (webEngine != null) {
                webEngine.load(url);
            } else {
                System.out.println("WebEngine not initialized");
            }
        });
    }

    public void updateWeb() {
        System.out.println("updateWeb called");
        Platform.runLater(() -> {
            if (webEngine != null) {
                webEngine.reload();
            } else {
                System.out.println("WebView or WebEngine is null");
            }
        });
    }
}
