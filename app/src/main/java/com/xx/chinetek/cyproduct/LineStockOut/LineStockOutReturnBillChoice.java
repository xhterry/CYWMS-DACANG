package com.xx.chinetek.cyproduct.LineStockOut;

import android.content.Context;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(R.layout.activity_line_stock_out_return_bill_choice)
public class LineStockOutReturnBillChoice extends BaseActivity {

    Context context=LineStockOutReturnBillChoice.this;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context=context;
        BaseApplication.toolBarTitle=new ToolBarTitle(getString(R.string.LineStockOutReturnBillChoice),true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=true;
    }
}
