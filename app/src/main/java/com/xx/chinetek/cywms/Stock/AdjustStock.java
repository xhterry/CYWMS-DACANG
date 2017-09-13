package com.xx.chinetek.cywms.Stock;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.WareHouseInfo;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_adjust_stock)
public class AdjustStock extends BaseActivity {

    String TAG_GetWareHouse = "AdjustStock_GetWareHouse";
    String TAG_GetInfoBySerial = "AdjustStock_GetInfoBySerial";
    String TAG_SaveInfo = "AdjustStock_SaveInfo";
    private final int RESULT_GetWareHouse = 101;
    private final int RESULT_GetInfoBySerial = 102;
    private final int RESULT_SaveInfo = 103;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetWareHouse:
                AnalysisGetWareHouseJson((String) msg.obj);
                break;
            case RESULT_GetInfoBySerial:
                AnalysisGetInfoBySerialJson((String) msg.obj);
                break;
            case RESULT_SaveInfo:
                AnalysisSaveInfoJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }


   Context context=AdjustStock.this;

    @ViewInject(R.id.edt_AdjustScanBarcode)
    EditText  edtAdjustScanBarcode;
    @ViewInject(R.id.edt_AdjustNum)
    EditText  edtAdjustNum;
    @ViewInject(R.id.edt_AdjustStock)
    EditText  edtAdjustStock;
    @ViewInject(R.id.edt_AdjustBatchNo)
    EditText  edtAdjustBatchNo;
    @ViewInject(R.id.txt_QCStatus)
    TextView txtQCStatus;
    @ViewInject(R.id.txt_Warehouse)
    TextView txtWarehouse;
    @ViewInject(R.id.txt_changeEData)
    TextView txtchangeEData;
    @ViewInject(R.id.txt_StrongHold)
    TextView txtStrongHold;
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
    @ViewInject(R.id.btn_Delete)
    TextView btnDelete;
    @ViewInject(R.id.btn_Submit)
    TextView btnSubmit;

    Barcode_Model barcodeModel;
    String[] QCStatus={"待检","检验合格","检验不合格"};
    int[] QCStatusType={1,3,4};
    String[] StrongHoldCode={"CY1","CX1","FC1"};
    String[] StrongHoldName={"上海创元","上海创馨","上海甫成"};


    int mYear, mMouth,mDay;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.adjust_title), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
        barcodeModel=null;
    }


    @Event(value = R.id.txt_Warehouse,type =View.OnClickListener.class )
    private void txtSelectWohouseClick(View view){
        if(barcodeModel!=null) {
            LogUtil.WriteLog(AdjustStock.class, TAG_GetWareHouse, "");
            final Map<String, String> params = new HashMap<>();
            try {
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetWareHouse, getString(R.string.Msg_GetWareHouse), context, mHandler, RESULT_GetWareHouse, null, URLModel.GetURL().GetWareHouse, params, null);
            } catch (Exception ex) {
                MessageBox.Show(context, ex.getMessage());
            }
            CommonUtil.setEditFocus(edtAdjustScanBarcode);
        }
    }

    @Event(value =R.id.txt_StrongHold,type =View.OnClickListener.class )
    private void txtStrongHoldClick(View view){
        if(barcodeModel!=null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("选择据点");
            builder.setCancelable(false);
            builder.setItems(StrongHoldName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    txtStrongHold.setText(StrongHoldName[which]);
                    barcodeModel.setStrongHoldCode(StrongHoldCode[which]);
                    barcodeModel.setStrongHoldName(StrongHoldName[which]);
                }
            });
            builder.show();
        }

    }

    @Event(value =R.id.edt_AdjustScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtAdjustScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String barcode = edtAdjustScanBarcode.getText().toString().trim();
            if (!barcode.equals("")) {
                final Map<String, String> params = new HashMap<String, String>();
                params.put("barcode", barcode);
                String para = (new JSONObject(params)).toString();
                LogUtil.WriteLog(AdjustStock.class, TAG_GetInfoBySerial, para);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetInfoBySerial, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetInfoBySerial, null, URLModel.GetURL().GetInfoBySerial, params, null);
            }
        }
        return false;
    }


    @Event(value = R.id.txt_QCStatus,type =View.OnClickListener.class )
    private void txtQCStatusClick(View view){
        if(barcodeModel!=null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("选择质检状态");
            builder.setCancelable(false);
            builder.setItems(QCStatus, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    txtQCStatus.setText(QCStatus[which]);
                    barcodeModel.setSTATUS(QCStatusType[which]);
                }
            });
            builder.show();
        }
    }

    @Event(value = R.id.txt_changeEData,type = View.OnClickListener.class )
    private void txtProductStartTimeClick(View view){
        if(barcodeModel!=null) {
            if (barcodeModel.getEds() == null || barcodeModel.getEds().equals("")) {
                final Calendar ca = Calendar.getInstance();
                mYear =ca.get(Calendar.YEAR);
                mMouth =ca.get(Calendar.MONTH);
                mDay = ca.get(Calendar.DAY_OF_MONTH);
            } else {
                String[] date = barcodeModel.getEds().split("/");
                if (date.length >= 3) {
                    mYear = Integer.parseInt(date[0]);
                    mMouth = Integer.parseInt(date[1]) - 1;
                    mDay = Integer.parseInt(date[2]);
                }
            }
            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mYear = year;
                    mMouth = month + 1;
                    mDay = dayOfMonth;
                    txtchangeEData.setText(display());
                    barcodeModel.setEds(display1());
                }
            }, mYear, mMouth, mDay).show();

        }
    }


    public String  display() {
        return new StringBuffer().append(mYear).append("-").append(mMouth).append("-").append(mDay).toString();
    }
    public String  display1() {
        return new StringBuffer().append(mYear).append("/").append(mMouth).append("/").append(mDay).toString();
    }


    @Event(value = {R.id.btn_Delete,R.id.btn_Submit},type = View.OnClickListener.class)
    private  void btnDelete(View view){
        boolean isBtnDelete=R.id.btn_Delete==view.getId();
        if(barcodeModel!=null) {
            if (isBtnDelete)
                barcodeModel.setAllIn("2");
            String adjustBatchNo = edtAdjustBatchNo.getText().toString();
            String adjustNum = edtAdjustNum.getText().toString();
            String adjustStock = edtAdjustStock.getText().toString();
            if (!isBtnDelete) {
                if (TextUtils.isEmpty(adjustBatchNo)) {
                    MessageBox.Show(context, getString(R.string.Error_BatchNoIsEmpty));
                    CommonUtil.setEditFocus(edtAdjustBatchNo);
                    return;
                }
                if (TextUtils.isEmpty(adjustStock)) {
                    MessageBox.Show(context, getString(R.string.Error_StockIsEmpty));
                    CommonUtil.setEditFocus(edtAdjustStock);
                    return;
                }
                if (!CommonUtil.isFloat(adjustNum)) {
                    MessageBox.Show(context, getString(R.string.Error_isnotnum));
                    CommonUtil.setEditFocus(edtAdjustNum);
                    return;
                }
                if(Float.parseFloat(adjustNum)==0f){
                    MessageBox.Show(context, getString(R.string.Error_isnotzero));
                    CommonUtil.setEditFocus(edtAdjustNum);
                    return;
                }
            }
            barcodeModel.setBatchNo(adjustBatchNo);
            barcodeModel.setAreano(adjustStock);
            barcodeModel.setQty(Float.parseFloat(adjustNum));
            barcodeModel.setEDate(CommonUtil.dateStrConvertDate( txtchangeEData.getText().toString()));

            ArrayList<Barcode_Model> barcodeModels=new ArrayList<>();
            barcodeModels.add(barcodeModel);
            final Map<String, String> params = new HashMap<String, String>();
            String ModelJson= GsonUtil.parseModelToJson(barcodeModels);
            params.put("json", ModelJson);
            params.put("man", BaseApplication.userInfo.getUserNo());
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(AdjustStock.class, TAG_SaveInfo, para);
            if(isBtnDelete){
                new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除物料？\n" + barcodeModel.getSerialNo() )
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO 自动生成的方法
                                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveInfo, getString(R.string.Msg_AdjustStockSubmit), context, mHandler, RESULT_SaveInfo, null, URLModel.GetURL().SaveInfo, params, null);
                            }
                        }).setNegativeButton("取消", null).show();
            }else{
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveInfo, getString(R.string.Msg_AdjustStockSubmit), context, mHandler, RESULT_SaveInfo, null, URLModel.GetURL().SaveInfo, params, null);

            }
        }

    }



    void AnalysisGetWareHouseJson(String result) {
        try {
            LogUtil.WriteLog(AdjustStock.class, TAG_GetWareHouse, result);
            ReturnMsgModelList<WareHouseInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<WareHouseInfo>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                final ArrayList<WareHouseInfo> wareHouseInfos = returnMsgModel.getModelJson();
                int size = wareHouseInfos.size();
                String[] wareHouseInfo = new String[size];
                for (int i = 0; i < size; i++) {
                    wareHouseInfo[i] = wareHouseInfos.get(i).getWareHouseNo() + wareHouseInfos.get(i).getWareHouseName();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("选择盘点所属仓库");
                builder.setCancelable(false);
                builder.setItems(wareHouseInfo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        barcodeModel.setWarehouseno(wareHouseInfos.get(which).getWareHouseNo());
                        barcodeModel.setWarehousename(wareHouseInfos.get(which).getWareHouseName());
                     //   WareHouseNo=wareHouseInfos.get(which).getWareHouseNo();
                    //    WareHouseName= wareHouseInfos.get(which).getWareHouseName();
                        txtWarehouse.setText(wareHouseInfos.get(which).getWareHouseName());
                       }
                });
                builder.show();
                CommonUtil.setEditFocus(edtAdjustScanBarcode);
            } else
                MessageBox.Show(context, returnMsgModel.getMessage());
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetInfoBySerialJson(String result){
        try {
            LogUtil.WriteLog(AdjustStock.class, TAG_GetWareHouse, result);
            ReturnMsgModelList<Barcode_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Barcode_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
               ArrayList<Barcode_Model> barcodeModels=returnMsgModel.getModelJson();
                if(barcodeModels!=null && barcodeModels.size()!=0) {
                    barcodeModel = barcodeModels.get(0);
                    txtCompany.setText(barcodeModel.getStrongHoldName());
                    txtBatch.setText(barcodeModel.getBatchNo());
                    txtStatus.setText("");
                    txtEDate.setText(barcodeModel.getEds());
                    txtMaterialName.setText(barcodeModel.getMaterialDesc());
                    txtStrongHold.setText(barcodeModel.getStrongHoldName());
                    edtAdjustBatchNo.setText(barcodeModel.getBatchNo());
                    edtAdjustNum.setText(barcodeModel.getQty() + "");
                    txtchangeEData.setText(barcodeModel.getEds());
                    txtQCStatus.setText(getQCStrStatus(barcodeModel.getSTATUS()));
                    txtWarehouse.setText(barcodeModel.getWarehousename());
                    edtAdjustStock.setText(barcodeModel.getAreano());
                    boolean isInsert = barcodeModel.getAllIn().equals("0");
                    txtStrongHold.setEnabled(!isInsert);
                    edtAdjustBatchNo.setEnabled(!isInsert);
                    edtAdjustNum.setEnabled(!isInsert);
                }
            } else
                MessageBox.Show(context, returnMsgModel.getMessage());
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        CommonUtil.setEditFocus(edtAdjustScanBarcode);
    }

    void AnalysisSaveInfoJson(String result){
        try {
            LogUtil.WriteLog(AdjustStock.class, TAG_SaveInfo,result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                MessageBox.Show(context, returnMsgModel.getMessage());
                barcodeModel=null;
                edtAdjustScanBarcode.setText("");
                edtAdjustStock.setText("");
                edtAdjustBatchNo.setText("");
                edtAdjustNum.setText("");
                txtCompany.setText("");
                txtBatch.setText("");
                txtStatus.setText("");
                txtEDate.setText("");
                txtchangeEData.setText("");
                txtMaterialName.setText("");
                txtQCStatus.setText("");
                txtWarehouse.setText("");
                txtStrongHold.setText("");
                txtStrongHold.setEnabled(true);
                edtAdjustBatchNo.setEnabled(true);
                edtAdjustNum.setEnabled(true);

            }else
                MessageBox.Show(context, returnMsgModel.getMessage());
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        CommonUtil.setEditFocus(edtAdjustScanBarcode);
    }

    String  getQCStrStatus(int status){
        String QCStaatus="";
        switch (status){
            case 1:
                QCStaatus=QCStatus[0];
                break;
            case 3:
                QCStaatus=QCStatus[1];
                break;
            case 4:
                QCStaatus=QCStatus[2];
                break;
        }
        return QCStaatus;
    }

}
