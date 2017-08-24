//package com.xx.chinetek.cyproduct.Receiption;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Message;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ListView;
//
//import com.android.volley.Request;
//import com.google.gson.reflect.TypeToken;
//import com.xx.chinetek.adapter.product.Receiption.ReceiptionBillChoiceItemAdapter;
//import com.xx.chinetek.base.BaseActivity;
//import com.xx.chinetek.base.BaseApplication;
//import com.xx.chinetek.base.ToolBarTitle;
//import com.xx.chinetek.cywms.R;
//import com.xx.chinetek.model.WMS.OffShelf.OutStockTaskInfo_Model;
//import com.xx.chinetek.model.Pallet.PalletDetail_Model;
//import com.xx.chinetek.model.ReturnMsgModelList;
//import com.xx.chinetek.model.URLModel;
//import com.xx.chinetek.util.Network.NetworkError;
//import com.xx.chinetek.util.Network.RequestHandler;
//import com.xx.chinetek.util.dialog.MessageBox;
//import com.xx.chinetek.util.dialog.ToastUtil;
//import com.xx.chinetek.util.function.CommonUtil;
//import com.xx.chinetek.util.function.GsonUtil;
//import com.xx.chinetek.util.log.LogUtil;
//
//import org.xutils.view.annotation.ContentView;
//import org.xutils.view.annotation.Event;
//import org.xutils.view.annotation.ViewInject;
//import org.xutils.x;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//@ContentView(R.layout.activity_product_receipt_bill_choice)
//public class ReceiptBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
//
//    String TAG_GetT_outStockTaskInfoModel = "ReceiptBillChoice_GetT_OutTaskListADF";
//    String TAG_GetT_PalletDetailByBarCode = "ReceiptBillChoice_GetT_PalletDetailByBarCode";
//    private final int RESULT_GetT_InStockList = 101;
//    private final int RESULT_GetT_PalletDetailByBarCode=102;
//
//    @Override
//    public void onHandleMessage(Message msg) {
//        mSwipeLayout.setRefreshing(false);
//        switch (msg.what) {
//            case RESULT_GetT_InStockList:
//                AnalysisGetT_InStockListJson((String) msg.obj);
//                break;
//            case RESULT_GetT_PalletDetailByBarCode:
//                AnalysisGetT_PalletDetailByBarCodeJson((String) msg.obj);
//                break;
//            case NetworkError.NET_ERROR_CUSTOM:
//                ToastUtil.show("获取请求失败_____"+ msg.obj);
//                CommonUtil.setEditFocus(edtfilterContent);
//                break;
//        }
//    }
//    Context context = ReceiptBillChoice.this;
//    @ViewInject(R.id.lsvChoiceReceipt)
//    ListView lsvChoiceReceipt;
//    @ViewInject(R.id.mSwipeLayout)
//    SwipeRefreshLayout mSwipeLayout;
//    @ViewInject(R.id.edt_filterContent)
//    EditText edtfilterContent;
//
//
//    ArrayList<OutStockTaskInfo_Model> outStockTaskInfoModels;//单据信息
//    ReceiptionBillChoiceItemAdapter receiptionBillChoiceItemAdapter;
//
//    List<PalletDetail_Model> palletDetailModels;
//
//    @Override
//    protected void initViews() {
//        super.initViews();
//        BaseApplication.context = context;
//        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_receipt_title), true);
//        x.view().inject(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        InitListView();
//    }
//
//    @Override
//    protected void initData() {
//        super.initData();
//        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
//    }
//
//    @Override
//    public void onRefresh() {
//        InitListView();
//    }
//
//
//    /**
//     * 初始化加载listview
//     */
//    private void InitListView() {
//        palletDetailModels=new ArrayList<>();
//        outStockTaskInfoModels=new ArrayList<>();
//        edtfilterContent.setText("");
//        OutStockTaskInfo_Model outStockTaskInfoModel = new OutStockTaskInfo_Model();
//        outStockTaskInfoModel.setStatus(1);
//        GetT_OutStockTaskList(outStockTaskInfoModel);
//    }
//
//    /**
//     * Listview item点击事件
//     */
//    @Event(value = R.id.lsvChoiceReceipt, type = AdapterView.OnItemClickListener.class)
//    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//       OutStockTaskInfo_Model outStockTaskInfoModel=(OutStockTaskInfo_Model) receiptionBillChoiceItemAdapter.getItem(position);
//        StartScanIntent(outStockTaskInfoModel,null);
//     }
//
//    @Event(value = R.id.edt_filterContent,type = View.OnKeyListener.class)
//    private  boolean onKey(View v, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
//        {
//            if(outStockTaskInfoModels!=null && outStockTaskInfoModels.size()>0) {
//                String code = edtfilterContent.getText().toString().trim();
//                //扫描单据号、检查单据列表
//                OutStockTaskInfo_Model outStockTaskInfoModel = new OutStockTaskInfo_Model(code);
//                int index=outStockTaskInfoModels.indexOf(outStockTaskInfoModel);
//                if (index!=-1) {
//                    StartScanIntent(outStockTaskInfoModels.get(index), null);
//                    return false;
//                } else {
//                    //扫描箱条码
//                    final Map<String, String> params = new HashMap<String, String>();
//                    params.put("BarCode", code);
//                    LogUtil.WriteLog(ReceiptBillChoice.class, TAG_GetT_PalletDetailByBarCode, code);
//                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCode, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_PalletDetailByBarCode, null,  URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);
//                    return false;
//                }
//            }
//            StartScanIntent(null,null);
//            CommonUtil.setEditFocus(edtfilterContent);
//        }
//        return false;
//    }
//
//
//    void AnalysisGetT_InStockListJson(String result){
//        try {
//            LogUtil.WriteLog(ReceiptBillChoice.class, TAG_GetT_outStockTaskInfoModel, result);
//            //Gson gson =new GsonBuilder().registerTypeAdapter(Date.class, new NetDateTimeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//            ReturnMsgModelList<OutStockTaskInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutStockTaskInfo_Model>>() {
//            }.getType());
//            if (returnMsgModel.getHeaderStatus().equals("S")) {
//                outStockTaskInfoModels = returnMsgModel.getModelJson();
//                if (outStockTaskInfoModels != null && outStockTaskInfoModels.size() == 1 && palletDetailModels != null && palletDetailModels.size()!=0)
//                    StartScanIntent(outStockTaskInfoModels.get(0), palletDetailModels.get(0));
//                else
//                    BindListVIew(outStockTaskInfoModels);
//            } else {
//                ToastUtil.show(returnMsgModel.getMessage());
//            }
//        }catch (Exception ex){
//            ToastUtil.show(ex.getMessage());
//        }
//        CommonUtil.setEditFocus(edtfilterContent);
//    }
//
//    void AnalysisGetT_PalletDetailByBarCodeJson(String result) {
//        LogUtil.WriteLog(ReceiptBillChoice.class, TAG_GetT_PalletDetailByBarCode, result);
//        try {
//            ReturnMsgModelList<PalletDetail_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<PalletDetail_Model>>() {
//            }.getType());
//            if (returnMsgModel.getHeaderStatus().equals("S")) {
//                this.palletDetailModels = returnMsgModel.getModelJson();
//                if (palletDetailModels != null) {
//                    // Receipt_Model receiptModel = new Receipt_Model(barCodeInfo.getBarCode());
//                    //  int index = receiptModels.indexOf(receiptModel);
//                    //  if (index != -1) {
//                    //调用GetT_InStockList 赋值ERP订单号字段，获取Receipt_Model列表，跳转到扫描界面
//                    OutStockTaskInfo_Model outStockTaskInfoModel = new OutStockTaskInfo_Model();
//                    outStockTaskInfoModel.setStatus(1);
//                    outStockTaskInfoModel.setErpVoucherNo(palletDetailModels.get(0).getErpVoucherNo());
//                    GetT_OutStockTaskList(outStockTaskInfoModel);
//                    //   } else {
//                    //     MessageBox.Show(context, R.string.Error_BarcodeNotInList);
//                    // }
//                }
//            } else {
//                ToastUtil.show(returnMsgModel.getMessage());
//            }
//        } catch (Exception ex) {
//            ToastUtil.show(ex.getMessage());
//        }
//        CommonUtil.setEditFocus(edtfilterContent);
//    }
//    void GetT_OutStockTaskList(OutStockTaskInfo_Model outStockTaskInfoModel){
//        try {
//            String ModelJson = GsonUtil.parseModelToJson(outStockTaskInfoModel);
//            Map<String, String> params = new HashMap<>();
//            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
//            params.put("ModelJson", ModelJson);
//            LogUtil.WriteLog(ReceiptBillChoice.class, TAG_GetT_outStockTaskInfoModel, ModelJson);
//            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_outStockTaskInfoModel, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_InStockList, null,  URLModel.GetURL().GetT_OutTaskListADF, params, null);
//        } catch (Exception ex) {
//            mSwipeLayout.setRefreshing(false);
//            MessageBox.Show(context, ex.getMessage());
//        }
//    }
//
//    void StartScanIntent(OutStockTaskInfo_Model outStockTaskInfoModel,PalletDetail_Model palletDetailModel){
//        Intent intent=new Intent(context, ReceiptionScan.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("outStockTaskInfoModel",outStockTaskInfoModel);
//        bundle.putParcelable("palletDetailModel",palletDetailModel);
//        intent.putExtras(bundle);
//        startActivityLeft(intent);
//    }
//
//    private void BindListVIew(ArrayList<OutStockTaskInfo_Model> outStockTaskInfoModels) {
//        receiptionBillChoiceItemAdapter =new ReceiptionBillChoiceItemAdapter(context,outStockTaskInfoModels);
//        lsvChoiceReceipt.setAdapter(receiptionBillChoiceItemAdapter);
//    }
//}
//
