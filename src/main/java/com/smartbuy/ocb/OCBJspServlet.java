package com.smartbuy.ocb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.smartbuy.ocb.bo.OrderCreationBatchBO;
import com.smartbuy.ocb.dto.SkuDto;
import com.smartbuy.ocb.exceptions.OcbException;

/**
 * Servlet implementation class OCBJspServlet
 */
public class OCBJspServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private OrderCreationBatchBO orderBo;
	private static Logger logger = Logger.getLogger(OCBJspServlet.class);
	private static String LIST_SKUS = "/index.jsp";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OCBJspServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int param = 1;
		PrintWriter writer = response.getWriter();
		List<SkuDto> skus = new ArrayList<SkuDto>();
		
		try {		
				String storeNumber = request.getParameter("storeNumber");
				logger.debug("Store Number parameter value ::" + storeNumber);
				response.setContentType("text/HTML");
				
				if (storeNumber != null && storeNumber.length() > 0) {
					orderBo = new OrderCreationBatchBO(param);
					  skus = orderBo.fetchSkuList(storeNumber);
						if (skus != null && skus.size() > 0) {
							skus = orderBo.executeOrderCreation(skus);
							if (skus !=null){
								listAllSkus(request,response,skus);
								}
						   }
					}
			} catch (OcbException exe) {
				logger.error("OCB Exception ::" + exe.getMessage(), exe);
				writer.println("OCB Exception ::" + exe.getMessage());
				}

				catch (Exception e) {
			// TODO: handle exception
						logger.error("OCB Exception ::" + e.getMessage(), e);
						writer.println("OCB Exception ::" + e.getMessage());
						}
						response.flushBuffer();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void listAllSkus(HttpServletRequest request, HttpServletResponse response,
			List<SkuDto> orderDetails) throws IOException,Exception,ServletException{
		
		request.setAttribute("orderDetails", orderDetails);
		RequestDispatcher dispatcher = request.getRequestDispatcher(LIST_SKUS);
		try {
			dispatcher.forward(request, response);
			} catch (ServletException e) {
					throw new OcbException(e.getMessage(), e);
			} catch (IOException e) {
				throw new OcbException(e.getMessage(), e);
			}
}
}
