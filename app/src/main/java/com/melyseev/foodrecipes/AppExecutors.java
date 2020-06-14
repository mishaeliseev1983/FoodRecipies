package com.melyseev.foodrecipes;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    private static AppExecutors instance;
    public static AppExecutors getInstance(){
        if(instance==null)
            instance= new AppExecutors();
        return instance;
    }

    private ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(3);

    public ScheduledExecutorService getScheduledExecutorService(){
        return scheduledExecutorService;
    }
}
