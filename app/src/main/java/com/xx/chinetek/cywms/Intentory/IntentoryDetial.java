package com.xx.chinetek.cywms.Intentory;

import android.content.Context;
import android.os.Message;
import android.widget.ListView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Intentory.InventoryScanItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Inventory.Barcode_Model;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_intentory_detial)
public class IntentoryDetial extends BaseActivity {

    String TAG_GetCheckDetail="IntentoryDetial_GetCheckDetail";
    private final int RESULT_Msg_GetCheckDetail=101;


    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetCheckDetail:
                AnalysisGetCheckDetailJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }


Context context=IntentoryDetial.this;
    @ViewInject(R.id.lsvInventoryDetail)
    ListView lsvInventoryDetail;
    InventoryScanItemAdapter inventoryScanItemAdapter;

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
        String checkno= getIntent().getStringExtra("checkno");
        InitListview(checkno);
    }

    private void InitListview(String checkno) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("checkno", checkno);
        String para = (new JSONObject(params)).toString();
        LogUtil.WriteLog(IntentoryDetial.class, TAG_GetCheckDetail, para);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetCheckDetail, getString(R.string.Msg_GetCheckDetail), context, mHandler, RESULT_Msg_GetCheckDetail, null,  URLModel.GetURL().GetCheckDetail, params, null);
    }

    void AnalysisGetCheckDetailJson(String result){
        LogUtil.WriteLog(IntentoryDetial.class, TAG_GetCheckDetail,result);
        ReturnMsgModelList<Barcode_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Barcode_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
             ArrayList<Barcode_Model> barcodeModels=returnMsgModel.getModelJson();
            inventoryScanItemAdapter=new InventoryScanItemAdapter(context,barcodeModels);
            lsvInventoryDetail.setAdapter(inventoryScanItemAdapter);
        }else{
            MessageBox.Show(context,returnMsgModel.getMessage());
        }
    }

}
