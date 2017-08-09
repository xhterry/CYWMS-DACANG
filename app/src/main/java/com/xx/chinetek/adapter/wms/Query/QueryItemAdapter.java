package com.xx.chinetek.adapter.wms.Query;

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

public class QueryItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<StockInfo_Model> stockInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtMaterialNo;
        public TextView txtAreaNo;
        public TextView txtMaterialDec;
        public TextView txtQty;
        public TextView txtBatchNo;
        public TextView txtCompany;
        public TextView txtQCStatus;
       // public TextView txtStockStatus;
    }

    public QueryItemAdapter(Context context, List<StockInfo_Model> stockInfoModels) {
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
            convertView = listContainer.inflate(R.layout.item_query_listview,null);
            listItemView.txtMaterialNo = (TextView) convertView.findViewById(R.id.txtMaterialNo);
            listItemView.txtAreaNo = (TextView) convertView.findViewById(R.id.txtAreaNo);
            listItemView.txtMaterialDec = (TextView) convertView.findViewById(R.id.txtMaterialDec);
            listItemView.txtQty = (TextView) convertView.findViewById(R.id.txtQty);
            listItemView.txtBatchNo = (TextView) convertView.findViewById(R.id.txtBatchNo);
            listItemView.txtCompany = (TextView) convertView.findViewById(R.id.txtCompany);
            listItemView.txtQCStatus = (TextView) convertView.findViewById(R.id.txtQCStatus);
           // listItemView.txtStockStatus = (TextView) convertView.findViewById(R.id.txtStockStatus);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        StockInfo_Model stockInfoModel=stockInfoModels.get(selectID);
        listItemView.txtMaterialNo.setText(stockInfoModel.getMaterialNo());
        listItemView.txtAreaNo.setText("库位："+stockInfoModel.getAreaNo());
        listItemView.txtMaterialDec.setText(stockInfoModel.getMaterialDesc());
        listItemView.txtQty.setText("数量："+stockInfoModel.getQty()+"");
        listItemView.txtBatchNo.setText("批："+stockInfoModel.getBatchNo());
        listItemView.txtCompany.setText("据："+stockInfoModel.getStrongHoldName());
        listItemView.txtQCStatus.setText(stockInfoModel.getStrStatus());
       // listItemView.txtStockStatus.setText("库存状态");
        return convertView;
    }


}
