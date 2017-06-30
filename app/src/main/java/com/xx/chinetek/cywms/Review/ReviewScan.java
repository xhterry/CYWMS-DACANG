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
import com.xx.chinetek.adapter.Review.ReviewScanDetailAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Pallet.PalletDetail_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.Review.OutStockDetailInfo_Model;
import com.xx.chinetek.model.Review.OutStock_Model;
import com.xx.chinetek.model.Stock.StockInfo_Model;
import com.xx.chinetek.model.URLModel;
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
import java.util.List;
import java.util.Map;

import static com.xx.chinetek.util.function.GsonUtil.parseModelToJson;

@ContentView(R.layout.activity_review_scan)
public class ReviewScan extends BaseActivity {

    String TAG_GetT_OutStockDetailListByHeaderIDADF="ReviewScan_GetT_OutStockDetailListByHeaderIDADF";
    String TAG_GetT_PalletDetailByBarCode="ReviewScan_GetT_PalletDetailByBarCode";
    String TAG_SaveT_PalletDetailADF="ReviewScan_SaveT_PalletDetailADF";
    String TAG_SaveT_InStockTaskDetailADF="ReviewScan_SaveT_InStockTaskDetailADF";

    private final int RESULT_Msg_GetT_OutStockDetailListByHeaderIDADF=101;
    private final int RESULT_Msg_GetT_PalletDetailByBarCode=102;
    private final int RESULT_Msg_SaveT_InStockTaskDetailADF=103;
    private final int RESULT_Msg_SaveT_PalletDetailADF=104;

    private final int  RequestCode_PalletDetail=10002;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetT_OutStockDetailListByHeaderIDADF:
                AnalysisGetT_OutStockDetailListByHeaderIDADFJson((String) msg.obj);
                break;
            case RESULT_Msg_GetT_PalletDetailByBarCode:
               // AnalysisetT_PalletDetailByBarCodeJson((String) msg.obj);
                break;
            case RESULT_Msg_SaveT_PalletDetailADF:
                 AnalysisetT_SaveT_PalletDetailADF((String) msg.obj);
                break;
            case RESULT_Msg_SaveT_InStockTaskDetailADF:
              //  AnalysisSaveT_InStockTaskDetailADFJson((String) msg.obj);
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
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.lsv_Reviewscan)
    ListView lsvReviewscan;

    ArrayList<OutStockDetailInfo_Model> outStockDetailInfoModels;
    OutStock_Model outStockModel=null;
    PalletDetail_Model palletDetailModel=null;
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
        palletDetailModel=getIntent().getParcelableExtra("palletDetailModel");
      //  txtVoucherNo.setText(outStockModel.getVoucherNo());
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
//            LogUtil.WriteLog(UpShelfScanActivity.class, TAG_GetT_PalletDetailByBarCode, code);
//            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCode, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null, URLModel.GetT_PalletDetailByBarCodeADF, params, null);
        }
        return false;
    }


    @Event(R.id.btn_Combinepallet)
    private void btnCombinepalletClick(View view){
        ArrayList<PalletDetail_Model> palletDetailModels=GetPalletModels();
        if(palletDetailModels.size()!=0){
            final Map<String, String> params = new HashMap<String, String>();
            String ModelJson = parseModelToJson(palletDetailModels);
            params.put("UserJson", parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(ReviewScan.class, TAG_SaveT_PalletDetailADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutStockDetailListByHeaderIDADF, getString(R.string.Msg_SaveT_PalletDetailADF), context, mHandler, RESULT_Msg_GetT_OutStockDetailListByHeaderIDADF, null,  URLModel.GetURL().SaveT_PalletDetailADF, params, null);

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
            final OutStockDetailInfo_Model outStockDetailInfoModel1 = new OutStockDetailInfo_Model();
            outStockDetailInfoModel1.setHeaderID(outStockModel.getID());
            outStockDetailInfoModel1.setERPVoucherNo(outStockModel.getErpVoucherNo());
            outStockDetailInfoModel1.setVoucherType(outStockModel.getVoucherType());
            final Map<String, String> params = new HashMap<String, String>();
            params.put("ModelDetailJson", parseModelToJson(outStockDetailInfoModel1));
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(ReviewScan.class, TAG_GetT_OutStockDetailListByHeaderIDADF, para);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutStockDetailListByHeaderIDADF, getString(R.string.Msg_GetT_OutStockDetailListByHeaderIDADF), context, mHandler, RESULT_Msg_GetT_OutStockDetailListByHeaderIDADF, null,  URLModel.GetURL().GetT_OutStockDetailListByHeaderIDADF, params, null);
        }
    }

    /*
 处理下架复核明细
  */
    void AnalysisGetT_OutStockDetailListByHeaderIDADFJson(String result){
        LogUtil.WriteLog(ReviewScan.class, TAG_GetT_OutStockDetailListByHeaderIDADF,result);
        ReturnMsgModelList<OutStockDetailInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutStockDetailInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            outStockDetailInfoModels=returnMsgModel.getModelJson();
            //自动确认扫描箱号
            if(palletDetailModel!=null && palletDetailModel.getLstStockInfo()!=null) {
                for (StockInfo_Model stockInfoModel : palletDetailModel.getLstStockInfo()) {
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
    void AnalysisetT_SaveT_PalletDetailADF(String result){
        try {
            LogUtil.WriteLog(ReviewScan.class, TAG_SaveT_PalletDetailADF,result);
            ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                //更改实体类组托状态
                for (int i=0;i<outStockDetailInfoModels.size();i++) {
                    for(int j=0;i<outStockDetailInfoModels.get(i).getLstStockInfo().size();j++){
                        outStockDetailInfoModels.get(i).getLstStockInfo().get(j).setStockBarCodeStatus(1);
                    }
                    outStockDetailInfoModels.get(i).setOustockStatus(0);
                }
                BindListVIew(outStockDetailInfoModels);
            }else
            {
                ToastUtil.show(returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }



    void InitFrm(StockInfo_Model stockInfoModel){
        if(stockInfoModel!=null ){
            txtCompany.setText(stockInfoModel.getStrongHoldName());
            txtBatch.setText(stockInfoModel.getBatchNo());
            txtStatus.setText(stockInfoModel.getStatus());
            txtMaterialName.setText(stockInfoModel.getMaterialDesc());
        }
    }

    boolean CheckBarcode(StockInfo_Model StockInfo_Model){
        if(StockInfo_Model!=null && outStockDetailInfoModels!=null){
            OutStockDetailInfo_Model outStockDetailInfoModel=new OutStockDetailInfo_Model(StockInfo_Model.getMaterialNo());
            int index=outStockDetailInfoModels.indexOf(outStockDetailInfoModel);
            if(index!=-1){
                if (outStockDetailInfoModels.get(index).getLstStockInfo() == null)
                    outStockDetailInfoModels.get(index).setLstStockInfo(new ArrayList<StockInfo_Model>());
                int StockIndex = outStockDetailInfoModels.get(index).getLstStockInfo().indexOf(StockInfo_Model);
                if(StockIndex==-1) {
                    float qty = outStockDetailInfoModels.get(index).getScanQty() + StockInfo_Model.getQty();
                    if (qty <= outStockDetailInfoModels.get(index).getRemainQty()) {
                        outStockDetailInfoModels.get(index).getLstStockInfo().add(0, StockInfo_Model);
                        outStockDetailInfoModels.get(index).setScanQty(qty);
                        outStockDetailInfoModels.get(index).setOustockStatus(1); //存在未组托条码
                    } else {
                        MessageBox.Show(context, getString(R.string.Error_ReviewFinish));
                        return false;
                    }
                }
                else{
                    MessageBox.Show(context, R.string.Error_BarcodeScaned+"|"+StockInfo_Model.getSerialNo());
                    return false;
                }
            }else{
                MessageBox.Show(context, R.string.Error_BarcodeNotInList+"|"+StockInfo_Model.getSerialNo());
                return false;
            }
        }
        return true;
    }

    /*
    获取需要组托条码
     */
    ArrayList<PalletDetail_Model> GetPalletModels(){
        ArrayList<PalletDetail_Model> palletDetailModels=new ArrayList<>();
        if(outStockDetailInfoModels!=null) {
            for (OutStockDetailInfo_Model outstockDetailModel : outStockDetailInfoModels) {
                if (outstockDetailModel.getLstStockInfo() != null) {
                    List<StockInfo_Model> tempStockModels = new ArrayList<>();
                    for (StockInfo_Model stockModel : outstockDetailModel.getLstStockInfo()) {
                        if (stockModel.getStockBarCodeStatus() == 0) {
                            tempStockModels.add(0, stockModel);
                        }
                    }
                    if (tempStockModels.size() == 0)
                        continue;
                    PalletDetail_Model palletDetail_model = new PalletDetail_Model();
                    palletDetail_model.setErpVoucherNo(outstockDetailModel.getErpVoucherNo());
                    palletDetail_model.setVoucherNo(outstockDetailModel.getVoucherNo());
                    palletDetail_model.setRowNo(outstockDetailModel.getRowNo());
                    palletDetail_model.setVoucherType(999);
                    palletDetail_model.setMaterialNo(outstockDetailModel.getMaterialNo());
                    palletDetail_model.setMaterialNoID(outstockDetailModel.getMaterialNoID());
                    palletDetail_model.setMaterialDesc(outstockDetailModel.getMaterialDesc());
                    if (outstockDetailModel.getLstStockInfo() != null && outstockDetailModel.getLstStockInfo().size() != 0) {
                        palletDetail_model.setLstStockInfo(tempStockModels);
                    }
                    palletDetailModels.add(palletDetail_model);
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
