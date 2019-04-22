package com.xx.chinetek.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.CheckNumRefMaterial;
import com.xx.chinetek.util.UpdateVersionService;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.CommonUtil;
import com.xx.chinetek.util.hander.IHandleMessage;
import com.xx.chinetek.util.hander.MyHandler;

import java.math.BigDecimal;

import static com.xx.chinetek.base.BaseApplication.context;

/**
 * Created by GHOST on 2017/3/15.
 */

public abstract class BaseActivity extends AppCompatActivity implements IHandleMessage {
    private ToolBarHelper mToolBarHelper;
    public Toolbar toolbar;
    public static final String ACTION_UPDATEUI = "action.updateUI";
    public MyHandler<BaseActivity> mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //屏幕保持竖屏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //隐藏输入法
        AppManager.getAppManager().addActivity(this); //添加当前Activity到avtivity管理类
        mHandler = new MyHandler<>(this);
        BaseApplication.isCloseActivity=true;
        updateVersionService = new UpdateVersionService(context);// 创建更新业务对象
        initViews(); //自定义的方法
        initData();
    }

    /**
     * 初始化控件
     */
    protected void initViews() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {
        if(BaseApplication.isCloseActivity)
            checkUpdate();
    }



    @Override
    public void onHandleMessage(Message msg) {

    }
//
    @Override
    public void setContentView(int layoutResID) {
        if(layoutResID==R.layout.activity_login)
            getDelegate().setContentView(layoutResID);
        else {
            mToolBarHelper = new ToolBarHelper(this, layoutResID);
            toolbar = mToolBarHelper.getToolBar();
            setContentView(mToolBarHelper.getContentView());
            if (!TextUtils.isEmpty(BaseApplication.toolBarTitle.Title))
                setTitle(BaseApplication.toolBarTitle.Title);
//        if (!TextUtils.isEmpty(BaseApplication.toolBarTitle.subTitle))
//            toolbar.setSubtitle(BaseApplication.toolBarTitle.subTitle);
//        //toolbar.setLogo(R.mipmap.ic_launcher);
            if (BaseApplication.toolBarTitle.isShowBack)
                toolbar.setNavigationIcon(R.drawable.back);
        /*把 toolbar 设置到Activity 中*/
            setSupportActionBar(toolbar);
        /*自定义的一些操作*/
        if(BaseApplication.toolBarTitle!=null) {
            onCreateCustomToolBar(toolbar);}
        }

    }

    public void onCreateCustomToolBar(Toolbar toolbar) {
            toolbar.setContentInsetsRelative(0, 0);
            toolbar.showOverflowMenu();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (!context.getClass().getName().equals("com.xx.chinetek.cywms.MainActivity") ||
//                    !context.getClass().getName().equals("com.xx.chinetek.cyproduct.MainActivity")
//                    ) {
//                        BackAlter();
//                    }
                    if(BaseApplication.isCloseActivity)
                        closeActiviry();
                    else
                        BackAlter();
                }
            });
    }



    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_UP) {
//            if(!(context.getClass().getName().equals("com.xx.chinetek.cywms.MainActivity") ||
//                    context.getClass().getName().equals("com.xx.chinetek.cyproduct.MainActivity") ||
//                    context.getClass().getName().equals("com.xx.chinetek.Login")))
//                BackAlter();
//            else{
//                closeActiviry();
//            }
            if(BaseApplication.isCloseActivity)
               closeActiviry();
            else
                BackAlter();
        }
        return true;
    }


    public void BackAlter(){
        new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("是否返回上一页面？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法
                        closeActiviry();
                    }
                }).setNegativeButton("取消", null).show();
    }

    /**
     * 隐藏键盘
     */
    public void keyBoardCancle() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public  void closeActiviry(){
        AppManager.getAppManager().finishActivity();
        BaseApplication.isCloseActivity=true;
        if(AppManager.getAppManager().GetActivityCount()!=0)
            context = AppManager.getAppManager().currentActivity();
    }

    /**
     * 左右推动跳转
     *
     * @param intent
     */
    protected void startActivityLeft(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }



    /*
       判断单位对应数字输入规则
     */
   public CheckNumRefMaterial CheckMaterialNumFormat(String qty, String UnitTypeCode, String DecimalLngth){
        CheckNumRefMaterial checkNumRefMaterial=new CheckNumRefMaterial();
       try {
           int unitTypeCode = Integer.parseInt(UnitTypeCode);
           int decimalLngth = Integer.parseInt(DecimalLngth);

           if (unitTypeCode == 4) {
               if (CommonUtil.isNumeric(qty)) {
                   checkNumRefMaterial.setIscheck(true);
                   checkNumRefMaterial.setCheckQty(Float.parseFloat(qty));
               } else {
                   checkNumRefMaterial.setIscheck(false);
                   checkNumRefMaterial.setErrMsg(getString(R.string.Error_IntRequire));
               }
           } else {
               if (CommonUtil.isFloat(qty)) {
                   checkNumRefMaterial.setIscheck(true);
                   BigDecimal mData = new BigDecimal(qty).setScale(decimalLngth, BigDecimal.ROUND_HALF_UP);
                   checkNumRefMaterial.setCheckQty(mData.floatValue());
               } else {
                   checkNumRefMaterial.setIscheck(false);
                   checkNumRefMaterial.setErrMsg(getString(R.string.Error_isnotnum));
               }
           }
       }catch (Exception ex){
           ToastUtil.show(ex.getMessage());
       }
        return checkNumRefMaterial;
    }

    public UpdateVersionService updateVersionService;

    /**
     * 检查更新
     */
    public void checkUpdate() {

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
