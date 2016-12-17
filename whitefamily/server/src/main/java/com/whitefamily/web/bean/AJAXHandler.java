package com.whitefamily.web.bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.faces.context.FacesContextFactoryImpl;
import com.sun.faces.lifecycle.LifecycleImpl;
import com.whitefamily.service.IGoodsService;
import com.whitefamily.service.IInventoryService;
import com.whitefamily.service.IShopService;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFArtifact;
import com.whitefamily.service.vo.WFArtifact.StaffGoods;
import com.whitefamily.service.vo.WFBrand;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFIncoming;
import com.whitefamily.service.vo.WFOperationCost;
import com.whitefamily.service.vo.WFShop;
import com.whitefamily.service.vo.WFVendor;

public class AJAXHandler extends HttpServlet {

	Log logger = LogFactory.getLog(AJAXHandler.class);
	
	public AJAXHandler() {
		super();
		// service = new BreakfastBasicService();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}

	private void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setCharacterEncoding("utf-8");
		String action = req.getParameter("action");
		if ("filterCategory".equalsIgnoreCase(action)) {
			handleCategoryFilter(req, resp);
		} else if ("filterGoods".equals(action)) {
			handleGoodsFilter(req, resp);
		} else if ("filterBrand".equals(action)) {
			handleBrandFilter(req, resp);
		}  else if ("filterVendor".equals(action)) {
			handleVendorFilter(req, resp);
		} else if ("filterShop".equals(action)) {
			handleShopFilter(req, resp);
		} else if ("cartAction".equals(action)) {
			handleCartAction(req, resp);
		} else if ("chart".equalsIgnoreCase(action)) {
			handleChartAction(req, resp);
		} else if ("artifact".equalsIgnoreCase(action)) {
			handleArtifactSearchAction(req, resp);
		} else if ("statist".equalsIgnoreCase(action)) {
			handleStatistAction(req, resp);
		}

	}

	private void handleCategoryFilter(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String filter = req.getParameter("filter");
		JSONArray ret = new JSONArray();
		HttpSession session = req.getSession();
		CategoryBean cartBean = (CategoryBean) session
				.getAttribute("categoryBean");
		if (cartBean == null) {
			FacesContext context =FacesContext.getCurrentInstance();
			if (context == null) {
				FacesContextFactory facesContextFactory = new FacesContextFactoryImpl();
				context =  facesContextFactory.getFacesContext
						   (req.getServletContext(), req, resp, new LifecycleImpl());
				cartBean = (CategoryBean) context.getApplication().evaluateExpressionGet(context, "#{categoryBean}", CategoryBean.class);
			}
		} 
		List<WFCategory> list =  ServiceFactory.getCategoryService().getAllCategory();
		if (list != null) {
			boolean needFilter = filter == null || filter.isEmpty() ? false: true;
			int len = 1;
			Pattern  p = Pattern.compile("^(" + filter +")");
			for (WFCategory wf : list) {
				if (!needFilter || p.matcher(wf.getAbbr()).find()|| p.matcher(wf.getName()).find()) {
				JSONObject o = new JSONObject();
				o.put("name", wf.getName());
				o.put("id", wf.getId());
				ret.put(o);
				len ++;
				if (len > 9) {
					break;
				}
				}
			}
		}
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.print(ret.toString());
		out.flush();
	}

	private void handleGoodsFilter(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String filter = req.getParameter("filter");
		JSONArray ret = new JSONArray();
		HttpSession session = req.getSession();
		GoodsBean goodsBean = (GoodsBean) session.getAttribute("goodsBean");
		if (goodsBean == null) {
			FacesContextFactory facesContextFactory = new FacesContextFactoryImpl();
			FacesContext context =  facesContextFactory.getFacesContext
					   (req.getServletContext(), req, resp, new LifecycleImpl());
			goodsBean = (GoodsBean) context.getApplication().evaluateExpressionGet(context, "#{goodsBean}", GoodsBean.class);
		}
		
		List<WFGoods> list = goodsBean.getGoodsList();
		if (list != null) {
			boolean needFilter = filter == null || filter.isEmpty() ? false: true;
			int len = 1;
			Pattern  p = Pattern.compile("^(" + filter +")");
			for (WFGoods wf : list) {
				if (!needFilter || p.matcher(wf.getAbbr()).find() || p.matcher(wf.getName()).find()) {
					JSONObject o = new JSONObject();
					o.put("name", wf.getName());
					o.put("id", wf.getId());
					ret.put(o);
					len ++;
					if (len > 9) {
						break;
					}
				}
			}
		}
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.print(ret.toString());
		out.flush();
	}

	private void handleBrandFilter(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		JSONArray ret = new JSONArray();
		HttpSession session = req.getSession();
		GoodsBean goodsBean = (GoodsBean) session.getAttribute("goodsBean");

		String goods_id = req.getParameter("goods_id");
		String name = req.getParameter("name");
		IGoodsService service = ServiceFactory.getGoodsService();
		List<WFBrand> list = service.searchBrand(name, 10);
		for (WFBrand wf : list) {
			JSONObject o = new JSONObject();
			o.put("name", wf.getName());
			o.put("id", wf.getId());
			ret.put(o);
		}
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.print(ret.toString());
		out.flush();
	}
	
	
	private void handleVendorFilter(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		JSONArray ret = new JSONArray();
		HttpSession session = req.getSession();
		GoodsBean goodsBean = (GoodsBean) session.getAttribute("goodsBean");

		String goods_id = req.getParameter("goods_id");
		String name = req.getParameter("name");
		IGoodsService service = ServiceFactory.getGoodsService();
		List<WFVendor> list = service.searchVendor(name, 10);
		for (WFVendor wf : list) {
			JSONObject o = new JSONObject();
			o.put("name", wf.getName());
			o.put("id", wf.getId());
			ret.put(o);
		}
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.print(ret.toString());
		out.flush();
	}

	private void handleShopFilter(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		JSONArray ret = new JSONArray();
		IShopService service = ServiceFactory.getShopService();
		List<WFShop> list = service.getShopList();
		if (list != null) {
			for (WFShop wf : list) {
				JSONObject o = new JSONObject();
				o.put("name", wf.getName());
				o.put("id", wf.getId());
				ret.put(o);
			}
		}
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.print(ret.toString());
		out.flush();
	}
	
	
	private void handleCartAction(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("subaction");
		int ret = 0;
		int itemCount = 0;
		CartBean cartBean = (CartBean) req.getSession().getAttribute("cartBean");
		if (cartBean == null) {
			FacesContextFactory facesContextFactory = new FacesContextFactoryImpl();
			FacesContext context =  facesContextFactory.getFacesContext
					   (req.getServletContext(), req, resp, new LifecycleImpl());
			cartBean = (CartBean) context.getApplication().evaluateExpressionGet(context, "#{cartBean}", CartBean.class);
		}
		
		if ("putlist".equals(action)) {
			handlePutGoodsListtAction(req, resp, cartBean);
			return;
		}
		
		int count = 0;
		long gid = 0;
		
		
		try {
			count = Integer.parseInt(req.getParameter("goodscount"));
			gid = Long.parseLong(req.getParameter("goodsid"));
		} catch(Exception e) {
			ret = -1;
		}
		IGoodsService goodsService = ServiceFactory.getGoodsService();
		WFGoods g = goodsService.getGoods(gid);
		if (g == null) {
			ret = -1;
		}
		
		if (ret == 0 && "plus".equalsIgnoreCase(action)) {
			cartBean.getCart().addItemCount(g, count);
		} else if (ret == 0 && "minus".equals(action)) {
			cartBean.getCart().minusItemCount(g, count);
		} else {
			ret = -1;
		}
		
		itemCount = cartBean.getCart().getItems().size();
		JSONObject data = new JSONObject();
		data.put("ret", ret);
		data.put("itemcount", itemCount);
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.print(data.toString());
		out.flush();
	}
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private void handleChartAction(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		IShopService shopService = ServiceFactory.getShopService();
		String type = req.getParameter("type");
		String shopId = req.getParameter("shop");
		String date  = req.getParameter("date");
		if ("month".equals(req.getParameter("t"))) {
			handleChartActionForMonth(req, resp);
			return;
		}
		Date qd = null;
		if (date == null) {
			qd = new Date();
		} else {
			try {
				qd = format.parse(date);
			} catch (ParseException e) {
				qd = new Date();
			}
		}
		
		JSONArray data = new JSONArray();
		List<WFShop> list = null;
		if (shopId == null || shopId.isEmpty()) {
			list = shopService.getShopList();
		} else {
			list = new ArrayList<WFShop>(1);
			list.add(shopService.getShop(Long.parseLong(shopId)));
		}
		for (WFShop s : list) {
			if ("incoming".equals(type)) {
				WFIncoming incoming = shopService.queryShopIncoming(s, qd);
				if (incoming != null) {
					JSONObject obj = new JSONObject();
					obj.put("name", s.getName());
					JSONArray incomingdata = new JSONArray();
					incomingdata.put(incoming.getZls());
					incomingdata.put(incoming.getCash());
					incomingdata.put(incoming.getAli());
					incomingdata.put(incoming.getWeixin());
					incomingdata.put(incoming.getDazhong());
					incomingdata.put(incoming.getNuomi());
					obj.put("data", incomingdata);
					data.put(obj);
				}
			} else if ("cost".equals(type)){
				WFOperationCost cost = shopService.queryShopOperationCost(s, qd);
				if (cost != null) {
					JSONObject obj = new JSONObject();
					obj.put("name", s.getName());
					JSONArray costdata = new JSONArray();
					costdata.put(cost.getRytl());
					costdata.put(cost.getSb());
					costdata.put(cost.getBc());
					costdata.put(cost.getHsf());
					costdata.put(cost.getYl());
					costdata.put(cost.getDf());
					costdata.put(cost.getRqf());
					costdata.put(cost.getFf());
					costdata.put(cost.getGz());
					costdata.put(cost.getRz());
					costdata.put(cost.getQt());
					obj.put("data", costdata);
					data.put(obj);
				}
			}
		}
		
		
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.print(data.toString());
		out.flush();
		
	}
	
	private void handleChartActionForMonth(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		IShopService shopService = ServiceFactory.getShopService();
		String type = req.getParameter("type");
		String shopId = req.getParameter("shop");
		String date  = req.getParameter("date");
		int month = 0;
		try {
			month = Integer.parseInt(date);
		} catch(NumberFormatException e) {
			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			out.flush();
		}
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, month -1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		Date start = c.getTime();
		
		if (month == 2) {
			c.set(Calendar.DAY_OF_MONTH, 29);
		} else if (month == 1 || month == 3 || month == 5 || month == 7
				|| month == 8 || month == 10 || month == 12) {
			c.set(Calendar.DAY_OF_MONTH, 31);
		} else {
			c.set(Calendar.DAY_OF_MONTH, 30);
		}
		Date end = c.getTime();
		List<WFShop> list = null;
		if (shopId == null || shopId.isEmpty()) {
			list = shopService.getShopList();
		} else {
			list = new ArrayList<WFShop>(1);
			list.add(shopService.getShop(Long.parseLong(shopId)));
		}
		
		
		JSONArray data = new JSONArray();
		for (WFShop s : list) {
			if ("incoming".equals(type)) {
				List<WFIncoming> incomings = shopService.queryShopIncoming(s, start, end);
				WFIncoming incoming = new WFIncoming();
				for (WFIncoming wfi : incomings) {
					incoming.setZls(incoming.getZls() + wfi.getZls());
					incoming.setCash(incoming.getCash() + wfi.getCash());
					incoming.setAli(incoming.getAli() + wfi.getAli());
					incoming.setWeixin(incoming.getWeixin() + wfi.getWeixin());
					incoming.setDazhong(incoming.getDazhong() + wfi.getDazhong());
					incoming.setNuomi(incoming.getNuomi() + wfi.getNuomi());
				}
				JSONObject obj = new JSONObject();
				obj.put("name", s.getName());
				JSONArray incomingdata = new JSONArray();
				incomingdata.put(incoming.getZls());
				incomingdata.put(incoming.getCash());
				incomingdata.put(incoming.getAli());
				incomingdata.put(incoming.getWeixin());
				incomingdata.put(incoming.getDazhong());
				incomingdata.put(incoming.getNuomi());
				obj.put("data", incomingdata);
				data.put(obj);
			} else if ("cost".equals(type)){
				List<WFOperationCost> costs = shopService.queryShopOperationCost(s, start, end);
				WFOperationCost cost = new WFOperationCost();
				for (WFOperationCost co : costs) {
					cost.setRytl(cost.getRytl() + co.getRytl());
					cost.setSb(cost.getSb() + co.getSb());
					cost.setBc(cost.getBc() + co.getBc());
					cost.setHsf(cost.getHsf() + co.getHsf());
					cost.setYl(cost.getYl() + co.getYl());
					cost.setDf(cost.getDf() + co.getDf());
					cost.setDf(cost.getRqf() + co.getRqf());
					cost.setFf(cost.getFf() + co.getFf());
					cost.setGz(cost.getGz() + co.getGz());
					cost.setRz(cost.getRz() + co.getRz());
					cost.setQt(cost.getQt() + co.getQt());
				}
				JSONObject obj = new JSONObject();
				obj.put("name", s.getName());
				JSONArray costdata = new JSONArray();
				costdata.put(cost.getRytl());
				costdata.put(cost.getSb());
				costdata.put(cost.getBc());
				costdata.put(cost.getHsf());
				costdata.put(cost.getYl());
				costdata.put(cost.getDf());
				costdata.put(cost.getRqf());
				costdata.put(cost.getFf());
				costdata.put(cost.getGz());
				costdata.put(cost.getRz());
				costdata.put(cost.getQt());
				obj.put("data", costdata);
				data.put(obj);
			}
		}
		
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.print(data.toString());
		out.flush();
		
	}
	
	
	
	private void handlePutGoodsListtAction(HttpServletRequest req,
			HttpServletResponse resp, CartBean bean) throws ServletException, IOException {
		String[] ids = req.getParameterValues("ids");
		String[] counts = req.getParameterValues("itemcounts");
		
		
		IGoodsService goodsService = ServiceFactory.getGoodsService();
		Long gid;
		Float c;
		for (int i = 0; i < ids.length; i++) {
			String id =ids[i];
			if (i >= counts.length) {
				continue;
			}
			String sc = counts[i];
			try {
				gid = Long.parseLong(id);
				c = Float.parseFloat(sc);
			} catch(NumberFormatException e) {
				continue;
			}
			WFGoods g = goodsService.getGoods(gid);
			if (g == null) {
				continue;
			}
			
			bean.getCart().addItemCount(g, c);
		}
		
		JSONObject data = new JSONObject();
		data.put("ret", 0);
		data.put("itemcount", bean.getCart().getItems().size());
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.print(data.toString());
		out.flush();
	}
	
	private void handleArtifactSearchAction(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		IGoodsService goodsService = ServiceFactory.getGoodsService();
		List<WFArtifact> list = null;
		if (name == null || name.isEmpty()) {
			List<WFArtifact> l = goodsService.loadArtifacts();
			if (l == null ) {
				return;
			} 
			int len = l.size() > 10 ? 10 : l.size();
			list = goodsService.loadArtifacts().subList(0, len);
		} else {
			list = goodsService.searchWFArtifactGoods(name);
			
		}
		JSONArray data = new JSONArray();
		for (WFArtifact wfa : list) {
			for (StaffGoods sg : wfa.getTargetGoods()) {
				JSONObject o = new JSONObject();
				//FIXME should update structure for support mulit-goods query
				o.put("name", sg.wfg.getName());
				o.put("style", sg.style);
				o.put("un", sg.unit);
				o.put("mn", sg.minialProduceUnit);
				o.put("id", wfa.getId());
				JSONArray jsa = new JSONArray();
				JSONObject source = null;
				for (StaffGoods sf : wfa.getStaffGoods()) {
					source = new JSONObject();
					source.put("name", sf.getWfg().getName());
					source.put("sv", sf.getUnit());
					source.put("ss", sf.getStyle());
					jsa.put(source);
				}
				o.put("source", jsa);
				data.put(o);
			}
		}

		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.print(data.toString());
		out.flush();
	}
	
	
	private void handleStatistAction(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		IInventoryService iis = ServiceFactory.getInventoryService();
		JSONObject ret = new JSONObject();
		double inventorycost = iis.queryCurrentInventoryCost();
		ret.put("ic", inventorycost);
		double totalIncoming = ServiceFactory.getShopService().queryTotalIncoming();
		ret.put("si", totalIncoming);
		double operationCost = ServiceFactory.getShopService().queryTotalOperationCost();
		ret.put("sc", operationCost);
		
		logger.info("==> " + ret.toString());
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.print(ret.toString());
		out.flush();
	}

}

