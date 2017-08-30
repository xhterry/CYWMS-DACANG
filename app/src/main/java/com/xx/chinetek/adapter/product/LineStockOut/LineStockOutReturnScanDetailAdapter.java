package com.xx.chinetek.adapter.product.LineStockOut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Production.LineStockOut.LineStockOutReturnDetail_Model;

import java.util.List;


/**
 * Created by GHOST on 2017/1/13.
 */

public class LineStockOutReturnScanDetailAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<LineStockOutReturnDetail_Model> receiptDetailModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtbarcode;
        public TextView txtScanNum;
        public TextView txtRemainQty;
        public TextView txtMaterialDesc;
    }

    public LineStockOutReturnScanDetailAdapter(Context context, List<LineStockOutReturnDetail_Model> receiptDetailModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.receiptDetailModels = receiptDetailModels;

    }

    @Override
    public int getCount() {
        return  receiptDetailModels==null?0:receiptDetailModels.size();
    }

    @Override
    public Object getItem(int position) {
        return receiptDetailModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        // 自定义视图
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_receiptscandetail_listview,null);
            listItemView.txtbarcode = (TextView) convertView.findViewById(R.id.txtbarcode);
            listItemView.txtScanNum = (TextView) convertView.findViewById(R.id.txtScanNum);
            listItemView.txtRemainQty = (TextView) convertView.findViewById(R.id.txtRemainQty);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        LineStockOutReturnDetail_Model receiptDetailModel=receiptDetailModels.get(selectID);
        listItemView.txtbarcode.setText(receiptDetailModel.getMaterialNo());
        listItemView.txtScanNum.setText("扫描数："+receiptDetailModel.getScanQty());
        listItemView.txtRemainQty.setText("退货数："+receiptDetailModel.getRemainQty());
        listItemView.txtMaterialDesc.setText(receiptDetailModel.getMaterialDesc());
        if (receiptDetailModel.getScanQty()!=0 &&
                receiptDetailModel.getScanQty().compareTo(receiptDetailModel.getRemainQty())<0) {
            convertView.setBackgroundResource(R.color.khaki);
        }
        else if (receiptDetailModel.getScanQty().compareTo(receiptDetailModel.getRemainQty())==0) {
            convertView.setBackgroundResource(R.color.springgreen);
        }else{
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }


}
