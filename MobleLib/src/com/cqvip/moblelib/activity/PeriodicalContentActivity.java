package com.cqvip.moblelib.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.Periodical;
import com.cqvip.moblelib.model.PeriodicalYear;
import com.cqvip.moblelib.view.picker.ArrayWheelAdapter;
import com.cqvip.moblelib.view.picker.OnWheelChangedListener;
import com.cqvip.moblelib.view.picker.WheelView;

/**
 * �ڿ���ϸ����
 * @author luojiang
 *
 */
public class PeriodicalContentActivity extends BaseImageActivity{
	private String mYear = null;
	private String mMonth = null;
	private ListView listview;
	private Context context;
	private TextView txt_date;
	private TextView title,directordept,publisher,chiefedit,pubcycle,price,num;
	private View upView;
	private ImageView img,img_back;
	private String gch;
	private Map<String, String> gparams;
	private ArrayList<PeriodicalYear> yearlist;
	private String year,month;//��¼�����Ƿ����仯
	private int yaer_record,month_record;
	private MyAdapter adapter;
	private Periodical perio;
	private View progress;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_periodical_content_main);
		context = this;
		
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		perio = (Periodical) bundle.getSerializable("periodical");
		
		findView();
		initViewFirst();
		initdate();
	
		setListener();
		
		txt_date.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				final Dialog dialog = new Dialog(v.getContext(), R.style.dialog_fullscreen);
				dialog.setContentView(R.layout.dialog_picker);
				//��¼��λ��
				final int ty = yaer_record;
				final int tm = month_record;
				//�ж������Ƿ����仯
				 WheelView country = (WheelView) dialog.findViewById(R.id.country);
			        final int length = yearlist.size() ;
			        final String[] count = new String[length];
			        for(int i=0;i<length;i++){
			        	count[i]=yearlist.get(i).getYear();
			        }
			        country.setVisibleItems(5);
			        country.setLabel("��");
			        country.setAdapter(new ArrayWheelAdapter<String>(count));
			        final WheelView city = (WheelView) dialog.findViewById(R.id.city);
			        final Button confirm = (Button) dialog.findViewById(R.id.btn_date_confirm);
			        city.setAdapter(new ArrayWheelAdapter<String>(yearlist.get(0).getNum()));
			        city.setVisibleItems(5);
			        city.setLabel("��");
			        city.setCyclic(true);
			        country.addChangingListener(new OnWheelChangedListener() {
						public void onChanged(WheelView wheel, int oldValue, int newValue) {
							String[] mNum = yearlist.get(newValue).getNum();
							city.setAdapter(new ArrayWheelAdapter<String>(mNum));
							//����С��oldvalue
							if( yearlist.get(newValue).getNum().length< yearlist.get(oldValue).getNum().length){
							city.setCurrentItem(yearlist.get(newValue).getNum().length / 2);
							}
							mYear = yearlist.get(newValue).getYear();
							confirm.setText(mYear+"���"+mMonth+"��");
							//��¼��λ��
							yaer_record = newValue;
						}
					});
			        city.addChangingListener(new OnWheelChangedListener() {
						public void onChanged(WheelView wheel, int oldValue, int newValue) {
							mMonth =  newValue+1+"";
							confirm.setText(mYear+"���"+mMonth+"��");
							//��¼��λ��
							month_record = newValue;
						}
					});
			        
			        country.setCurrentItem(yaer_record);
			        String[] arr = yearlist.get(yaer_record).getNum();
			        city.setCurrentItem(month_record);
			        mYear =  yearlist.get(yaer_record).getYear();
			        mMonth = arr[month_record];
				confirm.setText(mYear+"���"+mMonth+"��");
				confirm.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						//�����Ƿ����仯
						if(ty!=yaer_record||tm!=month_record){
							//�����仯����������
							Log.i("Periodical","======����=======");
							gparams = new HashMap<String, String>();
							gparams.put("gch", gch);
							gparams.put("years",mYear);
							gparams.put("num", mMonth);
							requestVolley(GlobleData.SERVER_URL + "/zk/search.aspx",
									backlistener_content, Method.POST);
							progress.setVisibility(View.VISIBLE);
						}
						txt_date.setText(mYear+"���"+mMonth+"�ڣ�Ŀ¼");
						//�ж��Ƿ����ڷ����仯���仯�����¼���
					}
				});
				dialog.setCancelable(false);
				dialog.show();
			}
		});
		
	}
	private void initViewFirst() {
		title.setText(perio.getName());
		price.setText(getResources().getString(R.string.title_cnno)+perio.getCnno());
		num.setText(getResources().getString(R.string.title_issn)+perio.getIssn());
		if (!TextUtils.isEmpty(perio.getImgurl())) {
	    	mImageFetcher.loadImage(perio.getImgurl(), img);
	    } else {
	    	img.setImageDrawable(context.getResources().getDrawable(
	    			R.drawable.defaut_book));
	    }
		
		
	}
	/**
	 * ���������ȡ����
	 */
	private void initdate() {
		gch = perio.getGch();
		gparams = new HashMap<String, String>();
		gparams.put("gch", gch);
		requestVolley(GlobleData.SERVER_URL + "/qk/detail.aspx",
				backlistener, Method.POST);
	}
	private void findView() {
		listview = (ListView) findViewById(R.id.listView1);
		upView = LayoutInflater.from(this).inflate(R.layout.activity_periodical_content_up, null);
		listview.addHeaderView(upView);
		progress = findViewById(R.id.footer_progress);
		//����
		  View v = findViewById(R.id.head_bar);
	     TextView tv = (TextView) v.findViewById(R.id.txt_header);
	     tv.setText(getResources().getString(R.string.main_periodical));
	     img_back = (ImageView) v.findViewById(R.id.img_back_header);
		//����
		title = (TextView)findViewById(R.id.periodical_title_txt);
		directordept  = (TextView)findViewById(R.id.periodical_host1_txt);
		publisher  = (TextView)findViewById(R.id.periodical_host2_txt);
		chiefedit  = (TextView)findViewById(R.id.periodical_author_txt);
		pubcycle = (TextView)findViewById(R.id.periodical_time_txt);
		price = (TextView)findViewById(R.id.periodical_price_txt);
		num = (TextView)findViewById(R.id.periodical_num_txt);
		//�ڿ�����
		txt_date = (TextView) findViewById(R.id.txt_year_month);
		//ͼƬ
		img = (ImageView) findViewById(R.id.periodical_icon_img);
		
	}
	private void setListener() {
		img_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EBook book = adapter.getLists().get(position);
				if (book != null) {
					Intent _intent = new Intent(context, EbookDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("book", book);
					_intent.putExtra("detaiinfo", bundle);
					startActivity(_intent);
				}
			}
		});
		
	}
	private void initView(Periodical periodical) {
		
		directordept.setText(getResources().getString(R.string.title_perio_chiefdept)+periodical.getDirectordept());
		publisher.setText(getResources().getString(R.string.title_perio_publisher)+periodical.getPublisher());
		chiefedit.setText(getResources().getString(R.string.title_perio_chiefeditor)+periodical.getChiefeditor()==null?"":periodical.getChiefeditor());
		pubcycle.setText(getResources().getString(R.string.title_perio_type)+periodical.getPubcycle()+","+periodical.getSize());
		
		
		//��ʼ���ڿ�����
		year = yearlist.get(0).getYear();
		String[] arr = yearlist.get(0).getNum();
		month = arr[arr.length-1];
		txt_date.setText(year+"���"+month+"�ڣ�Ŀ¼");
		yaer_record = 0;
		month_record = arr.length-1;
	
		
	}
	Listener<String> backlistener_content = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			customProgressDialog.dismiss();
			progress.setVisibility(View.GONE);
			try {
				List<EBook> lists = EBook.formList(response);
				adapter = new MyAdapter(context,lists);
				listview.setAdapter(adapter);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};
	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			customProgressDialog.dismiss();
			try {
				Periodical periodical =Periodical.formObject(response,Task.TASK_PERIODICAL_DETAIL);	
				yearlist = periodical.getYearsnumlist();
				//���view�ĳ�ʼ��;
				initView(periodical);
				//��ȡ�����ڿ�Ŀ¼�鼮
				String[] tmpary = yearlist.get(0).getNum();
				gparams = new HashMap<String, String>();
				gparams.put("gch", periodical.getGch());
				gparams.put("years", yearlist.get(0).getYear());
				gparams.put("num",  tmpary[tmpary.length-1]);
				requestVolley(GlobleData.SERVER_URL + "/zk/search.aspx",
						backlistener_content, Method.POST);
				progress.setVisibility(View.VISIBLE);
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	};
	ErrorListener el = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();

		}
	};
	
	private void requestVolley(String addr, Listener<String> bl, int method) {
		try {
			StringRequest mys = new StringRequest(method, addr, bl, el) {
				protected Map<String, String> getParams()
						throws com.android.volley.AuthFailureError {
					return gparams;
				};
			};
			mQueue.add(mys);
			mQueue.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class MyAdapter extends BaseAdapter{
		private Context context;
		private List<EBook> mlists;
		public MyAdapter(Context context){
			this.context = context;
		}
		public MyAdapter(Context context2, List<EBook> lists) {
			this.context = context2;
			this.mlists = lists;
		}
		public List<EBook> getLists(){
			return mlists;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(mlists == null){
				return 0;
			}
			return mlists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mlists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if(convertView==null){
				v =LayoutInflater.from(context).inflate(R.layout.item_news, null);
			}else{
				v = convertView;
			}
			TextView tv = (TextView) v.findViewById(R.id.tv_item_topic);
				tv.setText(mlists.get(position).getTitle_c());
			return v;
		}
		
	}
}