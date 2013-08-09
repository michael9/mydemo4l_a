package com.cqvip.moblelib.activity;

import java.util.Arrays;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.view.picker.ArrayWheelAdapter;
import com.cqvip.moblelib.view.picker.OnWheelChangedListener;
import com.cqvip.moblelib.view.picker.WheelView;

/**
 * 期刊详细界面
 * @author luojiang
 *
 */
public class PeriodicalContentActivity extends Activity {
	private String mYear = null;
	private String mMonth = null;
	private ListView listview;
	private Context context;
	private TextView txt_date;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_periodical_content_main);
		context = this;
		listview = (ListView) findViewById(R.id.listView1);
		View v = LayoutInflater.from(this).inflate(R.layout.activity_periodical_content_up, null);
		listview.addHeaderView(v);
		txt_date = (TextView) findViewById(R.id.txt_year_month);
		listview.setAdapter(new MyAdapter(context));
		txt_date.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				final Dialog dialog = new Dialog(v.getContext(), R.style.dialog_fullscreen);
				dialog.setContentView(R.layout.dialog_picker);
				 WheelView country = (WheelView) dialog.findViewById(R.id.country);
			        //String countries[] = new String[] {"USA", "Canada", "Ukraine", "France"};
			        final int length = 2013-1996 ;
			        final String[] count = new String[length+1];
			        for(int i=1996;i<=1996+length;i++){
			        	count[i-1996]=i+"";
			        }
			        System.out.println(Arrays.toString(count));
			        final String[] mouth = new String[6];
			        for(int i=1;i<=6;i++){
			        	mouth[i-1]=i+"";
			        }
			        System.out.println(Arrays.toString(mouth));
			        String[] mouth2 = new String[3];
			        for(int i=1;i<=3;i++){
			        	mouth2[i-1]=i+"";
			        }
			        System.out.println("mount2"+Arrays.toString(mouth2));
			        country.setVisibleItems(5);
			        country.setLabel("年");
			        country.setAdapter(new ArrayWheelAdapter<String>(count));

//			        final String cities[][] = new String[][] {
//			        		new String[] {"New York", "Washington", "Chicago", "Atlanta", "Orlando"},
//			        		new String[] {"Ottawa", "Vancouver", "Toronto", "Windsor", "Montreal"},
//			        		new String[] {"Kiev", "Dnipro", "Lviv", "Kharkiv"},
//			        		new String[] {"Paris", "Bordeaux"},
//			        		};
			        final String cities[][] = new String[length+1][];
			        for(int i=0;i<length+1;i++){
			        	if(i == length){
			        		cities[i]=mouth2;
			        	}else{
			        	cities[i]=mouth;
			        	}
			        }
			        
			        final WheelView city = (WheelView) dialog.findViewById(R.id.city);
			        final Button confirm = (Button) dialog.findViewById(R.id.btn_date_confirm);
			        city.setVisibleItems(5);
			        city.setLabel("期");
			        city.setCyclic(true);
			        System.out.println("哈哈哈哈"+cities[length].length);
			        
			        country.addChangingListener(new OnWheelChangedListener() {
						public void onChanged(WheelView wheel, int oldValue, int newValue) {
							city.setAdapter(new ArrayWheelAdapter<String>(cities[newValue]));
							//期数小于oldvalue
							if(cities[newValue].length<cities[oldValue].length){
							city.setCurrentItem(cities[newValue].length / 2);
							}
							mYear = count[newValue];
							confirm.setText(mYear+"年第"+mMonth+"期");
						}
					});
			        city.addChangingListener(new OnWheelChangedListener() {
						public void onChanged(WheelView wheel, int oldValue, int newValue) {
							mMonth =mouth[newValue];
							confirm.setText(mYear+"年第"+mMonth+"期");
						}
					});
			        
			        country.setCurrentItem(length);
			        city.setCurrentItem(cities[length].length-1);
			        mYear = count[length];
			        mMonth = mouth[cities[length].length-1];
				confirm.setText(mYear+"年第"+mMonth+"期");
				confirm.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						txt_date.setText(mYear+"年第"+mMonth+"期：目录");
						//判断是否日期发生变化，变化则重新加载
					}
				});
				dialog.show();
			}
		});
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(context, "电子书详细", 1).show();
				
			}
		});
	}
	class MyAdapter extends BaseAdapter{
		private Context context;
		public MyAdapter(Context context){
			this.context = context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView==null){
				convertView=LayoutInflater.from(context).inflate(R.layout.item_news, null);
			}
			
			return convertView;
		}
		
	}
}