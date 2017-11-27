package com.lynx.github.multinote.Main;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rizalfahmi on 24/11/17.
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private GoogleSignInClient mSignInClient;


    public MainPresenter(@NonNull MainContract.View view,
                         @NonNull GoogleSignInClient signInClient){
        this.mView = checkNotNull(view,"View cannot be null");
        this.mSignInClient = checkNotNull(signInClient,"GoogleSignInClient cannot be null");
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void saveImage(Bitmap bitmap) {

    }

    @Override
    public void signOut() {
        mView.setProgressBar(true);
        mSignInClient.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mView.setProgressBar(false);
                        mView.signOut(task);
                    }
                });
    }
}
