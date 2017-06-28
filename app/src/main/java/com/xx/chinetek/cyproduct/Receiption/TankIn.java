package com.xx.chinetek.cyproduct.Receiption;

import android.content.Context;
import android.widget.ListView;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_tank_in)
public class TankIn extends BaseActivity{

    Context context=TankIn.this;

    @ViewInject(R.id.lsvTankDetail)
    ListView lsvTankDetail;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
    }
}
