package com.whitefamily.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.whitefamily.ServerConstants;
import com.whitefamily.po.DamageReportGoods;
import com.whitefamily.po.DamageReportRecord;
import com.whitefamily.po.DamageStatus;
import com.whitefamily.po.InventoryRequestGoods;
import com.whitefamily.po.InventoryRequestRecord;
import com.whitefamily.po.InventoryStatus;
import com.whitefamily.po.Shop;
import com.whitefamily.po.delivery.DeliverySupplierConfiguration;
import com.whitefamily.po.incoming.Delivery;
import com.whitefamily.po.incoming.GroupOn;
import com.whitefamily.po.incoming.Incoming;
import com.whitefamily.service.InventoryService.LocalMappingSubRecord;
import com.whitefamily.service.vo.WFDamageReport;
import com.whitefamily.service.vo.WFIncoming;
import com.whitefamily.service.vo.WFIncoming.DeliveryItem;
import com.whitefamily.service.vo.WFIncoming.GroupOnItem;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFManager;
import com.whitefamily.service.vo.WFShop;
import com.whitefamily.service.vo.WFSupplierMapping;
import com.whitefamily.service.vo.WFUser;

public class ShopService extends BaseService implements IShopService {

	private static boolean cachedShop;
	private static Map<Long, WFShop> shopCache;
	

	private IGoodsService goodsService;
	private IUserService userService;
	private ISupplierService supplierService;
	

	public IGoodsService getGoodsService() {
		return goodsService;
	}

	public void setGoodsService(IGoodsService goodsService) {
		this.goodsService = goodsService;
	}

	
	public ISupplierService getSupplierService() {
		return supplierService;
	}

	public void setSupplierService(ISupplierService supplierService) {
		this.supplierService = supplierService;
	}
	
	
	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public ShopService() {
		super();
		shopCache = new HashMap<Long, WFShop>();
	}

	@Override
	public WFShop getShop(long id) {
		WFShop wf = shopCache.get(Long.valueOf(id));
		if (wf == null) {
			Session sess = getSession();
			Shop s  = (Shop)sess.get(Shop.class, id);
			if (s != null) {
				wf = new WFShop(s);
				shopCache.put(id, wf);
			}
		}
		
		//Do not close session, let proxy to close session
		
		return wf;
	}
	
	@Override
	public WFShop addShop(WFShop shop) {
		Shop s = new Shop();
		s.setName(shop.getName());
		s.setAddress(shop.getAddress());
		
		Session sess = openSession();
		Transaction tr = sess.beginTransaction();
		sess.save(s);
		tr.commit();
		sess.close();
		shop.setId(s.getId());
		shopCache.put(s.getId(), shop);
		return shop;
	}
	
	public WFShop updateShop(WFShop shop) {
		if (shop == null) {
			throw new NullPointerException(" shop is null");
		}
		Session sess = getSession();
		Shop s = (Shop)sess.get(Shop.class, shop.getId());
		if (s == null) {
			sess.close();
			throw new RuntimeException(" no such shop:" + shop.getId());
		}
		
		s.setName(shop.getName());
		s.setAddress(shop.getAddress());
		Transaction tr = sess.beginTransaction();
		sess.update(s);
		tr.commit();
		sess.close();
		return shop;
	}

	@Override
	public void removeShop(WFShop shop) {
		Session sess = getSession();
		Shop s = (Shop)sess.get(Shop.class, shop.getId());
		Transaction tr = sess.beginTransaction();
		sess.delete(s);
		tr.commit();
		shopCache.remove(s.getId());
	}
	
	
	public WFShop getShop(WFManager wfm) {
		WFShop wfs = new WFShop();
		wfs.setId(wfm.getShopId());
		wfs.setName(wfm.getShopName());
		wfs.setAddress(wfm.getShopAddress());
		return wfs;
	}


	
	public List<WFShop> getShopList() {
		if (cachedShop) {
			return new ArrayList<WFShop>(shopCache.values());
		}
		Session sess = openSession();
		Query query = sess.createQuery(" from Shop ");
		List<Shop> shopList = query.list();
		List<WFShop> newShopList = new ArrayList<WFShop>(shopList.size());
		for (Shop s : shopList) {
			WFShop wfs = new WFShop(s);
			shopCache.put(s.getId(), wfs);
			newShopList.add(wfs);
		}
		cachedShop = true;
		return newShopList;
	}
	
	
	
	public List<WFDamageReport> queryDamageReport(int start, int count) {
		return queryDamageReport(start, count, null, null);
	}
	
	public List<WFDamageReport> queryDamageReport(int start, int count, DamageStatus ds) {
		return queryDamageReport(start, count, null, ds);
	}
	
	public List<WFDamageReport> queryDamageReport(int start, int count, WFShop shop) {
		return queryDamageReport(start, count, shop, null);
	}
	
	public List<WFDamageReport> queryDamageReport(int start, int count, WFShop shop,  DamageStatus ds) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" from DamageReportRecord ");
		if (shop != null && ds != null) {
			buffer.append(" where 1  ");
		}
		
		if (shop != null) {
			buffer.append(" and shop.id =? ");
		}
		if (ds != null) {
			buffer.append(" and status =? ");
		}
		
		Session sess = getSession();
		Query query = sess.createQuery(buffer.toString());
		if (shop != null && ds != null) {
			query.setLong(0, shop.getId());
			query.setLong(1, ds.ordinal());
		} else if (shop != null) {
			query.setLong(0, shop.getId());
		} else if (ds != null) {
			query.setLong(0, ds.ordinal());
		}
		
		List<DamageReportRecord> rlist = query.list();
		List<WFDamageReport> list = new ArrayList<WFDamageReport>(rlist.size());
		for (DamageReportRecord r : rlist) {
			WFDamageReport report = new WFDamageReport();
			report.setId(r.getId());
			report.setDatetime(r.getDatetime());
			report.setIs(r.getStatus());
			report.setOperator(userService.getUser(r.getOperator().getId()));
			report.setShop(getShop(r.getShop().getId()));
			list.add(report);
		}
		
		sess.close();
		return list;
		
	}
	
	public void queryDamageReportDetail(WFDamageReport wr) {
		Session sess = getSession();
		Query query = sess
				.createQuery(" from DamageReportGoods  where record.id = ? ");
		query.setLong(0, wr.getId());
		List<DamageReportGoods> list = query.list();
		for (DamageReportGoods iur : list) {
			wr.addDamageItem(goodsService.getGoods(iur.getGoods().getId()), iur.getCount(), true);
		}
		sess.close();
	}


	public Result reportDamage(WFDamageReport report, WFShop shop, WFUser manager) {
		DamageReportRecord record = new DamageReportRecord();
		record.setDatetime(report.getDatetime());
		record.setOperator(manager);
		record.setStatus(DamageStatus.REPORTED);
		record.setShop(shop);

		Session sess = getSession();
		Transaction tr = sess.beginTransaction();
		sess.save(record);
		sess.flush();
		int count = report.getItemCount();
		DamageReportGoods ig = null;
		for (int i = 0; i < count; i++) {
			WFDamageReport.Item wi = report.getItem(i);
			if (wi.isPersisted()) {
				continue;
			}
			ig = new DamageReportGoods();
			ig.setGoods(goodsService.getGoods(wi.getGoods().getId()));
			ig.setCount(wi.getCount());
			ig.setRecord(record);
			sess.save(ig);
			wi.setPersisted(true);
		}

		tr.commit();
		sess.close();
		return Result.SUCCESS;
	}
	
	
	
	public void reportIncoming(WFShop shop, WFIncoming incoming, WFUser manager) {
		Session sess = getSession();
		Incoming inco = new Incoming();
		inco.setDate(incoming.getDate());
		inco.setCash(incoming.getCash());
		inco.setAli(incoming.getAli());
		inco.setDazhong(incoming.getDazhong());
		inco.setDelivery(incoming.getDelivery());
		inco.setNuomi(incoming.getNuomi());
		inco.setWeixin(incoming.getWeixin());
		inco.setOther(incoming.getOther());
		inco.setShop(shop);
		Transaction tr = sess.beginTransaction();
		sess.save(inco);
		
		List<DeliveryItem> deliList = incoming.getDelis();
		Delivery d = null;
		for (DeliveryItem item : deliList) {
			d = new Delivery();
			d.setShop(shop);
			d.setDate(incoming.getDate());
			d.setDeliveryFee(item.getDeliveryFee());
			d.setDeliveryType(item.getDeliveryType());
			d.setIncoming(item.getIncoming());
			d.setOnlinePayment(item.getOnlinePayment());
			d.setRefund(item.getRefund());
			d.setRefund1(item.getRefund1());
			d.setServiceFee(item.getServiceFee());
			d.setValid(item.getValid());
			sess.save(d);
		}
		
		List<GroupOnItem> groups = incoming.getGroups();
		GroupOn go = null;
		for (GroupOnItem item : groups) {
			go = new GroupOn();
			go.setDate(incoming.getDate());
			go.setShop(shop);
			go.setCe(item.getCe());
			go.setCount(item.getCount());
			go.setDjq(item.getDjq());
			go.setGme(item.getGme());
			go.setGroupType(item.getGroupType());
			go.setOther(item.getOther());
			go.setSje(item.getSje());
			go.setDesc(item.getDesc());
			sess.save(go);
		}
		tr.commit();
		sess.flush();
		sess.close();
		
	}
	
	
	public Result requestInventory(WFInventoryRequest inventory, WFShop shop, WFUser manager) {
		InventoryRequestRecord record = new InventoryRequestRecord();
		record.setDatetime(inventory.getDatetime());
		record.setOperator(manager);
		record.setStatus(InventoryStatus.REQUEST);
		record.setShop(shop);
		
		

		Session sess = getSession();
		
		List<WFSupplierMapping> mappingList = supplierService.getMappingList();
		Map<WFSupplierMapping, LocalMappingSubRecord> mapping = new HashMap<WFSupplierMapping, LocalMappingSubRecord>();
		
		Transaction tr = sess.beginTransaction();
		sess.save(record);
		sess.flush();
		int count = inventory.getItemCount();
		InventoryRequestGoods ig = null;
		for (int i = 0; i < count; i++) {
			WFInventoryRequest.Item wi = inventory.getItem(i);
			if (wi.isPersisted()) {
				continue;
			}
			
			WFGoods wfg = goodsService.getGoods(wi.getGoods().getId());
			//check 
			handleSubRecordsGoods(record, mapping, wfg, mappingList,wi);
			
			
			ig = new InventoryRequestGoods();
			ig.setGoods(wfg);
			ig.setCount(wi.getCount());
			ig.setRecord(record);
			sess.save(ig);
			wi.setPersisted(true);
		}
		
		for (LocalMappingSubRecord lmsr : mapping.values()) {
			saveSubRecord(lmsr);
		}

		tr.commit();
		sess.close();
		return Result.SUCCESS;
	}
	
	
	
	private void handleSubRecordsGoods(InventoryRequestRecord parent, Map<WFSupplierMapping, LocalMappingSubRecord> map, WFGoods wfg, List<WFSupplierMapping> ml, WFInventoryRequest.Item item) {
		for (WFSupplierMapping wfs : ml) {
			if (wfg.getId() == wfs.getMappingId() && wfs.getMc() == DeliverySupplierConfiguration.MC.GOODS) {
				LocalMappingSubRecord lmsr = map.get(wfs);
				if (lmsr == null) {
					lmsr = new LocalMappingSubRecord();
					lmsr.parent = parent;
					map.put(wfs, lmsr);
				}
				lmsr.addWFGoods(wfg, item);
			} else if (wfs.getMc() == DeliverySupplierConfiguration.MC.CATE) {
				WFCategory wf = (WFCategory)wfg.getCate();
				while (wf != null) {
					if (wf.getId() == wfs.getMappingId()) {
						LocalMappingSubRecord lmsr = map.get(wfs);
						if (lmsr == null) {
							lmsr = new LocalMappingSubRecord();
							lmsr.parent = parent;
							map.put(wfs, lmsr);
						}
						lmsr.addWFGoods(wfg, item);
						break;
					} 
					
					wf = wf.getParent();
				}
			}
		}
	}
	
	
	private void saveSubRecord(LocalMappingSubRecord subRecord) {
		Session sess = getSession();
		
		InventoryRequestRecord record = new InventoryRequestRecord();
		record.setDatetime(subRecord.parent.getDatetime());
		record.setOperator(subRecord.parent.getOperator());
		record.setShop(subRecord.parent.getShop());
		record.setParent(subRecord.parent);
		sess.save(record);
		sess.flush();
		
		int len = subRecord.items.size();
		for (int i = 0; i < len; i++) {
			WFGoods wfg = subRecord.goodsList.get(i);
			WFInventoryRequest.Item wi = subRecord.items.get(i);
			
			InventoryRequestGoods ig = new InventoryRequestGoods();
			ig = new InventoryRequestGoods();
			ig.setGoods(wfg);
			ig.setCount(wi.getCount());
			ig.setRecord(record);
			sess.save(ig);
		}
	}

	
	final static class LocalMappingSubRecord {
		InventoryRequestRecord parent;
		WFSupplierMapping wfm;
		List<WFInventoryRequest.Item> items;
		List<WFGoods> goodsList;
		
		public void addWFGoods(WFGoods wfg, WFInventoryRequest.Item item) {
			if (goodsList == null || items == null) {
				goodsList = new ArrayList<WFGoods>(20);
				items = new ArrayList<WFInventoryRequest.Item>(20);
			}
			goodsList.add(wfg);
			items.add(item);
		}
	}
	
	
	
	public WFIncoming queryShopIncoming(WFShop shop, Date date) {
		WFIncoming wf = null;
		Session sess = getSession();
		Query query = sess.createQuery(" from Incoming where shop.id = ? and date = ? ");
		query.setLong(0, shop.getId());
		query.setDate(1, date);
		List<Incoming> inList = (List<Incoming>)query.list();
		if (inList.size() > 0) {
			 wf = new WFIncoming();
			Incoming in = inList.get(0);
			wf.setCash(in.getCash());
			wf.setAli(in.getAli());
			wf.setDate(date);
			wf.setDazhong(in.getDazhong());
			wf.setNuomi(in.getNuomi());
			wf.setOther(in.getOther());
			wf.setWeixin(in.getWeixin());
			wf.setDesc(in.getDesc());
		}
		
		query = sess
				.createQuery(" from GroupOn where shop.id = ? and date = ? ");
		query.setLong(0, shop.getId());
		query.setDate(1, date);
		List<GroupOn> gList = (List<GroupOn>) query.list();
		if (gList.size() > 0  && wf == null) {
			 wf = new WFIncoming();
		}
		for (GroupOn g : gList) {
			wf.addShopOnItem(g.getGroupType(), g.getCount(), g.getDjq(),
					g.getGme(), g.getCe(), g.getSje(), g.getOther());
		}

		query = sess
				.createQuery(" from Delivery where shop.id = ? and date = ? ");
		query.setLong(0, shop.getId());
		query.setDate(1, date);
		List<Delivery> dList = (List<Delivery>) query.list();
		if (dList.size() > 0  && wf == null) {
			 wf = new WFIncoming();
		}
		for (Delivery d : dList) {
			wf.addDeliveryData(d.getDeliveryType(), d.getIncoming(),
					d.getOnlinePayment(), d.getRefund(), d.getRefund1(),
					d.getServiceFee(), d.getDeliveryFee(), d.getValid());
		}
		sess.close();
		return wf;
	}
	
	
	
	public List<WFInventoryRequest> queryInventoryRequestList(int start, int count) {
		return queryInventoryRequestList(null, null, null, false, start, count);
	}
	
	public List<WFInventoryRequest> queryInventoryRequestList(WFShop shop, Date date) {
		return queryInventoryRequestList(shop, date, null, true, 0, 30);
	}
	
	public List<WFInventoryRequest> queryInventoryRequestList(WFShop shop, Date start, Date end) {
		return queryInventoryRequestList(shop, start, end, true, 0, 30);
	}
	
	public List<WFInventoryRequest> queryInventoryRequestList(WFShop shop) {
		return queryInventoryRequestList(shop, null, null, false, 0, 30);
	}
	
	public List<WFInventoryRequest> queryInventoryRequestList(Date date) {
		return queryInventoryRequestList(null, date, null, false, 0, 30);
	}
	
	public List<WFInventoryRequest> queryInventoryRequestList(Date start, Date end) {
		return queryInventoryRequestList(null, start, end, true, 0, 30);
	}
	
	
	
	public List<WFInventoryRequest> queryInventoryRequestList(WFShop shop, Date start, Date end, boolean specificDate, int offset, int count) {
		
		StringBuffer hqlBuf = new StringBuffer();
		hqlBuf.append(" from InventoryRequestRecord where 1 = 1 ");
		if (shop != null) {
			hqlBuf.append(" and shopId = ? ");
		}
		
		if (specificDate) {
			if (start != null || end != null) {
				hqlBuf.append(" and datetime = ?");
			} 
		} else {
			if (start != null && end != null) {
				hqlBuf.append(" and datetime >= ? and datetime <= ?");
			} else if (start != null) {
				hqlBuf.append(" and datetime >= ? ");
			}  else if (end != null) {
				hqlBuf.append(" and datetime <= ? ");
			}
		}
		
		hqlBuf.append(" order by datetime desc ");
		
		Session sess = getSession();
		Query query = sess.createQuery(hqlBuf.toString());
		int idx = 0;
		if (shop != null) {
			query.setLong(idx++, shop.getId());
		}
		
		if (specificDate) {
			if (start != null ) {
				query.setDate(idx++, start);
			} else if (end != null) {
				query.setDate(idx++, end);
			} 
		} else {
			if (start != null && end != null) {
				query.setDate(idx++, start);
				query.setDate(idx++, end);
			} else if (start != null) {
				query.setDate(idx++, start);
			}  else if (end != null) {
				query.setDate(idx++, end);
			}
		}
		
		List<InventoryRequestRecord>  list = query.list();
		List<WFInventoryRequest> wflist = new ArrayList<WFInventoryRequest>(list.size());
		for (InventoryRequestRecord irq : list) {
			WFInventoryRequest wf = new WFInventoryRequest();
			wf.setId(irq.getId());
			wf.setDatetime(irq.getDatetime());
			wf.setOperator(irq.getOperator());
			wf.setIs(irq.getStatus());
			wf.setShop(new WFShop(irq.getShop()));
			wflist.add(wf);
		}
		
		sess.close();
		return wflist;
	}
	
	
	public Result queryInventoryRequestGoods(WFInventoryRequest ir) {
		if (ir == null) {
			return Result.ERR_GENERIC_FAILED;
		}
		Session sess = getSession();
		Query query = sess.createQuery(" from InventoryRequestGoods where record.id = ? ");
		query.setLong(0, ir.getId());
		List<InventoryRequestGoods>  goodsList = query.list();
		for (InventoryRequestGoods g : goodsList) {
			ir.addInventoryItem(goodsService.getGoods(g.getGoods().getId()), g.getCount(), true);
		}
		ir.setLoadItem(true);
		return Result.SUCCESS;
	}
	
	
	public File generateDeliveryForm(WFInventoryRequest ir) {
		if (ir == null) {
			throw new NullPointerException("ir is null ");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String rootDir = ServerConstants.getInstance().getDeliveryFormPath()
				+ File.separator + sdf.format(ir.getDatetime())
				+ File.separator;
		String fileName =  "shop_" + ir.getShop().getId() + "_ir_"
				+ ir.getId()+".pdf";
		File dir = new File(rootDir);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new RuntimeException(" can not make dir:"+ rootDir);
			}
		}
		
		File file = new File(rootDir + fileName);
		if (file.exists()) {
			return file;
		}
		
		
		Document document = new Document();
		BaseFont bf;
        try {
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
		    bf= BaseFont.createFont("hxb-meixinti.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
       
	
        Font chapterFont = new Font(bf, 16);
        Chunk chunk = new Chunk("", chapterFont);
        Chapter chapter = new Chapter(new Paragraph(chunk), 0);
        chapter.setNumberDepth(0);
        Paragraph title = new Paragraph("出货单 \n", chapterFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        chapter.add(title);
        chapter.add(new Paragraph("店铺： "+ir.getShop().getName()+" \n", chapterFont));
        chapter.add(new Paragraph("日期： "+sdf1.format(ir.getDatetime())+"\n", chapterFont));
        chapter.add(new Paragraph("地址： "+ir.getShop().getAddress()+ " \n\n", chapterFont));
        
        Font chapterFont1 = new Font(bf, 13);
        Paragraph detail = new Paragraph("产品明细 \n\n", chapterFont1);
        detail.setAlignment(Paragraph.ALIGN_CENTER);
        chapter.add(detail);
        
        
        PdfPTable table = new PdfPTable(7);
        table.addCell( new PdfPCell( new Phrase("序号", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("品名", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("品类", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("规格", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("数量", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("单价", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("小计", chapterFont1)));
        
        
        int count = ir.getItemCount();
        double sum = 0;
        for(int aw = 0; aw < count; aw++){
        	WFInventoryRequest.Item  item = ir.getItem(aw);
        	table.addCell(new PdfPCell( new Phrase((aw)+"", chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getGoods().getName(), chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getGoods().getCate().getName(), chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getGoods().getUnit() , chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getCount()+"", chapterFont1)));
        	table.addCell( new PdfPCell( new Phrase("23.4", chapterFont1)));
        	table.addCell( new PdfPCell( new Phrase("", chapterFont1)));
        	sum += item.getCount();
        }   
       
        
        chapter.add(table);
        
        PdfPTable table1 = new PdfPTable(1);
        table1.addCell(new Phrase("总计： " + sum, chapterFont1));
        chapter.add(table1);
        
        Chapter chapterbottom = new Chapter(new Paragraph(""), 0);
        chapterbottom.setNumberDepth(0);
        Paragraph p1 = new Paragraph("出单人：__________"   +" 配送人:___________" +"店长：______________", chapterFont);
        p1.setAlignment(Paragraph.ALIGN_CENTER);
        chapter.add(p1);
        
        try {
			document.add(chapter);
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			 document.close();
		}
        
        return file;
       
	}
}
