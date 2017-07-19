package com.xx.chinetek.cyproduct.Manage;

import android.content.Context;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Production.Manage.LineManageModel;
import com.xx.chinetek.model.Production.Wo.WoDetailModel;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.xx.chinetek.base.BaseApplication.userInfo;

@ContentView(R.layout.activity_product_material_config)
public class ProductMaterialConfig extends BaseActivity {

    String TAG_GetWoDetailModelByWoNo="ProductMaterialConfig_GetWoDetailModelByWoNo";
    private final int RESULT_GetWoDetailModelByWoNo=101;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetWoDetailModelByWoNo:
                AnalysisGetWoDetailModelByWoNoJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }


    Context context=ProductMaterialConfig.this;
    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVoucherNo;
    @ViewInject(R.id.txt_BatchNo)
    TextView txtBatchNo;
    @ViewInject(R.id.txt_ProductLineNo)
    TextView txtProductLineNo;
    @ViewInject(R.id.txt_MaterialDesc)
    TextView txtMaterialDesc;
    @ViewInject(R.id.edt_PrePruductNum)
    EditText edtPrePruductNum;
    @ViewInject(R.id.edt_Barcode)
    EditText edtBarcode;
    @ViewInject(R.id.edt_ScanQty)
    EditText edtScanQty;
    @ViewInject(R.id.lsv_Material)
    ListView lsvMaterial;
    @ViewInject(R.id.btn_StartProduct)
    Button btnStartProduct;

    LineManageModel lineManageModel;
    ArrayList<WoDetailModel> woDetailModels;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_MaterialConfig_subtitle), true);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        this.lineManageModel=getIntent().getParcelableExtra("lineManageModel");
        WoModel woModel=getIntent().getParcelableExtra("woModel");
        if(lineManageModel!=null && woModel!=null){
            txtVoucherNo.setText(woModel.getErpVoucherNo());
            txtBatchNo.setText(woModel.getBatchNo());
            txtProductLineNo.setText(lineManageModel.getProductLineNo());
            txtMaterialDesc.setText(woModel.getMaterialDesc());
            GetWoDetailModelByWoNo(woModel);
        }
    }

    void GetWoDetailModelByWoNo(WoModel woModel){
        try {
            String ModelJson = GsonUtil.parseModelToJson(woModel);
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(ProductMaterialConfig.class, TAG_GetWoDetailModelByWoNo, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetWoDetailModelByWoNo, getString(R.string.Mag_GetWoDetailModelByWoNo), context, mHandler, RESULT_GetWoDetailModelByWoNo, null,  URLModel.GetURL().GetWoDetailModelByWoNo, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void  AnalysisGetWoDetailModelByWoNoJson(String result){
        try {
            LogUtil.WriteLog(ProductMaterialConfig.class, TAG_GetWoDetailModelByWoNo, result);
            ReturnMsgModelList<WoDetailModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<WoDetailModel>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                woDetailModels = returnMsgModel.getModelJson();
                if (woDetailModels != null ){
                    BindListview(woDetailModels);
                }

            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){

            MessageBox.Show(context,ex.getMessage());
        }
    }

    void BindListview(ArrayList<WoDetailModel> woDetailModels){

    }
}
