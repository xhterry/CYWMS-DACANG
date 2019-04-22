package com.xx.chinetek.cywms.OffShelf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.OffShelf.OffSehlfBillChoiceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.Qc.QCBillChoice;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.UerInfo;
import com.xx.chinetek.model.WMS.OffShelf.OutStockTaskInfo_Model;
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


@ContentView(R.layout.activity_offshelfbill_choice)
public class OffShelfBillChoice extends BaseActivity  implements SwipeRefreshLayout.OnRefreshListener{


    String TAG_GetT_OutTaskListADF = "OffShelfBillChoice_GetT_OutTaskListADF";
    String TAG_GetPickUserListByUserADF = "OffShelfBillChoice_GetPickUserListByUserADF";
    String TAG_SavePickUserListADF = "OffShelfBillChoice_SavePickUserListADF";
    private final int RESULT_GetT_OutTaskListADF = 101;
    private final int RESULT_GetPickUserListByUserADF = 102;
    private final int RESULT_SavePickUserListADF = 103;

    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_GetT_OutTaskListADF:
                AnalysisGetT_OutTaskDetailListByHeaderIDADFJson((String) msg.obj);
                break;
            case RESULT_GetPickUserListByUserADF:
                AnalysisGetPickUserListByUserADFJson((String) msg.obj);
                break;
            case RESULT_SavePickUserListADF:
                AnalysisSavePickUserListADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtfilterContent);
                break;
        }
    }


    @ViewInject(R.id.lsvOffshelfChioce)
    ListView lsvOffshelfChioce;
    @ViewInject(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(R.id.edt_filterContent)
    EditText edtfilterContent;
    @ViewInject(R.id.btn_StartPicking)
    Button btnStartPicking;
    MenuItem  gMenuItem1=null;
    MenuItem  gMenuItem=null;

    Context context = OffShelfBillChoice.this;

    boolean isPickingAdmin=false;//是否有分配拣货单权限
    OffSehlfBillChoiceItemAdapter offSehlfBillChoiceItemAdapter;
    ArrayList<OutStockTaskInfo_Model> outStockTaskInfoModels;
    ArrayList<OutStockTaskInfo_Model> selectoutStockTaskInfoModels;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.receipt_subtitle), false);
        x.view().inject(this);
        isPickingAdmin=BaseApplication.userInfo.getIsPickLeader()==2;
    }

    @Override
    protected void initData() {
        super.initData();
        edtfilterContent.setVisibility(isPickingAdmin?View.GONE:View.VISIBLE);
        btnStartPicking.setVisibility(isPickingAdmin?View.GONE:View.VISIBLE);
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitListView();
    }

    @Override
    public void onRefresh() {
        outStockTaskInfoModels=new ArrayList<>();
        edtfilterContent.setText("");
        InitListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuoffshelfbillchoice, menu);
        gMenuItem=menu.findItem(R.id.action_filter);
        gMenuItem1=menu.findItem(R.id.action_filter1);
        gMenuItem.setVisible(isPickingAdmin);
        gMenuItem1.setVisible(isPickingAdmin);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter ) {
                try {
                    GetSelectTask();
                    if (selectoutStockTaskInfoModels.size() != 0) {
                        if (isPickingAdmin) {
                            Map<String, String> params = new HashMap<>();
                            String UserModel = GsonUtil.parseModelToJson(BaseApplication.userInfo);
                            params.put("UserJson", UserModel);
                            LogUtil.WriteLog(OffShelfBillChoice.class, TAG_GetPickUserListByUserADF, UserModel);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetPickUserListByUserADF, getString(R.string.Msg_GetT_GetPickUserListByUserADF), context, mHandler, RESULT_GetPickUserListByUserADF, null, URLModel.GetURL().GetPickUserListByUserADF, params, null);
                        }
                    } else {
                        MessageBox.Show(context, getString(R.string.Msg_NoSelectOffshelfTask));
                    }
                } catch (Exception ex) {
                    MessageBox.Show(context, ex.getMessage());
                }

        }
        if(item.getItemId() == R.id.action_filter1){
            try {
                isPickingAdmin=!isPickingAdmin;
                gMenuItem.setVisible(isPickingAdmin);
                edtfilterContent.setVisibility(isPickingAdmin?View.GONE:View.VISIBLE);
                btnStartPicking.setVisibility(isPickingAdmin?View.GONE:View.VISIBLE);
                item.setTitle(isPickingAdmin?getString(R.string.isPicking):getString(R.string.cancelPicking));
                InitListView();
            } catch (Exception ex) {
                MessageBox.Show(context, ex.getMessage());
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Event(value = R.id.edt_filterContent,type = View.OnKeyListener.class)
    private  boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            if(outStockTaskInfoModels!=null && outStockTaskInfoModels.size()>0) {
                String code = edtfilterContent.getText().toString().trim();
                //扫描单据号、检查单据列表
                OutStockTaskInfo_Model outStockTaskInfoModel = new OutStockTaskInfo_Model(code,code);
                int index=outStockTaskInfoModels.indexOf(outStockTaskInfoModel);
                if (index!=-1) {
                    offSehlfBillChoiceItemAdapter.modifyStates(index);
                    offSehlfBillChoiceItemAdapter.notifyDataSetInvalidated();
                }
            }
            CommonUtil.setEditFocus(edtfilterContent);
        }
        return false;
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvOffshelfChioce,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            offSehlfBillChoiceItemAdapter.modifyStates(position);
            offSehlfBillChoiceItemAdapter.notifyDataSetInvalidated();
    }

    @Event(R.id.btn_StartPicking)
    private void btnStartPickingClick(View view){
        if(!GetSelectTask()){
            MessageBox.Show(context,getString(R.string.Error_NotSameType));
            return;
        }
        if(selectoutStockTaskInfoModels!=null  && selectoutStockTaskInfoModels.size()!=0){
            StartScanIntent(selectoutStockTaskInfoModels);
        }

    }


    Boolean GetSelectTask(){
        selectoutStockTaskInfoModels = new ArrayList<>();
        int IsEdate=-1;
        for (int i = 0; i < outStockTaskInfoModels.size(); i++) {
            if (offSehlfBillChoiceItemAdapter.getStates(i)) {
                selectoutStockTaskInfoModels.add(0, outStockTaskInfoModels.get(i));
                if(IsEdate==-1){
                    IsEdate= outStockTaskInfoModels.get(i).getVoucherType();
                }
                if(IsEdate!=outStockTaskInfoModels.get(i).getVoucherType())
                    return false;
            }
        }
        return true;
    }
    /**
     * 初始化加载listview
     */
    private void InitListView() {
        outStockTaskInfoModels=new ArrayList<>();
        selectoutStockTaskInfoModels=new ArrayList<>();
        OutStockTaskInfo_Model outStockTaskInfoModel=new OutStockTaskInfo_Model();
        outStockTaskInfoModel.setStatus(1);
        if(isPickingAdmin)
            outStockTaskInfoModel.setPickLeaderUserNo(BaseApplication.userInfo.getUserNo());
        else
            outStockTaskInfoModel.setPickUserNo(BaseApplication.userInfo.getUserNo());
        GetT_OutStockTaskInfoList(outStockTaskInfoModel);
    }

    void GetT_OutStockTaskInfoList(OutStockTaskInfo_Model outStockTaskInfoModel){
        try {
            String ModelJson = GsonUtil.parseModelToJson(outStockTaskInfoModel);
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(OffShelfBillChoice.class, TAG_GetT_OutTaskListADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutTaskListADF, getString(R.string.Msg_GetT_OutTaskListADF), context, mHandler,
                    RESULT_GetT_OutTaskListADF, null,  URLModel.GetURL().GetT_OutTaskListADF, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }


    void AnalysisSavePickUserListADFJson(String result){
        LogUtil.WriteLog(QCBillChoice.class, TAG_SavePickUserListADF,result);
        ReturnMsgModelList<OutStockTaskInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutStockTaskInfo_Model>>() {}.getType());
        MessageBox.Show(context, returnMsgModel.getMessage());
        BindListVIew(outStockTaskInfoModels);
    }

    void AnalysisGetPickUserListByUserADFJson(String result){
        LogUtil.WriteLog(QCBillChoice.class, TAG_GetPickUserListByUserADF,result);
        ReturnMsgModelList<UerInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<UerInfo>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
           final List<UerInfo> usrInfos=returnMsgModel.getModelJson();
            if (usrInfos != null){
                final String[] person = new String[usrInfos.size()];
                for (int i=0;i<usrInfos.size();i++) {
                    person[i]=usrInfos.get(i).getUserName();
                }
                //选择拣货人员
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("选择拣货人员");
                builder.setCancelable(false);
                builder.setItems(person, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SavePickUserListADF(selectoutStockTaskInfoModels,usrInfos.get(which));
                    }
                });
                builder.show();
            }

        }else
        {
            MessageBox.Show(context,returnMsgModel.getMessage());
        }
    }

    void SavePickUserListADF(List<OutStockTaskInfo_Model> outStockModels ,UerInfo userInfo){

        try {
            String ModelJson = GsonUtil.parseModelToJson(outStockModels);
            Map<String, String> params = new HashMap<>();
            ArrayList<UerInfo> uerInfos=new ArrayList<>();
            uerInfos.add(userInfo);
            String UserJson= GsonUtil.parseModelToJson(uerInfos);
            params.put("UserJson",UserJson);
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(OffShelfBillChoice.class, TAG_SavePickUserListADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SavePickUserListADF, getString(R.string.Msg_SavePickUserListADF), context, mHandler, RESULT_SavePickUserListADF, null,  URLModel.GetURL().SavePickUserListADF, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetT_OutTaskDetailListByHeaderIDADFJson(String result){
        try {
            LogUtil.WriteLog(QCBillChoice.class, TAG_GetT_OutTaskListADF, result);
            ReturnMsgModelList<OutStockTaskInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutStockTaskInfo_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                outStockTaskInfoModels = returnMsgModel.getModelJson();
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
            if (outStockTaskInfoModels != null)
                BindListVIew(outStockTaskInfoModels);
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }



    private void BindListVIew(List<OutStockTaskInfo_Model> outStockTaskInfoModels) {
        offSehlfBillChoiceItemAdapter=new OffSehlfBillChoiceItemAdapter(context,isPickingAdmin,outStockTaskInfoModels);
        lsvOffshelfChioce.setAdapter(offSehlfBillChoiceItemAdapter);

    }

    void StartScanIntent(ArrayList<OutStockTaskInfo_Model> outStockTaskInfoModels){
        Intent intent = new Intent(context, OffshelfScan.class);
        Bundle  bundle=new Bundle();
        for(int i=0;i<outStockTaskInfoModels.size();i++){
            outStockTaskInfoModels.get(i).setWareHouseID(BaseApplication.userInfo.getWarehouseID());
        }
        bundle.putParcelableArrayList("outStockTaskInfoModel",outStockTaskInfoModels);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }
}

