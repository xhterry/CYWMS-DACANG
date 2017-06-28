package com.xx.chinetek.util.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cywms.R;


/**
 * Created by GHOST on 2017/3/13.
 */

public class LoadingDialog extends BaseDialog {


    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected View getDefaultView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);
        ImageView icon = (ImageView) v.findViewById(R.id.icon_loading);
        TextView tv = (TextView) v.findViewById(R.id.tv_loading_text);
        tv.setText(BaseApplication.DialogShowText);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate);
        icon.startAnimation(animation);
        return v;
    }
}
