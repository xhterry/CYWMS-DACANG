package com.xx.chinetek.cyproduct.Billinstock;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.Service.SocketService;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.SocketBaseActivity;
import com.xx.chinetek.cyproduct.work.ReportOutputNum;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Pallet.PalletDetail_Model;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.ArithUtil;
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


@ContentView(R.layout.activity_complete_product)
public class CompleteProduct extends  SocketBaseActivity {

    Context context=CompleteProduct.this;

    @ViewInject(R.id.txtNO)
    TextView txtNO;

    @ViewInject(R.id.txtMno)
    TextView txtMno;

    @ViewInject(R.id.txtdesc)
    TextView txtdesc;

    @ViewInject(R.id.txtlineno)
    TextView txtlineno;

    @ViewInject(R.id.etxtBatch)
    EditText etxtBatch;

    @ViewInject(R.id.etxtBNumber)
    EditText etxtBNumber;

    @ViewInject(R.id.EditW)
    EditText EditW;

    @ViewInject(R.id.butIn)
    Button butIn;

    @ViewInject(R.id.butOut)
    Button butOut;

    @ViewInject(R.id.butIOut)
    Button butIOut;

    @ViewInject(R.id.butT)
    Button butT;

    @ViewInject(R.id.XNumber)
    EditText txtBi;

    @ViewInject(R.id.textView75)
    TextView textView75;

    @ViewInject(R.id.txtdate)
    TextView txtdate;


    WoModel womodel;

    String TAG_Print_Inlabel = "OffShelfBillChoice_Print_Inlabel";
    private final int RESULT_Print_Inlabel = 101;

    String TAG_Print_Outlabel = "OffShelfBillChoice_Print_Outlabel";
    private final int RESULT_Print_Outlabel = 102;

    String TAG_Print_Tlabel = "OffShelfBillChoice_Print_Tlabel";
    private final int RESULT_Print_Tlabel = 103;

    @Override
    public void onHandleMessage(Message msg) {
     switch (msg.what) {
            case RESULT_Print_Inlabel:
                AnalysisGetT_RESULT_Print_InlabelADFJson((String)msg.obj);
                break;
         case RESULT_Print_Outlabel:
             AnalysisGetT_RESULT_Print_OutlabelADFJson((String)msg.obj);
             break;
         case RESULT_Print_Tlabel:
             AnalysisGetT_RESULT_Print_TlabelADFJson((String)msg.obj);
             break;

            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
//                CommonUtil.setEditFocus(edt_filterContent);
                break;
        }
    }
    void AnalysisGetT_RESULT_Print_InlabelADFJson(String result){
        try {
            LogUtil.WriteLog(BillsIn.class, TAG_Print_Inlabel, result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetT_RESULT_Print_OutlabelADFJson(String result){
        try {
            LogUtil.WriteLog(BillsIn.class, TAG_Print_Outlabel, result);
            ReturnMsgModel<Barcode_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Barcode_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                String serialno = returnMsgModel.getMaterialDoc();
                Barcode_Model limbarcode =  returnMsgModel.getModelJson();
                modelsAll.get(modelsAll.size()-1).setSerialNo(serialno);
                modelsAll.get(modelsAll.size()-1).setMaterialNo(limbarcode.getMaterialNo());
                modelsAll.get(modelsAll.size()-1).setMaterialDesc(limbarcode.getMaterialDesc());
                modelsAll.get(modelsAll.size()-1).setMaterialNoID(limbarcode.getMaterialNoID());

//              PalletDAll.get(0).getLstBarCode().get(PalletDAll.get(0).getLstBarCode().size()).setSerialNo(serialno);

            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }
    void AnalysisGetT_RESULT_Print_TlabelADFJson(String result){
        try {
            LogUtil.WriteLog(BillsIn.class, TAG_Print_Tlabel, result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
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
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
        CommonUtil.setEditFocus(etxtBatch);
        initVariables();//设置接收服务
    }

    @Override
    protected void initData() {
        super.initData();
        womodel=getIntent().getParcelableExtra("WoModel");
        butIn.setVisibility(womodel.getStrVoucherType().equals("散装物料")? View.VISIBLE:View.GONE);
        butIOut.setVisibility(womodel.getStrVoucherType().equals("成品")? View.GONE:View.VISIBLE);
        butT.setVisibility(womodel.getStrVoucherType().equals("成品")? View.VISIBLE:View.GONE);
        butOut.setVisibility(womodel.getStrVoucherType().equals("成品")? View.VISIBLE:View.GONE);

        //相对比重
        txtBi.setVisibility(womodel.getStrVoucherType().equals("散装物料")? View.VISIBLE:View.GONE);
        textView75.setVisibility(womodel.getStrVoucherType().equals("散装物料")? View.VISIBLE:View.GONE);

        GetWoModel(womodel);
    }


    @Override
    public void BackAlter() {
        String Msg="";
        if (modelsInAll.size()!=0||modelsAll.size()!=0){
            Msg="条码打印还没完毕，确认退出？";
        }
        else{
            Msg="是否返回上一页面？";
        }
        new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage(Msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法
                        closeActiviry();
                    }
                }).setNegativeButton("取消", null).show();
        }


    /*
        获取工单信息
         */
    void GetWoModel(WoModel womodel){
        if(womodel!=null) {
            txtNO.setText(womodel.getErpVoucherNo());
            etxtBatch.setText(womodel.getBatchNo());
            txtdesc.setText(womodel.getMaterialDesc());
            txtMno.setText(womodel.getMaterialNo());
        }
    }


    protected void initVariables()
    {
        //给全局消息接收器赋值，并进行消息处理
        mReciver = new MessageBackReciver(){
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if(action.equals(SocketService.MESSAGE_ACTION))
                {
                    String message = intent.getStringExtra("message");
                    Log.v("WMSLOG_Socket", message);
                    String message1=message.split("\r\n")[0];
                   String[] meg =message1.split(",");
                    if (meg.length>=3 &&isOpenWeight)
//                    {EditW.setText(message1.contains("ST,GS")?meg[2].trim():"");}
                    {EditW.setText((message1.contains(",NT")||message1.contains("ST,GS"))?meg[2].trim():"");}
                }
            }
        };
    }

    int mYear, mMonth,mDate;
    @Event(value = R.id.txtdate,type = View.OnClickListener.class )
    private void txtProductStartTimeClick(View view){
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH)+1;
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

    private boolean isOpenWeight=true;
    @Event(value = R.id.EditW,type = View.OnFocusChangeListener.class)
    private void OnFocusChange(View view, boolean isFouse) {
        if (isFouse)
        {
            EditW.setText("");
            isOpenWeight=false;
        }
        else
        {
            if(EditW.getText().toString().equals(""))
            {
                isOpenWeight=true;
            }
        }
     }

    @Event(value = {R.id.butIn,R.id.butOut,R.id.butT,R.id.butIOut},type = View.OnClickListener.class)
    private void onClick(View view) {
        if (DoubleClickCheck.isFastDoubleClick(context)) {
            return;
        }
        if (etxtBatch.getText().toString().isEmpty()||txtlineno.getText().toString().isEmpty()||etxtBNumber.getText().toString().isEmpty()){
            MessageBox.Show(context, "填写信息不能为空！");
            return;
        }
        else{
            womodel.setBatchNo(etxtBatch.getText().toString());
            etxtBatch.setEnabled(false);
        }
        if(R.id.butIn==view.getId())
        {
            printlabel(0);
        }
        if (R.id.butOut==view.getId())
        {
              printlabel(1);
        }
        if (R.id.butIOut==view.getId())
        {
            printlabel(1);
        }
        if (R.id.butT==view.getId())
        {
            if (modelsAll.size()==0)
            {
                MessageBox.Show(context, "没有外箱标签");
                return;
            }else{
                printlabel(2);
            }

        }
    }
    ArrayList<Barcode_Model> modelsIn =new ArrayList<>();//临时的内箱打印标签
    ArrayList<Barcode_Model> models =new ArrayList<>();//临时的外箱打印标签
    ArrayList<Barcode_Model> modelsInAll =new ArrayList<>();//全部的内箱打印标签
    ArrayList<Barcode_Model> modelsAll =new ArrayList<>();//全部的外箱打印标签
    ArrayList<PalletDetail_Model> PalletDAll = new ArrayList<>();//托标签
    PalletDetail_Model PalletD = new PalletDetail_Model();//托标签

    public void printlabel(int flag) {
        try {
            Barcode_Model model =new Barcode_Model();
            model.setIP(URLModel.PrintIP+":9100");
            model.setStrongHoldCode(womodel.getStrongHoldCode());
            model.setStrongHoldName(womodel.getStrongHoldName());
            model.setErpVoucherNo(womodel.getErpVoucherNo());
            model.setMaterialNo(womodel.getMaterialNo());
            model.setBatchNo(etxtBatch.getText().toString());
            model.setUnit(womodel.getUnit());
            model.setLineno(txtlineno.getText().toString());
            model.setEDate(CommonUtil.dateStrConvertDate(txtdate.getText().toString()));
            model.setQty(Float.parseFloat(etxtBNumber.getText().toString()));//数量
            model.setCompanyCode(womodel.getCompanyCode());
            model.setRowNoDel("1");


            String Weight=EditW.getText().toString();
            if (Weight.equals(""))
            {
                MessageBox.Show(context, "电子称不稳定，无法获取数据！");
                return;
            }
            else{
                if (!CommonUtil.isFloat(Weight)){
                    Weight=Weight.substring(0,Weight.length()-2).trim();
                    if (!CommonUtil.isFloat(Weight))
                    {
                        MessageBox.Show(context, "电子称不稳定，无法获取数据！");
                        return;
                    }
                    else{
                        model.setItemQty(Weight);//重量
                    }
                }else{
                    model.setItemQty(Weight);//重量
                }

            }


//        String aaa=txtWeight.getText().toString();
//        aaa=aaa.substring(0,aaa.length()-2);
//        model.setItemQty(aaa);//重量

            if (flag==0){
                model.setQty(Float.parseFloat(model.getItemQty()));
                model.setItemQty(etxtBNumber.getText().toString());
//            model.setAreano("Areano");//标题
//            model.setProductClass("1111");//生产班组
//            model.ProductBatch = "";//成品批号
//            model.setQty(bbb) = 1;//数量
//            model.BarcodeNo = 1;//第几箱总箱数
                model.setBarcodeType(0);
                model.setBarcodeNo(modelsInAll.size()+1);
                model.setRelaWeight(txtBi.getText().toString());
                model.setLabelMark("InSanZhuang");
                modelsInAll.add(model);
                modelsIn.add(model);
            }
            if (flag==1 ){
                model.setBarcodeType(1);
                //成品外
                if (womodel.getStrVoucherType().equals("成品"))
                {
                    if (txtdate.getText().toString().isEmpty()){
                        MessageBox.Show(context, "有效期不能为空！");
                        return;
                    }
                    if (Float.parseFloat(model.getItemQty())<=0)
                    {
                        MessageBox.Show(context, "成品称重数量不能小于等于0");
                        return;
                    }
                    if (womodel.getMaxProductQty()!=null)
                    {
                        Float Sum=0f;
                        for ( int i=0;i<modelsInAll.size();i++)
                        {
                            Sum= ArithUtil.add(modelsInAll.get(i).getQty(),Sum);
                        }
                        if (Sum>womodel.getMaxProductQty())
                        {
                            MessageBox.Show(context, "成品包装数量超过最大限制数量："+ womodel.getMaxProductQty().toString());
                            return;
                        }
                    }

                    model.setLabelMark("OutChengPin");
                }
                if (womodel.getStrVoucherType().equals("半制品"))
                {
                    //半制外
                    model.setLabelMark("OutBanZhi");
                    model.setSupPrdBatch(model.getBatchNo());
                    model.setBarcodeNo(modelsAll.size()+1);
                }
                if (womodel.getStrVoucherType().equals("散装物料"))
                {
                    //散装外
                    model.setItemQty("0");

                    String We=EditW.getText().toString();
                    if (We.equals(""))
                    {
                        MessageBox.Show(context, "电子称不稳定，无法获取数据！");
                        return;
                    }
                    else{
                        if (!CommonUtil.isFloat(We)){
                            We=We.substring(0,We.length()-2).trim();
                            if (!CommonUtil.isFloat(We))
                            {
                                MessageBox.Show(context, "电子称不稳定，无法获取数据！");
                                return;
                            }
                            else{
                                model.setQty(Float.parseFloat(We));//重量
                            }
                        }else{
                            model.setQty(Float.parseFloat(We));//重量
                        }

                    }

                    model.setLabelMark("OutSanZhuang");
                    model.setRelaWeight(txtBi.getText().toString());
//                model.setRelaWeight("");
//                model.setStoreCondition();
//                model.setProtectWay();
                    if (modelsInAll.size()==0){
                        MessageBox.Show(context, "没有内标签！");
                        return;
                    }

                    Float Sum=0f;
                    model.setBoxCount(modelsInAll.size());
                    for ( int i=0;i<modelsInAll.size();i++)
                    {
                        Sum= ArithUtil.add(modelsInAll.get(i).getQty(),Sum);
                    }
                    model.setQty(Sum);
                }
                modelsAll.add(model);
                models.add(model);
//            model.setSerialNo("20170825001999");
//            model.setAreano("Areano");//标题
//            model.setProductClass("1111");//生产班组
//            model.setBoxWeight("2");//包装方式
            }
            if (flag==2 ){
                ArrayList<BarCodeInfo> BarCodes =new ArrayList<>();//全部的打印标签
                for (int i=0;i<modelsAll.size();i++)
                {
                    BarCodeInfo barcode = new BarCodeInfo();
                    barcode.setQty(modelsAll.get(i).getQty());
                    barcode.setBarcodeType(modelsAll.get(i).getBarcodeType());
                    barcode.setCompanyCode(modelsAll.get(i).getCompanyCode());
                    barcode.setRowNoDel(modelsAll.get(i).getRowNoDel());

                    barcode.setStrongHoldName(modelsAll.get(i).getStrongHoldName());

                    barcode.setBatchNo(modelsAll.get(i).getBatchNo());
                    barcode.setErpVoucherNo(modelsAll.get(i).getErpVoucherNo());
                    barcode.setMaterialNo(modelsAll.get(i).getMaterialNo());
                    barcode.setMaterialDesc(modelsAll.get(i).getMaterialDesc());
                    barcode.setSerialNo(modelsAll.get(i).getSerialNo());
                    barcode.setMaterialNoID(modelsAll.get(i).getMaterialNoID());
                    barcode.setStrongHoldCode(modelsAll.get(i).getStrongHoldCode());
                    barcode.setEDate(modelsAll.get(i).getEDate());
//                barcode.setVoucherNo(modelsAll.get(i).getVoucherNo());

                    BarCodes.add(barcode);
                }

                //成品外托
                PalletD.setPrintIPAdress(URLModel.PrintIP);
                PalletD.setStrongHoldCode(model.getStrongHoldCode());
                PalletD.setErpVoucherNo(model.getErpVoucherNo());
                PalletD.setLstBarCode(BarCodes);

                PalletD.setBatchNo(BarCodes.get(0).getBatchNo());
                PalletD.setMaterialNo(BarCodes.get(0).getMaterialNo());
                PalletD.setMaterialDesc(BarCodes.get(0).getMaterialDesc());
                PalletD.setMaterialNoID(BarCodes.get(0).getMaterialNoID());
                PalletD.setEDate(BarCodes.get(0).getEDate());

                PalletDAll.add(PalletD);
            }


            String path="";
            int Returnpath=0;

            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            if (flag==2 )
            {
                params.put("json", GsonUtil.parseModelToJson(PalletDAll));
                params.put("printtype", GsonUtil.parseModelToJson(1));

                path=TAG_Print_Tlabel;
                Returnpath=RESULT_Print_Tlabel;
            }
            if (flag==1 ){
                params.put("json", GsonUtil.parseModelToJson(models));
                params.put("printtype", GsonUtil.parseModelToJson(0));

                path=TAG_Print_Outlabel;
                Returnpath=RESULT_Print_Outlabel;
            }
            if (flag==0 ){
                params.put("json", GsonUtil.parseModelToJson(modelsIn));
                params.put("printtype", GsonUtil.parseModelToJson(0));

                path=TAG_Print_Inlabel;
                Returnpath=RESULT_Print_Inlabel;
            }


//            LogUtil.WriteLog(OffShelfBillChoice.class, TAG_GetT_OutTaskListADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, path, getString(R.string.Msg_Print), context, mHandler,Returnpath, null,  URLModel.GetURL().PrintLabel, params, null);
//                MessageBox.Show(context, "打印成功！");
        } catch (Exception ex) {
//                mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }finally{
            if (flag==0 )
            {
                modelsIn.clear();
                models.clear();
            }
            if (flag==1 )
            {
                models.clear();
                modelsInAll.clear();
            }
            if (flag==2 )
            {
                PalletDAll.clear();
                PalletD=new PalletDetail_Model();
                modelsAll.clear();
                models.clear();
            }


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
