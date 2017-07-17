package com.xx.chinetek.cyproduct.work;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xx.chinetek.adapter.wms.Production.ReportOutputItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Production.ReportOutputModel;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_report_output)
public class ReportOutput extends BaseActivity {

    Context context=ReportOutput.this;

    @ViewInject(R.id.lsvPWODetail)
    ListView lsvPWODetail;

    ReportOutputItemAdapter reportOutputItemAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.ReportOutput_title),false);
        x.view().inject(this);

        List<ReportOutputModel> ReportOutputModels=getData();
        reportOutputItemAdapter=new ReportOutputItemAdapter(context,ReportOutputModels);
        lsvPWODetail.setAdapter(reportOutputItemAdapter);
    }

    List<ReportOutputModel> getData(){
        List<ReportOutputModel> ReportOutputModels=new ArrayList<>();
        for(int i=0;i<10;i++){
            ReportOutputModel ReportOutputModel=new ReportOutputModel();
            ReportOutputModel.setProductReportID("工单号：123"+i);
            ReportOutputModel.setReportBatch("生产批次"+i);
            ReportOutputModel.setReportNum(2.0f);
            ReportOutputModel.setLastReportTime("最后报量时间：05/24 11：00");
            ReportOutputModels.add(ReportOutputModel);
        }
        return ReportOutputModels;
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvPWODetail,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(context,ReportOutputNum.class);
        startActivity(intent);

    }
}
