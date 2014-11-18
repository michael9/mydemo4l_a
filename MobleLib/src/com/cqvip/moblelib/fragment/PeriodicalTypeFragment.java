package com.cqvip.moblelib.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.activity.EbookDetailActivity;
import com.cqvip.moblelib.activity.PeriodicalClassfyActivity;
import com.cqvip.moblelib.activity.PeriodicalListActivity;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.fragment.basefragment.BaseAbstractFragment;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

/**
 * �����б� ��ҽҩ�����̣����...
 * @author luojiang
 *
 */
public class PeriodicalTypeFragment extends Fragment {

	private ListView listview;
	private int sendtype;// ����
	private HashMap<String, String> gparams; // ����
	private MyAdapter adapter;
	private RequestQueue mQueue;
	private CustomProgressDialog customProgressDialog;
	
	
	public PeriodicalTypeFragment(){
		
	}
	
	public static PeriodicalTypeFragment instance(int position){
		PeriodicalTypeFragment ft = new PeriodicalTypeFragment();
		Bundle args = new Bundle();
		args.putInt("type", position);
		ft.setArguments(args);
		return ft;
	}
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 if (PeriodicalClassfyActivity.class.isInstance(getActivity())) {
	            mQueue = ((PeriodicalClassfyActivity) getActivity()).getRequestQueue();
	            customProgressDialog = ((PeriodicalClassfyActivity) getActivity()).getCustomDialog();
	        }
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_periodical_type,
				container, false);
		Bundle bundle = getArguments();
		int type = bundle.getInt("type");
		// TextView tv = (TextView) rootView.findViewById(R.id.txt_periodical);
		// tv.setText(title);
		// �ж����ĸ�����
		listview = (ListView) rootView.findViewById(R.id.lv_periodical_type);
		getHomePage(type);
		// ��ȡ���
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ShortBook mtype = adapter.getList().get(position);
				if (mtype != null) {
					Intent _intent = new Intent(getActivity(), PeriodicalListActivity.class);
					_intent.putExtra("classid", mtype.getId());
					startActivity(_intent);
				}
				
			}
		});
		return rootView;
	}

	private void getHomePage(int type) {
		gparams = new HashMap<String, String>();
		switch (type) {
		// ҽҩ����
		case Constant.MEDIAL:
			gparams.put("classid", GlobleData.MEDIAL_TYPEID + "");
			// requestVolley
			break;
		case Constant.ENGINE:
			gparams.put("classid", GlobleData.ENGINE_TYPEID + "");
			break;
		case Constant.SOCIAL:
			gparams.put("classid", GlobleData.SOCIAL_TYPEID + "");
			break;
		case Constant.NATURE:
			gparams.put("classid", GlobleData.NATURE_TYPEID + "");
			break;
		case Constant.FORESTS:
			gparams.put("classid", GlobleData.FORESTS_TYPEID + "");
			break;
		default:
			gparams.put("classid", GlobleData.MEDIAL_TYPEID + "");
			break;

		}
		requestVolley(gparams, GlobleData.SERVER_URL + "/qk/classlist.aspx",
				backlistener, Method.POST);
	}

	private void requestVolley(HashMap<String, String> gparams, String url,
			Listener<String> listener, int post) {
		final HashMap<String, String> gparams_t = gparams;
		StringRequest mys = new StringRequest(post, url, listener, el) {
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				return gparams_t;
			};
		};
		mQueue.add(mys);
		mQueue.start();

	}

	ErrorListener el = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			Tool.ShowMessages(getActivity(), getResources()
					.getString(R.string.loadfail));
		}
	};
	private Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			try {
				List<ShortBook> lists = ShortBook.formList(
						Task.TASK_PERIODICAL_TYPE, response);
				if (lists != null && !lists.isEmpty()) {
					adapter = new MyAdapter(getActivity(), lists);
					listview.setAdapter(adapter);
				}
			} catch (Exception e) {
				return;
			}
		}
	};

	class MyAdapter extends BaseAdapter {
		private FragmentActivity fragment;
		private List<ShortBook> mList;

		public MyAdapter(FragmentActivity activity, List<ShortBook> lists) {
			this.fragment = activity;
			this.mList = lists;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		public List<ShortBook> getList(){
			return mList;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if (convertView == null) {

				v = fragment.getLayoutInflater().inflate(
						R.layout.item_periodical_type, null);
			} else {
				v = convertView;
			}
			TextView tx = (TextView) v.findViewById(R.id.txt_type_peridical);
			tx.setText(mList.get(position).getDate());
			return v;
		}

	}

}
