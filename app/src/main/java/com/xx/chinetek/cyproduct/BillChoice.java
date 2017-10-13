package com.xx.chinetek.cyproduct;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xx.chinetek.adapter.BillChioceItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cyproduct.Manage.ProductManageAdd;
import com.xx.chinetek.cyproduct.MaterialChange.MaterialChange;
import com.xx.chinetek.cyproduct.OffShelf.OffshelfScan;
import com.xx.chinetek.cyproduct.OffShelf.SemiProductScan;
import com.xx.chinetek.cyproduct.QC.TakeSample;
import com.xx.chinetek.cyproduct.Billinstock.CompleteProduct;
import com.xx.chinetek.cyproduct.Receiption.ReceiptSemiProduct;
import com.xx.chinetek.cyproduct.Receiption.ReceiptionScan;
import com.xx.chinetek.cyproduct.Receiption.TankIn;
import com.xx.chinetek.cyproduct.Return.ProductReturn;
import com.xx.chinetek.cyproduct.Return.SemiproductReturn;
import com.xx.chinetek.cyproduct.Return.TankOut;
import com.xx.chinetek.cyproduct.Review.ReviewScan;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Receiption.SupplierModel;
import com.xx.chinetek.util.dialog.MessageBox;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_bill_choice)
public class BillChoice extends BaseActivity {

    @ViewInject(R.id.lsvChoice)
    ListView lsvChoice;

    Context context = BillChoice.this;
    BillChioceItemAdapter billChioceItemAdapter;

    @Override
    protected void initViews() {
        try{
            super.initViews();
            BaseApplication.context = context;

            x.view().inject(this);

            List<SupplierModel> supplierModels=getData();
            billChioceItemAdapter=new BillChioceItemAdapter(context,supplierModels);
            lsvChoice.setAdapter(billChioceItemAdapter);
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
        }

    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoice,type =  AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent =null;
        switch (this.getTitle().toString()){
            case "转料-入库单选择":
            case "转料-出库单选择":
                if(BaseApplication.toolBarTitle.Title.equals(getString(R.string.MaterialChange_title))) {
                    BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.MaterialChange_out_subtitle), true);
                    intent=new Intent(context,BillChoice.class);
                }
                else{
                    intent=new Intent(context,MaterialChange.class);
                }
                break;
            case "线边仓入库":
                intent=new Intent(context, ReceiptionScan.class);
                break;
            case "取样":
                intent=new Intent(context,TakeSample.class);
                break;
            case "包材发料":
                intent=new Intent(context, ReviewScan.class);
                break;
            case "原散发料":
                intent=new Intent(context, OffshelfScan.class);
                break;
            case "半制品发料":
                intent=new Intent(context, SemiProductScan.class);
                break;
            case "原散退料":
                intent=new Intent(context, ProductReturn.class);
                break;
            case "半制品退料":
                intent=new Intent(context, SemiproductReturn.class);
                break;
            case "半制品生产":
                intent=new Intent(context, ReceiptSemiProduct.class);
                break;
            case "生产记录":
                intent=new Intent(context,ProductManageAdd.class);
                break;
            case "散成品生产":
                intent=new Intent(context,CompleteProduct.class);
                break;
            case "坦克投料":
                intent=new Intent(context,TankIn.class);
                break;
            case "坦克退料":
                intent=new Intent(context,TankOut.class);
                break;
        }
if(intent!=null)
        startActivityLeft(intent);
    }

    List<SupplierModel> getData(){
        List<SupplierModel> supplierModels=new ArrayList<>();
        for(int i=0;i<10;i++){
            SupplierModel supplierModel=new SupplierModel();
            supplierModel.setSupplierID("123"+i);
            supplierModel.setSupplierName("供应商"+i);
            supplierModel.setVoucherNo("工单号"+i);
            supplierModel.setERPVoucherNo("批次号"+i);
            supplierModel.setStrVoucherType("单据类型"+i);
            supplierModel.setCompany("据点");
            supplierModel.setDepartment("部门");
            supplierModels.add(supplierModel);
        }
        return supplierModels;
    }
}
