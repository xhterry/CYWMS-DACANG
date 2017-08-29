package com.xx.chinetek.cyproduct.LineStockIn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.xx.chinetek.adapter.product.LineStockIn.LineStockInMaterialItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.cywms.Receiption.ReceiptionScan;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.WareHouseInfo;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.CommonUtil;
import com.xx.chinetek.util.function.DoubleClickCheck;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_product_linestockinproduct_scan)
public class LineStockInProduct extends BaseActivity {

    String TAG_GetPalletDetailByBarCode_Product="LineStockInProduct_GetPalletDetailByBarCode_Product";

    private final int RESULT_Msg_GetPalletDetailByBarCode_Product=102;


    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
                     case RESULT_Msg_GetPalletDetailByBarCode_Product:
                AnalysisetGetPalletDetailByBarCode_ProductJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtLineStockInScanBarcode);
                break;
        }
    }

    Context context = LineStockInProduct.this;
    @ViewInject(R.id.lsv_LineStockInProduct)
    ListView lsvLineStockInProduct;
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
    @ViewInject(R.id.txt_WareHousName)
    TextView txtWareHousName;
    @ViewInject(R.id.edt_LineStockInScanBarcode)
    EditText edtLineStockInScanBarcode;

    ArrayList<BarCodeInfo> SumbitbarCodeInfos=null;
    LineStockInMaterialItemAdapter lineStockInMaterialItemAdapter;


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.Product_ProductStockin_subtitle), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Override
    protected void initData() {
        super.initData();
        SelectWareHouse();
        SumbitbarCodeInfos=new ArrayList<>();
        CommonUtil.setEditFocus(edtLineStockInScanBarcode);
    }

    @Event(value =R.id.edt_LineStockInScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtLineStockInScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String code = edtLineStockInScanBarcode.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                CommonUtil.setEditFocus(edtLineStockInScanBarcode);
                return true;
            }
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            LogUtil.WriteLog(ReceiptionScan.class, TAG_GetPalletDetailByBarCode_Product, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetPalletDetailByBarCode_Product, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetPalletDetailByBarCode_Product, null,  URLModel.GetURL().GetPalletDetailByBarCode_Product, params, null);
        }
        return false;
    }

    @Event(R.id.txt_WareHousName)
    private void txtWareHousNameClick(View view){
        SelectWareHouse();
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

            }
        }
        return super.onOptionsItemSelected(item);
    }


    /*
   扫描条码
    */
    void AnalysisetGetPalletDetailByBarCode_ProductJson(String result){
        LogUtil.WriteLog(LineStockInProduct.class, TAG_GetPalletDetailByBarCode_Product,result);
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
        CommonUtil.setEditFocus(edtLineStockInScanBarcode);
    }

    void Bindbarcode(final ArrayList<BarCodeInfo> barCodeInfos){
        if (barCodeInfos != null && barCodeInfos.size() != 0) {
            try {
                if(SumbitbarCodeInfos.indexOf(barCodeInfos.get(0))!=-1){
                    MessageBox.Show(context,getString(R.string.Error_Barcode_hasScan));
                    return;
                }
                for (BarCodeInfo barCodeInfo : barCodeInfos) {
                    SumbitbarCodeInfos.add(0,barCodeInfo);
                }
                InitFrm(barCodeInfos.get(0));
                BindListVIew(SumbitbarCodeInfos);
            }catch (Exception ex){
                MessageBox.Show(context,ex.getMessage());
                CommonUtil.setEditFocus(edtLineStockInScanBarcode);
            }

        }
    }


    /*
   提交入库
    */
//    void AnalysisSaveT_InStockTaskDetailADFJson(String result){
//        try {
//            LogUtil.WriteLog(LineStockInProduct.class, TAG_SaveT_InStockTaskDetailADF,result);
//            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
//            }.getType());
//            MessageBox.Show(context, returnMsgModel.getMessage());
//            if(returnMsgModel.getHeaderStatus().equals("S")) {
//                ClearFrm();
//                GetInStockTaskDetail(inStockTaskInfoModel);
//            }
//            CommonUtil.setEditFocus(edtUpShelfScanBarcode);
//        } catch (Exception ex) {
//            MessageBox.Show(context, ex.getMessage());
//        }
//    }

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
            CommonUtil.setEditFocus(edtLineStockInScanBarcode);
        }
    }

    private void BindListVIew(ArrayList<BarCodeInfo> barCodeInfos) {
        lineStockInMaterialItemAdapter=new LineStockInMaterialItemAdapter(context,barCodeInfos);
        lsvLineStockInProduct.setAdapter(lineStockInMaterialItemAdapter);
    }



    void ClearFrm(){
        SumbitbarCodeInfos = new ArrayList<>();
        edtLineStockInScanBarcode.setText("");
        txtCompany.setText("");
        txtBatch.setText("");
        txtEDate.setText("");
        txtStatus.setText("");
        txtMaterialName.setText("");
        BindListVIew(SumbitbarCodeInfos);
    }


    int  SelectWareHouseID=-1;
    void SelectWareHouse(){
        if (BaseApplication.userInfo==null || BaseApplication.userInfo.getLstWarehouse() == null) return;
        List<String> wareHouses = new ArrayList<String>();
        if(BaseApplication.userInfo.getLstWarehouse().size()>1) {
            for (WareHouseInfo warehouse : BaseApplication.userInfo.getLstWarehouse()) {
                if (warehouse.getWareHouseName() != null && !warehouse.getWareHouseName().equals("")) {
                    wareHouses.add(warehouse.getWareHouseName());
                }
            }
            final String[] items = wareHouses.toArray(new String[0]);
            new AlertDialog.Builder(context).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法存根
                            String select_item = items[which].toString();
                            SelectWareHouseID = BaseApplication.userInfo.getLstWarehouse().get(which).getID();
                            txtWareHousName.setText(select_item);
                            BaseApplication.userInfo.setWarehouseID(SelectWareHouseID);
                            dialog.dismiss();
                        }
                    }).show();
        }else{
            SelectWareHouseID = BaseApplication.userInfo.getLstWarehouse().get(0).getID();
            txtWareHousName.setText(BaseApplication.userInfo.getLstWarehouse().get(0).getWareHouseName());
            BaseApplication.userInfo.setWarehouseID(SelectWareHouseID);
        }
    }




}
