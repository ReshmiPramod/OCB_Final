package com.smartbuy.ocb.dao;

import java.util.List;

import com.smartbuy.ocb.dto.SkuDto;
import com.smartbuy.ocb.exceptions.OcbException;



public interface IOrderCreationDAO {
	
	String getSkusfromStore = ""
			+ "SELECT `sku_store`.`SKU_NUMBER`, "
			+ "    `sku_store`.`STORE_NUM`, "
			+ "    `sku_store`.`SKU_VELOCITY`, "
			+ "    `sku_store`.`TRK_DLVR_TIME_DAYS`, "
			+ "    `sku_store`.`SHELF_QTY`, "
			+ "    `sku_store`.`IN_STR_QTY`, "
			+ "    `sar_parm`.`SKU_RCMD_THRD` "
			+ "FROM `retail`.`sku_store`,`retail`.`sar_parm` "
			+ "where `sku_store`.`SKU_NUMBER` = `sar_parm`.`SKU_NUMBER` "
			+ "and `sku_store`.`STORE_NUM` = ?;";
	
	String getPONumber = ""
			+ "SELECT `po_number`.`LAST_PO_NUM` "
			+ "FROM `retail`.`po_number`;";
	
	String updatePONumber = ""
			+ "UPDATE `retail`.`po_number` "
			+ "SET `LAST_PO_NUM` = ? "
			+ "WHERE `LAST_PO_NUM` = ?";

	String insertValues = ""
			+ "INSERT INTO `retail`.`sar_po` "
			+ "(`PO_NUMBER`, "
			+ "`SKU_NUMBER`, "
			+ "`STR_NUMBER`, "
			+ "`ORDR_QTY`, "
			+ "`IS_APPROVED`) "
			+ "VALUES (?,?,?,?,?);";

	String getSkuDesc = ""
			+ "SELECT  `sku`.`SKU_DESC` FROM `retail`.`sku` WHERE `sku`.`SKU_NUMBER`= ?;";
	
	String getOldOrderQty = ""
			+ "SELECT `sar_po`.`ORDR_QTY` FROM `retail`.`sar_po` WHERE `sar_po`.`SKU_NUMBER`= ?;";
	
	String updateSkus = ""
			+ "UPDATE `retail`.`sar_po` "
			+ "SET `ORDR_QTY` = ?,`IS_APPROVED` = ? "
			+ "WHERE `SKU_NUMBER` = ? ;";
	
	String getApprovalSkus = ""
			+ "SELECT * FROM `retail`.`sar_po` WHERE `sar_po`.`IS_APPROVED` = ? ;";
	
	public List<SkuDto> getSkusFromStore(int storeNumber) throws OcbException;
	
	public String getSkuDescription(long skuNumber) throws OcbException;

	public int getPurchaseOrderNum() throws OcbException;
 
	public boolean updateOrderCreation(SkuDto list) throws OcbException;
	
	public int getOldOrderQty(long skuNum) throws OcbException;
	
	public void updateOrdDetails(SkuDto appList) throws OcbException;

	public List<SkuDto> getApprSkus(int status) throws OcbException;

	 
	
}
