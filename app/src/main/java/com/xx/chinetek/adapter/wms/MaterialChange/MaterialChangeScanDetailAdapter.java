package com.xx.chinetek.adapter.wms.MaterialChange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.WMS.Review.OutStockDetailInfo_Model;

import java.util.ArrayList;


/**
 * Created by GHOST on 2017/1/13.
 */

public class MaterialChangeScanDetailAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private ArrayList<OutStockDetailInfo_Model> outStockDetailInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtbarcode;
        public TextView txtScanNum;
        public TextView txtRemainQty;
        public TextView txtMaterialDesc;
        public TextView txtreferStock;
        public TextView txtERPVoucherNo;
    }

    public MaterialChangeScanDetailAdapter(Context context, ArrayList<OutStockDetailInfo_Model> outStockDetailInfoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.outStockDetailInfoModels = outStockDetailInfoModels;

    }

    @Override
    public int getCount() {
        return  outStockDetailInfoModels==null?0:outStockDetailInfoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return outStockDetailInfoModels.get(position);
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
            convertView = listContainer.inflate(R.layout.item_materialchangedetail_listview,null);
            listItemView.txtbarcode = (TextView) convertView.findViewById(R.id.txtbarcode);
            listItemView.txtScanNum = (TextView) convertView.findViewById(R.id.txtScanNum);
            listItemView.txtERPVoucherNo = (TextView) convertView.findViewById(R.id.txtERPVoucherNo);
            listItemView.txtreferStock = (TextView) convertView.findViewById(R.id.txtreferStock);
            listItemView.txtRemainQty = (TextView) convertView.findViewById(R.id.txtRemainQty);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
       final OutStockDetailInfo_Model outStockTaskDetailsInfoModel=outStockDetailInfoModels.get(selectID);
        listItemView.txtbarcode.setText(outStockTaskDetailsInfoModel.getMaterialNo());
        listItemView.txtScanNum.setText("扫描数："+outStockTaskDetailsInfoModel.getScanQty());
        listItemView.txtRemainQty.setText("转料数："+outStockTaskDetailsInfoModel.getOutStockQty());
        listItemView.txtreferStock.setText(outStockTaskDetailsInfoModel.getFromErpAreaNo());
        listItemView.txtERPVoucherNo.setText(outStockTaskDetailsInfoModel.getFromBatchNo());
        listItemView.txtMaterialDesc.setText(outStockTaskDetailsInfoModel.getMaterialDesc());
        if (outStockTaskDetailsInfoModel.getScanQty()!=0 &&
                outStockTaskDetailsInfoModel.getScanQty().compareTo(outStockTaskDetailsInfoModel.getOutStockQty())<0) {
            convertView.setBackgroundResource(R.color.khaki);
        }
        else if (outStockTaskDetailsInfoModel.getScanQty().compareTo(outStockTaskDetailsInfoModel.getOutStockQty())==0) {
            convertView.setBackgroundResource(R.color.springgreen);
        }
        else{
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }





}
