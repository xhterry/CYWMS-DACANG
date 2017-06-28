package com.xx.chinetek.FillPrint;

import android.content.Context;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(R.layout.activity_fill_print)
public class FillPrint extends BaseActivity {

   Context context=FillPrint.this;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_fillPrint_subtitle), true);
        x.view().inject(this);
    }
}
