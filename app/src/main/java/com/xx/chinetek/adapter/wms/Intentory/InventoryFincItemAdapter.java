package com.xx.chinetek.adapter.wms.Intentory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;

import java.util.ArrayList;


/**
 * Created by GHOST on 2017/1/13.
 */

public class InventoryFincItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private ArrayList<Barcode_Model> barcodeModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtMaterialNo;
        public TextView txtSerialNo;
        public TextView txtQty;
        public TextView txtWarehouseno;
        public TextView txtAreano;
        public TextView txtBatchNo;
        public TextView txtStrongHoldCode;
    }

    public InventoryFincItemAdapter(Context context, ArrayList<Barcode_Model> barcodeModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.barcodeModels = barcodeModels;

    }

    @Override
    public int getCount() {
        return barcodeModels==null?0:barcodeModels.size();
    }

    @Override
    public Object getItem(int position) {
        return barcodeModels.get(position);
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
            convertView = listContainer.inflate(R.layout.item_inventoryfinc_listview,null);
            listItemView.txtMaterialNo = (TextView) convertView.findViewById(R.id.txtMaterialNo);
            listItemView.txtQty = (TextView) convertView.findViewById(R.id.txtQty);
            listItemView.txtSerialNo = (TextView) convertView.findViewById(R.id.txtSerialNo);
            listItemView.txtWarehouseno = (TextView) convertView.findViewById(R.id.txtWarehouseno);
            listItemView.txtAreano = (TextView) convertView.findViewById(R.id.txtAreano);
            listItemView.txtStrongHoldCode = (TextView) convertView.findViewById(R.id.txtStrongHoldCode);
            listItemView.txtBatchNo = (TextView) convertView.findViewById(R.id.txtBatchNo);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        Barcode_Model barcodeModel=barcodeModels.get(selectID);
        listItemView.txtMaterialNo.setText(barcodeModel.getMaterialNo());
        listItemView.txtSerialNo.setText(barcodeModel.getSerialNo());
        listItemView.txtWarehouseno.setText(barcodeModel.getWarehouseno());
        listItemView.txtAreano.setText(barcodeModel.getAreano());
        listItemView.txtStrongHoldCode.setText(barcodeModel.getStrongHoldCode());
        listItemView.txtQty.setText("数量："+barcodeModel.getQty());
        listItemView.txtBatchNo.setText(barcodeModel.getBatchNo());


        return convertView;
    }


}
