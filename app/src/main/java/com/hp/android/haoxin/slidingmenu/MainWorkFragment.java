package com.hp.android.haoxin.slidingmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.hp.android.haoxin.MainActivity;
import com.hp.android.haoxin.R;

public class MainWorkFragment extends Fragment{
	
	private Button mShowMenuBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View mView = inflater.inflate(R.layout.work_view, null);
		//FrameLayout coner = (FrameLayout) mView.findViewById(R.id.fm_work_container);
		
		mShowMenuBtn = (Button) mView.findViewById(R.id.main_menu_btn);
		mShowMenuBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				((MainActivity)getActivity()).showMenu();
			}
		});
		
		return mView;
	}
	
	/*public FrameLayout getContainer(){
		return (FrameLayout) findViewById(R.id.fm_work_container);
	}*/
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((MainActivity)getActivity()).getSlidingMenu().setSignView(mShowMenuBtn);
	}
	
	public View getShowButton(){
		return mShowMenuBtn;
	}
	public void setSlidingMenuScroll(boolean isb){
		mShowMenuBtn.setVisibility(isb ? View.VISIBLE : View.INVISIBLE);
		((MainActivity)getActivity()).setSlidMenuScroll(isb);
	}
	
	public void setOnlySlidingMenuScroll(boolean isb){
		((MainActivity)getActivity()).setSlidMenuScroll(isb);
	}
	
	public void setSlidingMenuShow(){
		((MainActivity)getActivity()).getSlidingMenu().showLeftView();
	}
	
	public boolean isSlidMenuCanScroll(){
		return ((MainActivity)getActivity()).getSlidingMenu().isLeftViewCanScroll();
	}
	
	public boolean isSlidMenuViewShow(){
		return ((MainActivity)getActivity()).getSlidingMenu().isLeftViewShow();
	}
	
	public boolean isArrowButtonShow(){
		return mShowMenuBtn.getVisibility() == View.VISIBLE;
	}
	
	/*public boolean isSlidMenuShow(){
		return (MainActivity)getActivity().isSlidMenuShow();
	}*/
	
	public SlidingMenu getSlidingMenu(){
		return ((MainActivity)getActivity()).getSlidingMenu();
	}
}
