//package com.xx.chinetek.cywms;
//
//import android.content.Context;
//import android.os.Message;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.xx.chinetek.base.BaseActivity;
//import com.xx.chinetek.base.BaseApplication;
//import com.xx.chinetek.model.UserInfo;
//import com.xx.chinetek.util.function.DESUtil;
//import com.xx.chinetek.util.function.GsonUtil;
//import com.xx.chinetek.base.ToolBarTitle;
//import com.xx.chinetek.util.Network.NetworkError;
//import com.xx.chinetek.util.Network.RequestHandler;
//import com.xx.chinetek.util.dialog.ToastUtil;
//
//import org.xutils.view.annotation.ContentView;
//import org.xutils.view.annotation.Event;
//import org.xutils.view.annotation.ViewInject;
//import org.xutils.x;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@ContentView(R.layout.activity_main)
//public class MainActivity123 extends BaseActivity {
//
//    Context context = MainActivity123.this;
//    @ViewInject(R.id.button)
//    Button button;
//
//    @Override
//    protected void initViews() {
//        super.initViews();
//        BaseApplication.context = context;
//        BaseApplication.toolBarTitle = new ToolBarTitle("WMS", "", false);
//        x.view().inject(this);
//        setContentView(R.layout.activity_main);
//
//    }
//
//    @Override
//    protected void initData() {
//        super.initData();
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_mian, menu);
//        return true;
//    }
//
//    @Event(value = R.id.button, type = View.OnLongClickListener.class)
//    private void BtnClcik(View view) {
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_edit) {
//            Toast.makeText(BaseApplication.context, "Click edit", Toast.LENGTH_SHORT).show();
//            UserInfo user = new UserInfo();
//            user.setUserNo("admin");
//            user.setPassWord(DESUtil.encode("admin"));
//            user.setWarehouseID(23);
//            String userJson = GsonUtil.parseModelToJson(user);
//            String url = "http://192.168.100.88:8000/AndroidService.svc/UserLoginADF";
//            Map<String, String> params = new HashMap<>();
//            params.put("UserJson", userJson);
//            RequestHandler.addRequestWithDialog(Request.Method.POST, "tag", "loading.....", MainActivity123.this, mHandler, RESULT_GET_IP_INFO, null, url, params, null);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private static final int RESULT_GET_IP_INFO = 101;
//    private static final int RESULT_GET_IP = 102;
//
//    @Override
//    public void onHandleMessage(Message msg) {
//        switch (msg.what) {
//            case RESULT_GET_IP_INFO:
//                String result = (String) msg.obj;
//                break;
//            case RESULT_GET_IP:
//                result = (String) msg.obj;
//                break;
//            case NetworkError.NET_ERROR_CUSTOM:
//                ToastUtil.show("获取请求失败");
//                break;
//        }
//    }
//
//
//}
