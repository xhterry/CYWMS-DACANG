//package com.xx.chinetek.cywms;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
//import com.xx.chinetek.adapter.BillChioceItemAdapter;
//import com.xx.chinetek.base.BaseActivity;
//import com.xx.chinetek.base.BaseApplication;
//import com.xx.chinetek.base.ToolBarTitle;
//import com.xx.chinetek.cywms.Intentory.IntentoryScan;
//import com.xx.chinetek.cywms.MaterialChange.MaterialChange;
//import com.xx.chinetek.cywms.OffShelf.OffshelfScan;
//import com.xx.chinetek.cywms.Qc.QCScan;
//import com.xx.chinetek.cywms.Review.ReviewScan;
//import com.xx.chinetek.cywms.UpShelf.UpShelfScanActivity;
//import com.xx.chinetek.model.Receiption.SupplierModel;
//
//import org.xutils.view.annotation.ContentView;
//import org.xutils.view.annotation.Event;
//import org.xutils.view.annotation.ViewInject;
//import org.xutils.x;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@ContentView(R.layout.activity_bill_choice)
//public class BillChoice extends BaseActivity {
//
//    @ViewInject(R.id.lsvChoice)
//    ListView lsvChoice;
//
//    Context context = BillChoice.this;
//    BillChioceItemAdapter billChioceItemAdapter;
//
//    @Override
//    protected void initViews() {
//        super.initViews();
//        BaseApplication.context = context;
//        x.view().inject(this);
//        List<SupplierModel> supplierModels=getData();
//        billChioceItemAdapter=new BillChioceItemAdapter(context,supplierModels);
//        lsvChoice.setAdapter(billChioceItemAdapter);
//    }
//
//    /**
//     * Listview item点击事件
//     */
//    @Event(value = R.id.lsvChoice,type =  AdapterView.OnItemClickListener.class)
//    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent =null;
//        switch (this.getTitle().toString()){
//            case "质检":
//                intent=new Intent(context,QCScan.class);
//                break;
//            case "上架":
//                intent=new Intent(context,UpShelfScanActivity.class);
//                break;
//            case "下架":
//                intent=new Intent(context,OffshelfScan.class);
//                break;
//            case "发货复核":
//                intent=new Intent(context,ReviewScan.class);
//                break;
//            case "盘点":
//                intent=new Intent(context,IntentoryScan.class);
//                break;
//            case "转料-入库单选择":
//            case "转料-出库单选择":
//                if(BaseApplication.toolBarTitle.Title.equals(getString(R.string.MaterialChange_title))) {
//                    BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.MaterialChange_out_subtitle), true);
//                    intent=new Intent(context,BillChoice.class);
//                }
//                else{
//                    intent=new Intent(context,MaterialChange.class);
//                }
//                break;
//
//        }
//if(intent!=null)
//        startActivityLeft(intent);
//    }
//
//    List<SupplierModel> getData(){
//        List<SupplierModel> supplierModels=new ArrayList<>();
//        for(int i=0;i<10;i++){
//            SupplierModel supplierModel=new SupplierModel();
//            supplierModel.setSupplierID("123"+i);
//            supplierModel.setSupplierName("供应商"+i);
//            supplierModel.setVoucherNo("WMS单据号"+i);
//            supplierModel.setERPVoucherNo("ERP单据号8"+i);
//            supplierModel.setStrVoucherType("单据类型"+i);
//            supplierModel.setCompany("据点");
//            supplierModel.setDepartment("部门");
//            supplierModels.add(supplierModel);
//        }
//        return supplierModels;
//    }
//}
