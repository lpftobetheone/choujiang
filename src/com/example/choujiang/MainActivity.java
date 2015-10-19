package com.example.choujiang;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choujiang.adapter.LuckyNameListAdapter;

public class MainActivity extends Activity implements OnClickListener {

	private TextView mName;
	private ListView mNameList;
	private ImageView mBtnSubmit;
	private ImageView mBtnStop;
	private ImageView mBtnClear;

	private Thread mThread;

	private int number;
	Random random = new Random();
	private boolean isContinue = true;
	private boolean isStop = false;

	private LuckyNameListAdapter mAdapter;
	private Context mContext;
	private List<String> luckyList = new ArrayList<String>();
	private List<String> afterChooseList = new ArrayList<String>();

	private String awardName = "";
	private MediaPlayer player;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (luckyList != null && number < luckyList.size()) {
					mName.setText(luckyList.get(number));
				}
				break;
			case 2:
				if (mAdapter == null) {
					mAdapter = new LuckyNameListAdapter(mContext,
							afterChooseList);
					mNameList.setAdapter(mAdapter);
				} else {
					mAdapter.notifyDataSetChanged();
					mNameList.setAdapter(mAdapter);
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		
		setTitle("");
		initViews();
	}
	
	/* 
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
		
		if(luckyList!=null&& luckyList.size()>0){
			luckyList.clear();
		}
		initDatas();
		
//		Toast.makeText(mContext, "���¿�ʼ�齱", Toast.LENGTH_SHORT).show();
		if (afterChooseList != null && afterChooseList.size() > 0) {
			afterChooseList.clear();
			mAdapter.notifyDataSetChanged();
			mNameList.setAdapter(mAdapter);
		}
		
	}

	/**
	 * 
	 * @Description:
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		mName = (TextView) this.findViewById(R.id.id_name);
		mNameList = (ListView) this.findViewById(R.id.id_list);
		mBtnSubmit = (ImageView) this.findViewById(R.id.id_start);
		mBtnStop = (ImageView) this.findViewById(R.id.id_stop);
		mBtnClear = (ImageView) this.findViewById(R.id.id_clear);

		mBtnSubmit.setOnClickListener(this);
		mBtnStop.setOnClickListener(this);
		mBtnClear.setOnClickListener(this);
		
		player = MediaPlayer.create(mContext, R.raw.music);
		player.setLooping(true);
	}

	/* 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/* 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.id_add:
			showAddNameDialog();
			break;

		case R.id.id_delete:
			Intent intent = new Intent(mContext, DeleteActivity.class);
			startActivity(intent);
			break;
		}
		return false;
	}

	/**
	 * 
	 * @Description:
	 */
	private void showAddNameDialog() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View textEntryView = inflater.inflate(R.layout.dialoglayout, null);
		final EditText edtInput = (EditText) textEntryView
				.findViewById(R.id.edtInput);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("����������");
		builder.setView(textEntryView);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ��ӵ�SharedPreferences��
				String name = edtInput.getText().toString();
				if (!TextUtils.isEmpty(name)) {
					addToSharedPreference(name);
				} else {
					Toast.makeText(mContext, "������һ�������˶�", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ʲô��������
			}
		});
		builder.show();

	}

	// ��ʼ������
	public void initDatas() {

		String nameStr = getFromSharedPreference();
		String[] allNameArrays = nameStr.split(",");

		if(luckyList!=null&& luckyList.size()>0){
			luckyList.clear();
		}
		// ��һ�δ�SharedPreferences�л�ȡ���û��б����Ƶ�luckyList�б���
		for (int i = 0; i < allNameArrays.length; i++) {
			String people = allNameArrays[i];
			luckyList.add(people);
		}
	}

	/* 
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_start:
			player.start();
			mName.setTextSize(40);
			mName.setTextColor(R.color.gray);
			// ��LuckyList��ɾ���û�
			String str = awardName;
			for (int i = 0; i < luckyList.size(); i++) {
				if (luckyList.get(i) == str) {
					luckyList.remove(i);
					break;
				}
			}

			isContinue = true;
			isStop = true;//���Խ��е��ֹͣ��ť
			mThread = new Thread() {
				@Override
				public void run() {
					Looper.prepare();
					while (isContinue) {
						try {
							Thread.sleep(30);
							if (luckyList != null && luckyList.size() >= 2) {
								number = random.nextInt(luckyList.size());
							} else {
								Toast.makeText(mContext, "����������������˶�",
										Toast.LENGTH_SHORT).show();
								isContinue = false;
							}
							mHandler.sendEmptyMessage(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					Looper.loop();
				}
			};
			mThread.start();
			mBtnSubmit.setClickable(false);
			mBtnStop.setClickable(true);
			break;

		case R.id.id_stop:
			player.pause();
			player.seekTo(0);
			if(false == isStop){
				return;
			}
			isContinue = false;
			isStop = false;
			mBtnSubmit.setClickable(true);
			mBtnStop.setClickable(false);
			mName.setTextColor(Color.RED);
			mName.setTextSize(60);

			if (luckyList.size() > 1) {
				try {
					Thread.sleep(200);
					// ���н�������м�¼
					awardName = luckyList.get(number);
					recordLuckyPeople(luckyList.get(number));

					LayoutInflater inflater = LayoutInflater.from(mContext);
					View awardView = inflater
							.inflate(R.layout.dialogshow, null);
					TextView awardName = (TextView) awardView
							.findViewById(R.id.id_award_name);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					awardName.setText(luckyList.get(number));
					builder.setView(awardView);
					builder.setTitle("�н�");
					// builder.setMessage("���˶���"+ luckyList.get(number));
					builder.setPositiveButton("ȷ��", null);
					builder.show();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(mContext, "��ǰ����ֻ��һ��������Ŷ��", Toast.LENGTH_SHORT)
						.show();
			}

			break;

		case R.id.id_clear:
			if (afterChooseList != null && afterChooseList.size() > 0) {
				afterChooseList.clear();
				mAdapter.notifyDataSetChanged();
				mNameList.setAdapter(mAdapter);
				initDatas();
			}
			break;
		}
	}
	
	

	/* 
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		player.pause();
	}

	/* 
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		player.release();
		player = null;
	}

	public void addToSharedPreference(String keyValue) {
		SharedPreferences getSp = getSharedPreferences("nameList",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = getSp.edit();

		// �Ȼ�ȡ�����˵�name�б�
		String allName = getSp.getString("namelist", "");
		String[] allNameArrays = allName.split(",");
		boolean isAdd = true;
		for (int i = 0; i < allNameArrays.length; i++) {
			if (allNameArrays[i].equals(keyValue)) {
				Toast.makeText(mContext, "���Ѿ������˶���", Toast.LENGTH_SHORT).show();
				isAdd = false;
				break;
			}
		}

		if (isAdd) {
			// ��ӵ�SharedPreferences��
			StringBuilder sb = new StringBuilder(allName);
			sb.append(keyValue + ",");
			// ��ӵ�list��
			luckyList.add(keyValue);
			// mAdapter.notifyDataSetChanged();
			editor.putString("namelist", sb.toString());
			editor.commit();
		}
	}

	public String getFromSharedPreference() {
		SharedPreferences getSp = getSharedPreferences("nameList",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = getSp.edit();
		String allName = getSp.getString("namelist", "");
		return allName;
	}

	// ��¼�н��ߺ���
	public void recordLuckyPeople(String name) {
		afterChooseList.add(name);

		mHandler.sendEmptyMessage(2);
	}
}
