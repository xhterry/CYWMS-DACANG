package com.xx.chinetek.adapter.product.Manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.User.UserInfo;

import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class UserInfoItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<UserInfo> userInfos; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtUserID;
        public TextView txtUserName;
    }

    public UserInfoItemAdapter(Context context, List<UserInfo> userInfos) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.userInfos = userInfos;
    }


    @Override
    public int getCount() {
        return userInfos==null?0: userInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return userInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        // 自定义视图
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();

            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_prod_userinfoitem_listview,null);
            listItemView.txtUserID = (TextView) convertView.findViewById(R.id.txt_UserID);
            listItemView.txtUserName = (TextView) convertView.findViewById(R.id.txt_UserName);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        UserInfo userInfo=userInfos.get(selectID);
        listItemView.txtUserID.setText(userInfo.getUserNo());
        listItemView.txtUserName.setText(userInfo.getUserName());
        return convertView;
    }

}
