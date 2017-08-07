package com.xx.chinetek.adapter.wms.Intentory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.WMS.Inventory.CheckArea_Model;

import java.util.ArrayList;

/**
 * Created by GHOST on 2017/1/13.
 */

public class InventoryAddItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private ArrayList<CheckArea_Model> checkAreaModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtAreaNo;
        public TextView txtWareHouse;
    }

    public InventoryAddItemAdapter(Context context, ArrayList<CheckArea_Model> checkAreaModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.checkAreaModels = checkAreaModels;

    }

    @Override
    public int getCount() {
        return checkAreaModels==null?0:checkAreaModels.size();
    }

    @Override
    public Object getItem(int position) {
        return checkAreaModels.get(position);
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
            convertView = listContainer.inflate(R.layout.item_inventoryadd_listview,null);
            listItemView.txtAreaNo = (TextView) convertView.findViewById(R.id.txtAreaNo);
            listItemView.txtWareHouse = (TextView) convertView.findViewById(R.id.txtWareHouse);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        CheckArea_Model checkAreaModel=checkAreaModels.get(selectID);
        listItemView.txtAreaNo.setText(checkAreaModel.getAREANO());
        listItemView.txtWareHouse.setText(checkAreaModel.getADDRESS());
        return convertView;
    }


}
