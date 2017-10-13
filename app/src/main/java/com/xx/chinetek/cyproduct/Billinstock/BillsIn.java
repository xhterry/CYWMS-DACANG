package com.xx.chinetek.cyproduct.Billinstock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Switch;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.Pallet.CombinPallet;
import com.xx.chinetek.adapter.product.BillsStockIn.BillAdapter;
import com.xx.chinetek.adapter.wms.QC.QCBillChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Pallet.PalletDetail_Model;
import com.xx.chinetek.model.Production.Wo.WoModel;
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

import static com.xx.chinetek.base.BaseApplication.userInfo;

/**
 * Created by ymh on 2017/8/22.
 */

@ContentView(R.layout.activity_product_bills_in)
public class BillsIn  extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @ViewInject(R.id.SW_WoType)
    Switch SW_WoType;

    @ViewInject(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    @ViewInject(R.id.lsvChoice)
    ListView lsvChoice;

    @ViewInject(R.id.edt_filterContent)
    EditText edt_filterContent;

    Context context = BillsIn.this;
    BillAdapter billAdapter;

    String TAG_GetT_InBill = "OffShelfBillChoice_GetT_InBill";
    private final int RESULT_GetT_InBill = 101;

    String TAG_GetT_Sync = "OffShelfBillChoice_GetT_Sync";
    private final int RESULT_GetT_Sync = 102;


    String TAG_GetT_WoninfoBack = "OffShelfBillChoice_GetT_WoninfoBack";
    private final int RESULT_GetT_WoninfoBack = 103;


    String TAG_GetT_PalletDetailByNoADF="CombinPallet_GetT_PalletDetailByNoADF";
    private final int RESULT_GetT_SerialNoByPalletADF = 104;
    ArrayList<WoModel> WoModels;

    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_GetT_InBill:
                AnalysisGetT_RESULT_GetT_InBillADFJson((String) msg.obj);
                break;
            case RESULT_GetT_Sync:
                AnalysisGetT_RESULT_GetT_SyncWoADFJson((String) msg.obj);
                break;
            case RESULT_GetT_WoninfoBack:
                AnalysisGetT_RESULT_GetT_WoinfoADFJson((String) msg.obj);
                break;
            case RESULT_GetT_SerialNoByPalletADF:
                AnalysisGetT_SerialNoByPalletAD((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edt_filterContent);
                break;
        }
    }
    private WoModel limkWoModel = new WoModel();

    void  AnalysisGetT_RESULT_GetT_WoinfoADFJson(String result){
        try {
            LogUtil.WriteLog(BillsIn.class, TAG_GetT_WoninfoBack, result);
            ReturnMsgModelList<WoModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<WoModel>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                WoModels = returnMsgModel.getModelJson();
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }


    void AnalysisGetT_RESULT_GetT_InBillADFJson(String result){
        try {
            LogUtil.WriteLog(BillsIn.class, TAG_GetT_InBill, result);
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

    void AnalysisGetT_RESULT_GetT_SyncWoADFJson(String result){
        try {
            LogUtil.WriteLog(BillsIn.class, TAG_GetT_Sync, result);
            ReturnMsgModelList<WoModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<WoModel>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                MessageBox.Show(context, "同步单据成功");
                getData();
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }




    @Event(value = R.id.SW_WoType,type = CompoundButton.OnCheckedChangeListener.class)
    private void SwWoTypeCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if (isChecked)
//        {
//            //外销工单
//            getData();
//        }
//        else
//        {
//            //正常工单
//            getData();
//        }


    }

    /**
     * 文本变化事件
     */
    TextWatcher TextWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }



        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                if(!edt_filterContent.getText().toString().equals(""))
                    billAdapter.getFilter().filter(edt_filterContent.getText().toString());
                else{
                    BindListVIew(WoModels);
                }
            }catch (Exception ex){
                MessageBox.Show(context,ex.getMessage());
            }

        }




        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Event(value = R.id.edt_filterContent, type = View.OnKeyListener.class)
    private boolean edtfilterOnKey(View v, int keyCode, KeyEvent event) {
        try{
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
            {
                keyBoardCancle();
                //区分扫描的是条码，工单
                String Fileter = edt_filterContent.getText().toString().trim();
                if (Fileter.length()!=18) {
                    try{
                        String barcode=edt_filterContent.getText().toString().trim();
                        final Map<String, String> params = new HashMap<String, String>();
                        params.put("Barcode", barcode);
                        params.put("PalletModel", "1");
                        LogUtil.WriteLog(BillsIn.class, TAG_GetT_PalletDetailByNoADF, barcode);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByNoADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetT_SerialNoByPalletADF, null,  URLModel.GetURL().GetT_PalletDetailByNoADF, params, null);
                        return false;
                    }catch (Exception ex){
                        MessageBox.Show(context,ex.toString());
                    }
//                if (barCodeInfo!=null){
//                    Fileter=barCodeInfo.getErpVoucherNo();
//                    edt_filterContent.setText(Fileter);
//                }
                }


                boolean flag = true;
                for(int i=0;i<WoModels.size();i++)
                {
                    if(WoModels.get(i).getErpVoucherNo().equals(Fileter))
                    {
                        flag=false;
                    }
                }
                if (flag){
                    if (!Fileter.isEmpty()){
                        Sync(Fileter);
                    }
                }

            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
        }

        return false;

    }


    /*
  解析物料条码扫描
   */
    void AnalysisGetT_SerialNoByPalletAD(String result){
        try {
            LogUtil.WriteLog(CombinPallet.class, TAG_GetT_PalletDetailByNoADF,result);
            ReturnMsgModelList<PalletDetail_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<PalletDetail_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                PalletDetail_Model palletDetailModel = returnMsgModel.getModelJson().get(0);
                BarCodeInfo barCodeInfo = palletDetailModel.getLstBarCode().get(0);
                String Fileter=barCodeInfo.getErpVoucherNo();
                edt_filterContent.setText(Fileter);

            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception e){
            MessageBox.Show(context,e.toString());
        }
    }





    //同步单据的方法
    private void Sync(String Fileter){
        try {
            Fileter=Fileter.replace("\"","");
            Map<String, String> params = new HashMap<>();
//            params.put("No", GsonUtil.parseModelToJson(Fileter));
            params.put("No", Fileter);
            LogUtil.WriteLog(BillsIn.class, TAG_GetT_Sync,Fileter);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_Sync, getString(R.string.Msg_SycWOInfo), context, mHandler,
                    RESULT_GetT_Sync, null, URLModel.GetURL().Sync_WoinfoModel, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }

    @Override
    protected void initViews() {
        try{
            super.initViews();
            BaseApplication.context = context;
            BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.Product_ProductYMH), true);
            x.view().inject(this);
            getData();
            edt_filterContent.addTextChangedListener(TextWatcher);
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
        }

    }


    @Override
    protected void initData() {
        try{
            super.initData();
            mSwipeLayout.setOnRefreshListener(this); //下拉刷新
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
        }

    }

    @Override
    public void onRefresh() {
        WoModels=new ArrayList<>();
        edt_filterContent.setText("");
        getData();
    }

    /**
     * Listview item 点击事件
     */
    @Event(value = R.id.lsvChoice,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            WoModel Model= (WoModel)billAdapter.getItem(position);
            Intent intent;
            if(SW_WoType.isChecked()){
                //外销工单
                intent = new Intent(context,CompleteProductW.class);

            }else{
                //正常工单
                intent = new Intent(context,CompleteProduct.class);
            }
            Bundle bundle=new Bundle();
//        getWoinfo(Model);
            bundle.putParcelable("WoModel",Model);

//        bundle.putString("flag",switch1.isChecked()?"1":"0");
            bundle.putString("flag","0");

            intent.putExtras(bundle);
            startActivityLeft(intent);
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
        }

    }

    void getWoinfo(WoModel model){
        try {
            Map<String, String> params = new HashMap<>();
            params.put("WoInfoJson", GsonUtil.parseModelToJson(model));
//            LogUtil.WriteLog(BillsIn.class, TAG_GetT_InBill,model);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_WoninfoBack, getString(R.string.Msg_GetWOInfo), context, mHandler,
                    RESULT_GetT_WoninfoBack, null,  URLModel.GetURL().GetT_WoinfoModelBack, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }



    void getData(){
        try {
            Map<String, String> params = new HashMap<>();
            String PathUrl="";
//            if(SW_WoType.isChecked()){
//                //外销工单
//                PathUrl=URLModel.GetURL().GetT_OutWoinfoModel;
//            }else{
                //正常工单
                PathUrl=URLModel.GetURL().GetT_WoinfoModel;
//            }
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
//            LogUtil.WriteLog(BillsIn.class, TAG_GetT_InBill,);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InBill, getString(R.string.Msg_GetWOInfo), context, mHandler,
                    RESULT_GetT_InBill, null,  PathUrl, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }

    private void BindListVIew(List<WoModel> WoModels) {
        billAdapter=new BillAdapter(context,WoModels);
        lsvChoice.setAdapter(billAdapter);

    }



}
