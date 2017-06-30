package com.xx.chinetek.cywms.Qc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.QC.QCBillChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.QC.QualityInfo_Model;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.Stock.StockInfo_Model;
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
import java.util.Map;


@ContentView(R.layout.activity_bill_choice)
public class QCBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    String TAG_GetT_QualityListADF = "QCBillChoice_GetT_QualityListADF";
    private final int RESULT_GetT_QualityListADF = 101;

    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_GetT_QualityListADF:
               AnalysisGetT_QualityListADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtfilterContent);
                break;
        }
    }

    @ViewInject(R.id.lsvChoice)
    ListView lsvChoice;
    @ViewInject(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(R.id.edt_filterContent)
    EditText edtfilterContent;
    @ViewInject(R.id.btn_NoTask)
    Button btnNoTask;


    Context context = QCBillChoice.this;
    QCBillChioceItemAdapter qcBillChioceItemAdapter;
    ArrayList<QualityInfo_Model> qualityInfoModels;
    StockInfo_Model stockInfoModel;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.QC_title), false);
        x.view().inject(this);
        btnNoTask.setVisibility(View.GONE);
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
        qualityInfoModels=new ArrayList<>();
        edtfilterContent.setText("");
        InitListView();
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoice,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        QualityInfo_Model qualityInfoModel=(QualityInfo_Model) qcBillChioceItemAdapter.getItem(position);
        StartScanIntent(qualityInfoModel);
    }

    @Event(value = R.id.edt_filterContent,type = View.OnKeyListener.class)
    private  boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            if(qualityInfoModels!=null && qualityInfoModels.size()>0) {
                String code = edtfilterContent.getText().toString().trim();
                //扫描单据号、检查单据列表
                QualityInfo_Model qualityInfoModel = new QualityInfo_Model(code);
                int index=qualityInfoModels.indexOf(qualityInfoModel);
                if (index!=-1) {
                    StartScanIntent(qualityInfoModels.get(index));
                    return false;
                }
            }
            StartScanIntent(null);
            CommonUtil.setEditFocus(edtfilterContent);
        }
        return false;
    }


    /**
     * 初始化加载listview
     */
    private void InitListView() {
        QualityInfo_Model qualityInfoModel=new QualityInfo_Model();
        qualityInfoModel.setStatus(1);
        GetT_QualityList(qualityInfoModel);
    }

    void GetT_QualityList(QualityInfo_Model qualityInfoModel){
        try {
            String ModelJson = GsonUtil.parseModelToJson(qualityInfoModel);
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(QCBillChoice.class, TAG_GetT_QualityListADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_QualityListADF, getString(R.string.Msg_GetT_QualityListADF), context, mHandler, RESULT_GetT_QualityListADF, null,  URLModel.GetURL().GetT_QualityListADF, params, null);
        } catch (Exception ex) {
            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisGetT_QualityListADFJson(String result){
        LogUtil.WriteLog(QCBillChoice.class, TAG_GetT_QualityListADF,result);
        ReturnMsgModelList<QualityInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<QualityInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            qualityInfoModels=returnMsgModel.getModelJson();
            if (qualityInfoModels != null && qualityInfoModels.size() == 1 && stockInfoModel != null)
                    StartScanIntent(qualityInfoModels.get(0));
            else
                BindListVIew(qualityInfoModels);
        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
        }
    }


    void StartScanIntent(QualityInfo_Model qualityInfoModel){
        Intent intent=new Intent(context,QCScan.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("qualityInfoModel",qualityInfoModel);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }

    private void BindListVIew(ArrayList<QualityInfo_Model> qualityInfoModels) {
        qcBillChioceItemAdapter=new QCBillChioceItemAdapter(context,qualityInfoModels);
        lsvChoice.setAdapter(qcBillChioceItemAdapter);
    }

}
