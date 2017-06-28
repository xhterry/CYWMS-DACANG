package com.xx.chinetek.adapter.Intentory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Inventory.CheckDet_Model;

import java.util.ArrayList;


/**
 * Created by GHOST on 2017/1/13.
 */

public class InventoryScanItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private ArrayList<CheckDet_Model> check_models; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtMaterialNo;
        public TextView txtSerialNo;
        public TextView txtQty;
        public TextView txtMaterialDec;
    }

    public InventoryScanItemAdapter(Context context, ArrayList<CheckDet_Model> check_models) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.check_models = check_models;

    }

    @Override
    public int getCount() {
        return check_models==null?0:check_models.size();
    }

    @Override
    public Object getItem(int position) {
        return check_models.get(position);
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
            convertView = listContainer.inflate(R.layout.item_inventoryscan_listview,null);
            listItemView.txtMaterialNo = (TextView) convertView.findViewById(R.id.txtMaterialNo);
            listItemView.txtQty = (TextView) convertView.findViewById(R.id.txtQty);
            listItemView.txtMaterialDec = (TextView) convertView.findViewById(R.id.txtMaterialDec);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        CheckDet_Model check_model=check_models.get(selectID);
        listItemView.txtMaterialNo.setText(check_model.getMATERIALNO());
        listItemView.txtMaterialDec.setText(check_model.getMATERIALDESC());
        listItemView.txtQty.setText(check_model.getQTY()+"");
        return convertView;
    }


}
