package com.ctrip.framework.apollo.portal.spi.letv.sso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author evilhex.
 * @date 2018/9/5 上午10:20.
 */
public class ThreadUtil {

    private static final Logger logger = LoggerFactory.getLogger(ThreadUtil.class);

    private static volatile ExecutorService executor;

    static {
        initExecutor();
    }

    /**
     * 异步执行一个任务
     *
     * @param task 任务
     * @return 任务结果
     */
    public static <V> Future<V> submit(Callable<V> task) {
        return executor.submit(task);
    }

    /**
     * 异步执行，超时会停止任务
     *
     * @param task
     * @param timeout 超时时间为秒
     * @return
     */
    public static <T> T submit(Callable<T> task, int timeout) {
        Future<T> future = executor.submit(task);
        T t;
        try {
            t = future.get(timeout, TimeUnit.SECONDS);
            return t;

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            future.cancel(true);
            task = null;
            t = null;
        }
        return null;
    }

    /**
     * 异步执行一个任务
     *
     * @param task 任务
     */
    public static Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    /**
     * 批量执行任务
     * 超时间为秒
     *
     * @param callables
     * @param timeout
     * @return
     */
    public static <V> List<V> submit(List<Callable<V>> callables, long timeout) {
        List<Future<V>> futures = new ArrayList<Future<V>>();
        List<V> result = new ArrayList<V>();

        try {
            futures = executor.invokeAll(callables, timeout, TimeUnit.SECONDS);

            for (Future<V> future : futures) {
                try {
                    V v = future.get();
                    if (v != null) {
                        result.add(v);
                    }
                } catch (Exception e) {
                    logger.error("submit error", e);
                }
            }
        } catch (InterruptedException e) {
            logger.error("submit error", e);
        }

        return result;
    }

    /**
     * 批量执行任务，返回结果
     *
     * @param callables
     * @return
     */
    public static <V> List<V> submit(List<Callable<V>> callables) {

        List<Future<V>> futures = new ArrayList<Future<V>>();
        List<V> result = new ArrayList<V>();

        try {
            futures = executor.invokeAll(callables);

            for (Future<V> future : futures) {
                try {
                    V v = future.get();
                    if (v != null) {
                        result.add(v);
                    }
                } catch (Exception e) {
                    logger.error("submit error", e);
                }
            }
        } catch (InterruptedException e) {
            logger.error("submit error", e);
        }

        return result;
    }

    /**
     * 关闭服务
     */
    public static void shutdown() {
        if (null != executor) {
            executor.shutdown();
        }
    }

    /**
     * 初始化线程池
     */
    private static void initExecutor() {
        if (executor == null) {
            synchronized (ThreadUtil.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 100, 1, TimeUnit.MINUTES,
                            new ArrayBlockingQueue<Runnable>(10000), new DefaultThreadFactory());
                    //停止监控
                    //                    executor.submit(new Monitor((ThreadPoolExecutor) executor));
                }
            }
        }
    }

    /**
     * 线程工厂
     *
     * @author shaoxiangfei
     */
    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final ThreadGroup threadGroup;
        private final String namePrefix;

        public DefaultThreadFactory() {
            SecurityManager sm = System.getSecurityManager();
            this.threadGroup = sm != null ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = "pool-" + DefaultThreadFactory.poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(this.threadGroup, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }

    }

    static class Monitor implements Runnable {
        final ThreadPoolExecutor executor;

        public Monitor(ThreadPoolExecutor executor) {
            this.executor = executor;
        }

        @Override
        public void run() {
            while (!this.executor.isShutdown()) {
                if (logger.isInfoEnabled()) {
                    logger.info("poolCount={},activeCount={},coreSize={},maxSize={},largetsSize={},queueSize={},reminning={},completedCount={}",
                            executor.getPoolSize(), executor.getActiveCount(), executor.getCorePoolSize(), executor.getMaximumPoolSize(),
                            executor.getLargestPoolSize(), executor.getQueue().size(), executor.getQueue().remainingCapacity(),
                            executor.getCompletedTaskCount());
                }

                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {
                    logger.warn(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}

