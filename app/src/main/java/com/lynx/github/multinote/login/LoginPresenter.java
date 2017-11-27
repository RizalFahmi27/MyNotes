package com.lynx.github.multinote.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.lynx.github.multinote.util.SharedPrefManager;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rizalfahmi on 17/11/17.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final SharedPrefManager sharedPrefManager;
    private LoginContract.View mLoginView;
    private GoogleSignInClient mSignInClient;
    private GoogleSignInAccount mSignInAccount;

    public static final int RC_SIGN_IN = 12;
    private final String TAG = getClass().getSimpleName();

    public LoginPresenter(@NonNull  SharedPrefManager sharedPrefManager, @NonNull LoginContract.View mLoginView,
                          @NonNull GoogleSignInClient signInClient, GoogleSignInAccount signInAccount) {
        this.mLoginView = checkNotNull(mLoginView,"LoginView cannot be null");
        this.sharedPrefManager = checkNotNull(sharedPrefManager,"SharedPrefManager cannot be null");
        this.mSignInAccount = signInAccount;
        this.mSignInClient = checkNotNull(signInClient,"GoogleSignInClient cannot be null");
        mLoginView.setPresenter(this);
    }

    @Override
    public void start() {
        if(checkIfAlreadySignedIn()){
            mLoginView.showMainActivity();
        }
    }

    public GoogleSignInClient getSignInClient(){
        return mSignInClient;
    }

    /**
     * Check whether user already signed in using google id
     * @return account if present
     */
    private boolean checkIfAlreadySignedIn() {
        return mSignInAccount != null;
    }


    /**
     * Handle google sign in result
     * @param task
     */
    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> task) {
        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);
            String id = account.getId();
            String idToken = account.getIdToken();
            String gievenName = account.getGivenName();
            String displayName = account.getDisplayName();
            String familyName = account.getFamilyName();
            String email = account.getEmail();
            String photoURL = account.getPhotoUrl().toString();
            sharedPrefManager.login(id,idToken,displayName,gievenName,familyName,email,photoURL);
            mLoginView.showMainActivity();
        }
        catch (ApiException e){
            Log.w(TAG,"Sign in failed code:"+e.getStatusCode());
            mLoginView.showSignInError(e);
            mLoginView.setProgressBar(false);
        }
    }
}
