package com.xx.chinetek.cyproduct.Manage;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.product.Manage.WoDetailMaterialItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Production.Manage.LineManageModel;
import com.xx.chinetek.model.Production.Wo.WoDetailModel;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.ArithUtil;
import com.xx.chinetek.util.function.CommonUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.xx.chinetek.cywms.R.id.edt_ScanQty;

@ContentView(R.layout.activity_product_material_config)
public class ProductMaterialConfig extends BaseActivity {

    String TAG_GetWoDetailModelByWoNo="ProductMaterialConfig_GetWoDetailModelByWoNo";
    String TAG_GetStockModelADF="ProductMaterialConfig_GetMaterialByBarcode";
    private final int RESULT_GetWoDetailModelByWoNo=101;
    private final int RESULT_Msg_GetStockModelADF=102;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetWoDetailModelByWoNo:
                AnalysisGetWoDetailModelByWoNoJson((String) msg.obj);
                break;
            case RESULT_Msg_GetStockModelADF:
                AnalysisGetStockModelADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }


    Context context=ProductMaterialConfig.this;
    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVoucherNo;
    @ViewInject(R.id.txt_BatchNo)
    TextView txtBatchNo;
    @ViewInject(R.id.txt_ProductLineNo)
    TextView txtProductLineNo;
    @ViewInject(R.id.txt_ProductStartTime)
    TextView txtProductStartTime;
    @ViewInject(R.id.txt_MaterialDesc)
    TextView txtMaterialDesc;
    @ViewInject(R.id.edt_PrePruductNum)
    EditText edtPrePruductNum;
    @ViewInject(R.id.edt_Barcode)
    EditText edtBarcode;
    @ViewInject(R.id.edt_ScanQty)
    EditText edtScanQty;
    @ViewInject(R.id.lsv_Material)
    ListView lsvMaterial;
    @ViewInject(R.id.btn_StartProduct)
    Button btnStartProduct;

    LineManageModel lineManageModel;
    WoModel woModel;
    ArrayList<WoDetailModel> woDetailModels;
    WoDetailMaterialItemAdapter woDetailMaterialItemAdapter;
    BarCodeInfo currentBarCodeInfo;//当前扫描物料
    int currentIndex=-1;//当前扫描物料对应工单物料
    int mHour, mMinute;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_MaterialConfig_subtitle), true);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        this.lineManageModel=getIntent().getParcelableExtra("lineManageModel");
        this.woModel=getIntent().getParcelableExtra("woModel");
        if(lineManageModel!=null && woModel!=null){
            txtVoucherNo.setText(woModel.getErpVoucherNo());
            txtBatchNo.setText(woModel.getBatchNo());
            txtProductLineNo.setText(lineManageModel.getProductLineNo());
            txtMaterialDesc.setText(woModel.getMaterialDesc());
            GetWoDetailModelByWoNo(woModel);
        }
    }


    @Event(value = R.id.edt_PrePruductNum,type = View.OnKeyListener.class)
    private  boolean edtPrePruductNumClick(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String preProductNum=edtPrePruductNum.getText().toString().trim();
            if(CommonUtil.isFloat(preProductNum)){
                lineManageModel.setPreProductNum(Float.parseFloat(preProductNum));
                edtPrePruductNum.setEnabled(false);
                CommonUtil.setEditFocus(edtBarcode);
            }else{
                MessageBox.Show(context,getString(R.string.Error_isnotnum));
                CommonUtil.setEditFocus(edtPrePruductNum);
                return true;
            }
        }
        return false;
    }

    @Event(value = R.id.edt_Barcode,type = View.OnKeyListener.class)
    private  boolean edtBarcodeClick(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String barcode=edtBarcode.getText().toString().trim();
            if(!TextUtils.isEmpty(barcode)){
                try {
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("BarCode", barcode);
                    params.put("ScanType", "2");
                    params.put("MoveType", "1"); //1：下架 2:移库
                    params.put("IsEdate", "2"); //1：不判断有效期 2:判断有效期
                    LogUtil.WriteLog(ProductMaterialConfig.class, TAG_GetStockModelADF, barcode);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetStockModelADF, null, URLModel.GetURL().GetStockModelADF, params, null);
                } catch (Exception ex) {
                    MessageBox.Show(context, ex.getMessage());
                }
            }
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            edtScanQty.setText("");
            edtBarcode.setText("");
            edtPrePruductNum.setEnabled(true);
            CommonUtil.setEditFocus(edtPrePruductNum);
            return true;

        }
        return false;
    }

    @Event(value = edt_ScanQty,type = View.OnKeyListener.class)
    private  boolean edtScanQtyClick(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String scanQty=edtScanQty.getText().toString().trim();
            if(CommonUtil.isFloat(scanQty)){
                if(currentBarCodeInfo!=null) {
                    Float qty = woDetailModels.get(currentIndex).getScanQty() + Float.parseFloat(scanQty) * currentBarCodeInfo.getOutPackQty();
                    if (qty <= woDetailModels.get(currentIndex).getWoQty()) {
                        woDetailModels.get(currentIndex).setScanQty(qty);
                        BindListview(woDetailModels);
                        edtScanQty.setText("");
                        CommonUtil.setEditFocus(edtBarcode);
                    } else {
                        MessageBox.Show(context, getString(R.string.Error_PackageQtyBigerThenWo));
                        CommonUtil.setEditFocus(edtScanQty);
                    }
                }

            }else{
                MessageBox.Show(context,getString(R.string.Error_isnotnum));
                CommonUtil.setEditFocus(edtScanQty);
                return true;
            }
        }
        return false;
    }

    @Event(value = R.id.txt_ProductStartTime,type = View.OnClickListener.class )
    private void txtProductStartTimeClick(View view){
        final Calendar ca = Calendar.getInstance();
        mHour = ca.get(Calendar.HOUR_OF_DAY);
        mMinute = ca.get(Calendar.MINUTE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour=hourOfDay;
                mMinute=minute;
                txtProductStartTime.setText(display());
                lineManageModel.setStartTime(display());
            }
        },mHour,mMinute,true).show();
    }

    @Event(R.id.btn_StartProduct)
    private void btnStartProductClick(View view){
        //判断是否满足齐套扫描要求
        //提交数据
    }

    void GetWoDetailModelByWoNo(WoModel woModel){
        try {
           // String ModelJson = GsonUtil.parseModelToJson(woModel);
            Map<String, String> params = new HashMap<>();
           // params.put("UserJson", GsonUtil.parseModelToJson(userInfo));
            params.put("HeadId", woModel.getID()+"");
            LogUtil.WriteLog(ProductMaterialConfig.class, TAG_GetWoDetailModelByWoNo, woModel.getID()+"");
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetWoDetailModelByWoNo, getString(R.string.Mag_GetWoDetailModelByWoNo), context, mHandler, RESULT_GetWoDetailModelByWoNo, null,  URLModel.GetURL().GetWoDetailModelByWoNo, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void  AnalysisGetWoDetailModelByWoNoJson(String result){
        try {
            LogUtil.WriteLog(ProductMaterialConfig.class, TAG_GetWoDetailModelByWoNo, result);
            ReturnMsgModelList<WoDetailModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<WoDetailModel>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                woDetailModels = returnMsgModel.getModelJson();
                if (woDetailModels != null ){
                    BindListview(woDetailModels);
                }

            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){

            MessageBox.Show(context,ex.getMessage());
        }
    }

    void AnalysisGetStockModelADFJson(String result){
        try {
            LogUtil.WriteLog(ProductMaterialConfig.class, TAG_GetStockModelADF, result);
            try {
                ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {
                }.getType());
                if (returnMsgModel.getHeaderStatus().equals("S")) {
                   ArrayList<StockInfo_Model> stockInfoModels = returnMsgModel.getModelJson();
                    if (stockInfoModels != null && stockInfoModels.size() != 0) {
                        //判断条码是否已经扫描
                        StockInfo_Model stockInfoModel = stockInfoModels.get(0);
                        WoDetailModel woDetailModel = new WoDetailModel(stockInfoModel.getMaterialNo());
                        int woDetailindex = woDetailModels.indexOf(woDetailModel);
                        if (woDetailindex == -1) {
                            MessageBox.Show(context, getString(R.string.Error_ErpvoucherNoMatch));
                            CommonUtil.setEditFocus(edtBarcode);
                            return;
                        }
                        if (woDetailModels.get(woDetailindex).getStockInfoModels() == null)
                            woDetailModels.get(woDetailindex).setStockInfoModels(new ArrayList<StockInfo_Model>());
                        if ( woDetailModels.get(woDetailindex).getStockInfoModels().indexOf(stockInfoModel)!= -1) {
                            MessageBox.Show(context, getString(R.string.Error_Barcode_hasScan));
                            CommonUtil.setEditFocus(edtBarcode);
                            return;
                        }
                        Float scanQty=woDetailModels.get(woDetailindex).getScanQty();
                        Float StockQty=stockInfoModel.getQty();
                        Float WoQty=woDetailModels.get(woDetailindex).getWoQty();
                        Float Qty=ArithUtil.add(scanQty,StockQty);
                        if(WoQty<Qty){
                            MessageBox.Show(context, getString(R.string.Error_PackageQtyBigerThenWo));
                            CommonUtil.setEditFocus(edtBarcode);
                            return;
                        }
                        woDetailModels.get(woDetailindex).setScanQty(Qty);
                        woDetailModels.get(woDetailindex).getStockInfoModels().add(0,stockInfoModel);
                        BindListview(woDetailModels);
                    }
                } else {
                    MessageBox.Show(context, returnMsgModel.getMessage());
                    CommonUtil.setEditFocus(edtBarcode);
                }
            } catch (Exception ex) {
                MessageBox.Show(context, ex.getMessage());
                CommonUtil.setEditFocus(edtBarcode);
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
            CommonUtil.setEditFocus(edtBarcode);
        }
    }

    void BindListview(ArrayList<WoDetailModel> woDetailModels){
        woDetailMaterialItemAdapter=new WoDetailMaterialItemAdapter(context,woDetailModels);
        lsvMaterial.setAdapter(woDetailMaterialItemAdapter);
    }


    public String  display() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String day=format.format(new Date());
        return new StringBuffer().append(day).append(" ").append(mHour<10?"0"+mHour:mHour).append(":").append(mMinute<10?"0"+mMinute:mMinute).toString();
    }
}
