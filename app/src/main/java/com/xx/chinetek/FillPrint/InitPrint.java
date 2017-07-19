package com.xx.chinetek.FillPrint;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Calendar;


@ContentView(R.layout.activity_init_print)
public class InitPrint extends BaseActivity {

    Context context=InitPrint.this;

    @ViewInject(R.id.edt_jd)
    TextView edtjd;
    @ViewInject(R.id.edt_bqlx)
    TextView edtbqlx;
    @ViewInject(R.id.edt_wlbh)
    EditText edtwlbh;
    @ViewInject(R.id.edt_ddh)
    EditText edtddh;
    @ViewInject(R.id.edt_gysp)
    EditText edtgysp;
    @ViewInject(R.id.edt_scrq)
    TextView edtscrq;
    @ViewInject(R.id.edt_cnp)
    EditText edtcnp;
    @ViewInject(R.id.edt_kw)
    EditText edtkw;
    @ViewInject(R.id.edt_dqrq)
    TextView edtdqrq;
    @ViewInject(R.id.edt_sl)
    EditText edtsl;

    String[] LabelType={"包材","原料","包材退"};
    String[] LabelTypeID={"OutBaoCai","OutR","OutBaoCaiTui"};
    String[] StrongHoldCode={"CY1","CX1","FC1"};
    String[] StrongHoldName={"上海创元","上海创馨","上海甫成"};
    int mYear, mMonth, mDay;
    Barcode_Model barcodeModel;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle("期初打印", false);
        x.view().inject(this);
        barcodeModel=new Barcode_Model();
        if(TextUtils.isEmpty(URLModel.PrintIP)){
            MessageBox.Show(context,"移动打印机IP地址未设置");
        }
    }

    @Event(value = {R.id.edt_jd,R.id.edt_bqlx},type =View.OnClickListener.class )
    private void edtClick(View view){
           final boolean isJD=view.getId()==R.id.edt_jd;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(isJD?"选择据点":"选择标签类型");
            builder.setItems(isJD?StrongHoldName:LabelType, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                        if(isJD){
                            edtjd.setText(StrongHoldName[which]);
                            barcodeModel.setStrongHoldCode(StrongHoldCode[which]);
                            barcodeModel.setStrongHoldName(StrongHoldName[which]);
                        }else{
                            edtbqlx.setText(LabelType[which]);
                            barcodeModel.setLabelMark(LabelTypeID[which]);
                            CommonUtil.setEditFocus(edtwlbh);
                        }
                }
            });
            builder.show();

    }

    @Event(value = {R.id.edt_scrq,R.id.edt_dqrq},type =View.OnClickListener.class )
    private void edtDateClick(View view){
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        final  boolean isscrq=view.getId()==R.id.edt_scrq;
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                if(isscrq) {
                    edtscrq.setText(display());
                    barcodeModel.setProductDate(CommonUtil.dateStrConvertDate(edtscrq.getText().toString()));
                }
                else {
                    edtdqrq.setText(display());
                    barcodeModel.setEDate(CommonUtil.dateStrConvertDate(edtdqrq.getText().toString()));

                }
            }
        }, mYear, mMonth, mDay).show();
    }


    @Event(R.id.btn_Print)
    private void BtnPrintClick(View view){
        if(Check()){
            barcodeModel.setAreano(edtkw.getText().toString().trim());
            barcodeModel.setMaterialNo(edtwlbh.getText().toString().trim());
            barcodeModel.setErpVoucherNo(edtddh.getText().toString().trim());
            barcodeModel.setSupPrdBatch(edtgysp.getText().toString().trim());
            barcodeModel.setBatchNo(edtcnp.getText().toString().trim());
            barcodeModel.setQty(Float.parseFloat(edtsl.getText().toString().trim()));
            barcodeModel.setIP(URLModel.PrintIP);
          //  ClearFrm();
        }
    }

    boolean Check(){
        ConstraintLayout sLinerLayout = (ConstraintLayout)findViewById(R.id.layout_InitPrint);
        boolean returnFlag=true;
        for (int i = 0; i < sLinerLayout.getChildCount(); i++) {
            View v=sLinerLayout.getChildAt(i);
            if ( v instanceof EditText){
                EditText mTextView = (EditText)sLinerLayout.getChildAt(i);
                //根据ID获取RadioButton的实例
                EditText tv = (EditText)findViewById(mTextView.getId());
                if(TextUtils.isEmpty(tv.getText().toString().trim())){
                    MessageBox.Show(context,"请输入完整数据");
                    returnFlag=false;
                    CommonUtil.setEditFocus(tv);
                    break;
                }
            }
            if ( v instanceof TextView){
                TextView mTextView = (TextView)sLinerLayout.getChildAt(i);
                //根据ID获取RadioButton的实例
                TextView tv = (TextView)findViewById(mTextView.getId());
                if(TextUtils.isEmpty(tv.getText().toString().trim())){
                    MessageBox.Show(context,"请输入完整数据");
                    returnFlag=false;
                    break;
                }
            }

        }

        if(!CommonUtil.isFloat(edtsl.getText().toString().trim())){
            returnFlag=false;
            MessageBox.Show(context,"数量格式不正确");
            CommonUtil.setEditFocus(edtsl);
        }

        return returnFlag;
    }

    public String  display() {
        return new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).toString();
    }

    void ClearFrm(){
        edtjd.setText("");
        edtbqlx.setText("");
        edtwlbh.setText("");
        edtddh.setText("");
        edtgysp.setText("");
        edtscrq.setText("");
        edtcnp.setText("");
        edtdqrq.setText("");
        edtsl.setText("");
        edtkw.setText("");
        barcodeModel=new Barcode_Model();
        CommonUtil.setEditFocus(edtkw);
    }

}
