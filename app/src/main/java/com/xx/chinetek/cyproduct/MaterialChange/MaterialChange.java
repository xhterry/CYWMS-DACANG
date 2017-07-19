package com.xx.chinetek.cyproduct.MaterialChange;

import android.content.Context;
import android.widget.ListView;

import com.xx.chinetek.adapter.wms.MaterialChange.MaterialChangeAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_product_material_change)
public class MaterialChange extends BaseActivity {

   Context context=MaterialChange.this;

    @ViewInject(R.id.lsvMaterialChange)
    ListView lsvMaterialChange;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.MaterialChange_scan_subtitle), true);
        x.view().inject(this);

        List<StockInfo_Model> stockInfo_models=getdata();
       MaterialChangeAdapter materialChangeAdapter=new MaterialChangeAdapter(context,stockInfo_models);
        lsvMaterialChange.setAdapter(materialChangeAdapter);
    }

    List<StockInfo_Model> getdata(){
        List<StockInfo_Model> stockInfoModels=new ArrayList<>();
        for(int i=0;i<7;i++){
            StockInfo_Model stockInfoModel=new StockInfo_Model();
            stockInfoModel.setSerialNo("34333"+i);
            stockInfoModel.setBatchNo("批次号:"+"1234");
            stockInfoModel.setMaterialDesc("物料描述");
            stockInfoModels.add(stockInfoModel);
        }
        return stockInfoModels;
    }
}
