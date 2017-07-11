package com.xx.chinetek.cyproduct.OffShelf;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.xx.chinetek.Service.SocketService;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.SocketBaseActivity;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_product_offshelf_scan)
public class OffshelfScan extends SocketBaseActivity {

   Context context=OffshelfScan.this;
@ViewInject(R.id.txt_Unboxing)
    TextView txtSendCount;
    @ViewInject(R.id.txtWeight)
    TextView txtWeight;

    @ViewInject(R.id.edtSendCount)
    EditText edtSendCount;



    //UpdateUIBroadcastReceiver broadcastReceiver;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
        initVariables();//设置接收服务
    }



    protected void initVariables()
    {
        //给全局消息接收器赋值，并进行消息处理
        mReciver = new MessageBackReciver(){
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if(action.equals(SocketService.MESSAGE_ACTION))
                {
                    String message = intent.getStringExtra("message");
                    Log.v("WMSLOG_Socket", message);
                    String message1=message.split("\r\n")[0];
                    txtWeight.setText(message1.contains("ST,GS")?message1.split(",")[2].trim():"");
                }
            }
        };
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
