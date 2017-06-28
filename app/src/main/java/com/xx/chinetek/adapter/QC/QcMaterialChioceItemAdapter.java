package com.xx.chinetek.adapter.QC;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Receiption.SupplierModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class QcMaterialChioceItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<SupplierModel> SupplierVoucherList; // 信息集合
    private LayoutInflater listContainer; // 视图容器
    private int selectItem = -1;
    private ArrayList<SupplierModel> mUnfilteredData;


    public final class ListItemView { // 自定义控件集合

        public TextView txtMaterialNo;
        public TextView txtReceiptNum;
        public TextView txtMaterialName;
        public TextView txtReceiptNo;
    }

    public QcMaterialChioceItemAdapter(Context context, List<SupplierModel> SupplierVoucherList) {
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
            convertView = listContainer.inflate(R.layout.item_qcmaterialchoice_listview,null);
            listItemView.txtMaterialNo = (TextView) convertView.findViewById(R.id.txtMaterialNo);
            listItemView.txtReceiptNum = (TextView) convertView.findViewById(R.id.txtReceiptNum);
            listItemView.txtMaterialName = (TextView) convertView.findViewById(R.id.txtMaterialName);
            listItemView.txtReceiptNo = (TextView) convertView.findViewById(R.id.txtReceiptNo);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        SupplierModel SupplierVoucher=SupplierVoucherList.get(selectID);
        listItemView.txtMaterialNo.setText("物料编码");
        listItemView.txtReceiptNum.setText("收货数量");
        listItemView.txtMaterialName.setText("物料名称");
        listItemView.txtReceiptNo.setText("收货单号");
        if (selectItem == position) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.mediumseagreen));
        }
        return convertView;
    }

}
