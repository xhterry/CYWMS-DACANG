package com.xx.chinetek.cywms.Receiption;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Receiption.ReceiptBillChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.Qc.QCMaterialChoice;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Receiption.Receipt_Model;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.CommonUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xx.chinetek.cywms.R.id.edt_filterContent;


@ContentView(R.layout.activity_receipt_bill_choice)
public class ReceiptBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    String TAG_GetT_InStockList = "ReceiptBillChoice_GetT_InStockList";
    String TAG_GetT_PalletDetailByBarCode = "ReceiptBillChoice_GetT_PalletDetailByBarCode";
    private final int RESULT_GetT_InStockList = 101;
    private final int RESULT_GetT_PalletDetailByBarCode=102;
    private final int supplierRequestCode = 1001;

    Context context = ReceiptBillChoice.this;
    boolean isCancelFilterButton=false; //供应商筛选标志


    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_GetT_InStockList:
                AnalysisGetT_InStockListJson((String) msg.obj);
                break;
            case RESULT_GetT_PalletDetailByBarCode:
                AnalysisGetT_PalletDetailByBarCodeJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtfilterContent);
                break;
        }
    }

    @ViewInject(R.id.lsvChoiceReceipt)
    ListView lsvChoiceReceipt;
    @ViewInject(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(R.id.edt_filterContent)
    EditText edtfilterContent;
    @ViewInject(R.id.txt_Suppliername)
    TextView txtSuppliername;
    MenuItem  gMenuItem=null;


    ArrayList<Receipt_Model> receiptModels;//单据信息
    List<Map<String, String>> SupplierList= new ArrayList<Map<String, String>>();//供应商列表
    ReceiptBillChioceItemAdapter receiptBillChioceItemAdapter;

    ArrayList<BarCodeInfo> barCodeInfos;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.receipt_subtitle), false);
        x.view().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isCancelFilterButton)
            InitListView();
    }

    @Override
    protected void initData() {
        super.initData();
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
    }

    @Override
    public void onRefresh() {
        if(isCancelFilterButton){
            isCancelFilterButton=false;
            gMenuItem.setTitle(getResources().getString(R.string.filter));
            txtSuppliername.setText(getResources().getString(R.string.supplierNoFilter));
        }
        InitListView();
    }


    /**
     * 初始化加载listview
     */
    private void InitListView() {
        barCodeInfos=new ArrayList<>();
        receiptModels=new ArrayList<>();
        edtfilterContent.setText("");
        Receipt_Model receiptModel = new Receipt_Model();
        receiptModel.setStatus(1);
        receiptModel.setStatus(1);
        GetT_InStockList(receiptModel);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbillchoice, menu);
        gMenuItem=menu.findItem(R.id.action_filter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            if (receiptModels!=null && receiptModels.size() != 0) {
                if(isCancelFilterButton){
                    isCancelFilterButton=false;
                    txtSuppliername.setText(getResources().getString(R.string.supplierNoFilter));
                    item.setTitle(getResources().getString(R.string.filter));
                    BindListVIew(receiptModels);
                }else {
                    for(int i=0;i<receiptModels.size();i++) {
                        Map<String, String> map = new HashMap<String, String>();
                        String SupplierName = receiptModels.get(i).getSupplierName();
                        String SupplierID = receiptModels.get(i).getSupplierNo();
                        map = new HashMap<String, String>();
                        map.put("SupplierName", SupplierName == null || SupplierName.isEmpty() ? "空" : SupplierName);
                        map.put("SupplierID", SupplierID == null || SupplierID.isEmpty() ? "000000" : SupplierID);
                        SupplierList.add(map);
                    }
                    Intent intent = new Intent(context, SupplierFilterActivity.class);
                    Bundle bundle = new Bundle();
                    ArrayList bundlelist = new ArrayList();
                    bundlelist.add(SupplierList);
                    bundle.putParcelableArrayList("SupplierList", bundlelist);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, supplierRequestCode);
                }
            }
        }

        if(item.getItemId() ==R.id.action_QCfilter){
            Intent intent = new Intent(context, QCMaterialChoice.class);
            intent.putExtra("ErpVourcherNo","");
            startActivityLeft(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 供应商筛选界面返回值接收
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==supplierRequestCode && resultCode==RESULT_OK){
            String SupplierID=data.getStringExtra("SupplierID");
            String SupplierName=data.getStringExtra("SupplierName");
            txtSuppliername.setText(SupplierName);
            ArrayList<Receipt_Model> receiptModelList= new ArrayList<>();
            for (  Receipt_Model tempreceiptModel:receiptModels ) {
                if(tempreceiptModel.getSupplierNo()!=null && tempreceiptModel.getSupplierNo().equals(SupplierID) && tempreceiptModel.getSupplierName().equals(SupplierName)){
                    receiptModelList.add(tempreceiptModel);
                }
           }
            isCancelFilterButton=true;
            if(gMenuItem!=null ){
                gMenuItem.setTitle(getResources().getString(R.string.title_Receipt_RightFilter));
            }
            BindListVIew(receiptModelList);
        }
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoiceReceipt, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       Receipt_Model receiptModel=(Receipt_Model) receiptBillChioceItemAdapter.getItem(position);
        StartScanIntent(receiptModel,null);
     }

    @Event(value = edt_filterContent,type = View.OnKeyListener.class)
    private  boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            if(receiptModels!=null && receiptModels.size()>0) {
                String code = edtfilterContent.getText().toString().trim();
                //扫描单据号、检查单据列表
                Receipt_Model receiptModel = new Receipt_Model(code);
                int index=receiptModels.indexOf(receiptModel);
                if (index!=-1) {
                    StartScanIntent(receiptModels.get(index), null);
                    return false;
                } else {
                    //扫描箱条码
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("BarCode", code);
                    LogUtil.WriteLog(ReceiptBillChoice.class, TAG_GetT_PalletDetailByBarCode, code);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCode, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_PalletDetailByBarCode, null,  URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);
                    return false;
                }
            }
           // StartScanIntent(null,null);
            CommonUtil.setEditFocus(edtfilterContent);
        }
        return false;
    }


    void AnalysisGetT_InStockListJson(String result){
        try {
            LogUtil.WriteLog(ReceiptBillChoice.class, TAG_GetT_InStockList, result);
            //Gson gson =new GsonBuilder().registerTypeAdapter(Date.class, new NetDateTimeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            ReturnMsgModelList<Receipt_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Receipt_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                receiptModels = returnMsgModel.getModelJson();
                if (receiptModels != null && receiptModels.size() == 1 && barCodeInfos != null && barCodeInfos.size()!=0)
                    StartScanIntent(receiptModels.get(0), barCodeInfos);
                else
                    BindListVIew(receiptModels);
            } else {
                ToastUtil.show(returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            ToastUtil.show(ex.getMessage());
        }
        CommonUtil.setEditFocus(edtfilterContent);
    }

    void AnalysisGetT_PalletDetailByBarCodeJson(String result) {
        LogUtil.WriteLog(ReceiptBillChoice.class, TAG_GetT_PalletDetailByBarCode, result);
        try {
            ReturnMsgModelList<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<BarCodeInfo>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                this.barCodeInfos = returnMsgModel.getModelJson();
                if (barCodeInfos != null) {
                    // Receipt_Model receiptModel = new Receipt_Model(barCodeInfo.getBarCode());
                    //  int index = receiptModels.indexOf(receiptModel);
                    //  if (index != -1) {
                    //调用GetT_InStockList 赋值ERP订单号字段，获取Receipt_Model列表，跳转到扫描界面
                    Receipt_Model receiptModel = new Receipt_Model();
                    receiptModel.setStatus(1);
                    receiptModel.setErpVoucherNo(barCodeInfos.get(0).getErpVoucherNo());
                    GetT_InStockList(receiptModel);
                    //   } else {
                    //     MessageBox.Show(context, R.string.Error_BarcodeNotInList);
                    // }
                }
            } else {
                ToastUtil.show(returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            ToastUtil.show(ex.getMessage());
        }
        CommonUtil.setEditFocus(edtfilterContent);
    }
    void GetT_InStockList(Receipt_Model receiptModel){
        try {
            String ModelJson = GsonUtil.parseModelToJson(receiptModel);
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(ReceiptBillChoice.class, TAG_GetT_InStockList, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockList, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_InStockList, null,  URLModel.GetURL().GetT_InStockListADF, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void StartScanIntent(Receipt_Model receiptModel,ArrayList<BarCodeInfo> barCodeInfo){
        Intent intent=new Intent(context,ReceiptionScan.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("receiptModel",receiptModel);
        bundle.putParcelableArrayList("barCodeInfo",barCodeInfo);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }

    private void BindListVIew(ArrayList<Receipt_Model> receiptModels) {
        receiptBillChioceItemAdapter=new ReceiptBillChioceItemAdapter(context,receiptModels);
        lsvChoiceReceipt.setAdapter(receiptBillChioceItemAdapter);
    }
}

