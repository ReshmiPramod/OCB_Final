package com.smartbuy.ocb.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbuy.ocb.bo.OrderCreationBatchBO;
import com.smartbuy.ocb.dto.SkuDto;
import com.smartbuy.ocb.exceptions.OcbException;
import com.smartbuy.ocb.web.util.XMLTransformer;

@Path("/ocb")
public class SARService {

	private final Logger logger = Logger.getLogger(SARService.class);
	private OrderCreationBatchBO orderBo;
	private int param = 1; 
//	private static List<SkuDto> adminlst = new ArrayList<SkuDto>();
	public SARService() {
	
	}

	@GET
	@Path("/getDetails/{storeNumber}")
	@Produces("application/json")
	public String getSkuDetails(@PathParam("storeNumber") String storeNumber) {
		String res = null;
		   
		List<SkuDto> skuDet = new ArrayList<SkuDto>();
		logger.debug("Received Store Number" + storeNumber);
		try {
			orderBo = new OrderCreationBatchBO(param);
			if (storeNumber != null) {
				skuDet = orderBo.fetchSkuList(storeNumber);
				
				//if skus list is not null order creation need to be called
				  if(skuDet != null && skuDet.size() > 0){
					  skuDet = orderBo.executeOrderCreation(skuDet);				
					  if(skuDet != null){
						  res = XMLTransformer.toXML(skuDet);
					  }
				 }
			} else {
				res = "Invalid Store Number";
			}
		} catch (OcbException e) {
			logger.error("Exception in Order BO" + e.getMessage(), e);
			res = e.getMessage();
		}
		return res;
	}
	
	@POST
	@Path("/updateOrderDetails/")
	@Consumes("application/json")
	@Produces("application/json")
	public String updateOrder(@FormParam("orderList") @DefaultValue("") String orderList) throws
											JsonParseException, JsonMappingException, IOException {
		
		OrderCreationBatchBO ordBO = new OrderCreationBatchBO(param);
		String res = null;
		ObjectMapper mapper = new ObjectMapper();
		List<SkuDto> adminlst = new ArrayList<SkuDto>();
		TypeReference<ArrayList<SkuDto>> mapType = new TypeReference<ArrayList<SkuDto>>(){};
		
		try{
			List<SkuDto> slist = mapper.readValue(orderList, mapType);
			logger.debug("Java object is received");
			if(slist != null && slist.size() > 0){
				 adminlst = ordBO.updateOrder(slist);
					if(adminlst != null){
						res = XMLTransformer.toXML(adminlst);						
					}				
			}else{
				res = "List is empty";
				}
				
		  }catch (OcbException e) {
			logger.error("Exception in Order BO" + e.getMessage(), e);
			res = e.getMessage();
		  	}
		return res ;
	}
	
	
	@GET
	@Path("/getOrgQty/")
	@Produces("application/json")
	public String getOriginalQty(){
		String res = null;
		List<SkuDto> qtyList = new ArrayList<SkuDto>();
		OrderCreationBatchBO ordBO = new OrderCreationBatchBO(param);
		try {
			qtyList = ordBO.getOriginalValues();		
			 if(qtyList != null){
					res = XMLTransformer.toXML(qtyList);						
				}	
		} catch (OcbException e) {
			logger.error("Exception in Order BO" + e.getMessage(), e);
			res = e.getMessage();
		  	}
		
		
		return res;
	}
	
	@GET
	@Path("/getPONumber/")
	@Produces("application/json")
	public String getPoNumber(){
		
		OrderCreationBatchBO ordBO = new OrderCreationBatchBO(param);
		String poNum = null;
		String res = null;
		
			try {
				poNum = ordBO.getPONum();
				if(poNum != null){
				res = XMLTransformer.toXML(poNum);
				}
			}catch (OcbException e) {
				logger.error("Exception in Order BO" + e.getMessage(), e);
				res = e.getMessage();
			  	}	
		return res;
	}
}
