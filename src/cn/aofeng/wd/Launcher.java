package cn.aofeng.wd;

import org.apache.log4j.Logger;

import cn.aofeng.wd.impl.DefaultScheduler;
import cn.aofeng.wd.impl.DownloadTask;

/**
 * 启动器。负责对网站的入口页进行解析并启动下载器、调度器。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class Launcher {

    private static Logger _logger = Logger.getLogger(Launcher.class);
    
    /** 
     * @param args 参数列表：
     * <ul>
     *  <li>参数1：网站主页地址</li>
     *  <li>参数2：读取网页内容的编码</li>
     *  <li>参数3：输出网页内容的编码</li>
     * </ul>
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            _logger.error("参数错误。语法：java cn.aofeng.wd.Downloader 网站主页地址 读取网页内容的编码 输出网页内容的编码");
            System.exit(-1);
        }
        
        // 启动调度器
        Scheduler scheduler = DefaultScheduler.getInstance();
        scheduler.init();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                _logger.info("执行关闭钩子");
                DefaultScheduler.getInstance().destroy();
            }
        });
        
        // 解析WEB站点的主页，开始进行下载
        String siteUrlStr = args[0];
        String readCharset = args[1];
        String writeCharset = args[2];
        scheduler.submit(new DownloadTask(siteUrlStr, readCharset, writeCharset));
        _logger.info("主线程执行完毕");
    }

}
