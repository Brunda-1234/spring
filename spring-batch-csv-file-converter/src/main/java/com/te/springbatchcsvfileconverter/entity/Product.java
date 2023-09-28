package com.te.springbatchcsvfileconverter.entity;

import lombok.Data;

@Data
public class Product {

	private int prodId;
	private String prodCode;
	private double prodCost;
	private double prodDic;
	private double prodGst;
}
