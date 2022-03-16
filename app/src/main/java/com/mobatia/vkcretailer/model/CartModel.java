/**
 * 
 */
package com.mobatia.vkcretailer.model;

/**
 * @author Archana.S
 * 
 */
public class CartModel {

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getProdSize() {
		return prodSize;
	}

	public void setProdSize(String prodSize) {
		this.prodSize = prodSize;
	}

	public String getProdColor() {
		return prodColor;
	}

	public void setProdColor(String prodColor) {
		this.prodColor = prodColor;
	}

	public String getProdQuantity() {
		return prodQuantity;
	}

	public void setProdQuantity(String prodQuantity) {
		this.prodQuantity = prodQuantity;
	}

	public String getProdGridValue() {
		return prodGridValue;
	}

	public void setProdGridValue(String prodGridValue) {
		this.prodGridValue = prodGridValue;
	}

	String prodId;
	String prodName;
	String prodSize;
	String prodColor;
	String prodQuantity;
	String prodGridValue;
	String prodSizeId;
	String prodColorId;
	String Prod_Unit;

	public String getProd_Unit() {
		return Prod_Unit;
	}

	public void setProd_Unit(String prod_Unit) {
		Prod_Unit = prod_Unit;
	}

	public String getProdSizeId() {
		return prodSizeId;
	}

	public void setProdSizeId(String prodSizeId) {
		this.prodSizeId = prodSizeId;
	}

	public String getProdColorId() {
		return prodColorId;
	}

	public void setProdColorId(String prodColorId) {
		this.prodColorId = prodColorId;
	}

}
