package com.xx.chinetek.cywms.Intentory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Intentory.InventoryItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Inventory.Check_Model;
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


@ContentView(R.layout.activity_bill_choice)
public class InventoryBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    String TAG_GetCheckADF = "InventoryBillChoice_GetCheck";
    private final int RESULT_GetCheckADF = 101;

    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_GetCheckADF:
               AnalysisGetCheckADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtfilterContent);
                break;
        }
    }

    @ViewInject(R.id.lsvChoice)
    ListView lsvChoice;
    @ViewInject(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(R.id.edt_filterContent)
    EditText edtfilterContent;



    Context context = InventoryBillChoice.this;
    ArrayList<Check_Model> check_models;//单据信息
    InventoryItemAdapter inventoryItemAdapter;

    int model=-1;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Intentory_title), false);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
        model=getIntent().getIntExtra("model",0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        check_models=new ArrayList<>();
        InitListView();
    }

    @Override
    public void onRefresh() {
        check_models=new ArrayList<>();
        edtfilterContent.setText("");
        InitListView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(model==1)
            getMenuInflater().inflate(R.menu.menu_linemanagel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            Intent intent=new Intent(context,IntentoryAdd.class);
            startActivityLeft(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoice,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Check_Model check_model=(Check_Model) inventoryItemAdapter.getItem(position);
        StartScanIntent(check_model);
    }

    @Event(value = R.id.edt_filterContent,type = View.OnKeyListener.class)
    private  boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            if(check_models!=null && check_models.size()>0) {
                String code = edtfilterContent.getText().toString().trim();
                //扫描单据号、检查单据列表
                Check_Model check_model = new Check_Model(code);
                int index=check_models.indexOf(check_model);
                if (index!=-1)
                    StartScanIntent(check_models.get(index));
                else{
                    MessageBox.Show(context,getString(R.string.No_Task));
                }
            }
          //  StartScanIntent(null);
            CommonUtil.setEditFocus(edtfilterContent);
        }
        return false;
    }


    /**
     * 初始化加载listview
     */
    private void InitListView() {
        try {
            Map<String, String> params = new HashMap<>();
            LogUtil.WriteLog(InventoryBillChoice.class, TAG_GetCheckADF, "");
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetCheckADF, getString(R.string.Msg_Inventory_Load), context, mHandler,
                    RESULT_GetCheckADF, null, model==1?URLModel.GetURL().GetCheckADF:URLModel.GetURL().GetCheckMing, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }


    void AnalysisGetCheckADFJson(String result){
        LogUtil.WriteLog(InventoryBillChoice.class, TAG_GetCheckADF,result);
        ReturnMsgModelList<Check_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Check_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            check_models=returnMsgModel.getModelJson();
            BindListVIew(check_models);
        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
        }
    }



    void StartScanIntent(Check_Model check_model){
        Intent intent=new Intent(context,model==1?IntentoryScan.class:IntentoryFinc.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("check_model",check_model);
        intent.putExtras(bundle);
        intent.putExtra("model",model);
        startActivityLeft(intent);
    }

    private void BindListVIew(ArrayList<Check_Model> check_models) {
        inventoryItemAdapter=new InventoryItemAdapter(context,check_models);
        lsvChoice.setAdapter(inventoryItemAdapter);
    }

}
