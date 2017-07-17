package com.xx.chinetek.cywms.InnerMove;

import android.content.Context;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xx.chinetek.adapter.wms.InnerMove.InnerMoveAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Stock.StockInfo_Model;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_inner_move_scan)
public class InnerMoveScan extends BaseActivity {





  Context context=InnerMoveScan.this;
    @ViewInject(R.id.lsv_InnerMoveDetail)
    ListView lsvInnerMoveDetail;
    @ViewInject(R.id.tb_MoveType)
    ToggleButton TBMoveType;
    @ViewInject(R.id.edt_MoveInStock)
    EditText edtMoveInStock;
    @ViewInject(R.id.edt_MoveScanBarcode)
    EditText edtMoveScanBarcode;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;

    List<StockInfo_Model> stockInfo_models;
    InnerMoveAdapter innerMoveAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.InnerMove_subtitle), false);
        x.view().inject(this);
    }

    @Event(value = R.id.edt_MoveScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtRecScanBarcode(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String barcode=edtMoveScanBarcode.getText().toString().trim();

        }
        return false;
    }

    @Event(value = R.id.edt_MoveInStock,type = View.OnKeyListener.class)
    private  boolean edtMoveInStock(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String stock=edtMoveInStock.getText().toString().trim();

        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
//            final Map<String, String> params = new HashMap<String, String>();
//            String ModelJson = GsonUtil.parseModelToJson(receiptDetailModels);
//            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
//            params.put("ModelJson", ModelJson);
//            LogUtil.WriteLog(ReceiptionScan.class, TAG_SaveT_InStockDetailADF, ModelJson);
//            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_InStockDetailADF, getString(R.string.Msg_SaveT_InStockDetailADF), context, mHandler, RESULT_Msg_SaveT_InStockDetailADF, null,  URLModel.GetURL().SaveT_InStockDetailADF, params, null);

        }
        return super.onOptionsItemSelected(item);
    }

    private void BindListVIew(List<StockInfo_Model> stockInfo_models) {
        innerMoveAdapter=new InnerMoveAdapter(context,stockInfo_models);
        lsvInnerMoveDetail.setAdapter(innerMoveAdapter);
    }

}
