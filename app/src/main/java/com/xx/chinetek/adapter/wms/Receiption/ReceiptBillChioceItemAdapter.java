package com.xx.chinetek.adapter.wms.Receiption;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Receiption.Receipt_Model;

import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class ReceiptBillChioceItemAdapter extends BaseAdapter  {
    private Context context; // 运行上下文
    private List<Receipt_Model> receiptModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器
    private int selectItem = -1;


    public final class ListItemView { // 自定义控件集合

        public TextView txtTaskNo;
       // public TextView txtSupplierName;
        public TextView txtERPVoucherNo;
        public TextView txtStrVoucherType;
        public TextView txtCompany;
        public TextView txtdepartment;
        public TextView txtSupplierCode;
        public TextView txtSupplier;
    }

    public ReceiptBillChioceItemAdapter(Context context, List<Receipt_Model> receiptModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.receiptModels = receiptModels;

    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return receiptModels==null?0: receiptModels.size();
    }

    @Override
    public Object getItem(int position) {
        return receiptModels.get(position);
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
            convertView = listContainer.inflate(R.layout.item_receiptbillchoice_listview,null);
            listItemView.txtTaskNo = (TextView) convertView.findViewById(R.id.txtTaskNo);
           // listItemView.txtSupplierName = (TextView) convertView.findViewById(R.id.txtSupplierName);
            listItemView.txtERPVoucherNo = (TextView) convertView.findViewById(R.id.txtERPVoucherNo);
            listItemView.txtStrVoucherType = (TextView) convertView.findViewById(R.id.txtStrVoucherType);
            listItemView.txtCompany = (TextView) convertView.findViewById(R.id.txtCompany);
            listItemView.txtdepartment = (TextView) convertView.findViewById(R.id.txtdepartment);
            listItemView.txtSupplierCode = (TextView) convertView.findViewById(R.id.txtSupplierCode);
            listItemView.txtSupplier = (TextView) convertView.findViewById(R.id.txtSupplier);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        Receipt_Model receiptModel=receiptModels.get(selectID);
        listItemView.txtTaskNo.setText(receiptModel.getErpVoucherNo());
      //  listItemView.txtSupplierName.setText(receiptModel.getSupplierName());
        listItemView.txtERPVoucherNo.setText(receiptModel.getVoucherNo());
        listItemView.txtStrVoucherType.setText(receiptModel.getStrVoucherType());
        listItemView.txtCompany.setText(receiptModel.getStrongHoldName());
        listItemView.txtdepartment.setText(receiptModel.getDepartmentName());
        listItemView.txtSupplierCode.setText(receiptModel.getSupplierNo());
        listItemView.txtSupplier.setText(receiptModel.getSupplierName());
        if (selectItem == position) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.mediumseagreen));
        }else{
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }

}
