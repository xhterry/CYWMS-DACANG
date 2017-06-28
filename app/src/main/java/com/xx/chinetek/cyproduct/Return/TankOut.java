package com.xx.chinetek.cyproduct.Return;

import android.content.Context;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;


@ContentView(R.layout.activity_tank_out)
public class TankOut extends BaseActivity {

   Context context=TankOut.this;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
    }
}
