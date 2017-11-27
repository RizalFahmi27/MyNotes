package com.lynx.github.multinote.login;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.lynx.github.multinote.BasePresenter;
import com.lynx.github.multinote.BaseView;

/**
 * Created by rizalfahmi on 17/11/17.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter>{
        void setProgressBar(boolean flag);
        void showMainActivity();
        void showSignInError(ApiException e);
    }

    interface Presenter extends BasePresenter{
        void handleSignInResult(Task<GoogleSignInAccount> task);
    }
}
