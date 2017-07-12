package com.xx.chinetek.cyproduct;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.xx.chinetek.FillPrint.FillPrint;
import com.xx.chinetek.adapter.GridViewItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cyproduct.Manage.LineManage;
import com.xx.chinetek.cyproduct.OffShelf.DeliveryProduct;
import com.xx.chinetek.cyproduct.Receiption.ReceiptBillChoice;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.gv_Function)
    GridView gridView;
    GridViewItemAdapter adapter;
    Context context = MainActivity.this;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.app_product),false);
        x.view().inject(this);
        List<Map<String, Object>> data_list = getData();
        adapter = new GridViewItemAdapter(context,data_list);
        gridView.setAdapter(adapter);
    }


    @Event(value = R.id.gv_Function,type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(context, ReceiptBillChoice.class);
                break;
            case 1:
                intent.setClass(context, DeliveryProduct.class);
                break;
            case 2:
                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_Package_subtitle), true);
                intent.setClass(context, BillChoice.class);
                break;
            case 3:
                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_rawMaterial_subtitle), true);
                intent.setClass(context, BillChoice.class);
                break;
            case 4:
                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_returnrawMaterial_subtitle), true);
                intent.setClass(context, BillChoice.class);
                break;
            case 5:
                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_Instock), true);
                intent.setClass(context, BillChoice.class);
                break;
            case 6:
                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_semiproduct_subtitle), true);
                intent.setClass(context, BillChoice.class);
                break;
            case 7:
                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_returnsemiproduct_subtitle), true);
                intent.setClass(context, BillChoice.class);
                break;
            case 8:
                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_receiptsemiproduct_subtitle), true);
                intent.setClass(context, BillChoice.class);
                break;
            case 9:
                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_tankin_title), true);
                intent.setClass(context, BillChoice.class);
                 break;
            case 10:
                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_tankout_title), true);
                intent.setClass(context, BillChoice.class);
                break;
            case 11:
                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.MaterialChange_title), true);
                intent.setClass(context, BillChoice.class);
                break;
            case 12:
                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.TakeSample_title), true);
                intent.setClass(context, BillChoice.class);
                break;
            case 13:
                intent.setClass(context, LineManage.class);
                break;
            case 14:
                intent.setClass(context, FillPrint.class);
                break;
        }
        if(intent!=null)
            startActivityLeft(intent);
    }


    @Override
    protected void initData() {
        super.initData();
    }

    public List<Map<String, Object>> getData(){
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        int[] itemIcon = new int[]{  R.drawable.receiption,R.drawable.deliveryproduct,R.drawable.packagematerial,
                R.drawable.rawmaterial,R.drawable.returnmaterial,R.drawable.receiptproduct,
                R.drawable.semiproduct,R.drawable.returnsemiproduct,R.drawable.receiptsemiproduct,
                R.drawable.tankin,R.drawable.tankout,R.drawable.materiel,
                R.drawable.takesample ,R.drawable.productmanage,R.drawable.fillprint


                //R.drawable.bulk,R.drawable.returnbluk,
        };
        String[] itemNames = new String[]{"线边仓入库","线边仓出库","包材发料",
                "原散发料","原散退料","散成品生产",
                "半制品发料","半制品退料","半制品生产",
                "坦克投料","坦克退料","车间转料",
                "取样","生产记录","标签补打"
//                ,"散装发料","散装退料",
//                "半制品发料","半制品退料",
        };
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<itemIcon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", itemIcon[i]);
            map.put("text", itemNames[i]);
            data_list.add(map);
        }
        return data_list;
    }
}
