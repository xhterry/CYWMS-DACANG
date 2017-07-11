package com.xx.chinetek.cywms.MaterialChange;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_material_change)
public class MaterialChange extends BaseActivity {

   Context context=MaterialChange.this;

    @ViewInject(R.id.txt_InOrder)
    TextView txtInOrder;
    @ViewInject(R.id.txt_OutOrder)
    TextView txtOutOrder;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.edt_ChanggeScanBarcode)
    EditText edtChanggeScanBarcode;
    @ViewInject(R.id.edt_ChangeNum)
    EditText edtChangeNum;
    @ViewInject(R.id.edt_StockScan)
    EditText edtStockScan;
    @ViewInject(R.id.btn_DisPallet)
    EditText btnDisPallet;
    @ViewInject(R.id.btn_Pallet)
    EditText btnPallet;
    @ViewInject(R.id.btn_Box)
    EditText btnBox;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.MaterialChange_scan_subtitle), true);
        x.view().inject(this);
    }

    @Event(R.id.btn_DisPallet)
    private void btnDisPalletClick(View view){
        Intent intent=new Intent(context,com.xx.chinetek.Pallet.DismantlePallet.class);
        startActivityLeft(intent);
    }

    @Event(R.id.btn_Box)
    private void btnBoxClick(View view){
        Intent intent=new Intent(context,com.xx.chinetek.Box.Boxing.class);
        startActivityLeft(intent);
    }

    @Event(R.id.btn_Pallet)
    private void btnPalletClick(View view){
        Intent intent=new Intent(context,com.xx.chinetek.Pallet.CombinPallet.class);
        startActivityLeft(intent);
    }
}
