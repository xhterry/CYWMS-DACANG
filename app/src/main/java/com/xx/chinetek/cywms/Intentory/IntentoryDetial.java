package com.xx.chinetek.cywms.Intentory;

import android.content.Context;
import android.widget.ListView;

import com.xx.chinetek.adapter.Intentory.InventoryScanItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Inventory.CheckDet_Model;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

@ContentView(R.layout.activity_intentory_detial)
public class IntentoryDetial extends BaseActivity {

Context context=IntentoryDetial.this;
    @ViewInject(R.id.lsvInventoryDetail)
    ListView lsvInventoryDetail;
    InventoryScanItemAdapter inventoryScanItemAdapter;
    ArrayList<CheckDet_Model> checkDetModels;//存放盘点记录

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Intentory_detail), true);
        x.view().inject(this);
        checkDetModels=getData();
        inventoryScanItemAdapter=new InventoryScanItemAdapter(context,checkDetModels);
        lsvInventoryDetail.setAdapter(inventoryScanItemAdapter);
    }

    ArrayList<CheckDet_Model> getData(){
        ArrayList<CheckDet_Model> checkDetModels=new ArrayList<>();
        for (int i=0;i<10;i++){
            CheckDet_Model checkDetModel=new CheckDet_Model();
            checkDetModel.setMATERIALNO("123"+i);
            checkDetModel.setMATERIALDESC("物料描述");
            checkDetModel.setQTY(10f);
            checkDetModels.add(checkDetModel);
        }

        return  checkDetModels;
    }
}
