package com.xx.chinetek.adapter.product.Manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Production.Manage.LineManageModel;

import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class LineManageItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<LineManageModel> lineManageModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtWONo;
        public TextView txtBatchNo;
        public TextView txtERPVoucherNo;
        public TextView txtEquipID;
        public TextView txtTeamNo;
        public TextView txtLineNo;
    }

    public LineManageItemAdapter(Context context, List<LineManageModel> lineManageModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.lineManageModels = lineManageModels;
    }


    @Override
    public int getCount() {
        return lineManageModels==null?0: lineManageModels.size();
    }

    @Override
    public Object getItem(int position) {
        return lineManageModels.get(position);
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
            convertView = listContainer.inflate(R.layout.item_prod_linemanageitem_listview,null);
            listItemView.txtWONo = (TextView) convertView.findViewById(R.id.txtWONo);
            listItemView.txtERPVoucherNo = (TextView) convertView.findViewById(R.id.txtERPVoucherNo);
            listItemView.txtBatchNo = (TextView) convertView.findViewById(R.id.txtBatchNo);
            listItemView.txtEquipID = (TextView) convertView.findViewById(R.id.txtEquipID);
            listItemView.txtTeamNo = (TextView) convertView.findViewById(R.id.txtTeamNo);
            listItemView.txtLineNo = (TextView) convertView.findViewById(R.id.txtLineNo);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        LineManageModel lineManageModel=lineManageModels.get(selectID);
        listItemView.txtWONo.setText(lineManageModel.getWoVoucherNo());
        listItemView.txtERPVoucherNo.setText(lineManageModel.getWoErpVoucherNo());
        listItemView.txtBatchNo.setText(lineManageModel.getWoBatchNo());
        listItemView.txtEquipID.setText(lineManageModel.getEquipID());
        listItemView.txtTeamNo.setText(lineManageModel.getProductTeamNo());
        listItemView.txtLineNo.setText(lineManageModel.getProductLineNo());
        return convertView;
    }

}
