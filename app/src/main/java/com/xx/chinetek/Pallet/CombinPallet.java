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
import com.xx.chinetek.adapter.Pallet.PalletItemAdapter;
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
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.CommonUtil;
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

    Context context=CombinPallet.this;
    private final int RESULT_GetT_SerialNoByPalletADF = 101;
    private final int RESULT_GetT_PalletDetailByNoADF = 102;
    private final int RESULT_SaveT_PalletDetailADF = 103;
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
    }

    @Override
    protected void initData() {
        super.initData();
        ShowPalletScan(SWPallet.isChecked());
        palletDetailModels=new ArrayList<>();
        palletDetailModels.add(new PalletDetail_Model());
        palletDetailModels.get(0).setLstBarCode(new ArrayList<BarCodeInfo>());
    }

    /*
    长按删除物料
     */
    @Event(value = R.id.lsv_PalletDetail,type =  AdapterView.OnItemLongClickListener.class)
    private  boolean lsvPalletDetailonLongClick(AdapterView<?> parent, View view, final int position, long id){
        if(id>=0) {
            BarCodeInfo delBarcode=(BarCodeInfo)palletItemAdapter.getItem(position);
            final String barcode=delBarcode.getSerialNo();
            new AlertDialog.Builder(context).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除物料数据？\n条码："+barcode)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法
                            palletDetailModels.get(0).getLstBarCode().remove(position);
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
            isBarcodeScaned=true;
            String barcode=edtBarcode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("Barcode", barcode);
            params.put("PalletModel", SWPallet.isChecked()?"2":"1"); //1：新建托盘  2：插入组托
            LogUtil.WriteLog(CombinPallet.class, TAG_GetT_PalletDetailByNoADF, barcode);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByNoADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetT_SerialNoByPalletADF, null,  URLModel.GetURL().GetT_PalletDetailByNoADF, params, null);
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            if(SWPallet.isChecked()){
                new AlertDialog.Builder(context).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否放弃此次组托任务？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO 自动生成的方法
                                edtPallet.setEnabled(true);
                                edtBarcode.setText("");
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
            isBarcodeScaned=false;
            String barcode=edtPallet.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("Barcode", barcode);
            LogUtil.WriteLog(CombinPallet.class, TAG_GetT_PalletDetailByNoADF, barcode);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByNoADF, getString(R.string.Msg_GetT_PalletADF), context, mHandler, RESULT_GetT_PalletDetailByNoADF, null,  URLModel.GetURL().GetT_PalletDetailByNoADF, params, null);
            return false;
        }
        return false;
    }

    @Event(value = SW_Pallet,type = CompoundButton.OnCheckedChangeListener.class)
    private void SwPalletonCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ShowPalletScan(isChecked);
    }


    @Event(R.id.btn_PrintPalletLabel)
    private void btnPrintPalletLabelClick(View v){
        String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
        String modelJson = GsonUtil.parseModelToJson(palletDetailModels);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("UserJson", userJson);
        params.put("ModelJson", modelJson);
        LogUtil.WriteLog(CombinPallet.class, TAG_SaveT_PalletDetailADF, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_PalletDetailADF, getString(R.string.Msg_SaveT_PalletDetailADF), context, mHandler, RESULT_SaveT_PalletDetailADF, null,  URLModel.GetURL().SaveT_PalletDetailADF, params, null);
    }


    /*
    解析物料条码扫描
     */
    void AnalysisGetT_SerialNoByPalletAD(String result){
        LogUtil.WriteLog(CombinPallet.class, TAG_GetT_PalletDetailByNoADF,result);
        ReturnMsgModelList<PalletDetail_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<PalletDetail_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            PalletDetail_Model  palletDetailModel=returnMsgModel.getModelJson().get(0);
                //判断组托条件：批次、据点、库位、物料相同才能组托
                if( palletDetailModels.get(0).getLstBarCode()!=null) {// &&
                    for (BarCodeInfo  barCodeInfo: palletDetailModel.getLstBarCode()) {
                        if(palletDetailModels.get(0).getLstBarCode().size()!=0) {
                            String checkError = CheckPalletCondition(barCodeInfo);
                            if (!TextUtils.isEmpty(checkError)) {
                                MessageBox.Show(context, checkError);
                                return;
                            }
                            barCodeInfo.setPalletno(palletDetailModels.get(0).getLstBarCode().get(0).getPalletno());
                        }
                        if(!palletDetailModels.get(0).getLstBarCode().contains(barCodeInfo)) {
                            palletDetailModels.get(0).setPalletNo(barCodeInfo.getPalletno());
                            palletDetailModels.get(0).setPalletType(barCodeInfo.getPalletType());
                            palletDetailModels.get(0).getLstBarCode().add(0, barCodeInfo);
                            palletDetailModels.get(0).setVoucherType(999);
                        }
                    }
                }
                BarCodeInfo barCodeInfo=palletDetailModel.getLstBarCode().get(0);
                txtCompany.setText(barCodeInfo.getStrongHoldName());
                txtBatch.setText(barCodeInfo.getBatchNo());
                txtStatus.setText(barCodeInfo.getStrStatus());
                txtMaterialName.setText(barCodeInfo.getMaterialDesc());
                txtCartonNum.setText(palletDetailModels.get(0).getLstBarCode().size() + "");
                BindListVIew(palletDetailModels.get(0).getLstBarCode());
        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
        }
        CommonUtil.setEditFocus(edtBarcode);
    }


    /*
    解析托盘条码扫描
     */
    void AnalysisGetT_PalletAD(String result){
        LogUtil.WriteLog(CombinPallet.class, TAG_GetT_PalletDetailByNoADF,result);
        ReturnMsgModelList<PalletDetail_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<BarCodeInfo>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            palletDetailModels=returnMsgModel.getModelJson();
            BindListVIew(palletDetailModels.get(0).getLstBarCode());
            edtPallet.setEnabled(false);
            CommonUtil.setEditFocus(edtBarcode);
        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
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
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    /*
    显示隐藏Pallet输入
     */
    void ShowPalletScan(boolean check){
        edtBarcode.setText("");
        edtPallet.setText("");
        txtCompany.setText("");
        txtBatch.setText("");
        txtStatus.setText("");
        txtMaterialName.setText("");
        txtCartonNum.setText("0");
        palletDetailModels=new ArrayList<>();
        BindListVIew(new ArrayList<BarCodeInfo>());
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


    String CheckPalletCondition(BarCodeInfo  barCodeInfo){


            //收货组托判断组托条件：批次、据点、物料、订单相同才能组托
            //在库组托判断库位相同才能组托
            //getPalletType为0：收货组托
            //新增：判断物料是否已组托 插入：判断物料所在托盘属性是否与现有托盘属性一致才能组托
            if (!SWPallet.isChecked() && barCodeInfo.getPalletType() != 0)
                return getString(R.string.Error_Contain_Barcode);
//        if(SWPallet.isChecked() && palletDetailModels.get(0).getPalletType()!=barCodeInfo.getPalletType())
//            return getString(R.string.Error_PalletypenotMatch);
            if (!palletDetailModels.get(0).getLstBarCode().get(0).getMaterialNo().equals(barCodeInfo.getMaterialNo()))
                return getString(R.string.Error_materialnotMatch);
            else if (!palletDetailModels.get(0).getLstBarCode().get(0).getBatchNo().equals(barCodeInfo.getBatchNo()))
                return getString(R.string.Error_BartchnotMatch);
            else if (!palletDetailModels.get(0).getLstBarCode().get(0).getSupPrdBatch().equals(barCodeInfo.getSupPrdBatch()))
                return getString(R.string.Error_ProductBartchnotMatch);
            else if (!palletDetailModels.get(0).getLstBarCode().get(0).getStrongHoldCode().equals(barCodeInfo.getStrongHoldCode()))
                return getString(R.string.Error_CompanynotMatch);
            if (palletDetailModels.get(0).getPalletType() == 0) {
                if (!palletDetailModels.get(0).getLstBarCode().get(0).getErpVoucherNo().equals(barCodeInfo.getErpVoucherNo()))
                    return getString(R.string.Error_VourcherNonotMatch);
            } else if (palletDetailModels.get(0).getLstBarCode().get(0).getAreaID() != (barCodeInfo.getAreaID()))
                return getString(R.string.Error_AreaotnotMatch);

        return "";
    }
}
