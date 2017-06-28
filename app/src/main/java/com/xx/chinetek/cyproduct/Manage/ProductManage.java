package com.xx.chinetek.cyproduct.Manage;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.xx.chinetek.adapter.MaterialChange.MaterialChangeAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Stock.StockInfo_Model;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_product_manage)
public class ProductManage extends BaseActivity {

Context context=ProductManage.this;

    @ViewInject(R.id.lsvPersonManage)
    ListView lsvPersonManage;

    @ViewInject(R.id.btnComplete)
    Button btnComplete;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_manage_subtitle), true);
        x.view().inject(this);

        List<StockInfo_Model> stockInfo_models=getdata();
        MaterialChangeAdapter materialChangeAdapter=new MaterialChangeAdapter(context,stockInfo_models);
        lsvPersonManage.setAdapter(materialChangeAdapter);
    }

    @Event(R.id.btnComplete)
    private  void btnCompleteClick(View view){
        Intent intent=new Intent(context,ProductComplete.class);
        startActivityLeft(intent);
    }

    List<StockInfo_Model> getdata(){
        List<StockInfo_Model> stockInfoModels=new ArrayList<>();
        for(int i=0;i<7;i++){
            StockInfo_Model stockInfoModel=new StockInfo_Model();
            stockInfoModel.setSerialNo("员工");
            stockInfoModel.setBatchNo("班组");
            stockInfoModel.setMaterialDesc("工单");
            stockInfoModels.add(stockInfoModel);
        }
        return stockInfoModels;
    }

}
