package com.xx.chinetek.cywms.Truck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.Intentory.IntentoryAdd;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Truck.Address;
import com.xx.chinetek.model.WMS.Truck.CusSup;
import com.xx.chinetek.model.WMS.Truck.TransportSupplierModel;
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

@ContentView(R.layout.activity_truck_load)
public class TruckLoad extends BaseActivity {

    String TAG_GetTransportSupplierListADF="TruckLoad_TAG_GetTransportSupplierListADF";
    String TAG_GetDeliveryInfoAndroid="TruckLoad_TAG_GetDeliveryInfoAndroid";
    String TAG_SaveTransportSupplierListADF="TruckLoad_TAG_SaveTransportSupplierListADF";
    private final int RESULT_GetTransportSupplierListADF = 101;
    private final int RESULT_GetDeliveryInfoAndroid = 102;
    private final int RESULT_SaveTransportSupplierListADF= 103;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetTransportSupplierListADF:
                AnalysisGetTransportSupplierListADFJson((String) msg.obj);
                break;
            case RESULT_GetDeliveryInfoAndroid:
                AnalysisGetDeliveryInfoAndroidJson((String) msg.obj);
                break;
            case RESULT_SaveTransportSupplierListADF:
                AnalysisSaveTransportSupplierListADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }

    Context context=TruckLoad.this;
    @ViewInject(R.id.edt_VourcherNo)
    TextView edtVourcherNo;
    @ViewInject(R.id.edt_PlateNumber)
    EditText edtPlateNumber;
    @ViewInject(R.id.edt_Volume)
    EditText edtVolume;
    @ViewInject(R.id.edt_Weight)
    EditText edtWeight;
    @ViewInject(R.id.edt_Number)
    EditText edtNumber;
    @ViewInject(R.id.edt_Feight)
    EditText edtFeight;
    @ViewInject(R.id.txt_Supplier)
    TextView txtSupplier;
    @ViewInject(R.id.txt_Destina)
    TextView txtDestina;
    @ViewInject(R.id.btn_Submit)
    Button btnSubmit;


    List<EditText> editTextList=new ArrayList<>();

    int SelectTransportSupplierID=-1;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.Truckload_title), true);
        x.view().inject(this);
        editTextList.add(edtPlateNumber);
        editTextList.add(edtVolume);
        editTextList.add(edtWeight);
        editTextList.add(edtNumber);
        editTextList.add(edtFeight);
        CommonUtil.setEditFocus(edtPlateNumber);
    }

    @Override
    protected void initData() {
        super.initData();
        String voucherNo=getIntent().getStringExtra("VoucherNo");
        GetDeliveryInfoAndroid(voucherNo);

    }

    @Event(value = R.id.txt_Supplier,  type = View.OnClickListener.class)
    private void txtSupplierOnClick(View v) {
        keyBoardCancle();
        try {
            SelectTransportSupplierID=-1;
            Map<String, String> params = new HashMap<>();
            LogUtil.WriteLog(IntentoryAdd.class, TAG_GetTransportSupplierListADF, "");
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetTransportSupplierListADF, getString(R.string.Msg_GetTransupplier), context, mHandler, RESULT_GetTransportSupplierListADF, null,  URLModel.GetURL().GetTransportSupplierListADF, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    @Event(value = R.id.txt_Destina,  type = View.OnClickListener.class)
    private void txtDestinaOnClick(View v) {
        keyBoardCancle();
        GetDeliveryInfoAndroid(edtVourcherNo.getText().toString());
    }

    @Event(value = R.id.edt_VourcherNo,type = View.OnKeyListener.class)
    private  boolean edtVourcherNoonKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            for (int i=0;i<editTextList.size()-1;i++) {
                if(editTextList.get(i).getId()==v.getId()){
                    CommonUtil.setEditFocus(editTextList.get(i));
                    break;
                }
            }
        }
        return false;
    }

    @Event(R.id.btn_Submit)
    private void btnSubmit(View view){
        if(Check()){
            TransportSupplierModel transportSupplierModel=new TransportSupplierModel();
            transportSupplierModel.setVoucherNo(edtVourcherNo.getText().toString());
            transportSupplierModel.setCreater(BaseApplication.userInfo.getUserName());
            transportSupplierModel.setPlateNumber(edtPlateNumber.getText().toString());
            transportSupplierModel.setVolume(Float.parseFloat(edtVolume.getText().toString()));
            transportSupplierModel.setWeight(Float.parseFloat(edtWeight.getText().toString()));
            transportSupplierModel.setCartonNum(Float.parseFloat(edtNumber.getText().toString()));
            transportSupplierModel.setFeight(Float.parseFloat(edtFeight.getText().toString()));
            transportSupplierModel.setTransportSupplierID(SelectTransportSupplierID);
            transportSupplierModel.setDestina(txtDestina.getText().toString());
            try {
                String ModelJson = GsonUtil.parseModelToJson(transportSupplierModel);
                final Map<String, String> params = new HashMap<String, String>();
                params.put("ModelJson", ModelJson);
                LogUtil.WriteLog(TruckLoad.class, TAG_SaveTransportSupplierListADF, ModelJson);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveTransportSupplierListADF, getString(R.string.Msg_GetT_GetT_QualityListADF), context, mHandler, RESULT_SaveTransportSupplierListADF, null,  URLModel.GetURL().SaveTransportSupplierListADF, params, null);
            } catch (Exception ex) {
                MessageBox.Show(context, ex.getMessage());
            }
        }
    }

    void  GetDeliveryInfoAndroid(String  voucherNo){
        if(!TextUtils.isEmpty(voucherNo)){
            edtVourcherNo.setText(voucherNo);
            try {
                Map<String, String> params = new HashMap<>();
                params.put("Erpvoucherno",voucherNo);
                LogUtil.WriteLog(TruckLoad.class, TAG_GetDeliveryInfoAndroid, voucherNo);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetDeliveryInfoAndroid, getString(R.string.Msg_GetDeliveryInfoAndroid), context, mHandler, RESULT_GetDeliveryInfoAndroid, null,  URLModel.GetURL().GetDeliveryInfoAndroid, params, null);
            } catch (Exception ex) {
                MessageBox.Show(context, ex.getMessage());
            }
        }
    }


    void AnalysisGetTransportSupplierListADFJson(String result){
        try {
            LogUtil.WriteLog(TruckLoad.class, TAG_GetTransportSupplierListADF,result);
            ReturnMsgModelList<TransportSupplierModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<TransportSupplierModel>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                ArrayList<TransportSupplierModel> transportSupplierModels=returnMsgModel.getModelJson();
                if(transportSupplierModels!=null && transportSupplierModels.size()!=0) {
                    SelectTransportSupplier(transportSupplierModels);
                }
            }else
                MessageBox.Show(context, returnMsgModel.getMessage());
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetDeliveryInfoAndroidJson(String result){
        try {
            LogUtil.WriteLog(TruckLoad.class, TAG_GetDeliveryInfoAndroid,result);
            ReturnMsgModel<CusSup> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<CusSup>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                CusSup cusSup=returnMsgModel.getModelJson();
                if(cusSup!=null && cusSup.getAddresses()!=null && cusSup.getAddresses().size()!=0) {
                    SelectAddresses(cusSup.getAddresses());
                }
            }else
                MessageBox.Show(context, returnMsgModel.getMessage());
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisSaveTransportSupplierListADFJson(String result){
        try {
            LogUtil.WriteLog(TruckLoad.class, TAG_SaveTransportSupplierListADF,result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
              closeActiviry();
            }else
                MessageBox.Show(context, returnMsgModel.getMessage());
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void SelectTransportSupplier(final ArrayList<TransportSupplierModel> transportSupplierModels){
        List<String> transportSupplierList = new ArrayList<String>();
        if(transportSupplierModels==null) return;
        if(transportSupplierModels.size()>1) {
            for (TransportSupplierModel transportSupplierModel : transportSupplierModels) {
                transportSupplierList.add(transportSupplierModel.getTransportSupplierID()+"|"+transportSupplierModel.getTransportSupplierName());
           }
            final String[] items = transportSupplierList.toArray(new String[0]);
            new AlertDialog.Builder(context).setCancelable(false).setTitle(getResources().getString(R.string.activity_Truck_TransupplierChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法存根
                            SelectTransportSupplierID = transportSupplierModels.get(which).getTransportSupplierID();
                            txtSupplier.setText(transportSupplierModels.get(which).getTransportSupplierName());
                            dialog.dismiss();
                        }
                    }).show();
        }else{
            SelectTransportSupplierID = transportSupplierModels.get(0).getTransportSupplierID();
            txtSupplier.setText(transportSupplierModels.get(0).getTransportSupplierName());
        }
    }

    void SelectAddresses(final ArrayList<Address> addresses){
        List<String> addressesList = new ArrayList<String>();
        if(addresses.size()>1) {
            for (Address address : addresses) {
                addressesList.add(address.getAddress());
            }
            final String[] items = addressesList.toArray(new String[0]);
            new AlertDialog.Builder(context).setCancelable(false).setTitle(getResources().getString(R.string.activity_Truck_AddressChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法存根
                            txtDestina.setText(addresses.get(which).getAddress());
                            dialog.dismiss();
                        }
                    }).show();
        }else{
            if(!TextUtils.isEmpty(addresses.get(0).getAddress())){
                txtDestina.setText(addresses.get(0).getAddress());
            }else{
                txtDestina.setText("无");
            }
        }
    }

    boolean Check(){
        ConstraintLayout sLinerLayout = (ConstraintLayout)findViewById(R.id.layout_TruckLoad);
        boolean returnFlag=true;
        for (int i = 0; i < sLinerLayout.getChildCount(); i++) {
            View v=sLinerLayout.getChildAt(i);
            if ( v instanceof EditText){
                EditText mTextView = (EditText)sLinerLayout.getChildAt(i);
                //根据ID获取RadioButton的实例
                EditText tv = (EditText)findViewById(mTextView.getId());
                if(TextUtils.isEmpty(tv.getText().toString().trim())){
                    MessageBox.Show(context,"请输入完整数据");
                    returnFlag=false;
                    CommonUtil.setEditFocus(tv);
                    break;
                }
                if(!CommonUtil.isFloat(tv.getText().toString().trim())){
                    returnFlag=false;
                    MessageBox.Show(context,"数据格式不正确");
                    CommonUtil.setEditFocus(tv);
                }
            }

//            if ( v instanceof TextView){
//                TextView mTextView = (TextView)sLinerLayout.getChildAt(i);
//                //根据ID获取RadioButton的实例
//                TextView tv = (TextView)findViewById(mTextView.getId());
//                if(TextUtils.isEmpty(tv.getText().toString().trim())){
//                    MessageBox.Show(context,"请选择承运商或目的地");
//                    returnFlag=false;
//                    break;
//                }
//            }

        }
        return returnFlag;
    }

}
