package com.xx.chinetek.cywms.Query;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.xx.chinetek.adapter.GridViewItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_query_main)
public class QueryMain extends BaseActivity {

  Context  context=QueryMain.this;
    @ViewInject(R.id.gv_QueryFunction)
    GridView gridView;
    GridViewItemAdapter adapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.query_title), false);
        x.view().inject(this);
        List<Map<String, Object>> data_list = getData();
        adapter = new GridViewItemAdapter(context,data_list);
        gridView.setAdapter(adapter);
    }

    //,R.drawable.workno
    public List<Map<String, Object>> getData(){
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        int[] itemIcon = new int[]{ R.drawable.material,R.drawable.stock, R.drawable.batch,
                R.drawable.supplier
        };//,"工单"
        String[] itemNames = new String[]{"物料","库位", "批次",
                "供应商"
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

    @Event(value = R.id.gv_QueryFunction,type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(context, Query.class);
        switch (position) {
            case 0:
                BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.query_Materialtitle), true);
                intent.putExtra("Type",1);
                break;
            case 1:
                BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.query_Stocktitle), true);
                intent.putExtra("Type",2);
                break;
            case 2:
                BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.query_Bathtitle), true);
                intent.putExtra("Type",3);
                break;
            case 3:
                BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.query_Suppliertitle), true);
                intent.putExtra("Type",4);
                break;
            case 4:
                BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.query_WorkNotitle), true);
                break;
        }
            startActivityLeft(intent);
    }
}
