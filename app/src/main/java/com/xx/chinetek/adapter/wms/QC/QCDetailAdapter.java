package com.xx.chinetek.adapter.wms.QC;

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

public class QCDetailAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<StockInfo_Model> stockInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtbarcode;
        public TextView txtScanNum;
        public TextView txtMaterialDesc;
    }

    public QCDetailAdapter(Context context, List<StockInfo_Model> stockInfoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.stockInfoModels = stockInfoModels;

    }

    @Override
    public int getCount() {
        return  stockInfoModels==null?0:stockInfoModels.size();
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
            convertView = listContainer.inflate(R.layout.item_qcscandetail_listview,null);
            listItemView.txtbarcode = (TextView) convertView.findViewById(R.id.txtbarcode);
            listItemView.txtScanNum = (TextView) convertView.findViewById(R.id.txtScanNum);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        StockInfo_Model stockInfoModel=stockInfoModels.get(selectID);
        listItemView.txtbarcode.setText(stockInfoModel.getMaterialNo());
        listItemView.txtScanNum.setText("取样数："+(stockInfoModel.getPickModel()==3?stockInfoModel.getAmountQty():stockInfoModel.getQty()));
        listItemView.txtMaterialDesc.setText(stockInfoModel.getMaterialDesc());
        return convertView;
    }


}
