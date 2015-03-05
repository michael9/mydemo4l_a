package com.cqvip.moblelib.fragment;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.mobelib.imgutils.RecyclingImageView;
import com.cqvip.moblelib.activity.PeriodicalClassfyActivity;
import com.cqvip.moblelib.activity.PeriodicalContentActivity;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Periodical;
import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

/**
 * 特别推荐
 * @author luojiang
 *
 */
public class SpecialPeriodicalFragment extends Fragment implements AdapterView.OnItemClickListener{

private int mImageThumbSize;
	private int mImageThumbSpacing;
	private ImageAdapter mAdapter;
	private GridView gridView;
	private HashMap<String, String> gparams; // 参数
	private List<Periodical> lists = null;
    private ImageFetcher mImageFetcher;
	private RequestQueue mQueue;
	private CustomProgressDialog customProgressDialog;
	public SpecialPeriodicalFragment(){
		
	}
	
	public static SpecialPeriodicalFragment instance(int position){
		SpecialPeriodicalFragment ft = new SpecialPeriodicalFragment();
		Bundle args = new Bundle();
		args.putInt("type", position);
		ft.setArguments(args);
		return ft;
	}
	
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		// TODO Auto-generated method stub
//		super.onSaveInstanceState(outState);
//		if(lists!=null){
//		outState.putSerializable("imgs", (Serializable) lists);
//		}
//	}
	@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
		}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		 mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
	     mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);
	     mQueue = ((PeriodicalClassfyActivity) getActivity()).getRequestQueue();
         customProgressDialog = ((PeriodicalClassfyActivity) getActivity()).getCustomDialog();
         
	}
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);

	        // Use the parent activity to load the image asynchronously into the ImageView (so a single
	        // cache can be used over all pages in the ViewPager
	        if (PeriodicalClassfyActivity.class.isInstance(getActivity())) {
	            mImageFetcher = ((PeriodicalClassfyActivity) getActivity()).getImageFetcher();
	            mImageFetcher.setLoadingImage(R.drawable.empty_photo);
	            mImageFetcher.setImageFadeIn(true);
	        }

	    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View rootView = inflater.inflate(R.layout.fragment_special_periodic,
				container, false);
		gridView = (GridView) rootView.findViewById(R.id.gridView);
		gridView.setOnItemClickListener(this);
//	     if(savedInstanceState!=null){
//	    	 lists = (List<Periodical>) savedInstanceState.getSerializable("imgs");
//	    	 if(lists!=null){
//	    	 mAdapter = new ImageAdapter(getActivity(),lists);
//	    	 gridView.setAdapter(mAdapter);
//	    	 }else{
//	    		 mAdapter = new ImageAdapter(getActivity(),null);
//		    	 customProgressDialog.show();
//		    	 requestVolley(GlobleData.SERVER_URL
//		    			 + "/qk/newlist.aspx", backlistener, null,
//		    			 Method.GET);
//	    	 }
//	     }else{
	    	 //发送请求获取图片机url
	    	 mAdapter = new ImageAdapter(getActivity(),null);
	    	 customProgressDialog.show();
	    	 requestVolley(GlobleData.SERVER_URL
	    			 + "/qk/newlist.aspx", backlistener, null,
	    			 Method.GET);
	//     }
	     
	     gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
	            @Override
	            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
	                // Pause fetcher to ensure smoother scrolling when flinging
	                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
	                    mImageFetcher.setPauseWork(true);
	                } else {
	                    mImageFetcher.setPauseWork(false);
	                }
	            }

	            @Override
	            public void onScroll(AbsListView absListView, int firstVisibleItem,
	                    int visibleItemCount, int totalItemCount) {
	            }
	        });
 
	     
		gridView.getViewTreeObserver().addOnGlobalLayoutListener(
	                new ViewTreeObserver.OnGlobalLayoutListener() {
	                    @Override
	                    public void onGlobalLayout() {
	                        if (mAdapter.getNumColumns() == 0) {
	                            final int numColumns = (int) Math.floor(
	                            		gridView.getWidth() / (mImageThumbSize + mImageThumbSpacing));
	                            if (numColumns > 0) {
	                                final int columnWidth =
	                                        (gridView.getWidth() / numColumns) - mImageThumbSpacing;
	                                mAdapter.setNumColumns(numColumns);
	                                mAdapter.setItemHeight((int)(columnWidth*1.4));
	                            }
	                        }
	                    }
	                });
		
		
		
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
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			Tool.ShowMessages(getActivity(), getResources()
					.getString(R.string.loadfail));
		}
	};

	private Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			try {
				Periodical temp =Periodical.formObject(response,Task.TASK_PERIODICAL_SPECIAL);
				lists = temp.qklist;
				if (lists != null && !lists.isEmpty()) {
//					String[] imgs = new String[lists.size()];
//					for(int i = 0;i<lists.size();i++){
//						imgs[i]=lists.get(i).getImgurl();
//					}
					mAdapter = new ImageAdapter(getActivity(),lists);
					gridView.setAdapter(mAdapter);
				}
				customProgressDialog.dismiss();
			} catch (Exception e) {
				customProgressDialog.dismiss();
				return;
			}
		}
	};
	  @Override
	    public void onResume() {
	        super.onResume();
	        mImageFetcher.setExitTasksEarly(false);
	        mAdapter.notifyDataSetChanged();
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        mImageFetcher.setPauseWork(false);
	        mImageFetcher.setExitTasksEarly(true);
	        mImageFetcher.flushCache();
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        mImageFetcher.closeCache();
	    }
	    
	private class ImageAdapter extends BaseAdapter {
		private int mNumColumns = 0;
        private final Context mContext;
        private int mItemHeight = 0;
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
//                convertView.setLayoutParams(new AbsListView.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, mActionBarHeight));
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
		Periodical periodical = mAdapter.getList().get(position-mAdapter.getNumColumns());
		 if(periodical!=null){
		Intent _intent = new Intent(getActivity(),PeriodicalContentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("periodical", periodical);
		_intent.putExtra("detaiinfo", bundle);
	    startActivity(_intent);
		
     	}
	}
}
