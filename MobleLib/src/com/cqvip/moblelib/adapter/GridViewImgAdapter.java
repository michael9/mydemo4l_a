package com.cqvip.moblelib.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.activity.ActivityDlg;
import com.cqvip.moblelib.activity.MainMenuActivity;
import com.cqvip.moblelib.constant.GlobleData;

public class GridViewImgAdapter extends BaseAdapter {

	private  Class[] activities;
	private Context mContext;

	// 定义整型数组 即图片源

	private Integer[] mImageIds = { R.drawable.sy_anniu_03,
			R.drawable.sy_anniu_05, R.drawable.sy_anniu_07,
			R.drawable.sy_anniu_12, R.drawable.sy_anniu_14,
			R.drawable.sy_anniu_21, R.drawable.sy_anniu_18,
			R.drawable.sy_anniu_19, R.drawable.sy_anniu_20 };

	private Integer[] mImageIds_big = { R.drawable.sy_anniu_03big,
			R.drawable.sy_anniu_05big, R.drawable.sy_anniu_07big,
			R.drawable.sy_anniu_12big, R.drawable.sy_anniu_14big,
			R.drawable.sy_anniu_21big, R.drawable.sy_anniu_18big,
			R.drawable.sy_anniu_19big, R.drawable.sy_anniu_20big };

	private int[] mTitle = { R.string.main_guide, R.string.main_search,
			R.string.main_ebook,
			R.string.main_readingguide,
			R.string.main_ebookstore,// R.string.main_bookcomment
			R.string.serv_favorite, R.string.main_notice, R.string.main_borrow,
			R.string.main_order

	};

	public GridViewImgAdapter(Context c) {
		mContext = c;
	}
	public GridViewImgAdapter(Context c,Class[] activities) {
		mContext = c;
		this.activities = activities;
	}

	@Override
	public int getCount() {
		return mImageIds.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	// 获取图片ID
	@Override
	public long getItemId(int position) {
		return position;
	}

	//显示对话框
	private void showLoginDialog(int id) {
		MainMenuActivity.cantouch = true;
		Intent intent = new Intent(mContext, ActivityDlg.class);
		intent.putExtra("ACTIONID", id);
		((Activity) mContext).startActivityForResult(intent, id);
	}
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ImageView img;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_main_menu, null);

		}
		img = (ImageView) convertView.findViewById(R.id.img_main);
		TextView tx = (TextView) convertView.findViewById(R.id.txt_main);
		tx.setText(mContext.getResources().getString(mTitle[position]));
		img.setImageResource(mImageIds[position]);
		img.setTag(position);
		img.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int tag = (Integer) v.getTag();

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					img.setImageResource(mImageIds_big[tag]);
					break;
				case MotionEvent.ACTION_UP:
					img.setImageResource(mImageIds[tag]);
					if (!MainMenuActivity.cantouch)
						break;
					MainMenuActivity.cantouch = false;
					Intent intent = new Intent();
					switch (tag) {
					case 0:
					case 1:
					case 2:
						intent.setClass(mContext, activities[tag]);
						mContext.startActivity(intent);
						break;

					case 3:
						intent.setClass(mContext, ActivityDlg.class);
						intent.putExtra("ACTIONID", 0);
						intent.putExtra("MSGBODY", "该项正在紧张开发中...");
						intent.putExtra("BTN_CANCEL", 0);
						mContext.startActivity(intent);
						break;
					//个人中心	
					case 4:
					//我的收藏	
					case 5:
					//借阅管理	
					case 7:
					//书友圈	
					case 8:
						//判断是否登录
						if (GlobleData.islogin) {
							intent.setClass(mContext, activities[tag]);
							mContext.startActivity(intent);
						} else {
							showLoginDialog(tag);
						}
						break;
					case 6:
						intent.setClass(mContext, activities[tag]);
						mContext.startActivity(intent);
						break;

					default:
						break;
					}
					break;
				default:
					break;
				}
				return true;
			}
		});
		return convertView;
	}

}
