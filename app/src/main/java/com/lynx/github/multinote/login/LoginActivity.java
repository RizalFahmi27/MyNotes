package com.lynx.github.multinote.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.lynx.github.multinote.Main.MainActivity;
import com.lynx.github.multinote.R;
import com.lynx.github.multinote.util.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.lynx.github.multinote.login.LoginPresenter.RC_SIGN_IN;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener{

    @BindView(R.id.container_login) RelativeLayout containerLogin;
    @BindView(R.id.image_bgLogin) ImageView imageBgLogin;
    @BindView(R.id.button_signIn) SignInButton signInButton;
    @BindView(R.id.container_progressBar) LinearLayout mContainerProgressBar;

    private LoginPresenter mLoginPresenter;
    private SharedPrefManager mSharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initStuffs();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoginPresenter.start();
    }

    private void initStuffs() {
        mSharedPrefManager = SharedPrefManager.getInstance(this);
        GoogleSignInOptions mSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,mSignInOptions);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        mLoginPresenter = new LoginPresenter(mSharedPrefManager,this,signInClient,signInAccount);
    }

    private void initView() {
        signInButton.setOnClickListener(this);
        Bitmap background = BitmapFactory.decodeResource(getResources(),R.drawable.bg_login);
        Blurry.with(this).radius(25).sampling(2).from(background).into(imageBgLogin);
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setPadding(0, 0, 20, 0);
                return;
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ActivityCompat.getColor(this,R.color.colorEasyBlack));
        }

    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
//        this.mLoginPresenter = checkNotNull(mLoginPresenter,"LoginPresenter cannot be null");

    }

    @Override
    public void setProgressBar(boolean flag) {
        if(flag){
             mContainerProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            mContainerProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showSignInError(ApiException e) {
        Toast.makeText(this,getString(R.string.text_sign_in_error) + e.getStatusCode(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_signIn:
                signIn();
                break;
        }
    }


    public void signIn() {
        setProgressBar(true);
        Intent signInIntent = mLoginPresenter.getSignInClient().getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            mLoginPresenter.handleSignInResult(task);
        }
    }


}
