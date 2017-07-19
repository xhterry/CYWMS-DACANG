package com.xx.chinetek.adapter.product.Receiption;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.WMS.OffShelf.OutStockTaskDetailsInfo_Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class ReceiptionScanItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<OutStockTaskDetailsInfo_Model> outStockTaskDetailsInfo_models; // 信息集合
    private LayoutInflater listContainer; // 视图容器
    //private int selectItem = -1;
    private List<Boolean> listselected;//用布尔型的list记录每一行的选中状态

    public final class ListItemView { // 自定义控件集合

        public TextView txtTaskNo;
        public TextView txtERPVoucherNo;
        public TextView txtStrVoucherType;
        public TextView txtCompany;
        public TextView txtdepartment;
        public TextView txtCustoms;
    }

    public ReceiptionScanItemAdapter(Context context, List<OutStockTaskDetailsInfo_Model> outStockTaskDetailsInfo_models) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.outStockTaskDetailsInfo_models = outStockTaskDetailsInfo_models;
        this.setListselected(new ArrayList<Boolean>(getCount()));
        for(int i=0;i<getCount();i++)
            getListselected().add(false);//初始为false，长度和listview一样
    }

    public List<Boolean> getListselected() {
        return listselected;
    }
    public void setListselected(List<Boolean> listselected) {
        this.listselected = listselected;
    }

    public Boolean getStates(int position){
        return getListselected().get(position);
    }

    public void modifyStates(int position){
        if(getListselected().get(position)==false){
            getListselected().set(position, true);//如果相应position的记录是未被选中则设置为选中（true）
            notifyDataSetChanged();
        }else{
            getListselected().set(position, false);//否则相应position的记录是被选中则设置为未选中（false）
            notifyDataSetChanged();}
    }

//    public void setSelectItem(int selectItem) {
//        this.selectItem = selectItem;
//    }

    @Override
    public int getCount() {
        return outStockTaskDetailsInfo_models==null?0: outStockTaskDetailsInfo_models.size();
    }

    @Override
    public Object getItem(int position) {
        return outStockTaskDetailsInfo_models.get(position);
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
        OutStockTaskDetailsInfo_Model outStockTaskDetailsInfoModel=outStockTaskDetailsInfo_models.get(selectID);
        listItemView.txtTaskNo.setText(outStockTaskDetailsInfoModel.getTaskNo());
        listItemView.txtERPVoucherNo.setText(outStockTaskDetailsInfoModel.getErpVoucherNo());
        listItemView.txtStrVoucherType.setText(outStockTaskDetailsInfoModel.getStrVoucherType());
        //listItemView.txtCompany.setText(outStockTaskInfoModel.getCompany());
       // listItemView.txtdepartment.setText(outStockTaskInfoModel.getDepartment());
        listItemView.txtCustoms.setText("客户名称1，客户名称2，客户名称3，客户名称4,客户名称5,客户名称6");
        if (getListselected().get(position)==false) {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
        else {
            convertView.setBackgroundColor(Color.GREEN);
        }
        return convertView;
    }

}
