package com.xx.chinetek.adapter.wms.Pallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Material.BarCodeInfo;

import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class PalletItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<BarCodeInfo> barCodeInfoList; // 信息集合
    private LayoutInflater listContainer; // 视图容器


    public final class ListItemView { // 自定义控件集合
        public TextView txtBarcode;
        public TextView txtMaterialName;
        public TextView txtBatch;
    }

    public PalletItemAdapter(Context context, List<BarCodeInfo> barCodeInfoList) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.barCodeInfoList = barCodeInfoList;

    }


    @Override
    public int getCount() {
        return barCodeInfoList==null?0: barCodeInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return barCodeInfoList.get(position);
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
            convertView = listContainer.inflate(R.layout.item_pallet_listview,null);
            listItemView.txtBarcode = (TextView) convertView.findViewById(R.id.item_Barcode);
            listItemView.txtMaterialName = (TextView) convertView.findViewById(R.id.item_MattterialName);
            listItemView.txtBatch = (TextView) convertView.findViewById(R.id.item_Batch);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        BarCodeInfo barCodeInfo=barCodeInfoList.get(selectID);
        listItemView.txtBarcode.setText(barCodeInfo.getSerialNo());
        listItemView.txtMaterialName.setText(barCodeInfo.getMaterialDesc());
        listItemView.txtBatch.setText(barCodeInfo.getBatchNo());
        return convertView;
    }




}
