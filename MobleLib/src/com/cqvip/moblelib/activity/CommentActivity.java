package com.cqvip.moblelib.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.view.CustomProgressDialog;

public class CommentActivity extends BaseActivity implements IBookManagerActivity {
	private TextView intro_tv;
	private EditText comment_et;
	private Button commit_btn;
	private Book dBook;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		intro_tv=(TextView) findViewById(R.id.intro_tv);
		commit_btn = (Button)findViewById(R.id.commit_btn);
		comment_et = (EditText)findViewById(R.id.comment_et);
		
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (Book)bundle.getSerializable("book");
		intro_tv.setText("°∂"+dBook.getU_title()+"°∑\n"
				+getString(R.string.item_author)+dBook.getAuthor()+"\n"
				+getString(R.string.item_publish)+dBook.getU_publish()+"\n"
				+getString(R.string.item_subject)+dBook.getSubject()+"\n"
//				+getString(R.string.item_callno)+dBook.getCallno()+"\n"
//				+getString(R.string.item_classno)+dBook.getClassno()+"\n"
				+"ISBN:"+dBook.getIsbn()+"\n"
				+getString(R.string.item_price)+dBook.getU_price()+"\n"				
				+dBook.getU_abstract());
		
		ManagerService.allActivity.add(this);
		customProgressDialog=CustomProgressDialog.createDialog(this);		
		
		commit_btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void refresh(Object... obj) {
		//œ‘ æ
		customProgressDialog.dismiss();
	}
}
