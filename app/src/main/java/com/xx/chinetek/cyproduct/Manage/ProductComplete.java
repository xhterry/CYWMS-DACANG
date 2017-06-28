package com.xx.chinetek.cyproduct.Manage;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

@ContentView(R.layout.activity_product_complete)
public class ProductComplete extends BaseActivity {

    Context context=ProductComplete.this;




    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_Complete_subtitle), true);
        x.view().inject(this);
    }




}
