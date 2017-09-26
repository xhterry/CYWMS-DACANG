package com.xx.chinetek.adapter.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.xx.chinetek.adapter.product.BillsStockIn.BillAdapter;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Production.Wo.WoModel;

import java.util.ArrayList;

/**
 * Created by GHOST on 2017/1/13.
 */

public class WoBillChioceItemAdapter extends BaseAdapter implements Filterable {
    private Context context; // 运行上下文
    private ArrayList<WoModel> woModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器
    private ArrayList<WoModel> mUnfilteredData;
    private ArrayFilter mFilter;


    public final class ListItemView { // 自定义控件集合

        public TextView txtTaskNo;
        public TextView txtERPVoucherNo;
        public TextView txtStrVoucherType;
        public TextView txtCompany;
        public TextView txtdepartment;
    }

    public WoBillChioceItemAdapter(Context context, ArrayList<WoModel> woModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.woModels = woModels;

    }

    @Override
    public int getCount() {
        return woModels==null?0: woModels.size();
    }

    @Override
    public Object getItem(int position) {
        return woModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new WoBillChioceItemAdapter.ArrayFilter();
        }
        return mFilter;
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
        WoModel woModel=woModels.get(selectID);
        listItemView.txtTaskNo.setText(woModel.getErpVoucherNo());
        listItemView.txtERPVoucherNo.setText(woModel.getVoucherNo());
        listItemView.txtStrVoucherType.setText(woModel.getStrVoucherType());
        listItemView.txtCompany.setText(woModel.getStrongHoldName());
        listItemView.txtdepartment.setText(woModel.getDepartmentName());
        return convertView;
    }


    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData =  new ArrayList<WoModel>(woModels);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<WoModel> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<WoModel> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<WoModel> newValues = new ArrayList<WoModel>(count);

                for (int i = 0; i < count; i++) {
                    WoModel pc = unfilteredValues.get(i);
                    if (pc != null) {
                        int len =pc.getErpVoucherNo().toUpperCase().length();
                        int LenPre = prefixString.toUpperCase().length();
                        if (LenPre<=8)
                        {
                            if(pc.getErpVoucherNo().toUpperCase().substring(len-8,len).startsWith(prefixString.toUpperCase())){
                                newValues.add(pc);
                            }
                        }
                        if (LenPre==18)
                        {
                            if(pc.getErpVoucherNo().toUpperCase().substring(0,len).startsWith(prefixString.toUpperCase())){
                                newValues.add(pc);
                            }
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
            woModels = (ArrayList<WoModel>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }



}
