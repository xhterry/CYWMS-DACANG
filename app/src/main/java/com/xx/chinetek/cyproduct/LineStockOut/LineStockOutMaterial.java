package com.xx.chinetek.cyproduct.LineStockOut;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.product.Manage.WoDetailMaterialItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cyproduct.Adjust.AdjustCP;
import com.xx.chinetek.cyproduct.Manage.ProductMaterialConfig;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.CheckNumRefMaterial;
import com.xx.chinetek.model.Production.Wo.WoDetailModel;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.UerInfo;
import com.xx.chinetek.model.User.UserInfo;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.ArithUtil;
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
import java.util.Map;

@ContentView(R.layout.activity_line_stock_out_material)
public class LineStockOutMaterial extends BaseActivity {

    String TAG_GetWoDetailModelByWoNo="LineStockOutMaterial_GetWoDetailModelByWoNo";
    String TAG_GetStockModelADF="LineStockOutMaterial_GetStockModelADF";
    String TAG_SaveT_BarCodeToStockADF="LineStockOutMaterial_SaveT_BarCodeToStockADF";
    private final int RESULT_GetWoDetailModelByWoNo=101;
    private final int RESULT_Msg_GetStockModelADF=102;
    private final int RESULT_SaveT_BarCodeToStockADF = 104;

    String TAG_SaveBarcodeListOutStockForLingLiao="LineStockOutMaterial_SaveBarcodeListOutStockForLingLiaoADF";
    private final int RESULT_SaveBarcodeListOutStockForLingLiao=105;





    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_SaveBarcodeListOutStockForLingLiao:
                AnalysisSaveBarcodeListOutStockForLingLiao((String) msg.obj);
                break;
            case RESULT_GetWoDetailModelByWoNo:
                AnalysisGetWoDetailModelByWoNoJson((String) msg.obj);
                break;
            case RESULT_Msg_GetStockModelADF:
                AnalysisGetStockModelADFJson((String) msg.obj);
                break;
            case RESULT_SaveT_BarCodeToStockADF:
                AnalysisSaveT_BarCodeToStockADF((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }

    @ViewInject(R.id.lsv_LineStockOutMaterial)
    ListView lsv_LineStockOutMaterial;
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
    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVoucherNo;
    @ViewInject(R.id.tb_UnboxType)
    ToggleButton tbUnboxType;
    @ViewInject(R.id.tb_PalletType)
    ToggleButton tbPalletType;
    @ViewInject(R.id.tb_BoxType)
    ToggleButton tbBoxType;
    @ViewInject(R.id.edt_LineStockOutScanBarcode)
    EditText edtLineStockOutScanBarcode;
    @ViewInject(R.id.edt_Unboxing)
    EditText edtUnboxing;
    @ViewInject(R.id.txt_Unboxing)
    TextView txtUnboxing;

    Context context=LineStockOutMaterial.this;
    ArrayList<WoDetailModel> woDetailModels;
    WoDetailMaterialItemAdapter woDetailMaterialItemAdapter;
    ArrayList<StockInfo_Model> stockInfoModels;//扫描条码

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    WoModel woModel;

    @Override
    protected void initData() {
        super.initData();
        woModel=getIntent().getParcelableExtra("woModel");
        GetWoDetailModelByWoNo(woModel);
    }

    @Event(value ={R.id.tb_UnboxType,R.id.tb_PalletType,R.id.tb_BoxType} ,type = CompoundButton.OnClickListener.class)
    private void TBonCheckedChanged(View view) {
        tbUnboxType.setChecked(view.getId()== R.id.tb_UnboxType);
        tbPalletType.setChecked(view.getId()== R.id.tb_PalletType);
        tbBoxType.setChecked(view.getId()== R.id.tb_BoxType);
        ShowUnboxing(view.getId()== R.id.tb_UnboxType);
        stockInfoModels=new ArrayList<>();
    }


    @Event(value =R.id.edt_LineStockOutScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtLineStockOutScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String code=edtLineStockOutScanBarcode.getText().toString().trim();
            int type=tbPalletType.isChecked()?1:(tbBoxType.isChecked()?2:3);
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            params.put("ScanType", type+"");
            params.put("MoveType", "1"); //1：下架 2:移库
            params.put("IsEdate", "2"); //1：不判断有效期 2:判断有效期
            LogUtil.WriteLog(LineStockOutMaterial.class, TAG_GetStockModelADF, code);
//            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetStockModelADF, null, URLModel.GetURL().GetStockModelADF, params, null);
            if(woModel.getStrVoucherType().equals("散装物料")){
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetStockModelADF, null, URLModel.GetURL().GetStockModelADF, params, null);
            }else{
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetStockModelADF, null, URLModel.GetURL().GetStockModelADF_Product, params, null);
            }

        }
        return false;
    }

    @Event(value =R.id.edt_Unboxing,type = View.OnKeyListener.class)
    private  boolean edtUnboxingClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String num=edtUnboxing.getText().toString().trim();
            if (stockInfoModels != null && stockInfoModels.size() != 0) {
                CheckNumRefMaterial checkNumRefMaterial = CheckMaterialNumFormat(num, stockInfoModels.get(0).getUnitTypeCode(), stockInfoModels.get(0).getDecimalLngth());
                if (!checkNumRefMaterial.ischeck()) {
                    MessageBox.Show(context, checkNumRefMaterial.getErrMsg());
                    CommonUtil.setEditFocus(edtUnboxing);
                    return true;
                }
                Float qty = checkNumRefMaterial.getCheckQty(); //输入数量
                Float scanQty = stockInfoModels.get(0).getQty(); //箱数量
                if (qty > scanQty) {
                    MessageBox.Show(context, getString(R.string.Error_PackageQtyBiger));
                    CommonUtil.setEditFocus(edtUnboxing);
                    return true;
                }
                WoDetailModel tempWodetail=new WoDetailModel(stockInfoModels.get(0).getMaterialNo());
                final int index=woDetailModels.indexOf(tempWodetail);
                if(index!=-1) {

//                    if (ArithUtil.add(woDetailModels.get(index).getScanQty(), qty) > woDetailModels.get(index).getRemainQty()) {
//                        MessageBox.Show(context, getString(R.string.Error_OutMaterialQtyBiger));
//                        return true;
//                    }
                }
                //拆零
                stockInfoModels.get(0).setPickModel(3);
                stockInfoModels.get(0).setAmountQty(qty);
                String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
                String strOldBarCode = GsonUtil.parseModelToJson(stockInfoModels.get(0));
                final Map<String, String> params = new HashMap<String, String>();
                params.put("UserJson", userJson);
                params.put("strOldBarCode", strOldBarCode);
                params.put("strNewBarCode", "");
                params.put("PrintFlag", "1"); //1：打印 2：不打印
                LogUtil.WriteLog(LineStockOutMaterial.class, TAG_SaveT_BarCodeToStockADF, strOldBarCode);
//              RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_BarCodeToStockADF, getString(R.string.Msg_SaveT_BarCodeToStockADF), context, mHandler, RESULT_SaveT_BarCodeToStockADF, null, URLModel.GetURL().SaveT_BarCodeToStockADF, params, null);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_BarCodeToStockADF, getString(R.string.Msg_SaveT_BarCodeToStockADF), context, mHandler, RESULT_SaveT_BarCodeToStockADF, null, URLModel.GetURL().SaveNewBarcodeToStockForChaiXiang, params, null);

            } else {
                MessageBox.Show(context, getString(R.string.Hit_ScanBarcode));
                edtUnboxing.setText("");
                CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
                return true;
            }
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
            try {
                Map<String, String> params = new HashMap<>();
                UerInfo User = BaseApplication.userInfo;
                User.setErpVoucherNo(woModel.getErpVoucherNo().toString());
                User.setSex(woModel.getID());
                String userJson = GsonUtil.parseModelToJson(User);
                ArrayList<StockInfo_Model> allstocks = new ArrayList<>();
                if(woDetailModels!=null){
                    for (int i=0;i<woDetailModels.size();i++){
                        if(woDetailModels.get(i).getStockInfoModels()!=null){
                            for (int j=0;j<woDetailModels.get(i).getStockInfoModels().size();j++){
                                allstocks.add(woDetailModels.get(i).getStockInfoModels().get(j));
                            }
                        }
                    }
                }
                String ModelJson = GsonUtil.parseModelToJson(allstocks);
                params.put("UserJson", userJson);
                params.put("ModelJson", ModelJson);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveBarcodeListOutStockForLingLiao, getString(R.string.Mag_GetWoDetailModelByWoNo), context, mHandler, RESULT_SaveBarcodeListOutStockForLingLiao, null, URLModel.GetURL().SaveBarcodeListOutStockForLingLiao, params, null);
            } catch (Exception ex) {
                MessageBox.Show(context, ex.getMessage());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    void GetWoDetailModelByWoNo(WoModel woModel){
        if(woModel!=null) {
            txtVoucherNo.setText(woModel.getErpVoucherNo());
            try {
                Map<String, String> params = new HashMap<>();
                params.put("HeadId", woModel.getID() + "");
                LogUtil.WriteLog(LineStockOutMaterial.class, TAG_GetWoDetailModelByWoNo, woModel.getID() + "");
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetWoDetailModelByWoNo, getString(R.string.Mag_GetWoDetailModelByWoNo), context, mHandler, RESULT_GetWoDetailModelByWoNo, null, URLModel.GetURL().GetWoDetailModelByWoNo, params, null);
            } catch (Exception ex) {
                MessageBox.Show(context, ex.getMessage());
            }
        }
    }

    void  AnalysisSaveBarcodeListOutStockForLingLiao(String result){
        try {
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {}.getType());
            MessageBox.Show(context,returnMsgModel.getMessage());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ClearFrm();
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
    }

    //清空数据
    void ClearFrm(){
        if(woDetailModels!=null){
            for (int i=0;i<woDetailModels.size();i++){
                woDetailModels.get(i).setStockInfoModels(new ArrayList<StockInfo_Model>());
                woDetailModels.get(i).setScanQty(0f);
            }
        }
        BindListview(woDetailModels);
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

    /*
   扫描条码
    */
    void AnalysisGetStockModelADFJson(String result) {
        LogUtil.WriteLog(LineStockOutMaterial.class, TAG_GetStockModelADF, result);
        try {
            ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                stockInfoModels = returnMsgModel.getModelJson();
                if (stockInfoModels != null && stockInfoModels.size() != 0) {
                    if (tbUnboxType.isChecked()) {//拆零
                        CommonUtil.setEditFocus(edtUnboxing );
                        return;
                    }
                    Float SumQty=0f;
                    for (StockInfo_Model stockInfoModel:stockInfoModels) {
                        SumQty= ArithUtil.add(SumQty,stockInfoModel.getQty());
                    }
                    Bindbarcode(stockInfoModels,SumQty);
                }
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
                CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
            CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
        }

    }

    /*
 拆箱提交
  */
    void AnalysisSaveT_BarCodeToStockADF(String result){
        try {
            LogUtil.WriteLog(LineStockOutMaterial.class, TAG_SaveT_BarCodeToStockADF, result);
            ReturnMsgModel<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<StockInfo_Model>>() {
            }.getType());
            edtLineStockOutScanBarcode.setText("");
            if(returnMsgModel.getHeaderStatus().equals("S")){
                StockInfo_Model stockInfoModel=returnMsgModel.getModelJson();
                stockInfoModels=new ArrayList<>();
                stockInfoModels.add(stockInfoModel);
                Bindbarcode(stockInfoModels,stockInfoModel.getQty());
            }
            else{
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        edtUnboxing.setText("");
        CommonUtil.setEditFocus(edtLineStockOutScanBarcode);

    }

    void Bindbarcode(final ArrayList<StockInfo_Model> stockInfoModels,Float SumQty){
        if (stockInfoModels != null && stockInfoModels.size() != 0) {
            try {
                String MaterialNo=stockInfoModels.get(0).getMaterialNo();

                final Float sumQty=SumQty;
                WoDetailModel tempWodetail=new WoDetailModel(MaterialNo);
                final int index=woDetailModels.indexOf(tempWodetail);
                if(index!=-1){
                    if(woDetailModels.get(index).getStockInfoModels()==null)
                        woDetailModels.get(index).setStockInfoModels(new ArrayList<StockInfo_Model>());
                    if(woDetailModels.get(index).getStockInfoModels().indexOf(stockInfoModels.get(0))!=-1){
                        //MessageBox.Show(context,getString(R.string.Error_Barcode_hasScan));
                        new AlertDialog.Builder(context) .setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除已扫描条码？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO 自动生成的方法
                                        woDetailModels.get(index).getStockInfoModels().removeAll(stockInfoModels);
                                        woDetailModels.get(index).setScanQty(ArithUtil.sub(woDetailModels.get(index).getScanQty(),sumQty));
                                        InitFrm(stockInfoModels.get(0));
                                        BindListview(woDetailModels);
                                    }
                                }).setNegativeButton("取消", null).show();
                        return;
                    }
//                    if(ArithUtil.add(woDetailModels.get(index).getScanQty(),SumQty)>woDetailModels.get(index).getRemainQty()){
//                        MessageBox.Show(context,getString(R.string.Error_OutMaterialQtyBiger));
//                        return;
//                    }


                    woDetailModels.get(index).setScanQty(ArithUtil.add(woDetailModels.get(index).getScanQty(),SumQty));
                    woDetailModels.get(index).getStockInfoModels().addAll(0,stockInfoModels);
                }else{
                    MessageBox.Show(context,getString(R.string.Error_BarcodeNotInList));
                    return;
                }
                InitFrm(stockInfoModels.get(0));
                BindListview(woDetailModels);
            }catch (Exception ex){
                MessageBox.Show(context,ex.getMessage());
                CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
            }

        }
    }

    void InitFrm(StockInfo_Model stockInfoModel){
        if(stockInfoModel!=null ){
            txtCompany.setText(stockInfoModel.getStrongHoldName());
            txtBatch.setText(stockInfoModel.getBatchNo());
            txtStatus.setText(stockInfoModel.getStrStatus());
            txtMaterialName.setText(stockInfoModel.getMaterialDesc());
            txtEDate.setText(CommonUtil.DateToString(stockInfoModel.getEDate()));
        }
        CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
    }

    void BindListview(ArrayList<WoDetailModel> woDetailModels){
        woDetailMaterialItemAdapter=new WoDetailMaterialItemAdapter(context,woDetailModels);
        lsv_LineStockOutMaterial.setAdapter(woDetailMaterialItemAdapter);
    }

    void ShowUnboxing(Boolean show){
        int visiable=show? View.VISIBLE:View.GONE;
        txtUnboxing.setVisibility(visiable);
        edtUnboxing.setVisibility(visiable);
    }

}
