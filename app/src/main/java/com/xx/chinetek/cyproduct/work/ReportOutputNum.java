package com.xx.chinetek.cyproduct.work;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(R.layout.activity_report_output_num)
public class ReportOutputNum extends AppCompatActivity {

    Context context=ReportOutputNum.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.context = context;
        x.view().inject(this);
    }
}
