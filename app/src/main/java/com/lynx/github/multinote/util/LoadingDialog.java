package com.lynx.github.multinote.util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lynx.github.multinote.R;


/**
 * Created by rizalfahmi on 12/10/17.
 */

public class LoadingDialog extends BaseDialogFragment<Context> {

    private String message;
    private String title;

    public static LoadingDialog newInstance(String title, String message){
        LoadingDialog loadingDialog = new LoadingDialog();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("message",message);
        loadingDialog.setArguments(args);
        return loadingDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(getDialog()!=null)
            getDialog().setCanceledOnTouchOutside(false);
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.layout_custom_dialog,container,false);
        view.setBackgroundResource(android.R.color.transparent);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void dismiss() {
        try{
            super.dismiss();
        }
        catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    //    @Override
//    public void dismiss() {
//        if(isAdded())
//            super.dismiss();
//    }
}
