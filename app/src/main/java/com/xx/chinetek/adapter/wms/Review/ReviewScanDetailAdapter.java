package com.xx.chinetek.adapter.wms.Review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.WMS.Review.OutStockDetailInfo_Model;

import java.util.List;


/**
 * Created by GHOST on 2017/1/13.
 */

public class ReviewScanDetailAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<OutStockDetailInfo_Model> outStockDetailInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtbarcode;
        public TextView txtScanNum;
        public TextView txtRemainQty;
        public TextView txtMaterialDesc;
    }

    public ReviewScanDetailAdapter(Context context, List<OutStockDetailInfo_Model> outStockDetailInfoModels) {
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
            convertView = listContainer.inflate(R.layout.item_receiptscandetail_listview,null);
            listItemView.txtbarcode = (TextView) convertView.findViewById(R.id.txtbarcode);
            listItemView.txtScanNum = (TextView) convertView.findViewById(R.id.txtScanNum);
            listItemView.txtRemainQty = (TextView) convertView.findViewById(R.id.txtRemainQty);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        OutStockDetailInfo_Model outStockDetailInfoModel=outStockDetailInfoModels.get(selectID);
        listItemView.txtbarcode.setText(outStockDetailInfoModel.getMaterialNo());
        listItemView.txtScanNum.setText("扫描数："+outStockDetailInfoModel.getScanQty());
        listItemView.txtRemainQty.setText("复核数："+outStockDetailInfoModel.getOutStockQty());
        listItemView.txtMaterialDesc.setText(outStockDetailInfoModel.getMaterialDesc());
        if (outStockDetailInfoModel.getScanQty()!=0 &&
                outStockDetailInfoModel.getScanQty().compareTo(outStockDetailInfoModel.getOutStockQty())<0) {
            convertView.setBackgroundResource(R.color.khaki);
        }
        else if (outStockDetailInfoModel.getScanQty().compareTo(outStockDetailInfoModel.getOutStockQty())==0) {
            convertView.setBackgroundResource(R.color.springgreen);
        }else{
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }


}
