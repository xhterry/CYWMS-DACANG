package com.xx.chinetek.cyproduct.LineStockOut;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Pallet.PalletItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cyproduct.LineStockIn.LineStockInProduct;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.cywms.Receiption.ReceiptionScan;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.ArithUtil;
import com.xx.chinetek.util.function.CommonUtil;
import com.xx.chinetek.util.function.DoubleClickCheck;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;
import com.xx.chinetek.model.WMS.Stock.AreaInfo_Model;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_line_stock_out_product)
public class LineStockOutProduct extends BaseActivity {

    String TAG_GetT_PalletDetailByBarCodeADF="LineStockOutProduct_GetT_ScanInStockModelADF";
    private final int RESULT_Msg_GetT_PalletDetailByBarCode=102;

    String TAG_GetT_SaveBarCodeADF="LineStockOutProduct_GetT_SaveInStockModelADF";
    private final int RESULT_Msg_GetT_SaveBarCode=103;




    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetT_PalletDetailByBarCode:
                AnalysisetT_PalletDetailByBarCodeJson((String) msg.obj);
                break;
            case RESULT_Msg_GetT_SaveBarCode:
                AnalysisetT_SaveBarCodeJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
                break;
        }
    }


    Context context=LineStockOutProduct.this;

    @ViewInject(R.id.lsv_LineStockOutProduct)
    ListView lsvLineStockOutProduct;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_EDate)
    TextView txtEDate;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.txt_LineOutStockNum)
    TextView txtLineOutStockNum;
    @ViewInject(R.id.edt_LineStockOutScanBarcode)
    EditText edtLineStockOutScanBarcode;

    ArrayList<BarCodeInfo> SumbitbarCodeInfos=new ArrayList<>();
    PalletItemAdapter palletItemAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.Product_ProductStockout_subtitleYMH), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }



    @Event(value =R.id.edt_LineStockOutScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtLineStockOutScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String code = edtLineStockOutScanBarcode.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
                return true;
            }
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            LogUtil.WriteLog(ReceiptionScan.class, TAG_GetT_PalletDetailByBarCodeADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null,  URLModel.GetURL().GetPalletDetailByBarCodeForStockOut, params, null);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            if (DoubleClickCheck.isFastDoubleClick(context)) {
                return false;
            }
            //提交
            if(SumbitbarCodeInfos!=null && SumbitbarCodeInfos.size()!=0){
                final Map<String, String> params = new HashMap<String, String>();
                params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
                params.put("ModelJson", GsonUtil.parseModelToJson(SumbitbarCodeInfos));
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_SaveBarCodeADF, getString(R.string.Msg_GetT_SaveStouckOutADF),
                        context, mHandler, RESULT_Msg_GetT_SaveBarCode, null,  URLModel.GetURL().Save_StockOutADF, params, null);

            }else{
                MessageBox.Show(context,"没有需要出库的成品条码！");
            }
        }
        return super.onOptionsItemSelected(item);
    }


    void AnalysisetT_SaveBarCodeJson(String result){
        LogUtil.WriteLog(LineStockInProduct.class, TAG_GetT_SaveBarCodeADF,result);
        ReturnMsgModelList<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<String>>() {}.getType());
        try {
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ClearFrm();
                MessageBox.Show(context,"提交成功！");
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
        CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
    }






    void AnalysisetT_PalletDetailByBarCodeJson(String result){
        LogUtil.WriteLog(LineStockInProduct.class, TAG_GetT_PalletDetailByBarCodeADF,result);
        ReturnMsgModelList<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<BarCodeInfo>>() {}.getType());
        try {
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ArrayList<BarCodeInfo> barCodeInfos = returnMsgModel.getModelJson();
                Bindbarcode(barCodeInfos);

            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
        CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
    }

    void Bindbarcode(final ArrayList<BarCodeInfo> barCodeInfos){
        if (barCodeInfos != null && barCodeInfos.size() != 0) {

            try {
                if(SumbitbarCodeInfos.indexOf(barCodeInfos.get(0))!=-1){
                    MessageBox.Show(context,getString(R.string.Error_Barcode_hasScan));
                    return;
                }
                float sumAll=0;
                for (BarCodeInfo barCodeInfo : barCodeInfos) {
                    SumbitbarCodeInfos.add(0,barCodeInfo);
                }
                for (BarCodeInfo barCodeInfo : SumbitbarCodeInfos) {
                    sumAll= ArithUtil.add(sumAll,barCodeInfo.getQty());
                }
                txtLineOutStockNum.setText(String.valueOf(sumAll));
                InitFrm(barCodeInfos.get(0));
                BaseApplication.userInfo.setEmail(barCodeInfos.get(0).getErpVoucherNo());//ymh 加字段
                BindListVIew(SumbitbarCodeInfos);
            }catch (Exception ex){
                MessageBox.Show(context,ex.getMessage());
                CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
            }

        }
    }

    void InitFrm(BarCodeInfo barCodeInfo){
        try {
            if (barCodeInfo != null) {
                txtCompany.setText(barCodeInfo.getStrongHoldName());
                txtBatch.setText(barCodeInfo.getBatchNo());
                txtStatus.setText("");
                txtMaterialName.setText(barCodeInfo.getMaterialDesc());
                txtEDate.setText(CommonUtil.DateToString(barCodeInfo.getEDate()));
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
            CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
        }
    }

    private void BindListVIew(ArrayList<BarCodeInfo> barCodeInfos) {
        palletItemAdapter=new PalletItemAdapter(context,barCodeInfos);
        lsvLineStockOutProduct.setAdapter(palletItemAdapter);
    }



    void ClearFrm(){
        SumbitbarCodeInfos = new ArrayList<>();
        edtLineStockOutScanBarcode.setText("");
        txtLineOutStockNum.setText("");
        txtCompany.setText("");
        txtBatch.setText("");
        txtEDate.setText("");
        txtStatus.setText("");
        txtMaterialName.setText("");
        BindListVIew(SumbitbarCodeInfos);
    }

}
