package com.xx.chinetek.cyproduct.Manage;

import android.content.Context;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(R.layout.activity_product_material_config)
public class ProductMaterialConfig extends BaseActivity {

    Context context=ProductMaterialConfig.this;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_MaterialConfig_subtitle), true);
        x.view().inject(this);
    }
}
