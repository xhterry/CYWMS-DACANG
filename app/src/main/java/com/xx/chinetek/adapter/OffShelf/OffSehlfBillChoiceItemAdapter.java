package com.xx.chinetek.adapter.OffShelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Receiption.SupplierModel;

import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class OffSehlfBillChoiceItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<SupplierModel> SupplierVoucherList; // 信息集合
    private LayoutInflater listContainer; // 视图容器
    private int selectItem = -1;


    public final class ListItemView { // 自定义控件集合

        public TextView txtTaskNo;
        public TextView txtERPVoucherNo;
        public TextView txtStrVoucherType;
        public TextView txtCompany;
        public TextView txtdepartment;
        public TextView txtCustoms;
    }

    public OffSehlfBillChoiceItemAdapter(Context context, List<SupplierModel> SupplierVoucherList) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.SupplierVoucherList = SupplierVoucherList;

    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return SupplierVoucherList==null?0: SupplierVoucherList.size();
    }

    @Override
    public Object getItem(int position) {
        return SupplierVoucherList.get(position);
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
            convertView = listContainer.inflate(R.layout.item_offshelfbillchoice_listview,null);
            listItemView.txtTaskNo = (TextView) convertView.findViewById(R.id.txtTaskNo);
            listItemView.txtERPVoucherNo = (TextView) convertView.findViewById(R.id.txtERPVoucherNo);
            listItemView.txtStrVoucherType = (TextView) convertView.findViewById(R.id.txtStrVoucherType);
            listItemView.txtCompany = (TextView) convertView.findViewById(R.id.txtCompany);
            listItemView.txtdepartment = (TextView) convertView.findViewById(R.id.txtdepartment);
            listItemView.txtCustoms = (TextView) convertView.findViewById(R.id.txtCustoms);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        SupplierModel SupplierVoucher=SupplierVoucherList.get(selectID);
        listItemView.txtTaskNo.setText(SupplierVoucher.getVoucherNo());
        listItemView.txtERPVoucherNo.setText(SupplierVoucher.getERPVoucherNo());
        listItemView.txtStrVoucherType.setText(SupplierVoucher.getStrVoucherType());
        listItemView.txtCompany.setText(SupplierVoucher.getCompany());
        listItemView.txtdepartment.setText(SupplierVoucher.getDepartment());
        listItemView.txtCustoms.setText("客户名称1，客户名称2，客户名称3，客户名称4,客户名称5,客户名称6");
        if (selectItem == position) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.mediumseagreen));
        }
        return convertView;
    }

}
