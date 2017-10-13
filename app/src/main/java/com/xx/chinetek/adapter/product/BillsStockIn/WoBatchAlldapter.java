package com.xx.chinetek.adapter.product.BillsStockIn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Production.Wo.WoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class WoBatchAlldapter extends BaseAdapter  implements Filterable {
    private Context context; // 运行上下文
    private List<WoModel> WoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器
    private int selectItem = -1;
    private ArrayList<WoModel> mUnfilteredData;
    private ArrayFilter mFilter;

    public final class ListItemView { // 自定义控件集合

        public TextView txtTaskNo;
        public TextView txtERPVoucherNo;
        public TextView txtStrVoucherType;
        public TextView txtCompany;
    }

    public WoBatchAlldapter(Context context, List<WoModel> WoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.WoModels = WoModels;

    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return WoModels==null?0: WoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return WoModels.get(position);
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
            convertView = listContainer.inflate(R.layout.item_billin_listview,null);
            listItemView.txtTaskNo = (TextView) convertView.findViewById(R.id.txtERPVoucherNo);
            listItemView.txtERPVoucherNo = (TextView) convertView.findViewById(R.id.txtTaskNo);
            listItemView.txtStrVoucherType = (TextView) convertView.findViewById(R.id.txtStrVoucherType);
            listItemView.txtCompany = (TextView) convertView.findViewById(R.id.txtCompany);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        WoModel woModel=WoModels.get(selectID);
//        listItemView.txtTaskNo.setText("批次："+woModel.getBatchNo().toString());
        listItemView.txtERPVoucherNo.setText("批次："+woModel.getBatchNo().toString());
//        listItemView.txtStrVoucherType.setText(woModel.getStrVoucherType());
        listItemView.txtCompany.setText("完工量：" + woModel.getScanReportQty().toString());
        if(String.valueOf(woModel.getStatus()).equals("2")){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.red));
        }else{
            convertView.setBackgroundColor(context.getResources().getColor(R.color.mediumseagreen));
        }
//        if (selectItem == position) {
//            convertView.setBackgroundColor(context.getResources().getColor(R.color.mediumseagreen));
//        }
        return convertView;
    }


    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new WoBatchAlldapter.ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData =  new ArrayList<WoModel>(WoModels);
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
            WoModels = (ArrayList<WoModel>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
