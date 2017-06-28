package com.xx.chinetek.cyproduct.Receiption;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.xx.chinetek.adapter.Receiption.ReceiptScanDetailAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Receiption.ReceiptDetail_Model;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_product_receiption_scan)
public class ReceiptionScan extends BaseActivity {

    Context context = ReceiptionScan.this;
    @ViewInject(R.id.lsvReceiptScan)
    ListView lsvReceiptScan;
    @ViewInject(R.id.btn_ReceiptDetail)
    Button btnReceiptDetail;

    ReceiptScanDetailAdapter receiptScanDetailAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.receiptscan_subtitle), true);
        x.view().inject(this);
        List<ReceiptDetail_Model> receiptDetailModels=getData();
        receiptScanDetailAdapter=new ReceiptScanDetailAdapter(context,receiptDetailModels);
        lsvReceiptScan.setAdapter(receiptScanDetailAdapter);
    }

    @Event(R.id.btn_ReceiptDetail)
    private void btnReceiptDetailOnclick(View view) {
        Intent intent = new Intent(context, ReceipyionBillDetail.class);
        startActivityLeft(intent);
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

    List<ReceiptDetail_Model> getData(){
        List<ReceiptDetail_Model> receiptDetailModels=new ArrayList<>();
        for(int i=0;i<10;i++){
            ReceiptDetail_Model receiptDetailModel=new ReceiptDetail_Model();
            receiptDetailModel.setMaterialNo("条码"+i);
            receiptDetailModel.setMaterialDesc("物料描述"+i);
            receiptDetailModel.setScanQty(1f);
            receiptDetailModels.add(receiptDetailModel);
        }
        return receiptDetailModels;
    }
}
