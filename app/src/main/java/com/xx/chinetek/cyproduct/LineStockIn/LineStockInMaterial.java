package com.xx.chinetek.cyproduct.LineStockIn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.product.LineStockIn.LineStockInMaterialItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cyproduct.work.ReportOutputNum;
import com.xx.chinetek.cywms.InnerMove.InnerMoveDetail;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.cywms.Receiption.ReceiptionScan;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Production.LineStockIn.LineStockInProductModel;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.UerInfo;
import com.xx.chinetek.model.WMS.Stock.AreaInfo_Model;
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

@ContentView(R.layout.activity_line_stock_in_material)
public class LineStockInMaterial extends BaseActivity {

    String TAG_GetPalletDetailByBarCode_Product="LineStockInMaterial_GetPalletDetailByBarCode_Product";
    String TAG_SaveModeListForT_StockT="LineStockInMaterial_SaveModeListForT_StockT";
    private final int RESULT_Msg_GetPalletDetailByBarCode_Product=102;
    private final int RESULT_SaveModeListForT_StockT=101;

    String TAG_GetAreaModelADF="LineStockInMaterial_GetT_SaveInStockModelADF";
    private final int RESULT_Msg_GetAreaModelADF=103;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetAreaModelADF:
                AnalysisetT_GetAreaModelADFJson((String) msg.obj);
                break;
            case RESULT_SaveModeListForT_StockT:
                AnalysisetSaveModeListForT_StockTJson((String) msg.obj);
                break;
            case RESULT_Msg_GetPalletDetailByBarCode_Product:
                AnalysiseGetPalletDetailByBarCode_ProductJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                CommonUtil.setEditFocus(edtLineStockInBarcode);
                break;
        }
    }


    @ViewInject(R.id.lsv_LineStockInMaterial)
    ListView lsvLineStockInMaterial;
    @ViewInject(R.id.edt_LineStockInBarcode)
    EditText edtLineStockInBarcode;
    @ViewInject(R.id.editArea)
    EditText editArea;
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
    @ViewInject(R.id.txt_WareHousName)
    TextView txtWareHousName;

    Context context=LineStockInMaterial.this;
    ArrayList<LineStockInProductModel> lineStockInProductModels;
    LineStockInMaterialItemAdapter lineStockInMaterialItemAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle=new ToolBarTitle(getString(R.string.LineStockInMaterialY),true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
        lineStockInProductModels=new ArrayList<>();
        txtWareHousName.setText(BaseApplication.userInfo.getWarehouseName());
        CommonUtil.setEditFocus(editArea);
    }

    @Event(value = R.id.edt_LineStockInBarcode,type = View.OnKeyListener.class)
    private  boolean edtLineInstockScanbarcode(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String code=edtLineStockInBarcode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            LogUtil.WriteLog(ReceiptionScan.class, TAG_GetPalletDetailByBarCode_Product, code);
//            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetPalletDetailByBarCode_Product, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetPalletDetailByBarCode_Product, null,  URLModel.GetURL().GetPalletDetailByBarCode_Product, params, null);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetPalletDetailByBarCode_Product, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetPalletDetailByBarCode_Product, null,  URLModel.GetURL().GetPalletDetailByBarCodeForInStock, params, null);
        }
        return false;
    }

    @Event(value = R.id.editArea,type = View.OnKeyListener.class)
    private  boolean edtarea(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String code = editArea.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                CommonUtil.setEditFocus(editArea);
                return true;
            }
            final Map<String, String> params = new HashMap<String, String>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("AreaNo", code);
            LogUtil.WriteLog(ReportOutputNum.class, TAG_GetAreaModelADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreaModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetAreaModelADF, null,  URLModel.GetURL().GetAreaModelADF, params, null);

        }
        return false;
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
            //提交
            if(lineStockInProductModels!=null && lineStockInProductModels.size()!=0){
                ArrayList<BarCodeInfo> SumbitbarCodeInfos=new ArrayList<>();
                for (LineStockInProductModel lineStockInProduct:lineStockInProductModels) {
                    if(lineStockInProduct.getBarCodeInfos()!=null && lineStockInProduct.getBarCodeInfos().size()!=0){
                        SumbitbarCodeInfos.addAll(0,lineStockInProduct.getBarCodeInfos());
                    }
                }
                if(SumbitbarCodeInfos.size()!=0) {
                    final Map<String, String> params = new HashMap<String, String>();
                    String ModelJson = GsonUtil.parseModelToJson(SumbitbarCodeInfos);

                    //设置入库的货位ID
                    UerInfo user = BaseApplication.userInfo;
                    if(!WgFlag)
                    {
                        MessageBox.Show(context, "没有扫描正确库位！");
                        return false;
                    }
                    user.setReceiveAreaID(LReceiveAreaID);
                    user.setWarehouseID(LWarehouseID);
                    user.setReceiveHouseID(LReceiveHouseID);


                    String UserJson = GsonUtil.parseModelToJson(user);
                    params.put("UserJson", UserJson);
                    params.put("ModelJson", ModelJson);
                    LogUtil.WriteLog(ReceiptionScan.class, TAG_SaveModeListForT_StockT, ModelJson);
//                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveModeListForT_StockT, getString(R.string.Msg_SaveT_LineInStockProductlADF), context, mHandler, RESULT_SaveModeListForT_StockT, null, URLModel.GetURL().SaveModeListForT_StockT, params, null);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveModeListForT_StockT, getString(R.string.Msg_SaveT_LineInStockProductlADF), context, mHandler, RESULT_SaveModeListForT_StockT, null, URLModel.GetURL().SaveModeListForLingLiao_Stock, params, null);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Event(value = R.id.lsv_LineStockInMaterial,type =  AdapterView.OnItemClickListener.class)
    private  boolean lsvLineStockInMaterialClick(AdapterView<?> parent, View view, int position, long id){
        if(id>=0) {
            LineStockInProductModel lineStockInProduct=(LineStockInProductModel)lineStockInMaterialItemAdapter.getItem(position);
            if (lineStockInProduct.getBarCodeInfos().size() != 0) {
                Intent intent = new Intent(context, InnerMoveDetail.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("barCodeInfos", lineStockInProduct.getBarCodeInfos());
                intent.putExtras(bundle);
                startActivityLeft(intent);
            }
        }
        return true;
    }

    private int LReceiveAreaID=0;
    private int LReceiveHouseID=0;
    private int LWarehouseID=0;
    private boolean WgFlag=false;
    void AnalysisetT_GetAreaModelADFJson(String result){
        try {
            LogUtil.WriteLog(LineStockInMaterial.class, TAG_GetAreaModelADF,result);
            ReturnMsgModel<AreaInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<AreaInfo_Model>>() {}.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                LReceiveAreaID=returnMsgModel.getModelJson().getID();
                LReceiveHouseID=returnMsgModel.getModelJson().getHouseID();
                LWarehouseID=returnMsgModel.getModelJson().getWarehouseID();
                if(String.valueOf(BaseApplication.userInfo.getWarehouseID()).equals(String.valueOf(LWarehouseID))){
                    WgFlag=true;
                }else{
                    MessageBox.Show(context,"扫描的货位不在登陆人的仓库下！");
                    editArea.setText("");
                    CommonUtil.setEditFocus(editArea);
                }
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
                CommonUtil.setEditFocus(editArea);
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
        CommonUtil.setEditFocus(edtLineStockInBarcode);
    }

    /*
   扫描条码
    */
    void AnalysiseGetPalletDetailByBarCode_ProductJson(String result){
        LogUtil.WriteLog(ReceiptionScan.class, TAG_GetPalletDetailByBarCode_Product,result);
        try {
            ReturnMsgModelList<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<BarCodeInfo>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ArrayList<BarCodeInfo> barCodeInfos = returnMsgModel.getModelJson();
                Bindbarcode(barCodeInfos);
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
        CommonUtil.setEditFocus(edtLineStockInBarcode);
    }

    void  AnalysisetSaveModeListForT_StockTJson(String result){
        LogUtil.WriteLog(LineStockInProduct.class, TAG_SaveModeListForT_StockT,result);
        ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {}.getType());
        try {
            MessageBox.Show(context,returnMsgModel.getMessage());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ClearFrm();
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
        CommonUtil.setEditFocus(edtLineStockInBarcode);
    }


    void Bindbarcode(final ArrayList<BarCodeInfo> barCodeInfos){
        if (barCodeInfos != null && barCodeInfos.size() != 0) {
            try {
                String MaterialNo=barCodeInfos.get(0).getMaterialNo();
                String BatchNo=barCodeInfos.get(0).getBatchNo();
                String MaterialDesc=barCodeInfos.get(0).getMaterialDesc();
                Float SumQty=0f;
                for (BarCodeInfo barcodinfo:barCodeInfos) {
                    SumQty= ArithUtil.add(SumQty,barcodinfo.getQty());
                }
                final Float sumQty=SumQty;
                LineStockInProductModel templineStockIn=new LineStockInProductModel(MaterialNo,BatchNo);
                final int index=lineStockInProductModels.indexOf(templineStockIn);
                if(index!=-1){
                    if(lineStockInProductModels.get(index).getBarCodeInfos().indexOf(barCodeInfos.get(0))!=-1){
                        //MessageBox.Show(context,getString(R.string.Error_Barcode_hasScan));
                        new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除已扫描条码？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO 自动生成的方法
                                        lineStockInProductModels.get(index).getBarCodeInfos().removeAll(barCodeInfos);
                                        lineStockInProductModels.get(index).setQty(ArithUtil.sub(lineStockInProductModels.get(index).getQty(),sumQty));
                                        if( lineStockInProductModels.get(index).getBarCodeInfos().size()==0){
                                            lineStockInProductModels.remove(index);
                                        }
                                        InitFrm(barCodeInfos.get(0));
                                        BindListVIew(lineStockInProductModels);
                                    }
                                }).setNegativeButton("取消", null).show();
                        return;
                    }
                    lineStockInProductModels.get(index).setQty(ArithUtil.add(lineStockInProductModels.get(index).getQty(),SumQty));
                    lineStockInProductModels.get(index).getBarCodeInfos().addAll(0,barCodeInfos);
                }else{
                    templineStockIn.setMaterialDesc(MaterialDesc);
                    templineStockIn.setQty(SumQty);
                    if(templineStockIn.getBarCodeInfos()==null)
                        templineStockIn.setBarCodeInfos(new ArrayList<BarCodeInfo>());
                    templineStockIn.getBarCodeInfos().addAll(0,barCodeInfos);
                    lineStockInProductModels.add(0,templineStockIn);
                }
                InitFrm(barCodeInfos.get(0));
                BindListVIew(lineStockInProductModels);
            }catch (Exception ex){
                MessageBox.Show(context,ex.getMessage());
                CommonUtil.setEditFocus(edtLineStockInBarcode);
            }

        }
    }


    private void BindListVIew(ArrayList<LineStockInProductModel> lineStockInProductModels) {
        lineStockInMaterialItemAdapter=new LineStockInMaterialItemAdapter(context,lineStockInProductModels);
        lsvLineStockInMaterial.setAdapter(lineStockInMaterialItemAdapter);
    }

    void InitFrm(BarCodeInfo barCodeInfo){
        try {
            if (barCodeInfo != null) {
                txtCompany.setText(barCodeInfo.getStrongHoldName());
                txtBatch.setText(barCodeInfo.getBatchNo());
                txtStatus.setText("");
                txtMaterialName.setText(barCodeInfo.getMaterialDesc());
                txtEDate.setText(CommonUtil.DateToString(barCodeInfo.getEDate()));
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
            CommonUtil.setEditFocus(edtLineStockInBarcode);
        }
    }

    void ClearFrm(){
        lineStockInProductModels = new ArrayList<>();
        edtLineStockInBarcode.setText("");
        txtCompany.setText("");
        txtBatch.setText("");
        txtEDate.setText("");
        txtStatus.setText("");
        txtMaterialName.setText("");
        BindListVIew(lineStockInProductModels);
    }
}
