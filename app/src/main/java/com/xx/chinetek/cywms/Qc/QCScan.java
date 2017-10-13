package com.xx.chinetek.cywms.Qc;

import android.content.Context;
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
import com.xx.chinetek.adapter.wms.QC.QCDetailAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.OffShelf.OffshelfScan;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.CheckNumRefMaterial;
import com.xx.chinetek.model.QC.QualityDetailInfo_Model;
import com.xx.chinetek.model.QC.QualityInfo_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;
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

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xx.chinetek.cywms.R.id.edt_unboxing;
import static com.xx.chinetek.util.function.GsonUtil.parseModelToJson;


@ContentView(R.layout.activity_qcscan)
public class QCScan extends BaseActivity {

    String TAG_GetT_QualityDetailListByHeaderIDADF="QCScan_GetT_QualityDetailListByHeaderIDADF";
    String TAG_GetT_OutBarCodeInfoForQuanADF="QCScan_GetT_OutBarCodeInfoForQuanADF";
    String TAG_SaveT_QuanlitySampADF="QCScan_SaveT_QuanlitySampADF";
    String TAG_SaveT_BarCodeToStockADF="QCScan_SaveT_BarCodeToStockADF";
    String TAG_PrintQYAndroid = "QCScan_PrintQYAndroid";

    private final int RESULT_Msg_GetT_QualityDetailListByHeaderIDADF=101;
    private final int RESULT_Msg_GetT_OutBarCodeInfoForQuanADF=102;
    private final int RESULT_Msg_SaveT_QuanlitySampADF=103;
    private final int RESULT_SaveT_BarCodeToStockADF = 104;
    private final int RESULT_PrintQYAndroid = 105;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetT_QualityDetailListByHeaderIDADF:
                AnalysisGetT_QualityDetailListByHeaderIDADFJson((String) msg.obj);
                break;
            case RESULT_Msg_GetT_OutBarCodeInfoForQuanADF:
                AnalysisGetT_OutBarCodeInfoForQuanADFJson((String) msg.obj);
                break;
            case RESULT_Msg_SaveT_QuanlitySampADF:
                AnalysisSaveT_QuanlitySampADFJson((String) msg.obj);
                break;
            case RESULT_SaveT_BarCodeToStockADF:
                AnalysisSaveT_BarCodeToStockADF((String) msg.obj);
                break;
            case RESULT_PrintQYAndroid:
                AnalysisPrintQYAndroidJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtQCScanBarcode);
                break;
        }
    }

    Context context=QCScan.this;
    @ViewInject(R.id.lsv_QCScan)
    ListView lsvQCScan;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Stock)
    TextView txtStock;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_EDate)
    TextView txtEDate;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.txt_QCMaterialName)
    TextView txtQCMaterialName;
    @ViewInject(R.id.tb_unboxType)
    ToggleButton TBunboxType;
    @ViewInject(R.id.edt_QCScanBarcode)
    EditText edtQCScanBarcode;
    @ViewInject(edt_unboxing)
    EditText edtunboxing;
    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVoucherNo;
    @ViewInject(R.id.txt_SampleNum)
    TextView txtSampleNum;
    @ViewInject(R.id.txt_QCSampleNum)
    TextView txtQCSampleNum;
    @ViewInject(R.id.txt_unboxing)
    TextView txtunboxing;
    @ViewInject(R.id.txt_ScanQty)
    TextView  txtScanQty;

    QualityInfo_Model qualityInfoModel;
    ArrayList<QualityDetailInfo_Model> qualityDetailInfoModels;
    QCDetailAdapter qcDetailAdapter;
    StockInfo_Model stockInfoModel;//扫描 条码


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.QC_subtitle), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Override
    protected void initData() {
        super.initData();
        TBunboxType.setChecked(true);
        qualityInfoModel=getIntent().getParcelableExtra("qualityInfoModel");
        GetQualityDetailInf(qualityInfoModel);
    }

    @Event(value = R.id.tb_unboxType,type = CompoundButton.OnCheckedChangeListener.class)
    private void unboxTypeonCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        ShowUnboxing(isChecked);
    }

    @Event(value =R.id.edt_QCScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtQCScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String code=edtQCScanBarcode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            LogUtil.WriteLog(QCScan.class, TAG_GetT_OutBarCodeInfoForQuanADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutBarCodeInfoForQuanADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetT_OutBarCodeInfoForQuanADF, null, URLModel.GetURL().GetT_OutBarCodeInfoForQuanADF, params, null);
        }
        return false;
    }

    @Event(value = edt_unboxing,type = View.OnKeyListener.class)
    private  boolean edtunboxingClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String num=edtunboxing.getText().toString().trim();

            CheckNumRefMaterial checkNumRefMaterial=CheckMaterialNumFormat(num,stockInfoModel.getUnitTypeCode(),stockInfoModel.getDecimalLngth());
            if(!checkNumRefMaterial.ischeck()) {
                MessageBox.Show(context,checkNumRefMaterial.getErrMsg());
                return true;
            }
            Float qty=checkNumRefMaterial.getCheckQty();
          //  Float scanQty=Float.parseFloat(txtScanQty.getText().toString());
            if(qty>stockInfoModel.getQty()){
                MessageBox.Show(context,getString(R.string.Error_PackageQtyBiger));
                CommonUtil.setEditFocus(edtunboxing);
                return true;
            }
//            if(qty>ArithUtil.sub( qualityDetailInfoModels.get(0).getRemainQty(),qualityDetailInfoModels.get(0).getScanQty())) {
//                MessageBox.Show(context, getString(R.string.Error_QCQtyBiger));
//                CommonUtil.setEditFocus(edtunboxing);
//                return true;
//            }
            //拆零
            stockInfoModel.setPickModel(3);
            stockInfoModel.setAmountQty(qty);
            String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
            String strOldBarCode = GsonUtil.parseModelToJson(stockInfoModel);
            final Map<String, String> params = new HashMap<String, String>();
            params.put("UserJson", userJson);
            params.put("strOldBarCode", strOldBarCode);
            params.put("strNewBarCode", "");
            params.put("PrintFlag","2"); //1：打印 2：不打印
            LogUtil.WriteLog(QCScan.class, TAG_SaveT_BarCodeToStockADF, strOldBarCode);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_BarCodeToStockADF, getString(R.string.Msg_SaveT_BarCodeToStockADF), context, mHandler, RESULT_SaveT_BarCodeToStockADF, null,  URLModel.GetURL().SaveT_BarCodeToStockADF, params, null);
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
//            if (qualityDetailInfoModels.get(0).getScanQty()-qualityDetailInfoModels.get(0).getSampQty()!=0f) {
//                MessageBox.Show(context, getString(R.string.Error_SampNumIsNotMatch));
//                CommonUtil.setEditFocus(edtQCScanBarcode);
//            } else {
            if (DoubleClickCheck.isFastDoubleClick(context)) {
                return false;
            }
            if (qualityDetailInfoModels.get(0).getLstStock() != null && qualityDetailInfoModels.get(0).getLstStock().size() != 0) {
                final Map<String, String> params = new HashMap<String, String>();
                String ModelJson = GsonUtil.parseModelToJson(qualityDetailInfoModels);
                String UserJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
                params.put("UserJson", UserJson);
                params.put("ModelJson", ModelJson);
                LogUtil.WriteLog(QCScan.class, TAG_SaveT_QuanlitySampADF, ModelJson);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_QuanlitySampADF, getString(R.string.Msg_SaveT_QuanlitySampADF), context, mHandler, RESULT_Msg_SaveT_QuanlitySampADF, null, URLModel.GetURL().SaveT_QuanlitySampADF, params, null);
            }

        }
        return super.onOptionsItemSelected(item);
    }


    /*
装箱拆箱提交
 */
    void AnalysisSaveT_BarCodeToStockADF(String result){
        try {
            LogUtil.WriteLog(OffshelfScan.class, TAG_SaveT_BarCodeToStockADF, result);
            ReturnMsgModel<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<StockInfo_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                StockInfo_Model stockInfoModel=returnMsgModel.getModelJson();
                qualityDetailInfoModels.get(0).setVoucherType(9996);
                qualityDetailInfoModels.get(0).setScanQty(ArithUtil.add( qualityDetailInfoModels.get(0).getScanQty(),stockInfoModel.getQty()));
                qualityDetailInfoModels.get(0).getLstStock().add(0,stockInfoModel);
//                Float scanQty=Float.parseFloat(txtScanQty.getText().toString());
//                Float qty=Float.parseFloat(edtunboxing.getText().toString().trim());
                txtScanQty.setText( qualityDetailInfoModels.get(0).getScanQty()+"");
                BindListVIew(qualityDetailInfoModels.get(0).getLstStock());
               // ClearFrm();
                //打印质检标签
                PrintQCLabel(stockInfoModel);
            }
            else{
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        CommonUtil.setEditFocus(edtQCScanBarcode);

    }

    /*
        获取质检任务明细
   */
    void GetQualityDetailInf(QualityInfo_Model qualityInfoModel){
        if(qualityInfoModel!=null) {
            txtVoucherNo.setText(qualityInfoModel.getErpVoucherNo());
            txtQCMaterialName.setText(qualityInfoModel.getMaterialDesc());
            final QualityDetailInfo_Model qualityDetailInfoModel = new QualityDetailInfo_Model();
            qualityDetailInfoModel.setHeaderID(qualityInfoModel.getID());
            final Map<String, String> params = new HashMap<String, String>();
            params.put("ModelDetailJson", parseModelToJson(qualityDetailInfoModel));
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(QCScan.class, TAG_GetT_QualityDetailListByHeaderIDADF, para);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_QualityDetailListByHeaderIDADF, getString(R.string.Msg_QualityDetailListByHeaderIDADF), context, mHandler, RESULT_Msg_GetT_QualityDetailListByHeaderIDADF, null,  URLModel.GetURL().GetT_QualityDetailListByHeaderIDADF, params, null);
        }
    }


    void AnalysisGetT_QualityDetailListByHeaderIDADFJson(String result){
        LogUtil.WriteLog(QCScan.class, TAG_GetT_OutBarCodeInfoForQuanADF,result);
        ReturnMsgModelList<QualityDetailInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<QualityDetailInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            qualityDetailInfoModels=returnMsgModel.getModelJson();
            if(qualityDetailInfoModels.get(0).getLstStock()==null)
                qualityDetailInfoModels.get(0).setLstStock(new ArrayList<StockInfo_Model>());
            BindListVIew(qualityDetailInfoModels.get(0).getLstStock());
        }else
        {
            MessageBox.Show(context, returnMsgModel.getMessage());
        }
    }

    void AnalysisSaveT_QuanlitySampADFJson(String result){
       try {
           LogUtil.WriteLog(QCScan.class, TAG_SaveT_QuanlitySampADF,result);
           ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {}.getType());
           MessageBox.Show(context, returnMsgModel.getMessage());
           CommonUtil.setEditFocus(edtQCScanBarcode);
           if(returnMsgModel.getHeaderStatus().equals("S")) {

               closeActiviry();
           }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetT_OutBarCodeInfoForQuanADFJson(String result){
        try {
            LogUtil.WriteLog(QCScan.class, TAG_GetT_OutBarCodeInfoForQuanADF, result);
            ReturnMsgModel<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<StockInfo_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                stockInfoModel = returnMsgModel.getModelJson();

                if (!qualityDetailInfoModels.get(0).getMaterialNo().equals(stockInfoModel.getMaterialNo())) {
                    MessageBox.Show(context, getString(R.string.Error_BarcodeNotInList));
                    CommonUtil.setEditFocus(edtQCScanBarcode);
                } else {
                    if (qualityDetailInfoModels.get(0).getLstStock().indexOf(stockInfoModel) != -1) {
                        MessageBox.Show(context, getString(R.string.Error_BarcodeScaned));
                        CommonUtil.setEditFocus(edtQCScanBarcode);
                        return;
                    }

                    //判断扫描条码批次、据点、仓库与质检单物料相同
                    if (qualityDetailInfoModels.get(0).getBatchNo().equals(stockInfoModel.getBatchNo())
                            && qualityDetailInfoModels.get(0).getWarehouseNo().equals(stockInfoModel.getWarehouseNo())
                            && qualityDetailInfoModels.get(0).getCompanyCode().equals(stockInfoModel.getCompanyCode())) {
                        txtCompany.setText(stockInfoModel.getStrongHoldName());
                        txtBatch.setText(stockInfoModel.getBatchNo());
                        txtStatus.setText(stockInfoModel.getStrStatus());
                        txtMaterialName.setText(stockInfoModel.getMaterialDesc());
                        txtStock.setText(stockInfoModel.getQty().toString());
                        txtEDate.setText(CommonUtil.DateToString(stockInfoModel.getEDate()));
                        if (!TBunboxType.isChecked()) {//整箱
                            Float scanQty = Float.parseFloat(txtScanQty.getText().toString());//以扫描数量
                            //if (stockInfoModel.getQty() >= qualityDetailInfoModels.get(0).getRemainQty() - scanQty) {
//                            if (stockInfoModel.getQty() > ArithUtil.sub(qualityDetailInfoModels.get(0).getRemainQty(),scanQty)) {
//                                MessageBox.Show(context, getString(R.string.Error_QCQtyBiger));
//                                CommonUtil.setEditFocus(edtQCScanBarcode);
//                                return;
//                            } else {
                                stockInfoModel.setPickModel(2);
                                qualityDetailInfoModels.get(0).setVoucherType(9996);
                                qualityDetailInfoModels.get(0).setScanQty(ArithUtil.add(qualityDetailInfoModels.get(0).getScanQty(),stockInfoModel.getQty()));
                                qualityDetailInfoModels.get(0).getLstStock().add(0, stockInfoModel);
                                txtScanQty.setText(qualityDetailInfoModels.get(0).getScanQty() + "");
                                BindListVIew(qualityDetailInfoModels.get(0).getLstStock());
                                //打印质检标签
                                PrintQCLabel(stockInfoModel);
                            //}
                        }
                        CommonUtil.setEditFocus(TBunboxType.isChecked() ? edtunboxing : edtQCScanBarcode);
                    } else {
                        MessageBox.Show(context, getString(R.string.Error_materialNotMatch));
                        CommonUtil.setEditFocus(edtQCScanBarcode);
                    }

                }
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
                CommonUtil.setEditFocus(edtQCScanBarcode);
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
            CommonUtil.setEditFocus(edtQCScanBarcode);
        }

    }

    void AnalysisPrintQYAndroidJson(String result){
        try {
            LogUtil.WriteLog(QCBillChoice.class, TAG_PrintQYAndroid,result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {}.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){

            }else{
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void BindListVIew(List<StockInfo_Model> stockInfoModels){
        txtSampleNum.setText(ArithUtil.sub(qualityDetailInfoModels.get(0).getRemainQty(),qualityDetailInfoModels.get(0).getScanQty())+"");
        txtQCSampleNum.setText(qualityDetailInfoModels.get(0).getRemainQty()+"");
        qcDetailAdapter=new QCDetailAdapter(context,stockInfoModels);
        lsvQCScan.setAdapter(qcDetailAdapter);
    }

    void ClearFrm(){
        stockInfoModel=null;
        txtCompany.setText("");
        txtBatch.setText("");
        txtStatus.setText("");
        txtStock.setText("");
        txtMaterialName.setText("");
        txtEDate.setText("");
        edtunboxing.setText("");
        edtQCScanBarcode.setText("");
        CommonUtil.setEditFocus(edtQCScanBarcode);
    }

    void ShowUnboxing(Boolean show){
        int visiable=show?View.VISIBLE:View.GONE;
        txtunboxing.setVisibility(visiable);
        edtunboxing.setVisibility(visiable);
    }

    void PrintQCLabel(StockInfo_Model stockInfoModel){
        try {
            ArrayList<Barcode_Model> temp = new ArrayList<>();
            // int size=qualityDetailInfoModels.get(0).getLstStock().size();
            //  for (int i=0;i<size;i++) {
            //      StockInfo_Model stockInfoModel = qualityDetailInfoModels.get(0).getLstStock().get(i);
            Barcode_Model barcodeModel = new Barcode_Model();
            barcodeModel.setSerialNo(stockInfoModel.getSerialNo());
            barcodeModel.setBarCode(stockInfoModel.getBarcode());
            barcodeModel.setCreater(BaseApplication.userInfo.getUserName());
            barcodeModel.setMaterialNo(stockInfoModel.getMaterialNo());
            barcodeModel.setQty(stockInfoModel.getQty());
            barcodeModel.setBatchNo(stockInfoModel.getBatchNo());
            barcodeModel.setIP(URLModel.PrintIP);
            temp.add(0, barcodeModel);
            //  }
            String ModelJson = GsonUtil.parseModelToJson(temp);
            final Map<String, String> params = new HashMap<String, String>();
            params.put("json", ModelJson);
            LogUtil.WriteLog(QCBillChoice.class, TAG_PrintQYAndroid, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PrintQYAndroid, getString(R.string.Msg_PrintQYAndroid), context, mHandler, RESULT_PrintQYAndroid, null, URLModel.GetURL().PrintQYAndroid, params, null);
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
            CommonUtil.setEditFocus(edtQCScanBarcode);
        }
    }

}
