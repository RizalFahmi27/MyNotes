package com.lynx.github.multinote.Main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.lynx.github.multinote.Main.Contacts.ContactsListFragment;
import com.lynx.github.multinote.R;
import com.lynx.github.multinote.login.LoginActivity;
import com.lynx.github.multinote.util.ActivityUtils;
import com.lynx.github.multinote.util.LoadingDialog;
import com.lynx.github.multinote.util.SharedPrefManager;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.nav_view) NavigationView mNavigationView;
    ImageView imageProfile;
    TextView textName;
    TextView textEmail;

    private SharedPrefManager mSharedPrefManager;
    private MainPresenter mMainPresenter;
    private LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initStuffs();
        initView();
    }

    private void initStuffs() {
        GoogleSignInOptions mSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,mSignInOptions);
        mSharedPrefManager = SharedPrefManager.getInstance(this);

        mMainPresenter = new MainPresenter(this,signInClient);
        dialog = LoadingDialog.newInstance(null,null);
    }


    private void initView() {
        setupDrawer();

        textName.setText(mSharedPrefManager.getDisplayName());
        textEmail.setText(mSharedPrefManager.getEmail());

        loadProfilePict();
        configureFragment(0);
    }

    private void loadProfilePict() {
        Glide.with(this).load(mSharedPrefManager.getPhotoURL()).into(imageProfile);
    }

    private void configureFragment(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = ContactsListFragment.newInstance();
                break;
        }

        ActivityUtils.replaceFragment(getSupportFragmentManager(),fragment,R.id.container_fragmentMain);
    }

    private void setupDrawer() {
        mDrawerLayout.setStatusBarBackground(R.color.colorEasyBlack);
        if(mNavigationView!=null){
            View view = mNavigationView.getHeaderView(0);
            textEmail = view.findViewById(R.id.text_email);
            textName = view.findViewById(R.id.text_displayName);
            imageProfile = view.findViewById(R.id.image_profile);
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_logout:
                            logout();
                            break;
                    }
                    item.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                }
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.text_confirm))
                .setMessage(getString(R.string.text_are_you_sure_logout))
                .setNegativeButton(getString(R.string.text_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.text_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mMainPresenter.signOut();
                    }
                }).show();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {

    }

    @Override
    public void setProgressBar(boolean isActive) {
        if(isActive){
            dialog.show(getFragmentManager(),"dialog");
        }
        else {
            dialog.dismiss();
        }
    }

    @Override
    public void signOut(Task<Void> task) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
