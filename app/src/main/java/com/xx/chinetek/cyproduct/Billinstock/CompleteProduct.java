package com.xx.chinetek.cyproduct.Billinstock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
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
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_complete_product)
public class CompleteProduct extends  SocketBaseActivity {

    Context context=CompleteProduct.this;
    @ViewInject(R.id.txtNO)
    TextView txtNO;

    @ViewInject(R.id.txtdesc)
    TextView txtdesc;

    @ViewInject(R.id.txtlineno)
    TextView txtlineno;

    @ViewInject(R.id.etxtBatch)
    EditText etxtBatch;

    @ViewInject(R.id.etxtBNumber)
    EditText etxtBNumber;

    @ViewInject(R.id.txtWeight)
    TextView txtWeight;

    @ViewInject(R.id.butIn)
    Button butIn;

    @ViewInject(R.id.butOut)
    Button butOut;

    @ViewInject(R.id.butIOut)
    Button butIOut;

    @ViewInject(R.id.butT)
    Button butT;

    WoModel womodel;

    String TAG_Print_Outlabel = "OffShelfBillChoice_GetT_InBill";
    private final int RESULT_Print_Outlabel = 101;


    @Override
    public void onHandleMessage(Message msg) {
//        mSwipeLayout.setRefreshing(false);
     switch (msg.what) {
            case RESULT_Print_Outlabel:
                AnalysisGetT_RESULT_Print_OutlabelADFJson((String)msg.obj);
                break;

            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
//                CommonUtil.setEditFocus(edt_filterContent);
                break;
        }
    }

    void AnalysisGetT_RESULT_Print_OutlabelADFJson(String result){
        try {
            LogUtil.WriteLog(BillsIn.class, TAG_Print_Outlabel, result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                MessageBox.Show(context, "打印成功！");

                String serialno = returnMsgModel.getMaterialDoc();
                modelsAll.get(modelsAll.size()-1).setSerialNo(serialno);
//                PalletDAll.get(0).getLstBarCode().get(PalletDAll.get(0).getLstBarCode().size()).setSerialNo(serialno);

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


        GetWoModel(womodel);
    }


    /*
获取工单信息
 */
    void GetWoModel(WoModel womodel){
        if(womodel!=null) {
            txtNO.setText(womodel.getVoucherNo());
            etxtBatch.setText(womodel.getBatchNo());
            txtdesc.setText(womodel.getMaterialDesc());
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
                    if (meg.length>=3 )
                    {txtWeight.setText(message1.contains("ST,GS")?meg[2].trim():"");}

                }
            }
        };
    }

    @Event(value = {R.id.butIn,R.id.butOut,R.id.butT,R.id.butIOut},type = View.OnClickListener.class)
    private void onClick(View view) {
        if (etxtBatch.getText().toString().isEmpty()||txtlineno.getText().toString().isEmpty()||etxtBNumber.getText().toString().isEmpty()){
            MessageBox.Show(context, "填写信息不能为空！");
            return;
        }
        else{
            etxtBatch.setEnabled(false);
        }

        if (R.id.butIn==view.getId())
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

    ArrayList<Barcode_Model> models =new ArrayList<>();//临时的打印标签
    ArrayList<Barcode_Model> modelsInAll =new ArrayList<>();//全部的内箱打印标签
    ArrayList<Barcode_Model> modelsAll =new ArrayList<>();//全部的外箱打印标签
    ArrayList<PalletDetail_Model> PalletDAll = new ArrayList<>();//托标签
    PalletDetail_Model PalletD = new PalletDetail_Model();//托标签

    public void printlabel(int flag) {
        Barcode_Model model =new Barcode_Model();
        model.setIP(URLModel.PrintIP+":9100");
        model.setStrongHoldCode(womodel.getStrongHoldCode());
        model.setErpVoucherNo(womodel.getErpVoucherNo());
        model.setMaterialNo(womodel.getMaterialNo());
        model.setBatchNo(etxtBatch.getText().toString());
        model.setUnit(womodel.getUnit());
        model.setLineno(txtlineno.getText().toString());
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        model.setEDate(curDate);
        model.setQty(Float.parseFloat(etxtBNumber.getText().toString()));//数量
        String aaa=txtWeight.getText().toString();
        if (aaa.equals("称重数量")){
            MessageBox.Show(context, "电子称没有启动，无法获取重量！");
            return;
        }
        aaa=aaa.substring(0,aaa.length()-2);
//        Float bbb = Float.parseFloat(aaa);
        model.setItemQty(aaa);//重量

        if (flag==0){
//            model.setAreano("Areano");//标题
//            model.setProductClass("1111");//生产班组
//            model.ProductBatch = "";//成品批号
//            model.setQty(bbb) = 1;//数量
//            model.BarcodeNo = 1;//第几箱总箱数

            model.setLabelMark("InSanZhuang");
            modelsInAll.add(model);

        }
        if (flag==1 ){
            //成品外
            if (womodel.getStrVoucherType().equals("成品"))
            {
                model.setLabelMark("OutChengPin");
                modelsAll.add(model);
            }
            if (womodel.getStrVoucherType().equals("半制品"))
            {
                //半制外
                model.setLabelMark("OutBanZhi");
                model.setSupPrdBatch(model.getBatchNo());
                modelsAll.add(model);
                model.setBarcodeNo(modelsAll.size());
            }
            if (womodel.getStrVoucherType().equals("散装物料"))
            {
                //散装外
                model.setLabelMark("OutSanZhuang");
//                model.setRelaWeight("");
//                model.setStoreCondition();
//                model.setProtectWay();
                if (modelsInAll.size()==0){
                    MessageBox.Show(context, "没有内标签！");
                    return;
                }
                model.setBoxCount(modelsInAll.size());
            }

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
                barcode.setBatchNo(modelsAll.get(i).getBatchNo());
                barcode.setErpVoucherNo(modelsAll.get(i).getErpVoucherNo());
                barcode.setMaterialNo(modelsAll.get(i).getMaterialNo());
                barcode.setMaterialDesc(modelsAll.get(i).getMaterialDesc());
                barcode.setSerialNo(modelsAll.get(i).getSerialNo());
                BarCodes.add(barcode);
            }
            //成品外托
            PalletD.setPrintIPAdress(URLModel.PrintIP);
            PalletD.setStrongHoldCode(model.getStrongHoldCode());
            PalletD.setErpVoucherNo(model.getErpVoucherNo());
            PalletD.setLstBarCode(BarCodes);
            PalletDAll.add(PalletD);
        }


        try {
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            if (flag==2 )
            {
                params.put("json", GsonUtil.parseModelToJson(PalletDAll));
                params.put("printtype", GsonUtil.parseModelToJson(1));
            }
            else{
                params.put("json", GsonUtil.parseModelToJson(models));
                params.put("printtype", GsonUtil.parseModelToJson(0));
            }


//            LogUtil.WriteLog(OffShelfBillChoice.class, TAG_GetT_OutTaskListADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Print_Outlabel, getString(R.string.Msg_Print), context, mHandler,
                    RESULT_Print_Outlabel, null,  URLModel.GetURL().PrintLabel, params, null);
//                MessageBox.Show(context, "打印成功！");
        } catch (Exception ex) {
//                mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }finally{
            if (flag==2 )
            {
                PalletDAll.clear();
                PalletD=new PalletDetail_Model();
                modelsAll.clear();
                models.clear();
            }
            else{
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
            Intent intent=new Intent(context, ReportOutputNum.class);

            Bundle bundle=new Bundle();
            bundle.putParcelable("WoModel",womodel);
            intent.putExtras(bundle);
            startActivityLeft(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}
