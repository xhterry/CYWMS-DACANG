package com.xx.chinetek.cywms.MaterialChange;

import android.content.Context;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.MaterialChange.MaterialChangeScanDetailAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.CheckNumRefMaterial;
import com.xx.chinetek.model.Receiption.ReceiptDetail_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Review.OutStockDetailInfo_Model;
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

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_material_change)
public class MaterialChange extends BaseActivity {

    String TAG_GetStockModelADF="MaterialChange_GetStockModelADF";
    String TAG_SaveT_BarCodeToStockADF="MaterialChange_SaveT_BarCodeToStockADF";
    String TAG_SaveT_ChangeMaterialADF="MaterialChange_SaveT_ChangeMaterialADF";
    private final int RESULT_Msg_GetStockModelADF=102;
    private final int RESULT_SaveT_BarCodeToStockADF = 104;
    private final int RESULT_SaveT_ChangeMaterialADF = 105;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetStockModelADF:
                AnalysisGetStockModelADFJson((String) msg.obj);
                break;
            case RESULT_SaveT_BarCodeToStockADF:
                AnalysisSaveT_BarCodeToStockADF((String) msg.obj);
                break;
            case RESULT_SaveT_ChangeMaterialADF:
                AnalysisSaveT_ChangeMaterialADF((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtChanggeScanBarcode);
                break;
        }
    }

    Context context=MaterialChange.this;

    @ViewInject(R.id.txt_InOrder)
    TextView txtInOrder;
    @ViewInject(R.id.txt_OutOrder)
    TextView txtOutOrder;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.txt_EDate)
    TextView txtEDate;
    @ViewInject(R.id.edt_ChanggeScanBarcode)
    EditText edtChanggeScanBarcode;
    @ViewInject(R.id.edt_Unboxing)
    EditText edtUnboxing;
    @ViewInject(R.id.txt_Unboxing)
    TextView txtUnboxing;
    @ViewInject(R.id.tb_UnboxType)
    ToggleButton tbUnboxType;
    @ViewInject(R.id.tb_PalletType)
    ToggleButton tbPalletType;
    @ViewInject(R.id.tb_BoxType)
    ToggleButton tbBoxType;
    @ViewInject(R.id.lsv_MaterialChangeList)
    ListView lsv_MaterialChangeList;

    ArrayList<ReceiptDetail_Model> receiptDetailModels;
    ArrayList<OutStockDetailInfo_Model> outStockDetailInfoModels;
    ArrayList<StockInfo_Model> stockInfoModels;
    MaterialChangeScanDetailAdapter materialChangeScanDetailAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.MaterialChange_scan_subtitle), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Override
    protected void initData() {
        super.initData();
        receiptDetailModels=getIntent().getParcelableArrayListExtra("receiptDetailModels");
        outStockDetailInfoModels=getIntent().getParcelableArrayListExtra("outStockDetailInfoModels");
        if(receiptDetailModels!=null && outStockDetailInfoModels!=null){
            txtInOrder.setText(receiptDetailModels.get(0).getErpVoucherNo());
            txtOutOrder.setText(outStockDetailInfoModels.get(0).getErpVoucherNo());
            BindListVIew(outStockDetailInfoModels);
        }
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
                    MessageBox.Show(context, getString(R.string.Error_CannotmaterialChange));
                    isFinishReceipt = false;
                    break;
                }
            }

            if (isFinishReceipt) {
                final Map<String, String> params = new HashMap<String, String>();
                String OutStockDetailListJson = GsonUtil.parseModelToJson(outStockDetailInfoModels);
                String InStockDetailListJson = GsonUtil.parseModelToJson(receiptDetailModels);
                String UserJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
                params.put("UserJson", UserJson);
                params.put("OutStockDetailListJson", OutStockDetailListJson);
                params.put("InStockDetailListJson", InStockDetailListJson);
                LogUtil.WriteLog(MaterialChange.class, TAG_SaveT_ChangeMaterialADF,"OutStockDetailListJson\r\n"+ OutStockDetailListJson+"\r\n\r\nInStockDetailListJson\r\n"+InStockDetailListJson);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_ChangeMaterialADF, getString(R.string.Msg_SaveT_ChangeMaterialADF), context, mHandler, RESULT_SaveT_ChangeMaterialADF, null, URLModel.GetURL().SaveT_ChangeMaterialADF, params, null);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Event(value ={R.id.tb_UnboxType,R.id.tb_PalletType,R.id.tb_BoxType} ,type = CompoundButton.OnClickListener.class)
    private void TBonCheckedChanged(View view) {
        tbUnboxType.setChecked(view.getId()== R.id.tb_UnboxType);
        tbPalletType.setChecked(view.getId()== R.id.tb_PalletType);
        tbBoxType.setChecked(view.getId()== R.id.tb_BoxType);
        ShowUnboxing(view.getId()== R.id.tb_UnboxType);
    }

    @Event(value = R.id.edt_ChanggeScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtChanggeScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String code=edtChanggeScanBarcode.getText().toString().trim();
            int type=tbPalletType.isChecked()?1:(tbBoxType.isChecked()?2:3);
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            params.put("ScanType", type+"");
            params.put("MoveType", "1"); //1：下架 2:移库
            params.put("IsEdate","");
            LogUtil.WriteLog(MaterialChange.class, TAG_GetStockModelADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetStockModelADF, null, URLModel.GetURL().GetStockModelADF, params, null);
        }
        return false;
    }

    @Event(value = R.id.edt_Unboxing,type = View.OnKeyListener.class)
    private  boolean edtunboxingClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String num=edtUnboxing.getText().toString().trim();
            CheckNumRefMaterial checkNumRefMaterial=CheckMaterialNumFormat(num,stockInfoModels.get(0).getUnitTypeCode(),stockInfoModels.get(0).getDecimalLngth());
            if(!checkNumRefMaterial.ischeck()) {
                MessageBox.Show(context,checkNumRefMaterial.getErrMsg());
                CommonUtil.setEditFocus(edtUnboxing);
                return true;
            }
            Float qty=checkNumRefMaterial.getCheckQty(); //输入数量
            Float scanQty=stockInfoModels.get(0).getQty(); //箱数量
            if(qty>scanQty){
                MessageBox.Show(context,getString(R.string.Error_PackageQtyBiger));
                CommonUtil.setEditFocus(edtUnboxing);
                return true;
            }
            OutStockDetailInfo_Model outStockDetailInfoModel = new OutStockDetailInfo_Model(stockInfoModels.get(0).getMaterialNo(), stockInfoModels.get(0).getStrongHoldCode());
            int index = outStockDetailInfoModels.indexOf(outStockDetailInfoModel);
            if (index != -1){
                Float remainqty=ArithUtil.sub(outStockDetailInfoModels.get(index).getOutStockQty(),
                        outStockDetailInfoModels.get(index).getScanQty());
                if (qty >remainqty ) {
                    MessageBox.Show(context, getString(R.string.Error_offshelfQtyBiger));
                    CommonUtil.setEditFocus(edtUnboxing);
                    return true;
                }
                //拆零
                stockInfoModels.get(0).setPickModel(3);
                stockInfoModels.get(0).setAmountQty(qty);
                String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
                String strOldBarCode = GsonUtil.parseModelToJson(stockInfoModels.get(0));
                final Map<String, String> params = new HashMap<String, String>();
                params.put("UserJson", userJson);
                params.put("strOldBarCode", strOldBarCode);
                params.put("strNewBarCode", "");
                params.put("PrintFlag","1"); //1：打印 2：不打印
                LogUtil.WriteLog(MaterialChange.class, TAG_SaveT_BarCodeToStockADF, strOldBarCode);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_BarCodeToStockADF, getString(R.string.Msg_SaveT_BarCodeToStockADF), context, mHandler, RESULT_SaveT_BarCodeToStockADF, null, URLModel.GetURL().SaveT_BarCodeToStockADF, params, null);
            }
        }
        return false;
    }

    /*
  扫描条码
   */
    void AnalysisGetStockModelADFJson(String result) {
        LogUtil.WriteLog(MaterialChange.class, TAG_GetStockModelADF, result);
        try {
            ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
               stockInfoModels = returnMsgModel.getModelJson();
                if (stockInfoModels != null && stockInfoModels.size() != 0) {
                    //判断条码是否已经扫描
                    if (CheckBarcodeScaned(stockInfoModels)) {
                        OutStockDetailInfo_Model outStockDetailInfoModel = new OutStockDetailInfo_Model(stockInfoModels.get(0).getMaterialNo(), stockInfoModels.get(0).getStrongHoldCode());
                        int index = outStockDetailInfoModels.indexOf(outStockDetailInfoModel);
                        if (index != -1) {
                            String outBatchNo = outStockDetailInfoModels.get(0).getFromBatchNo();
                            String outAreaNo = outStockDetailInfoModels.get(0).getFromErpAreaNo();
                            String outWarehouse = outStockDetailInfoModels.get(0).getFromErpWarehouse();
                            if ( outBatchNo.equals(stockInfoModels.get(0).getBatchNo())
                                    && outAreaNo.equals(stockInfoModels.get(0).getAreaNo())
                                   && outWarehouse.equals(stockInfoModels.get(0).getWarehouseNo())) {
                                txtCompany.setText(stockInfoModels.get(0).getStrongHoldName());
                                txtBatch.setText(stockInfoModels.get(0).getBatchNo());
                                txtStatus.setText(stockInfoModels.get(0).getStrStatus());
                                txtMaterialName.setText(stockInfoModels.get(0).getMaterialDesc());
                                txtEDate.setText(CommonUtil.DateToString(stockInfoModels.get(0).getEDate()));
                                if (tbPalletType.isChecked()) {//整托
                                    Float scanQty = stockInfoModels.get(0).getPalletQty();
                                    checkQTY(scanQty, true,index);
                                } else if (tbBoxType.isChecked()) { //整箱
                                    Float scanQty = stockInfoModels.get(0).getQty();
                                    checkQTY(scanQty, false,index);
                                }
                                CommonUtil.setEditFocus(tbUnboxType.isChecked() ? edtUnboxing : edtChanggeScanBarcode);

                            } else {
                                MessageBox.Show(context, getString(R.string.Error_materialChangenotMatch));
                                CommonUtil.setEditFocus(edtChanggeScanBarcode);
                            }
                        }else{
                            MessageBox.Show(context, getString(R.string.Error_BarcodeNotInList));
                            CommonUtil.setEditFocus(edtChanggeScanBarcode);
                        }
                    }
                }
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
                CommonUtil.setEditFocus(edtChanggeScanBarcode);
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
            CommonUtil.setEditFocus(edtChanggeScanBarcode);
        }

    }

    /*
拆箱提交
 */
    void AnalysisSaveT_BarCodeToStockADF(String result){
        try {
            LogUtil.WriteLog(MaterialChange.class, TAG_SaveT_BarCodeToStockADF, result);
            ReturnMsgModel<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<StockInfo_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                StockInfo_Model stockInfoModel=returnMsgModel.getModelJson();
                stockInfoModels=new ArrayList<>();
                stockInfoModels.add(stockInfoModel);
                OutStockDetailInfo_Model outStockDetailInfoModel = new OutStockDetailInfo_Model(stockInfoModels.get(0).getMaterialNo(), stockInfoModels.get(0).getStrongHoldCode());
                int index = outStockDetailInfoModels.indexOf(outStockDetailInfoModel);
                SetOutStockTaskDetailsInfoModels(stockInfoModel.getQty(),3,index);
            }
            else{
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        edtUnboxing.setText("");
        CommonUtil.setEditFocus(edtChanggeScanBarcode);

    }

    void AnalysisSaveT_ChangeMaterialADF(String result){
        try {
            LogUtil.WriteLog(MaterialChange.class, TAG_SaveT_ChangeMaterialADF,result);
            final ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                closeActiviry();
            }else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    Boolean CheckBarcodeScaned(ArrayList<StockInfo_Model> stockInfoModels){
        if(!tbUnboxType.isChecked()) { //整箱、整托需要检查条码是否扫描
            for (OutStockDetailInfo_Model temoStockTaskDetail : outStockDetailInfoModels) {
                if(temoStockTaskDetail.getLstStock()!=null) {
                    if (temoStockTaskDetail.getLstStock().indexOf(stockInfoModels.get(0)) != -1) {
                        MessageBox.Show(context, getString(R.string.Error_Barcode_hasScan));
                        CommonUtil.setEditFocus(edtChanggeScanBarcode);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    void checkQTY(float scanQty,Boolean isPallet,int index) {
        //根据物料查询扫描剩余数量的总数
        if (outStockDetailInfoModels.get(index).getOutStockQty()< scanQty  ) {
            MessageBox.Show(context, getString(R.string.Error_materialChangeQtyBiger));
            CommonUtil.setEditFocus(edtChanggeScanBarcode);
            return;
        }
        SetOutStockTaskDetailsInfoModels(scanQty,isPallet?1:2,index);

    }

    void  SetOutStockTaskDetailsInfoModels(Float scanQty,int type,int index) {
        if( outStockDetailInfoModels.get(index).getLstStock()==null)
            outStockDetailInfoModels.get(index).setLstStock(new ArrayList<StockInfo_Model>());
        switch (type) {
            case 1: //托盘
                for (StockInfo_Model stockInfoModel : stockInfoModels) {
                    stockInfoModel.setPickModel(1);
                    stockInfoModel.setOKSelect(true);
                    outStockDetailInfoModels.get(index).
                            setScanQty(ArithUtil.add(outStockDetailInfoModels.get(index).getScanQty(),stockInfoModel.getQty()));
                    outStockDetailInfoModels.get(index).getLstStock().add(0, stockInfoModel);
                }
                break;
            case 2://箱子
                stockInfoModels.get(0).setPickModel(2);
                stockInfoModels.get(0).setOKSelect(true);
                outStockDetailInfoModels.get(index).
                        setScanQty(ArithUtil.add(outStockDetailInfoModels.get(index).getScanQty(),scanQty));
                outStockDetailInfoModels.get(index).getLstStock().add(0, stockInfoModels.get(0));
                break;
            case 3: //拆零
                stockInfoModels.get(0).setPickModel(3);
                stockInfoModels.get(0).setOKSelect(true);
                outStockDetailInfoModels.get(index).
                        setScanQty(ArithUtil.add(outStockDetailInfoModels.get(index).getScanQty(), scanQty));
                outStockDetailInfoModels.get(index).getLstStock().add(0, stockInfoModels.get(0));
                break;
        }
        BindListVIew(outStockDetailInfoModels);
    }

    void ShowUnboxing(Boolean show){
        int visiable=show? View.VISIBLE:View.GONE;
        txtUnboxing.setVisibility(visiable);
        edtUnboxing.setVisibility(visiable);
    }

    private void BindListVIew(ArrayList<OutStockDetailInfo_Model> outStockDetailInfoModels) {
        materialChangeScanDetailAdapter=new MaterialChangeScanDetailAdapter(context,outStockDetailInfoModels);
        lsv_MaterialChangeList.setAdapter(materialChangeScanDetailAdapter);
    }
}
