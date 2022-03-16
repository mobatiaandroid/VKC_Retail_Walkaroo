package com.mobatia.vkcretailer.model;

import androidx.appcompat.R.string;

public class Related_Images extends ParentFilterModel {
	private String imageurl;
	private String color_id;
	private String product_id;

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public void setmageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageId(String color_id) {
		this.color_id = color_id;
	}

	public String getImageId() {
		return color_id;
	}
}
