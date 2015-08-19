package com.tongdao.sdk.beans;

import java.util.ArrayList;

public class TdPageBean {
	
	private String image;
	
	private ArrayList<TdRewardBean> rewardList;
	
	
	/**
	 * 
	 *获得奖品信息列表
	 *@return
	 *ArrayList<RewardBean> 奖品信息列表
	 */
	public ArrayList<TdRewardBean> getRewardList() {
		return rewardList;
	}

	/**
	 * 
	 *同道内部调用,不建议使用
	 */
	public void setRewardList(ArrayList<TdRewardBean> rewardList) {
		this.rewardList = rewardList;
	}

	/**
	 * 
	 *获得着陆页的图片链接
	 *@return
	 *String 图片链接
	 */
	public String getImage() {
		return image;
	}

	/**
	 * 
	 *同道内部调用,不建议使用
	 */
	public void setImage(String image) {
		this.image = image;
	}
}