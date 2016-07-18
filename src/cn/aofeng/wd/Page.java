package cn.aofeng.wd;

/**
 * 页面信息和内容。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class Page {

    /** 页面URL的主机部分 */
    private String host;
    
    /** 页面URL的端口部分 */
    private int port;
    
    /** 页面URL的路径部分 */
    private String path;
    
    /** 页面URL的查询参数部分 */
    private String query;
    
    /** 页面的HTML内容 */
    private String content;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + port;
        result = prime * result + ((query == null) ? 0 : query.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Page)) {
            return false;
        }
        Page other = (Page) obj;
        if (host == null) {
            if (other.host != null) {
                return false;
            }
        } else if (!host.equals(other.host)) {
            return false;
        }
        if (path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!path.equals(other.path)) {
            return false;
        }
        if (port != other.port) {
            return false;
        }
        if (query == null) {
            if (other.query != null) {
                return false;
            }
        } else if (!query.equals(other.query)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(128)
            .append("Page [host=").append(host)
            .append(", port=").append(port)
            .append(", path=").append(path)
            .append(", query=").append(query)
            .append( "]");
        return buffer.toString();
    }

}
