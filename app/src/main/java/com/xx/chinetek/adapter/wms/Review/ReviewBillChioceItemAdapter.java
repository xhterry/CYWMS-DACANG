package com.xx.chinetek.adapter.wms.Review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Receiption.SupplierModel;
import com.xx.chinetek.model.Review.OutStock_Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class ReviewBillChioceItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<OutStock_Model> outStockModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器
    private int selectItem = -1;
    private ArrayList<SupplierModel> mUnfilteredData;


    public final class ListItemView { // 自定义控件集合

        public TextView txtTaskNo;
        public TextView txtERPVoucherNo;
        public TextView txtStrVoucherType;
        public TextView txtCompany;
        public TextView txtdepartment;
    }

    public ReviewBillChioceItemAdapter(Context context, List<OutStock_Model> outStockModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.outStockModels = outStockModels;

    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return outStockModels==null?0: outStockModels.size();
    }

    @Override
    public Object getItem(int position) {
        return outStockModels.get(position);
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
            convertView = listContainer.inflate(R.layout.item_billchoice_listview,null);
            listItemView.txtTaskNo = (TextView) convertView.findViewById(R.id.txtTaskNo);
            listItemView.txtERPVoucherNo = (TextView) convertView.findViewById(R.id.txtERPVoucherNo);
            listItemView.txtStrVoucherType = (TextView) convertView.findViewById(R.id.txtStrVoucherType);
            listItemView.txtCompany = (TextView) convertView.findViewById(R.id.txtCompany);
            listItemView.txtdepartment = (TextView) convertView.findViewById(R.id.txtdepartment);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        OutStock_Model outStockModel=outStockModels.get(selectID);
        listItemView.txtTaskNo.setText(outStockModel.getVoucherNo());
        listItemView.txtERPVoucherNo.setText(outStockModel.getErpVoucherNo());
        listItemView.txtStrVoucherType.setText(outStockModel.getStrVoucherType());
        listItemView.txtCompany.setText(outStockModel.getStrongHoldName());
        listItemView.txtdepartment.setText(outStockModel.getDepartmentName());
        if (selectItem == position) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.mediumseagreen));
        }
        return convertView;
    }



}