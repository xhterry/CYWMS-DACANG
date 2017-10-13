package com.xx.chinetek.cywms.Intentory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Intentory.InventoryFincItemAdapter;
import com.xx.chinetek.adapter.wms.Intentory.InventoryScanItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
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

@ContentView(R.layout.activity_intentory_detial)
public class IntentoryDetial extends BaseActivity {

    String TAG_GetCheckDetail="IntentoryDetial_GetCheckDetail";
    String TAG_DeleteCheckDetail="IntentoryDetial_DeleteCheckDetail";
    private final int RESULT_Msg_GetCheckDetail=101;
    private final int RESULT_DeleteCheckDetail=102;


    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetCheckDetail:
                AnalysisGetCheckDetailJson((String) msg.obj);
                break;
            case RESULT_DeleteCheckDetail:
                AnalysisDeleteCheckDetailJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }

    String checkno="";
Context context=IntentoryDetial.this;
    @ViewInject(R.id.lsvInventoryDetail)
    ListView lsvInventoryDetail;
    InventoryScanItemAdapter inventoryScanItemAdapter;
    InventoryFincItemAdapter inventoryFincItemAdapter;
    int model=-1;
    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Intentory_detail), true);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        checkno= getIntent().getStringExtra("checkno");
        model=getIntent().getIntExtra("model",-1);
        int id=getIntent().getIntExtra("id",-1);
        InitListview(checkno,id);
    }

    private void InitListview(String checkno,int id) {
        final Map<String, String> params = new HashMap<String, String>();
        if(model==1)
            params.put("checkno", checkno);
        else
            params.put("id", id+"");
        String para = (new JSONObject(params)).toString();
        LogUtil.WriteLog(IntentoryDetial.class, TAG_GetCheckDetail, para);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetCheckDetail,
                getString(R.string.Msg_GetCheckDetail), context, mHandler, RESULT_Msg_GetCheckDetail,
                null,model==1?URLModel.GetURL().GetCheckDetail:URLModel.GetURL().GetMinSerialno, params, null);
    }

    /*
   长按删除物料
    */
    @Event(value = R.id.lsvInventoryDetail,type =  AdapterView.OnItemLongClickListener.class)
    private  boolean lsvInventoryDetailLongClick(AdapterView<?> parent, View view, final int position, long id){
        if(id>=0 && model==1) {
            final Barcode_Model delBarcode=(Barcode_Model)inventoryScanItemAdapter.getItem(position);
            new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除盘点记录？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法
                            final Map<String, String> params = new HashMap<String, String>();
                            String ModelJson = GsonUtil.parseModelToJson(delBarcode);
                            params.put("checkno", checkno);
                            params.put("json", ModelJson);
                            String para = (new JSONObject(params)).toString();
                            LogUtil.WriteLog(IntentoryDetial.class, TAG_DeleteCheckDetail, para);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_DeleteCheckDetail, getString(R.string.Msg_DeleteCheckDetail), context, mHandler, RESULT_DeleteCheckDetail, null,  URLModel.GetURL().DeleteCheckDetail, params, null);

                        }
                    }).setNegativeButton("取消", null).show();
        }
        return true;
    }

    void AnalysisGetCheckDetailJson(String result){
        ArrayList<Barcode_Model> barcodeModels=new ArrayList<>();
        LogUtil.WriteLog(IntentoryDetial.class, TAG_GetCheckDetail,result);
        ReturnMsgModelList<Barcode_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Barcode_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            barcodeModels=returnMsgModel.getModelJson();
        }else{
            MessageBox.Show(context,returnMsgModel.getMessage());
        }
        if(model==1) {
            inventoryScanItemAdapter = new InventoryScanItemAdapter(context, model, barcodeModels);
            lsvInventoryDetail.setAdapter(inventoryScanItemAdapter);
        }else{
            inventoryFincItemAdapter = new InventoryFincItemAdapter(context,barcodeModels);
            lsvInventoryDetail.setAdapter(inventoryFincItemAdapter);
        }
    }

    void  AnalysisDeleteCheckDetailJson(String result){
        LogUtil.WriteLog(IntentoryDetial.class, TAG_DeleteCheckDetail,result);
        ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {}.getType());
        MessageBox.Show(context,returnMsgModel.getMessage());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            InitListview(checkno,-1);
        }
    }
}
