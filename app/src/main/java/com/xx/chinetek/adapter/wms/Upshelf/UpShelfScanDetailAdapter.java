package com.xx.chinetek.adapter.wms.Upshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.WMS.Stock.AreaInfo_Model;
import com.xx.chinetek.model.WMS.UpShelf.InStockTaskDetailsInfo_Model;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by GHOST on 2017/1/13.
 */

public class UpShelfScanDetailAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<InStockTaskDetailsInfo_Model> inStockTaskDetailsInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtbarcode;
        public TextView txtScanNum;
        public TextView txtRemainQty;
        public TextView txtMaterialDesc;
        public TextView txtreferStock;
    }

    public UpShelfScanDetailAdapter(Context context, List<InStockTaskDetailsInfo_Model> inStockTaskDetailsInfoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.inStockTaskDetailsInfoModels = inStockTaskDetailsInfoModels;

    }

    @Override
    public int getCount() {
        return  inStockTaskDetailsInfoModels==null?0:inStockTaskDetailsInfoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return inStockTaskDetailsInfoModels.get(position);
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
            convertView = listContainer.inflate(R.layout.item_uploadscandetail_listview,null);
            listItemView.txtbarcode = (TextView) convertView.findViewById(R.id.txtbarcode);
            listItemView.txtScanNum = (TextView) convertView.findViewById(R.id.txtScanNum);
            listItemView.txtreferStock = (TextView) convertView.findViewById(R.id.txtreferStock);
            listItemView.txtRemainQty = (TextView) convertView.findViewById(R.id.txtRemainQty);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
       final InStockTaskDetailsInfo_Model inStockTaskDetailsInfoModel=inStockTaskDetailsInfoModels.get(selectID);
        listItemView.txtbarcode.setText(inStockTaskDetailsInfoModel.getMaterialNo());
        listItemView.txtScanNum.setText("扫描数："+inStockTaskDetailsInfoModel.getScanQty());
        listItemView.txtRemainQty.setText("上架数："+inStockTaskDetailsInfoModel.getRemainQty());
        listItemView.txtreferStock.setText("推荐库位："+GetReferStock(inStockTaskDetailsInfoModel.getLstArea()));
        listItemView.txtMaterialDesc.setText(inStockTaskDetailsInfoModel.getMaterialDesc());
        if (inStockTaskDetailsInfoModel.getScanQty()!=0 &&
                inStockTaskDetailsInfoModel.getScanQty().compareTo(inStockTaskDetailsInfoModel.getRemainQty())<0) {
            convertView.setBackgroundResource(R.color.khaki);
        }
        else if (inStockTaskDetailsInfoModel.getScanQty().compareTo(inStockTaskDetailsInfoModel.getRemainQty())==0) {
            convertView.setBackgroundResource(R.color.springgreen);
        }else{
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }


    String GetReferStock(ArrayList<AreaInfo_Model> areaInfoModels){
        StringBuffer Area=new StringBuffer();
        String[] referStocks=new String[areaInfoModels.size()];
        if(areaInfoModels!=null) {
            int i = 0;
            for (AreaInfo_Model areaInfoModel : areaInfoModels) {
                Area.append(areaInfoModel.getAreaNo() + ",");
                referStocks[i++] = areaInfoModel.getAreaNo();
            }
        }
        return areaInfoModels==null || areaInfoModels.size()==0?"":Area.substring(0,Area.length()-1);
    }


}
