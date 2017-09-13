package com.xx.chinetek.cyproduct.Manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.product.Manage.LineManageItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cyproduct.WoBillChoice;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Production.Manage.LineManageModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_line_manage)
public class LineManage extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{


    String TAG_GetT_LineManageInfoModel="LineManage_GetT_LineManageInfoModel";
    private final int RESULT_GetT_LineManageInfoModel=101;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetT_LineManageInfoModel:
                AnalysisGetT_LineManageInfoModelJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                mSwipeLayout.setRefreshing(false);
                break;
        }
    }

    Context context=LineManage.this;

    @ViewInject(R.id.LsvLineManage)
    ListView LsvLineManage;
    @ViewInject(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    ArrayList<LineManageModel> lineManageModels=new ArrayList<>();
    LineManageItemAdapter lineManageItemAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_Line_subtitle), true);
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
        lineManageModels=new ArrayList<>();
//        lineManageModels=getdata();
        BindListView(lineManageModels);
        GetLineManages();
    }

    @Override
    public void onRefresh() {
        GetLineManages();
    }

    @Event(value = R.id.LsvLineManage,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, ProductManage.class);
        LineManageModel lineManageModel=(LineManageModel)lineManageItemAdapter.getItem(position);
        Bundle bundle=new Bundle();
        bundle.putParcelable("lineManageModel",lineManageModel);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_linemanagel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            Intent intent = new Intent();
            BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_manage_subtitle), true);
            intent.setClass(context, WoBillChoice.class);
            startActivityLeft(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    void GetLineManages(){
        try {
//            String ModelJson = GsonUtil.parseModelToJson(lineManageModels);
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
//            params.put("ModelJson", ModelJson);
//            LogUtil.WriteLog(LineManage.class, TAG_GetT_LineManageInfoModel, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_LineManageInfoModel, getString(R.string.Mag_GetT_LineManageInfoModel), context, mHandler, RESULT_GetT_LineManageInfoModel, null,  URLModel.GetURL().GetT_LineManageInfoModel, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetT_LineManageInfoModelJson(String result){
        try {
            mSwipeLayout.setRefreshing(false);
            LogUtil.WriteLog(LineManage.class, TAG_GetT_LineManageInfoModel, result);
            ReturnMsgModelList<LineManageModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<LineManageModel>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                lineManageModels = returnMsgModel.getModelJson();
                if (lineManageModels != null ){
                    BindListView(lineManageModels);
                }

            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){

            MessageBox.Show(context,ex.getMessage());
        }

    }

    void BindListView(ArrayList<LineManageModel> lineManageModels){
        lineManageItemAdapter=new LineManageItemAdapter(context,lineManageModels);
        LsvLineManage.setAdapter(lineManageItemAdapter);
    }

//    ArrayList<LineManageModel> getdata(){
//        ArrayList<LineManageModel> lineManageModels=new ArrayList<>();
//        for(int i=0;i<7;i++){
//            LineManageModel lineManageModel=new LineManageModel();
//            lineManageModel.setWoVoucherNo("W12345678"+i);
//            lineManageModel.setWoErpVoucherNo("CY1-WO-1707170000"+i);
//            lineManageModel.setWoBatchNo("批123bn");
//            lineManageModel.setEquipID("设备ID");
//            lineManageModel.setProductLineNo("产线ID");
//            lineManageModel.setProductTeamNo("班组ID");
//            lineManageModels.add(lineManageModel);
//        }
//        return lineManageModels;
//    }

}
