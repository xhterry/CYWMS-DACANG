package com.xx.chinetek.cywms.Intentory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Intentory.InventoryAddItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.WareHouseInfo;
import com.xx.chinetek.model.WMS.Inventory.CheckArea_Model;
import com.xx.chinetek.model.WMS.Inventory.Check_Model;
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

@ContentView(R.layout.activity_intentory_add)
public class IntentoryAdd extends BaseActivity {

    String TAG_GetPDNoAndroid = "IntentoryAdd_GetPDNoAndroid";
    String TAG_GetAreanoID = "IntentoryAdd_GetAreanoID";
    String TAG_GetWareHouse = "IntentoryAdd_GetWareHouse";
    String TAG_SaveCheckAndroid = "IntentoryAdd_SaveCheckAndroid";
    private final int RESULT_GetPDNoAndroid = 101;
    private final int RESULT_GetAreanoID = 102;
    private final int RESULT_SaveCheckAndroid = 103;
    private final int RESULT_GetWareHouse = 104;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetPDNoAndroid:
                AnalysisGetPDNoAndroidJson((String) msg.obj);
                break;
            case RESULT_GetAreanoID:
                AnalysisGetAreanoIDJson((String) msg.obj);
                break;
            case RESULT_SaveCheckAndroid:
                AnalysisSaveCheckAndroidJson((String) msg.obj);
                break;
            case RESULT_GetWareHouse:
                AnalysisGetWareHouseJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }


    Context context = IntentoryAdd.this;
    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVoucherNo;
    @ViewInject(R.id.txt_SelectWohouse)
    TextView txtSelectWohouse;
    @ViewInject(R.id.edt_InventoryDesc)
    EditText edtInventoryDesc;
    @ViewInject(R.id.edt_InventoryRemark)
    EditText edtInventoryRemark;
    @ViewInject(R.id.edt_InventoryAreaNo)
    EditText edtInventoryAreaNo;
    @ViewInject(R.id.lsv_InventoryAdd)
    ListView lsvInventoryAdd;

    ArrayList<CheckArea_Model> checkAreaModels;
    InventoryAddItemAdapter inventoryAddItemAdapter;
    String WareHouseNo="";

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Intentory_add), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Override
    protected void initData() {
        super.initData();
        GetPDNoAndroid();
        checkAreaModels=new ArrayList<>();
        CommonUtil.setEditFocus(edtInventoryAreaNo);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
          if(checkAreaModels!=null && checkAreaModels.size()!=0){
              Check_Model checkModel=new Check_Model();
              checkModel.setCHECKNO(txtVoucherNo.getText().toString());
              checkModel.setCHECKDESC(edtInventoryDesc.getText().toString().trim());
              checkModel.setREMARKS(edtInventoryRemark.getText().toString().trim());
              checkModel.setCREATER(BaseApplication.userInfo.getUserName());
              try {
                  String json1= GsonUtil.parseModelToJson(checkModel);
                  String json2= GsonUtil.parseModelToJson(checkAreaModels);
                  Map<String, String> params = new HashMap<>();
                  params.put("json1",json1);
                  params.put("json2",json2);
                  LogUtil.WriteLog(IntentoryAdd.class, TAG_SaveCheckAndroid, json1+"|"+json2);
                  RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveCheckAndroid, getString(R.string.Msg_InsertCheckDetail), context, mHandler, RESULT_SaveCheckAndroid, null,  URLModel.GetURL().SaveCheckAndroid, params, null);
              } catch (Exception ex) {
                  MessageBox.Show(context, ex.getMessage());
              }
          }
        }
        return super.onOptionsItemSelected(item);
    }

    @Event(value =R.id.edt_InventoryAreaNo,type = View.OnKeyListener.class)
    private  boolean edtInventoryAreaNoClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String areaNo=edtInventoryAreaNo.getText().toString().trim();
            if(!TextUtils.isEmpty(areaNo)&& !TextUtils.isEmpty(WareHouseNo)){
                try {
                    CheckArea_Model checkAreaModel = new CheckArea_Model();
                    checkAreaModel.setAREANO(areaNo);
                    checkAreaModel.setWarehouseno(WareHouseNo);
                    if (checkAreaModels.indexOf(checkAreaModel) == -1) {
                        String json = GsonUtil.parseModelToJson(checkAreaModel);
                        Map<String, String> params = new HashMap<>();
                        params.put("json", json);
                        LogUtil.WriteLog(IntentoryAdd.class, TAG_GetPDNoAndroid, json);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreanoID, getString(R.string.Msg_GetAreanobyCheckno), context, mHandler, RESULT_GetAreanoID, null, URLModel.GetURL().GetAreanoID, params, null);
                    }else{
                        MessageBox.Show(context,getString(R.string.Error_areanoHasScan));
                        CommonUtil.setEditFocus(edtInventoryAreaNo);
                        return true;
                    }
                } catch (Exception ex) {
                    MessageBox.Show(context, ex.getMessage());
                    CommonUtil.setEditFocus(edtInventoryAreaNo);
                    return true;
                }
            }
        }
        CommonUtil.setEditFocus(edtInventoryAreaNo);
        return false;
    }

    @Event(value = R.id.txt_SelectWohouse,type =View.OnClickListener.class )
    private void txtSelectWohouseClick(View view){
        LogUtil.WriteLog(IntentoryAdd.class, TAG_GetWareHouse,"");
       final Map<String, String> params = new HashMap<>();
        try {
            if(checkAreaModels!=null && checkAreaModels.size()==0){
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetWareHouse, getString(R.string.Msg_GetWareHouse), context, mHandler, RESULT_GetWareHouse, null,  URLModel.GetURL().GetWareHouse, params, null);
            }else{
                new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("已存在盘点库位信息，无法更改仓库！")
                        .setPositiveButton("确定", null).show();
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        CommonUtil.setEditFocus(edtInventoryAreaNo);
    }

    void GetPDNoAndroid(){
        try {
            Map<String, String> params = new HashMap<>();
            LogUtil.WriteLog(IntentoryAdd.class, TAG_GetPDNoAndroid, "");
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetPDNoAndroid, getString(R.string.Msg_Inventory_Load), context, mHandler, RESULT_GetPDNoAndroid, null,  URLModel.GetURL().GetPDNoAndroid, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }


    void AnalysisGetPDNoAndroidJson(String result){
        LogUtil.WriteLog(IntentoryAdd.class, TAG_GetPDNoAndroid,result);
        ReturnMsgModel<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<String>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            txtVoucherNo.setText(returnMsgModel.getModelJson());
        }else
        {
            MessageBox.Show(context,returnMsgModel.getMessage());
        }
    }

    void AnalysisGetAreanoIDJson(String result){
        LogUtil.WriteLog(IntentoryAdd.class, TAG_GetAreanoID,result);
        ReturnMsgModel<CheckArea_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<CheckArea_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
           CheckArea_Model checkAreaModel=returnMsgModel.getModelJson();
            if(checkAreaModel!=null) {
                checkAreaModel.setADDRESS(txtSelectWohouse.getText().toString());
                checkAreaModels.add(0, checkAreaModel);
                inventoryAddItemAdapter=new InventoryAddItemAdapter(context,checkAreaModels);
                lsvInventoryAdd.setAdapter(inventoryAddItemAdapter);
            }
        }else
        {
            MessageBox.Show(context,returnMsgModel.getMessage());
        }
        CommonUtil.setEditFocus(edtInventoryAreaNo);
    }

    void AnalysisSaveCheckAndroidJson(String result){
        try {
            LogUtil.WriteLog(IntentoryAdd.class, TAG_SaveCheckAndroid,result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
               closeActiviry();
            }else
                MessageBox.Show(context, returnMsgModel.getMessage());
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetWareHouseJson(String result){
        try {
            LogUtil.WriteLog(IntentoryAdd.class, TAG_GetWareHouse,result);
            ReturnMsgModelList<WareHouseInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<WareHouseInfo>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
               final ArrayList<WareHouseInfo> wareHouseInfos=returnMsgModel.getModelJson();
                int size=wareHouseInfos.size();
                String[] wareHouseInfo=new String[size];
                for(int i=0;i<size;i++){
                    wareHouseInfo[i]=wareHouseInfos.get(i).getWareHouseNo()+wareHouseInfos.get(i).getWareHouseName();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("选择盘点所属仓库");
                builder.setCancelable(false);
                builder.setItems(wareHouseInfo, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        final int  selectID=which;
                        new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否选择当前仓库建立盘点单？\n【"+wareHouseInfos.get(which).getWareHouseName()+"】")
                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        WareHouseNo=wareHouseInfos.get(selectID).getWareHouseNo();
                                        txtSelectWohouse.setText(wareHouseInfos.get(selectID).getWareHouseName());
                                        // TODO 自动生成的方法
                                    }
                                }).setNegativeButton("否", null).show();
                    }
                });
                builder.show();
                CommonUtil.setEditFocus(edtInventoryAreaNo);
            }else
                MessageBox.Show(context, returnMsgModel.getMessage());
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

}
