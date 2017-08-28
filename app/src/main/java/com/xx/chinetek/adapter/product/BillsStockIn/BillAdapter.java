package com.xx.chinetek.adapter.product.BillsStockIn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.Receiption.SupplierModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class BillAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<WoModel> WoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器
    private int selectItem = -1;
    private ArrayList<WoModel> mUnfilteredData;


    public final class ListItemView { // 自定义控件集合

        public TextView txtTaskNo;
        public TextView txtERPVoucherNo;
        public TextView txtStrVoucherType;
        public TextView txtCompany;
    }

    public BillAdapter(Context context, List<WoModel> WoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.WoModels = WoModels;

    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return WoModels==null?0: WoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return WoModels.get(position);
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
            convertView = listContainer.inflate(R.layout.item_billin_listview,null);
            listItemView.txtTaskNo = (TextView) convertView.findViewById(R.id.txtTaskNo);
            listItemView.txtERPVoucherNo = (TextView) convertView.findViewById(R.id.txtERPVoucherNo);
            listItemView.txtStrVoucherType = (TextView) convertView.findViewById(R.id.txtStrVoucherType);
            listItemView.txtCompany = (TextView) convertView.findViewById(R.id.txtCompany);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        WoModel woModel=WoModels.get(selectID);
        listItemView.txtTaskNo.setText(woModel.getVoucherNo());
        listItemView.txtERPVoucherNo.setText(woModel.getErpVoucherNo());
        listItemView.txtStrVoucherType.setText(woModel.getStrVoucherType());
        listItemView.txtCompany.setText(woModel.getMaterialDesc());
        if (selectItem == position) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.mediumseagreen));
        }
        return convertView;
    }



}
