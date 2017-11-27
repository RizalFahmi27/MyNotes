package com.lynx.github.multinote.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by rizalfahmi on 17/11/17.
 */

public class SharedPrefManager {

    public static final String SHAREDPREF_NAME = "do_sharedfpref";

    public static final String KEY_IS_LOGGED_IN = "key_is_logged_in";
    public static final String KEY_DISPLAY_NAME = "key_display_name";
    public static final String KEY_GIVEN_NAME = "key_given_name";
    public static final String KEY_FAMILY_NAME = "key_family_name";
    public static final String KEY_EMAIL = "key_email";
    public static final String KEY_ID_TOKEN = "key_id_token";
    public static final String KEY_ID = "key_id";
    public static final String KEY_PHOTO_URL = "key_photo_url";

    private Context mContext;

    public SharedPrefManager(Context context){
        this.mContext = context;
    }

    public static  synchronized SharedPrefManager getInstance(Context context){
        return new SharedPrefManager(context);
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREDPREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN,false);
    }

    @NonNull
    public String getDisplayName(){
        return getSharedPreferences().getString(KEY_DISPLAY_NAME,"");
    }

    @NonNull
    public String getFamilyName(){
        return getSharedPreferences().getString(KEY_FAMILY_NAME,"");
    }

    @NonNull
    public String getGivenName(){
        return getSharedPreferences().getString(KEY_GIVEN_NAME,"");
    }

    @NonNull
    public String getEmail(){
        return getSharedPreferences().getString(KEY_EMAIL,"");
    }

    @NonNull
    public String getIdToken(){
        return getSharedPreferences().getString(KEY_ID_TOKEN,"");
    }

    @NonNull
    public String getId(){
        return getSharedPreferences().getString(KEY_ID,"");
    }

    @NonNull
    public String getPhotoURL(){
        return getSharedPreferences().getString(KEY_PHOTO_URL,"");
    }

    public SharedPreferences getSharedPreferences(){
        return mContext.getSharedPreferences(SHAREDPREF_NAME,Context.MODE_PRIVATE);
    }

    /**
     * Save params value to sharedpref for future user id retrieval
     * @param id
     * @param idToken
     * @param displayName
     * @param givenName
     * @param familyName
     * @param email
     * @param photoUrl
     */
    public void login(String id, String idToken, String displayName, String givenName, String familyName, String email, String photoUrl){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREDPREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_ID,id);
        editor.putString(KEY_ID_TOKEN,idToken);
        editor.putString(KEY_DISPLAY_NAME,displayName);
        editor.putString(KEY_GIVEN_NAME,givenName);
        editor.putString(KEY_FAMILY_NAME,familyName);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PHOTO_URL,photoUrl);
        editor.putBoolean(KEY_IS_LOGGED_IN,true);
        editor.commit();
    }

}
