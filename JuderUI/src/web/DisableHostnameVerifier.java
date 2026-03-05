/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;
/**
 *
 * @author tange
 */
public class DisableHostnameVerifier {
    public static void disable() throws Exception {
        // 创建 SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        
        // 创建一个 TrustManager，实现 X509TrustManager 接口并禁用主题备用名称检查
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
        };

        // 初始化 SSLContext，将自定义的 TrustManager 添加到 TrustManager 数组中
        sslContext.init(null, new TrustManager[] { trustManager }, null);
        
        // 获取 SSLContext 中的 SSLSocketFactory，并设置为默认的 SSLSocketFactory
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        
        // 禁用主题备用名称检查
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
}