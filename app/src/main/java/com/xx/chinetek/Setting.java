package com.xx.chinetek;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.SharePreferUtil;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


@ContentView(R.layout.activity_setting)
public class Setting extends BaseActivity {

    Context context=Setting.this;

    @ViewInject(R.id.edt_IPAdress)
    EditText edtIPAdress;
    @ViewInject(R.id.edt_Port)
    EditText edtPort;
    @ViewInject(R.id.edt_TimeOut)
    EditText edtTimeOut;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.login_setting),true);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        SharePreferUtil.ReadShare(context);
        edtIPAdress.setText(URLModel.IPAdress);
        edtPort.setText(URLModel.Port+"");
        edtTimeOut.setText(RequestHandler.SOCKET_TIMEOUT/1000+"");
    }

    @Event(R.id.btn_SaveSetting)
    private void btnSetting(View view){
        String IPAdress=edtIPAdress.getText().toString().trim();
        Integer Port=Integer.parseInt(edtPort.getText().toString().trim());
        Integer TimeOut=Integer.parseInt(edtTimeOut.getText().toString().trim())*1000;
        if(CommonUtil.MatcherIP(IPAdress)){
            SharePreferUtil.SetShare(context,IPAdress,Port,TimeOut);
            MessageBox.ShowAndClose(context,getResources().getString(R.string.SaveSuccess));
        }else{
            MessageBox.Show(context,getResources().getString(R.string.Error_Setting_IPAdressError));
            CommonUtil.setEditFocus(edtIPAdress);
        }
    }
}
