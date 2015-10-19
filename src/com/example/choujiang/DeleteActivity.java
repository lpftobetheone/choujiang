/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:SJS
 */
package com.example.choujiang;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.example.choujiang.adapter.DeleteNameListAdapter;


/**
 *@Title:
 *@Description:
 *@Author:liupf5
 *@Since:2015-9-23
 *@Version:1.1.0
 */
public class DeleteActivity extends Activity{
	
	private Context mContext;
	private ListView mDeleteList;
	private DeleteNameListAdapter mAdapter;
	private List<String> deleteList = new ArrayList<String>();
	
	
	private String allName = "";
	SharedPreferences getSp = null;
	SharedPreferences.Editor editor = null;
	
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mAdapter == null) {
					mAdapter = new DeleteNameListAdapter(mContext,
							deleteList);
					mDeleteList.setAdapter(mAdapter);
				} else {
					mAdapter.notifyDataSetChanged();
					mDeleteList.setAdapter(mAdapter);
				}
				break;
			}
		};
	};
	
	/* 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete);
		mContext = this;
		setTitle("");
		
		initViews();
		
		getFromSharedPreference();
	}

	/**
	 * 
	 * @Description:
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		mDeleteList = (ListView)this.findViewById(R.id.id_all_name);
		
		mDeleteList.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("删除吗？");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 添加到SharedPreferences中
						String name = deleteList.get(position);
						deleteList.remove(position);
						mAdapter.notifyDataSetChanged();
						
						allName = getSp.getString("namelist", "");
						String[] allNameArrays = allName.split(",");
						boolean isAdd = true;
						int startPosition = 0;
						int endPosition = 0;
						
						for (int i = 0; i < allNameArrays.length; i++) {
							if (allNameArrays[i].equals(name)) {
								// 得到重复字符串的终点位置
								endPosition = startPosition + name.length() + 1;
								allName = allName.substring(0, startPosition)
										+ allName.substring(endPosition);
								break;
							}
							startPosition += allNameArrays[i].length() + 1;
						}
						
						StringBuilder sb = new StringBuilder(allName);
						editor.putString("namelist", sb.toString());
						editor.commit();
						
					}
				});

				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 什么都不操作
					}
				});
				builder.show();
				return false;
			}
			
		});
		
	}
	
	public void getFromSharedPreference() {
		getSp = getSharedPreferences("nameList",
				Activity.MODE_PRIVATE);
		editor = getSp.edit();
		allName = getSp.getString("namelist", "");
		
		String[] allNameArrays = allName.split(",");

		// 第一次从SharedPreferences中获取到用户列表，复制到luckyList列表中
		for (int i = 0; i < allNameArrays.length; i++) {
			String people = allNameArrays[i];
			deleteList.add(people);
		}
		
		if(deleteList.size()>0){
			mHandler.sendEmptyMessage(1);
		}else{
			Toast.makeText(mContext, "还没有数据", Toast.LENGTH_SHORT).show();
		}
	}
}
