package com.xx.chinetek.cyproduct.OffShelf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.xx.chinetek.Service.ServiceElceSync;
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



    UpdateUIBroadcastReceiver broadcastReceiver;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATEUI);
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);
        // 启动服务
        Intent intent = new Intent(this, ServiceElceSync.class);
        startService(intent);
    }

    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            txtSendCount.setText(String.valueOf(intent.getExtras().getInt("count")));
        }

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

    @Override
    protected void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
        // 注销广播
        unregisterReceiver(broadcastReceiver);
    }


}
