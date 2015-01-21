package com.cqvip.moblelib.activity;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cqvip.moblelib.sm.R;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.utils.BitmapCache;
import com.cqvip.utils.FileUtils;
import com.cqvip.utils.Tool;

public class DetailTextActivity extends BaseActivity {
	private int type;
	private TextView t1, content;
	private LinearLayout piclayouy;
	private Context context;
	private ImageLoader mImageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_text);
		context = this;
		View v = findViewById(R.id.seach_title);
		t1 = (TextView) v.findViewById(R.id.txt_header);
		ImageView back = (ImageView) v.findViewById(R.id.img_back_header);
		piclayouy = (LinearLayout) findViewById(R.id.ll_pic_layout);
		if(cache==null){
			cache = new BitmapCache(Tool.getCachSize());
		}
	    mImageLoader = new ImageLoader(mQueue, cache);
		
		// ManagerService.allActivity.add(this);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// title = (TextView)findViewById(R.id.title_txt);
		content = (TextView) findViewById(R.id.content_txt);
		type = getIntent().getIntExtra("enter", 0);

		//customProgressDialog.show();
		switch (type) {
		case 1:
			t1.setText(R.string.guide_needknow1);
//			requestVolley(GlobleData.SERVER_URL
//					+ "/library/guide/notice.aspx?libid=2", mj, null,
//					Method.GET);
//			String temp="<font color=" +
//					"\"#666666\">&nbsp;&nbsp;&nbsp;&nbsp;���ղ�óְҵѧԺͼ�������Ժ��ί�߶������£��ڷֹ�Ժ�����쵼�£�ͼ����ѽ��ɾ��й����ִ�����ͼ��ݣ�������͸��ŨŨ���Ļ���ζ���ö�������֪ʶ�ĵ������ſ����ɵĳ�򣬼�ȡ֪ʶ�����֣����ϳ�ʵ�Լ��������Լ������������ҹݲ��ϸ��¹����ǿ�쵼������ʵ����ʼ��׼ȷ����ݵİ����ִ�ͼ��ݽ��跢չ���򣬰��ִ�ͼ��ݹ����������õ�ʵ�ʹ�����ȥ��ʹ�ҹݸ����ȡ�ó���ķ�չ���ֽ�ѧԺͼ��ݵĻ�����������������£�<br>&nbsp;&nbsp;&nbsp;&nbsp;һ�����齨������Լ���������<br>&nbsp;&nbsp;&nbsp;&nbsp;ͼ����ڱ���Ա9�ˣ�����ͼ��ݸ��о���Աְ��1�ˣ��м�ְ��3�ˣ�ר������ѧ��6�ˡ��ҹݸ���&nbsp;�������ٽ��������ٸģ������ٹܣ�������ϣ����ڽ��衱�������������룬�Խ���Ϊ���ģ�ͨ��ӭ����������һ֧˼���Ӳ���Ž�Э�������ڷ��ף��ܷ����µ�ս�����顣<br>&nbsp;&nbsp;&nbsp;&nbsp;ͼ�������ΪѧԺ�в���������������ƣ�Ϊ���֡�����Ч�ܡ���ԭ�򣬹��ڻ��������Ż���ϣ����á��ɱಿ����������������&nbsp;����ͨ����������Ϣ���ġ��Ȼ�����<br>&nbsp;&nbsp;&nbsp;&nbsp;�����ƶ�������ͼ��ݸ�������ƶ�<br>&nbsp;&nbsp;&nbsp;&nbsp;�ִ���ͼ��ݱ���ʵ�п�ѧ��������Ҫ���¹��������ƹ����ʩ��������ȫ��������ƶȣ���ȷ��λְ�𣬹涨���˰취����֤��ʩ�Ĺִ᳹�С��ҹ��ƶ��������ˡ�ͼ��ݹ����ƶȻ�ࡷ���乤�����ݼ���������ҵ�����з���Ͷ��齨���˼�����ι�������������Ȱ��������ߣ��ְ������ݹ�����Ա��һ����ອ����ͼ��ݸ�����λ�Ĺ���ְ�𡢹�������������׼�ȸ����ƶȣ��Դ���Ϊͼ��ݹ淶���ĸ����Ҫ���ݡ����⣬�������ƶ���ǽ����Ƭ��ǽ�������������ǽ���Լ�������Ա�����ϸڣ���ʱ���ܶ��߼ල���յ������õ�Ч����<br>&nbsp;&nbsp;&nbsp;&nbsp;����ͳ�￼��������Դ���裬�γɸ����ɫ<br>&nbsp;&nbsp;&nbsp;&nbsp;������Ϣ��Դ������ͼ��ݵ�һ����Ҫ�����������ḻ�ݲ�������ϵ�ǹݲ���Դ��Ч���õĻ������������ҹ�����ѧԺ�����ѧĿ�꣬ʼ��ע��ϵͳ��ѡ���ռ����ӹ�����֯����������Դ�������˶������塢��ɫ�����Ĳ�����ϵ��<br>&nbsp;&nbsp;&nbsp;&nbsp;1�����ӻ���������ֽ�����׽��蹤��<br>&nbsp;&nbsp;&nbsp;&nbsp;��Ժͼ���1999��ʵ�ּ�����ִ�������������ͬ��ѧУ��ǰ�档�ڽ���ĳ��ڣ����Ƕ�ͼ����������ӹ����ڲ�Ӱ������������ǰ���£��ҹݶ�ͼ������˻��ݽ��⹤������ĿǰΪֹ���ҹݲ���433,471�᣻�϶��ڿ�20,379�᣻�϶���ֽ5,450�᣻����ͼ��50,000�ᣬ�����ڿ�12��ᣨ9100���֣���˶������11,200�᣻�ҹݲ�������Դ�ﵽ64.5��ᡣ������٣�ͨ����Ͷ�귽ʽ�ֹ���������֮�ҵ�17���ֵ���ͼ�飬��˶��������15��ƪ����������Ժ������ѧ�����е���Ҫ��<br>&nbsp;&nbsp;&nbsp;&nbsp;ͼ�����Ҫ�Ĺ�������δ���Ժ�Ľ�������ѧ�������ص��������������ΪѧԺ�Ľ�ѧ�����з������Ϣ��Դ������ϵ����Ҳ���ҹݷ�չ�ķ���Ϊ˳Ӧרҵѧ���ϵ�ת�䣬���Ǽ�ʱ����������Դ����ṹ���𽥼Ӵ�ƾ�����Ϣ��Դ���裬��������Ӣ�������ȷ����������Դ���裬��ǿ����רҵ���������Դ���裬�Ӵ����רҵͼ������������γ��Բƾ�Ϊ��Ҫ��ɫ���ۺ����ײ�����ϵ��<br>&nbsp;&nbsp;&nbsp;&nbsp;2����Ӧ��Ϣ��ᷢչ��ǿ��������Դ���蹤��<br>�ھ�����ֽ֯�������ղص�ͬʱ���ҹݽ���Ӵ������ֻ���Դ�Ľ��蹤����ͨ���б���ʽ�����ˡ�ά�������ڿ����ݿ⡱9100�֡�12��ᣬ���򷽲�˶�������ݿ⡱15��ƪ��������ͼ��������ڿ������ա��й�����Ŀ¼��ʽ����CNMARC������¼����¼��Ϣ��ϸ���ܹ���ʾͼ����Ϣ������ʵ���˹����ִ������淶������ѧ����ʵ���˲ɷá���Ŀ����ء���ͨ��ҵ���ȫ���Զ�����<br>&nbsp;&nbsp;&nbsp;&nbsp;���ֻ��ݲ���Ϣ��Դ�Ľ����������Ϣ����Դ�Ŀ������ã�Ϊ��Ժ��ѧ�����й����ṩ��������������Ϣ���ϣ��γ�����ʵ��������Դ����Ϊ������������Դ��������Դ������ݲ�Э����չ���������干�桢��ɫ�������ṹ��������ѧԺ��չ��Ҫ����ɫ�ݲ���ϵ<br>&nbsp;&nbsp;&nbsp;&nbsp;3�������ִ�������ϵͳ<br>&nbsp;&nbsp;&nbsp;&nbsp;�����ִ���ͼ�����Ϣ����ϵͳ�������ͼ������ױ�����������ַ��ӹݲ����׵ı�Ҫ�ֶΣ���ʵ��������Դ�����ǰ�᣻Ҳ��Ӧ����Ϣ����������ǿ������ʩ���ҹ�������У԰����Ϊһ�塣<br>&nbsp;&nbsp;&nbsp;&nbsp;����ѧԺͶ��10����Ԫ�������Ƚ��Ĵ洢ϵͳ��Ϊ���ֻ���Դ�ṩ���ϣ���ϵͳΪͼ��ݹݲض�ý����Դ�ṩǿ���Ӳ��֧�֣�Ϊ���ݿ⾵����������ݿ�Ľ����ṩ�Ⱦ�������<br>&nbsp;&nbsp;&nbsp;&nbsp;4����չ��ý�������ң��ṩȫ��λ����<br>ͼ��ݵ�������������110̨��ý��������Ϊ�����ṩ�����������ȹ��ܣ������ṩ��30���ڵ�����������ҡ�<br>&nbsp;&nbsp;&nbsp;&nbsp;�ġ�ͼ�����������������ҡ���λ���<br>ѧԺ��ί�ǳ�����ͼ��ݽ��裬�Ѿ����¹ݽ�������2008�ļƻ����У�������Ƶ�ǰ�ڹ�����Ŀǰͼ����ڹ������ڣ������4350ƽ���ף������ڿ������ҡ���ֽ����������������չʾ�ҡ���һ������⡢������ο����������ҡ�˼��ͼ�������ҡ��϶��ڿ������ҡ��϶���ֽ�����ҡ������������ȣ�������λ�ﵽ710���������ܹ�����ѧ��ѧϰ��Ҫ��<br>&nbsp;&nbsp;&nbsp;&nbsp;�塢ͼ���ΪѧԺ��ѧ�������<br>&nbsp;&nbsp;&nbsp;&nbsp;��֪ʶ����ʱ�������绷���£�ͼ��ݲ��ϵ��Ƴ³��£���ʱ���·����ֶΣ��ı����ʽ��ǧ���ټ���߷���������Ϊ�����ṩ���㡢��ݺ�ʵ�õķ��񣬲��𲽽��������Ĵ�һ����������η���չ��<br>&nbsp;&nbsp;&nbsp;&nbsp;1�������������ǿ����������<br>&nbsp;&nbsp;&nbsp;&nbsp;��Ա��ݵ�ʵ�������Ϊ����޶�������ߵ��Ķ��������ǳ���������еĹݲأ��෽λ�Ŀ�չ�������������ܵ��������Χ��������ȫ���ּ�����ϵ���ṩ���������ļ����ֶΣ�ʹ���ڷḻ��������Ϣ��Դ�õ���ֵĿ��������á����⣬�ӳ�����ʱ�䣨ÿ�ܿ���ʱ����ѳ���72Сʱ����Ŀǰ�վ���ͨ���ﵽ1300���˴Σ���վ24Сʱ����߿��š�<br>&nbsp;&nbsp;&nbsp;&nbsp;2�����ݽ�ѧ��������Ҫ����չ���⡢ר�����<br>&nbsp;&nbsp;&nbsp;&nbsp;ͼ��ݵĸ��Ի���Ϣ������ǽ����ɻ�����Ϣ��Դ����һ����Ŀ�ĺ��ض���ʦ����,�Լ���Ϣ��Դ�ĸ��Ի��������д����γ���ɫ������Ϣ��Ʒ����ͨ�����������Ͷ���������Ϣ����ʽ����ɢ���ݸ��й���ʦ�Ĺ��̣�Ҳ���Ǵ����Ͳ����Ż���Ӧ��ɫ��Դ����Ҫ��Ĳ���Ϊ�ض���ʦ�ṩ���ʸ�Ч����ĸ��Ի���Ϣ����ƽ̨�Ĺ��̡���Ժ�ĸ��Ի���Ϣ������Բ�ͬ����ʦ���ò�ͬ�ķ�����Ժͷ������еķ�ʸ��Ϊ������ʦ�����������Ի��������Ϣ������Ϊ���ṩԤ����Ϣ�����ȣ���������ʦ����������Ϣϵͳ������������ʦΪ���ģ�����ʦ��������ھ��������������ؽ���Ϣ���͸���ʦ���ص㡣��ǿ�������뱻����֮����һһ��Ӧ�Ĺ�ϵ���ܼ����������ʦ���Ի�����Ϣ������ˣ���֮��ͳͼ��ݵ���Ϣ������ֳ������֮����<br>���ǹ��ڿ�չ����������ʱ��������<br>&nbsp;&nbsp;&nbsp;&nbsp;��1������ʦΪ���ģ���������ʦ���ض���������������ʦ�ṩ��Ϣ�������еķ�����Է�����ʦ��������ʦΪǰ�ᡣ<br>&nbsp;&nbsp;&nbsp;&nbsp;��2�������ԡ���̬�ԣ����Ի���Ϣ����Ĺ�����һ�������Ĺ��̣�����ʵ����Ҫ��ͨ��ֱ������ʦ�Ľ����������ʦ����Ϊ����ȡ��ʦ��������<br>&nbsp;&nbsp;&nbsp;&nbsp;��3������ԡ������ԣ����Ի���Ϣ����ʽ������ʦ�����ʱ��͵ص��ṩ���񣬴Ӷ���������Ϣ�Ŀ���Ȩ������ʦ���С�<br>&nbsp;&nbsp;&nbsp;&nbsp;��4�������ԣ����Ի���Ϣ�����չ������Ҫ�ۺϵ����ø�����Ϣ��������Ϣ�������������������ʼ�����ǲ��ɻ�ȱ�ġ�<br>&nbsp;&nbsp;&nbsp;&nbsp;��5�������רҵ������ʦ��Ϣ������Ի�����Ϣ����Ҳ���ֳ������רҵ�������ơ�ͼ���ͨ������Ϣ��ɸѡ�����������飬�γ��µ���ֵ��Ϣ�ṩ����ʦ���Ӷ������Ϣ�����������<br>&nbsp;&nbsp;&nbsp;&nbsp;3����չ������Ŀ���ḻ�������ݣ�չ���������ʷ���<br>&nbsp;&nbsp;&nbsp;&nbsp;Ϊ�˸��õ�ΪѧԺ�Ľ�ѧ�����ҹ������׵Ĳɹ��Ϲ㷺����ѧԺ��ϵ�����ʦ���Բɹ����������ͽ��飬���߳�ȥ������������뵽ʦ����ȥ���˽⵱ǰ��ѧ�ĸ�Ľ�չ����������ռ����ĸ��ֳ�����Ϣ���ڷ��ŵ����ϵ���������ǲο���ͬʱ��������������֯��ؽ�ʦ�����������ֳ����飬�Լ����ڷ��Ŷ�����Ϣ�����������޶ȵ������רҵ�Ľ�ѧ����<br>&nbsp;&nbsp;&nbsp;&nbsp;�ҹݻ����ñ�����ɫ��Դ���ƣ�������չ�������׷�����������վ������ר�ŵ������Ƽ���Ŀ���ṩרҵ��Ϣ��ժ�ࡢ��������Ҫ�ȷ���ͬʱ�ҹ��ԡ�����������Դ���������á�Ϊ�⣬Ϊ��Ժѧ������ר�⽲�����ܵ��˹��ѧ���ĺ�����<br>&nbsp;&nbsp;&nbsp;&nbsp;4�����Ʒ���������ȫ��λ��ΪѧԺ��ѧ�����з���<br>Ϊ�˼����ҹݵ����ֻ����貽�������Ʒ�����������Χ����������ȫ��λ��ΪѧԺ��ѧ�����з���ѧԺ������ͼ��ݹ���ίԱ�ᣬ�������ٿ����飬����ѧԺ���׹����ش����⣬��ӳʦ���������Ҫ����ѧԺ��ͼ�������Ľ�ͼ��ݹ����Ľ��顣ʹͼ��ݵĸ������ѧԺȫ�ֽ�����ϵ��ͬ����չ��<br>&nbsp;&nbsp;&nbsp;&nbsp;����������ͼ��ݰ���һ����Ҫ���Ŀ�꣬����˰���������Դ�ݲؽ��衢��Ϣ����ϵͳ���衢ͼ����˲�������רҵ���齨������ݵİ��ղ�óְҵѧԺͼ�������滮�������Ч�������Ƚ��ļ��������������������Ϣ��Դ�����ط�����Ŀ�����Ʒ��񻷾����ḻ�������ݡ��������Χ��������Ρ���߷���Ч�ʣ�ͬʱ��ǿ������Ϣ��Դ���裬ͨ�����߷����ԭ�����״������ϵ���ʽ��������������ʣ���ͼ��ݽ�����Ϣ�洢���ġ���Ϣ�������ĺ���Ϣ�������ģ�����޶ȵ�������߶�������Ϣ������<br>&nbsp;&nbsp;&nbsp;&nbsp;5�����ơ�������ѧ��ժ����Ϊ��ѧ�����д�����ʱ��Ϣ<br>&nbsp;&nbsp;&nbsp;&nbsp;��֪ʶ������������֪ʶ�ڼ���������֪ʶ���µ�����Խ��Խ�̣���ˣ������޵�ʱ���ȡ�����ܶ��Ҿ���ʵ�ʼ�ֵ����Ϣ�ͳ�Ϊ���ǵ�����������Ժͼ��ݱ༭�ġ�������ѧ��ժ����˵ÿ��һ�ڡ�ֻ�а�ҳֽ����������Ϣ����֪ʶ��ǿ�����ݷḻ�Ͷ�С�������ص㣬һ�����־͹���ʦ��Ա����������<br>&nbsp;&nbsp;&nbsp;&nbsp;������ǽ����������ʱ��������ش��µĹ�����������Ƚ��ļ����ֶΣ����ϼ�ǿ���齨�裬�ں����裬�������뿪չ���ζ��߷�������Ŭ��������ɫ�������ִ���ͼ��ݣ��Դﵽ�ݲ���̿������Ӧ�����������ù������¾��档&nbsp;<br></font>"
//					+"<br><p align=\"right\"><font color=\"#02378F\">ͼ���  </font><font color=\"#666666\">2013-4-22 15:57:25</font></p>";
//				content.setText(Html.fromHtml(temp));
			String temp1 = FileUtils.getFromAssets(context,"lib_server1.txt");
			content.setText(Html.fromHtml(temp1));
			break;
		case 3:
			t1.setText(R.string.guide_needknow3);
//			requestVolley(GlobleData.SERVER_URL
//					+ "/library/guide/time.aspx?libid=2", mj, null, Method.GET);
			String temp3= FileUtils.getFromAssets(context,"lib_server3.txt");
			content.setText(Html.fromHtml(temp3));
			break;
		case 4:
			t1.setText(R.string.guide_needknow4);
			String temp4= FileUtils.getFromAssets(context,"lib_server4.txt");
			content.setText(Html.fromHtml(temp4));
			break;
		case 6:
			t1.setText(R.string.guide_needknow6);
			String temp6= FileUtils.getFromAssets(context,"lib_server6.txt");
			content.setText(Html.fromHtml(temp6));
			break;
        default:
        	break;
		}

	}

	Listener<JSONObject> mj = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject arg0) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			try {
				if (arg0.getString("success").equalsIgnoreCase("true")) {
					content.setText(Html.fromHtml(arg0.getString("contents")));
					if(!arg0.isNull("contentImgUrl")){
					String [] imgs = getImgs(arg0.getString("contentImgUrl"));
					if(imgs!=null&&imgs.length>0){
						for(int i=0;i<imgs.length;i++){
							ImageView img = new ImageView(context);
							ImageListener listener = ImageLoader.getImageListener(img,
									R.drawable.defaut_book, R.drawable.defaut_book);
					      	ImageContainer imageContainer=mImageLoader.get(imgs[i], listener);
					      	Bitmap bitmap = imageContainer.getBitmap();
					      	if(bitmap!=null){
							img.setImageBitmap(imageContainer.getBitmap());
							piclayouy.addView(img);
					      	}
						}
					  }
					}
				}
			} catch (Exception e) {
			    e.printStackTrace();
				content.setText(DetailTextActivity.this.getResources()
						.getString(R.string.loadfail));
			}
		}

		private String[] getImgs(String string) {
			String[] array = null ;
			if(!TextUtils.isEmpty(string)){
				array = string.split(";");
				return array;
			}
			return array;
		}
	};


	private void requestVolley(String addr, Listener<JSONObject> mj,
			JSONObject js, int method) {

		try {
			JsonObjectRequest myjson = new JsonObjectRequest(method, addr, js,
					mj, el);
			mQueue.add(myjson);
			mQueue.start();
		} catch (Exception e) {
			onError(2);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_text, menu);
		return true;
	}
}
