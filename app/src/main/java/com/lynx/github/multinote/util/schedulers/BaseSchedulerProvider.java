package com.lynx.github.multinote.util.schedulers;

/**
 * Created by rizalfahmi on 24/11/17.
 */

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;


/**
 * Allow providing different types of {@link Scheduler}s.
 */
public interface BaseSchedulerProvider {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
