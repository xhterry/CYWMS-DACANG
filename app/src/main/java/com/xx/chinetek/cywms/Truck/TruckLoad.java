package com.xx.chinetek.cywms.Truck;

import android.content.Context;
import android.os.Message;
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
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
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
    private final int RESULT_GetTransportSupplierListADF = 101;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetTransportSupplierListADF:
                AnalysisGetTransportSupplierListADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }

    Context context=TruckLoad.this;
    @ViewInject(R.id.edt_VourcherNo)
    EditText edtVourcherNo;
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

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.Truckload_title), true);
        x.view().inject(this);
        editTextList.add(edtVourcherNo);
        editTextList.add(edtPlateNumber);
        editTextList.add(edtVolume);
        editTextList.add(edtWeight);
        editTextList.add(edtNumber);
        editTextList.add(edtFeight);
        CommonUtil.setEditFocus(edtVourcherNo);
    }

    @Override
    protected void initData() {
        super.initData();
        String voucherNo=getIntent().getStringExtra("VoucherNo");
        edtVourcherNo.setText(voucherNo);
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
        CommonUtil.setEditFocus(edtVourcherNo);
        try {
            Map<String, String> params = new HashMap<>();
            LogUtil.WriteLog(IntentoryAdd.class, TAG_GetTransportSupplierListADF, "");
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetTransportSupplierListADF, getString(R.string.Msg_GetAreanobyCheckno), context, mHandler, RESULT_GetTransportSupplierListADF, null,  URLModel.GetURL().GetTransportSupplierListADF, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetTransportSupplierListADFJson(String result){
        try {
            LogUtil.WriteLog(IntentoryAdd.class, TAG_GetTransportSupplierListADF,result);
            ReturnMsgModelList<TransportSupplierModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<TransportSupplierModel>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                ArrayList<TransportSupplierModel> transportSupplierModels=returnMsgModel.getModelJson();

            }else
                MessageBox.Show(context, returnMsgModel.getMessage());
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

}
