//package com.xx.chinetek.util.dialog;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnDismissListener;
//import android.content.DialogInterface.OnShowListener;
//import android.os.Handler;
//import android.os.Message;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.xx.chinetek.util.function.CommonUtil;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//
//public class DelayMessageBox {
//    AlertDialog alertDialog=null;
//
//    private final int BUTTON_CLICK = 1;  //定义关闭对话框的动作信号标志
//
//    private Handler mainHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case BUTTON_CLICK:
//                    Button btn = ((AlertDialog) alertDialog).getButton(DialogInterface.BUTTON_POSITIVE)
//                    if(alertDialog != null && alertDialog.isShowing()) {
//                        alertDialog.dismiss();  //关闭对话框
//                    }
//                    break;
//            }
//        }
//    };
//
//    private DelayCloseController delayCloseController = new DelayCloseController();
//    private class DelayCloseController extends TimerTask {
//        private Timer timer = new Timer();
//        private int actionFlags = 0;//标志位参数
//        public void setCloseFlags(int flag)
//        {
//            actionFlags = flag;
//        }
//        @Override
//        public void run() {
//            Message messageFinish = new Message();
//            messageFinish.what = actionFlags ;
//            mainHandler.sendMessage(messageFinish);
//        }
//    }
//
//
//
//////初始化对话框并显示
////    alertDialog = new AlertDialog.Builder(this)
////            .setTitle("自动关闭对话框")
////.setMessage("对话框将在8s之后关闭")
////.show();
////delayCloseController.setCloseFlags(CLOSE_ALERTDIALOG);             //设置信息标志位
////delayCloseController.timer.schedule(delayCloseController, 5000);   //启动定时器
//
//    /**
//     * 弹出默认提示框
//     *
//     * @param context 上下文
//     * @param message 需要弹出的消息
//     */
//    public static void Show(Context context, String message) {
//        new AlertDialog.Builder(context).setTitle("提示").setMessage(message).setPositiveButton("确定", null).show();
//    }
//
//    public static void Show(Context context, int resourceID) {
//        String msg = context.getResources().getString(resourceID);
//        new AlertDialog.Builder(context).setTitle("提示").setMessage(msg).setPositiveButton("确定", null).show();
//    }
//
//    public static void Show(Context context, String mString, EditText togText, AlertDialog alertDialog) {
//        alertDialog = new AlertDialog.Builder(context).setTitle("提示").setMessage(mString).setPositiveButton("确定", null).create();
//
//        final EditText tagEditText = togText;
//        alertDialog.setOnShowListener(new OnShowListener() {
//
//            @Override
//            public void onShow(DialogInterface dialog) {
//                CommonUtil.setEditFocus(tagEditText);
//            }
//        });
//
//        alertDialog.show();
//    }
//
//    public static void Show(Context context, String mString, EditText togText) {
//        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("提示").setMessage(mString).setPositiveButton("确定", null).create();
//
//        final EditText tagEditText = togText;
//        dialog.setOnShowListener(new OnShowListener() {
//
//            @Override
//            public void onShow(DialogInterface dialog) {
//                CommonUtil.setEditFocus(tagEditText);
//            }
//        });
//        dialog.setOnDismissListener(new OnDismissListener() {
//
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                CommonUtil.setEditFocus(tagEditText);
//            }
//        });
//        dialog.show();
//    }
//
//    public static void Show(Context context, String message, EditText recivceTEXT, EditText sendTEXT) {
//        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("提示").setMessage(message).setPositiveButton("是", null).create();
//        final EditText RecivceTEXT = recivceTEXT;
//        final EditText SendTEXT = sendTEXT;
//        dialog.setOnShowListener(new OnShowListener() {
//
//            @Override
//            public void onShow(DialogInterface dialog) {
//                CommonUtil.setEditFocus(RecivceTEXT);
//            }
//        });
//        dialog.setOnDismissListener(new OnDismissListener() {
//
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                CommonUtil.setEditFocus(SendTEXT);
//            }
//        });
//        dialog.show();
//    }
//
//}
