package com.xx.chinetek.cywms.OffShelf;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xx.chinetek.adapter.OffShelf.OffSehlfBillChoiceItemAdapter;
import com.xx.chinetek.adapter.Receiption.ReceiptBillChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Receiption.SupplierModel;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@ContentView(R.layout.activity_offshelfbill_choice)
public class OffShelfBillChoice extends BaseActivity {

    @ViewInject(R.id.lsvOffshelfChioce)
    ListView lsvOffshelfChioce;

    int supplierRequestCode=1001;

    Context context = OffShelfBillChoice.this;
    ReceiptBillChioceItemAdapter  receiptBillChioceItemAdapter;
    List<Map<String, String>> SupplierList= new ArrayList<Map<String, String>>();//供应商列表

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.receipt_subtitle), false);
        x.view().inject(this);

        List<SupplierModel> supplierModels=getData();
        OffSehlfBillChoiceItemAdapter receiptBillChioceItemAdapter=new OffSehlfBillChoiceItemAdapter(context,supplierModels);
        lsvOffshelfChioce.setAdapter(receiptBillChioceItemAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuoffshelfbillchoice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvOffshelfChioce,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(context,OffshelfScan.class);
        startActivityLeft(intent);
    }


    List<SupplierModel> getData(){
        List<SupplierModel> supplierModels=new ArrayList<>();
        for(int i=0;i<10;i++){
            SupplierModel supplierModel=new SupplierModel();
            supplierModel.setSupplierID("123"+i);
            supplierModel.setSupplierName("供应商"+i);
            supplierModel.setVoucherNo("WMS单据号"+i);
            supplierModel.setERPVoucherNo("ERP单据号8"+i);
            supplierModel.setStrVoucherType("单据类型"+i);
            supplierModel.setCompany("据点");
            supplierModel.setDepartment("部门");
            supplierModels.add(supplierModel);
        }
        return supplierModels;
    }

}

