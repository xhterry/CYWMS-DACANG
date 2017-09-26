package com.xx.chinetek.cyproduct;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.product.BillsStockIn.BillAdapter;
import com.xx.chinetek.adapter.product.WoBillChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cyproduct.LineStockIn.LineStockInReturn;
import com.xx.chinetek.cyproduct.LineStockOut.LineStockOutMaterial;
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
import java.util.List;
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
        edtfilterContent.addTextChangedListener(TextWatcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetWoinfoModel();
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
            if(!edtfilterContent.getText().toString().equals(""))
                woBillChioceItemAdapter.getFilter().filter(edtfilterContent.getText().toString());
            else{
                BindListView(woModels);
            }
        }




        @Override
        public void afterTextChanged(Editable s) {

        }
    };


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
        Intent intent = new Intent();
        if(BaseApplication.toolBarTitle.Title.equals("生产记录")) {
            intent.setClass(context, ProductManageAdd.class);
            closeActiviry();
        }else if(BaseApplication.toolBarTitle.Title.equals("退料入库")){
            intent.setClass(context, LineStockInReturn.class);
        }else if(BaseApplication.toolBarTitle.Title.equals("领料出库")){
            intent.setClass(context, LineStockOutMaterial.class);
        }
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

}
