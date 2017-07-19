package com.xx.chinetek.adapter.wms.MaterialChange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;

import java.util.List;


/**
 * Created by GHOST on 2017/1/13.
 */

public class MaterialChangeAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<StockInfo_Model> stockInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView list_Batch;
        public TextView list_SerialNo;
        public TextView list_MaterialDesc;
    }

    public MaterialChangeAdapter(Context context, List<StockInfo_Model> stockInfoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.stockInfoModels = stockInfoModels;

    }

    @Override
    public int getCount() {
        return stockInfoModels==null?0:stockInfoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return stockInfoModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        // 自定义视图
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();

            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_product_materialchange_listview,null);
            listItemView.list_Batch = (TextView) convertView.findViewById(R.id.list_Batch);
            listItemView.list_SerialNo = (TextView) convertView.findViewById(R.id.list_SerialNo);
            listItemView.list_MaterialDesc = (TextView) convertView.findViewById(R.id.list_MaterialDesc);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        StockInfo_Model stockInfoModel=stockInfoModels.get(selectID);
        listItemView.list_Batch.setText(stockInfoModel.getBatchNo());
        listItemView.list_SerialNo.setText(stockInfoModel.getSerialNo());
        listItemView.list_MaterialDesc.setText(stockInfoModel.getMaterialDesc());
        return convertView;
    }


}
