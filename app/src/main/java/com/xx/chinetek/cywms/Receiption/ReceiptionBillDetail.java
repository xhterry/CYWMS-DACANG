package com.xx.chinetek.cywms.Receiption;

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

/*
没有用到
 */
@ContentView(R.layout.activity_receipyion_bill_detail)
public class ReceiptionBillDetail extends BaseActivity {


    Context context = ReceiptionBillDetail.this;
    @ViewInject(R.id.lsvReceiptDetail)
    ListView lsvReceiptDetail;
//    @ViewInject(R.id.edt_DelBarcode)
//    EditText edtDelBarcode;

    ReceiptBillDetailAdapter receiptDetailAdapter;
    ReceiptDetail_Model receiptDetailModel;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.receiptscan_billdetail), true);
        x.view().inject(this);
        receiptDetailModel=getIntent().getParcelableExtra("receiptDetailModel");
        bindListview();
    }

//    @Event(value = R.id.edt_DelBarcode,type = View.OnKeyListener.class)
//    private  boolean edtDelBarcodeClcik(View v, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)// 如果为Enter键
//        {
//            keyBoardCancle();
//            String code=edtDelBarcode.getText().toString().trim();
//            BarCodeInfo barCodeInfo=new BarCodeInfo(code);
//            int index=receiptDetailModel.getLstBarCode().indexOf(barCodeInfo);
//            if(index!=-1){
//                receiptDetailModel.getLstBarCode().remove(index);
//                bindListview();
//            }else{
//                MessageBox.Show(context, getString(R.string.Error_BarcodeNotInList)+"|"+barCodeInfo.getSerialNo());
//                return false;
//            }
//            CommonUtil.setEditFocus(edtDelBarcode);
//        }
//        return false;
//    }

    void bindListview(){
        if(receiptDetailModel!=null){
            receiptDetailAdapter = new ReceiptBillDetailAdapter(context, receiptDetailModel.getLstBarCode());
            lsvReceiptDetail.setAdapter(receiptDetailAdapter);
        }

    }

}
