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

@ContentView(R.layout.activity_product_manage)
public class ProductManage extends BaseActivity {

    String TAG_GetT_UserInfoModel="ProductManage_GetWareHouseByUserADF";
    private final int RESULT_GetT_UserInfoModel=101;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetT_UserInfoModel:
                AnalysisGetT_UserInfoModelJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }



Context context=ProductManage.this;

    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVoucherNo;
    @ViewInject(R.id.txt_BatchNo)
    TextView txtBatchNo;
    @ViewInject(R.id.txt_EquipID)
    TextView txtEquipID;
    @ViewInject(R.id.txt_ProductLineNo)
    TextView txtProductLineNo;
    @ViewInject(R.id.txt_GroupNo)
    TextView txtGroupNo;
    @ViewInject(R.id.btn_MaterialConfig)
    Button btnMaterialConfig;
    @ViewInject(R.id.btn_ProductComplete)
    Button btnProductComplete;
    @ViewInject(R.id.edt_StaffNo)
    EditText edtStaffNo;
    @ViewInject(R.id.lsvPersonManage)
    ListView lsvPersonManage;

    LineManageModel lineManageModel;
    UserInfoItemAdapter  userInfoItemAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_manage_subtitle), true);
        x.view().inject(this);
        lineManageModel=getIntent().getParcelableExtra("lineManageModel");
        initFrm(lineManageModel);
    }

    @Event(value =R.id.edt_StaffNo,type = View.OnKeyListener.class)
    private  boolean onKeyClick(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String code=edtStaffNo.getText().toString().trim();
            if(TextUtils.isEmpty(code)) {
                MessageBox.Show(context,getString(R.string.Msg_edit_isNotNull));
                CommonUtil.setEditFocus(edtStaffNo);
                return true;
            }
            GetUserInfo(code);
        }
        return false;
    }


    @Event(R.id.btn_ProductComplete)
    private  void btnCompleteClick(View view){
        Intent intent=new Intent(context,ProductComplete.class);
        startActivityLeft(intent);
    }

    @Event(R.id.btn_MaterialConfig)
    private  void btnMaterialConfigClick(View view){
        Intent intent = new Intent(context, ProductMaterialConfig.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("lineManageModel", lineManageModel);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }

    void GetUserInfo(String userCode){
        try {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("UserNo", userCode);
            LogUtil.WriteLog(ProductManage.class, TAG_GetT_UserInfoModel, userCode);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_UserInfoModel, getString(R.string.Msg_GetWareHouse), context, mHandler, RESULT_GetT_UserInfoModel, null,  URLModel.GetURL().GetWareHouseByUserADF, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetT_UserInfoModelJson(String result){
        try {
            LogUtil.WriteLog(ProductManageAdd.class, TAG_GetT_UserInfoModel, result);
            ReturnMsgModel<UserInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<UserInfo>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                UserInfo userInfo = returnMsgModel.getModelJson();
                if (userInfo != null ){
                    if(lineManageModel.getUserInfos()==null)
                        lineManageModel.setUserInfos(new ArrayList<UserInfo>());
                    int index= lineManageModel.getUserInfos().indexOf(userInfo);
                    if(index==-1)
                        lineManageModel.getUserInfos().add(0,userInfo);
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

    void BindListView(ArrayList<UserInfo> userInfos){
        userInfoItemAdapter=new UserInfoItemAdapter(context,userInfos);
        lsvPersonManage.setAdapter(userInfoItemAdapter);
    }


    void initFrm(LineManageModel lineManageModel){
        if(lineManageModel!=null && lineManageModel.getUserInfos()!=null){
            txtBatchNo.setText(lineManageModel.getWoBatchNo());
            txtEquipID.setText(lineManageModel.getEquipID());
            txtGroupNo.setText(lineManageModel.getProductTeamNo());
            txtProductLineNo.setText(lineManageModel.getProductLineNo());
            txtVoucherNo.setText(lineManageModel.getErpVoucherNo());
            BindListView(lineManageModel.getUserInfos());
        }
    }


}
