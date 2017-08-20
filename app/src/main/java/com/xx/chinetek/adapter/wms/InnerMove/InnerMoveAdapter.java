package com.xx.chinetek.adapter.wms.InnerMove;

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

public class InnerMoveAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<StockInfo_Model> stockInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView list_OutLoacl;
        public TextView list_MaterialNo;
        public TextView list_SerialNo;
        public TextView list_MaterialDesc;
       // public TextView list_qty;
    }

    public InnerMoveAdapter(Context context, List<StockInfo_Model> stockInfoModels) {
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
            convertView = listContainer.inflate(R.layout.item_innermove_listview,null);
            listItemView.list_OutLoacl = (TextView) convertView.findViewById(R.id.list_OutLoacl);
            listItemView.list_SerialNo = (TextView) convertView.findViewById(R.id.list_SerialNo);
            listItemView.list_MaterialDesc = (TextView) convertView.findViewById(R.id.list_MaterialDesc);
            listItemView.list_MaterialNo = (TextView) convertView.findViewById(R.id.list_MaterialNo);
            //listItemView.list_qty = (TextView) convertView.findViewById(R.id.list_qty);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        StockInfo_Model stockInfoModel=stockInfoModels.get(selectID);
        listItemView.list_OutLoacl.setText("移出库:"+stockInfoModel.getFromAreaNo());
        //listItemView.list_SerialNo.setText("箱号:"+stockInfoModel.getSerialNo());
        listItemView.list_MaterialDesc.setText(stockInfoModel.getMaterialDesc()==null || stockInfoModel.getMaterialDesc().equals("")?stockInfoModel.getMaterialNo():stockInfoModel.getMaterialDesc());
        listItemView.list_MaterialNo.setText(stockInfoModel.getMaterialNo());
        listItemView.list_SerialNo.setText("合计："+stockInfoModel.getQty().toString());
        return convertView;
    }
}
