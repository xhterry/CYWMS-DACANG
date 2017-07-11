package com.xx.chinetek.FillPrint;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.Query.Query;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.ReturnMsgModel;
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

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_fill_print)
public class FillPrint extends BaseActivity {

    String TAG_GetStockADF = "Query_GetStockADF";

    private final int RESULT_Msg_GetStockADF=101;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetStockADF:
                AnalysisGetStockADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtLabelScanbarcode);
                break;
        }
    }


   Context context=FillPrint.this;

    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.txt_BatchNo)
    TextView txtBatchNo;
    @ViewInject(R.id.txt_Weight)
    TextView txtWeight;
    @ViewInject(R.id.edt_LabelScanbarcode)
    EditText edtLabelScanbarcode;
    @ViewInject(R.id.btn_labelPrint)
    Button btnlabelPrint;

    BarCodeInfo barCodeInfo=new BarCodeInfo();

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_fillPrint_subtitle), true);
        x.view().inject(this);
    }

    @Event(value = R.id.edt_LabelScanbarcode,type = View.OnKeyListener.class)
    private boolean edtLabelScanbarcodeClick(View v, int keyCode, KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_UP){
            keyBoardCancle();
            String barcode=edtLabelScanbarcode.getText().toString().trim();
            if(!TextUtils.isEmpty(barcode)){
                final Map<String, String> params = new HashMap<String, String>();
                params.put("barcode", barcode);
                params.put("type", "1");
                String para = (new JSONObject(params)).toString();
                LogUtil.WriteLog(Query.class, TAG_GetStockADF, para);
                RequestHandler.addRequestWithDialog(Request.Method.POST,TAG_GetStockADF,String.format(getString(R.string.Msg_QueryStockInfo),BaseApplication.toolBarTitle.Title), context, mHandler, RESULT_Msg_GetStockADF, null,  URLModel.GetURL().GetStockADF, params, null);
            }
        }
        return false;
    }

    @Event(R.id.btn_labelPrint)
    private  void btnlabelPrintClick(View view){

    }

    void AnalysisGetStockADFJson(String result){
        LogUtil.WriteLog(Query.class, TAG_GetStockADF,result);

        ReturnMsgModel<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<BarCodeInfo>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            barCodeInfo=returnMsgModel.getModelJson();
            txtMaterialName.setText(barCodeInfo.getMaterialDesc());
            txtBatchNo.setText(barCodeInfo.getBatchNo());
        }else{
            MessageBox.Show(context,returnMsgModel.getMessage());
        }
    }
}
