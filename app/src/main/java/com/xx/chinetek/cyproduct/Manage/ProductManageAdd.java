package com.xx.chinetek.cyproduct.Manage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.product.Manage.UserInfoItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Production.Manage.LineManageModel;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.UserInfo;
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

@ContentView(R.layout.activity_product_manage_add)
public class ProductManageAdd extends BaseActivity {

    String TAG_GetT_UserInfoModel="ProductManageAdd_GetWareHouseByUserADF";
    private final int RESULT_GetT_UserInfoModel=101;

    String TAG_nsert_LineManageInfoModel="ProductManageAdd_Insert_LineManageInfoModel";
    private final int RESULT_Insert_LineManageInfoModel=102;

    String TAG_GetT_IsLINEMANAGEID="ProductManageAdd_IsLINEMANAGEID";
    private final int RESULT_IsLINEMANAGEID=103;


    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_IsLINEMANAGEID:
                Analysis_IsLINEMANAGEIDJson((String) msg.obj);
                break;
            case RESULT_GetT_UserInfoModel:
                AnalysisGetT_UserInfoModelJson((String) msg.obj);
                break;
            case RESULT_Insert_LineManageInfoModel:
                AnalysisInsert_UserInfoModelJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }


   Context context=ProductManageAdd.this;

    @ViewInject(R.id.lsv_Person)
    ListView lsvPerson;
    @ViewInject(R.id.btn_MaterialConfig)
    Button btnMaterialConfig;
    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVoucherNo;
    @ViewInject(R.id.txt_BatchNo)
    TextView txtBatchNo;
    @ViewInject(R.id.edt_EquipID)
    EditText edtEquipID;
    @ViewInject(R.id.edt_ProductLineNo)
    EditText edtProductLineNo;
    @ViewInject(R.id.edt_TeamNo)
    EditText edtTeamNo;
    @ViewInject(R.id.edt_StaffNo)
    EditText edtStaffNo;
    @ViewInject(R.id.edt_Batch)
    EditText edtBatch;


    WoModel woModel;
    LineManageModel lineManageModel;
    UserInfoItemAdapter userInfoItemAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_manage_subtitle), true);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        lineManageModel=new LineManageModel();
        lineManageModel.setUserInfos(new ArrayList<UserInfo>());
        //lineManageModel.getUserInfos().addAll(getdata());
        this.woModel=getIntent().getParcelableExtra("woModel");
        if(woModel!=null) {
            txtVoucherNo.setText(woModel.getErpVoucherNo());
            txtBatchNo.setText(woModel.getBatchNo());
            lineManageModel.setWoBatchNo(woModel.getBatchNo());
            lineManageModel.setWoErpVoucherNo(woModel.getErpVoucherNo());
            lineManageModel.setWoVoucherNo(woModel.getVoucherNo());
        }

    }


    @Event(value = {R.id.edt_EquipID,R.id.edt_ProductLineNo,R.id.edt_TeamNo,R.id.edt_StaffNo,R.id.edt_Batch},type = View.OnKeyListener.class)
    private  boolean onKeyClick(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            EditText tv = (EditText)findViewById(view.getId());
            String code=tv.getText().toString().trim();
            if(TextUtils.isEmpty(code)) {
                MessageBox.Show(context,getString(R.string.Msg_edit_isNotNull));
                CommonUtil.setEditFocus(tv);
                return true;
            }
            switch (view.getId()){
                case R.id.edt_EquipID:
                    lineManageModel.setEquipID(code);
                    break;
                case R.id.edt_Batch:
                    lineManageModel.setWoBatchNo(code);
                    break;
                case R.id.edt_ProductLineNo:
                    lineManageModel.setProductLineNo(code);
                    break;
                case R.id.edt_TeamNo:
                    lineManageModel.setProductTeamNo(code);
                    break;
                case R.id.edt_StaffNo:
                    GetUserInfo(code);
                    break;
            }
        }
        return false;
    }

    @Event(R.id.btn_MaterialConfig)
    private void btnMaterialConfigClick(View view){
        try{
            lineManageModel.setEquipID(edtEquipID.getText().toString());
            lineManageModel.setProductLineNo(edtProductLineNo.getText().toString());
            lineManageModel.setProductTeamNo(edtTeamNo.getText().toString());
            lineManageModel.setWoBatchNo(edtBatch.getText().toString());

            if(lineManageModel.getUserInfos()!=null && lineManageModel.getUserInfos().size()>0
                    && !TextUtils.isEmpty(lineManageModel.getProductLineNo())
                    && !TextUtils.isEmpty(lineManageModel.getProductTeamNo())
                    && !TextUtils.isEmpty(lineManageModel.getWoBatchNo())) {

                InsertUser(lineManageModel);
//            Intent intent = new Intent(context, ProductMaterialConfig.class);
//            Bundle bundle = new Bundle();
//            lineManageModel.setWoModel(woModel);
//            bundle.putParcelable("lineManageModel", lineManageModel);
//            intent.putExtras(bundle);
//            startActivityLeft(intent);
//            closeActiviry();
            }else{
                MessageBox.Show(context,getString(R.string.Msg_edit_isNotNull));
            }
    }catch (Exception ex){
        MessageBox.Show(context,ex.getMessage());
    }
    }


    void IsLINEMANAGEID(String userCode){
        try {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("user", userCode);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_IsLINEMANAGEID, getString(R.string.Msg_GetWareHouse), context, mHandler, RESULT_IsLINEMANAGEID, null,  URLModel.GetURL().IsLINEMANAGEID, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void GetUserInfo(String userCode){
        try {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("UserNo", userCode);
            LogUtil.WriteLog(ProductManageAdd.class, TAG_GetT_UserInfoModel, userCode);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_UserInfoModel, "获取人员信息", context, mHandler, RESULT_GetT_UserInfoModel, null,  URLModel.GetURL().GetWareHouseByUserADF, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

   //插入人员
    void InsertUser(LineManageModel model){
        try {
            final Map<String, String> params = new HashMap<String, String>();
            String userJson=GsonUtil.parseModelToJson(BaseApplication.userInfo);
            String smodel=GsonUtil.parseModelToJson(model);
            params.put("UserJson", userJson);
            params.put("model", smodel);
            LogUtil.WriteLog(ProductManageAdd.class, TAG_nsert_LineManageInfoModel, userJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_nsert_LineManageInfoModel, getString(R.string.Msg_Insert_User), context, mHandler, RESULT_Insert_LineManageInfoModel, null,  URLModel.GetURL().Insert_LineManageInfoModel, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisInsert_UserInfoModelJson(String result){
        try {
            LogUtil.WriteLog(ProductManageAdd.class, TAG_nsert_LineManageInfoModel, result);
            ReturnMsgModel<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<String>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                MessageBox.Show(context,"人员插入成功！");

                Intent intent = new Intent(context, ProductMaterialConfig.class);
                Bundle bundle = new Bundle();
                lineManageModel.setWoModel(woModel);
                bundle.putParcelable("lineManageModel", lineManageModel);
                intent.putExtras(bundle);
                startActivityLeft(intent);
                closeActiviry();

            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
        }

    }

    void Analysis_IsLINEMANAGEIDJson(String result){
        try {
            LogUtil.WriteLog(ProductManageAdd.class, TAG_GetT_IsLINEMANAGEID, result);
            ReturnMsgModel<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<String>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                if(returnMsgModel.getModelJson().equals("1")){
                    //不能插入
                    MessageBox.Show(context,"该人员已经存在其他计划中，不能添加！");
                    if(limuser!=null){
                            if(lineManageModel.getUserInfos()==null)
                                lineManageModel.setUserInfos(new ArrayList<UserInfo>());
                            int index= lineManageModel.getUserInfos().indexOf(limuser);
                            if(index==-1) {}
                            else{
                                lineManageModel.getUserInfos().remove(index);
                                BindListView(lineManageModel.getUserInfos());
                            }
                            BindListView(lineManageModel.getUserInfos());
                        }

                }
            }

        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
        }

    }




    public UserInfo limuser= new UserInfo();
    void AnalysisGetT_UserInfoModelJson(String result){
        try {
            LogUtil.WriteLog(ProductManageAdd.class, TAG_GetT_UserInfoModel, result);
            ReturnMsgModel<UserInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<UserInfo>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                UserInfo userInfo = returnMsgModel.getModelJson();
                limuser=userInfo;
                if (userInfo != null ){
                  if(lineManageModel.getUserInfos()==null)
                      lineManageModel.setUserInfos(new ArrayList<UserInfo>());
                    int index= lineManageModel.getUserInfos().indexOf(userInfo);
                    if(index==-1) {
                        lineManageModel.getUserInfos().add(0, userInfo);
                        IsLINEMANAGEID(userInfo.getUserNo());
                    }
                    else{
                        RemoveUser(index);
                    }
                    BindListView(lineManageModel.getUserInfos());
                }

            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){

            MessageBox.Show(context,ex.getMessage());
        }
        CommonUtil.setEditFocus(edtStaffNo);
    }

    void BindListView(ArrayList<UserInfo> userInfos){
        userInfoItemAdapter=new UserInfoItemAdapter(context,userInfos);
        lsvPerson.setAdapter(userInfoItemAdapter);
    }

    boolean RemoveUser(final  int index){
        new AlertDialog.Builder(context) .setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除该员工？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法
                    lineManageModel.getUserInfos().remove(index);
                        BindListView(lineManageModel.getUserInfos());
                    }
                }).setNegativeButton("取消", null).show();
        return true;
    }

//    ArrayList<UserInfo> getdata(){
//        ArrayList<UserInfo> stockInfoModels=new ArrayList<>();
//        for(int i=0;i<7;i++){
//            UserInfo userInfo=new UserInfo();
//            userInfo.setUserNo(i+"");
//            userInfo.setUserName("姓名12"+i);
//            stockInfoModels.add(userInfo);
//        }
//        return stockInfoModels;
//    }
}
