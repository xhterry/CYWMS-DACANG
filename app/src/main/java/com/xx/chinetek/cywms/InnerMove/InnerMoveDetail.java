package com.xx.chinetek.cywms.InnerMove;

import android.content.Context;
import android.widget.ListView;

import com.xx.chinetek.adapter.wms.Receiption.ReceiptBillDetailAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Material.BarCodeInfo;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

@ContentView(R.layout.activity_inner_move_detail)
public class InnerMoveDetail extends BaseActivity {

    Context context = InnerMoveDetail.this;
    @ViewInject(R.id.lsv_InnerMoveList)
    ListView lsvInnerMoveList;

    ReceiptBillDetailAdapter receiptDetailAdapter;
    ArrayList<BarCodeInfo> barCodeInfos;

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
            receiptDetailAdapter = new ReceiptBillDetailAdapter(context, barCodeInfos);
            lsvInnerMoveList.setAdapter(receiptDetailAdapter);
        }

    }
}
