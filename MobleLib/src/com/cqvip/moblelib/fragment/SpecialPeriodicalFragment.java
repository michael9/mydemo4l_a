package com.cqvip.moblelib.fragment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.mobelib.imgutils.RecyclingImageView;
import com.cqvip.mobelib.imgutils.ImageCache.ImageCacheParams;
import com.cqvip.moblelib.xxu.R;
import com.cqvip.moblelib.activity.PeriodicalClassfyActivity;
import com.cqvip.moblelib.activity.PeriodicalContentActivity;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.fragment.basefragment.BaseAbstractFragment;
import com.cqvip.moblelib.model.Periodical;
import com.cqvip.moblelib.view.CustomProgressDialog;

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
         customProgressDialog.show();
    	 requestVolley(GlobleData.SERVER_URL
    			 + "/qk/newlist.aspx", backlistener, null,
    			 Method.GET);
         
    	 ImageCacheParams cacheParams = new ImageCacheParams(getActivity(), GlobleData.IMAGE_CACHE_DIR);
         cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 12.5% of app memory
    	 
    	 mImageFetcher = new ImageFetcher(getActivity(), getResources().getDimensionPixelSize(R.dimen.bookicon_width),
				   getResources().getDimensionPixelSize(R.dimen.bookicon_height));
    	 
	    mImageFetcher.setLoadingImage(R.drawable.empty_photo);
	    mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
         
         
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View rootView = inflater.inflate(R.layout.fragment_special_periodic,
				container, false);
		gridView = (GridView) rootView.findViewById(R.id.gridView);
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
			try {
				Periodical temp =Periodical.formObject(response,Task.TASK_PERIODICAL_SPECIAL);
				lists = temp.qklist;
				Log.i("Periodical","list"+lists.size());
				if (lists != null && !lists.isEmpty()) {

					mAdapter = new ImageAdapter(getActivity(),lists);
					gridView.setAdapter(mAdapter);
					gridView.setOnItemClickListener(SpecialPeriodicalFragment.this);
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
				
				
				}
				mImageFetcher.setExitTasksEarly(false);
				mAdapter.notifyDataSetChanged();
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
	        if(mImageFetcher!=null){
	        mImageFetcher.setExitTasksEarly(false);
	        }
	        if(mAdapter!=null){
	        mAdapter.notifyDataSetChanged();
	        }
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
        }
        public List<Periodical> getList(){
        	return mlists;
        }
        
        @Override
        public int getCount() {
            // Size + number of columns for top empty row
            return mlists.size();
        }

        @Override
        public Object getItem(int position) {
            return mlists.get(position).getImgurl();
        }

        @Override
        public long getItemId(int position) {
            return  position;
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
            mImageFetcher.loadImage(mlists.get(position).getImgurl(), imageView);
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
		 if(periodical!=null){
		Intent _intent = new Intent(getActivity(),PeriodicalContentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("periodical", periodical);
		_intent.putExtra("detaiinfo", bundle);
	    startActivity(_intent);
		
     	}
	}
}
