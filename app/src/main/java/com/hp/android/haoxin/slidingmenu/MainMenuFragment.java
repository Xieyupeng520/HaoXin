package com.hp.android.haoxin.slidingmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.utils.Constant;
import com.hp.android.haoxin.utils.Tool;
import com.hp.android.haoxin.workview.ViewController;

public class MainMenuFragment extends Fragment implements OnClickListener{

	private static final int[] mIconIds = {R.drawable.selector_menu_icon_home,R.drawable.selector_menu_icon_clean,
		R.drawable.selector_menu_icon_site,R.drawable.selector_menu_icon_system,R.drawable.selector_menu_icon_about};
	
	private static final int ITEM_ID_BASE = 70;
	
	private View mView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.menu_view_bg, null);
		initView();
		
		return mView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}

	private void initView(){
		if(mView == null) return;
		
		LinearLayout container = (LinearLayout) mView.findViewById(R.id.menu_container_linear);
		String[] names = getResources().getStringArray(R.array.menu_names);
		for(int i = 0; i < mIconIds.length; i++){
			//--YY-HaoXinApplication
			//View item = LayoutInflater.from(HaoXinApplication.getAppContext()).inflate(R.layout.menu_view_item, null);
			View item = LayoutInflater.from(getActivity()).inflate(R.layout.menu_view_item, null);
			item.setId(ITEM_ID_BASE + i);
			((ImageView) item.findViewById(R.id.menu_icon_img)).setBackgroundResource(mIconIds[i]);
			TextView textView = Tool.setTextType(item, R.id.menu_title_tv, Constant.FONT_TYPE_FANGZ);
			textView.setText(names[i]);
			
			item.setOnClickListener(this);
			if(i == 0)item.setSelected(true);
			
			container.addView(item);
		}
	}

	public void setSelected(int index){
		LinearLayout container = (LinearLayout) mView.findViewById(R.id.menu_container_linear);
		int count = container.getChildCount();
		
		for(int i = 0; i < count; i++){
			View view = container.getChildAt(i);
			if(i == index){
				if(view.isSelected()){
					break;
				}else{
					view.setSelected(true);
				}
			}else if(view.isSelected()){
				view.setSelected(false);
			};
		}
	}
	
	private void updateUI() {
		
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		
		if(view.isSelected())return;
		view.setSelected(true);

		for(int i = 0; i < mIconIds.length; i++){
			int mid = ITEM_ID_BASE+i;
			View mview = mView.findViewById(mid);
			if(mid != id && mview.isSelected()){
				mview.setSelected(false);
			}
		}

		ViewController.getInstance().changeView(id - ITEM_ID_BASE);
	}
}
