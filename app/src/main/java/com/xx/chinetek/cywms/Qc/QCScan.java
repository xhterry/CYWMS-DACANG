package com.xx.chinetek.cywms.Qc;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


@ContentView(R.layout.activity_qcscan)
public class QCScan extends BaseActivity {

    Context context=QCScan.this;
    @ViewInject(R.id.lsvQCScan)
    ListView lsvQCScan;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.QC_subtitle), true);
        x.view().inject(this);
        //List<ReceiptDetail_Model> receiptDetailModels=getData();
//        ReceiptScanDetailAdapter receiptScanDetailAdapter=new ReceiptScanDetailAdapter(context,receiptDetailModels);
//        lsvQCScan.setAdapter(receiptScanDetailAdapter);
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


//    List<ReceiptDetail_Model> getData(){
//        List<ReceiptDetail_Model> receiptDetailModels=new ArrayList<>();
//        for(int i=0;i<10;i++){
//            ReceiptDetail_Model receiptDetailModel=new ReceiptDetail_Model();
//            receiptDetailModel.setMaterialNo("条码"+i);
//            receiptDetailModel.setMaterialDesc("物料描述"+i);
//            receiptDetailModel.setScanQty(1f);
//            receiptDetailModels.add(receiptDetailModel);
//        }
//        return receiptDetailModels;
//    }
}
