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
//					"\"#666666\">&nbsp;&nbsp;&nbsp;&nbsp;安徽财贸职业学院图书馆是在院党委高度重视下，在分管院长的领导下，图书馆已建成具有管理现代化的图书馆，到处渗透着浓浓的文化韵味，让读者在这知识的殿堂里张开自由的翅膀，汲取知识的养分，不断充实自己，提升自己。几年来，我馆不断更新观念，加强领导，勤于实践，始终准确、快捷的把握现代图书馆建设发展方向，把现代图书馆管理理念运用到实际工作中去，使我馆各项工作取得长足的发展。现将学院图书馆的基本情况等做简述如下：<br>&nbsp;&nbsp;&nbsp;&nbsp;一、队伍建设情况以及机构设置<br>&nbsp;&nbsp;&nbsp;&nbsp;图书馆在编人员9人，其中图书馆副研究馆员职称1人，中级职称3人，专科以上学历6人。我馆根据&nbsp;“以评促建，以评促改，以评促管，评建结合，重在建设”的评估工作方针，以建设为中心，通过迎评，锻炼了一支思想过硬，团结协作，甘于奉献，奋发创新的战斗队伍。<br>&nbsp;&nbsp;&nbsp;&nbsp;图书馆设置为学院中层机构，副处级建制，为体现“精简效能”的原则，馆内机构进行优化组合，设置“采编部”、“阅览部”、&nbsp;“流通部”、“信息中心”等机构。<br>&nbsp;&nbsp;&nbsp;&nbsp;二、制定和完善图书馆各项规章制度<br>&nbsp;&nbsp;&nbsp;&nbsp;现代化图书馆必须实行科学管理，首先要更新管理观念，完善管理措施，建立健全各项规章制度，明确岗位职责，规定考核办法，保证措施的贯彻执行。我馆制订和完善了《图书馆规章制度汇编》。其工作内容既有行政和业务，又有服务和队伍建设等思想政治工作；工作对象既包括广大读者，又包括本馆工作人员。一本汇编涵盖了图书馆各个岗位的工作职责、工作质量评定标准等各种制度，以此作为图书馆规范化改革的重要内容。此外，还做到制度上墙，照片上墙，读者意见箱上墙，以及工作人员挂牌上岗，随时接受读者监督。收到了良好的效果。<br>&nbsp;&nbsp;&nbsp;&nbsp;三、统筹考虑文献资源建设，形成格局特色<br>&nbsp;&nbsp;&nbsp;&nbsp;文献信息资源建设是图书馆的一项重要基础工作，丰富馆藏文献体系是馆藏资源有效利用的基础，近年来我馆依据学院总体办学目标，始终注重系统的选择、收集、加工、组织管理文献资源，建立了多种载体、特色鲜明的藏书体系。<br>&nbsp;&nbsp;&nbsp;&nbsp;1、重视基础，保障纸质文献建设工作<br>&nbsp;&nbsp;&nbsp;&nbsp;我院图书馆1999年实现计算机现代化管理，走在了同类学校的前面。在建库的初期，我们对图书进行整理、加工，在不影响正常工作的前提下，我馆对图书进行了回溯建库工作。到目前为止，我馆藏书433,471册；合订期刊20,379册；合订报纸5,450册；电子图书50,000册，电子期刊12万册（9100多种）；硕博论文11,200册；我馆藏书总资源达到64.5万册。今年暑假，通过招投标方式又购买了书生之家的17万种电子图书，万方硕、博论文15万篇。满足了我院教育教学、科研的需要。<br>&nbsp;&nbsp;&nbsp;&nbsp;图书馆重要的工作是如何从我院的教育、教学、科研特点出发，建立起能为学院的教学、科研服务的信息资源保障体系，这也是我馆发展的方向。为顺应专业学科上的转变，我们及时调整文献资源建设结构，逐渐加大财经类信息资源建设，不断扩大英语、计算机等方面的文献资源建设，加强新增专业相关文献资源建设，加大相关专业图书比例，初步形成以财经为主要特色的综合文献藏书体系。<br>&nbsp;&nbsp;&nbsp;&nbsp;2、适应信息社会发展，强化数字资源建设工作<br>在精心组织纸质文献收藏的同时，我馆今年加大了数字化资源的建设工作，通过招标形式购置了“维普中文期刊数据库”9100种、12万册，“万方博硕论文数据库”15万篇。对中文图书和中文期刊均按照《中国机读目录格式》、CNMARC进行著录，著录信息详细，能够揭示图书信息，真正实现了管理现代化、规范化、科学化。实现了采访、编目、典藏、流通等业务的全面自动化。<br>&nbsp;&nbsp;&nbsp;&nbsp;数字化馆藏信息资源的建设和网络信息化资源的开放利用，为我院教学、科研工作提供了有力的文献信息保障，形成了以实体文献资源建设为基础，电子资源、网络资源等虚拟馆藏协调发展，各种载体共存、特色鲜明、结构合理、符合学院发展需要的特色馆藏体系<br>&nbsp;&nbsp;&nbsp;&nbsp;3、构建现代化网络系统<br>&nbsp;&nbsp;&nbsp;&nbsp;构建现代化图书馆信息网络系统，是提高图书馆文献保障能力，充分发挥馆藏文献的必要手段，是实现文献资源共享的前提；也是应对信息技术革命的强有力措施。我馆网络与校园网连为一体。<br>&nbsp;&nbsp;&nbsp;&nbsp;今年学院投入10多万元购买了先进的存储系统，为数字化资源提供保障，该系统为图书馆馆藏多媒体资源提供强大的硬件支持，为数据库镜像和自主数据库的建设提供先决条件。<br>&nbsp;&nbsp;&nbsp;&nbsp;4、发展多媒体阅览室，提供全方位服务。<br>图书馆电子阅览室设有110台多媒体计算机，为读者提供了在线阅览等功能；另外提供有30个节点的网络自修室。<br>&nbsp;&nbsp;&nbsp;&nbsp;四、图书馆面积及各类阅览室、座位情况<br>学院党委非常重视图书馆建设，已经把新馆建设纳入2008的计划当中，着手设计等前期工作。目前图书馆在过渡期内，面积是4350平方米，设有期刊阅览室、报纸阅览室两个、新书展示室、第一至五书库、工具书参考资料阅览室、思政图书阅览室、合订期刊阅览室、合订报纸阅览室、自修室两个等，阅览座位达到710个，基本能够满足学生学习需要。<br>&nbsp;&nbsp;&nbsp;&nbsp;五、图书馆为学院教学服务简述<br>&nbsp;&nbsp;&nbsp;&nbsp;在知识经济时代和网络环境下，图书馆不断地推陈出新，适时更新服务手段，改变服务方式，千方百计提高服务质量，为读者提供方便、快捷和实用的服务，并逐步将服务重心从一般服务向深层次服务发展。<br>&nbsp;&nbsp;&nbsp;&nbsp;1、立足基础服务，强化服务理念<br>&nbsp;&nbsp;&nbsp;&nbsp;针对本馆的实际情况，为最大限度满足读者的阅读需求，我们充分利用现有的馆藏，多方位的开展服务工作，尽可能的扩大服务范围、建立健全各种检索体系，提供更多更方便的检索手段，使馆内丰富的文献信息资源得到充分的开发和利用。此外，延长开放时间（每周开放时间均已超过72小时）、目前日均流通量达到1300多人次，网站24小时向读者开放。<br>&nbsp;&nbsp;&nbsp;&nbsp;2、根据教学、科研需要，开展定题、专题服务<br>&nbsp;&nbsp;&nbsp;&nbsp;图书馆的个性化信息服务就是将集成化的信息资源依据一定的目的和特定老师需求,以及信息资源的个性化特征进行处理，形成特色化的信息产品，并通过多种渠道和多样化的信息服务方式，分散传递给有关老师的过程，也就是创建和不断优化适应特色资源整合要求的并能为特定老师提供优质高效服务的个性化信息服务平台的过程。我院的个性化信息服务针对不同的老师采用不同的服务策略和方法，有的放矢地为具体老师创造符合其个性化需求的信息环境，为其提供预定信息与服务等，并帮助老师建立个人信息系统。它具有以老师为中心，对老师需求进行挖掘，灵活多样且主动地将信息推送给老师的特点。其强调服务与被服务之间是一一对应的关系，能极大地满足老师个性化的信息需求，因此，较之传统图书馆的信息服务呈现出其独特之处。<br>我们馆在开展个牲化服务时，做到：<br>&nbsp;&nbsp;&nbsp;&nbsp;（1）以老师为中心：即根据老师的特定需求，主动地向老师提供信息服务，所有的服务均以方便老师、满足老师为前提。<br>&nbsp;&nbsp;&nbsp;&nbsp;（2）交互性、动态性：个性化信息服务的过程是一个互动的过程，它的实现主要是通过直接与老师的交互或跟踪老师的行为来获取老师的特征。<br>&nbsp;&nbsp;&nbsp;&nbsp;（3）灵活性、多样性：个性化信息服务方式能在老师方便的时间和地点提供服务，从而将接受信息的控制权交到老师手中。<br>&nbsp;&nbsp;&nbsp;&nbsp;（4）智能性：个性化信息服务的展开，需要综合地运用各种信息技术，信息技术在整个服务过程自始至终是不可或缺的。<br>&nbsp;&nbsp;&nbsp;&nbsp;（5）纵深化、专业化：老师信息需求个性化的信息服务也呈现出纵深化、专业化的趋势。图书馆通过对信息的筛选、分析、重组，形成新的增值信息提供给老师，从而提高信息服务的质量。<br>&nbsp;&nbsp;&nbsp;&nbsp;3、开展服务项目，丰富服务内容，展开深层次优质服务<br>&nbsp;&nbsp;&nbsp;&nbsp;为了更好地为学院的教学服务，我馆在文献的采购上广泛征求学院各系部广大师生对采购方面的意见和建议，并走出去，请进来，深入到师生中去，了解当前教学改革的进展情况，并将收集来的各种出版信息定期发放到相关系、部供他们参考，同时还积极主动地组织相关教师到各书店进行现场采书，以及定期发放读者信息调查表，以最大限度地满足各专业的教学需求。<br>&nbsp;&nbsp;&nbsp;&nbsp;我馆还利用本馆特色资源优势，积极开展二次文献服务工作，在网站上设有专门的新书推荐栏目，提供专业信息的摘编、索引、提要等服务。同时我馆以“电子文献资源检索和利用”为题，为我院学生进行专题讲座，受到了广大学生的好评。<br>&nbsp;&nbsp;&nbsp;&nbsp;4、改善服务条件，全方位的为学院教学、科研服务<br>为了加速我馆的数字化建设步伐，改善服务条件，大范围、高质量、全方位的为学院教学、科研服务，学院成立了图书馆工作委员会，并定期召开会议，讨论学院文献工作重大问题，反映师生的意见和要求，向学院和图书馆提出改进图书馆工作的建议。使图书馆的各项工作与学院全局紧密联系，同步发展。<br>&nbsp;&nbsp;&nbsp;&nbsp;近两年来，图书馆按照一流的要求和目标，提出了包括电子资源馆藏建设、信息服务系统建设、图书馆人才培养及专业队伍建设等内容的安徽财贸职业学院图书馆总体规划。充分有效地利用先进的技术，合理的配置文献信息资源；开拓服务项目、改善服务环境、丰富服务内容、扩大服务范围、深化服务层次、提高服务效率；同时加强电子信息资源建设，通过在线服务和原文文献传递相结合的形式来提高文献利用率；把图书馆建成信息存储中心、信息交换中心和信息服务中心，最大限度的满足读者对文献信息的需求。<br>&nbsp;&nbsp;&nbsp;&nbsp;5、编制《教育教学文摘》，为教学、科研大量及时信息<br>&nbsp;&nbsp;&nbsp;&nbsp;在知识经济社会里，各种知识在急剧增长、知识更新的周期越来越短，因此，以有限的时间获取尽可能多且具有实际价值的信息就成为人们的迫切需求。我院图书馆编辑的《教育教学文摘》虽说每月一期、只有八页纸，但具有信息量大、知识性强、内容丰富和短小精悍等特点，一经出现就广受师生员工的青睐。<br>&nbsp;&nbsp;&nbsp;&nbsp;今后，我们将继续坚持与时俱进、开拓创新的观念，积极引进先进的技术手段，不断加强队伍建设，内涵建设，不断深入开展深层次读者服务工作，努力创建特色鲜明的现代化图书馆，以达到馆藏与教科研相呼应，积累与利用共进的新局面。&nbsp;<br></font>"
//					+"<br><p align=\"right\"><font color=\"#02378F\">图书馆  </font><font color=\"#666666\">2013-4-22 15:57:25</font></p>";
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
