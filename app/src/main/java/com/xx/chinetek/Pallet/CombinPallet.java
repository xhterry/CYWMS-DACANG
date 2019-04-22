package com.xx.chinetek.Pallet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
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
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Pallet.PalletDetail_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;
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
import java.util.List;
import java.util.Map;

import static com.xx.chinetek.cywms.R.id.SW_Pallet;

@ContentView(R.layout.activity_combin_pallet)
public class CombinPallet extends BaseActivity {

    String TAG_GetT_PalletDetailByNoADF="CombinPallet_GetT_PalletDetailByNoADF";
    String TAG_SaveT_PalletDetailADF="CombinPallet_TAG_SaveT_PalletDetailADF";
    String TAG_PrintLpkPalletAndroid="CombinPallet_TAG_PrintLpkPalletAndroid";
    String TAG_SaveT_ProductPalletDetailADF="CombinPallet_TAG_SaveT_ProductPalletDetailADF";//成品组托

    Context context=CombinPallet.this;
    private final int RESULT_GetT_SerialNoByPalletADF = 101;
    private final int RESULT_GetT_PalletDetailByNoADF = 102;
    private final int RESULT_SaveT_PalletDetailADF = 103;
    private final int RESULT_PrintLpkPalletAndroid = 104;
    private final int RESULT_GetT_ProductPalletDetailByNoADF = 105;//成品组托

    boolean isBarcodeScaned=false;

    @Override
    public void onHandleMessage(Message msg) {

        switch (msg.what) {
            case RESULT_GetT_SerialNoByPalletADF:
                AnalysisGetT_SerialNoByPalletAD((String) msg.obj);
                break;
            case RESULT_GetT_PalletDetailByNoADF:
                AnalysisGetT_PalletAD((String) msg.obj);
                break;
            case RESULT_SaveT_PalletDetailADF:
                AnalysisSaveT_PalletDetailADF((String) msg.obj);
                break;
            case RESULT_PrintLpkPalletAndroid:
                AnalysisPrintLpkPalletAndroid((String) msg.obj);
                break;
            case RESULT_GetT_ProductPalletDetailByNoADF:
                AnalysisSaveT_ProductPalletDetailADF((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(isBarcodeScaned?edtBarcode:edtPallet);
                break;
        }
    }


    @ViewInject(SW_Pallet)
    Switch SWPallet;
    @ViewInject(R.id.txt_Pallet)
    TextView txtPallet;
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
    @ViewInject(R.id.txt_CartonNum)
    TextView txtCartonNum;
    @ViewInject(R.id.edt_Pallet)
    EditText edtPallet;
    @ViewInject(R.id.edt_Barcode)
    EditText edtBarcode;
    @ViewInject(R.id.lsv_PalletDetail)
    ListView lsvPalletDetail;
    @ViewInject(R.id.btn_PrintPalletLabel)
    Button btnPrintPalletLabel;

    PalletItemAdapter palletItemAdapter;
    List<PalletDetail_Model> palletDetailModels;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.Pallet_scan), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Override
    protected void initData() {
        super.initData();
        ShowPalletScan(SWPallet.isChecked());
    }

    /*
    长按删除物料
     */
    @Event(value = R.id.lsv_PalletDetail,type =  AdapterView.OnItemLongClickListener.class)
    private  boolean lsvPalletDetailonLongClick(AdapterView<?> parent, View view, final int position, long id){
        if(id>=0) {
            BarCodeInfo delBarcode=(BarCodeInfo)palletItemAdapter.getItem(position);
            final String barcode=delBarcode.getSerialNo();
            new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除物料数据？\n条码："+barcode)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法
                            palletDetailModels.get(0).getLstBarCode().remove(position);
                            txtCartonNum.setText(ShowNum());
                            BindListVIew( palletDetailModels.get(0).getLstBarCode());
                        }
                    }).setNegativeButton("取消", null).show();
        }
        return true;
    }

    @Event(value = R.id.edt_Barcode,type = View.OnKeyListener.class)
    private  boolean edtBarcodeonKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            isBarcodeScaned=true;
            String barcode=edtBarcode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("Barcode", barcode);
            params.put("PalletModel", "1"); //1：新建托盘  2：插入组托
            LogUtil.WriteLog(CombinPallet.class, TAG_GetT_PalletDetailByNoADF, barcode);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByNoADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetT_SerialNoByPalletADF, null,  URLModel.GetURL().GetT_PalletDetailByNoADF, params, null);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            if(SWPallet.isChecked()){
                new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否放弃此次组托任务？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO 自动生成的方法
                                edtPallet.setEnabled(true);
                                InitFrm();
                                CommonUtil.setEditFocus(edtPallet);
                            }
                        }).setNegativeButton("取消", null).show();
                return true;
            }
        }
        return false;
    }

    @Event(value = R.id.edt_Pallet,type = View.OnKeyListener.class)
    private  boolean edtPalletonKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            isBarcodeScaned=false;
            String barcode=edtPallet.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("Barcode", barcode);
            params.put("PalletModel","2"); //1：新建托盘  2：插入组托
            LogUtil.WriteLog(CombinPallet.class, TAG_GetT_PalletDetailByNoADF, barcode);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByNoADF, getString(R.string.Msg_GetT_PalletADF), context, mHandler, RESULT_GetT_PalletDetailByNoADF, null,  URLModel.GetURL().GetT_PalletDetailByNoADF, params, null);
            return true;
        }
        return false;
    }

    @Event(value = SW_Pallet,type = CompoundButton.OnCheckedChangeListener.class)
    private void SwPalletonCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ShowPalletScan(isChecked);
    }


    @Event(R.id.btn_PrintPalletLabel)
    private void btnPrintPalletLabelClick(View v) {
        if (DoubleClickCheck.isFastDoubleClick(context)) {
            return;
        }
        if (palletDetailModels != null && palletDetailModels.size() != 0 && palletDetailModels.get(0).getLstBarCode()!=null
                && palletDetailModels.get(0).getLstBarCode().size()!=0) {
            palletDetailModels.get(0).setVoucherType(999);
            palletDetailModels.get(0).setPrintIPAdress(URLModel.PrintIP);
            String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
            String modelJson = GsonUtil.parseModelToJson(palletDetailModels);
            final Map<String, String> params = new HashMap<String, String>();

            LogUtil.WriteLog(CombinPallet.class, TAG_SaveT_PalletDetailADF, modelJson);
            InitFrm();
            if (URLModel.isWMS){
                params.put("UserJson", userJson);
                params.put("ModelJson", modelJson);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_PalletDetailADF, getString(R.string.Msg_SaveT_PalletDetailADF), context, mHandler, RESULT_SaveT_PalletDetailADF, null, URLModel.GetURL().SaveT_PalletDetailADF, params, null);
            }else{
                params.put("UserJson", userJson);
                params.put("json", modelJson);
                params.put("printtype", "1");
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_ProductPalletDetailADF, getString(R.string.Msg_SaveT_PalletDetailADF), context, mHandler, RESULT_GetT_ProductPalletDetailByNoADF, null, URLModel.GetURL().SaveT_CPPalletDetailADF, params, null);
            }


        }
    }


    /*
    解析物料条码扫描
     */
    void AnalysisGetT_SerialNoByPalletAD(String result){
        LogUtil.WriteLog(CombinPallet.class, TAG_GetT_PalletDetailByNoADF,result);
        try {
            ReturnMsgModelList<PalletDetail_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<PalletDetail_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                PalletDetail_Model palletDetailModel = returnMsgModel.getModelJson().get(0);
                //判断组托条件：批次、据点、库位、物料相同才能组托
                if (palletDetailModels.get(0).getLstBarCode() != null) {// &&
                    for (BarCodeInfo barCodeInfo : palletDetailModel.getLstBarCode()) {
                        if (palletDetailModels.get(0).getLstBarCode().size() != 0) {
                            if(palletDetailModels.get(0).getLstBarCode().contains(barCodeInfo)){
                                MessageBox.Show(context, getString(R.string.Error_Contain_Barcode));
                                CommonUtil.setEditFocus(edtBarcode);
                                return;
                            }
                            String checkError = CheckPalletCondition(barCodeInfo);
                            if (!TextUtils.isEmpty(checkError)) {
                                MessageBox.Show(context, checkError);
                                CommonUtil.setEditFocus(edtBarcode);
                                return;
                            }
                            barCodeInfo.setPalletno(palletDetailModels.get(0).getLstBarCode().get(0).getPalletno());
                        }
                        if (!palletDetailModels.get(0).getLstBarCode().contains(barCodeInfo)) {
                           // palletDetailModels.get(0).setPalletNo(barCodeInfo.getPalletno());
                            palletDetailModels.get(0).setPalletType(barCodeInfo.getPalletType());

                            palletDetailModels.get(0).getLstBarCode().add(0, barCodeInfo);
                           // palletDetailModels.get(0).setVoucherType(999);
                            palletDetailModels.get(0).setStrongHoldCode(barCodeInfo.getStrongHoldCode());
                            palletDetailModels.get(0).setStrongHoldName(barCodeInfo.getStrongHoldName());
                            palletDetailModels.get(0).setCompanyCode(barCodeInfo.getCompanyCode());
                            palletDetailModels.get(0).setMaterialNo(barCodeInfo.getMaterialNo());
                            palletDetailModels.get(0).setBatchNo(barCodeInfo.getBatchNo());
                            palletDetailModels.get(0).setSupPrdBatch(barCodeInfo.getSupPrdBatch());
                            palletDetailModels.get(0).setSuppliernNo(barCodeInfo.getSupCode());
                            palletDetailModels.get(0).setSuppliernName(barCodeInfo.getSupName());
                            palletDetailModels.get(0).setErpVoucherNo(barCodeInfo.getErpVoucherNo());
                            palletDetailModels.get(0).setAreaID(barCodeInfo.getAreaID());
                        }
                    }
                }
                BarCodeInfo barCodeInfo = palletDetailModel.getLstBarCode().get(0);
                txtCompany.setText(barCodeInfo.getStrongHoldName());
                txtBatch.setText(barCodeInfo.getBatchNo());
                txtStatus.setText(barCodeInfo.getStrStatus());
                txtMaterialName.setText(barCodeInfo.getMaterialDesc());
                txtEDate.setText(CommonUtil.DateToString(barCodeInfo.getEDate()));
                txtCartonNum.setText(ShowNum());
                BindListVIew(palletDetailModels.get(0).getLstBarCode());
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception e){
            MessageBox.Show(context,e.toString());
        }
        CommonUtil.setEditFocus(edtBarcode);
    }


    /*
    解析托盘条码扫描
     */
    void AnalysisGetT_PalletAD(String result){
        LogUtil.WriteLog(CombinPallet.class, TAG_GetT_PalletDetailByNoADF,result);
        ReturnMsgModelList<PalletDetail_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<PalletDetail_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            palletDetailModels=returnMsgModel.getModelJson();
            if(palletDetailModels!=null) {
                BindListVIew(palletDetailModels.get(0).getLstBarCode());
                edtPallet.setEnabled(false);
                txtCartonNum.setText(ShowNum());
            }
            CommonUtil.setEditFocus(edtBarcode);
        }else{
            MessageBox.Show(context,returnMsgModel.getMessage());
            edtPallet.setEnabled(true);
            CommonUtil.setEditFocus(edtPallet);
        }

    }

    /*
    保存组托信息
     */
    void AnalysisSaveT_PalletDetailADF(String result){
        try {
            LogUtil.WriteLog(CombinPallet.class, TAG_SaveT_PalletDetailADF, result);
            ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            MessageBox.Show(context, returnMsgModel.getMessage());
            if(returnMsgModel.getHeaderStatus().equals("S")) {
                InitFrm();

                String palletNo=returnMsgModel.getTaskNo();
                Barcode_Model barcodeModel=new Barcode_Model();
                barcodeModel.setSerialNo(palletNo);
                barcodeModel.setIP(URLModel.PrintIP);
                ArrayList<Barcode_Model> barcodeModels=new ArrayList<>();
                barcodeModels.add(barcodeModel);
                String modelJson = GsonUtil.parseModelToJson(barcodeModels);
                final Map<String, String> params = new HashMap<String, String>();
                params.put("json", modelJson);
                LogUtil.WriteLog(CombinPallet.class, TAG_PrintLpkPalletAndroid, modelJson);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PrintLpkPalletAndroid, getString(R.string.Msg_PrintLpkPalletAndroid), context, mHandler, RESULT_PrintLpkPalletAndroid, null,  URLModel.GetURL().PrintLpkPalletAndroid, params, null);
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
            CommonUtil.setEditFocus(edtBarcode);
        }
    }



    /*
    保存成品组托信息
     */
    void AnalysisSaveT_ProductPalletDetailADF(String result){
        try {
            LogUtil.WriteLog(CombinPallet.class, TAG_SaveT_PalletDetailADF, result);
            ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")) {
                InitFrm();
            }
            MessageBox.Show(context, returnMsgModel.getMessage());
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
            CommonUtil.setEditFocus(edtBarcode);
        }
    }


    void AnalysisPrintLpkPalletAndroid(String result){
        try {
            LogUtil.WriteLog(CombinPallet.class, TAG_PrintLpkPalletAndroid, result);
            ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(!returnMsgModel.getHeaderStatus().equals("S"))
                MessageBox.Show(context,returnMsgModel.getMessage());

        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        CommonUtil.setEditFocus(SWPallet.isChecked()?edtPallet:edtBarcode);
    }

    /*
    显示隐藏Pallet输入
     */
    void ShowPalletScan(boolean check){
        InitFrm();
        txtPallet.setEnabled(true);
        if(!check){
            txtPallet.setVisibility(View.GONE);
            edtPallet.setVisibility(View.GONE);
        }else{
            txtPallet.setVisibility(View.VISIBLE);
            edtPallet.setVisibility(View.VISIBLE);
            CommonUtil.setEditFocus(edtPallet);
        }
    }

    private void BindListVIew(List<BarCodeInfo> barCodeInfos) {
            palletItemAdapter = new PalletItemAdapter(context, barCodeInfos);
            lsvPalletDetail.setAdapter(palletItemAdapter);
    }

    void InitFrm(){
        palletDetailModels=new ArrayList<>();
        palletDetailModels.add(new PalletDetail_Model());
        palletDetailModels.get(0).setLstBarCode(new ArrayList<BarCodeInfo>());
        BindListVIew(palletDetailModels.get(0).getLstBarCode());
        edtPallet.setEnabled(true);
        edtBarcode.setText("");
        edtPallet.setText("");
        txtCompany.setText("");
        txtBatch.setText("");
        txtStatus.setText("");
        txtEDate.setText("");
        txtMaterialName.setText("");
        txtCartonNum.setText("0 / 0");
    }

    Float GetAllQunantity(){
        Float sumPackageQty=0f;
        if(palletDetailModels.get(0).getLstBarCode()!=null) {
            for (BarCodeInfo barCodeInfo : palletDetailModels.get(0).getLstBarCode()) {
                sumPackageQty = ArithUtil.add(sumPackageQty,barCodeInfo.getQty());
            }
        }
       return sumPackageQty;
    }

    String ShowNum(){
        return new StringBuffer().append(palletDetailModels.get(0).getLstBarCode().size()).append(" / ").append(GetAllQunantity()).toString();
    }


    String CheckPalletCondition(BarCodeInfo  barCodeInfo) {
        if (palletDetailModels.get(0).getPalletType() == 0) {
            if (!palletDetailModels.get(0).getErpVoucherNo().equals(barCodeInfo.getErpVoucherNo()))
                return getString(R.string.Error_VourcherNonotMatch);
            if(!palletDetailModels.get(0).getSuppliernNo().equals(barCodeInfo.getSupCode())){
                return getString(R.string.Error_SuppilerNoMatch);
            }
        } else if (palletDetailModels.get(0).getAreaID() != (barCodeInfo.getAreaID()))
            return getString(R.string.Error_AreaotnotMatch);
        //收货组托判断组托条件：批次、据点、物料、订单相同才能组托
        //在库组托判断库位相同才能组托
        //getPalletType为0：收货组托
        //新增：判断物料是否已组托 插入：判断物料所在托盘属性是否与现有托盘属性一致才能组托
        if (!SWPallet.isChecked() && barCodeInfo.getPalletType() != 0)
            return getString(R.string.Error_Contain_Barcode);
//        if (SWPallet.isChecked() && palletDetailModels.get(0).getPalletType() != barCodeInfo.getPalletType())
//            return getString(R.string.Error_PalletypenotMatch);//.getLstBarCode().get(0)
        if (!palletDetailModels.get(0).getMaterialNo().equals(barCodeInfo.getMaterialNo()))
            return getString(R.string.Error_materialnotMatch);
        else if (barCodeInfo.getBatchNo()==null || !palletDetailModels.get(0).getBatchNo().equals(barCodeInfo.getBatchNo()))
            return getString(R.string.Error_BartchnotMatch);
        else if (barCodeInfo.getSupPrdBatch()==null || !palletDetailModels.get(0).getSupPrdBatch().equals(barCodeInfo.getSupPrdBatch()))
            return getString(R.string.Error_ProductBartchnotMatch);
        else if (!palletDetailModels.get(0).getStrongHoldCode().equals(barCodeInfo.getStrongHoldCode()))
            return getString(R.string.Error_CompanynotMatch);
        return "";
    }
}
