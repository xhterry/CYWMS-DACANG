package com.xx.chinetek.cyproduct.Receiption;

import android.content.Context;
import android.widget.ListView;

import com.xx.chinetek.adapter.wms.Receiption.ReceiptBillDetailAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Receiption.ReceiptDetail_Model;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_receipyion_bill_detail)
public class ReceiptionBillDetail extends BaseActivity {


    Context context = ReceiptionBillDetail.this;
    @ViewInject(R.id.lsvReceiptDetail)
    ListView lsvReceiptDetail;
    ReceiptBillDetailAdapter receiptScanDetailAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.receiptscan_billdetail), true);
        x.view().inject(this);
        List<ReceiptDetail_Model> receiptDetailModels=getData();
        receiptScanDetailAdapter=new ReceiptBillDetailAdapter(context,receiptDetailModels.get(0).getLstBarCode());
        lsvReceiptDetail.setAdapter(receiptScanDetailAdapter);
    }



    List<ReceiptDetail_Model> getData(){
        List<ReceiptDetail_Model> receiptDetailModels=new ArrayList<>();
        for(int i=0;i<10;i++){
            ReceiptDetail_Model receiptDetailModel=new ReceiptDetail_Model();
            receiptDetailModel.setMaterialNo("123455"+i);
            receiptDetailModel.setMaterialDesc("物料描述"+i);
            receiptDetailModel.setScanQty(1f);
            receiptDetailModel.setCompany("据点");
            receiptDetailModel.setDepartment("部门");
            receiptDetailModels.add(receiptDetailModel);
        }
        return receiptDetailModels;
    }
}
