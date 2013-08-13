package com.cqvip.moblelib.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.activity.PeriodicalListActivity;

public class PeriodicalTypeFragment extends Fragment{

	private static final String[] MEDICAL = 
		{
		"ҽҩ����",   
		"ҽҩ����",
		"ҽҩ����",       
		"ҽҩ����",
		"ҽҩ����",
		"ҽҩ����", 
		"ҽҩ����"
		};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_periodical_type,container,false);
		
		Bundle bundle = getArguments();
		String title = bundle.getString("arg");
//		TextView tv = (TextView) rootView.findViewById(R.id.txt_periodical);
//		tv.setText(title);
		ListView listview = (ListView) rootView.findViewById(R.id.lv_periodical_type);
		listview.setAdapter(new MyAdapter(this));
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(getActivity(),PeriodicalListActivity.class));
			}
		});
		return rootView;
	}
	class MyAdapter extends BaseAdapter{
		private Fragment fragment;

		public MyAdapter(PeriodicalTypeFragment periodicalTypeFragment) {
			fragment = periodicalTypeFragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
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
			if(convertView ==null){
				convertView = fragment.getActivity().getLayoutInflater().inflate(R.layout.item_periodical_type, null);
			}
			
			// TODO Auto-generated method stub
			return convertView;
		}
		
	}

}
