package com.xx.chinetek.cyproduct.OffShelf;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(R.layout.activity_product_semi_scan)
public class SemiProductScan extends BaseActivity {

    Context context=SemiProductScan.this;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {

        }
        return super.onOptionsItemSelected(item);
    }
}
