package com.yuan.cn.blog.commons;

import java.util.concurrent.*;

public class ThreadUtil {
    private static ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void submitTask(Runnable task){
        service.submit(task);
    }
    public static <T> T submitTaskCallable(Callable<T> task) throws ExecutionException, InterruptedException, TimeoutException {
        Future<T> submit = service.submit(task);
        return submit.get(2000, TimeUnit.SECONDS);
    }
}
