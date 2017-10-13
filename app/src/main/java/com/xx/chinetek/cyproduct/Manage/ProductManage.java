package com.xx.chinetek.cyproduct.Manage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.product.BillsStockIn.WoBatchAlldapter;
import com.xx.chinetek.adapter.product.Manage.UserInfoItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cyproduct.Billinstock.BillsIn;
import com.xx.chinetek.cyproduct.LineStockIn.LineStockInBackProduct;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Production.Manage.LineManageModel;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.UerInfo;
import com.xx.chinetek.model.User.UserInfo;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.CommonUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.common.task.AbsTask;
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

    String TAG_UpdateLineManageUser="ProductManage_UpdateLineManageUserADF";
    private final int RESULT_UpdateLineManageUser=102;

    String TAG_StartWork="ProductManage_StartWork";
    private final int RESULT_StartWork=103;

    String TAG_OverWork="ProductManage_OverWork";
    private final int RESULT_OverWork=104;

    String TAG_SuspendOrReWork="ProductManage_SuspendOrReWork";
    private final int RESULT_SuspendOrReWork=105;

    String TAG_GetLinemanagestate="ProductManage_GetLinemanagestate";
    private final int RESULT_GetLinemanagestate=106;

    String TAG_BaogongADF="ProductManage_TAG_Baogong";
    private final int RESULT_BaogongADF = 107;



    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetT_UserInfoModel:
                AnalysisGetT_UserInfoModelJson((String) msg.obj);
                break;
            case RESULT_UpdateLineManageUser:
                UpdateLineManageUserJson((String) msg.obj);
                break;
            case RESULT_StartWork:
                AnalysisStartWorkADFJson((String) msg.obj);
                break;
            case RESULT_OverWork:
                AnalysisStartWorkADFJson((String) msg.obj);
                break;
            case RESULT_SuspendOrReWork:
                AnalysisStartWorkADFJson((String) msg.obj);
                break;
            case RESULT_GetLinemanagestate:
                AnalysisGetLinemanagestateADFJson((String) msg.obj);
                break;
            case RESULT_BaogongADF:
                AnalysisBAOGONGADFJson((String) msg.obj);
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

    @ViewInject(R.id.txtbaogong)
    EditText txtbaogong;
    @ViewInject(R.id.textView135)
    TextView textView135;

//    @ViewInject(R.id.btn_MaterialConfig)
//    Button btnMaterialConfig;
//    @ViewInject(R.id.btn_ProductComplete)
//    Button btnProductComplete;
    @ViewInject(R.id.buttonov)
    Button buttonov;
    @ViewInject(R.id.butopen)
    Button butopen;
    @ViewInject(R.id.buttonovre)
    Button buttonovre;
    @ViewInject(R.id.buttonstop)
    Button buttonstop;
    @ViewInject(R.id.buttonBao)
    Button buttonBao;

    @ViewInject(R.id.edt_StaffNo)
    EditText edtStaffNo;
    @ViewInject(R.id.lsvPersonManage)
    ListView lsvPersonManage;

    LineManageModel lineManageModel;
    UserInfoItemAdapter  userInfoItemAdapter;

    @Override
    protected void initViews() {
        try{
            super.initViews();
            BaseApplication.context = context;
            BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_manage_subtitle), true);
            x.view().inject(this);
            buttonBao.setVisibility(View.GONE);
            butopen.setVisibility(View.GONE);
            buttonov.setVisibility(View.GONE);
            buttonovre.setVisibility(View.GONE);
            buttonstop.setVisibility(View.GONE);
            txtbaogong.setVisibility(View.GONE);
            textView135.setVisibility(View.GONE);
            lineManageModel=getIntent().getParcelableExtra("lineManageModel");
            initFrm(lineManageModel);
            Getstate();
        }catch(Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }

    }


    void Getstate(){
        final Map<String, String> params = new HashMap<String, String>();
        params.put("modelid", String.valueOf(lineManageModel.getID()));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetLinemanagestate, "获取计划状态", context, mHandler, RESULT_GetLinemanagestate, null,  URLModel.GetURL().GetLinemanagestate, params, null);

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
            if((FlagBut.equals("")&&state.equals("0"))||(FlagBut.equals("")&&state.equals("5"))){
                GetUserInfo(code);
            }else{
                if(FlagBut.equals("buttonov")){
                    GetUserInfo(code);
                }else{
                    MessageBox.Show(context,"生产计划在创建和暂停状态才能删除添加人员！");
                }
            }

        }
        return false;
    }


//    @Event(R.id.btn_ProductComplete)
//    private  void btnCompleteClick(View view){
//        Intent intent=new Intent(context,ProductComplete.class);
//        startActivityLeft(intent);
//    }

    private String FlagBut="";
    @Event(value = {R.id.btn_MaterialConfig,R.id.butopen,R.id.buttonstop,R.id.buttonov,R.id.buttonovre,R.id.buttonBao},type = View.OnClickListener.class)
    private  void btnClick(View view){
        try{
            if (R.id.buttonBao == view.getId()) {
                //报工
                if(txtbaogong.getText().toString().equals("")||(!CommonUtil.isFloat(txtbaogong.getText().toString()))){
                    MessageBox.Show(context,"报工数格式不正确！");
                    return;
                }

                WoModel womodel =  lineManageModel.getWoModel();
                womodel.setUserNo(BaseApplication.userInfo.getUserNo());
                womodel.setVoucherType(36);
                womodel.setUnitName(String.valueOf(lineManageModel.getID()));
                womodel.setReportQty(Float.parseFloat(txtbaogong.getText().toString()));
                ArrayList<WoModel> womodelssa = new ArrayList<>();
                womodelssa.add(womodel);
                UerInfo User= BaseApplication.userInfo;
                if(String.valueOf(womodel.getStrongHoldCode()).equals("CY1")){
                    womodel.setUserNo(User.getUserNo()+"A");
//                        User.setUserNo(User.getUserNo()+"A");
                }
                if(String.valueOf(womodel.getStrongHoldCode()).equals("CX1")){
                    womodel.setUserNo(User.getUserNo()+"B");
//                        User.setUserNo(User.getUserNo()+"B");
                }
                if(String.valueOf(womodel.getStrongHoldCode()).equals("FC1")){
                    womodel.setUserNo(User.getUserNo()+"C");
//                        User.setUserNo(User.getUserNo()+"C");
                }
                String userJsona = GsonUtil.parseModelToJson(User);
                String modelJsona = GsonUtil.parseModelToJson(womodelssa);
                final Map<String, String> paramsa = new HashMap<String, String>();
                paramsa.put("UserJson", userJsona);
                paramsa.put("WoInfoJson", modelJsona);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_BaogongADF,  "正在报工中", context, mHandler, RESULT_BaogongADF, null, URLModel.GetURL().GetBaoGongByListWoinfo, paramsa, null);

            }
            if (R.id.butopen == view.getId()) {
                FlagBut="butopen";
                //开始
                final Map<String, String> params = new HashMap<String, String>();
                params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
                params.put("modelid", String.valueOf(lineManageModel.getID()));
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_StartWork, "开工", context, mHandler, RESULT_StartWork, null,  URLModel.GetURL().StartWork, params, null);
            }
            if (R.id.buttonstop == view.getId()) {
                if(txtbaogong.getText().toString().equals("")||(!CommonUtil.isFloat(txtbaogong.getText().toString()))){
                    MessageBox.Show(context,"报工数格式不正确！");
                    return;
                }
                FlagBut="buttonstop";

                //结束
                final Map<String, String> params = new HashMap<String, String>();
                params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
                params.put("modelid", String.valueOf(lineManageModel.getID()));
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_OverWork, "完工", context, mHandler, RESULT_OverWork, null,  URLModel.GetURL().OverWork, params, null);

            }
            if (R.id.buttonov == view.getId()) {
                FlagBut="buttonov";
                //暂停
                final Map<String, String> params = new HashMap<String, String>();
                params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
                params.put("modelid", String.valueOf(lineManageModel.getID()));
                params.put("Flag", "1");
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SuspendOrReWork, "暂停", context, mHandler, RESULT_SuspendOrReWork, null,  URLModel.GetURL().SuspendOrReWork, params, null);

            }
            if (R.id.buttonovre == view.getId()) {
                FlagBut="buttonovre";
                //重新开始
                final Map<String, String> params = new HashMap<String, String>();
                params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
                params.put("modelid", String.valueOf(lineManageModel.getID()));
                params.put("Flag", "2");
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SuspendOrReWork, "重新开始", context, mHandler, RESULT_SuspendOrReWork, null,  URLModel.GetURL().SuspendOrReWork, params, null);

            }
            if (R.id.btn_MaterialConfig == view.getId()) {
                Intent intent = new Intent(context, ProductMaterialConfig.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("lineManageModel", lineManageModel);
                intent.putExtras(bundle);
                startActivityLeft(intent);
            }

        }catch(Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }

    }

    void GetUserInfo(String userCode){
        try {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("UserNo", userCode);
            LogUtil.WriteLog(ProductManage.class, TAG_GetT_UserInfoModel, userCode);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_UserInfoModel, "获取人员信息", context, mHandler, RESULT_GetT_UserInfoModel, null,  URLModel.GetURL().GetWareHouseByUserADF, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
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
                    if(index==-1){
                        //添加用户人员
                        lineManageModel.getUserInfos().add(0,userInfo);

                        final Map<String, String> params = new HashMap<String, String>();
                        params.put("model", GsonUtil.parseModelToJson(lineManageModel));
                        params.put("userno", userInfo.getUserNo());
                        params.put("flag", "1");
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_UpdateLineManageUser, "添加人员中", context, mHandler, RESULT_UpdateLineManageUser, null,  URLModel.GetURL().UpdateLineManageUser, params, null);

                    }else{
                        //删除用户人员
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


    private String state="";//放计划状态

//0：创建 1：生产中 2：已经结束 3:不存在 4：异常计划 5: 暂停 6：非暂停
    void AnalysisGetLinemanagestateADFJson(String result){
        try {
            ReturnMsgModel<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<String>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
               state =  returnMsgModel.getModelJson();
                if(state.equals("0")){butopen.setVisibility(View.VISIBLE);}
                if(state.equals("6")){buttonov.setVisibility(View.VISIBLE);buttonstop.setVisibility(View.VISIBLE);txtbaogong.setVisibility(View.VISIBLE);textView135.setVisibility(View.VISIBLE);}
//                if(state.equals("5")){buttonovre.setVisibility(View.VISIBLE);buttonBao.setVisibility(View.VISIBLE);buttonstop.setVisibility(View.VISIBLE);txtbaogong.setVisibility(View.VISIBLE);textView135.setVisibility(View.VISIBLE); }
                if(state.equals("5")){buttonovre.setVisibility(View.VISIBLE);buttonstop.setVisibility(View.VISIBLE); }

                if(state.equals("2")){buttonBao.setVisibility(View.VISIBLE);txtbaogong.setVisibility(View.VISIBLE);textView135.setVisibility(View.VISIBLE);}

            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisStartWorkADFJson(String result){
        try {
            ReturnMsgModel<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<String>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                if(FlagBut.equals("butopen")){butopen.setVisibility(View.GONE);buttonov.setVisibility(View.VISIBLE);buttonstop.setVisibility(View.VISIBLE);txtbaogong.setVisibility(View.VISIBLE);textView135.setVisibility(View.VISIBLE);}
//                if(FlagBut.equals("buttonov")){buttonov.setVisibility(View.GONE);buttonstop.setVisibility(View.GONE);buttonovre.setVisibility(View.VISIBLE);txtbaogong.setVisibility(View.VISIBLE);textView135.setVisibility(View.VISIBLE);buttonBao.setVisibility(View.VISIBLE);}
//                if(FlagBut.equals("buttonovre")){buttonovre.setVisibility(View.GONE);buttonstop.setVisibility(View.VISIBLE);buttonov.setVisibility(View.VISIBLE);txtbaogong.setVisibility(View.VISIBLE);textView135.setVisibility(View.VISIBLE);buttonBao.setVisibility(View.GONE);}
                if(FlagBut.equals("buttonov")){buttonov.setVisibility(View.GONE);buttonstop.setVisibility(View.GONE);buttonovre.setVisibility(View.VISIBLE);}
                if(FlagBut.equals("buttonovre")){buttonovre.setVisibility(View.GONE);buttonstop.setVisibility(View.VISIBLE);buttonov.setVisibility(View.VISIBLE);}
                if(FlagBut.equals("buttonstop")){
                    buttonstop.setVisibility(View.GONE);
                    buttonov.setVisibility(View.GONE);
                    buttonBao.setVisibility(View.GONE);
                    txtbaogong.setVisibility(View.GONE);textView135.setVisibility(View.GONE);

                    WoModel womodel =  lineManageModel.getWoModel();
                    womodel.setUserNo(BaseApplication.userInfo.getUserNo());
                    womodel.setVoucherType(36);
                    womodel.setUnitName(String.valueOf(lineManageModel.getID()));
                    womodel.setReportQty(Float.parseFloat(txtbaogong.getText().toString()));
                    ArrayList<WoModel> womodelssa = new ArrayList<>();
                    womodelssa.add(womodel);
                    UerInfo User= BaseApplication.userInfo;
                    if(String.valueOf(womodel.getStrongHoldCode()).equals("CY1")){
                        womodel.setUserNo(User.getUserNo()+"A");
//                        User.setUserNo(User.getUserNo()+"A");
                    }
                    if(String.valueOf(womodel.getStrongHoldCode()).equals("CX1")){
                        womodel.setUserNo(User.getUserNo()+"B");
//                        User.setUserNo(User.getUserNo()+"B");
                    }
                    if(String.valueOf(womodel.getStrongHoldCode()).equals("FC1")){
                        womodel.setUserNo(User.getUserNo()+"C");
//                        User.setUserNo(User.getUserNo()+"C");
                    }
                    String userJsona = GsonUtil.parseModelToJson(User);
                    String modelJsona = GsonUtil.parseModelToJson(womodelssa);
                    final Map<String, String> paramsa = new HashMap<String, String>();
                    paramsa.put("UserJson", userJsona);
                    paramsa.put("WoInfoJson", modelJsona);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_BaogongADF,  "正在报工中", context, mHandler, RESULT_BaogongADF, null, URLModel.GetURL().GetBaoGongByListWoinfo, paramsa, null);

                }
                MessageBox.Show(context,"操作成功！");
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }


    void UpdateLineManageUserJson(String result){
        try {
            ReturnMsgModel<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<String>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
            } else {
                MessageBox.Show(context, "该人员已经存在其他生产计划中，不能添加！");

                if (limuser != null ){
                    if(lineManageModel.getUserInfos()==null)
                        lineManageModel.setUserInfos(new ArrayList<UserInfo>());
                    int index= lineManageModel.getUserInfos().indexOf(limuser);
                    if(index==-1){
                    }else{
                        lineManageModel.getUserInfos().remove(index);
                        BindListView(lineManageModel.getUserInfos());
                    }
                }

            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }

    /*
  报功返回信息
   */
    void AnalysisBAOGONGADFJson(String result){
        try {
            ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                MessageBox.Show(context, returnMsgModel.getMessage());
                buttonBao.setVisibility(View.GONE);
//                //报功
//                WoModel womodel =  lineManageModel.getWoModel();
//                womodel.setReportQty(Float.parseFloat("1"));
//                womodel.setUserNo(BaseApplication.userInfo.getUserNo());
//                womodel.setVoucherType(36);
//                womodel.setUnitName(String.valueOf(lineManageModel.getID()));
//                womodel.setReportQty(Float.parseFloat(txtbaogong.getText().toString()));
//                ArrayList<WoModel> womodelssa = new ArrayList<>();
//                womodelssa.add(womodel);
//                String userJsona = GsonUtil.parseModelToJson(BaseApplication.userInfo);
//                String modelJsona = GsonUtil.parseModelToJson(womodelssa);
//                final Map<String, String> paramsa = new HashMap<String, String>();
//                paramsa.put("UserJson", userJsona);
//                paramsa.put("WoInfoJson", modelJsona);
//                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_BaogongADF,  "正在报工中", context, mHandler, RESULT_BaogongADF, null, URLModel.GetURL().GetBaoGongByListWoinfo, paramsa, null);


            }else{
                MessageBox.Show(context, returnMsgModel.getMessage());
            }

        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }


    boolean RemoveUser(final  int index){
        new AlertDialog.Builder(context) .setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除该员工？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法
                        final Map<String, String> params = new HashMap<String, String>();
                        params.put("model", GsonUtil.parseModelToJson(lineManageModel));
                        params.put("userno", lineManageModel.getUserInfos().get(index).getUserNo());
                        params.put("flag", "2");
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_UpdateLineManageUser, "删除人员中", context, mHandler, RESULT_UpdateLineManageUser, null,  URLModel.GetURL().UpdateLineManageUser, params, null);

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
            txtVoucherNo.setText(lineManageModel.getWoErpVoucherNo());
            BindListView(lineManageModel.getUserInfos());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            Intent intent = new Intent(context, ProductMaterialConfig.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("lineManageModel", lineManageModel);
            intent.putExtras(bundle);
            startActivityLeft(intent);
            closeActiviry();
        }
        return super.onOptionsItemSelected(item);
    }


}
