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

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Review.ReviewBillChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.cywms.Receiption.ReceiptionScan;
import com.xx.chinetek.cywms.Review.ReviewScan;
import com.xx.chinetek.model.Receiption.ReceiptDetail_Model;
import com.xx.chinetek.model.Receiption.Receipt_Model;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Review.OutStockDetailInfo_Model;
import com.xx.chinetek.model.WMS.Review.OutStock_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.CommonUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.xx.chinetek.cywms.R.id.edt_filterContent;
import static com.xx.chinetek.util.function.GsonUtil.parseModelToJson;


@ContentView(R.layout.activity_bill_choice)
public class MaterialChangeReviewBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    String TAG_GetT_OutStockReviewDetailListByHeaderIDADF="ReviewScan_GetT_OutStockReviewDetailListByHeaderIDADF";
    String TAG_GetT_InStockDetailListByHeaderIDADF="ReceiptionScan_GetT_InStockDetailListByHeaderIDADF";
    String TAG_GetT_OutStockReviewListADF = "ReviewBillChoice_GetT_OutStockReviewListADF";
    private final int RESULT_GetT_OutStockReviewListADF = 101;
    private final int RESULT_Msg_GetT_InStockDetailListByHeaderIDADF=102;
    private final int RESULT_GetT_OutStockReviewDetailListByHeaderIDADF=103;



    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_GetT_OutStockReviewListADF:
                AnalysisGetT_OutStockListADFJson((String) msg.obj);
                break;
            case RESULT_Msg_GetT_InStockDetailListByHeaderIDADF:
                AnalysisGetT_InStockDetailListJson((String) msg.obj);
                break;
            case RESULT_GetT_OutStockReviewDetailListByHeaderIDADF:
                AnalysisGetT_OutStockReviewDetailListByHeaderIDADFJson((String) msg.obj);
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
//    @ViewInject(R.id.btn_PrintQCLabrl)
//    Button btnNoTask;


    Context context = MaterialChangeReviewBillChoice.this;
    ReviewBillChioceItemAdapter reviewBillChioceItemAdapter;
    ArrayList<OutStock_Model> outStockModels;
    ArrayList<ReceiptDetail_Model> receiptDetailModels;
    ArrayList<OutStockDetailInfo_Model> outStockDetailInfoModels;
    Receipt_Model receiptModel;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.MaterialChange_out_subtitle), false);
        x.view().inject(this);
      //  btnNoTask.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
        receiptModel=getIntent().getParcelableExtra("receiptModel");
        GetReceiptDetail(receiptModel);
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
        OutStock_Model outStockModel=(OutStock_Model) reviewBillChioceItemAdapter.getItem(position);
        GetOutStockDetailInfo(outStockModel);
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
                    GetOutStockDetailInfo(outStockModels.get(index));
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
        outStock_model.setStatus(1);
        outStock_model.setVoucherType(31);
        GetT_InStockTaskInfoList(outStock_model);
    }

    /*
  获取收货明细
   */
    void GetReceiptDetail(Receipt_Model receiptModel){
        if(receiptModel!=null) {
            final ReceiptDetail_Model receiptDetailModel = new ReceiptDetail_Model();
            receiptDetailModel.setHeaderID(receiptModel.getID());
            receiptDetailModel.setErpVoucherNo(receiptModel.getErpVoucherNo());
            receiptDetailModel.setVoucherType(receiptModel.getVoucherType());
            final Map<String, String> params = new HashMap<String, String>();
            params.put("ModelDetailJson", parseModelToJson(receiptDetailModel));
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(ReceiptionScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, para);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockDetailListByHeaderIDADF, getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), context, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null,  URLModel.GetURL().GetT_InStockDetailListByHeaderIDADF, params, null);
        }
    }

    /*
    处理收货明细
     */
    void AnalysisGetT_InStockDetailListJson(String result){
        LogUtil.WriteLog(ReceiptionScan.class, TAG_GetT_InStockDetailListByHeaderIDADF,result);
        try {
            ReturnMsgModelList<ReceiptDetail_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<ReceiptDetail_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                receiptDetailModels = returnMsgModel.getModelJson();
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex) {
            MessageBox.Show(context,ex.getMessage());
        }
    }

    /*
 获取下架复核明细
  */
    void GetOutStockDetailInfo(OutStock_Model outStockModel){
        if(outStockModel!=null) {
            final OutStockDetailInfo_Model outStockDetailInfoModel1 = new OutStockDetailInfo_Model();
            outStockDetailInfoModel1.setHeaderID(outStockModel.getID());
            outStockDetailInfoModel1.setErpVoucherNo(outStockModel.getErpVoucherNo());
            outStockDetailInfoModel1.setVoucherType(outStockModel.getVoucherType());
            final Map<String, String> params = new HashMap<String, String>();
            params.put("ModelDetailJson", parseModelToJson(outStockDetailInfoModel1));
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(ReviewScan.class, TAG_GetT_OutStockReviewDetailListByHeaderIDADF, para);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutStockReviewDetailListByHeaderIDADF, getString(R.string.Msg_GetT_OutStockDetailListByHeaderIDADF), context, mHandler, RESULT_GetT_OutStockReviewDetailListByHeaderIDADF, null,  URLModel.GetURL().GetT_OutStockReviewDetailListByHeaderIDADF, params, null);
        }
    }

    /*
 处理下架复核明细
  */
    void AnalysisGetT_OutStockReviewDetailListByHeaderIDADFJson(String result) {
        LogUtil.WriteLog(ReviewScan.class, TAG_GetT_OutStockReviewDetailListByHeaderIDADF, result);
        ReturnMsgModelList<OutStockDetailInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutStockDetailInfo_Model>>() {
        }.getType());
        if (returnMsgModel.getHeaderStatus().equals("S")) {
            outStockDetailInfoModels = returnMsgModel.getModelJson();
            if (receiptDetailModels != null && outStockDetailInfoModels != null) {
                if (CheckDetails(receiptDetailModels, outStockDetailInfoModels)) {
                    StartScanIntent();
                } else {
                    MessageBox.Show(context, getString(R.string.Error_InOrOutContentDiff));
                }
            }
        } else {
            MessageBox.Show(context,returnMsgModel.getMessage());
        }
    }


    void GetT_InStockTaskInfoList(OutStock_Model outStock_model){
        try {
            String ModelJson = parseModelToJson(outStock_model);
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(MaterialChangeReviewBillChoice.class, TAG_GetT_OutStockReviewListADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutStockReviewListADF, getString(R.string.Msg_GetT_OutStockListADF), context, mHandler, RESULT_GetT_OutStockReviewListADF, null,  URLModel.GetURL().GetT_OutStockReviewListADF, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetT_OutStockListADFJson(String result){
        LogUtil.WriteLog(MaterialChangeReviewBillChoice.class, TAG_GetT_OutStockReviewListADF,result);
        ReturnMsgModelList<OutStock_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutStock_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            outStockModels=returnMsgModel.getModelJson();
            BindListVIew(outStockModels);
        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
        }
    }



    void StartScanIntent(){
        Intent intent=new Intent(context,MaterialChange.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("outStockDetailInfoModels",outStockDetailInfoModels);
        bundle.putParcelableArrayList("receiptDetailModels",receiptDetailModels);
        intent.putExtras(bundle);
        startActivityLeft(intent);
        closeActiviry();
    }

    private void BindListVIew(ArrayList<OutStock_Model> outStockModels) {
        reviewBillChioceItemAdapter=new ReviewBillChioceItemAdapter(context,outStockModels);
        lsvChoice.setAdapter(reviewBillChioceItemAdapter);
    }

    Boolean CheckDetails(ArrayList<ReceiptDetail_Model> receiptDetailModels,ArrayList<OutStockDetailInfo_Model> outStockDetailInfoModels){
        if(receiptDetailModels.size()!=outStockDetailInfoModels.size()){
            MessageBox.Show(context,getString(R.string.Error_InOrOutSizeDiff));
            return false;
        }
        Boolean isMatch=false;
        int receiptSize=receiptDetailModels.size();
        int outStockSize=outStockDetailInfoModels.size();
        for(int i=0;i<receiptSize;i++){
            isMatch=false;
            String MaterialNo=receiptDetailModels.get(i).getMaterialNo();
            String BatchNo=receiptDetailModels.get(i).getFromBatchNo();
            String StrongHoldCode=receiptDetailModels.get(i).getStrongHoldCode();
            String AreaNo=receiptDetailModels.get(i).getFromErpAreaNo();
            String Warehouse=receiptDetailModels.get(i).getFromErpWarehouse();
            float qty=receiptDetailModels.get(i).getInStockQty();
            for(int j=0;j<outStockSize;j++){
                String outMaterialNo=outStockDetailInfoModels.get(i).getMaterialNo();
                String outBatchNo=outStockDetailInfoModels.get(i).getFromBatchNo();
                String outStrongHoldCode=outStockDetailInfoModels.get(i).getStrongHoldCode();
                String outAreaNo=outStockDetailInfoModels.get(i).getFromErpAreaNo();
                String outWarehouse=outStockDetailInfoModels.get(i).getFromErpWarehouse();
                float outqty=outStockDetailInfoModels.get(i).getOutStockQty();
                if(outMaterialNo.equals(MaterialNo) && outBatchNo.equals(BatchNo)
                        && !StrongHoldCode.equals(outStrongHoldCode)
                        && AreaNo.equals(outAreaNo)
                        && Warehouse.equals(outWarehouse)
                        && qty==outqty){
                    isMatch=true;
                    break;
                }
            }

            if(!isMatch){
                break;
            }
        }

        return isMatch;
    }

}
