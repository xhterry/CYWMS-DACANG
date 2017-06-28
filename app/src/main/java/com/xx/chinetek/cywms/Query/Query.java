package com.xx.chinetek.cywms.Query;

import android.content.Context;
import android.widget.ListView;
import android.widget.TextView;

import com.xx.chinetek.adapter.Query.QueryItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Stock.StockInfo_Model;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_query)
public class Query extends BaseActivity {

   Context context=Query.this;

    @ViewInject(R.id.txtname)
    TextView txtname;
    @ViewInject(R.id.lsvQuery)
    ListView lsvQuery;

    QueryItemAdapter queryItemAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
        txtname.setText(BaseApplication.toolBarTitle.Title+"号：");
        List<StockInfo_Model> stockInfoModels=getData();
        queryItemAdapter=new QueryItemAdapter(context,stockInfoModels);
        lsvQuery.setAdapter(queryItemAdapter);
    }

    List<StockInfo_Model> getData(){
        List<StockInfo_Model> stockInfo_models=new ArrayList<>();
        for(int i=0;i<10;i++){
            StockInfo_Model stockInfoModel=new StockInfo_Model();
            stockInfoModel.setMaterialDesc("物料描述");
            stockInfoModel.setMaterialNo("料号123"+i);
            stockInfoModel.setAreaNo("A010"+i);
            stockInfoModel.setQty(10f);
            stockInfo_models.add(stockInfoModel);
        }
        return stockInfo_models;
    }
}
