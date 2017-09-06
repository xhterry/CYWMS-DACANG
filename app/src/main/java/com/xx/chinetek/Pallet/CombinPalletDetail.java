package com.xx.chinetek.Pallet;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
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
import com.xx.chinetek.model.Pallet.PalletDetail_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Review.OutStockDetailInfo_Model;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.ArithUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.view.annotation.ContentView;
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
    ArrayList<OutStockDetailInfo_Model> outStockDetailInfoModels;
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        Get_PalletDetailByVoucherNo(voucherNo);
    }

//    @Event(value = R.id.lsvPalletDetail,type = AdapterView.OnItemLongClickListener.class)
//    private boolean lsvGroupDetailonItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        if(id>=0) {
//            delPalletModel=(PalletDetail_Model)palletDetailItemAdapter.getGroup(position);
//            new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除组托数据？\n托盘号："+delPalletModel.getPalletNo())
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // TODO 自动生成的方法
//                            Del_PalletOrSerialNo(delPalletModel);
//                        }
//                    }).setNegativeButton("取消", null).show();
//        }
//        return true;
//    }

//    @Event(value = R.id.lsvPalletDetail,type = ExpandableListView.OnChildClickListener.class)
//    private boolean lsvGroupDetailonChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//        delBarCodeInfo= ((PalletDetail_Model)palletDetailItemAdapter.getGroup(groupPosition)).getLstBarCode().get(childPosition);
//        new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除此条序列号？\n序列号："+delBarCodeInfo.getSerialNo())
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO 自动生成的方法
//                        PalletDetail_Model palletDetailModel=new PalletDetail_Model();
//                        palletDetailModel.setLstBarCode(new ArrayList<BarCodeInfo>());
//                        palletDetailModel.getLstBarCode().add(delBarCodeInfo);
//                        Del_PalletOrSerialNo(palletDetailModel);
//                    }
//                }).setNegativeButton("取消", null).show();
//
//        return false;
//    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // 过滤按键动作
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getAction() == KeyEvent.ACTION_UP) {
            close();
        }
        return true;
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

    private void Del_PalletOrSerialNo(PalletDetail_Model palletDetailModel) {
        final Map<String, String> params = new HashMap<String, String>();
        String userJson=GsonUtil.parseModelToJson(BaseApplication.userInfo);
        String palletDetailJson=GsonUtil.parseModelToJson(palletDetailModel);
        params.put("UserJson", userJson);
        params.put("PalletDetailJson", palletDetailJson);
        LogUtil.WriteLog(CombinPalletDetail.class, TAG_Del_PalletOrSerialNo, palletDetailJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Del_PalletOrSerialNo, getString(R.string.Msg_Del_PalletOrbarcode), context, mHandler,
                RESULT_Msg_Del_PalletOrSerialNo, null,  URLModel.GetURL().Delete_PalletORBarCodeADF, params, null);
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

//        outStockDetailInfoModels.get(index).getLstStock().add(0, StockInfo_Model);
//        outStockDetailInfoModels.get(index).setScanQty(qty);
//        outStockDetailInfoModels.get(index).setOustockStatus(1); //存在未组托条码

        StockInfo_Model stockInfoModel=new StockInfo_Model("",SerialNo);
        for(int i=0;i<outStockDetailInfoModels.size();i++){
            int index= outStockDetailInfoModels.get(i).getLstStock().indexOf(stockInfoModel);
            if(index!=-1){
                outStockDetailInfoModels.get(i).setScanQty(ArithUtil.sub(outStockDetailInfoModels.get(i).getScanQty(),outStockDetailInfoModels.get(i).getLstStock().get(index).getQty()));
                outStockDetailInfoModels.get(i).getLstStock().remove(index);
                outStockDetailInfoModels.get(i).setOustockStatus(1);
            }
        }
    }

    void close(){
        Intent mIntent = new Intent();
        mIntent.putParcelableArrayListExtra("outStockDetailInfoModels",outStockDetailInfoModels);
        setResult(RESULT_OK, mIntent);
        closeActiviry();
    }

}
