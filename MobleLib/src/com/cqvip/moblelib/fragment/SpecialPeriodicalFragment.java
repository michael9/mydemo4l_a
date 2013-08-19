package com.cqvip.moblelib.fragment;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.mobelib.imgutils.RecyclingImageView;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.activity.PeriodicalContentActivity;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.fragment.basefragment.BaseAbstractFragment;
import com.cqvip.moblelib.model.Periodical;

/**
 * 特别推荐
 * @author luojiang
 *
 */
public class SpecialPeriodicalFragment extends BaseAbstractFragment implements AdapterView.OnItemClickListener{

	private ImageAdapter mAdapter;
	private GridView gridView;
	private HashMap<String, String> gparams; // 参数
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_special_periodic,
				container, false);
		gridView = (GridView) rootView.findViewById(R.id.gridView);
		gridView.setOnItemClickListener(this);
		//发送请求获取图片机url
		customProgressDialog.show();
		requestVolley(GlobleData.SERVER_URL
				+ "/qk/newlist.aspx", backlistener, null,
				Method.GET);
		
		return rootView;
	}
	private void requestVolley(String addr, Listener<String> mj,
			JSONObject js, int method) {

		try {
			StringRequest myjson = new StringRequest(method, addr,mj, el);
			mQueue.add(myjson);
			mQueue.start();
		} catch (Exception e) {

		}

	}

	ErrorListener el = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
		}
	};

	private Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			try {
				Periodical temp =Periodical.formObject(response,Task.TASK_PERIODICAL_SPECIAL);
				List<Periodical> lists = temp.qklist;
				if (lists != null && !lists.isEmpty()) {
//					String[] imgs = new String[lists.size()];
//					for(int i = 0;i<lists.size();i++){
//						imgs[i]=lists.get(i).getImgurl();
//					}
					mAdapter = new ImageAdapter(getActivity(),lists);
					gridView.setAdapter(mAdapter);
				}
			} catch (Exception e) {
				return;
			}
		}
	};
	
	
	private class ImageAdapter extends BaseAdapter {

        private final Context mContext;
        private int mItemHeight = 0;
        private int mNumColumns = 0;
        private int mActionBarHeight = 0;
        private GridView.LayoutParams mImageViewLayoutParams;
        //private String[] imgsurl;
        private List<Periodical> mlists;

        public ImageAdapter(Context context,List<Periodical> mlists) {
            super();
            mContext = context;
            this.mlists = mlists;
            mImageViewLayoutParams = new GridView.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            // Calculate ActionBar height
            TypedValue tv = new TypedValue();
            if (context.getTheme().resolveAttribute(
                    android.R.attr.actionBarSize, tv, true)) {
                mActionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, context.getResources().getDisplayMetrics());
            }
        }
        public List<Periodical> getList(){
        	return mlists;
        }
        
        @Override
        public int getCount() {
            // Size + number of columns for top empty row
            return mlists.size() + mNumColumns;
        }

        @Override
        public Object getItem(int position) {
            return position < mNumColumns ?
                    null : mlists.get(position - mNumColumns).getImgurl();
        }

        @Override
        public long getItemId(int position) {
            return position < mNumColumns ? 0 : position - mNumColumns;
        }

        @Override
        public int getViewTypeCount() {
            // Two types of views, the normal ImageView and the top row of empty views
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return (position < mNumColumns) ? 1 : 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            // First check if this is the top row
            if (position < mNumColumns) {
                if (convertView == null) {
                    convertView = new View(mContext);
                }
                // Set empty view with height of ActionBar
                convertView.setLayoutParams(new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, mActionBarHeight));
                return convertView;
            }

            // Now handle the main ImageView thumbnails
            ImageView imageView;
            if (convertView == null) { // if it's not recycled, instantiate and initialize
                imageView = new RecyclingImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(mImageViewLayoutParams);
            } else { // Otherwise re-use the converted view
                imageView = (ImageView) convertView;
            }

            // Check the height matches our calculated column width
            if (imageView.getLayoutParams().height != mItemHeight) {
                imageView.setLayoutParams(mImageViewLayoutParams);
            }

            // Finally load the image asynchronously into the ImageView, this also takes care of
            // setting a placeholder image while the background thread runs
            mImageFetcher.loadImage(mlists.get(position - mNumColumns).getImgurl(), imageView);
            return imageView;
        }

        /**
         * Sets the item height. Useful for when we know the column width so the height can be set
         * to match.
         *
         * @param height
         */
        public void setItemHeight(int height) {
            if (height == mItemHeight) {
                return;
            }
            mItemHeight = height;
            mImageViewLayoutParams =
                    new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
            mImageFetcher.setImageSize(height);
            notifyDataSetChanged();
        }

        public void setNumColumns(int numColumns) {
            mNumColumns = numColumns;
        }

        public int getNumColumns() {
            return mNumColumns;
        }
    }


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Periodical periodical = mAdapter.getList().get(position);
		Log.i("onItemClick","========================"+periodical.getImgurl());
		 if(periodical!=null){
		Intent _intent = new Intent(getActivity(),PeriodicalContentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("periodical", periodical);
		_intent.putExtra("detaiinfo", bundle);
	    startActivity(_intent);
		
     	}
	}
}
