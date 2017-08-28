package com.xx.chinetek.cyproduct;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.product.WoBillChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cyproduct.Manage.ProductManageAdd;
import com.xx.chinetek.cywms.R;
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
import java.util.Map;

import static com.xx.chinetek.cywms.R.id.edt_filterContent;


@ContentView(R.layout.activity_bill_choice)
public class WoBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{


    String TAG_GetT_WoinfoModel="WoBillChoice_GetT_WoinfoModel";
    private final int RESULT_GetT_WoinfoModel=101;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetT_WoinfoModel:
                AnalysisGetT_WoinfoModelJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                mSwipeLayout.setRefreshing(false);
                break;
        }
    }

    @ViewInject(R.id.lsvChoice)
    ListView lsvChoice;
    @ViewInject(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(edt_filterContent)
    EditText edtfilterContent;

    Context context = WoBillChoice.this;
    WoBillChioceItemAdapter woBillChioceItemAdapter;
    ArrayList<WoModel> woModels;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);

    }

    @Override
    protected  void initData(){
        super.initData();
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetWoinfoModel();

    }

    @Override
    public void onRefresh() {
        GetWoinfoModel();
    }


    @Event(value = R.id.edt_filterContent,type = View.OnKeyListener.class)
    private  boolean edtfilterContentonKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            CommonUtil.setEditFocus(edtfilterContent);
        }
        return false;
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoice,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WoModel woModel = (WoModel) woBillChioceItemAdapter.getItem(position);
        Intent intent = new Intent(context, ProductManageAdd.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("woModel", woModel);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }


    void GetWoinfoModel(){
        try {
            Map<String, String> params = new HashMap<>();
            String userJson=GsonUtil.parseModelToJson(BaseApplication.userInfo);
            params.put("UserJson",userJson );
            LogUtil.WriteLog(WoBillChoice.class, TAG_GetT_WoinfoModel, userJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_WoinfoModel, getString(R.string.Mag_GetT_WoinfoModel), context, mHandler, RESULT_GetT_WoinfoModel, null,  URLModel.GetURL().GetT_WoinfoModel, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetT_WoinfoModelJson(String result){
        try {
            mSwipeLayout.setRefreshing(false);
            LogUtil.WriteLog(WoBillChoice.class, TAG_GetT_WoinfoModel, result);
            ReturnMsgModelList<WoModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<WoModel>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                woModels = returnMsgModel.getModelJson();
                if (woModels != null ){
                    BindListView(woModels);
                }

            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){

            MessageBox.Show(context,ex.getMessage());
        }

    }

    void BindListView(ArrayList<WoModel> woModels){
        woBillChioceItemAdapter=new WoBillChioceItemAdapter(context,woModels);
        lsvChoice.setAdapter(woBillChioceItemAdapter);
    }

//    ArrayList<WoModel> getData(){
//        ArrayList<WoModel> woModels=new ArrayList<>();
//        for(int i=0;i<10;i++){
//            WoModel woModel=new WoModel();
//            woModel.setVoucherNo("工单号W123099"+i);
//            woModel.setErpVoucherNo("ERP单号E12333"+i);
//            woModel.setBatchNo("批次号BC123"+i);
//            woModel.setStrVoucherType("工单领料单");
//            woModel.setStrongHoldName("据点"+i);
//            woModel.setDepartmentName("部门"+i);
//            woModel.setMaterialNo("M123333"+i);
//            woModel.setMaterialDesc("工单物料名称"+i);
//            woModels.add(woModel);
//        }
//        return woModels;
//    }
}
