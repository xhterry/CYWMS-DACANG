package com.xx.chinetek.Pallet;

import android.content.Context;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Pallet.PalletItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cyproduct.Billinstock.BillsIn;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Pallet.PalletDetail_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
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

@ContentView(R.layout.activity_dismantle_pallet)
public class DismantlePallet extends BaseActivity {

    String TAG_GetT_SerialNoByPalletADF="DisCombinPallet_GetT_SerialNoByPalletADF";
    String TAG_GetT_PalletDetailByNoADF="DisCombinPallet_GetT_PalletDetailByNoADF";
    String TAG_Delete_PalletORBarCodeADF="DisCombinPallet_Delete_PalletORBarCodeADF";


    private final int RESULT_GetT_SerialNoByPalletADF = 101;
    private final int RESULT_GetT_PalletADF = 102;
    private final int RESULT_Delete_PalletORBarCodeADF = 103;

    String TAG_Print_Tlabel = "OffShelfBillChoice_Print_Tlabel";
    private final int RESULT_Print_Tlabel = 104;

    @Override
    public void onHandleMessage(Message msg) {

        switch (msg.what) {
            case RESULT_GetT_SerialNoByPalletADF:
              //  AnalysisGetT_SerialNoByPalletAD((String) msg.obj);
                break;
            case RESULT_GetT_PalletADF:
                AnalysisGetT_PalletAD((String) msg.obj);
                break;
            case RESULT_Delete_PalletORBarCodeADF:
                AnalysisDelete_PalletORBarCodeADF((String) msg.obj);
                break;
            case RESULT_Print_Tlabel:
                AnalysisGetT_RESULT_Print_TlabelADFJson((String) msg.obj);
                break;

            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtBarcode);
                break;
        }
    }


    Context context=DismantlePallet.this;

    @ViewInject(R.id.conLay_DisPallet)
    ConstraintLayout conLayDIsPallet;
    @ViewInject(R.id.SW_DisPallet)
    Switch SWDisPallet;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.txt_EDate)
    TextView txtEDate;
    @ViewInject(R.id.textView2201)
    TextView txtPalletNo;
    @ViewInject(R.id.edt_Barcode)
    EditText edtBarcode;
    @ViewInject(R.id.lsv_DisPalletDetail)
    ListView lsvDisPalletDetail;
    @ViewInject(R.id.btn_Config)
    Button btnConfig;

    PalletItemAdapter palletItemAdapter;
    ArrayList<PalletDetail_Model> palletDetailModels;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.DisPallet_scan), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Override
    protected void initData() {
        super.initData();
        ShowMaterialScan(SWDisPallet.isChecked());
        palletDetailModels=new ArrayList<>();
        palletDetailModels.add(new PalletDetail_Model());
        palletDetailModels.get(0).setLstBarCode(new ArrayList<BarCodeInfo>());
    }

    @Event(value = R.id.SW_DisPallet,type = CompoundButton.OnCheckedChangeListener.class)
    private void SwPalletonCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ShowMaterialScan(isChecked);
    }

    @Event(value = R.id.edt_Barcode,type = View.OnKeyListener.class)
    private  boolean edtBarcodeonKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String barcode=edtBarcode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("Barcode", barcode);
            params.put("PalletModel","2");
//            if(!SWDisPallet.isChecked()) {
//                LogUtil.WriteLog(DismantlePallet.class, TAG_GetT_SerialNoByPalletADF, barcode);
//                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_SerialNoByPalletADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetT_SerialNoByPalletADF, null,  URLModel.GetURL().GetT_SerialNoByPalletADF, params, null);
//            }else{
                LogUtil.WriteLog(DismantlePallet.class, TAG_GetT_PalletDetailByNoADF, barcode);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByNoADF, getString(R.string.Msg_GetT_PalletADF), context, mHandler, RESULT_GetT_PalletADF, null,  URLModel.GetURL().GetT_PalletDetailByNoADF, params, null);
           // }
            return false;
        }
        return false;
    }

    void AnalysisGetT_RESULT_Print_TlabelADFJson(String result){
        try {
            LogUtil.WriteLog(BillsIn.class, TAG_Print_Tlabel, result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ClearFrm();
                MessageBox.Show(context, "托盘条码打印成功！");
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }


    /*
    提交
     */
    @Event(R.id.btn_Config)
    private void btnConfigClick(View v){
        if (DoubleClickCheck.isFastDoubleClick(context)) {
            return ;
        }
        String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
        String modelJson = GsonUtil.parseModelToJson(palletDetailModels);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("UserJson", userJson);
        params.put("PalletDetailJson", modelJson);
        LogUtil.WriteLog(DismantlePallet.class, TAG_Delete_PalletORBarCodeADF, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Delete_PalletORBarCodeADF, getString(R.string.Msg_Delete_PalletORBarCodeADF), context, mHandler, RESULT_Delete_PalletORBarCodeADF, null,  URLModel.GetURL().Delete_PalletORBarCodeADF, params, null);
    }


    /*
   解析物料条码扫描
    */
//    void AnalysisGetT_SerialNoByPalletAD(String result){
//        LogUtil.WriteLog(DismantlePallet.class, TAG_GetT_SerialNoByPalletADF,result);
//        ReturnMsgModel<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<BarCodeInfo>>() {}.getType());
//        if(returnMsgModel.getHeaderStatus().equals("S")){
//            BarCodeInfo  barCodeInfo=returnMsgModel.getModelJson();
//            if(palletDetailModels.get(0).getLstBarCode().contains(barCodeInfo)){ //存在条码
//                MessageBox.Show(context,R.string.Error_Barcode_hasScan);
//            }else{
//                //判断拆托条件：批次、据点、库位、物料、托盘属性相同才能一起拆托
//                if(palletDetailModels.get(0).getLstBarCode()!=null && palletDetailModels.get(0).getLstBarCode().size()!=0) {
//                    String checkError=CheckPalletCondition(barCodeInfo);
//                    if (!TextUtils.isEmpty(checkError)) {
//                        MessageBox.Show(context, checkError);
//                        return;
//                    }
//                    barCodeInfo.setPalletno(palletDetailModels.get(0).getLstBarCode().get(0).getPalletno());
//                }
//                palletDetailModels.get(0).setPalletNo(barCodeInfo.getPalletno());
//                palletDetailModels.get(0).setPalletType(barCodeInfo.getPalletType());
//                palletDetailModels.get(0).getLstBarCode().add(0,barCodeInfo);
//                txtCompany.setText(barCodeInfo.getStrongHoldName());
//                txtBatch.setText(barCodeInfo.getBatchNo());
//                txtStatus.setText(barCodeInfo.getStatus());
//                txtEDate.setText(CommonUtil.DateToString(barCodeInfo.getEDate()));
//                txtMaterialName.setText(barCodeInfo.getMaterialDesc());
//                txtPalletNo.setText(palletDetailModels.get(0).getPalletNo());
//                BindListVIew(palletDetailModels.get(0).getLstBarCode());
//            }
//        }else
//        {
//            ToastUtil.show(returnMsgModel.getMessage());
//        }
//        CommonUtil.setEditFocus(edtBarcode);
//    }
//

    ArrayList<PalletDetail_Model> palletDetailModelsLast;
    /*
    解析托盘条码扫描
     */
    void AnalysisGetT_PalletAD(String result){
        LogUtil.WriteLog(DismantlePallet.class, TAG_GetT_PalletDetailByNoADF,result);
        ReturnMsgModelList<PalletDetail_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<PalletDetail_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            try {
                ArrayList<PalletDetail_Model> temppalletDetailModels = returnMsgModel.getModelJson();
                if (temppalletDetailModels != null && temppalletDetailModels.get(0).getLstBarCode() != null) {
                    String barcoce = edtBarcode.getText().toString().trim();
                    String PalletNo=txtPalletNo.getText().toString();
                    if(!TextUtils.isEmpty(PalletNo)){
                        if(!PalletNo.equals(temppalletDetailModels.get(0).getPalletNo())){
                            MessageBox.Show(context,getString(R.string.Error_PalletNoMatch));
                           CommonUtil.setEditFocus(edtBarcode);
                            return;
                        }
                    }


                    //Boolean isFoundBarcode = false;
                    for (int i = 0; i < temppalletDetailModels.get(0).getLstBarCode().size(); i++) {
                        if (!SWDisPallet.isChecked() && !temppalletDetailModels.get(0).getLstBarCode().get(i).getBarCode().contains(barcoce))
                            continue;
//                        isFoundBarcode = true;
                        BarCodeInfo barCodeInfo = temppalletDetailModels.get(0).getLstBarCode().get(i);
                        if (palletDetailModels.get(0).getLstBarCode().indexOf(barCodeInfo) == -1) {
                            palletDetailModels.get(0).setPalletNo(temppalletDetailModels.get(0).getPalletNo());
                            palletDetailModels.get(0).setPalletType(temppalletDetailModels.get(0).getPalletType());
                            palletDetailModels.get(0).getLstBarCode().add(0, barCodeInfo);
                            txtCompany.setText(barCodeInfo.getStrongHoldName());
                            txtBatch.setText(barCodeInfo.getBatchNo());
                            txtStatus.setText("");
                            txtEDate.setText(CommonUtil.DateToString(barCodeInfo.getEDate()));
                            txtMaterialName.setText(barCodeInfo.getMaterialDesc());
                            txtPalletNo.setText(temppalletDetailModels.get(0).getPalletNo());
                            //BindListVIew(palletDetailModels.get(0).getLstBarCode());
                        } else {
                            MessageBox.Show(context, getString(R.string.Error_BarcodeScaned));
                        }
                        if(!SWDisPallet.isChecked()) break;

                    }
//                    if (!isFoundBarcode) {
//                        MessageBox.Show(context, getString(R.string.Error_BarcodeNotInList));
//                    }
                }
            }catch (Exception ex){
                MessageBox.Show(context, ex.getMessage());
            }
            BindListVIew(palletDetailModels.get(0).getLstBarCode());
        }else
        {
            MessageBox.Show(context,returnMsgModel.getMessage());
        }
        CommonUtil.setEditFocus(edtBarcode);
    }

    /*
   删除组托信息
    */
    void AnalysisDelete_PalletORBarCodeADF(String result){
        try {
            LogUtil.WriteLog(DismantlePallet.class, TAG_Delete_PalletORBarCodeADF, result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                ClearFrm();
                MessageBox.Show(context, "拆托成功！");
//                //供应商打印去除打印
                String PalletNoy=txtPalletNo.getText().toString();
                if(!TextUtils.isEmpty(PalletNoy)){
                    //打印托盘标签
                    ArrayList<PalletDetail_Model> palletDetailModelsLast =new ArrayList<>();
                    PalletDetail_Model mo = new PalletDetail_Model();
                    mo.setPrintIPAdress(URLModel.PrintIP);
                    mo.setTaskNo(PalletNoy);
                    palletDetailModelsLast.add(mo);
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("PalletJson", GsonUtil.parseModelToJson(palletDetailModelsLast));

//                LogUtil.WriteLog(CombinPallet.class, TAG_Print_Tlabel, modelJson);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Print_Tlabel, getString(R.string.Msg_Print), context, mHandler,RESULT_Print_Tlabel, null,  URLModel.GetURL().PrintForChaiTuoProductAndroid, params, null);

                }

            }
            else{
                MessageBox.Show(context, returnMsgModel.getMessage());
            }

        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    private void BindListVIew(List<BarCodeInfo> barCodeInfos) {
            palletItemAdapter = new PalletItemAdapter(context, barCodeInfos);
            lsvDisPalletDetail.setAdapter(palletItemAdapter);
    }

    /*
    显示隐藏物料信息
     */
    void ShowMaterialScan(boolean check){
        if(!check){
            conLayDIsPallet.setVisibility(View.VISIBLE);
           // edtBarcode.setHint(R.string.Hit_ScanBarcode);
        }else{
            conLayDIsPallet.setVisibility(View.GONE);
          //  edtBarcode.setHint(R.string.Hit_ScanPallet);
        }
        ClearFrm();
    }

    String CheckPalletCondition(BarCodeInfo  barCodeInfo){
        //判断拆托条件：批次、据点、库位、物料、托盘属性相同才能一起拆托
        if(palletDetailModels.get(0).getPalletType()!=barCodeInfo.getPalletType())
            return getString(R.string.Error_PalletypenotMatch);
        if(barCodeInfo.getAreaID()!=0)
            return getString(R.string.Error_Barcode_Instock);
        if (!palletDetailModels.get(0).getLstBarCode().get(0).getMaterialNo().equals(barCodeInfo.getMaterialNo()))
            return getString(R.string.Error_materialnotMatch);
        else if(!palletDetailModels.get(0).getLstBarCode().get(0).getBatchNo().equals(barCodeInfo.getBatchNo()))
            return getString(R.string.Error_BartchnotMatch);
        else if(!palletDetailModels.get(0).getLstBarCode().get(0).getStrongHoldCode().equals(barCodeInfo.getStrongHoldCode()))
            return getString(R.string.Error_CompanynotMatch);
        else if(palletDetailModels.get(0).getLstBarCode().get(0).getAreaID() != (barCodeInfo.getAreaID()))
            return getString(R.string.Error_AreaotnotMatch);
        else
            return "";
    }

    void ClearFrm(){
        edtBarcode.setText("");
        txtCompany.setText("");
        txtBatch.setText("");
        txtEDate.setText("");
        txtStatus.setText("");
        txtMaterialName.setText("");
        txtPalletNo.setText("");
        palletDetailModels=new ArrayList<>();
        palletDetailModels.add(new PalletDetail_Model());
        palletDetailModels.get(0).setLstBarCode(new ArrayList<BarCodeInfo>());
        BindListVIew(palletDetailModels.get(0).getLstBarCode());
        CommonUtil.setEditFocus(edtBarcode);
    }
}
