package com.xx.chinetek.adapter.wms.OffShelf;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.WMS.OffShelf.OutStockTaskInfo_Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class OffSehlfBillChoiceItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<OutStockTaskInfo_Model> outStockTaskInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器
    //private int selectItem = -1;
    private List<Boolean> listselected;//用布尔型的list记录每一行的选中状态
    boolean isPickingAdmin=false;

    public final class ListItemView { // 自定义控件集合

        public TextView txtTaskNo;
        public TextView txtERPVoucherNo;
        public TextView txtStrVoucherType;
        public TextView txtCompany;
        public TextView txtdepartment;
        public TextView txtPcikName;
        public TextView txtFloorName;
        public TextView txtStockLeave;
        public TextView txtVouUser;
        public TextView txtIssueType;
    }

    public OffSehlfBillChoiceItemAdapter(Context context,boolean isPickingAdmin, List<OutStockTaskInfo_Model> outStockTaskInfoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.outStockTaskInfoModels = outStockTaskInfoModels;
        this.isPickingAdmin=isPickingAdmin;
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
        return outStockTaskInfoModels==null?0: outStockTaskInfoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return outStockTaskInfoModels.get(position);
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
            listItemView.txtVouUser = (TextView) convertView.findViewById(R.id.txtVouUser);
            listItemView.txtPcikName = (TextView) convertView.findViewById(R.id.txtPcikName);
            listItemView.txtFloorName = (TextView) convertView.findViewById(R.id.txtFloorName);
            listItemView.txtStockLeave = (TextView) convertView.findViewById(R.id.txtStockLeave);
            listItemView.txtIssueType = (TextView) convertView.findViewById(R.id.txtIssueType);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        OutStockTaskInfo_Model outStockTaskInfoModel=outStockTaskInfoModels.get(selectID);
        listItemView.txtTaskNo.setText(outStockTaskInfoModel.getErpVoucherNo());
        listItemView.txtERPVoucherNo.setText(outStockTaskInfoModel.getTaskNo());
        listItemView.txtStrVoucherType.setText(outStockTaskInfoModel.getStrVoucherType());
        listItemView.txtCompany.setText("据点："+outStockTaskInfoModel.getStrongHoldName());
        listItemView.txtdepartment.setText("部门："+outStockTaskInfoModel.getDepartmentName());
        listItemView.txtPcikName.setText("拣货人:"+outStockTaskInfoModel.getPickUserName());
        listItemView.txtFloorName.setText("楼层："+outStockTaskInfoModel.getFloorName());
        listItemView.txtStockLeave.setText("货位："+outStockTaskInfoModel.getHeightAreaName());
        listItemView.txtVouUser.setText("制单人："+outStockTaskInfoModel.getVouUser());
        listItemView.txtIssueType.setText(outStockTaskInfoModel.getIssueType());
       // listItemView.txtCustoms.setText("客户名称1，客户名称2，客户名称3，客户名称4,客户名称5,客户名称6");
        if(outStockTaskInfoModel.getIsEdate().equals("1"))
            convertView.setBackgroundResource(R.color.antiquewhite);
        else if(outStockTaskInfoModel.getIsEdate().equals("2"))
            convertView.setBackgroundResource(R.color.khaki);
        else
            convertView.setBackgroundColor(Color.TRANSPARENT);

        if(isPickingAdmin){
            if(!TextUtils.isEmpty(outStockTaskInfoModel.getPickUserNo())){
                convertView.setBackgroundResource(R.color.mediumseagreen);
            }
        }

        if (getListselected().get(position)==true) {
            convertView.setBackgroundColor(Color.GREEN);
        }
        return convertView;
    }

}
