package com.mobatia.vkcretailer.model;

import java.util.ArrayList;

public class ProductImages extends ParentFilterModel{

	
	private String mImageName;
	//color_code
	//color_id
	private ColorModel colorModel;
	
	
	/**
	 * @return the colorModel
	 */
	public ColorModel getColorModel() {
		return colorModel;
	}

	/**
	 * @param colorModel the colorModel to set
	 */
	public void setColorModel(ColorModel colorModel) {
		this.colorModel = colorModel;
	}

	/**
	 * @return the mImageName
	 */
	public String getImageName() {
		return mImageName;
	}

	/**
	 * @param mImageName the mImageName to set
	 */
	public void setImageName(String mImageName) {
		this.mImageName = mImageName;
	}
	
}
