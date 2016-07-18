package cn.aofeng.wd.impl;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.log4j.Logger;

import cn.aofeng.wd.Downloader;

/**
 * 默认的内容下载器实现。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class DefaultDownloader implements Downloader {

    private static Logger _logger = Logger.getLogger(DefaultDownloader.class);
    
    public String read(String url, String readCharset) {
        Response response = null;
        try {
            response = Request.Get(url)
                .addHeader(HttpHeaders.USER_AGENT, "WD")
                .connectTimeout(3 * 1000)
                .socketTimeout(10 * 1000)
                .execute();
        } catch (ClientProtocolException e) {
            _logger.error("错误的协议", e);
        } catch (IOException e) {
            _logger.error("连接服务器出错", e);
        }
        if (null == response) {
            return null;
        }
        
        Content content = null;
        try {
            content = response.returnContent();
        } catch (IOException e) {
            _logger.error("获取响应处理时出错", e);
        }
        String temp = content.asString(Charset.forName(readCharset));
        return temp;
    }

}
