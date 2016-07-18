package cn.aofeng.wd;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.aofeng.wd.impl.DefaultScheduler;
import cn.aofeng.wd.impl.DownloadTask;

/**
 * 页面内容解析器。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class Parser {
    
    private static Logger _logger = Logger.getLogger(Parser.class);

    private final static String _includeFileSuffix = "js,css,bmp,gif,png,jpg,jpeg,tif,tiff,svg";
    private static Set<String> _includeFileSuffixSet = new HashSet<String>(32);
    
    private final static String _excludeFileSuffix = "zip,tar,gz,rar,7z,mov,avi,mp4";
    private static Set<String> _excludeFileSuffixSet = new HashSet<String>(32);
    
    private String _readCharset;
    private String _writeCharset;
    
    public Parser(String readCharset, String writeCharset) {
        this._readCharset = readCharset;
        this._writeCharset = writeCharset;
    }
    
    public void parse(Page page) {
        Document doc = Jsoup.parse(page.getContent());
        process(page, doc, "href"); // 处理标签<a>、<script>和<link>
        process(page, doc, "src");   // 处理标签<img>
        writeFile(page, doc);
    }
    
    private void process(Page page, Document doc, String filterAttr) {
        Elements elements = doc.getElementsByAttribute(filterAttr);
        for (Iterator<Element> iterator = elements.iterator(); iterator.hasNext();) {
            Element e = iterator.next();
            String linkStr = e.attr(filterAttr);
            if (_logger.isDebugEnabled()) {
                _logger.debug("待处理的元素:"+e.outerHtml());
                _logger.debug("元素中的链接:"+linkStr);
            }
            if (StringUtils.isBlank(linkStr)) {
                continue;
            }
            URI link = URI.create(linkStr);
            String linkHost = link.getHost();
            int linkPort = link.getPort();
            String linkPath = link.getPath();
            Scheduler scheduler = DefaultScheduler.getInstance();
            if (null == linkHost  || (null != linkHost && linkHost.equalsIgnoreCase(page.getHost()) && linkPort == page.getPort()) ) {
                // 当前站点的页面，处理所有资源（但是需要排除的资源不处理）
                String suffix = obtainSuffix(linkPath);
                if (!needExclude(suffix)) {
                    String targetUrl = assembleFullUrl(page.getHost(), page.getPort(), linkPath);
                    scheduler.submit(new DownloadTask(targetUrl, _readCharset, _writeCharset));
                    if (_logger.isDebugEnabled()) {
                        _logger.debug("开始处理站内链接:"+linkStr);
                        _logger.debug("完整的站内目标链接:"+targetUrl);
                    }
                } else {
                    if (_logger.isDebugEnabled()) {
                        _logger.debug("忽略站内链接:"+linkStr);
                    }
                }
            } else {
                // 不是当前站点的页面，只处理静态文件
                String suffix = obtainSuffix(linkPath);
                if (null != suffix && needInclude(suffix)) {
                    String targetUrl = assembleFullUrl(linkHost, link.getPort(), linkPath);
                    scheduler.submit(new DownloadTask(targetUrl, _readCharset, _writeCharset));
                    if (_logger.isDebugEnabled()) {
                        _logger.debug("开始处理站外链接:"+linkStr);
                        _logger.debug("完整的站外目标链接:"+targetUrl);
                    }
                } else {
                    if (_logger.isDebugEnabled()) {
                        _logger.debug("忽略站外链接:"+linkStr);
                    }
                }
            }
        } // end of for
    }
    
    private String obtainSuffix(String linkPath) {
        if (StringUtils.isBlank(linkPath)) {
            return null;
        }
        
        int index = linkPath.lastIndexOf('.');
        if (-1 != index && index < (linkPath.length()-1)) {
            return linkPath.substring(index+1);
        }
        
        return null;
    }
    
    private void writeFile(Page page, Document doc) {
        String dir = "/home/nieyong/temp/WebsiteDownloader"; // FIXME 为调通代码，暂时hardcode输出文件的目录
        String fileFullPath = assembleFileFullPath(dir, page.getHost(), page.getPath());
        File file = new File(fileFullPath);
        if (!file.exists()) {
            try {
                FileUtils.writeStringToFile(file, doc.toString(), _writeCharset);
            } catch (IOException e) {
                _logger.error("写文件"+file+"出错", e);
            }
        }
    }
    
    private String assembleFileFullPath(String dir, String host, String path) {
        StringBuilder buffer = new StringBuilder(64)
            .append(dir)
            .append(dir.endsWith("/") ? "" : "/")
            .append(StringUtils.isBlank(host) ? "" : host);
        if (!StringUtils.isBlank(path)) {
            String[] temp = path.split("/\\|\\\\");
            for (String str : temp) {
                buffer.append(str);
            }
        }
        
        return buffer.toString();
    }
    
    private String assembleFullUrl(String host, int port, String path) {
        StringBuilder buffer = new StringBuilder(64)
            .append("http://")
            .append(host)
            .append(port > 0 ? (":"+port) : "")
            .append(path.startsWith("/") ? path : ("/"+path));
        
        return buffer.toString();
    }
    
    /**
     * 判断资源是否需要处理。
     * 
     * @param fileSuffix 文件名后缀
     * @return 如果需要处理，返回true。否则，返回false。
     */
    public boolean needInclude(String fileSuffix) {
        return _includeFileSuffixSet.contains(fileSuffix);
    }
    
    /**
     * 判断资源是否需要排除。
     * 
     * @param fileSuffix 文件名后缀
     * @return 如果需要排除，返回true。否则，返回false。
     */
    public boolean needExclude(String fileSuffix) {
        return _excludeFileSuffixSet.contains(fileSuffix);
    }

    static {
        Collections.addAll(_includeFileSuffixSet, _includeFileSuffix.split(","));
        Collections.addAll(_excludeFileSuffixSet, _excludeFileSuffix.split(","));
    }
    
}
