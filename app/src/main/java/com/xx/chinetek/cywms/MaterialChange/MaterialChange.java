package com.xx.chinetek.cywms.MaterialChange;

import android.content.Context;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(R.layout.activity_material_change)
public class MaterialChange extends BaseActivity {

   Context context=MaterialChange.this;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.MaterialChange_scan_subtitle), true);
        x.view().inject(this);
    }
}
