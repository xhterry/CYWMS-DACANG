package com.xx.chinetek.cyproduct.Adjust;

import android.content.Context;
import android.widget.ListView;

import com.xx.chinetek.adapter.wms.Receiption.ReceiptBillDetailymhAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

@ContentView(R.layout.activity_inner_move_detailymh)
public class InnerMoveDetailymh extends BaseActivity {

    Context context = InnerMoveDetailymh.this;
    @ViewInject(R.id.lsv_InnerMoveList)
    ListView lsvInnerMoveList;

    ReceiptBillDetailymhAdapter receiptDetailAdapter;
    ArrayList<StockInfo_Model> barCodeInfos;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
        barCodeInfos=getIntent().getParcelableArrayListExtra("barCodeInfos");
        bindListview();
    }


    void bindListview(){
        if(barCodeInfos!=null){
            receiptDetailAdapter = new ReceiptBillDetailymhAdapter(context, barCodeInfos);
            lsvInnerMoveList.setAdapter(receiptDetailAdapter);
        }

    }
}
