/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:SJS
 */
package com.example.choujiang.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.choujiang.R;


/**
 *@Title:
 *@Description:
 *@Author:liupf5
 *@Since:2015-9-23
 *@Version:1.1.0
 */
public class DeleteNameListAdapter extends BaseAdapter{
	
	private Context mContext;
	private List<String> mPeopleList = new ArrayList<String>();
	
	public DeleteNameListAdapter(Context context,List<String> list){
		this.mContext = context;
		this.mPeopleList = list;
	}

	/* 
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPeopleList.size();
	}

	/* 
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mPeopleList.get(position);
	}

	/* 
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		String bean = mPeopleList.get(position);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		if(convertView==null){
			convertView = inflater.inflate(R.layout.namelist_item, null);
			holder = new ViewHolder();
			holder.tvName = (TextView)convertView.findViewById(R.id.id_lucky_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.tvName.setText(bean);
		return convertView;
	}
	
	class ViewHolder{
		private TextView tvName;
	}

}
