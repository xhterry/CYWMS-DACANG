package com.xx.chinetek.cywms.Qc;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.xx.chinetek.adapter.QC.QcMaterialChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Receiption.SupplierModel;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_qcmaterial_choice)
public class QCMaterialChoice extends BaseActivity {


    Context context=QCMaterialChoice.this;

    @ViewInject(R.id.lvsQCMaterialChioce)
    ListView lvsQCMaterialChioce;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.QC_Material_subtitle), true);
        x.view().inject(this);

        List<SupplierModel> supplierModels=getData();
        QcMaterialChioceItemAdapter qcMaterialChioceItemAdapter=new QcMaterialChioceItemAdapter(context,supplierModels);
        lvsQCMaterialChioce.setAdapter(qcMaterialChioceItemAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
       }
        return super.onOptionsItemSelected(item);
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
