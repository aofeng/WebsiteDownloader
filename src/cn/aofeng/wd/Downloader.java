package cn.aofeng.wd;

/**
 * 内容下载器。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public interface Downloader {

    /**
     * 读取指定地址的内容。
     * 
     * @param url 待读取内容的地址
     * @param readCharset 将读取的内容转换成字符串的编码
     * @return 指定地址的内容根据指定编码转换后的字符串
     */
    public String read(String url, String readCharset);

}