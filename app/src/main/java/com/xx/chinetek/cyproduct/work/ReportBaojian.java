package com.xx.chinetek.cyproduct.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.product.BillsStockIn.BillAdapter;
import com.xx.chinetek.adapter.product.BillsStockIn.WoBatchAlldapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cyproduct.Billinstock.BillsIn;
import com.xx.chinetek.cyproduct.Billinstock.CompleteProduct;
import com.xx.chinetek.cyproduct.Billinstock.CompleteProductW;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.UerInfo;
import com.xx.chinetek.model.User.UserInfo;
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

@ContentView(R.layout.activity_report_baojian)
public class ReportBaojian extends BaseActivity {

    Context context=ReportBaojian.this;

    @ViewInject(R.id.txtNo)
    TextView txtNo;

    @ViewInject(R.id.txtNumTC)
    TextView txtNumT;


    @ViewInject(R.id.editText)
    EditText edtBarcode;

    @ViewInject(R.id.butO)
    Button butO;


    @ViewInject(R.id.lstpi)
    ListView lstpi;

    WoModel womodel;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);

    }

    @Override
    protected void initData() {
        super.initData();
        try{
            CommonUtil.setEditFocus(edtBarcode);
            womodel=getIntent().getParcelableExtra("WoModel");
            String NumT=getIntent().getStringExtra("NumT");

            txtNo.setText(womodel.getErpVoucherNo());
            txtNumT.setText(NumT);
            getData();
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }

    }

    @Event(value = R.id.editText, type = View.OnKeyListener.class)
    private boolean edtfilterOnKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            try{
                keyBoardCancle();
                String barcode = edtBarcode.getText().toString().trim();
                final Map<String, String> params = new HashMap<String, String>();
                params.put("BarCode", barcode);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_BatchNoBySerialnoForOnlyADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetT_BatchNoBySerialnoForOnlyADF, null,  URLModel.GetURL().GetBatchNoBySerialnoForOnly, params, null);
                return false;
            }catch (Exception ex){
                MessageBox.Show(context,ex.toString());
            }
        }
        return false;
    }








    @Event(value = {R.id.butO},type = View.OnClickListener.class)
    private void onClick(View view) {
        try{
            if(edtBarcode.getText().toString().isEmpty()||edtBarcode.getText().toString().length()>10){
                MessageBox.Show(context, "完工报检批次号输入不正确！");
                return;
            }else{
                womodel.setBatchNo(edtBarcode.getText().toString());
            }

            if(womodel.getBatchNo()==null||womodel.getBatchNo().toString().isEmpty()){
                MessageBox.Show(context, "完工报检批次号不能为空！");
            }else{
                final Map<String, String> params = new HashMap<String, String>();
                UerInfo user =BaseApplication.userInfo;
                user.setErpVoucherNo(womodel.getErpVoucherNo().toString());
                params.put("UserJson", GsonUtil.parseModelToJson(user));
                params.put("WoInfoJson", GsonUtil.parseModelToJson(womodel));
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PostBaoJianByListWoinfoADF, getString(R.string.Msg_Post), context, mHandler, RESULT_Msg_PostBaoJianByListWoinfoADF, null, URLModel.GetURL().PostBaoJianByListWoinfo, params, null);
            }
        }catch (Exception ex){

            MessageBox.Show(context, ex.getMessage());
        }

    }


    String TAG_GetT_BatchNoBySerialnoForOnlyADF = "TAG_GetT_BatchNoBySerialnoForOnlyADF";
    private final int RESULT_GetT_BatchNoBySerialnoForOnlyADF = 101;

    String TAG_PostBaoJianByListWoinfoADF="LineStockOutProduct_GetT_SaveInStockModelADF";//报检
    private final int RESULT_Msg_PostBaoJianByListWoinfoADF=102;


    String TAG_GetT_BaojianAll="LineStockOutProduct_GetT_BaojianAllADF";
    private final int RESULT_GetT_BaojianAll=103;


    @Override
    public void onHandleMessage(Message msg) {
//        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_GetT_BaojianAll:
                GetT_BaojianallADF((String)msg.obj);
                break;
            case RESULT_GetT_BatchNoBySerialnoForOnlyADF:
                GetT_BatchNoBySerialnoForOnlyADF((String)msg.obj);
                break;

            case RESULT_Msg_PostBaoJianByListWoinfoADF:
                AnalysisetT_GetPostBaoJianByListWoinfoJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
//                CommonUtil.setEditFocus(edt_filterContent);
                break;
        }
    }

    void GetT_BatchNoBySerialnoForOnlyADF(String result){
        try {
            ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S"))
            {
                edtBarcode.setText(returnMsgModel.getTaskNo());
            }
            else{
                edtBarcode.setText("");
                MessageBox.Show(context, returnMsgModel.getMessage());
            }

        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
            CommonUtil.setEditFocus(edtBarcode);
        }
    }



    void AnalysisetT_GetPostBaoJianByListWoinfoJson(String result){
        try {
            ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S"))
            {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
            else{
                MessageBox.Show(context, returnMsgModel.getMessage());
            }

        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
            CommonUtil.setEditFocus(edtBarcode);
        }
    }

    ArrayList<WoModel> WoModels;
    WoBatchAlldapter wobatchalldapter;
    void GetT_BaojianallADF(String result){
        try {
            LogUtil.WriteLog(ReportBaojian.class, TAG_GetT_BaojianAll, result);
            ReturnMsgModelList<WoModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<WoModel>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                WoModels = returnMsgModel.getModelJson();
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
                BindListVIew(null);
            }
            if (WoModels != null)
                BindListVIew(WoModels);
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void getData(){
        try {
            Map<String, String> params = new HashMap<>();
            params.put("ErpVoucherNo", womodel.getErpVoucherNo());
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_BaojianAll, getString(R.string.Msg_GetWOInfo), context, mHandler,
                    RESULT_GetT_BaojianAll, null,  URLModel.GetURL().GetWoBanGongListByErpVoucherNoForBaoJian, params, null);
        } catch (Exception ex) {
//            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }

    private void BindListVIew(List<WoModel> WoModels) {
        wobatchalldapter=new WoBatchAlldapter(context,WoModels);
        lstpi.setAdapter(wobatchalldapter);

    }

    /**
     * Listview item 点击事件
     */
    @Event(value = R.id.lstpi,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WoModel Model= (WoModel)wobatchalldapter.getItem(position);
        edtBarcode.setText(Model.getBatchNo().toString());
    }


}
