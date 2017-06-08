package com.smartbuy.ocb.bo;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;


import com.smartbuy.ocb.exceptions.OcbException;
import com.smartbuy.ocb.dao.IOrderCreationDAO;
import com.smartbuy.ocb.dao.OrderCreationDaoImpl;
import com.smartbuy.ocb.dto.SkuDto;

public class OrderCreationBatchBO {
	private IOrderCreationDAO dao;
	private static Logger logger = Logger.getLogger(OrderCreationBatchBO.class);
	private static int poNum;
	private String skuDescription;


	public OrderCreationBatchBO(int param) {
		dao = new OrderCreationDaoImpl(param);
	}

	// get the list of skus by store number
	public List<SkuDto> fetchSkuList(String storeNumber) throws OcbException {
		List<SkuDto> skuList = new ArrayList<SkuDto>();

		try {
			if (dao != null) {
				int intStoreNum = Integer.parseInt(storeNumber);
				 skuList = dao.getSkusFromStore(intStoreNum);
			}
		} catch (Exception e) {
			throw new OcbException(e.getMessage(), e);
		}

		return skuList;

	}

	public List<SkuDto> executeOrderCreation(List<SkuDto> skuList) throws OcbException {

		List<SkuDto> skuDetails = new ArrayList<SkuDto>();
		// List<OrderDTO> skuDetails = null;
		boolean poExecuted = false;
		int skuVel = 0;
		for (SkuDto sList : skuList) {
			logger.debug("List of Skudetails :" + sList.getSkuNumber() + " " + sList.getShelfQty() + " "
					+ sList.getInStrQty() + " " + sList.getSkuRecThres());

			int qty = sList.getShelfQty() + sList.getInStrQty();
			logger.debug("Total Quantity :" + qty);

			// get SKU description from SKU table
			skuDescription = dao.getSkuDescription(sList.getSkuNumber());

			// set SKU description in the sku list
			sList.setSkuDescription(skuDescription);
			if (qty < sList.getSkuRecThres()) {
				try {
					if (!poExecuted) {
						poNum = dao.getPurchaseOrderNum(); // get the last PO
															// number
						poExecuted = true;
					}
					skuVel = Integer.parseInt(sList.getSkuVelocity());
				} catch (NumberFormatException e) {
					logger.error("Number format Exception", e);
					throw new OcbException(e.getMessage());
				}
				// calculate the order quantity
				if (poNum != 0) {
					sList.setPONumber(poNum);
					int orderQty = skuVel * sList.getTrkDlvrDays();
					sList.setOrderQty(orderQty);
					int flag = 0;
					sList.setIsApproved(flag);
					try {
						dao.updateOrderCreation(sList);
				//		sList.setPONumber(poNum);
						// sList = new SkuDto();
						skuDetails.add(sList);

					} catch (Exception e) {
						throw new OcbException(e.getMessage(), e);
					}
				} // end if of poNum check
			}
		} // end of for loop
		return skuDetails;
	}

	public List<SkuDto> updateOrder(List<SkuDto> ordList) throws OcbException{
		
		List<SkuDto> adminlist = new ArrayList<SkuDto>();
		int minPer;
		int maxPer;
		int apprStatus = 1;
		
		for(SkuDto sk : ordList){		
			int oldOrdVal = dao.getOldOrderQty(sk.getSkuNumber());
			int newOrdVal = sk.getOrderQty();
			if (oldOrdVal != newOrdVal){
				int percent = (int) (oldOrdVal * 0.1);
				minPer = oldOrdVal - percent;
				maxPer = oldOrdVal + percent;

					if(newOrdVal >= minPer && newOrdVal <= maxPer){
						//update SAR_PO table with  flag has approved
						sk.setIsApproved(apprStatus);
						sk.setOrderQty(newOrdVal);
						dao.updateOrdDetails(sk);
						 
					}else{
						//collect those skus and sent jms message
						SkuDto adminDto = new SkuDto();
						adminDto.setSkuNumber(sk.getSkuNumber());
						adminDto.setSkuDescription(sk.getSkuDescription());
						adminDto.setOrderQty(newOrdVal);
						adminlist.add(adminDto);
					}
					
			}else // end of first if condition
				{
				sk.setIsApproved(apprStatus);
				dao.updateOrdDetails(sk);
				}
//			dao.updateOrdDetails(sk);
		}     //end of for loop
		
		//send jms message
		try {
			sendJmsMessage();
		} catch (Exception e) {
			throw new OcbException(e.getMessage(), e);
		}
		
		return adminlist;
	}
	
	//function to send jms message
	private void sendJmsMessage() throws JMSException, OcbException {
		 try {
			//Create and start connection  
			InitialContext context = new InitialContext();
			Context envContex = (Context) context.lookup("java:comp/env");

			ConnectionFactory qcf = (ConnectionFactory) envContex.lookup("jms/ConnectionFactory");
			Connection con = qcf.createConnection();
			con.start();
			//create queue session
			Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("SAR.ADMIN.APPROVAL");
			MessageProducer producer = session.createProducer(queue);
			Message message = session.createTextMessage("Following Skus need approval :");
			producer.send(message);
			con.close();
			
		} catch (Exception e) {
			throw new OcbException(e.getMessage(), e);
		}
		 
		
	}

	
	public List<SkuDto> getOriginalValues() throws OcbException {
		int status = 0;
		List<SkuDto> orgList = new ArrayList<SkuDto>();
		try {
			if (dao != null)
				{
				 orgList = dao.getApprSkus(status);
				}
			} catch (Exception e) {
				throw new OcbException(e.getMessage(), e);
			
			}
		
		return orgList;
	}

	public String getPONum() {
		
		String purchaseNum = null;
		
			if(poNum != 0){
				purchaseNum = "SAR"+ poNum;	
			}
			return purchaseNum;
	}
	

}
