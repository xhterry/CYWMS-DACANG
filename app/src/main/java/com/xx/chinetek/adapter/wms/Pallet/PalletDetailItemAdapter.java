package com.xx.chinetek.adapter.wms.Pallet;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Pallet.PalletDetail_Model;

import java.util.List;

/**
 * Created by GHOST on 2016/11/28.
 */

public class PalletDetailItemAdapter extends BaseExpandableListAdapter {

    private Context context;

        List<PalletDetail_Model> palletDetailModels;

    public PalletDetailItemAdapter(Context context, List<PalletDetail_Model> palletDetailModels) {
        this.context = context;
        this.palletDetailModels=palletDetailModels;

    }


    TextView getTextView(){
        AbsListView.LayoutParams lp=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,64);
        TextView textView=new TextView(context);
        //设置 textView控件的布局
        textView.setLayoutParams(lp);
        //设置该textView中的内容相对于textView的位置
        textView.setGravity(Gravity.CENTER_VERTICAL);
        //设置txtView的内边距
        textView.setPadding(80, 0, 0, 0);
        //设置文本颜色
        textView.setTextColor(Color.BLACK);
        return textView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }
    //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //定义一个LinearLayout用于存放ImageView、TextView
        LinearLayout ll=new LinearLayout(context);
        //设置子控件的显示方式为水平
        //定义一个ImageView用于显示列表图片
        TextView textView=getTextView();
        textView.setTextSize(25);
        textView.setBackgroundColor(context.getResources().getColor(R.color.lightblue));
        textView.setText("托盘号："+palletDetailModels.get(groupPosition).getPalletNo());
        ll.addView(textView);
        return ll;
    }
    //取得指定分组的ID.该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）.
    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }
    //取得分组数
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return  palletDetailModels==null?0:palletDetailModels.size();
    }
    //取得与给定分组关联的数据
    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return palletDetailModels.get(groupPosition);
    }
    //取得指定分组的子元素数.
    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return palletDetailModels.get(groupPosition).getLstBarCode().size();
    }
    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //定义一个LinearLayout用于存放ImageView、TextView
        LinearLayout ll=new LinearLayout(context);
        //设置子控件的显示方式为水平
        //设置logo的大小
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(40, 40);
        TextView textView=getTextView();
        textView.setTextSize(18);
        textView.setBackgroundColor(context.getResources().getColor(R.color.lightgray));
        textView.setText(palletDetailModels.get(groupPosition).getLstBarCode().get(childPosition).getSerialNo());
        ll.addView(textView);
        return ll;
    }
    //取得给定分组中给定子视图的ID. 该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return palletDetailModels.get(groupPosition).getLstBarCode().get(childPosition);
    }
}


