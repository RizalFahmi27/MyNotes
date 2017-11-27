package com.lynx.github.multinote.util.schedulers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rizalfahmi on 24/11/17.
 */

public class SchedulerProvider implements BaseSchedulerProvider{

    @Nullable
    private static SchedulerProvider mInstance;

    private SchedulerProvider(){

    }

    public static synchronized SchedulerProvider getInstance(){
        if(mInstance == null){
            mInstance = new SchedulerProvider();
        }
        return mInstance;
    }

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
