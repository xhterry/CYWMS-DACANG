package com.xx.chinetek.cywms.Truck;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.util.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_truck_load)
public class TruckLoad extends BaseActivity {

    Context context=TruckLoad.this;
    @ViewInject(R.id.edt_VourcherNo)
    EditText edtVourcherNo;
    @ViewInject(R.id.edt_PlateNumber)
    EditText edtPlateNumber;
    @ViewInject(R.id.edt_Volume)
    EditText edtVolume;
    @ViewInject(R.id.edt_Weight)
    EditText edtWeight;
    @ViewInject(R.id.edt_Number)
    EditText edtNumber;
    @ViewInject(R.id.edt_Feight)
    EditText edtFeight;
    @ViewInject(R.id.txt_Supplier)
    TextView txtSupplier;
    @ViewInject(R.id.txt_Destina)
    TextView txtDestina;
    @ViewInject(R.id.btn_Submit)
    Button btnSubmit;


    List<EditText> editTextList=new ArrayList<>();

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.Truckload_title), true);
        x.view().inject(this);
        editTextList.add(edtVourcherNo);
        editTextList.add(edtPlateNumber);
        editTextList.add(edtVolume);
        editTextList.add(edtWeight);
        editTextList.add(edtNumber);
        editTextList.add(edtFeight);
        CommonUtil.setEditFocus(edtVourcherNo);
    }

    @Event(value = R.id.edt_VourcherNo,type = View.OnKeyListener.class)
    private  boolean edtVourcherNoonKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            for (int i=0;i<editTextList.size()-1;i++) {
                if(editTextList.get(i).getId()==v.getId()){
                    CommonUtil.setEditFocus(editTextList.get(i));
                    break;
                }
            }
        }
        return false;
    }

    @Event(R.id.btn_Submit)
    private void btnSubmit(View view){
        CommonUtil.setEditFocus(edtVourcherNo);
    }

}
