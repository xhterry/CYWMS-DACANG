package com.xx.chinetek.cywms.Review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Review.ReviewBillChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Pallet.PalletDetail_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Review.OutStock_Model;
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
    String TAG_GetT_PalletDetailByBarCode = "ReviewBillChoice_GetT_PalletDetailByBarCode";
    private final int RESULT_GetT_OutStockReviewListADF = 101;
    private final int RESULT_GetT_PalletDetailByBarCode=102;

    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_GetT_OutStockReviewListADF:
                AnalysisGetT_OutStockListADFJson((String) msg.obj);
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

    @ViewInject(R.id.lsvChoice)
    ListView lsvChoice;
    @ViewInject(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(edt_filterContent)
    EditText edtfilterContent;
    @ViewInject(R.id.btn_PrintQCLabrl)
    Button btnNoTask;


    Context context = ReviewBillChoice.this;
    ReviewBillChioceItemAdapter reviewBillChioceItemAdapter;
    ArrayList<OutStock_Model> outStockModels;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Review_title), false);
        x.view().inject(this);
        btnNoTask.setVisibility(View.GONE);
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
        edtfilterContent.setText("");
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
                    params.put("SerialNo", code);
                    LogUtil.WriteLog(ReviewBillChoice.class, TAG_GetT_PalletDetailByBarCode, code);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCode, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_PalletDetailByBarCode, null,  URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);
                    return false;
                }
            }
            StartScanIntent(null,null);
            CommonUtil.setEditFocus(edtfilterContent);
        }
        return false;
    }


    /**
     * 初始化加载listview
     */
    private void InitListView() {
        OutStock_Model outStock_model=new OutStock_Model();
        outStock_model.setStatus(1);
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
            if(outStockModels!=null)
                BindListVIew(outStockModels);
        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
        }
    }

    void AnalysisGetT_PalletDetailByBarCodeJson(String result){
        LogUtil.WriteLog(ReviewBillChoice.class, TAG_GetT_PalletDetailByBarCode,result);
        Gson gson = new Gson();
        ReturnMsgModel<PalletDetail_Model> returnMsgModel = gson.fromJson(result, new TypeToken<ReturnMsgModel<PalletDetail_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            PalletDetail_Model palletDetailModel=returnMsgModel.getModelJson();
            if(palletDetailModel!=null) {
                // Receipt_Model receiptModel = new Receipt_Model(barCodeInfo.getBarCode());
                //  int index = receiptModels.indexOf(receiptModel);
                //  if (index != -1) {
                //调用GetT_InStockList 赋值ERP订单号字段，获取Receipt_Model列表，跳转到扫描界面
                OutStock_Model outStock_model=new OutStock_Model();
                outStock_model.setStatus(1);
                outStock_model.setErpVoucherNo(palletDetailModel.getErpVoucherNo());
                GetT_InStockTaskInfoList(outStock_model);
                //   } else {
                //     MessageBox.Show(context, R.string.Error_BarcodeNotInList);
                // }
            }
        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
        }
    }

    void StartScanIntent(OutStock_Model outStock_model,PalletDetail_Model palletDetailModel){
        Intent intent=new Intent(context,ReviewScan.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("outStock_model",outStock_model);
        bundle.putParcelable("palletDetailModel",palletDetailModel);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }

    private void BindListVIew(ArrayList<OutStock_Model> outStockModels) {
        reviewBillChioceItemAdapter=new ReviewBillChioceItemAdapter(context,outStockModels);
        lsvChoice.setAdapter(reviewBillChioceItemAdapter);
    }

}
