package com.xx.chinetek.cyproduct.Manage;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xx.chinetek.adapter.wms.MaterialChange.MaterialChangeAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cyproduct.BillChoice;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Stock.StockInfo_Model;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_line_manage)
public class LineManage extends BaseActivity {


    Context context=LineManage.this;

    @ViewInject(R.id.LsvLineManage)
    ListView LsvLineManage;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_Line_subtitle), true);
        x.view().inject(this);

        List<StockInfo_Model> stockInfo_models=getdata();
        MaterialChangeAdapter materialChangeAdapter=new MaterialChangeAdapter(context,stockInfo_models);
        LsvLineManage.setAdapter(materialChangeAdapter);
    }

    @Event(value = R.id.LsvLineManage,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(context, ProductManage.class);
        startActivityLeft(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_linemanagel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            Intent intent = new Intent();
            BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_manage_subtitle), true);
            intent.setClass(context, BillChoice.class);
            startActivityLeft(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    List<StockInfo_Model> getdata(){
        List<StockInfo_Model> stockInfoModels=new ArrayList<>();
        for(int i=0;i<7;i++){
            StockInfo_Model stockInfoModel=new StockInfo_Model();
            stockInfoModel.setSerialNo("工单34333"+i);
            stockInfoModel.setBatchNo("批次号:"+"1234");
            stockInfoModel.setMaterialDesc("开始时间");
            stockInfoModels.add(stockInfoModel);
        }
        return stockInfoModels;
    }

}
