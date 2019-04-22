package com.xx.chinetek.cywms.Review;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import com.xx.chinetek.cywms.Truck.TruckLoad;
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
import com.xx.chinetek.util.function.ArithUtil;
import com.xx.chinetek.util.function.CommonUtil;
import com.xx.chinetek.util.function.DoubleClickCheck;
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
    String TAG_GetStockByOutStockReviewByID="ReviewScan_GetStockByOutStockReviewByID";
    String TAG_DeletePalletByErpVoucherNo="ReviewScan_DeletePalletByErpVoucherNo";

    private final int RESULT_GetT_OutStockReviewDetailListByHeaderIDADF=101;
    private final int RESULT_ScanOutStockReviewByBarCodeADF=102;
    private final int RESULT_SaveT_OutStockReviewDetailADF=103;
    private final int RESULT_Msg_SaveT_OutStockReviewPalletDetailADF=104;
    private final int RESULT_GetStockByOutStockReviewByID=105;
    private final int RESULT_DeletePalletByErpVoucherNo=106;

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
            case RESULT_GetStockByOutStockReviewByID:
                AnalysisGetStockByOutStockReviewByIDJson((String) msg.obj);
                break;
            case RESULT_DeletePalletByErpVoucherNo:
                AnalysisDeletePalletByErpVoucherNoJson((String) msg.obj);
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
    int mPosition=-1;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.Review_subtitle), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }


    @Override
    protected void initData() {
        super.initData();
        lsvReviewscan.setOnScrollListener(onScrollListener);
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
            if (DoubleClickCheck.isFastDoubleClick(context)) {
                return false;
            }
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

    @Event(value =R.id.lsv_Reviewscan,type =AdapterView.OnItemClickListener.class )
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OutStockDetailInfo_Model outStockDetailInfoModel=(OutStockDetailInfo_Model)reviewScanDetailAdapter.getItem(position);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("ID", outStockDetailInfoModel.getID()+"");
        LogUtil.WriteLog(ReviewScan.class, TAG_GetStockByOutStockReviewByID,  outStockDetailInfoModel.getID()+"");
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockByOutStockReviewByID, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetStockByOutStockReviewByID, null, URLModel.GetURL().GetStockByOutStockReviewByID, params, null);
    }

    @Event(value =R.id.edt_ReviewScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtReviewScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String code=edtReviewScanBarcode.getText().toString().trim();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            LogUtil.WriteLog(ReviewScan.class, TAG_ScanOutStockReviewByBarCodeADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_ScanOutStockReviewByBarCodeADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_ScanOutStockReviewByBarCodeADF, null, URLModel.GetURL().ScanOutStockReviewByBarCodeADF, params, null);
        }
        return false;
    }


    @Event(R.id.btn_Combinepallet)
    private void btnCombinepalletClick(View view){
        if (DoubleClickCheck.isFastDoubleClick(context)) {
            return;
        }
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



    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("ErpVoucherNo", outStockModel.getErpVoucherNo());
            params.put("PalletType", "2");
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(ReviewScan.class, TAG_DeletePalletByErpVoucherNo, para);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_DeletePalletByErpVoucherNo, getString(R.string.Msg_DeletePalletByErpVoucherNo), context, mHandler, RESULT_DeletePalletByErpVoucherNo, null,  URLModel.GetURL().DeletePalletByErpVoucherNo, params, null);
        }
        return super.onKeyUp(keyCode, event);
    }

    /*
       获取下架复核明细
        */
    void GetOutStockDetailInfo(OutStock_Model outStockModel){
        if(outStockModel!=null) {
            txtVoucherNo.setText(outStockModel.getErpVoucherNo());
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

    void AnalysisGetStockByOutStockReviewByIDJson(String result){
        LogUtil.WriteLog(ReviewScan.class, TAG_GetStockByOutStockReviewByID,result);
        ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {}.getType());
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
        CommonUtil.setEditFocus(edtReviewScanBarcode);

    }

    /*
    删除托盘
     */
    void AnalysisDeletePalletByErpVoucherNoJson(String result){
        LogUtil.WriteLog(ReviewScan.class, TAG_DeletePalletByErpVoucherNo, result);
       // ReturnMsgModel<PalletDetail_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<PalletDetail_Model>>() {}.getType());
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
            MessageBox.Show(context,returnMsgModel.getMessage());
        }
        CommonUtil.setEditFocus(edtReviewScanBarcode);
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
                    if(outStockDetailInfoModels.get(i).getLstStock()!=null) {
                        for (int j = 0; j < outStockDetailInfoModels.get(i).getLstStock().size(); j++) {
                            outStockDetailInfoModels.get(i).getLstStock().get(j).setStockBarCodeStatus(1);
                        }
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
                        if(!checkID(stockModel))
                            break;
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
        if(returnMsgModel.getHeaderStatus().equals("S")) {
            new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage(returnMsgModel.getMessage())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法
                            Intent intent = new Intent(context, TruckLoad.class);
                            intent.putExtra("VoucherNo", txtVoucherNo.getText().toString().trim());
                            startActivityLeft(intent);
                            closeActiviry();
                        }
                    }).show();
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

    boolean checkID(StockInfo_Model stockInfoModel){
        boolean isContainID=false;
        for (OutStockDetailInfo_Model outStockDetailInfoModel: outStockDetailInfoModels){
            if(outStockDetailInfoModel.getID()==stockInfoModel.getOutstockDetailID()){
                isContainID=true;
                break;
            }
        }
        if(!isContainID){
            MessageBox.Show(context,getString(R.string.Error_notContainID)+"|"+stockInfoModel.getSerialNo());
        }
        return isContainID;
    }

    boolean CheckBarcode(StockInfo_Model StockInfo_Model){
        if(StockInfo_Model!=null && outStockDetailInfoModels!=null) {
            //int index = -1;
            int size = outStockDetailInfoModels.size();
            //判断条码是否重复
            for (int i = 0; i < size; i++) {
                if(outStockDetailInfoModels.get(i).getLstStock() == null) continue;
                int StockIndex = outStockDetailInfoModels.get(i).getLstStock().indexOf(StockInfo_Model);
                if (StockIndex != -1) {
                    MessageBox.Show(context, getString(R.string.Error_BarcodeScaned) + "|" + StockInfo_Model.getSerialNo());
                    return false;
                }
            }
            Float Qty=StockInfo_Model.getQty();
            Boolean hasMaterial=false;
          //  Boolean isReviewFinish=true;
            for (int i = 0; i < size; i++) {
                //制定批次
                if(outStockDetailInfoModels.get(i).getIsSpcBatch().equals("Y")){
                    if(!outStockDetailInfoModels.get(i).getFromBatchNo().equals(StockInfo_Model.getBatchNo())){
                        continue;
                    }
                }
                if(Qty<=0) break;
                if (outStockDetailInfoModels.get(i).getMaterialNo().equals(StockInfo_Model.getMaterialNo())
                        && outStockDetailInfoModels.get(i).getStrongHoldCode().equals(StockInfo_Model.getStrongHoldCode())) {
                    hasMaterial=true;
                    if(!outStockDetailInfoModels.get(i).getReviewFinish()) {
                       // isReviewFinish=false;
                        if (outStockDetailInfoModels.get(i).getLstStock() == null)
                            outStockDetailInfoModels.get(i).setLstStock(new ArrayList<StockInfo_Model>());
                        try {
                            Float remainQty = ArithUtil.sub(outStockDetailInfoModels.get(i).getOutStockQty(), outStockDetailInfoModels.get(i).getScanQty());
                            Float addQty=remainQty > Qty ? Qty : remainQty;
                            Float ScanQty = ArithUtil.add(addQty, outStockDetailInfoModels.get(i).getScanQty());
                            Qty = ArithUtil.sub(Qty, remainQty);
                            StockInfo_Model stockInfoModel = StockInfo_Model.clone();
                            stockInfoModel.setQty(addQty);
                            outStockDetailInfoModels.get(i).getLstStock().add(0, stockInfoModel);
                            outStockDetailInfoModels.get(i).setToBatchno(StockInfo_Model.getBatchNo());
                            outStockDetailInfoModels.get(i).setScanQty(ScanQty);
                            outStockDetailInfoModels.get(i).setOustockStatus(1); //存在未组托条码
                            if(ArithUtil.sub(outStockDetailInfoModels.get(i).getScanQty(),outStockDetailInfoModels.get(i).getOutStockQty())==0f)
                                outStockDetailInfoModels.get(i).setReviewFinish(true);
                        }catch (Exception ex){
                            MessageBox.Show(context, ex.getMessage());
                            return false;
                        }
                    }

                }
            }
            if(!hasMaterial) {
                MessageBox.Show(context, getString(R.string.Error_BarcodeNotInList) + "|" + StockInfo_Model.getSerialNo());
                return false;
            }
//            if(isReviewFinish){
//                MessageBox.Show(context, getString(R.string.Error_ReviewFinish));
//                return false;
//            }
        }
        return true;
    }


//    boolean CheckBarcode(StockInfo_Model StockInfo_Model){
//        if(StockInfo_Model!=null && outStockDetailInfoModels!=null) {
//            int index = -1;
//            int size = outStockDetailInfoModels.size();
//            for (int i = 0; i < size; i++) {
//                if (outStockDetailInfoModels.get(i).getID() == StockInfo_Model.getOutstockDetailID()) {
//                    index = i;
//                    break;
//                }
//            }
//            if (index != -1) {
//                if (outStockDetailInfoModels.get(index).getLstStock() == null)
//                    outStockDetailInfoModels.get(index).setLstStock(new ArrayList<StockInfo_Model>());
//
//                int StockIndex = outStockDetailInfoModels.get(index).getLstStock().indexOf(StockInfo_Model);
//                if (StockIndex == -1) {
//
//                    //需要删除
//                    outStockDetailInfoModels.get(index).setToBatchno(StockInfo_Model.getBatchNo());
//
//                    float qty = ArithUtil.add(outStockDetailInfoModels.get(index).getScanQty(), StockInfo_Model.getQty());
//                    if (qty <= outStockDetailInfoModels.get(index).getOutStockQty()) {
//                        outStockDetailInfoModels.get(index).getLstStock().add(0, StockInfo_Model);
//                        outStockDetailInfoModels.get(index).setScanQty(qty);
//                        outStockDetailInfoModels.get(index).setOustockStatus(1); //存在未组托条码
//                    } else {
//                        MessageBox.Show(context, getString(R.string.Error_ReviewFinish));
//                        return false;
//                    }
//                } else {
//                    MessageBox.Show(context, getString(R.string.Error_BarcodeScaned) + "|" + StockInfo_Model.getSerialNo());
//                    return false;
//                }
//            } else {
//                MessageBox.Show(context, getString(R.string.Error_BarcodeNotInList) + "|" + StockInfo_Model.getSerialNo());
//                return false;
//            }
//        }
//        return true;
//    }

    /*
    获取需要组托条码
     */
    ArrayList<OutStockDetailInfo_Model> GetPalletModels(){

        ArrayList<OutStockDetailInfo_Model> palletDetailModels=new ArrayList<>();
        try {
            if (outStockDetailInfoModels != null) {
                for (OutStockDetailInfo_Model outstockDetailModel : outStockDetailInfoModels) {
                    if (outstockDetailModel.getOustockStatus() == 1) {
                        if (outstockDetailModel.getLstStock() != null) {
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
                            if (palletDetail_model.getLstStock() == null)
                                palletDetail_model.setLstStock(new ArrayList<StockInfo_Model>());
                            //  ArrayList<StockInfo_Model> tempStockModels = new ArrayList<>();
                            for (StockInfo_Model stockModel : outstockDetailModel.getLstStock()) {
                                StockInfo_Model stockInfoModel=stockModel.clone();
                                int index = palletDetailModels.indexOf(palletDetail_model);
                                if (stockInfoModel.getStockBarCodeStatus() == 0) {
                                    if (index == -1) {
                                        palletDetail_model.getLstStock().add(0, stockInfoModel);
                                        palletDetailModels.add(palletDetail_model);
                                    } else {
                                        int stockIndex = palletDetailModels.get(index).getLstStock().indexOf(stockInfoModel);
                                        if(stockIndex==-1){
                                            palletDetailModels.get(index).getLstStock().add(0, stockInfoModel);
                                        }else {
                                            palletDetailModels.get(index).getLstStock().get(stockIndex).setQty(
                                                    ArithUtil.add(palletDetailModels.get(index).getLstStock().get(stockIndex).getQty(), stockModel.getQty())
                                            );
                                        }
                                    }

                                    //   tempStockModels.add(0, stockModel);
                                }
                            }
//                        if (tempStockModels.size() == 0)
//                            continue;
                        }
                    }
                }
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
            palletDetailModels=new ArrayList<>();
        }
        return palletDetailModels;
    }

    private void BindListVIew(ArrayList<OutStockDetailInfo_Model> outStockDetailInfoModels) {
        reviewScanDetailAdapter=new ReviewScanDetailAdapter(context,outStockDetailInfoModels);
        lsvReviewscan.setAdapter(reviewScanDetailAdapter);
        if(mPosition!=-1)
            lsvReviewscan.setSelection(mPosition);
    }


    AbsListView.OnScrollListener onScrollListener= new AbsListView.OnScrollListener() {
        /**
         * 滚动状态改变时调用
         */
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // 不滚动时保存当前滚动到的位置
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                mPosition = lsvReviewscan.getFirstVisiblePosition();
            }
        }

        /**
         * 滚动时调用
         */
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    };

}
