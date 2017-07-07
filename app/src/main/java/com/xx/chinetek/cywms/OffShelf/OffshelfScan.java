package com.xx.chinetek.cywms.OffShelf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.Qc.QCScan;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.OffShelf.OutStockTaskDetailsInfo_Model;
import com.xx.chinetek.model.OffShelf.OutStockTaskInfo_Model;
import com.xx.chinetek.model.QC.QualityDetailInfo_Model;
import com.xx.chinetek.model.ReturnMsgModelList;
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

import static com.xx.chinetek.cywms.R.id.tb_UnboxType;
import static com.xx.chinetek.util.function.GsonUtil.parseModelToJson;


@ContentView(R.layout.activity_offshelf_scan)
public class OffshelfScan extends BaseActivity {

    String TAG_GetT_OutTaskDetailListByHeaderIDADF="OffshelfScan_GetT_OutTaskDetailListByHeaderIDADF";
    String TAG_GetT_OutBarCodeInfoForQuanADF="OffshelfScan_GetT_OutBarCodeInfoForQuanADF";
    String TAG_SaveT_OutStockTaskDetailADF="OffshelfScan_SaveT_OutStockTaskDetailADF";

    private final int RESULT_Msg_GetT_OutTaskDetailListByHeaderIDADF=101;
   private final int RESULT_Msg_GetT_OutBarCodeInfoForQuanADF=102;
    private final int RESULT_Msg_SaveT_OutStockTaskDetailADF=103;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetT_OutTaskDetailListByHeaderIDADF:
                AnalysisGetT_OutTaskDetailListByHeaderIDADFJson((String) msg.obj);
                break;
            case RESULT_Msg_GetT_OutBarCodeInfoForQuanADF:
                AnalysisGetT_OutBarCodeInfoForQuanADFJson((String) msg.obj);
                break;
            case RESULT_Msg_SaveT_OutStockTaskDetailADF:
                AnalysisSaveT_OutStockTaskDetailADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtOffShelfScanbarcode);
                break;
        }
    }

   Context context=OffshelfScan.this;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(tb_UnboxType)
    ToggleButton tbUnboxType;
    @ViewInject(R.id.tb_PalletType)
    ToggleButton tbPalletType;
    @ViewInject(R.id.tb_BoxType)
    ToggleButton tbBoxType;
    @ViewInject(R.id.edt_OffShelfScanbarcode)
    EditText edtOffShelfScanbarcode;
    @ViewInject(R.id.edt_Unboxing)
    EditText edtUnboxing;
    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVourcherNo;
    @ViewInject(R.id.txt_SugestStock)
    TextView txtSugestStock;
    @ViewInject(R.id.txt_OffshelfNum)
    TextView txtOffshelfNum;
    @ViewInject(R.id.txt_Unboxing)
    TextView txtUnboxing;
    @ViewInject(R.id.btn_OutOfStock)
    TextView btnOutOfStock;
    @ViewInject(R.id.btn_BillDetail)
    TextView btnBillDetail;
    @ViewInject(R.id.btn_PrintBox)
    TextView btnPrintBox;

    OutStockTaskInfo_Model outStockTaskInfoModel;
    ArrayList<OutStockTaskDetailsInfo_Model> outStockTaskDetailsInfoModels;
    List<StockInfo_Model> stockInfoModels;//扫描 条码
    int currentPickMaterial=0;//当前拣货物料位置

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.OffShelf_subtitle), true);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        outStockTaskInfoModel=getIntent().getParcelableExtra("outStockTaskInfoModel");
        // txtVourcherNo.setText(outStockTaskInfoModel.getTaskNo());
        GetT_OutTaskDetailListByHeaderIDADF(outStockTaskInfoModel);

    }

    @Event(value ={R.id.tb_UnboxType,R.id.tb_PalletType,R.id.tb_BoxType} ,type = CompoundButton.OnClickListener.class)
    private void TBonCheckedChanged(View view) {
        tbUnboxType.setChecked(view.getId()== R.id.tb_UnboxType);
        tbPalletType.setChecked(view.getId()== R.id.tb_PalletType);
        tbBoxType.setChecked(view.getId()== R.id.tb_BoxType);
        ShowUnboxing(view.getId()== R.id.tb_UnboxType);
    }

    @Event(value =R.id.edt_OffShelfScanbarcode,type = View.OnKeyListener.class)
    private  boolean edtOffShelfScanbarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String code=edtOffShelfScanbarcode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            LogUtil.WriteLog(QCScan.class, TAG_GetT_OutBarCodeInfoForQuanADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutBarCodeInfoForQuanADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetT_OutBarCodeInfoForQuanADF, null, URLModel.GetURL().GetT_OutBarCodeInfoForQuanADF, params, null);
        }
        return false;
    }

    @Event(value = R.id.edt_Unboxing,type = View.OnKeyListener.class)
    private  boolean edtunboxingClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String num=edtUnboxing.getText().toString().trim();
            if(!CommonUtil.isFloat(num)) {
                MessageBox.Show(context,getString(R.string.Error_isnotnum));
                return true;
            }
            Float qty=Float.parseFloat(num); //输入数量
            Float scanQty=stockInfoModels.get(0).getQty(); //箱数量
            if(qty>scanQty){
                MessageBox.Show(context,getString(R.string.Error_PackageQtyBiger));
                return true;
            }
            if(qty>outStockTaskDetailsInfoModels.get(currentPickMaterial).getRemainQty()){
                MessageBox.Show(context,getString(R.string.Error_offshelfQtyBiger));
                return true;
            }
            //拆零
            stockInfoModels.get(0).setPickModel(3);
            stockInfoModels.get(0).setAmountQty(qty);
            outStockTaskDetailsInfoModels.get(currentPickMaterial).getLstStockInfo().add(0,stockInfoModels.get(0));
            currentPickMaterial++;
            ShowPickMaterialInfo(outStockTaskDetailsInfoModels.get(currentPickMaterial));
        }
        return false;
    }

    @Event(R.id.btn_OutOfStock)
    private void btnOutofStockClick(View view){
        final String MaterialDesc=outStockTaskDetailsInfoModels.get(currentPickMaterial).getMaterialDesc();
        new AlertDialog.Builder(context).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否跳过物料：\n"+MaterialDesc+"拣货？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法
                        currentPickMaterial++;
                        ShowPickMaterialInfo(outStockTaskDetailsInfoModels.get(currentPickMaterial));
                    }
                }).setNegativeButton("取消", null).show();
    }

    @Event(R.id.btn_BillDetail)
    private void btnBillDetailClick(View view){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            final Map<String, String> params = new HashMap<String, String>();
            String ModelJson = GsonUtil.parseModelToJson(outStockTaskDetailsInfoModels);
            String UserJson=GsonUtil.parseModelToJson(BaseApplication.userInfo);
            params.put("UserJson",UserJson );
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(OffshelfScan.class, TAG_SaveT_OutStockTaskDetailADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_OutStockTaskDetailADF, getString(R.string.Msg_SaveT_OutStockTaskDetailADF), context, mHandler, RESULT_Msg_SaveT_OutStockTaskDetailADF, null,  URLModel.GetURL().SaveT_OutStockTaskDetailADF, params, null);
        }
        return super.onOptionsItemSelected(item);
    }


    void GetT_OutTaskDetailListByHeaderIDADF(OutStockTaskInfo_Model outStockTaskInfoModel){
        if(outStockTaskInfoModel!=null) {
            final OutStockTaskDetailsInfo_Model outStockTaskDetailsInfoModel = new OutStockTaskDetailsInfo_Model();
            outStockTaskDetailsInfoModel.setHeaderID(outStockTaskInfoModel.getID());
            final Map<String, String> params = new HashMap<String, String>();
            params.put("ModelDetailJson", parseModelToJson(outStockTaskDetailsInfoModel));
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(OffshelfScan.class, TAG_GetT_OutTaskDetailListByHeaderIDADF, para);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutTaskDetailListByHeaderIDADF, getString(R.string.Msg_QualityDetailListByHeaderIDADF), context, mHandler, RESULT_Msg_GetT_OutTaskDetailListByHeaderIDADF, null,  URLModel.GetURL().GetT_OutTaskDetailListByHeaderIDADF, params, null);
        }
    }

    void AnalysisGetT_OutTaskDetailListByHeaderIDADFJson(String result){
        LogUtil.WriteLog(OffshelfScan.class, TAG_GetT_OutTaskDetailListByHeaderIDADF,result);
        ReturnMsgModelList<OutStockTaskDetailsInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutStockTaskDetailsInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            outStockTaskDetailsInfoModels=returnMsgModel.getModelJson();
            ShowPickMaterialInfo(outStockTaskDetailsInfoModels.get(currentPickMaterial));
        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
        }
    }

    void AnalysisGetT_OutBarCodeInfoForQuanADFJson(String result){
        LogUtil.WriteLog(QCScan.class, TAG_GetT_OutBarCodeInfoForQuanADF,result);
        ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            stockInfoModels=returnMsgModel.getModelJson();
            OutStockTaskDetailsInfo_Model currentOutTaskDetailInfo=outStockTaskDetailsInfoModels.get(currentPickMaterial);
            if(!currentOutTaskDetailInfo.getMaterialNo().equals(stockInfoModels.get(0).getMaterialNo())){
                MessageBox.Show(context,getString(R.string.Error_NotPickMaterial));
                CommonUtil.setEditFocus(edtOffShelfScanbarcode);
            }else{
                if(tbPalletType.isChecked()){//整托
                    Float scanQty=stockInfoModels.get(0).getPalletQty();
                    checkQTY(currentOutTaskDetailInfo,scanQty,true);
                }else if(tbBoxType.isChecked()){ //整箱
                    Float scanQty=stockInfoModels.get(0).getQty();
                    checkQTY(currentOutTaskDetailInfo,scanQty,false);
                }
                CommonUtil.setEditFocus(tbUnboxType.isChecked()?edtUnboxing:edtOffShelfScanbarcode);
            }
        }else
        {
            MessageBox.Show(context,returnMsgModel.getMessage());
            CommonUtil.setEditFocus(edtOffShelfScanbarcode);
        }
    }

    void AnalysisSaveT_OutStockTaskDetailADFJson(String result){
        try {
            LogUtil.WriteLog(OffshelfScan.class, TAG_SaveT_OutStockTaskDetailADF,result);
            ReturnMsgModelList<QualityDetailInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<QualityDetailInfo_Model>>() {}.getType());
            MessageBox.Show(context, returnMsgModel.getMessage());
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void checkQTY(OutStockTaskDetailsInfo_Model currentOutTaskDetailInfo,float scanQty,Boolean isPallet){
        if(currentOutTaskDetailInfo.getRemainQty()-scanQty<0){
            MessageBox.Show(context,getString(R.string.Error_offshelfQtyBiger));
            CommonUtil.setEditFocus(edtOffShelfScanbarcode);
        }else {
            if(isPallet){
                for (StockInfo_Model stockInfoModel:stockInfoModels) {
                    stockInfoModel.setPickModel(1);
                    outStockTaskDetailsInfoModels.get(currentPickMaterial).getLstStockInfo().add(0,stockInfoModel);
                }
            }
            else {
                stockInfoModels.get(0).setPickModel(2);
                outStockTaskDetailsInfoModels.get(currentPickMaterial).getLstStockInfo().add(0, stockInfoModels.get(0));
            }
            currentPickMaterial++;
            ShowPickMaterialInfo(outStockTaskDetailsInfoModels.get(currentPickMaterial));
        }
    }


    void ShowPickMaterialInfo(OutStockTaskDetailsInfo_Model outStockTaskDetailsInfoModel){
        txtCompany.setText(outStockTaskDetailsInfoModel.getStrongHoldName());
        txtBatch.setText(outStockTaskDetailsInfoModel.getBatchNo());
        txtStatus.setText(outStockTaskDetailsInfoModel.getStrStatus());
        txtMaterialName.setText(outStockTaskDetailsInfoModel.getMaterialDesc());
        txtSugestStock.setText(outStockTaskDetailsInfoModel.getAreaNo());
        txtOffshelfNum.setText(outStockTaskDetailsInfoModel.getRemainQty()+"");
    }

    void ShowUnboxing(Boolean show){
        int visiable=show? View.VISIBLE:View.GONE;
        txtUnboxing.setVisibility(visiable);
        edtUnboxing.setVisibility(visiable);
        btnPrintBox.setVisibility(visiable);
    }




}
