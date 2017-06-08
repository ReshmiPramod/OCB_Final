package com.smartbuy.ocb.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Skus")
public class SkuDto {
	
	private long skuNumber;
	private int storeNumber;
	private int trkDlvrDays;
	private int shelfQty;
	private int inStrQty;
	private int skuRecThres; 
	private String skuDescription;
	private String skuVelocity;
	private int orderQty;
	private int PONumber;
	private int isApproved;
	
	public SkuDto() {
		// TODO Auto-generated constructor stub
	}
	
	public long getSkuNumber() {
		return skuNumber;
	}
	public void setSkuNumber(long skuNumber) {
		this.skuNumber = skuNumber;
	}
	public int getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(int storeNumber) {
		this.storeNumber = storeNumber;
	}
	public int getTrkDlvrDays() {
		return trkDlvrDays;
	}
	public void setTrkDlvrDays(int trkDlvrDays) {
		this.trkDlvrDays = trkDlvrDays;
	}
	public int getShelfQty() {
		return shelfQty;
	}
	public void setShelfQty(int shelfQty) {
		this.shelfQty = shelfQty;
	}
	public int getInStrQty() {
		return inStrQty;
	}
	public void setInStrQty(int inStrQty) {
		this.inStrQty = inStrQty;
	}
	public int getSkuRecThres() {
		return skuRecThres;
	}
	public void setSkuRecThres(int skuRecThres) {
		this.skuRecThres = skuRecThres;
	}
	public String getSkuDescription() {
		return skuDescription;
	}
	public void setSkuDescription(String skuDescription) {
		this.skuDescription = skuDescription;
	}
	public String getSkuVelocity() {
		return skuVelocity;
	}
	public void setSkuVelocity(String skuVelocity) {
		this.skuVelocity = skuVelocity;
	}
	public int getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}
	public int getPONumber() {
		return PONumber;
	}
	public void setPONumber(int pONumber) {
		PONumber = pONumber;
	}

	public int getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(int isApproved) {
		this.isApproved = isApproved;
	}

}
