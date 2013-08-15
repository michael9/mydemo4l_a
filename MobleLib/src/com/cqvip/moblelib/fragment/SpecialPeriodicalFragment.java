package com.cqvip.moblelib.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.fragment.basefragment.BaseAbstractFragment;

public class SpecialPeriodicalFragment extends BaseAbstractFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_special_periodic,
				container, false);
		
		
		return rootView;
	}

	
}
