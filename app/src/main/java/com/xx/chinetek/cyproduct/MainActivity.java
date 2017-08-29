package com.xx.chinetek.cyproduct;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xx.chinetek.FillPrint.FillPrint;
import com.xx.chinetek.Pallet.CombinPallet;
import com.xx.chinetek.Pallet.DismantlePallet;
import com.xx.chinetek.adapter.GridViewItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cyproduct.Billinstock.BillsIn;
import com.xx.chinetek.cyproduct.LineStockIn.LineStockInMaterial;
import com.xx.chinetek.cyproduct.LineStockIn.LineStockInProduct;
import com.xx.chinetek.cyproduct.LineStockOut.LineStockOutProduct;
import com.xx.chinetek.cyproduct.Manage.LineManage;
import com.xx.chinetek.cywms.InnerMove.InnerMoveScan;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.User.MenuInfo;

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
        LinearLayout linearLayout=(LinearLayout) gridView.getAdapter().getView(position,view,null);
        TextView textView=(TextView)linearLayout.getChildAt(1);
        Intent intent = new Intent();
        if(textView.getText().toString().equals("生产入库"))
            intent.setClass(context, LineStockInProduct.class);
        else if(textView.getText().toString().equals("生产出库"))
            intent.setClass(context, LineStockOutProduct.class);
        else if(textView.getText().toString().equals("领料入库"))
            intent.setClass(context, LineStockInMaterial.class);
        else if(textView.getText().toString().equals("产线生产"))
            intent.setClass(context, BillsIn.class);
        else if(textView.getText().toString().equals("生产记录"))
            intent.setClass(context, LineManage.class);
        else if(textView.getText().toString().equals("坦克投料"))
            intent.setClass(context, BillChoice.class);
        else if(textView.getText().toString().equals("坦克退料"))
            intent.setClass(context, BillChoice.class);
        else if(textView.getText().toString().equals("标签补打"))
            intent.setClass(context, FillPrint.class);
        else if(textView.getText().toString().equals("取样"))
            intent.setClass(context, BillChoice.class);
        else if(textView.getText().toString().equals("组托"))
            intent.setClass(context, CombinPallet.class);
        else if(textView.getText().toString().equals("拆托"))
            intent.setClass(context, DismantlePallet.class);
        else if(textView.getText().toString().equals("移库"))
            intent.setClass(context, InnerMoveScan.class);
        if(intent!=null)
            startActivityLeft(intent);
//        switch (position) {
//            case 0:
//                intent.setClass(context, LineStockInBillChoice.class);
//                break;
//            case 1:
//                intent.setClass(context, DeliveryProduct.class);
//                break;
//            case 2:
//                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_Package_subtitle), true);
//                intent.setClass(context, BillChoice.class);
//                break;
//            case 3:
//                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_rawMaterial_subtitle), true);
//                intent.setClass(context, BillChoice.class);
//                break;
//            case 4:
//                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_returnrawMaterial_subtitle), true);
//                intent.setClass(context, BillChoice.class);
//                break;
//            case 5:
//                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_Instock), true);
//                intent.setClass(context, BillChoice.class);
//                break;
//            case 6:
//                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_semiproduct_subtitle), true);
//                intent.setClass(context, BillChoice.class);
//                break;
//            case 7:
//                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_returnsemiproduct_subtitle), true);
//                intent.setClass(context, BillChoice.class);
//                break;
//            case 8:
//                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_receiptsemiproduct_subtitle), true);
//                intent.setClass(context, BillChoice.class);
//                break;
//            case 9:
//                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_tankin_title), true);
//                intent.setClass(context, BillChoice.class);
//                 break;
//            case 10:
//                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_tankout_title), true);
//                intent.setClass(context, BillChoice.class);
//                break;
//            case 11:
//                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.MaterialChange_title), true);
//                intent.setClass(context, BillChoice.class);
//                break;
//            case 12:
//                BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.TakeSample_title), true);
//                intent.setClass(context, BillChoice.class);
//                break;
//            case 13:
//                intent.setClass(context, LineManage.class);
//                break;
//            case 14:
//                intent.setClass(context, FillPrint.class);
//                break;
//        }

    }


    @Override
    protected void initData() {
        super.initData();
    }

    public List<Map<String, Object>> getData(){
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        ArrayList<Integer>  itemIconList=new ArrayList<>();
        ArrayList<String>  itemNamesList=new ArrayList<>();
        List<MenuInfo> menuInfos=BaseApplication.userInfo.getLstMenu();

        itemIconList.add(R.drawable.receiption);
        itemNamesList.add("领料入库");
        itemIconList.add(R.drawable.returnmaterial);
        itemNamesList.add("退料入库");
        itemIconList.add(R.drawable.receiptsemiproduct);
        itemNamesList.add("生产入库");
        itemIconList.add(R.drawable.packagematerial);
        itemNamesList.add("领料出库");
        itemIconList.add(R.drawable.semiproduct);
        itemNamesList.add("退料出库");
        itemIconList.add(R.drawable.deliveryproduct);
        itemNamesList.add("生产出库");
        itemIconList.add(R.drawable.productmanage);
        itemNamesList.add("生产记录");
        itemIconList.add(R.drawable.receiptproduct);
        itemNamesList.add("产线生产");
        itemIconList.add(R.drawable.combinepallet);
        itemNamesList.add("组托");
        itemIconList.add(R.drawable.dismantlepallet);
        itemNamesList.add("拆托");
        itemIconList.add(R.drawable.tankin);
        itemNamesList.add("坦克投料");
        itemIconList.add(R.drawable.tankout);
        itemNamesList.add("坦克退料");
        itemIconList.add(R.drawable.innermove);
        itemNamesList.add("移库");
        itemIconList.add(R.drawable.materiel);
        itemNamesList.add("车间转料");
        itemIconList.add(R.drawable.takesample);
        itemNamesList.add("取样");
        itemIconList.add(R.drawable.fillprint);
        itemNamesList.add("标签补打");
        for (int i = 0; i < itemIconList.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", itemIconList.get(i));
            map.put("text", itemNamesList.get(i));
            data_list.add(map);
        }
        return data_list;
    }
}
