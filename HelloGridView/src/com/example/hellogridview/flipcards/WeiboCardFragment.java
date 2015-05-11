package com.example.hellogridview.flipcards;

import java.util.ArrayList;
import java.util.List;

import com.example.hellogridview.R;
import com.example.hellogridview.TextUtils;
import com.spark.hudsharedlib.MessageWeiboItem;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint({ "ViewHolder", "ValidFragment" })
public class WeiboCardFragment extends Fragment {
	MessageWeiboItem mItem;
	// Bitmap imageSrc;
	Bitmap iconSrc;// = new Bitmap();
	List<Bitmap> imageSrc = new ArrayList<Bitmap>();
	List<Bitmap> retimageSrc = new ArrayList<Bitmap>();

	public WeiboCardFragment(MessageWeiboItem msgWeibo) {
		mItem = msgWeibo;

		byte[] iconb = msgWeibo.getIcon();
		if (iconb != null && iconb.length != 0) {
			iconSrc = BitmapFactory.decodeByteArray(iconb, 0, iconb.length);
		}

		for (int i = 0; i < msgWeibo.getPics().size(); i++) {

			byte[] imgb = msgWeibo.getPics().get(i);
			if (imgb.length != 0) {
				imageSrc.add(BitmapFactory
						.decodeByteArray(imgb, 0, imgb.length));
			}
		}

		if (msgWeibo.getRetStat() == true) {
			for (int i = 0; i < msgWeibo.getRetPics().size(); i++) {

				byte[] retimgb = msgWeibo.getRetPics().get(i);
				if (retimgb.length != 0) {
					retimageSrc.add(BitmapFactory.decodeByteArray(retimgb, 0,
							retimgb.length));
				}
			}
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*
		 * initialize and insert one sample element into mCities.
		 */

		View convertView = inflater.inflate(R.layout.weibo, null);

		WeiBoHolder holder = new WeiBoHolder();

		holder.wbicon = (ImageView) convertView.findViewById(R.id.wbicon);
		holder.wbtime = (TextView) convertView.findViewById(R.id.wbtime);
		holder.wbuser = (TextView) convertView.findViewById(R.id.wbuser);

		convertView.setTag(0);
		holder.wbicon.setImageBitmap(iconSrc);
		holder.wbtime.setText((String) mItem.getTime());
		holder.wbuser.setText((String) mItem.getUser());

		// textHighlight(holder.wbtext,new char[]{'#'},new char[]{'#'});
		// textHighlight(holder.wbtext,new char[]{'@'},new char[]{':',' '});
		// textHighlight2(holder.wbtext,"http://"," ");

		LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);

		holder.wbtext = (TextView) convertView.findViewById(R.id.wbtext);
		holder.wbtext.setText((String) mItem.getText());
		float txtsize = 23;

		if (mItem.getText().length() < 30) {
			txtsize = 23;
		} else if (mItem.getText().length() < 200) {
			txtsize = 22;
		} else {
			txtsize = 21;
		}
		TextUtils.textViewSpan(getActivity(), holder.wbtext, holder.wbtext
				.getText().toString());
		holder.wbtext.setTextSize(TypedValue.COMPLEX_UNIT_DIP, txtsize);

		/*
		 * holder.wbtext = new TextView(getActivity());
		 * holder.wbtext.setText((String) mItem.getText()); float txtsize =
		 * mItem.getText().length()/100*20; holder.wbtext.setTextSize(txtsize);
		 * ll.addView(holder.wbtext);
		 */

		// holder.wbimage = (ImageView)convertView.findViewById(R.id.wbimage);
		if (imageSrc.size() != 0) {
			RelativeLayout img = new RelativeLayout(getActivity());
			RelativeLayout.LayoutParams lp0 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp0.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			lp0.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
			ImageView imgApple0 = new ImageView(getActivity());
			imgApple0.setId(110); // 注意这点 设置id
			imgApple0.setImageBitmap(imageSrc.get(0));
			imgApple0.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imgApple0.setAdjustViewBounds(true);
			imgApple0.setMaxWidth(dip2px(getActivity(), 200));
			imgApple0.setMaxHeight(dip2px(getActivity(), 200));
			RelativeLayout.LayoutParams lp00 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp00.width = dip2px(getActivity(), 150);
			lp00.height = dip2px(getActivity(), 150);
			lp00.leftMargin = dip2px(getActivity(), 25);

			img.addView(imgApple0, lp00);

			if (imageSrc.size() > 1) {
				ImageView imgApple1 = new ImageView(getActivity());
				imgApple1.setId(111); // 注意这点 设置id
				imgApple1.setImageBitmap(imageSrc.get(1));
				imgApple1.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imgApple1.setAdjustViewBounds(true);
				imgApple1.setMaxWidth(dip2px(getActivity(), 200));
				imgApple1.setMaxHeight(dip2px(getActivity(), 200));
				RelativeLayout.LayoutParams lp01 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp01.addRule(RelativeLayout.RIGHT_OF, 110);
				lp01.width = dip2px(getActivity(), 150);
				lp01.height = dip2px(getActivity(), 150);
				lp01.leftMargin = dip2px(getActivity(), 20);
				// lp1.topMargin = 100;
				// img.addView(imgApple1,lp01);
				img.addView(imgApple1, lp01);
			}
			TextView txtApple0 = new TextView(getActivity());
			RelativeLayout.LayoutParams lp02 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			if (imageSrc.size() > 1) {
				lp02.addRule(RelativeLayout.RIGHT_OF, 111);
			} else {
				lp02.addRule(RelativeLayout.RIGHT_OF, 110);
			}
			lp02.addRule(RelativeLayout.CENTER_VERTICAL);
			if (imageSrc.size() > 2) {
				txtApple0.setText("共" + imageSrc.size() + "幅图像");
			}
			txtApple0.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
			img.addView(txtApple0, lp02);

			ll.addView(img, lp0);
		}

		if (mItem.getRetStat() == true) {
			TextView rettext = new TextView(getActivity());
			String retuser = "@" + mItem.getRetUser();
			float rettxtsize = 22;

			if (mItem.getRetText().length() < 30) {
				rettxtsize = 22;
			} else if (mItem.getRetText().length() < 200) {
				rettxtsize = 20;
			} else {
				rettxtsize = 18;
			}
			rettext.setText(retuser + ": " + mItem.getRetText());
			TextUtils.textViewSpan(getActivity(), rettext, rettext.getText()
					.toString());
			rettext.setTextSize(TypedValue.COMPLEX_UNIT_DIP, rettxtsize);
			rettext.setBackgroundColor(Color.LTGRAY);
			ll.addView(rettext);

			if (retimageSrc.size() != 0) {
				RelativeLayout relatimg = new RelativeLayout(getActivity());
				RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				lp1.addRule(RelativeLayout.CENTER_HORIZONTAL,
						RelativeLayout.TRUE);

				ImageView retimgApple0 = new ImageView(getActivity());
				retimgApple0.setId(220); // 注意这点 设置id
				retimgApple0.setImageBitmap(retimageSrc.get(0));
				retimgApple0.setScaleType(ImageView.ScaleType.FIT_CENTER);
				retimgApple0.setAdjustViewBounds(true);
				// retimgApple0.setMaxHeight(400);
				// retimgApple0.setMaxWidth(300);

				RelativeLayout.LayoutParams lp20 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp20.width = dip2px(getActivity(), 150);
				lp20.height = dip2px(getActivity(), 150);
				lp20.leftMargin = dip2px(getActivity(), 25);

				// lp1.topMargin = 100;
				relatimg.addView(retimgApple0, lp20);
				if (retimageSrc.size() > 1) {
					ImageView retimgApple1 = new ImageView(getActivity());
					retimgApple1.setId(221); // 注意这点 设置id
					retimgApple1.setImageBitmap(retimageSrc.get(1));
					RelativeLayout.LayoutParams lp21 = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					lp21.addRule(RelativeLayout.RIGHT_OF, 220);
					lp21.width = dip2px(getActivity(), 150);
					lp21.height = dip2px(getActivity(), 150);
					lp21.leftMargin = dip2px(getActivity(), 20);
					// lp1.topMargin = 100;
					relatimg.addView(retimgApple1, lp21);
				}
				TextView txtApple1 = new TextView(getActivity());
				RelativeLayout.LayoutParams lp22 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				if (retimageSrc.size() > 1) {
					lp22.addRule(RelativeLayout.RIGHT_OF, 221);
				} else {
					lp22.addRule(RelativeLayout.RIGHT_OF, 220);
				}
				lp22.addRule(RelativeLayout.CENTER_VERTICAL);
				lp22.leftMargin = dip2px(getActivity(), 20);
				if (retimageSrc.size() > 2) {
					txtApple1.setText("共" + retimageSrc.size() + "幅图像");
				}
				txtApple1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
				relatimg.addView(txtApple1, lp22);
				// relatimg.setBackgroundColor(Color.GRAY);

				ll.addView(relatimg, lp1);
			}
		}

		return convertView;

	}

	/**
	 * 将dip转换为px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px转换为dip
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public final class WeiBoHolder {

		public ImageView wbicon;
		public TextView wbtext;
		public TextView wbsrc;
		public TextView wbtime;
		public TextView wbuser;
		public ImageView wbimage;

		public TextView ret_wbtext;
		public TextView ret_wbuser;
		public ImageView ret_wbimage;

	}

}
