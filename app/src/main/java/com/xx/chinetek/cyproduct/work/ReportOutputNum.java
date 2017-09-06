package com.xx.chinetek.cyproduct.work;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cyproduct.Billinstock.BillsIn;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Pallet.PalletDetail_Model;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.ArithUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_report_output_num)
public class ReportOutputNum extends BaseActivity {

    Context context=ReportOutputNum.this;

    @ViewInject(R.id.txtNo)
    TextView txtNo;
    @ViewInject(R.id.txtBatch)
    TextView txtBatch;
    @ViewInject(R.id.txtNumber)
    TextView txtNumber;
    @ViewInject(R.id.txtLast)
    TextView txtLast;
    @ViewInject(R.id.editTxtNumber)
    EditText editTxtNumber;

    @ViewInject(R.id.butB)
    Button butB;

    @ViewInject(R.id.butO)
    Button butO;



    WoModel womodel;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        womodel=getIntent().getParcelableExtra("WoModel");

        GetWoModel(womodel);
    }


    @Event(value = {R.id.butB,R.id.butO},type = View.OnClickListener.class)
    private void onClick(View view) {
        if (editTxtNumber.getText().toString().isEmpty()){
            MessageBox.Show(context, "填写信息不能为空！");
            return;
        }
        ArrayList<WoModel> models =new ArrayList<>();
        String Path = "";
        if (R.id.butB == view.getId()) {
            womodel.setReportQty(Float.parseFloat(editTxtNumber.getText().toString()));
            womodel.setUserNo(BaseApplication.userInfo.getUserNo());
            womodel.setVoucherType(36);
            Path = URLModel.GetURL().GetBaoGongByListWoinfo;
        }
        if (R.id.butO == view.getId()) {
            if (womodel.getMaxProductQty()!=null)
            {
                if (Float.parseFloat(editTxtNumber.getText().toString())>womodel.getMaxProductQty())
                {
                    MessageBox.Show(context, "成品包装报工数量不能超过最大限制数量："+ womodel.getMaxProductQty().toString());
                    return;
                }
            }
            womodel.setInQty(Float.parseFloat(editTxtNumber.getText().toString()));
            womodel.setUserNo(BaseApplication.userInfo.getUserNo());
            womodel.setWareHouseNo(BaseApplication.userInfo.getReceiveWareHouseNo());
            womodel.setAreaNo(BaseApplication.userInfo.getReceiveAreaNo());
            womodel.setVoucherType(37);
            Path = URLModel.GetURL().GetFinishInStockByListWoinfo;
        }

        models.add(womodel);
        try {
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("WoInfoJson", GsonUtil.parseModelToJson(models));
//            LogUtil.WriteLog(OffShelfBillChoice.class, TAG_GetT_OutTaskListADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Get_ReportOutPutNum, getString(R.string.Msg_Post), context, mHandler,
                    RESULT_Get_ReportOutPutNum, null,  Path, params, null);
        } catch (Exception ex) {
//                mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }finally {
            models.clear();
        }

    }

    String TAG_Get_ReportOutPutNum = "ReportOutPutNum_Get_ReportOutPutNum";
    private final int RESULT_Get_ReportOutPutNum = 101;

    String TAG_Get_Over = "ReportOutPutNum_Get_Over";
    private final int RESULT_Get_Over = 102;

    String TAG_Get_Barcode = "ReportOutPutNum_Get_Barcode";
    private final int RESULT_Get_Barcode = 103;

    String TAG_Get_Mes = "ReportOutPutNum_Get_Mes";
    private final int RESULT_Get_Mes = 104;

    @Override
    public void onHandleMessage(Message msg) {
//        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_Get_ReportOutPutNum:
                Analysis((String)msg.obj,TAG_Get_ReportOutPutNum);
                break;
            case RESULT_Get_Over:
                Analysis((String)msg.obj,TAG_Get_Over);
                break;
            case RESULT_Get_Mes:
                GetMesAnalysis((String)msg.obj,TAG_Get_Mes);
                break;
            case RESULT_Get_Barcode:
                GetBarcodeAnalysis((String)msg.obj,TAG_Get_Barcode);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
//                CommonUtil.setEditFocus(edt_filterContent);
                break;
        }
    }


    void Analysis(String result,String Tag){
        try {
            LogUtil.WriteLog(ReportOutputNum.class, Tag, result);
            ReturnMsgModelList<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Base_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                MessageBox.Show(context, "提交成功！");

            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void GetMesAnalysis(String result,String Tag){
        try {
            LogUtil.WriteLog(ReportOutputNum.class, Tag, result);
            ReturnMsgModelList<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Base_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                MessageBox.Show(context, "提交成功！");

            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void GetBarcodeAnalysis(String result,String Tag){
        try {
            LogUtil.WriteLog(ReportOutputNum.class, Tag, result);
            ReturnMsgModelList<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Base_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                MessageBox.Show(context, "提交成功！");

            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }


    /*
获取工单信息
 */
    void GetWoModel(WoModel womodel){
        if(womodel!=null) {
            txtNo.setText(womodel.getVoucherNo());
            txtBatch.setText(womodel.getBatchNo());
            txtNumber.setText("111");
            txtLast.setText("222");
        }
    }
}
