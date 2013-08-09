package com.cqvip.moblelib.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.activity.PeriodicalListActivity;

public class PeriodicalTypeFragment extends Fragment{

	private static final String[] MEDICAL = 
		{
		"医药卫生",   
		"医药卫生",
		"医药卫生",       
		"医药卫生",
		"医药卫生",
		"医药卫生", 
		"医药卫生"
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
		listview.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
				MEDICAL));
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(getActivity(),PeriodicalListActivity.class));
			}
		});
		return rootView;
	}


}
