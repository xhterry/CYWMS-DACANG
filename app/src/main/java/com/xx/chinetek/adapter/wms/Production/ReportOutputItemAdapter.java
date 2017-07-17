package com.xx.chinetek.adapter.wms.Production;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Production.ReportOutputModel;

import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class ReportOutputItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<ReportOutputModel> ReportOutputModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器


    public final class ListItemView { // 自定义控件集合

        public TextView txtTaskNo;
        public TextView txtBatchNo;
        public TextView txtReportNum;
        public TextView txtLastReportTime;
    }

    public ReportOutputItemAdapter(Context context, List<ReportOutputModel> ReportOutputModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.ReportOutputModels = ReportOutputModels;

    }


    @Override
    public int getCount() {
        return ReportOutputModels==null?0: ReportOutputModels.size();
    }

    @Override
    public Object getItem(int position) {
        return ReportOutputModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        // 自定义视图
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();

            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_reportoutput_listview,null);
            listItemView.txtTaskNo = (TextView) convertView.findViewById(R.id.txtTaskNo);
            listItemView.txtBatchNo = (TextView) convertView.findViewById(R.id.txtBatchNo);
            listItemView.txtReportNum = (TextView) convertView.findViewById(R.id.txtReportNum);
            listItemView.txtLastReportTime = (TextView) convertView.findViewById(R.id.txtLastReportTime);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        ReportOutputModel ReportOutputModel=ReportOutputModels.get(selectID);
        listItemView.txtTaskNo.setText(ReportOutputModel.getProductReportID());
        listItemView.txtBatchNo.setText(ReportOutputModel.getReportBatch());
        listItemView.txtReportNum.setText(ReportOutputModel.getReportNum().toString());
        listItemView.txtLastReportTime.setText(ReportOutputModel.getLastReportTime());
        return convertView;
    }




}
