package com.lynx.github.multinote.Main;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.Task;
import com.lynx.github.multinote.BasePresenter;
import com.lynx.github.multinote.BaseView;

/**
 * Created by rizalfahmi on 24/11/17.
 */

public interface MainContract {
    interface View extends BaseView<Presenter>{
        void setProgressBar(boolean isActive);
        void signOut(Task<Void> task);

    }

    interface Presenter extends BasePresenter{
        void saveImage(Bitmap bitmap);
        void signOut();
    }
}
