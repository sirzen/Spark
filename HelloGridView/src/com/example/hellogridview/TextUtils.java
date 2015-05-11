package com.example.hellogridview;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.hellogridview.R;
import com.example.hellogridview.R.drawable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

public class TextUtils {
	private static List<Emotion> emotions = new ArrayList<Emotion>();

	/**
	 * 
	 * @param mContext
	 * @param textview
	 * @param content
	 * @param hasClick
	 *            是否添加click
	 */
	public static void textViewSpan(Context mContext, TextView textview,
			String content) {
		boolean hasClick = false;
		List<PositionItem> list = paresString2(content);
		Spannable span = new SpannableString(content);
		// 测试表情
		Emotion emotion_0 = new Emotion("aini.gif", "[爱你]");
		Emotion emotion_1 = new Emotion("baibai.gif", "[拜拜]");
		Emotion emotion_2 = new Emotion("baobao.gif", "[抱抱]");
		Emotion emotion_3 = new Emotion("bizui.gif", "[闭嘴]");
		Emotion emotion_4 = new Emotion("chijing.gif", "[吃惊]");
		Emotion emotion_5 = new Emotion("fuyun.gif", "[浮云]");
		Emotion emotion_6 = new Emotion("good.gif", "[good]");
		Emotion emotion_7 = new Emotion("guzhang.gif", "[鼓掌]");
		Emotion emotion_8 = new Emotion("haha.gif", "[哈哈]");
		Emotion emotion_9 = new Emotion("han.gif", "[汗]");
		Emotion emotion_10 = new Emotion("hehe.gif", "[呵呵]");
		Emotion emotion_11 = new Emotion("jiyan.gif", "[挤眼]");
		Emotion emotion_12 = new Emotion("keai.gif", "[可爱]");
		Emotion emotion_13 = new Emotion("kelian.gif", "[可怜]");
		Emotion emotion_14 = new Emotion("lei.gif", "[泪]");
		Emotion emotion_15 = new Emotion("nu.gif", "[怒]");
		Emotion emotion_16 = new Emotion("touxiao.gif", "[偷笑]");
		Emotion emotion_17 = new Emotion("xin.gif", "[心]");
		Emotion emotion_18 = new Emotion("xixi.gif", "[嘻嘻]");
		Emotion emotion_19 = new Emotion("zan.gif", "[赞]");
		Emotion emotion_20 = new Emotion("huaxin.gif", "[花心]");
		Emotion emotion_21 = new Emotion("shangxin.gif", "[伤心]");
		Emotion emotion_22 = new Emotion("zhutou.gif", "[猪头]");
		Emotion emotion_23 = new Emotion("taikaixin.gif", "[太开心]");
		Emotion emotion_24 = new Emotion("laidelini.gif", "[懒得理你]");
		Emotion emotion_25 = new Emotion("xu.gif", "[嘘]");
		Emotion emotion_26 = new Emotion("weiqu.gif", "[委屈]");
		Emotion emotion_27 = new Emotion("tu.gif", "[吐]");
		Emotion emotion_28 = new Emotion("chanzui.gif", "[馋嘴]");
		Emotion emotion_29 = new Emotion("shuijiao.gif", "[睡觉]");
		Emotion emotion_30 = new Emotion("kun.gif", "[困]");
		Emotion emotion_31 = new Emotion("heng.gif", "[哼]");
		Emotion emotion_32 = new Emotion("shiwang.gif", "[失望]");
		Emotion emotion_33 = new Emotion("dese.gif", "[得瑟]");
		Emotion emotion_34 = new Emotion("leiliumanmian.gif", "[泪流满面]");
		Emotion emotion_35 = new Emotion("xiaohaha.gif", "[笑哈哈]");
		Emotion emotion_36 = new Emotion("ye.gif", "[耶]");
		Emotion emotion_37 = new Emotion("ok.gif", "[ok]");
		Emotion emotion_38 = new Emotion("yinxian.gif", "[阴险]");
		Emotion emotion_39 = new Emotion("ku.gif", "[酷]");
		Emotion emotion_40 = new Emotion("zhuakuang.gif", "[抓狂]");
		Emotion emotion_41 = new Emotion("dahaqian.gif", "[打哈欠]");
		Emotion emotion_42 = new Emotion("shuai.gif", "[衰]");
		Emotion emotion_43 = new Emotion("jiong.gif", "[囧]");
		Emotion emotion_44 = new Emotion("hufen.gif", "[互粉]");
		Emotion emotion_45 = new Emotion("weiwu.gif", "[威武]");
		Emotion emotion_46 = new Emotion("weiguan.gif", "[围观]");
		Emotion emotion_47 = new Emotion("xiongmao.gif", "[熊猫]");
		Emotion emotion_48 = new Emotion("buyao.gif", "[不要]");
		Emotion emotion_49 = new Emotion("tuzi.gif", "[兔子]");
		Emotion emotion_50 = new Emotion("yun.gif", "[晕]");
		Emotion emotion_51 = new Emotion("heixian.gif", "[黑线]");
		Emotion emotion_52 = new Emotion("shengbing.gif", "[生病]");
		Emotion emotion_53 = new Emotion("qinqin.gif", "[亲亲]");
		Emotion emotion_54 = new Emotion("wabishi.gif", "[挖鼻屎]");

		if (emotions.isEmpty()) {
			emotions.add(emotion_0);
			emotions.add(emotion_1);
			emotions.add(emotion_2);
			emotions.add(emotion_3);
			emotions.add(emotion_4);
			emotions.add(emotion_5);
			emotions.add(emotion_6);
			emotions.add(emotion_7);
			emotions.add(emotion_8);
			emotions.add(emotion_9);
			emotions.add(emotion_10);
			emotions.add(emotion_11);
			emotions.add(emotion_12);
			emotions.add(emotion_13);
			emotions.add(emotion_14);
			emotions.add(emotion_15);
			emotions.add(emotion_16);
			emotions.add(emotion_17);
			emotions.add(emotion_18);
			emotions.add(emotion_19);
			emotions.add(emotion_20);
			emotions.add(emotion_21);
			emotions.add(emotion_22);
			emotions.add(emotion_23);
			emotions.add(emotion_24);
			emotions.add(emotion_25);
			emotions.add(emotion_26);
			emotions.add(emotion_27);
			emotions.add(emotion_28);
			emotions.add(emotion_29);
			emotions.add(emotion_30);
			emotions.add(emotion_31);
			emotions.add(emotion_32);
			emotions.add(emotion_33);
			emotions.add(emotion_34);
			emotions.add(emotion_35);
			emotions.add(emotion_36);
			emotions.add(emotion_37);
			emotions.add(emotion_38);
			emotions.add(emotion_39);
			emotions.add(emotion_40);
			emotions.add(emotion_41);
			emotions.add(emotion_42);
			emotions.add(emotion_43);
			emotions.add(emotion_44);
			emotions.add(emotion_45);
			emotions.add(emotion_46);
			emotions.add(emotion_47);
			emotions.add(emotion_48);
			emotions.add(emotion_49);
			emotions.add(emotion_50);
			emotions.add(emotion_51);
			emotions.add(emotion_52);
			emotions.add(emotion_53);
			emotions.add(emotion_54);
		}
		// 结束测试

		for (PositionItem pi : list) {
			if (pi.getPrefixType() == 4) {
				String imageName = "";
				for (Emotion em : emotions) {
					if (em.getPhrase().equals(pi.getContent())) {
						imageName = em.getSaveName2();
						break;
					}
				}
				//
				try {
					Field f = (Field) R.drawable.class
							.getDeclaredField(imageName);
					int eId = f.getInt(R.drawable.class);
					Drawable drawable = mContext.getResources()
							.getDrawable(eId);
					if (drawable != null) {
						drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
								drawable.getIntrinsicHeight());
						ImageSpan imgSpan = new ImageSpan(drawable,
								ImageSpan.ALIGN_BASELINE);
						span.setSpan(imgSpan, pi.start, pi.end,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					} else {
						span.setSpan(new ForegroundColorSpan(Color.BLUE),
								pi.start, pi.end,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				} catch (Exception e) {
					// TODO: handle exception
					span.setSpan(new ForegroundColorSpan(Color.BLUE), pi.start,
							pi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} else if (pi.getPrefixType() == 3) {
				span.setSpan(new URLSpan("链接"), pi.start, pi.end,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else {
				span.setSpan(new ForegroundColorSpan(Color.BLUE), pi.start,
						pi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		textview.setText(span);
	}

	public static List<PositionItem> paresString(String content) {
		String regex = "@[^\\s:：《]+([\\s:：《]|$)|#(.+?)#|http://t\\.cn/\\w+|\\[(.*?)\\]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		boolean b = m.find();
		List<PositionItem> list = new ArrayList<PositionItem>();
		while (b) {
			System.out.println(m.start());
			System.out.println(m.end());
			System.out.println(m.group());
			int start = m.start();
			int end = m.end();
			String str = m.group();
			list.add(new PositionItem(start, end, str, content.length()));
			b = m.find(m.end() - 1);
		}
		return list;
	}

	/**
	 * 这个是处理一条信息有多个#...
	 * 
	 * @param content
	 * @return
	 */
	public static List<PositionItem> paresString2(String content) {
		String regex = "@[^\\s:：《]+([\\s:：《]|$)|#(.+?)#|http://t\\.cn/\\w+|\\[(.*?)\\]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		boolean b = m.find();
		List<PositionItem> list = new ArrayList<PositionItem>();
		int count = 0;
		int lastIndex = 0;
		while (b) {
			System.out.println(m.start());
			System.out.println(m.end());
			System.out.println(m.group());
			int start = m.start();
			int end = m.end();
			String str = m.group();
			if (str.startsWith("#")) {
				count++;
				if (count % 2 == 0) {
					b = m.find(lastIndex);
					continue;
				}
			}
			list.add(new PositionItem(start, end, str, content.length()));
			b = m.find(m.end() - 1);
			try {
				lastIndex = m.start() + 1;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return list;
	}

	public static class PositionItem {
		public int start;
		public int end;
		private int prefixType;
		private String content;
		private int strLenght;

		public PositionItem(int start, int end, String content, int strLenght) {
			// TODO Auto-generated constructor stub
			this.start = start;
			this.end = end;
			this.content = content;
			this.strLenght = strLenght;
		}

		public PositionItem(int start, int end, String content) {
			// TODO Auto-generated constructor stub
			this.start = start;
			this.end = end;
			this.content = content;
		}

		public String getContent() {
			return content;
		}

		public String getContentWithoutPrefix() {
			switch (getPrefixType()) {
			case 1:
				if (end == strLenght)
					return content.substring(1, strLenght);
				return content.substring(1, content.length() - 1);
			case 2:
				return content.substring(1, content.length() - 1);
			case 3:
				return content;
			default:
				return content;
			}
		}

		/**
		 * 1 @ 人物 2 # 话题 3 http://t.cn/ 短链 4 [ 表情
		 * 
		 * @return
		 */
		public int getPrefixType() {
			if (content.startsWith("@"))
				return 1;
			if (content.startsWith("#"))
				return 2;
			if (content.startsWith("http://"))
				return 3;
			if (content.startsWith("["))
				return 4;
			return -1;
		}
	}
}