package com.xx.chinetek.cyproduct.LineStockIn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.product.LineStockIn.LineStockInMaterialItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.cywms.Receiption.ReceiptionScan;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
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

import static com.xx.chinetek.cywms.R.id.edt_LineInstockScanbarcode;
import static com.xx.chinetek.util.dialog.ToastUtil.show;

@ContentView(R.layout.activity_line_stock_in_material)
public class LineStockInMaterial extends BaseActivity {

    String TAG_GetT_PalletDetailByBarCodeADF="LineStockInMaterial_GetT_PalletDetailByBarCodeADF";

    private final int RESULT_Msg_GetT_PalletDetailByBarCode=102;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetT_PalletDetailByBarCode:
                AnalysisGetT_PalletDetailByNoADF((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtLineInstockScanbarcode);
                break;
        }
    }


    @ViewInject(R.id.lsv_LineStockInMaterial)
    ListView lsvLineStockInMaterial;
    @ViewInject(edt_LineInstockScanbarcode)
    EditText edtLineInstockScanbarcode;
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

    Context context=LineStockInMaterial.this;
    ArrayList<BarCodeInfo> SubmitbarCodeInfos=new ArrayList<>();
    LineStockInMaterialItemAdapter lineStockInMaterialItemAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle=new ToolBarTitle(getString(R.string.LineStockInMaterial),true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Event(value = edt_LineInstockScanbarcode,type = View.OnKeyListener.class)
    private  boolean edtLineInstockScanbarcode(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String code=edtLineInstockScanbarcode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            LogUtil.WriteLog(ReceiptionScan.class, TAG_GetT_PalletDetailByBarCodeADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null,  URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);
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
            if(SubmitbarCodeInfos!=null && SubmitbarCodeInfos.size()!=0){

            }

//                final Map<String, String> params = new HashMap<String, String>();
//                String ModelJson = GsonUtil.parseModelToJson(receiptDetailModels);
//                String UserJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
//                params.put("UserJson", UserJson);
//                params.put("ModelJson", ModelJson);
//                LogUtil.WriteLog(ReceiptionScan.class, TAG_SaveT_InStockDetailADF, ModelJson);
//                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_InStockDetailADF, getString(R.string.Msg_SaveT_InStockDetailADF), context, mHandler, RESULT_Msg_SaveT_InStockDetailADF, null, URLModel.GetURL().SaveT_InStockDetailADF, params, null);
//
        }
        return super.onOptionsItemSelected(item);
    }

    /*
   扫描条码
    */
    void AnalysisGetT_PalletDetailByNoADF(String result){
        LogUtil.WriteLog(ReceiptionScan.class, TAG_GetT_PalletDetailByBarCodeADF,result);
        try {
            ReturnMsgModelList<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<BarCodeInfo>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ArrayList<BarCodeInfo> barCodeInfos = returnMsgModel.getModelJson();
                isDel=false;
                Bindbarcode(barCodeInfos);
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
        CommonUtil.setEditFocus(edtLineInstockScanbarcode);
    }

    boolean isDel=false;
    void Bindbarcode(final ArrayList<BarCodeInfo> barCodeInfos){
        if (barCodeInfos != null && barCodeInfos.size() != 0) {
            try {
                for (BarCodeInfo barCodeInfo : barCodeInfos) {
                    if (barCodeInfo != null && SubmitbarCodeInfos != null) {
                        final int barIndex = SubmitbarCodeInfos.indexOf(barCodeInfo);
                        if (barIndex != -1) {
                            if (isDel) {
                                RemoveBarcode(barIndex);
                            } else {
                                new AlertDialog.Builder(context).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除已扫描条码？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // TODO 自动生成的方法
                                                //RemoveBarcode(index, barIndex);
                                                isDel = true;
                                                Bindbarcode(barCodeInfos);
                                            }
                                        }).setNegativeButton("取消", null).show();
                                break;
                            }
                        } else {
                            if (!CheckBarcode(barCodeInfo))
                                break;
                        }
                    }

                }
                InitFrm(barCodeInfos.get(0));
            }catch (Exception ex){
                MessageBox.Show(context,ex.getMessage());
                CommonUtil.setEditFocus(edtLineInstockScanbarcode);
            }

        }
    }

    boolean RemoveBarcode(final int barIndex){
        SubmitbarCodeInfos.remove(barIndex);
        return true;
    }

    boolean CheckBarcode(BarCodeInfo barCodeInfo) {
        SubmitbarCodeInfos.add(0, barCodeInfo);
        return true;
    }

    private void BindListVIew(ArrayList<BarCodeInfo> barCodeInfos) {
        lineStockInMaterialItemAdapter=new LineStockInMaterialItemAdapter(context,barCodeInfos);
        lsvLineStockInMaterial.setAdapter(lineStockInMaterialItemAdapter);
    }

    void InitFrm(BarCodeInfo barCodeInfo){
        if(barCodeInfo!=null ){
            txtCompany.setText(barCodeInfo.getStrongHoldName());
            txtBatch.setText(barCodeInfo.getBatchNo());
            txtStatus.setText(barCodeInfo.getStrStatus());
            txtMaterialName.setText(barCodeInfo.getMaterialDesc());
            txtEDate.setText(CommonUtil.DateToString(barCodeInfo.getEDate()));
        }
        if(SubmitbarCodeInfos!=null)
            BindListVIew(SubmitbarCodeInfos);
        CommonUtil.setEditFocus(edtLineInstockScanbarcode);
    }
}
