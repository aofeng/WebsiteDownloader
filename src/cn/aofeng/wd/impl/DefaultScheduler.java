package cn.aofeng.wd.impl;

import cn.aofeng.threadpool4j.ThreadPoolImpl;
import cn.aofeng.wd.Scheduler;

/**
 * 下载任务调度器。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class DefaultScheduler implements Scheduler {

    private ThreadPoolImpl _pool;
    
    private static DefaultScheduler _instance = new DefaultScheduler();
    
    public static DefaultScheduler getInstance() {
        return _instance;
    }
    
    public DefaultScheduler() {
        _pool = new ThreadPoolImpl();
    }
    
    @Override
    public void init() {
        _pool.init();
    }
    
    @Override
    public void destroy() {
        _pool.destroy();
    }
    
    @Override
    public void submit(Runnable task) {
        _pool.submit(task);
    }

}
