package com.xx.chinetek.adapter.wms.Receiption;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Material.BarCodeInfo;

import java.util.ArrayList;


/**
 * Created by GHOST on 2017/1/13.
 */

public class ReceiptBillDetailAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private ArrayList<BarCodeInfo> barCodeInfos; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtbarcode;
    }

    public ReceiptBillDetailAdapter(Context context, ArrayList<BarCodeInfo> barCodeInfos) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.barCodeInfos = barCodeInfos;

    }

    @Override
    public int getCount() {
        return  barCodeInfos==null?0:barCodeInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return barCodeInfos.get(position);
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
            convertView = listContainer.inflate(R.layout.item_receiptbilldetail_listview,null);
            listItemView.txtbarcode = (TextView) convertView.findViewById(R.id.txtbarcode);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        BarCodeInfo barCodeInfo=barCodeInfos.get(selectID);
        listItemView.txtbarcode.setText(barCodeInfo.getSerialNo());
        return convertView;
    }


}
