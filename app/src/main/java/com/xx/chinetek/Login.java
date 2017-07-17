package com.xx.chinetek;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cywms.MainActivity;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.UerInfo;
import com.xx.chinetek.model.User.UserInfo;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.SharePreferUtil;
import com.xx.chinetek.util.UpdateVersionService;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.DESUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;



@ContentView(R.layout.activity_login)
public class Login extends BaseActivity {

    String TAG="Loagin";
    private static final int RESULT_GET_LOGIN_INFO = 101;

    private UpdateVersionService updateVersionService;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GET_LOGIN_INFO:
                AnalysisJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }

    @ViewInject(R.id.txt_Verion)
    TextView txtVersion;
    @ViewInject(R.id.edt_UserName)
    EditText edtUserName;
    @ViewInject(R.id.edt_Password)
    EditText edtPassword;

    Context context=Login.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.context = context;
        x.view().inject(this);
        SharePreferUtil.ReadShare(context);
        checkUpdate();
        SharePreferUtil.ReadUserShare(context);
        if( BaseApplication.userInfo!=null){
            edtUserName.setText( BaseApplication.userInfo.getUserNo());
            edtPassword.setText(DESUtil.decode( BaseApplication.userInfo.getPassWord()));
        }
        txtVersion.setText(getString(R.string.login_Version)+(updateVersionService.getVersionCode(context)));
    }



    @Event(R.id.btn_Login)
    private  void  btnLoginClick(View view){
        String userName=edtUserName.getText().toString().trim();
        String password=edtPassword.getText().toString().trim();
        UserInfo user = new UserInfo();
        user.setUserNo(userName);
        user.setPassWord(password);
        if(user.CheckUserAndPass()){
            user.setPassWord(DESUtil.encode(user.getPassWord()));
            String userJson = GsonUtil.parseModelToJson(user);
            LogUtil.WriteLog(Login.class, TAG,userJson);
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", userJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG, getString(R.string.Msg_Login), context, mHandler, RESULT_GET_LOGIN_INFO, null, URLModel.GetURL().UserLoginADF, params, null);
        }
    }

    @Event(R.id.btn_Setting)
    private void btnSetting(View view){
        startActivityLeft(new Intent(context,Setting.class));
    }

    void AnalysisJson(String result){
        LogUtil.WriteLog(Login.class, TAG,result);
        ReturnMsgModel<UerInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<UerInfo>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            BaseApplication.userInfo=returnMsgModel.getModelJson();
            SharePreferUtil.SetUserShare(context, BaseApplication.userInfo);
            Intent intent=new Intent(context, URLModel.isWMS?MainActivity.class: com.xx.chinetek.cyproduct.MainActivity.class);
            startActivity(intent);
        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
        }
    }


    /**
     * 检查更新
     */
    private void checkUpdate() {
        updateVersionService = new UpdateVersionService(context);// 创建更新业务对象
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                if (updateVersionService.isUpdate()) {
                    handler.sendEmptyMessage(0);
                }// 调用检查更新的方法,如果可以更新.就更新.不能更新就提示已经是最新的版本了
                else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    updateVersionService.showDownloadDialog();
                    break;
            }
        };
    };
}
