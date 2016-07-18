package cn.aofeng.wd;

public interface Scheduler {

    /**
     * 初始化调度器。单个虚拟机进程只需要调用一次。
     */
    public abstract void init();

    /**
     * 销毁调度器。
     */
    public abstract void destroy();

    /**
     * 向调度器提交一个下载任务，由调度器异步执行。
     * 
     * @param task 下载任务
     */
    public abstract void submit(Runnable task);

}