package com.xx.chinetek.cyproduct.Billinstock;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.SocketBaseActivity;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cyproduct.work.ReportOutputNum;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.CommonUtil;
import com.xx.chinetek.util.function.DoubleClickCheck;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


@ContentView(R.layout.activity_complete_productw)
public class CompleteProductW extends  SocketBaseActivity {

    Context context=CompleteProductW.this;

    @ViewInject(R.id.txtNO)
    TextView txtNO;

    @ViewInject(R.id.txtdesc)
    TextView txtdesc;

    @ViewInject(R.id.txtMno)
    TextView txtMno;

    @ViewInject(R.id.txtlineno)
    TextView txtlineno;

    @ViewInject(R.id.etxtBatch)
    EditText etxtBatch;

    @ViewInject(R.id.TNumber)
    EditText TNumber;

    @ViewInject(R.id.XNumber)
    EditText XNumber;

    @ViewInject(R.id.LNumber)
    EditText LNumber;


    @ViewInject(R.id.txtdate)
    TextView txtdate;

    @ViewInject(R.id.butOut)
    Button butOut;


    WoModel womodel;


    String TAG_Print_OutlabelW = "OffShelfBillChoice_Print_OutlabelW";
    private final int RESULT_Print_OutlabelW = 100;


    @Override
    public void onHandleMessage(Message msg) {
        try{
            switch (msg.what) {
                case RESULT_Print_OutlabelW:
                    AnalysisGetT_RESULT_Print_OutlabelWADFJson((String)msg.obj);
                    break;
                case NetworkError.NET_ERROR_CUSTOM:
                    ToastUtil.show("获取请求失败_____"+ msg.obj);
//                CommonUtil.setEditFocus(edt_filterContent);
                    break;
            }
        }catch(Exception ex){
            MessageBox.Show(context,"接口异常！");
        }

    }


    void AnalysisGetT_RESULT_Print_OutlabelWADFJson(String result){
        try {
            LogUtil.WriteLog(BillsIn.class, TAG_Print_OutlabelW, result);
            ReturnMsgModel<Barcode_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Barcode_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {

            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }




    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.Product_ProductYMH), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
        CommonUtil.setEditFocus(etxtBatch);

    }

    @Override
    protected void initData() {
        super.initData();
        womodel=getIntent().getParcelableExtra("WoModel");
        GetWoModel(womodel);
    }


//    @Override
//    public void BackAlter() {
//        String Msg="";
//        new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage(Msg)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO 自动生成的方法
//                        closeActiviry();
//                    }
//                }).setNegativeButton("取消", null).show();
//        }


    /*
        获取工单信息
         */
    void GetWoModel(WoModel womodel){
        if(womodel!=null) {
            txtNO.setText(womodel.getErpVoucherNo());
            etxtBatch.setText(womodel.getBatchNo());
            txtdesc.setText(womodel.getMaterialDesc());
            txtMno.setText(womodel.getMaterialNo());
            TNumber.setText(womodel.getBox_Amount()==null?"":womodel.getBox_Amount().toString());
            XNumber.setText(womodel.getPack_Amount()==null?"":womodel.getPack_Amount().toString());


        }
    }

    int mYear, mMonth,mDate;
    @Event(value = R.id.txtdate,type = View.OnClickListener.class )
    private void txtProductStartTimeClick(View view){
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDate=ca.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month+1;
                mDate=dayOfMonth;
                txtdate.setText(display());
            }
            },mYear, mMonth,mDate).show();

    }

    public String  display() {
        return new StringBuffer().append(mYear).append("-").append(mMonth).append("-").append(mDate).toString();
    }

    @Event(value = {R.id.butOut},type = View.OnClickListener.class)
    private void onClick(View view) {
        if (DoubleClickCheck.isFastDoubleClick(context)) {
            return;
        }
        try{
            if (!CommonUtil.isNumeric(LNumber.getText().toString())
                    ||!CommonUtil.isNumeric(TNumber.getText().toString())
                    ||!CommonUtil.isNumeric(XNumber.getText().toString())
                    ||txtlineno.getText().toString().isEmpty()
                    ||etxtBatch.getText().toString().isEmpty())
            {
                MessageBox.Show(context, "填写信息错误！");
                return;
            }
            else{
                womodel.setBatchNo(etxtBatch.getText().toString());
                etxtBatch.setEnabled(false);
            }
            printlabel();

        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }

    }


    public void printlabel() {
        try {
            ArrayList<Barcode_Model> models =new ArrayList<>();//临时的外箱打印标签
            Barcode_Model model =new Barcode_Model();
            model.setWarehouseno("3");
            model.setIP(URLModel.PrintIP+":9100");
            model.setStrongHoldCode(womodel.getStrongHoldCode());
            model.setStrongHoldName(womodel.getStrongHoldName());
            model.setErpVoucherNo(womodel.getErpVoucherNo());
            model.setMaterialNo(womodel.getMaterialNo());
            model.setBatchNo(etxtBatch.getText().toString());
            model.setUnit(womodel.getUnit());
            model.setProductClass(txtlineno.getText().toString());
            model.setEDate(CommonUtil.dateStrConvertDate(txtdate.getText().toString()));
            model.setBoxCount(Integer.parseInt(TNumber.getText().toString()));//箱数
            model.setQty(Float.parseFloat(XNumber.getText().toString())*Integer.parseInt(TNumber.getText().toString())+Integer.parseInt(LNumber.getText().toString().isEmpty()?"0":LNumber.getText().toString()));//支数
            model.setCompanyCode(womodel.getCompanyCode());
            model.setRowNoDel("1");
            model.setBarcodeType(1);
            model.setLabelMark("OutChengPin");
            model.setSupPrdBatch(model.getBatchNo());

            model.setVoucherNo(womodel.getVoucherNo());
            model.setVoucherType(womodel.getVoucherType());

            models.add(model);

            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("json", GsonUtil.parseModelToJson(models));
            params.put("printtype", GsonUtil.parseModelToJson(0));
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Print_OutlabelW, getString(R.string.Msg_Print), context, mHandler,RESULT_Print_OutlabelW, null,  URLModel.GetURL().PrintLabel, params, null);

        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }


        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_complete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            if (womodel.getBatchNo()==null||womodel.getBatchNo().equals("")){
                MessageBox.Show(context,"还未打印条码不能报工！");
                return false;
            }
            Intent intent=new Intent(context, ReportOutputNum.class);
            Bundle bundle=new Bundle();
            bundle.putParcelable("WoModel",womodel);
            intent.putExtras(bundle);
            startActivityLeft(intent);
            closeActiviry();
        }
        return super.onOptionsItemSelected(item);
    }
}
