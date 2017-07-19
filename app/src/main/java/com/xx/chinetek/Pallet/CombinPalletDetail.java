package com.xx.chinetek.Pallet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Pallet.PalletDetailItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Material.SerialNo_Model;
import com.xx.chinetek.model.Pallet.PalletDetail_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.WMS.Review.OutStockDetailInfo_Model;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_combin_pallet_detail)
public class CombinPalletDetail extends BaseActivity {

    String TAG_Get_PalletDetailByVoucherNo="CombinPalletDetail_Get_PalletDetailByVoucherNo";
    String TAG_Del_PalletOrSerialNo="CombinPalletDetail_Del_PalletOrSerialNo";
    private final  int RESULT_Msg_Get_PalletDetailByVoucherNo=101;
    private final  int RESULT_Msg_Del_PalletOrSerialNo=102;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_Get_PalletDetailByVoucherNo:
                AnalysisGet_PalletDetailByVoucherNoJson((String) msg.obj);
                break;
            case RESULT_Msg_Del_PalletOrSerialNo:
                AnalysisDel_PalletOrSerialNoJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }


    Context context = CombinPalletDetail.this;
    @ViewInject(R.id.lsvPalletDetail)
    ExpandableListView lsvPalletDetail;

    List<PalletDetail_Model> PalletDetailModelList;
    PalletDetail_Model delPalletModel=null;
    BarCodeInfo delBarCodeInfo=null;
    PalletDetailItemAdapter palletDetailItemAdapter;
    ArrayList<OutStockDetailInfo_Model> outStockDetailInfoModels;//用于收货
    String voucherNo;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.Pallet_subtitle), true);
        x.view().inject(this);
        voucherNo=getIntent().getStringExtra("VoucherNo");
        outStockDetailInfoModels=getIntent().getParcelableArrayListExtra("outStockDetailInfoModels");
        PalletDetailModelList=new ArrayList<PalletDetail_Model>();
        Get_PalletDetailByVoucherNo(voucherNo);
    }

    @Event(value = R.id.lsvPalletDetail,type = AdapterView.OnItemLongClickListener.class)
    private boolean lsvGroupDetailonItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(id>=0) {
            delPalletModel=(PalletDetail_Model)palletDetailItemAdapter.getGroup(position);
            new AlertDialog.Builder(context).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除组托数据？\n托盘号："+delPalletModel.getPalletNo())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法
                            Del_PalletOrSerialNo(delPalletModel.getPalletNo(),"","");
                        }
                    }).setNegativeButton("取消", null).show();
        }
        return true;
    }

    @Event(value = R.id.lsvPalletDetail,type = ExpandableListView.OnChildClickListener.class)
    private boolean lsvGroupDetailonChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        delPalletModel=((PalletDetail_Model)palletDetailItemAdapter.getGroup(groupPosition));
        delBarCodeInfo= ((PalletDetail_Model)palletDetailItemAdapter.getGroup(groupPosition)).getLstBarCode().get(childPosition);
        new AlertDialog.Builder(context).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除此条序列号？\n序列号："+delBarCodeInfo.getSerialNo())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法
                        Del_PalletOrSerialNo(delPalletModel.getPalletNo(),delBarCodeInfo.getSerialNo(),delBarCodeInfo.getMaterialNo());
                    }
                }).setNegativeButton("取消", null).show();

        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 过滤按键动作
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Intent mIntent = new Intent();
            mIntent.putParcelableArrayListExtra("outStockDetailInfoModels",outStockDetailInfoModels);
            setResult(RESULT_OK, mIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
 获取组托明细
  */
    private void Get_PalletDetailByVoucherNo(String VoucherNo) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("VoucherNo", VoucherNo);
        LogUtil.WriteLog(CombinPalletDetail.class, TAG_Get_PalletDetailByVoucherNo, VoucherNo);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Get_PalletDetailByVoucherNo, getString(R.string.Msg_Get_PalletDetailByVoucherNo), context, mHandler, RESULT_Msg_Get_PalletDetailByVoucherNo, null,  URLModel.GetURL().Get_PalletDetailByVoucherNo, params, null);

    }

    void AnalysisGet_PalletDetailByVoucherNoJson(String result){
        try {
            LogUtil.WriteLog(CombinPalletDetail.class, TAG_Get_PalletDetailByVoucherNo, result);
            ReturnMsgModelList<PalletDetail_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<PalletDetail_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                PalletDetailModelList=returnMsgModel.getModelJson();
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
            if(PalletDetailModelList==null) {PalletDetailModelList=new ArrayList<PalletDetail_Model>();}
            palletDetailItemAdapter = new PalletDetailItemAdapter(context, PalletDetailModelList);
            lsvPalletDetail.setAdapter(palletDetailItemAdapter);

        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    private void Del_PalletOrSerialNo(final String PalletNo,final String SerialNo,final String MaterialNo) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("PalletNo", PalletNo);
        params.put("SerialNo", SerialNo);
        String para = (new JSONObject(params)).toString();
        LogUtil.WriteLog(CombinPalletDetail.class, TAG_Del_PalletOrSerialNo, para);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Del_PalletOrSerialNo, getString(R.string.Msg_Del_PalletOrbarcode), context, mHandler,
                RESULT_Msg_Del_PalletOrSerialNo, null,  URLModel.GetURL().Del_PalletOrSerialNo, params, null);
    }

    void AnalysisDel_PalletOrSerialNoJson(String result){
        try {
            LogUtil.WriteLog(CombinPalletDetail.class, TAG_Del_PalletOrSerialNo, result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                PalletDetailModelList = new ArrayList<PalletDetail_Model>();
                if(delBarCodeInfo!=null){
                    DeleteSerial(delBarCodeInfo.getSerialNo(), delBarCodeInfo.getMaterialNo());
                }else{
                    for (BarCodeInfo serials : delPalletModel.getLstBarCode()) {
                        DeleteSerial(serials.getSerialNo(), serials.getMaterialNo());
                    }
                }
                delPalletModel=new PalletDetail_Model();
                delBarCodeInfo=null;
                Get_PalletDetailByVoucherNo(voucherNo);
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }

        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void DeleteSerial(String SerialNo,String MaterialNo){
        OutStockDetailInfo_Model temp = new OutStockDetailInfo_Model();
        temp.setMaterialNo(MaterialNo);
        int RowIndex = outStockDetailInfoModels.indexOf(temp);
        SerialNo_Model serialNo_model=new SerialNo_Model();
        serialNo_model.setSerialNo(SerialNo);
        int sindex=outStockDetailInfoModels.get(RowIndex).getLstSerialNo().indexOf(serialNo_model);
        if(sindex!=-1){
            outStockDetailInfoModels.get(RowIndex).getLstSerialNo().remove(sindex);
            outStockDetailInfoModels.get(RowIndex).setScanQty(outStockDetailInfoModels.get(RowIndex).getScanQty() - 1);
        }
    }


}
