package com.smartbuy.ocb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.smartbuy.ocb.dto.SkuDto;
import com.smartbuy.ocb.exceptions.OcbException;


public class OrderCreationDaoImpl implements IOrderCreationDAO {
	
	private DAOFactory daoFactory  = new DAOFactory();
	private List<SkuDto> skuList = new ArrayList<SkuDto>();
	private int param = 0;
	
	public OrderCreationDaoImpl(int param) {
		this.param = param;
	}

	public String getSkuDescription(long skuNumber) throws OcbException{
		PreparedStatement psSku = null;
		Connection con = null;
		String skuDesc = null;
					
			try {
				con = daoFactory.getDBConnection(param);
				psSku = con.prepareStatement(getSkuDesc);
				psSku.setLong(1, skuNumber);
				ResultSet rs = psSku.executeQuery();
				while (rs.next()){
					skuDesc = rs.getString("SKU_DESC");
				}
			
				} catch (Exception e) {
					throw new OcbException(e.getMessage(), e);
					}
				  finally{
					  daoFactory.closeConnection(con);
				  	}
			return skuDesc;
		}

	
	public List<SkuDto> getSkusFromStore(int storeNumber) throws OcbException {
		PreparedStatement ps = null;
		Connection con = null;
		
	try {
		
		con = daoFactory.getDBConnection(param);
		ps = con.prepareStatement(getSkusfromStore);
		ps.setInt(1, storeNumber);
		ResultSet rs = ps.executeQuery();
		int i;

		while (rs.next()) {
			i=0;
			SkuDto skus = new SkuDto();
			skus.setSkuNumber(rs.getLong("SKU_NUMBER"));	
			skus.setStoreNumber(rs.getInt("STORE_NUM"));
			skus.setSkuVelocity(rs.getString("SKU_VELOCITY"));
			skus.setTrkDlvrDays(rs.getInt("TRK_DLVR_TIME_DAYS"));
			skus.setShelfQty(rs.getInt("SHELF_QTY"));
			skus.setInStrQty(rs.getInt("IN_STR_QTY"));
			skus.setSkuRecThres(rs.getInt("SKU_RCMD_THRD"));
			skuList.add(i, skus);
			i++;
		
		}
	} catch (Exception e) {
		throw new OcbException(e.getMessage(), e);
				}
		finally{
			daoFactory.closeConnection(con);
				}

	return skuList;
	}

	public int getPurchaseOrderNum() throws OcbException {
		SkuDto PONum = new SkuDto();
		PreparedStatement psSelect = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;
		Connection con = null;
		int poNum =0;
		try{
			con = daoFactory.getDBConnection(param);
		    con.setAutoCommit(false);
		
		psSelect = con.prepareStatement(getPONumber);
		rs = psSelect.executeQuery();
		while(rs.next()){
			PONum.setPONumber(rs.getInt("LAST_PO_NUM"));
			poNum = PONum.getPONumber();
			poNum = poNum + 1;
		}
		if(!PONum.equals(null)){
			psUpdate = con.prepareStatement(updatePONumber);
			psUpdate.setInt(1,poNum);
			psUpdate.setInt(2, PONum.getPONumber());
			
			psUpdate.executeUpdate();
			System.out.println(poNum);
			con.commit();
		}
		}catch (Exception e) {
			throw new OcbException(e.getMessage(), e);
					}
			finally{
				daoFactory.closeConnection(con);
					}
		return poNum;
	}

	public boolean updateOrderCreation(SkuDto list) throws OcbException {
		PreparedStatement psInsert = null;
		Connection con = null;
		try{
			con = daoFactory.getDBConnection(param);
			psInsert = con.prepareStatement(insertValues);
			String poValue = Integer.toString(list.getPONumber());
			psInsert.setString(1, poValue);
			psInsert.setLong(2, list.getSkuNumber());
			psInsert.setInt(3, list.getStoreNumber());
			psInsert.setInt(4, list.getOrderQty());
			psInsert.setInt(5, list.getIsApproved());
			
			psInsert.executeUpdate();
			psInsert.close();
			
		}catch (Exception e) {
			throw new OcbException(e.getMessage(), e);
		}
			finally{
				daoFactory.closeConnection(con);
					}
		
		return true;
	}

	public int getOldOrderQty(long skuNum) throws OcbException {
		PreparedStatement psQty = null;
		ResultSet rs = null;
		Connection con = null;
		int qty = 0;
		try {
			con = daoFactory.getDBConnection(param);
			psQty = con.prepareStatement(getOldOrderQty);
			psQty.setLong(1, skuNum);
			rs = psQty.executeQuery();
			while (rs.next()){
				qty = rs.getInt("ORDR_QTY");
			}
		
			} catch (Exception e) {
				throw new OcbException(e.getMessage(), e);
				}
			  finally{
				  daoFactory.closeConnection(con);
			  	}
		
		
		return qty;
	}

	public void updateOrdDetails(SkuDto appList) throws OcbException {
		
		PreparedStatement psOrd = null;
		Connection con = null;
		
		try {
			con = daoFactory.getDBConnection(param);
			psOrd = con.prepareStatement(updateSkus);
			psOrd.setInt(1, appList.getOrderQty());
			psOrd.setInt(2, appList.getIsApproved());			
			psOrd.setLong(3, appList.getSkuNumber());
			
			psOrd.executeUpdate();
					
			} catch (Exception e) {
				throw new OcbException(e.getMessage(), e);
				}
			  finally{
				  daoFactory.closeConnection(con);
			  	}
		
	}

	public List<SkuDto> getApprSkus(int appStatus) throws OcbException {
		PreparedStatement ps = null;
		Connection con = null;
		
	try {
		
		con = daoFactory.getDBConnection(param);
		ps = con.prepareStatement(getApprovalSkus);
		ps.setInt(1, appStatus);
		ResultSet rs = ps.executeQuery();
		int i;

		while (rs.next()) {
			i=0;
			SkuDto skus = new SkuDto();
			skus.setPONumber(rs.getInt("PO_NUMBER"));
			skus.setSkuNumber(rs.getLong("SKU_NUMBER"));	
			skus.setStoreNumber(rs.getInt("STR_NUMBER"));
			skus.setOrderQty(rs.getInt("ORDR_QTY"));
			skus.setIsApproved(rs.getInt("IS_APPROVED"));
			skuList.add(i, skus);
			i++;
		
		}
	} catch (Exception e) {
		throw new OcbException(e.getMessage(), e);
				}
		finally{
			daoFactory.closeConnection(con);
				}

	return skuList;
	}
	

	

}
