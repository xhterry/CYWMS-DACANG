package com.xx.chinetek.cywms.Qc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.QC.QcMaterialChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.cywms.Receiption.ReceiptionScan;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.QC.QualityInfo_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.QuanUserModel;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.function.DoubleClickCheck;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xx.chinetek.util.dialog.ToastUtil.show;

@ContentView(R.layout.activity_qcmaterial_choice)
public class QCMaterialChoice extends BaseActivity {

    String TAG_GetT_QualityListADF="QCMaterialChoice_GetT_QualityListADF";
    String TAG_UpadteT_QualityUserADF="QCMaterialChoice_UpadteT_QualityUserADF";
    private final int RESULT_Msg_GetT_QualityListADF=101;
    private final int RESULT_Msg_UpadteT_QualityUserADF=102;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetT_QualityListADF:
                AnalysisGetT_QualityListADFJson((String) msg.obj);
                break;
            case RESULT_Msg_UpadteT_QualityUserADF:
                AnalysisUpadteT_QualityUserADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                show("获取请求失败_____"+ msg.obj);
                break;
        }
    }

    Context context=QCMaterialChoice.this;

    @ViewInject(R.id.lvsQCMaterialChioce)
    ListView lvsQCMaterialChioce;

    ArrayList<QualityInfo_Model> qualityInfoModels;
    QcMaterialChioceItemAdapter qcMaterialChioceItemAdapter;
    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.QC_Material_subtitle), true);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        String ErpVourcherNo=getIntent().getStringExtra("ErpVourcherNo");
        GetT_QualityListADF(ErpVourcherNo);
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
            if (BaseApplication.userInfo.getLstQuanUser() != null){
                final String[] person = new String[BaseApplication.userInfo.getLstQuanUser().size()];
                for (int i=0;i<BaseApplication.userInfo.getLstQuanUser() .size();i++) {
                    person[i]=BaseApplication.userInfo.getLstQuanUser() .get(i).getQuanUserName();
                }
                //选择拣货人员
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("选择取样人员");
                builder.setCancelable(false);
                builder.setItems(person, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                       SumbitQCMaterial(BaseApplication.userInfo.getLstQuanUser().get(which));
                    }
                });
                builder.show();
            }



        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lvsQCMaterialChioce,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        qcMaterialChioceItemAdapter.modifyStates(position);
        qcMaterialChioceItemAdapter.notifyDataSetInvalidated();
    }


    void SumbitQCMaterial(QuanUserModel quanUserModel){
        List<QualityInfo_Model> temp = new ArrayList<>();
        int size = qualityInfoModels.size();
        for (int i = 0; i < size; i++) {
            if (qcMaterialChioceItemAdapter.getStates(i)) {
                qualityInfoModels.get(i).setQuanUserNo(quanUserModel.getQuanUserNo());
                qualityInfoModels.get(i).setStrQuanUserNo(quanUserModel.getQuanUserNo());
                temp.add(0, qualityInfoModels.get(i));
            }
        }
        if (temp.size() != 0) {
            String ModelJson = GsonUtil.parseModelToJson(temp);
            String UserJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
            final Map<String, String> params = new HashMap<String, String>();
            params.put("UserJson", UserJson);
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(ReceiptionScan.class, TAG_UpadteT_QualityUserADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_UpadteT_QualityUserADF, getString(R.string.Msg_UpadteT_QualityUserADF), context, mHandler, RESULT_Msg_UpadteT_QualityUserADF, null, URLModel.GetURL().UpadteT_QualityUserADF, params, null);
        }else{
            MessageBox.Show(context,getString(R.string.Msg_NoSelectmaterialchange));
        }
    }

    void GetT_QualityListADF(String ErpVourcherNo){
        QualityInfo_Model qualityInfoModel=new QualityInfo_Model();
        qualityInfoModel.setVoucherType(20);
        qualityInfoModel.setStockType(10);
        qualityInfoModel.setStatus(1);
        qualityInfoModel.setERPStatus("N");
        qualityInfoModel.setERPStatusCode("N");
        if(!TextUtils.isEmpty(ErpVourcherNo)) {
            qualityInfoModel.setErpVoucherNo(ErpVourcherNo);
            if(!ErpVourcherNo.contains("QC"))
                qualityInfoModel.setErpInVoucherNo(ErpVourcherNo);
        }

        String ModelJson = GsonUtil.parseModelToJson(qualityInfoModel);
        String UserJson=GsonUtil.parseModelToJson(BaseApplication.userInfo);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("UserJson",UserJson );
        params.put("ModelJson", ModelJson);
        LogUtil.WriteLog(ReceiptionScan.class, TAG_GetT_QualityListADF, ModelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_QualityListADF, getString(R.string.Msg_GetT_GetT_QualityListADF), context, mHandler, RESULT_Msg_GetT_QualityListADF, null,  URLModel.GetURL().GetT_QualityListADF, params, null);
    }

    void AnalysisGetT_QualityListADFJson(String result){
        LogUtil.WriteLog(ReceiptionScan.class, TAG_GetT_QualityListADF,result);
        try {
            ReturnMsgModelList<QualityInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<QualityInfo_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                qualityInfoModels= returnMsgModel.getModelJson();
                if (qualityInfoModels != null && qualityInfoModels.size() != 0) {
                    BindListview(qualityInfoModels);
                }else{
                    closeActiviry();
                }
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
    }

    void AnalysisUpadteT_QualityUserADFJson(String result){
        try {
            LogUtil.WriteLog(ReceiptionScan.class, TAG_UpadteT_QualityUserADF,result);
            ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                closeActiviry();
            }else
            {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void  BindListview(ArrayList<QualityInfo_Model> qualityInfoModels){
        qcMaterialChioceItemAdapter=new QcMaterialChioceItemAdapter(context,qualityInfoModels);
        lvsQCMaterialChioce.setAdapter(qcMaterialChioceItemAdapter);
    }
}
