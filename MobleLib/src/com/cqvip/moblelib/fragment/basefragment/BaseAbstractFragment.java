package com.cqvip.moblelib.fragment.basefragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cqvip.moblelib.view.CustomProgressDialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public class BaseAbstractFragment extends Fragment{

	protected RequestQueue mQueue;
	protected CustomProgressDialog customProgressDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 mQueue = Volley.newRequestQueue(getActivity());
		 customProgressDialog = CustomProgressDialog.createDialog(getActivity());
		 customProgressDialog.show();
	}
	
}
