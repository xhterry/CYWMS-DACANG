package com.xx.chinetek.cywms.Review;

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
import com.xx.chinetek.adapter.wms.Review.ReviewBillChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Review.OutStock_Model;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;
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
public class ReviewBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    String TAG_GetT_OutStockReviewListADF = "ReviewBillChoice_GetT_OutStockReviewListADF";
    String TAG_ScanOutStockReviewByBarCodeADF = "ReviewBillChoice_ScanOutStockReviewByBarCodeADF";
    private final int RESULT_GetT_OutStockReviewListADF = 101;
    private final int RESULT_ScanOutStockReviewByBarCodeADF=102;

    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_GetT_OutStockReviewListADF:
                AnalysisGetT_OutStockListADFJson((String) msg.obj);
                break;
            case RESULT_ScanOutStockReviewByBarCodeADF:
                AnalysisScanOutStockReviewByBarCodeADFJson((String) msg.obj);
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
    @ViewInject(edt_filterContent)
    EditText edtfilterContent;


    Context context = ReviewBillChoice.this;
    ReviewBillChioceItemAdapter reviewBillChioceItemAdapter;
    ArrayList<OutStock_Model> outStockModels;
    ArrayList<StockInfo_Model> stockInfoModels;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Review_title), false);
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
    }

    @Override
    public void onRefresh() {
        outStockModels=new ArrayList<>();
        InitListView();
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoice,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OutStock_Model outStock_model=(OutStock_Model) reviewBillChioceItemAdapter.getItem(position);
        StartScanIntent(outStock_model,null);
    }

    @Event(value = R.id.edt_filterContent,type = View.OnKeyListener.class)
    private  boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            if(outStockModels!=null && outStockModels.size()>0) {
                String code = edtfilterContent.getText().toString().trim();
                //扫描单据号、检查单据列表
                OutStock_Model outStock_model = new OutStock_Model(code);
                int index=outStockModels.indexOf(outStock_model);
                if (index!=-1) {
                    StartScanIntent(outStockModels.get(index), null);
                    return false;
                } else {
                    //扫描箱条码
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("BarCode", code);
                    LogUtil.WriteLog(ReviewBillChoice.class, TAG_ScanOutStockReviewByBarCodeADF, code);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_ScanOutStockReviewByBarCodeADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_ScanOutStockReviewByBarCodeADF, null,  URLModel.GetURL().ScanOutStockReviewByBarCodeADF, params, null);
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
        edtfilterContent.setText("");
        OutStock_Model outStock_model=new OutStock_Model();
        outStock_model.setStatus(2);
        GetT_InStockTaskInfoList(outStock_model);
    }

    void GetT_InStockTaskInfoList(OutStock_Model outStock_model){
        try {
            String ModelJson = GsonUtil.parseModelToJson(outStock_model);
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(ReviewBillChoice.class, TAG_GetT_OutStockReviewListADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutStockReviewListADF, getString(R.string.Msg_GetT_OutStockListADF), context, mHandler, RESULT_GetT_OutStockReviewListADF, null,  URLModel.GetURL().GetT_OutStockReviewListADF, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetT_OutStockListADFJson(String result){
        LogUtil.WriteLog(ReviewBillChoice.class, TAG_GetT_OutStockReviewListADF,result);
        ReturnMsgModelList<OutStock_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutStock_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            outStockModels=returnMsgModel.getModelJson();
            if (outStockModels != null && outStockModels.size() == 1 && stockInfoModels != null && stockInfoModels.size()!=0)
                StartScanIntent(outStockModels.get(0), stockInfoModels);
            else
                BindListVIew(outStockModels);
        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
        }
    }

    void AnalysisScanOutStockReviewByBarCodeADFJson(String result){
        LogUtil.WriteLog(ReviewBillChoice.class, TAG_ScanOutStockReviewByBarCodeADF,result);
        ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            stockInfoModels=returnMsgModel.getModelJson();
            if(stockInfoModels!=null) {
                // Receipt_Model receiptModel = new Receipt_Model(barCodeInfo.getBarCode());
                //  int index = receiptModels.indexOf(receiptModel);
                //  if (index != -1) {
                //调用GetT_InStockList 赋值ERP订单号字段，获取Receipt_Model列表，跳转到扫描界面
                OutStock_Model outStock_model=new OutStock_Model();
                outStock_model.setStatus(2);
                outStock_model.setErpVoucherNo(stockInfoModels.get(0).getErpVoucherNo());
                GetT_InStockTaskInfoList(outStock_model);
            }
        }else
        {
            MessageBox.Show(context,returnMsgModel.getMessage());
        }
        CommonUtil.setEditFocus(edtfilterContent);
    }

    void StartScanIntent(OutStock_Model outStock_model,ArrayList<StockInfo_Model> stockInfoModels){
        Intent intent=new Intent(context,ReviewScan.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("outStock_model",outStock_model);
        bundle.putParcelableArrayList("stockInfoModels",stockInfoModels);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }

    private void BindListVIew(ArrayList<OutStock_Model> outStockModels) {
        reviewBillChioceItemAdapter=new ReviewBillChioceItemAdapter(context,outStockModels);
        lsvChoice.setAdapter(reviewBillChioceItemAdapter);
    }

}
