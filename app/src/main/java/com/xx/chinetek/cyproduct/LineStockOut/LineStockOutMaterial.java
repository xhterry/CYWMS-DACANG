package com.xx.chinetek.cyproduct.LineStockOut;

import android.content.Context;
import android.os.Message;
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
import com.xx.chinetek.adapter.product.Manage.WoDetailMaterialItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cyproduct.Manage.ProductMaterialConfig;
import com.xx.chinetek.cywms.R;
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
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.xx.chinetek.cywms.R.id.tb_UnboxType;

@ContentView(R.layout.activity_line_stock_out_material)
public class LineStockOutMaterial extends BaseActivity {

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

    @ViewInject(R.id.lsv_LineStockOutMaterial)
    ListView lsv_LineStockOutMaterial;
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
    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVoucherNo;
    @ViewInject(tb_UnboxType)
    ToggleButton tbUnboxType;
    @ViewInject(R.id.tb_PalletType)
    ToggleButton tbPalletType;
    @ViewInject(R.id.tb_BoxType)
    ToggleButton tbBoxType;
    @ViewInject(R.id.edt_LineStockOutScanBarcode)
    EditText edtOffShelfScanbarcode;
    @ViewInject(R.id.edt_Unboxing)
    EditText edtUnboxing;
    @ViewInject(R.id.txt_Unboxing)
    TextView txtUnboxing;

    Context context=LineStockOutMaterial.this;
    ArrayList<WoDetailModel> woDetailModels;
    WoDetailMaterialItemAdapter woDetailMaterialItemAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        WoModel woModel=getIntent().getParcelableExtra("woModel");
        GetWoDetailModelByWoNo(woModel);
    }

    @Event(value ={R.id.tb_UnboxType,R.id.tb_PalletType,R.id.tb_BoxType} ,type = CompoundButton.OnClickListener.class)
    private void TBonCheckedChanged(View view) {
        tbUnboxType.setChecked(view.getId()== R.id.tb_UnboxType);
        tbPalletType.setChecked(view.getId()== R.id.tb_PalletType);
        tbBoxType.setChecked(view.getId()== R.id.tb_BoxType);
        ShowUnboxing(view.getId()== R.id.tb_UnboxType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {

        }
        return super.onOptionsItemSelected(item);
    }

    void GetWoDetailModelByWoNo(WoModel woModel){
        if(woModel!=null) {
            txtVoucherNo.setText(woModel.getErpVoucherNo());
            try {
                Map<String, String> params = new HashMap<>();
                params.put("HeadId", woModel.getID() + "");
                LogUtil.WriteLog(ProductMaterialConfig.class, TAG_GetWoDetailModelByWoNo, woModel.getID() + "");
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetWoDetailModelByWoNo, getString(R.string.Mag_GetWoDetailModelByWoNo), context, mHandler, RESULT_GetWoDetailModelByWoNo, null, URLModel.GetURL().GetWoDetailModelByWoNo, params, null);
            } catch (Exception ex) {
                MessageBox.Show(context, ex.getMessage());
            }
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
        woDetailMaterialItemAdapter=new WoDetailMaterialItemAdapter(context,woDetailModels);
        lsv_LineStockOutMaterial.setAdapter(woDetailMaterialItemAdapter);
    }

    void ShowUnboxing(Boolean show){
        int visiable=show? View.VISIBLE:View.GONE;
        txtUnboxing.setVisibility(visiable);
        edtUnboxing.setVisibility(visiable);
    }

}
