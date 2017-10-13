package com.xx.chinetek.cyproduct.LineStockOut;

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
import com.xx.chinetek.adapter.product.LineStockOut.LineStockOutReturnBillAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.cywms.Receiption.ReceiptBillChoice;
import com.xx.chinetek.model.Material.BarCodeInfo;
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

@ContentView(R.layout.activity_line_stock_out_return_bill_choice)
public class LineStockOutReturnBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    String TAG_GetT_InStockList = "ReceiptBillChoice_GetT_InStockList";
    String TAG_GetT_PalletDetailByBarCode = "ReceiptBillChoice_GetT_PalletDetailByBarCode";
    private final int RESULT_GetT_InStockList = 101;
    private final int RESULT_GetT_PalletDetailByBarCode=102;

    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_GetT_InStockList:
                AnalysisGetT_InStockListJson((String) msg.obj);
                break;
            case RESULT_GetT_PalletDetailByBarCode:
                AnalysisGetT_PalletDetailByBarCodeJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtfilterContent);
                break;
        }
    }

    Context context=LineStockOutReturnBillChoice.this;
    @ViewInject(R.id.lsvChoice)
    ListView lsvChoice;
    @ViewInject(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(edt_filterContent)
    EditText edtfilterContent;
    ArrayList<Receipt_Model> receiptModels;//单据信息
    LineStockOutReturnBillAdapter lineStockOutReturnBillAdapter;
    ArrayList<BarCodeInfo> barCodeInfos;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context=context;
        BaseApplication.toolBarTitle=new ToolBarTitle(getString(R.string.LineStockOutReturnBillChoice),true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=true;
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
        GetT_InStockList(receiptModel);
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
                    StartScanIntent(receiptModels.get(index), null);
                    return false;
                } else {
                    //扫描箱条码
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("BarCode", code);
                    LogUtil.WriteLog(ReceiptBillChoice.class, TAG_GetT_PalletDetailByBarCode, code);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCode, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_PalletDetailByBarCode, null,  URLModel.GetURL().GetPalletDetailByBarCodeForStockOut, params, null);
                    return false;
                }
            }
            CommonUtil.setEditFocus(edtfilterContent);
        }
        return false;
    }

    @Event(value = R.id.lsvChoice, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Receipt_Model receiptModel=(Receipt_Model) lineStockOutReturnBillAdapter.getItem(position);
        StartScanIntent(receiptModel,null);
    }


    void GetT_InStockList(Receipt_Model receiptModel){
        try {
            String ModelJson = GsonUtil.parseModelToJson(receiptModel);
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(LineStockOutReturnBillChoice.class, TAG_GetT_InStockList, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockList, getString(R.string.Msg_GetT_ReturnStockListADF), context, mHandler, RESULT_GetT_InStockList, null,  URLModel.GetURL().GetT_InStockListADF, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetT_InStockListJson(String result){
        try {
            LogUtil.WriteLog(LineStockOutReturnBillChoice.class, TAG_GetT_InStockList, result);
            ReturnMsgModelList<Receipt_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Receipt_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                receiptModels = returnMsgModel.getModelJson();
                if(receiptModels!=null)
                    BindListVIew(receiptModels);
            } else {
                ToastUtil.show(returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            ToastUtil.show(ex.getMessage());
        }
        CommonUtil.setEditFocus(edtfilterContent);
    }

    void AnalysisGetT_PalletDetailByBarCodeJson(String result) {
        LogUtil.WriteLog(ReceiptBillChoice.class, TAG_GetT_PalletDetailByBarCode, result);
        try {
            ReturnMsgModelList<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<BarCodeInfo>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                this.barCodeInfos = returnMsgModel.getModelJson();
                if (barCodeInfos != null) {
                    //调用GetT_InStockList 赋值ERP订单号字段，获取Receipt_Model列表，跳转到扫描界面
                    Receipt_Model receiptModel = new Receipt_Model();
                    receiptModel.setStatus(1);
                    receiptModel.setErpVoucherNo(barCodeInfos.get(0).getErpVoucherNo());
                    GetT_InStockList(receiptModel);
                }
            } else {
                ToastUtil.show(returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            ToastUtil.show(ex.getMessage());
        }
        CommonUtil.setEditFocus(edtfilterContent);
    }


    void StartScanIntent(Receipt_Model receiptModel,ArrayList<BarCodeInfo> barCodeInfo){
        Intent intent=new Intent(context,LineStockOutReturnScan.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("receiptModel",receiptModel);
        bundle.putParcelableArrayList("barCodeInfo",barCodeInfo);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }

    private void BindListVIew(ArrayList<Receipt_Model> receiptModels) {
        lineStockOutReturnBillAdapter=new LineStockOutReturnBillAdapter(context,receiptModels);
        lsvChoice.setAdapter(lineStockOutReturnBillAdapter);
    }
}
