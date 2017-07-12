package com.xx.chinetek.cyproduct.Receiption;

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
import com.xx.chinetek.adapter.Receiption.ReceiptBillChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Pallet.PalletDetail_Model;
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
import java.util.List;
import java.util.Map;

import static com.xx.chinetek.cywms.R.id.edt_filterContent;


@ContentView(R.layout.activity_receipt_bill_choice)
public class ReceiptBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    String TAG_GetT_InStockList = "ReceiptBillChoice_GetT_InStockList";
    String TAG_GetT_PalletDetailByBarCode = "ReceiptBillChoice_GetT_PalletDetailByBarCode";
    private final int RESULT_GetT_InStockList = 101;
    private final int RESULT_GetT_PalletDetailByBarCode=102;

    Context context = ReceiptBillChoice.this;


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

    List<PalletDetail_Model> palletDetailModels;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_receipt_title), true);
        x.view().inject(this);
        txtSupplierContent.setVisibility(View.GONE);
        txtSuppliername.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitListView();
    }

    @Override
    protected void initData() {
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
        palletDetailModels=new ArrayList<>();
        receiptModels=new ArrayList<>();
        edtfilterContent.setText("");
        Receipt_Model receiptModel = new Receipt_Model();
        receiptModel.setStatus(1);
        GetT_InStockList(receiptModel);
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoiceReceipt, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       Receipt_Model receiptModel=(Receipt_Model) receiptBillChioceItemAdapter.getItem(position);
        StartScanIntent(receiptModel,null);
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
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCode, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_PalletDetailByBarCode, null,  URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);
                    return false;
                }
            }
            StartScanIntent(null,null);
            CommonUtil.setEditFocus(edtfilterContent);
        }
        return false;
    }


    void AnalysisGetT_InStockListJson(String result){
        try {
            LogUtil.WriteLog(ReceiptBillChoice.class, TAG_GetT_InStockList, result);
            //Gson gson =new GsonBuilder().registerTypeAdapter(Date.class, new NetDateTimeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            ReturnMsgModelList<Receipt_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Receipt_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                receiptModels = returnMsgModel.getModelJson();
                if (receiptModels != null && receiptModels.size() == 1 && palletDetailModels != null && palletDetailModels.size()!=0)
                    StartScanIntent(receiptModels.get(0), palletDetailModels.get(0));
                else
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
            ReturnMsgModelList<PalletDetail_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<PalletDetail_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                this.palletDetailModels = returnMsgModel.getModelJson();
                if (palletDetailModels != null) {
                    // Receipt_Model receiptModel = new Receipt_Model(barCodeInfo.getBarCode());
                    //  int index = receiptModels.indexOf(receiptModel);
                    //  if (index != -1) {
                    //调用GetT_InStockList 赋值ERP订单号字段，获取Receipt_Model列表，跳转到扫描界面
                    Receipt_Model receiptModel = new Receipt_Model();
                    receiptModel.setStatus(1);
                    receiptModel.setErpVoucherNo(palletDetailModels.get(0).getErpVoucherNo());
                    GetT_InStockList(receiptModel);
                    //   } else {
                    //     MessageBox.Show(context, R.string.Error_BarcodeNotInList);
                    // }
                }
            } else {
                ToastUtil.show(returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
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
            LogUtil.WriteLog(ReceiptBillChoice.class, TAG_GetT_InStockList, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockList, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_InStockList, null,  URLModel.GetURL().GetT_InStockListADF, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void StartScanIntent(Receipt_Model receiptModel,PalletDetail_Model palletDetailModel){
        Intent intent=new Intent(context, ReceiptionScan.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("receiptModel",receiptModel);
        bundle.putParcelable("palletDetailModel",palletDetailModel);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }

    private void BindListVIew(ArrayList<Receipt_Model> receiptModels) {
        receiptBillChioceItemAdapter=new ReceiptBillChioceItemAdapter(context,receiptModels);
        lsvChoiceReceipt.setAdapter(receiptBillChioceItemAdapter);
    }
}

