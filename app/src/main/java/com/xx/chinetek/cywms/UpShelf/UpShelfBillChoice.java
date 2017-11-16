package com.xx.chinetek.cywms.UpShelf;

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
import com.xx.chinetek.adapter.wms.Upshelf.UpshelfBillChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;
import com.xx.chinetek.model.WMS.UpShelf.InStockTaskInfo_Model;
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
public class UpShelfBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    String TAG_GetT_InTaskListADF = "UpShelfBillChoice_GetT_InTaskListADF";
    String TAG_GetT_ScanInStockModelADF = "UpShelfBillChoice_GetT_ScanInStockModelADF";
    private final int RESULT_GetT_InTaskListADF = 101;
    private final int RESULT_GetT_ScanInStockModelADF=102;

    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_GetT_InTaskListADF:
               AnalysisGetT_InTaskListADFJson((String) msg.obj);
                break;
            case RESULT_GetT_ScanInStockModelADF:
                AnalysisGetT_ScanInStockModelADFJson((String) msg.obj);
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


    Context context = UpShelfBillChoice.this;
    UpshelfBillChioceItemAdapter upshelfBillChioceItemAdapter;
    ArrayList<InStockTaskInfo_Model> inStockTaskInfoModels;
    ArrayList<StockInfo_Model> stockInfoModels;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.UpShelf_title), false);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitListView();
        edtfilterContent.setText("");
        CommonUtil.setEditFocus(edtfilterContent);
    }

    @Override
    public void onRefresh() {
        inStockTaskInfoModels=new ArrayList<>();
        edtfilterContent.setText("");
        InitListView();
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoice,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        InStockTaskInfo_Model inStockTaskInfoModel=(InStockTaskInfo_Model) upshelfBillChioceItemAdapter.getItem(position);
        StartScanIntent(inStockTaskInfoModel,null);
    }

    @Event(value = R.id.edt_filterContent,type = View.OnKeyListener.class)
    private  boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            if(inStockTaskInfoModels!=null && inStockTaskInfoModels.size()>0) {
                String code = edtfilterContent.getText().toString().trim();
                //扫描单据号、检查单据列表
                InStockTaskInfo_Model inStockTaskInfoModel = new InStockTaskInfo_Model(code);
                int index=inStockTaskInfoModels.indexOf(inStockTaskInfoModel);
                if (index!=-1) {
                    StartScanIntent(inStockTaskInfoModels.get(index), null);
                    return false;
                } else {
                    //扫描箱条码
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("SerialNo", code);
                    params.put("ERPVoucherNo", "");
                    params.put("TaskNo","");
                    params.put("AreaNo", "");
                    params.put("WareHouseID", BaseApplication.userInfo.getWarehouseID()+"");
                    LogUtil.WriteLog(UpShelfBillChoice.class, TAG_GetT_ScanInStockModelADF, code);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_ScanInStockModelADF, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_ScanInStockModelADF, null, URLModel.GetURL().GetT_ScanInStockModelADF, params, null);
                    return false;
                }
            }
           // StartScanIntent(null,null);
            CommonUtil.setEditFocus(edtfilterContent);
        }
        return false;
    }


    /**
     * 初始化加载listview
     */
    private void InitListView() {
        InStockTaskInfo_Model inStockTaskInfoModel=new InStockTaskInfo_Model();
        inStockTaskInfoModel.setStatus(1);
        inStockTaskInfoModel.setWareHouseID(BaseApplication.userInfo.getWarehouseID());
        GetT_InStockTaskInfoList(inStockTaskInfoModel);
    }

    void GetT_InStockTaskInfoList(InStockTaskInfo_Model inStockTaskInfoModel){
        try {
            String ModelJson = GsonUtil.parseModelToJson(inStockTaskInfoModel);
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
           // params.put("WareHouseID",BaseApplication.userInfo.getWarehouseID()+"");
            LogUtil.WriteLog(UpShelfBillChoice.class, TAG_GetT_InTaskListADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InTaskListADF, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_InTaskListADF, null,  URLModel.GetURL().GetT_InTaskListADF, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
            CommonUtil.setEditFocus(edtfilterContent);
        }
    }

    void AnalysisGetT_InTaskListADFJson(String result){
        LogUtil.WriteLog(UpShelfBillChoice.class, TAG_GetT_InTaskListADF,result);

        ReturnMsgModelList<InStockTaskInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<InStockTaskInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            inStockTaskInfoModels=returnMsgModel.getModelJson();
            if (inStockTaskInfoModels != null && inStockTaskInfoModels.size() == 1 && stockInfoModels != null && stockInfoModels.size()!=0)
                    StartScanIntent(inStockTaskInfoModels.get(0), stockInfoModels);
            else
                BindListVIew(inStockTaskInfoModels);
        }else
        {
            MessageBox.Show(context,returnMsgModel.getMessage());
            CommonUtil.setEditFocus(edtfilterContent);
        }
    }

    void AnalysisGetT_ScanInStockModelADFJson(String result){
        LogUtil.WriteLog(UpShelfBillChoice.class, TAG_GetT_ScanInStockModelADF,result);
        ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            stockInfoModels=returnMsgModel.getModelJson();
            if(stockInfoModels!=null) {
                // Receipt_Model receiptModel = new Receipt_Model(barCodeInfo.getBarCode());
                //  int index = receiptModels.indexOf(receiptModel);
                //  if (index != -1) {
                //调用GetT_InStockList 赋值ERP订单号字段，获取Receipt_Model列表，跳转到扫描界面
                InStockTaskInfo_Model inStockTaskInfoModel=new InStockTaskInfo_Model();
                inStockTaskInfoModel.setStatus(1);
                inStockTaskInfoModel.setTaskNo(stockInfoModels.get(0).getTaskNo());
                GetT_InStockTaskInfoList(inStockTaskInfoModel);
                //   } else {
                //     MessageBox.Show(context, R.string.Error_BarcodeNotInList);
                // }
            }
        }else
        {
            MessageBox.Show(context,returnMsgModel.getMessage());
            CommonUtil.setEditFocus(edtfilterContent);
        }
    }

    void StartScanIntent(InStockTaskInfo_Model inStockTaskInfoModel,ArrayList<StockInfo_Model> stockInfoModels){
        Intent intent=new Intent(context,UpShelfScanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("inStockTaskInfoModel",inStockTaskInfoModel);
        bundle.putParcelableArrayList("stockInfoModels",stockInfoModels);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }

    private void BindListVIew(ArrayList<InStockTaskInfo_Model> inStockTaskInfoModels) {
        upshelfBillChioceItemAdapter=new UpshelfBillChioceItemAdapter(context,inStockTaskInfoModels);
        lsvChoice.setAdapter(upshelfBillChioceItemAdapter);
    }

}
