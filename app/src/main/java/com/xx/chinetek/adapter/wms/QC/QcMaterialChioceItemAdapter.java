package com.xx.chinetek.adapter.wms.QC;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.QC.QualityInfo_Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class QcMaterialChioceItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<QualityInfo_Model> qualityInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器
    private List<Boolean> listselected;//用布尔型的list记录每一行的选中状态

    public final class ListItemView { // 自定义控件集合

        public TextView txtMaterialNo;
        public TextView txtReceiptNum;
        public TextView txtMaterialName;
        public TextView txtReceiptNo;
    }

    public QcMaterialChioceItemAdapter(Context context, List<QualityInfo_Model> qualityInfoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.qualityInfoModels = qualityInfoModels;
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

    @Override
    public int getCount() {
        return qualityInfoModels==null?0: qualityInfoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return qualityInfoModels.get(position);
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
        QualityInfo_Model qualityInfoModel=qualityInfoModels.get(selectID);
        listItemView.txtMaterialNo.setText(qualityInfoModel.getMaterialNo());
        listItemView.txtReceiptNum.setText(qualityInfoModel.getInSQty()+"");
        listItemView.txtMaterialName.setText(qualityInfoModel.getMaterialDesc());
        listItemView.txtReceiptNo.setText(qualityInfoModel.getErpVoucherNo());
        if (getListselected().get(position)==false) {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
        else {
            convertView.setBackgroundColor(Color.GREEN);
        }
        return convertView;
    }

}
