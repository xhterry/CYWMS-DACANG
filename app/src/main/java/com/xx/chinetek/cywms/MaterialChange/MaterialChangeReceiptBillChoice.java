package com.xx.chinetek.cywms.MaterialChange;

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
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Receiption.ReceiptBillChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Receiption.Receipt_Model;
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


@ContentView(R.layout.activity_receipt_bill_choice)
public class MaterialChangeReceiptBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    String TAG_GetT_InStockList = "ReceiptBillChoice_GetT_InStockList";
    private final int RESULT_GetT_InStockList = 101;

    Context context = MaterialChangeReceiptBillChoice.this;


    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_GetT_InStockList:
                AnalysisGetT_InStockListJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtfilterContent);
                break;
        }
    }

    @ViewInject(R.id.lsvChoiceReceipt)
    ListView lsvChoiceReceipt;
    @ViewInject(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(R.id.edt_filterContent)
    EditText edtfilterContent;
    @ViewInject(R.id.txt_Suppliername)
    TextView txtSuppliername;
    @ViewInject(R.id.txt_SupplierContent)
    TextView txtSupplierContent;


    ArrayList<Receipt_Model> receiptModels;//单据信息
    ReceiptBillChioceItemAdapter receiptBillChioceItemAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.MaterialChange_title), false);
        x.view().inject(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
            InitListView();
    }

    @Override
    protected void initData() {
        txtSuppliername.setVisibility(View.GONE);
        txtSupplierContent.setVisibility(View.GONE);
        super.initData();
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
    }

    @Override
    public void onRefresh() {
         InitListView();
    }


    /**
     * 初始化加载listview
     */
    private void InitListView() {
        receiptModels=new ArrayList<>();
        edtfilterContent.setText("");
        Receipt_Model receiptModel = new Receipt_Model();
        receiptModel.setStatus(1);
        receiptModel.setVoucherType(30);
        GetT_InStockList(receiptModel);
    }


    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoiceReceipt, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       Receipt_Model receiptModel=(Receipt_Model) receiptBillChioceItemAdapter.getItem(position);
        StartScanIntent(receiptModel);
     }

    @Event(value = edt_filterContent,type = View.OnKeyListener.class)
    private  boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            if(receiptModels!=null && receiptModels.size()>0) {
                String code = edtfilterContent.getText().toString().trim();
                //扫描单据号、检查单据列表
                Receipt_Model receiptModel = new Receipt_Model(code);
                int index=receiptModels.indexOf(receiptModel);
                if (index!=-1) {
                    StartScanIntent(receiptModels.get(index));
                    return false;
                }
            }
           // StartScanIntent(null,null);
            CommonUtil.setEditFocus(edtfilterContent);
        }
        return false;
    }


    void AnalysisGetT_InStockListJson(String result){
        try {
            LogUtil.WriteLog(MaterialChangeReceiptBillChoice.class, TAG_GetT_InStockList, result);
            //Gson gson =new GsonBuilder().registerTypeAdapter(Date.class, new NetDateTimeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            ReturnMsgModelList<Receipt_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Receipt_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                receiptModels = returnMsgModel.getModelJson();
                BindListVIew(receiptModels);
            } else {
                ToastUtil.show(returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            ToastUtil.show(ex.getMessage());
        }
        CommonUtil.setEditFocus(edtfilterContent);
    }


    void GetT_InStockList(Receipt_Model receiptModel){
        try {
            String ModelJson = GsonUtil.parseModelToJson(receiptModel);
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(MaterialChangeReceiptBillChoice.class, TAG_GetT_InStockList, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockList, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_InStockList, null,  URLModel.GetURL().GetT_InStockListADF, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void StartScanIntent(Receipt_Model receiptModel){
        Intent intent=new Intent(context,MaterialChangeReviewBillChoice.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("receiptModel",receiptModel);
        intent.putExtras(bundle);
        startActivityLeft(intent);
        closeActiviry();
    }

    private void BindListVIew(ArrayList<Receipt_Model> receiptModels) {
        receiptBillChioceItemAdapter=new ReceiptBillChioceItemAdapter(context,receiptModels);
        lsvChoiceReceipt.setAdapter(receiptBillChioceItemAdapter);
    }
}

