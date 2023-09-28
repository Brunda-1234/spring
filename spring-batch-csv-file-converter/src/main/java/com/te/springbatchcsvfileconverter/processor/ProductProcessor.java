package com.te.springbatchcsvfileconverter.processor;

import org.springframework.batch.item.ItemProcessor;

import com.te.springbatchcsvfileconverter.entity.Product;

public class ProductProcessor implements ItemProcessor<Product, Product> {

	@Override
	public Product process(Product item) throws Exception {
	double cost = item.getProdCost();
	item.setProdDic(cost* 12/100.0);
	item.setProdGst(cost* 22/100.0);
	
		return item;
	}

}
