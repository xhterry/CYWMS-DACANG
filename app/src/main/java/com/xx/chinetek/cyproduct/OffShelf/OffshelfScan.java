package com.xx.chinetek.cyproduct.OffShelf;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_product_offshelf_scan)
public class OffshelfScan extends BaseActivity {

   Context context=OffshelfScan.this;
@ViewInject(R.id.txt_Unboxing)
    TextView txtSendCount;
    @ViewInject(R.id.edtSendCount)
    EditText edtSendCount;

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
