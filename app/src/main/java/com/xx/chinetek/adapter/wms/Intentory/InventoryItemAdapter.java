package com.xx.chinetek.adapter.wms.Intentory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.WMS.Inventory.Check_Model;

import java.util.ArrayList;

/**
 * Created by GHOST on 2017/1/13.
 */

public class InventoryItemAdapter extends BaseAdapter implements Filterable {
    private ArrayFilter mFilter;
    private ArrayList<Check_Model> mUnfilteredData;
    private Context context; // 运行上下文
    private ArrayList<Check_Model> check_models; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtTaskNo;
        public TextView txtCheckDesc;
        public TextView txtCheckStatus;
    }

    public InventoryItemAdapter(Context context, ArrayList<Check_Model> check_models) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.check_models = check_models;

    }

    @Override
    public int getCount() {
        return check_models==null?0:check_models.size();
    }

    @Override
    public Object getItem(int position) {
        return check_models.get(position);
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
            convertView = listContainer.inflate(R.layout.item_inventory_listview,null);
            listItemView.txtTaskNo = (TextView) convertView.findViewById(R.id.txtTaskNo);
            listItemView.txtCheckDesc = (TextView) convertView.findViewById(R.id.txtCheckDesc);
            listItemView.txtCheckStatus = (TextView) convertView.findViewById(R.id.txtCheckStatus);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        Check_Model check_model=check_models.get(selectID);
        listItemView.txtTaskNo.setText(check_model.getCHECKNO());
        listItemView.txtCheckDesc.setText(check_model.getREMARKS());
        listItemView.txtCheckStatus.setText(check_model.getCHECKSTATUS());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<Check_Model>(check_models);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<Check_Model> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<Check_Model> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<Check_Model> newValues = new ArrayList<Check_Model>(count);

                for (int i = 0; i < count; i++) {
                    Check_Model pc = unfilteredValues.get(i);
                    if (pc != null) {

                        if(pc.getCHECKNO()!=null && pc.getCHECKNO().substring(pc.getCHECKNO().length()-5).startsWith(prefixString.toUpperCase())){

                            newValues.add(pc);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            //noinspection unchecked
            check_models = (ArrayList<Check_Model>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
