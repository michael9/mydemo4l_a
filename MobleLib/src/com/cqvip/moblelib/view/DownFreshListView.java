package com.cqvip.moblelib.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cqvip.moblelib.sychild.R;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * <p>
 * [�� Ҫ] ��Ч���listview
 * </p>
 * <p>
 * [�� ϸ] �»�Ч��listview��OnRefreshListener��onRefreshCompleteʵ��
 * </p>
 * <p>
 * [�� ��]
 * </p>
 * <p>
 * [�� ��] J2SE 1.6
 * </p>
 * *
 * 
 * @version 1.0
 * @author lj
 */
public class DownFreshListView extends ListView 
implements OnScrollListener,android.widget.AdapterView.OnItemLongClickListener{  
	  
    private static final String TAG = "DownFreshListView";  
  
    private final static int RELEASE_To_REFRESH = 0;  //�ͷ�ˢ��
    private final static int PULL_To_REFRESH = 1;  //����
    private final static int REFRESHING = 2;  //ˢ����
    private final static int DONE = 3;  //ˢ�����
    private final static int LOADING = 4;  //����
  
    // ʵ�ʵ�padding�ľ����������ƫ�ƾ���ı���  
    private final static int RATIO = 3;  
  
    private LayoutInflater inflater;  
  
    private LinearLayout headView;  
  
    private TextView tipsTextview;   //��ʾ
    private TextView lastUpdatedTextView;  //������
   // private ImageView arrowImageView;  //ͼ��
    private ProgressBar progressBar;  //ȦȦ
  
  
    private RotateAnimation animation;   //��ת����
    private RotateAnimation reverseAnimation;   
  
    // ���ڱ�֤startY��ֵ��һ�������touch�¼���ֻ����¼һ��  
    private boolean isRecored;  
  
    private int headContentWidth;  
    private int headContentHeight;  
  
    private int startY;  
    private int firstItemIndex;  
  
    private int state;  
  
    private boolean isBack;  
  
    private OnRefreshListener refreshListener;  
  
    private boolean isRefreshable;  
  
    public DownFreshListView(Context context) {  
        super(context);  
        init(context);  
    }  
  
    public DownFreshListView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init(context);  
    }  
  
    private void init(Context context) {  
        //setCacheColorHint(context.getResources().getColor(R.color.transparent));  
        try{
        inflater = LayoutInflater.from(context);  
  
        headView = (LinearLayout) inflater.inflate(R.layout.fresh_list_head, null);  
  
      //  arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
                
      //  arrowImageView.setMinimumWidth(70);  
      //  arrowImageView.setMinimumHeight(50);  
        progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
        tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
        lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);
                
  
        measureView(headView);  
        headContentHeight = headView.getMeasuredHeight();  
        headContentWidth = headView.getMeasuredWidth();  
  
        headView.setPadding(0, -1 * headContentHeight, 0, 0);  
        headView.invalidate();  
  
//        Log.v("size", "width:" + headContentWidth + " height:"  
//                + headContentHeight);  
  
        //addHeaderView(headView, null, false);  
        setOnScrollListener(this);  
  
        animation = new RotateAnimation(0, -180,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);  
        animation.setInterpolator(new LinearInterpolator());  
        animation.setDuration(250);  
        animation.setFillAfter(true);  
  
        reverseAnimation = new RotateAnimation(-180, 0,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);  
        reverseAnimation.setInterpolator(new LinearInterpolator());  
        reverseAnimation.setDuration(200);  
        reverseAnimation.setFillAfter(true);  
  
        state = DONE;  
        isRefreshable = false;  
        }catch (Exception e) {
            // TODO: handle exception
        }
    }  
  
    public void addHeaderView() {
    	addHeaderView(headView, null, false); 
    }
    
    public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,  
            int arg3) {  
        firstItemIndex = firstVisiableItem;  
    }  
  
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
    	
    }  
  
    public boolean onTouchEvent(MotionEvent event) {  
  
        if (isRefreshable) {  
            switch (event.getAction()) {  
            case MotionEvent.ACTION_DOWN:  
                if (firstItemIndex == 0 && !isRecored) {  
                    isRecored = true;  
                    startY = (int) event.getY();  
//                    Log.v(TAG, "��downʱ���¼��ǰλ�á�");  
                }  
                break;  
  
            case MotionEvent.ACTION_UP:  
  
                if (state != REFRESHING && state != LOADING) {  
                    if (state == DONE) {  
                        // ʲô������  
                    }  
                    if (state == PULL_To_REFRESH) {  
                        state = DONE;  
                        changeHeaderViewByState();  
  
//                        Log.v(TAG, "������ˢ��״̬����done״̬");  
                    }  
                    if (state == RELEASE_To_REFRESH) {  
                        state = REFRESHING;  
                        changeHeaderViewByState();  
                        onRefresh();  
  
//                        Log.v(TAG, "���ɿ�ˢ��״̬����done״̬");  
                    }  
                }  
  
                isRecored = false;  
                isBack = false;  
  
                break;  
  
            case MotionEvent.ACTION_MOVE:  
                int tempY = (int) event.getY();  
  
                if (!isRecored && firstItemIndex == 0) {  
//                    Log.v(TAG, "��moveʱ���¼��λ��");  
                    isRecored = true;  
                    startY = tempY;  
                }  
  
                if (state != REFRESHING && isRecored && state != LOADING) {  
  
                    // ��֤������padding�Ĺ���У���ǰ��λ��һֱ����head����������б?����Ļ�Ļ����������Ƶ�ʱ���б��ͬʱ���й���  
  
                    // ��������ȥˢ����  
                    if (state == RELEASE_To_REFRESH) {  
  
                        setSelection(0);  
  
                        // �������ˣ��Ƶ�����Ļ�㹻�ڸ�head�ĳ̶ȣ����ǻ�û���Ƶ�ȫ���ڸǵĵز�  
                        if (((tempY - startY) / RATIO < headContentHeight)  
                                && (tempY - startY) > 0) {  
                            state = PULL_To_REFRESH;  
                            changeHeaderViewByState();  
  
//                            Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽����ˢ��״̬");  
                        }  
                        // һ�����Ƶ�����  
                        else if (tempY - startY <= 0) {  
                            state = DONE;  
                            changeHeaderViewByState();  
  
//                            Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽done״̬");  
                        }  
                        // �������ˣ����߻�û�����Ƶ���Ļ�����ڸ�head�ĵز�  
                        else {  
                            // ���ý����ر�Ĳ�����ֻ�ø���paddingTop��ֵ������  
                        }  
                    }  
                    // ��û�е�����ʾ�ɿ�ˢ�µ�ʱ��,DONE������PULL_To_REFRESH״̬  
                    if (state == PULL_To_REFRESH) {  
  
                        setSelection(0);  
  
                        // ���������Խ���RELEASE_TO_REFRESH��״̬  
                        if ((tempY - startY) / RATIO >= headContentHeight) {  
                            state = RELEASE_To_REFRESH;  
                            isBack = true;  
                            changeHeaderViewByState();  
  
//                            Log.v(TAG, "��done��������ˢ��״̬ת�䵽�ɿ�ˢ��");  
                        }  
                        // ���Ƶ�����  
                        else if (tempY - startY <= 0) {  
                            state = DONE;  
                            changeHeaderViewByState();  
  
//                            Log.v(TAG, "��DOne��������ˢ��״̬ת�䵽done״̬");  
                        }  
                    }  
  
                    // done״̬��  
                    if (state == DONE) {  
                        if (tempY - startY > 0) {  
                            state = PULL_To_REFRESH;  
                            changeHeaderViewByState();  
                        }  
                    }  
  
                    // ����headView��size  
                    if (state == PULL_To_REFRESH) {  
                        headView.setPadding(0, -1 * headContentHeight  
                                + (tempY - startY) / RATIO, 0, 0);  
  
                    }  
  
                    // ����headView��paddingTop  
                    if (state == RELEASE_To_REFRESH) {  
                        headView.setPadding(0, (tempY - startY) / RATIO  
                                - headContentHeight, 0, 0);  
                    }  
  
                }  
  
                break;  
            }  
        }  
  
        return super.onTouchEvent(event);  
    }  
  
    // ��״̬�ı�ʱ�򣬵��ø÷������Ը��½���  
    private void changeHeaderViewByState() {  
        try{
        switch (state) {  
        case RELEASE_To_REFRESH:  
           // arrowImageView.setVisibility(View.VISIBLE);  
            progressBar.setVisibility(View.GONE);  
            tipsTextview.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
  
           // arrowImageView.clearAnimation();  
           // arrowImageView.startAnimation(animation);  
  
            tipsTextview.setText("�ɿ�����ˢ��");  
  
//            Log.v(TAG, "��ǰ״̬���ɿ�ˢ��");  
            break;  
        case PULL_To_REFRESH:  
            progressBar.setVisibility(View.GONE);  
            tipsTextview.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
            //arrowImageView.clearAnimation();  
           // arrowImageView.setVisibility(View.VISIBLE);  
            // ����RELEASE_To_REFRESH״̬ת������  
            if (isBack) {  
                isBack = false;  
              //  arrowImageView.clearAnimation();  
               // arrowImageView.startAnimation(reverseAnimation);  
  
                tipsTextview.setText("�����϶�����ˢ��");  
            } else {  
                tipsTextview.setText("�����϶�����ˢ��");  
            }  
//            Log.v(TAG, "��ǰ״̬������ˢ��");  
            break;  
  
        case REFRESHING:  
  
            headView.setPadding(0, 0, 0, 0);  
  
            progressBar.setVisibility(View.VISIBLE);  
            //arrowImageView.clearAnimation();  
           // arrowImageView.setVisibility(View.GONE);  
            tipsTextview.setText("����ˢ��...");  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
  
//            Log.v(TAG, "��ǰ״̬,����ˢ��...");  
            break;  
        case DONE:  
            headView.setPadding(0, -1 * headContentHeight, 0, 0);  
  
            progressBar.setVisibility(View.GONE);  
           // arrowImageView.clearAnimation();  
           // arrowImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);  
            tipsTextview.setText("�ɿ�����ˢ��");  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
  
//            Log.v(TAG, "��ǰ״̬��done");  
            break;  
        }  
        }catch (Exception e) {
            // TODO: handle exception
            return;
        }
    }  
  
    public void setOnRefreshListener(OnRefreshListener refreshListener) {  
        this.refreshListener = refreshListener;  
        isRefreshable = true;  
    }  
  
    public interface OnRefreshListener {  
        public void onRefresh();  
    }  
  
    public void onRefreshComplete() {  
        state = DONE;  
        SimpleDateFormat format=new SimpleDateFormat("yyyy��MM��dd��  HH:mm");  
        String date=format.format(new Date());  
        lastUpdatedTextView.setText("�ϴ�ˢ�� ��" + date);  
        changeHeaderViewByState();  
    }  
  
    private void onRefresh() {  
    	 if ( null != refreshListener) {    
            refreshListener.onRefresh();  
        }  
    }  
  
    // �˷���ֱ���հ��������ϵ�һ������ˢ�µ�demo���˴��ǡ����ơ�headView��width�Լ�height  
    private void measureView(View child) {  
         ViewGroup.LayoutParams p = child.getLayoutParams();  
            if (p == null) {  
                p = new ViewGroup.LayoutParams(  
                        ViewGroup.LayoutParams.FILL_PARENT,  
                        ViewGroup.LayoutParams.WRAP_CONTENT);  
            }  
  
            int childWidthSpec = ViewGroup.getChildMeasureSpec(0,  
                    0 + 0, p.width);  
            int lpHeight = p.height;  
            int childHeightSpec;  
            if (lpHeight > 0) {  
                childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);  
            } else {  
                childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);  
            }  
            child.measure(childWidthSpec, childHeightSpec);  
    }  

    public void setAdapter(BaseAdapter adapter) {  
        SimpleDateFormat format=new SimpleDateFormat("yyyy��MM��dd��  HH:mm");  
        String date=format.format(new Date());  
        lastUpdatedTextView.setText("�ϴ�ˢ�� ��" + date);  
        super.setAdapter(adapter);  
    }

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
		return false;
	}

	public boolean refreshByCode() {
		state = REFRESHING;
		changeHeaderViewByState();
		onRefresh();
		return true;
	}  
}
