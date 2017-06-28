package com.xx.chinetek.cyproduct.Receiption;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cyproduct.work.ReportOutputNum;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(R.layout.activity_product_receiptsemiproduct)
public class ReceiptSemiProduct extends BaseActivity {

   Context context=ReceiptSemiProduct.this;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_complete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            Intent intent=new Intent(context, ReportOutputNum.class);
            startActivityLeft(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
