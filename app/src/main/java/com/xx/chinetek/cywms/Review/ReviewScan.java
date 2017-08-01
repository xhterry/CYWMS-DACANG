package com.xx.chinetek.cywms.Review;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.Pallet.CombinPalletDetail;
import com.xx.chinetek.adapter.wms.Review.ReviewScanDetailAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Review.OutStockDetailInfo_Model;
import com.xx.chinetek.model.WMS.Review.OutStock_Model;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;
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

import static com.xx.chinetek.util.function.GsonUtil.parseModelToJson;

@ContentView(R.layout.activity_review_scan)
public class ReviewScan extends BaseActivity {

    String TAG_GetT_OutStockReviewDetailListByHeaderIDADF="ReviewScan_GetT_OutStockReviewDetailListByHeaderIDADF";
    String TAG_ScanOutStockReviewByBarCodeADF="ReviewScan_ScanOutStockReviewByBarCodeADF";
    String TAG_SaveT_OutStockReviewPalletDetailADF="ReviewScan_SaveT_OutStockReviewPalletDetailADF";
    String TAG_SaveT_OutStockReviewDetailADF="ReviewScan_SaveT_OutStockReviewDetailADF";

    private final int RESULT_GetT_OutStockReviewDetailListByHeaderIDADF=101;
    private final int RESULT_ScanOutStockReviewByBarCodeADF=102;
    private final int RESULT_SaveT_OutStockReviewDetailADF=103;
    private final int RESULT_Msg_SaveT_OutStockReviewPalletDetailADF=104;

    private final int  RequestCode_PalletDetail=10002;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetT_OutStockReviewDetailListByHeaderIDADF:
                AnalysisGetT_OutStockReviewDetailListByHeaderIDADFJson((String) msg.obj);
                break;
            case RESULT_ScanOutStockReviewByBarCodeADF:
                AnalysiseScanOutStockReviewByBarCodeADFJson((String) msg.obj);
                break;
            case RESULT_Msg_SaveT_OutStockReviewPalletDetailADF:
                 AnalysisetT_SaveT_OutStockReviewPalletDetailADFJson((String) msg.obj);
                break;
            case RESULT_SaveT_OutStockReviewDetailADF:
                AnalysisSaveT_OutStockReviewDetailADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtReviewScanBarcode);
                break;
        }
    }



    Context context = ReviewScan.this;
    @ViewInject(R.id.btn_PalletDetail)
    Button btnPalletDetail;
    @ViewInject(R.id.btn_Combinepallet)
    Button btnCombinepallet;
    @ViewInject(R.id.edt_ReviewScanBarcode)
    EditText edtReviewScanBarcode;
    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVoucherNo;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_EDate)
    TextView txtEDate;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.lsv_Reviewscan)
    ListView lsvReviewscan;

    ArrayList<OutStockDetailInfo_Model> outStockDetailInfoModels;
    ArrayList<StockInfo_Model> stockInfoModels;//扫描条码
    OutStock_Model outStockModel=null;
    ReviewScanDetailAdapter reviewScanDetailAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.Review_subtitle), true);
        x.view().inject(this);
    }


    @Override
    protected void initData() {
        super.initData();
        outStockModel=getIntent().getParcelableExtra("outStock_model");
        stockInfoModels=getIntent().getParcelableArrayListExtra("stockInfoModels");
        GetOutStockDetailInfo(outStockModel);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            Boolean isFinishReceipt = true;
            for (OutStockDetailInfo_Model outStockDetailInfoModel : outStockDetailInfoModels) {
                if (outStockDetailInfoModel.getScanQty().compareTo(outStockDetailInfoModel.getOutStockQty()) != 0) {
                    MessageBox.Show(context, getString(R.string.Error_CannotReview));
                    isFinishReceipt = false;
                    break;
                }
            }
            if (isFinishReceipt) {
                String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
                String modelJson = GsonUtil.parseModelToJson(outStockDetailInfoModels);
                final Map<String, String> params = new HashMap<String, String>();
                params.put("UserJson", userJson);
                params.put("ModelJson", modelJson);
                LogUtil.WriteLog(ReviewScan.class, TAG_SaveT_OutStockReviewDetailADF, modelJson);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_OutStockReviewDetailADF, getString(R.string.Msg_SaveT_OutStockReviewDetailADF), context, mHandler, RESULT_SaveT_OutStockReviewDetailADF, null, URLModel.GetURL().SaveT_OutStockReviewDetailADF, params, null);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Event(value =R.id.edt_ReviewScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtReviewScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String code=edtReviewScanBarcode.getText().toString().trim();
            if(TextUtils.isEmpty(code)){
                CommonUtil.setEditFocus(edtReviewScanBarcode);
                return true;
            }
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            LogUtil.WriteLog(ReviewScan.class, TAG_ScanOutStockReviewByBarCodeADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_ScanOutStockReviewByBarCodeADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_ScanOutStockReviewByBarCodeADF, null, URLModel.GetURL().ScanOutStockReviewByBarCodeADF, params, null);
        }
        return false;
    }


    @Event(R.id.btn_Combinepallet)
    private void btnCombinepalletClick(View view){
        ArrayList<OutStockDetailInfo_Model> palletDetailModels=GetPalletModels();
        if(palletDetailModels.size()!=0){
            final Map<String, String> params = new HashMap<String, String>();
            String ModelJson = parseModelToJson(palletDetailModels);
            params.put("UserJson", parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(ReviewScan.class, TAG_SaveT_OutStockReviewPalletDetailADF, ModelJson);
           RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_OutStockReviewPalletDetailADF, getString(R.string.Msg_SaveT_PalletDetailADF), context, mHandler, RESULT_Msg_SaveT_OutStockReviewPalletDetailADF, null,  URLModel.GetURL().SaveT_OutStockReviewPalletDetailADF, params, null);
        }
    }

    @Event(R.id.btn_PalletDetail)
    private void btnPalletDetailClick(View view){
        Intent intent = new Intent(context, CombinPalletDetail.class);
        intent.putExtra("VoucherNo",txtVoucherNo.getText().toString());
        intent.putParcelableArrayListExtra("outStockDetailInfoModels",outStockDetailInfoModels);
        startActivityForResult(intent,RequestCode_PalletDetail);
    }

    /*
     界面返回值
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RequestCode_PalletDetail  && resultCode == RESULT_OK){
            outStockDetailInfoModels = data.getParcelableArrayListExtra("outStockDetailInfoModels");
            BindListVIew(outStockDetailInfoModels);
        }
    }

    /*
   获取下架复核明细
    */
    void GetOutStockDetailInfo(OutStock_Model outStockModel){
        if(outStockModel!=null) {
            txtVoucherNo.setText(outStockModel.getVoucherNo());
            final OutStockDetailInfo_Model outStockDetailInfoModel1 = new OutStockDetailInfo_Model();
            outStockDetailInfoModel1.setHeaderID(outStockModel.getID());
            outStockDetailInfoModel1.setERPVoucherNo(outStockModel.getErpVoucherNo());
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
    void AnalysisGetT_OutStockReviewDetailListByHeaderIDADFJson(String result){
        LogUtil.WriteLog(ReviewScan.class, TAG_GetT_OutStockReviewDetailListByHeaderIDADF,result);
        ReturnMsgModelList<OutStockDetailInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutStockDetailInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            outStockDetailInfoModels=returnMsgModel.getModelJson();
            //自动确认扫描箱号
            if(stockInfoModels!=null && stockInfoModels.size()!=0) {
                for (StockInfo_Model stockInfoModel :stockInfoModels) {
                    CheckBarcode(stockInfoModel);
                    InitFrm(stockInfoModel);
                }
            }
            BindListVIew(outStockDetailInfoModels);
        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
        }
    }

    /*
    提交组托
     */
    void AnalysisetT_SaveT_OutStockReviewPalletDetailADFJson(String result){
        try {
            LogUtil.WriteLog(ReviewScan.class, TAG_SaveT_OutStockReviewPalletDetailADF,result);
            ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                MessageBox.Show(context,returnMsgModel.getMessage());
                //更改实体类组托状态
                for (int i=0;i<outStockDetailInfoModels.size();i++) {
                    for(int j=0;j<outStockDetailInfoModels.get(i).getLstStock().size();j++){
                        outStockDetailInfoModels.get(i).getLstStock().get(j).setStockBarCodeStatus(1);
                    }
                    outStockDetailInfoModels.get(i).setOustockStatus(0);
                }
                BindListVIew(outStockDetailInfoModels);
            }else
            {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        CommonUtil.setEditFocus(edtReviewScanBarcode);
    }


    /*
    条码扫描
     */
    void AnalysiseScanOutStockReviewByBarCodeADFJson(String result){
        try {
            LogUtil.WriteLog(ReviewScan.class, TAG_ScanOutStockReviewByBarCodeADF,result);
            ReturnMsgModelList<StockInfo_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                stockInfoModels=returnMsgModel.getModelJson();
                if(stockInfoModels!=null){
                    for (StockInfo_Model stockModel:stockInfoModels) {
                        if(!CheckBarcode(stockModel))
                            break;
                    }
                    InitFrm(stockInfoModels.get(0));
                }
                BindListVIew(outStockDetailInfoModels);
            }else
            {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        CommonUtil.setEditFocus(edtReviewScanBarcode);
    }

    void AnalysisSaveT_OutStockReviewDetailADFJson(String result){
        LogUtil.WriteLog(ReviewScan.class, TAG_SaveT_OutStockReviewDetailADF,result);
        ReturnMsgModelList<OutStock_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutStock_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            closeActiviry();
//            Intent intent=new Intent(context, TruckLoad.class);
//            intent.putExtra("VoucherNo",txtVoucherNo.getText().toString().trim());
//            startActivityLeft(intent);
        }else
        {
           MessageBox.Show(context,returnMsgModel.getMessage());
        }
    }

    void InitFrm(StockInfo_Model stockInfoModel){
        if(stockInfoModel!=null ){
            txtCompany.setText(stockInfoModel.getStrongHoldName());
            txtBatch.setText(stockInfoModel.getBatchNo());
            txtStatus.setText("");
            txtMaterialName.setText(stockInfoModel.getMaterialDesc());
            txtEDate.setText(CommonUtil.DateToString(stockInfoModel.getEDate()));
        }
    }

    boolean CheckBarcode(StockInfo_Model StockInfo_Model){
        if(StockInfo_Model!=null && outStockDetailInfoModels!=null) {
            int index = -1;
            int size = outStockDetailInfoModels.size();
            for (int i = 0; i < size; i++) {
                if (outStockDetailInfoModels.get(i).getID() == StockInfo_Model.getOutstockDetailID()) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                if (outStockDetailInfoModels.get(index).getLstStock() == null)
                    outStockDetailInfoModels.get(index).setLstStock(new ArrayList<StockInfo_Model>());

                int StockIndex = outStockDetailInfoModels.get(index).getLstStock().indexOf(StockInfo_Model);
                if (StockIndex == -1) {

                    //需要删除
                    outStockDetailInfoModels.get(index).setToBatchno(StockInfo_Model.getBatchNo());

                    float qty = outStockDetailInfoModels.get(index).getScanQty() + StockInfo_Model.getQty();
                    if (qty <= outStockDetailInfoModels.get(index).getOutStockQty()) {
                        outStockDetailInfoModels.get(index).getLstStock().add(0, StockInfo_Model);
                        outStockDetailInfoModels.get(index).setScanQty(qty);
                        outStockDetailInfoModels.get(index).setOustockStatus(1); //存在未组托条码
                    } else {
                        MessageBox.Show(context, getString(R.string.Error_ReviewFinish));
                        return false;
                    }
                } else {
                    MessageBox.Show(context, getString(R.string.Error_BarcodeScaned) + "|" + StockInfo_Model.getSerialNo());
                    return false;
                }
            } else {
                MessageBox.Show(context, getString(R.string.Error_BarcodeNotInList) + "|" + StockInfo_Model.getSerialNo());
                return false;
            }
        }
        return true;
    }

    /*
    获取需要组托条码
     */
    ArrayList<OutStockDetailInfo_Model> GetPalletModels(){
        ArrayList<OutStockDetailInfo_Model> palletDetailModels=new ArrayList<>();
        if(outStockDetailInfoModels!=null) {
            for (OutStockDetailInfo_Model outstockDetailModel : outStockDetailInfoModels) {
                if (outstockDetailModel.getOustockStatus() == 1) {
                    if (outstockDetailModel.getLstStock() != null) {
                        ArrayList<StockInfo_Model> tempStockModels = new ArrayList<>();
                        for (StockInfo_Model stockModel : outstockDetailModel.getLstStock()) {
                            if (stockModel.getStockBarCodeStatus() == 0) {
                                tempStockModels.add(0, stockModel);
                            }
                        }
                        if (tempStockModels.size() == 0)
                            continue;
                        OutStockDetailInfo_Model palletDetail_model = new OutStockDetailInfo_Model();
                        palletDetail_model.setErpVoucherNo(outstockDetailModel.getErpVoucherNo());
                        palletDetail_model.setVoucherNo(outstockDetailModel.getVoucherNo());
                        palletDetail_model.setRowNo(outstockDetailModel.getRowNo());
                        palletDetail_model.setRowNoDel(outstockDetailModel.getRowNoDel());
                        palletDetail_model.setCompanyCode(outstockDetailModel.getCompanyCode());
                        palletDetail_model.setStrongHoldCode(outstockDetailModel.getStrongHoldCode());
                        palletDetail_model.setStrongHoldName(outstockDetailModel.getStrongHoldName());
                        palletDetail_model.setVoucherType(999);
                        palletDetail_model.setMaterialNo(outstockDetailModel.getMaterialNo());
                        palletDetail_model.setMaterialNoID(outstockDetailModel.getMaterialNoID());
                        palletDetail_model.setMaterialDesc(outstockDetailModel.getMaterialDesc());
                        if (outstockDetailModel.getLstStock() != null && outstockDetailModel.getLstStock().size() != 0) {
                            palletDetail_model.setLstStock(tempStockModels);
                        }
                        palletDetailModels.add(palletDetail_model);
                    }
                }
            }
        }
        return palletDetailModels;
    }

    private void BindListVIew(ArrayList<OutStockDetailInfo_Model> outStockDetailInfoModels) {
        reviewScanDetailAdapter=new ReviewScanDetailAdapter(context,outStockDetailInfoModels);
        lsvReviewscan.setAdapter(reviewScanDetailAdapter);

    }

}
