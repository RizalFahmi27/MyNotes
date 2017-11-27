package com.lynx.github.multinote.util;

import android.app.DialogFragment;
import android.content.Context;

/**
 * Created by rizalfahmi on 12/10/17.
 */

public abstract class BaseDialogFragment<T> extends DialogFragment {
    private T mActivity;

    public final T getActivityInstance(){
        return mActivity;
    }

    @Override
    public void onAttach(Context context) {
        mActivity = (T) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }
}
