package com.whitefamily.web.bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.faces.context.FacesContextFactoryImpl;
import com.sun.faces.lifecycle.LifecycleImpl;
import com.whitefamily.service.IGoodsService;
import com.whitefamily.service.IShopService;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFBrand;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFShop;

public class AJAXHandler extends HttpServlet {

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
		} else if ("filterShop".equals(action)) {
			handleShopFilter(req, resp);
		} else if ("cartAction".equals(action)) {
			handleCartAction(req, resp);
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
		List<WFCategory> list = cartBean.getCategoryList();
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
		IGoodsService service = ServiceFactory.getGoodsService();
		if (goods_id == null || goods_id.isEmpty()) {
			return;
		}
		WFGoods g = service.getGoods(Long.parseLong(goods_id));

		if (goodsBean == null) {
		} else {
			List<WFBrand> list = null;
			if (g.isBrandsLoad()) {
				list = g.getBrands();
			} else {
				list = service.queryBrands(g, 0, 50);
			}
			if (list != null) {
				for (WFBrand wf : list) {
					JSONObject o = new JSONObject();
					o.put("name", wf.getName());
					o.put("id", wf.getId());
					ret.put(o);
				}
			}
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
			cartBean.getCart().addItem(g, count);
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

}
