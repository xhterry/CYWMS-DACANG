package com.xx.chinetek.cywms;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.xx.chinetek.Box.Boxing;
import com.xx.chinetek.FillPrint.FillPrint;
import com.xx.chinetek.Pallet.CombinPallet;
import com.xx.chinetek.Pallet.DismantlePallet;
import com.xx.chinetek.adapter.GridViewItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.InnerMove.InnerMoveScan;
import com.xx.chinetek.cywms.Intentory.InventoryBillChoice;
import com.xx.chinetek.cywms.OffShelf.OffShelfBillChoice;
import com.xx.chinetek.cywms.Qc.QCBillChoice;
import com.xx.chinetek.cywms.Query.QueryMain;
import com.xx.chinetek.cywms.Receiption.ReceiptBillChoice;
import com.xx.chinetek.cywms.Review.ReviewBillChoice;
import com.xx.chinetek.cywms.Truck.TruckLoad;
import com.xx.chinetek.cywms.UpShelf.UpShelfBillChoice;

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
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.app_name),false);
        x.view().inject(this);
        List<Map<String, Object>> data_list = getData();
        adapter = new GridViewItemAdapter(context,data_list);
        gridView.setAdapter(adapter);
    }


    @Event(value = R.id.gv_Function,type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        switch (position) {
            case 0://质检单
                intent.setClass(context, QCBillChoice.class);
                break;
            case 1://收货任务
                intent.setClass(context, ReceiptBillChoice.class);
                break;
            case 2://上架任务
                intent.setClass(context, UpShelfBillChoice.class);
                break;
            case 3://下架任务单
                intent.setClass(context, OffShelfBillChoice.class);
                break;
            case 4://复核
                intent.setClass(context, ReviewBillChoice.class);
                break;
            case 5:
                intent.setClass(context, InnerMoveScan.class);
                break;
            case 6://盘点任务
                intent.setClass(context, InventoryBillChoice.class);
                break;
            case 7:
                intent.setClass(context, QueryMain.class);
                break;
            case 8:
                intent.setClass(context, CombinPallet.class);
                break;
            case 9:
                intent.setClass(context, DismantlePallet.class);
                break;
            case 10:
                intent.setClass(context, Boxing.class);
                break;
//            case 11:
//                intent.setClass(context, AdjustStock.class);
//                break;
            case 11:
                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.MaterialChange_title), false);
                intent.setClass(context, BillChoice.class);
                break;
            case 12:
                intent.setClass(context, TruckLoad.class);
                break;
            case 13:
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
        int[] itemIcon = new int[]{ R.drawable.qc,R.drawable.receiption, R.drawable.upshelves,
                R.drawable.offshelf,R.drawable.review, R.drawable.innermove,
                R.drawable.inventory, R.drawable.query,R.drawable.combinepallet,
                R.drawable.dismantlepallet,R.drawable.dismounting,
                R.drawable.materiel,R.drawable.truckload,R.drawable.fillprint
                //R.drawable.adjustment,
        };
        String[] itemNames = new String[]{"质检","收货", "上架",
                "下架","发货复核", "移库",
                "盘点", "查询",//"调拨",盘点(普通、随机)"库存调整",
                "组托","拆托","装箱拆箱","物料转换","装车","标签补打"
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
