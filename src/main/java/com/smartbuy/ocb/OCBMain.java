package com.smartbuy.ocb;

import java.util.List;

import org.apache.log4j.Logger;

import com.smartbuy.ocb.bo.OrderCreationBatchBO;
import com.smartbuy.ocb.dto.SkuDto;
import com.smartbuy.ocb.exceptions.OcbException;


public class OCBMain {
	//  Log4j implementation 
	private static Logger logger = Logger.getLogger(OCBMain.class);
//	OrderCreationDaoImpl dao;
	
	public OCBMain() {
		
	}

	public static void main(String[] args) throws OcbException {
	
		
		int param = 0;
		OrderCreationBatchBO orderBo = new OrderCreationBatchBO(param);
		
		//  take store from main argument array
//		int storeNumber = Integer.parseInt(args[0]);
		String storeNumber = args[0];
		 List<SkuDto> skus = orderBo.fetchSkuList(storeNumber);
		
			try {
				if(skus != null && skus.size() > 0){
					orderBo.executeOrderCreation(skus);
				}
			} catch (OcbException exe) {
				logger.error("OCB Exception ::" + exe.getMessage(),exe);
				
			}
			catch (Exception e) {
				logger.error("OCB Exception ::" + e.getMessage(),e);
			}	
	  }//main close		
	}


