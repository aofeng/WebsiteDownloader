package cn.aofeng.wd.impl;

import java.net.URI;

import cn.aofeng.wd.Downloader;
import cn.aofeng.wd.Page;
import cn.aofeng.wd.Parser;

/**
 * 页面下载任务。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class DownloadTask implements Runnable  {

    private String _url;
    
    /** 读取网页内容的编码 */
    private String _readCharset;
    
    /** 输出网页内容的编码 */
    private String _writeCharset;
    
    public DownloadTask(String url, String readCharset, String writeCharset) {
        this._url = url;
        this._readCharset = readCharset;
        this._writeCharset = writeCharset;
    }
    
    @Override
    public void run() {
        URI siteUri = URI.create(_url);
        Downloader downloader = new DefaultDownloader();
        String content = downloader.read(_url, _readCharset);
        
        Page page  = new Page();
        page.setHost(siteUri.getHost());
        page.setPort(siteUri.getPort());
        page.setPath(siteUri.getPath());
        page.setQuery(siteUri.getQuery());
        page.setContent(content);
        Parser parser = new Parser(_readCharset, _writeCharset);
        parser.parse(page);
    }

}
