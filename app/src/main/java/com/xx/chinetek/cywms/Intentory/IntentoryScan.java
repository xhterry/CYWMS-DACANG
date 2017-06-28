package com.xx.chinetek.cywms.Intentory;

import android.content.Context;
import android.content.Intent;
import android.view.View;
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

@ContentView(R.layout.activity_intentory_scan)
public class IntentoryScan extends BaseActivity {

  Context context=IntentoryScan.this;
@ViewInject(R.id.lsvIntentoryScan)
    ListView lsvIntentoryScan;


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Intentory_subtitle), true);
        x.view().inject(this);
        List<ReceiptDetail_Model> receiptDetailModels=getData();
        ReceiptScanDetailAdapter receiptScanDetailAdapter=new ReceiptScanDetailAdapter(context,receiptDetailModels);
        lsvIntentoryScan.setAdapter(receiptScanDetailAdapter);
    }


    @Event(R.id.btn_PalletDetail)
    private void btnPalletDetailClick(View view){
        Intent intent=new Intent(context,IntentoryDetial.class);
        startActivityLeft(intent);
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
