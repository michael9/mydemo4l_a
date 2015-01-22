package com.cqvip.moblelib.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cqvip.moblelib.sm.R;
import com.cqvip.utils.FileUtils;

public class TableTextActivity extends BaseActivity {
	private int type;
	private TextView t1;
	private LinearLayout piclayouy;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		context = this;
		View v = findViewById(R.id.seach_title);
		t1 = (TextView) v.findViewById(R.id.txt_header);
		ImageView back = (ImageView) v.findViewById(R.id.img_back_header);
		piclayouy = (LinearLayout) findViewById(R.id.ll_pic_layout);
	

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// title = (TextView)findViewById(R.id.title_txt);
		type = getIntent().getIntExtra("enter", 0);

		//customProgressDialog.show();
		switch (type) {
		case 2:
			t1.setText(R.string.guide_needknow2);
			String temp = FileUtils.getFromAssets(context,"lib_server2.txt");
			String[] temparr = temp.split("#");
			String temp2_s = temparr[0];
			String temp2_t = temparr[1];
			String bak = temparr[2];
			String[][] arr_t = getDimesionArray(temp2_t);
			String[][] arr_s = getDimesionArray(temp2_s);
			
			addWegit(arr_s);
			
			LinearLayout ll = (LinearLayout) findViewById(R.id.ll_student);
			TextView testview1 = (TextView) findViewById(R.id.tx_student); 
			TextView testview2 = (TextView) findViewById(R.id.tx_teacher); 
			TextView testview3 = (TextView) findViewById(R.id.tx_teacher_bak); 
			TextView testview4 = (TextView) findViewById(R.id.tx_teacher_content);
			testview1.setVisibility(View.VISIBLE);
			testview2.setVisibility(View.VISIBLE);
			testview3.setVisibility(View.VISIBLE);
			testview4.setVisibility(View.VISIBLE);
			testview4.setText(bak);
			addWegit2(arr_t);
			break;
		case 5:
			t1.setText(R.string.guide_needknow5);
			String temp5 = FileUtils.getFromAssets(context,"lib_server5.txt");
			String[][] arr = getDimesionArrayWithregit(temp5);
			addWegit(arr);
			break;
	
        default:
        	break;
		}

	}
	
	 private  String[][] getDimesionArrayWithregit(String str){
		 String[][] arrray;
		 str = str.replaceAll("#", "\n");
	      String[]   arr1 = str.split(";");
	        arrray = new String[arr1.length][];
	        for (int i = 0; i < arr1.length; i++) {
	        	arrray[i] = arr1[i].split(",");
			}
		 return arrray;
	 }
	 private  String[][] getDimesionArray(String str){
		 String[][] arrray;
	      String[]   arr1 = str.split(";");
	        arrray = new String[arr1.length][];
	        for (int i = 0; i < arr1.length; i++) {
	        	arrray[i] = arr1[i].split(",");
			}
		 return arrray;
	 }
	
	  private void addWegit(String[][] arrray) { 
	        TableLayout table = (TableLayout) findViewById(R.id.tablelayout); 
	        table.setBackgroundColor(getResources().getColor(android.R.color.white));
	        table.setStretchAllColumns(true); 
	        for (int i = 0; i < arrray.length; i++) { 
	            TableRow tablerow = new TableRow(context); 
	            tablerow.setBackgroundColor(getResources().getColor(R.color.tablerow_color1)); 
	            for (int j = 0; j < arrray[i].length; j++) { 
	                	TextView testview = new TextView(context); 
	                	testview.setTextSize(18);
	                     testview.setText(arrray[i][j]); 
	                     testview.setBackgroundResource(R.drawable.shape);
	                     testview.setGravity(Gravity.CENTER);
	                     tablerow.addView(testview, new TableRow.LayoutParams( 
	     	                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
	            } 
	            table.addView(tablerow, new TableLayout.LayoutParams( 
	                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
	        } 
	    } 
	  private void addWegit2(String[][] arrray) { 
	        TableLayout table = (TableLayout) findViewById(R.id.tablelayout2); 
	        table.setVisibility(View.VISIBLE);
	        table.setBackgroundColor(getResources().getColor(android.R.color.white));
	        table.setStretchAllColumns(true); 
	        for (int i = 0; i < arrray.length; i++) { 
	            TableRow tablerow = new TableRow(context); 
	            tablerow.setBackgroundColor(getResources().getColor(R.color.tablerow_color1)); 
	            for (int j = 0; j < arrray[i].length; j++) { 
	                	TextView testview = new TextView(context); 
	                	testview.setTextSize(18);
	                     testview.setText(arrray[i][j]); 
	                     testview.setBackgroundResource(R.drawable.shape);
	                     testview.setGravity(Gravity.CENTER);
	                     tablerow.addView(testview); 
	            } 
	            table.addView(tablerow, new TableLayout.LayoutParams( 
	                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)); 
	        } 
	    } 

}
