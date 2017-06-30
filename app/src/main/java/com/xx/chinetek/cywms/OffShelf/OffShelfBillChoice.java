package com.xx.chinetek.cywms.OffShelf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.OffShelf.OffSehlfBillChoiceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.Qc.QCBillChoice;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.OffShelf.OutStockTaskInfo_Model;
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


@ContentView(R.layout.activity_offshelfbill_choice)
public class OffShelfBillChoice extends BaseActivity  implements SwipeRefreshLayout.OnRefreshListener{


    String TAG_GetT_OutTaskDetailListByHeaderIDADF = "OffShelfBillChoice_GetT_OutTaskDetailListByHeaderIDADF";
    private final int RESULT_GetT_OutTaskDetailListByHeaderIDADF = 101;

    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_GetT_OutTaskDetailListByHeaderIDADF:
                AnalysisGetT_OutTaskDetailListByHeaderIDADFJson((String) msg.obj);
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

    Context context = OffShelfBillChoice.this;

    boolean isPickingAdmin=false;//是否有分配拣货单权限
    OffSehlfBillChoiceItemAdapter offSehlfBillChoiceItemAdapter;
    List<OutStockTaskInfo_Model> outStockTaskInfoModels;


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.receipt_subtitle), false);
        x.view().inject(this);
       // isPickingAdmin=BaseApplication.userInfo.isBIsAdmin();

    }

    @Override
    protected void initData() {
        super.initData();
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
        MenuItem gMenuItem=menu.findItem(R.id.action_filter);
        gMenuItem.setVisible(isPickingAdmin);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            List<OutStockTaskInfo_Model> temp=new ArrayList<>();
            for (int i=0;i<outStockTaskInfoModels.size();i++){
                if(offSehlfBillChoiceItemAdapter.getStates(i)){
                    temp.add(0,outStockTaskInfoModels.get(i));
                }
            }

            //选择拣货人员
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("选择拣货人员");
            //    指定下拉列表的显示数据
            final String[] person = {"人1", "人2", "人3", "人4", "人5"};
            //    设置一个下拉的列表选择项
            builder.setItems(person, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Toast.makeText(context,  person[which], Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvOffshelfChioce,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!isPickingAdmin) {
            OutStockTaskInfo_Model outStockTaskInfoModel=(OutStockTaskInfo_Model)offSehlfBillChoiceItemAdapter.getItem(position);
            Intent intent = new Intent(context, OffshelfScan.class);
            Bundle  bundle=new Bundle();
            bundle.putParcelable("outStockTaskInfoModel",outStockTaskInfoModel);
            intent.putExtras(bundle);
            startActivityLeft(intent);
        }else{
            offSehlfBillChoiceItemAdapter.modifyStates(position);
            offSehlfBillChoiceItemAdapter.notifyDataSetInvalidated();
        }
    }

    /**
     * 初始化加载listview
     */
    private void InitListView() {
        OutStockTaskInfo_Model outStockTaskInfoModel=new OutStockTaskInfo_Model();
        outStockTaskInfoModel.setStatus(1);
        GetT_OutStockTaskInfoList(outStockTaskInfoModel);
    }

    void GetT_OutStockTaskInfoList(OutStockTaskInfo_Model outStockTaskInfoModel){
        try {
            String ModelJson = GsonUtil.parseModelToJson(outStockTaskInfoModel);
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(OffShelfBillChoice.class, TAG_GetT_OutTaskDetailListByHeaderIDADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutTaskDetailListByHeaderIDADF, getString(R.string.Msg_GetT_OutTaskDetailListByHeaderIDADF), context, mHandler, RESULT_GetT_OutTaskDetailListByHeaderIDADF, null,  URLModel.GetURL().GetT_OutTaskDetailListByHeaderIDADF, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }


    void AnalysisGetT_OutTaskDetailListByHeaderIDADFJson(String result){
        LogUtil.WriteLog(QCBillChoice.class, TAG_GetT_OutTaskDetailListByHeaderIDADF,result);
        ReturnMsgModelList<OutStockTaskInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutStockTaskInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            outStockTaskInfoModels=returnMsgModel.getModelJson();
            if (outStockTaskInfoModels != null)
                BindListVIew(outStockTaskInfoModels);
        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
        }
    }

    private void BindListVIew(List<OutStockTaskInfo_Model> outStockTaskInfoModels) {
        offSehlfBillChoiceItemAdapter=new OffSehlfBillChoiceItemAdapter(context,outStockTaskInfoModels);
        lsvOffshelfChioce.setAdapter(offSehlfBillChoiceItemAdapter);

    }





}

