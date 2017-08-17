package com.xx.chinetek.cywms.Query;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Query.QueryItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;
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

@ContentView(R.layout.activity_query)
public class Query extends BaseActivity {

    String TAG_GetStockByMaterialNoADF = "Query_GetStockByMaterialNoADF";

    private final int RESULT_Msg_GetStockADF=101;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetStockADF:
                AnalysisGetStockADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtqueryScanBarcode);
                break;
        }
    }

    Context context=Query.this;
    @ViewInject(R.id.txtname)
    TextView txtname;
    @ViewInject(R.id.lsvQuery)
    ListView lsvQuery;
    @ViewInject(R.id.edt_queryScanBarcode)
    EditText edtqueryScanBarcode;

    QueryItemAdapter queryItemAdapter;
int Type=-1;
    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
        String Context=getIntent().getStringExtra("MaterialNO");
        txtname.setText(BaseApplication.toolBarTitle.Title+"号：");
        Type=getIntent().getIntExtra("Type",-1);
        if(!TextUtils.isEmpty(Context)){
            txtname.setVisibility(View.GONE);
            edtqueryScanBarcode.setVisibility(View.GONE);
            GetStockInfo(Context);
        }
    }


    @Event(value = R.id.edt_queryScanBarcode,type = View.OnKeyListener.class)
    private boolean edtqueryScanBarcodeClick(View v, int keyCode, KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_UP){
            keyBoardCancle();
            String barcode=edtqueryScanBarcode.getText().toString().trim();
            GetStockInfo(barcode);
        }
        return false;
    }

    void GetStockInfo(String barcode){
        if(!TextUtils.isEmpty(barcode)){
            final Map<String, String> params = new HashMap<String, String>();
            params.put("MaterialNo", barcode);
            params.put("ScanType", Type+"");
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(Query.class, TAG_GetStockByMaterialNoADF, para);
            RequestHandler.addRequestWithDialog(Request.Method.POST,TAG_GetStockByMaterialNoADF,String.format(getString(R.string.Msg_QueryStockInfo),BaseApplication.toolBarTitle.Title), context, mHandler, RESULT_Msg_GetStockADF, null,  URLModel.GetURL().GetStockByMaterialNoADF, params, null);
        }
    }

    void AnalysisGetStockADFJson(String result){
        LogUtil.WriteLog(Query.class, TAG_GetStockByMaterialNoADF,result);
        try {
            List<StockInfo_Model> stockInfoModels = new ArrayList<>();
            ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                stockInfoModels = returnMsgModel.getModelJson();
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
            queryItemAdapter = new QueryItemAdapter(context, stockInfoModels);
            lsvQuery.setAdapter(queryItemAdapter);
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());

        }
        CommonUtil.setEditFocus(edtqueryScanBarcode);
    }
}
