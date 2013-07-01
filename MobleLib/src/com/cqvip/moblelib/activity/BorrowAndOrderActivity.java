package com.cqvip.moblelib.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.BookAdapter;
import com.cqvip.moblelib.adapter.BorrowBookAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.BorrowBook;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>
 * 文件名称: BorrowAndOrderActivity.java
 * 文件描述: 借阅管理
 * 版权所有: 版权所有(C)2013-2020
 * 公          司: 重庆维普咨询有限公司
 * 内容摘要: 
 * 其他说明:
 * 完成日期： 201年5月10日
 * 修改记录: 
 * </p>
 * 
 * @author LHP,LJ
 */
public class BorrowAndOrderActivity extends BaseActivity implements IBookManagerActivity{

	public static final int BORROWLIST = 1;
	public static final int RENEW = 2;
	private Context context;
	private ViewPager mPager;//页卡内容
	private List<View> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private TextView t1, t2;// 页卡头标
	private int currIndex = 0;// 当前页卡编号
	private int offset = 0;// 动画图片偏移量
	private int bmpW;// 动画图片宽度
	private ListView listview;
	private BorrowBookAdapter adapter;
	private List<BorrowBook>  lists;
	private RelativeLayout noborrow_rl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_borrow_and_order2);
		context = this;
		View v = findViewById(R.id.borrow_title);
		TextView title = (TextView)v.findViewById(R.id.txt_header);
		title.setText(R.string.main_borrow);
		ImageView back = (ImageView)v.findViewById(R.id.img_back_header);
		noborrow_rl = (RelativeLayout) findViewById(R.id.noborrow_rl);
		//ImageView history = (ImageView)v.findViewById(R.id.btn_right_header);
		//history.setVisibility(View.VISIBLE);
		//history.setImageResource(R.drawable.lscx);
		customProgressDialog=CustomProgressDialog.createDialog(this);
		if(adapter!=null){
			
			
		}
		listview = (ListView)findViewById(R.id.borrow_list);
		init();
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		history.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//			}
//		});
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int postion,
					long id) {
				
				
			}
		}) ;
	
		
		
		
	}



	@Override
	public void init() {
		customProgressDialog.show();
		ManagerService.allActivity.add(this);
		if(GlobleData.userid==null){
			Tool.ShowMessages(context, "用户没有登陆");
		}else{
		HashMap map = new HashMap();
		map.put("id", GlobleData.readerid);
		Task tsHome = new Task(Task.TASK_BORROW_LIST, map);
		ManagerService.addNewTask(tsHome);
		}
	}

	@Override
	public void refresh(Object... obj) {
		customProgressDialog.dismiss();
		Integer type = (Integer)obj[0];
		switch(type){
		case BORROWLIST:
			lists = (List<BorrowBook>)obj[1];
			if(lists==null||lists.isEmpty()){
				listview.setVisibility(View.GONE);
				noborrow_rl.setVisibility(View.VISIBLE);
			   //Tool.ShowMessages(context, "查询无记录");
			}else {
				listview.setVisibility(View.VISIBLE);
				noborrow_rl.setVisibility(View.GONE);
				adapter = new BorrowBookAdapter(context,lists);
				listview.setAdapter(adapter);
			}
			break;
			
		case RENEW:
			//成功
			ShortBook result = (ShortBook)obj[1];
			if(result!=null){
//				if(result.getSucesss().equals("true")){
				for(int i=0;i<lists.size();i++){
					if(result.getId().equals(lists.get(i).getBarcode())){
						lists.get(i).setRenew(1);
						lists.get(i).setReturndate(result.getDate()+getResources().getString(R.string.alreadyrenew));
						adapter.notifyDataSetChanged();
						break;
					}
				  }
//				}
				Tool.ShowMessages(context, result.getMessage());
			}
			break;
			
		}
	}
	
}
