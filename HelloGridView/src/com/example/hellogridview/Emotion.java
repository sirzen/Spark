package com.example.hellogridview;

public class Emotion {

	private String phrase;// ����ʹ�õ��������
	private String type;
	private String url;// ����ͼƬ��ŵ�λ��
	private boolean hot;// �Ƿ�Ϊ���ű���
	private boolean common;// �Ƿ�����ͨ��
	private String category;// �������
	private String icon;
	private String picid;
	private String saveName;

	public Emotion() {
		// TODO Auto-generated constructor stub
	}

	public Emotion(String url, String phrase) {
		// TODO Auto-generated constructor stub
		this.url = url;
		this.phrase = phrase;
	}

	/**
	 * �����׺.gif,��drawable�л�ȡ��ʱ����Ҫ���������
	 * 
	 * @return
	 */
	public String getSaveName2() {
		String s1 = url.substring(url.lastIndexOf("/") + 1, url.length());
		return s1.substring(0, s1.indexOf(".gif"));
	}

	/**
	 * ���׺.gif
	 * 
	 * @return
	 */
	public String getSaveName() {
		return url.substring(url.lastIndexOf("/") + 1, url.length());
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isHot() {
		return hot;
	}

	public void setHot(boolean hot) {
		this.hot = hot;
	}

	public boolean isCommon() {
		return common;
	}

	public void setCommon(boolean common) {
		this.common = common;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPicid() {
		return picid;
	}

	public void setPicid(String picid) {
		this.picid = picid;
	}

}
