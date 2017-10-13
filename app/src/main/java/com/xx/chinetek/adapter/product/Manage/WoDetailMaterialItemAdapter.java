package com.xx.chinetek.adapter.product.Manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Production.Wo.WoDetailModel;

import java.util.ArrayList;

/**
 * Created by GHOST on 2017/1/13.
 */

public class WoDetailMaterialItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private ArrayList<WoDetailModel> woDetailModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtMaterialNo;
        public TextView txtBatchNo;
        public TextView txtMaterialDesc;
        public TextView txtWoQty;
        public TextView txtScanQty;
    }

    public WoDetailMaterialItemAdapter(Context context, ArrayList<WoDetailModel> woDetailModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.woDetailModels = woDetailModels;
    }


    @Override
    public int getCount() {
        return woDetailModels==null?0: woDetailModels.size();
    }

    @Override
    public Object getItem(int position) {
        return woDetailModels.get(position);
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
            convertView = listContainer.inflate(R.layout.item_prod_womaterialitem_listview,null);
            listItemView.txtMaterialNo = (TextView) convertView.findViewById(R.id.txtMaterialNo);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
            listItemView.txtBatchNo = (TextView) convertView.findViewById(R.id.txtBatchNo);
            listItemView.txtWoQty = (TextView) convertView.findViewById(R.id.txtWoQty);
            listItemView.txtScanQty = (TextView) convertView.findViewById(R.id.txtScanQty);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        WoDetailModel woDetailModel=woDetailModels.get(selectID);
        listItemView.txtMaterialNo.setText(woDetailModel.getMaterialNo());
        listItemView.txtMaterialDesc.setText(woDetailModel.getMaterialDesc());
        listItemView.txtBatchNo.setText(woDetailModel.getFromBatchNo());
        listItemView.txtWoQty.setText((woDetailModel.getWoQty()==null?"0":woDetailModel.getWoQty())+"");
        listItemView.txtScanQty.setText((woDetailModel.getScanQty()==null?"0":woDetailModel.getScanQty())+"");
        if ((woDetailModel.getScanQty()==null?"0":woDetailModel.getScanQty())!="0" &&
                (woDetailModel.getWoQty()==null?"0":woDetailModel.getWoQty().toString()).compareTo((woDetailModel.getScanQty()==null?"0":woDetailModel.getScanQty()).toString())<0) {
            convertView.setBackgroundResource(R.color.khaki);
        }
        else if ((woDetailModel.getWoQty()==null?"0":woDetailModel.getWoQty().toString()).compareTo((woDetailModel.getScanQty()==null?"0":woDetailModel.getScanQty()).toString())==0) {
            convertView.setBackgroundResource(R.color.springgreen);
        }
        else{
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }

}
