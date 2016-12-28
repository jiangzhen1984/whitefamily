package com.whitefamily.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
import com.whitefamily.service.vo.WFDelivery;
import com.whitefamily.service.vo.WFIncoming;
import com.whitefamily.service.vo.WFOperationCost;
import com.whitefamily.service.vo.WFDelivery.Item;

public class WFPdfExporter implements WFExporter {
	
	
	private static Log logger = LogFactory.getLog(WFPdfExporter.class);

	@Override
	public File export(WFDelivery wf) {
		if (wf == null) {
			throw new NullPointerException("wf is null ");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String rootDir = ServerConstants.getInstance().getDeliveryFormPath()
				+ File.separator + sdf.format(wf.getDatetime())
				+ File.separator;
		String fileName =  "shop_" + wf.getShop().getId() + "_ir_"
				+ wf.getId()+".pdf";
		File dir = new File(rootDir);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new RuntimeException(" can not make dir:"+ rootDir);
			}
			dir.setWritable(true, false);
			dir.setReadable(true, false);
			
			 Runtime rt = Runtime.getRuntime();
		        try {
					rt.exec("chmod 777 " + dir.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
		        logger.info(" Update dir permisson:" + dir.getAbsolutePath());
		}
		
		File file = new File(rootDir + fileName);
		
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
       
	
        Font chapterFont = new Font(bf, 14);
        Chunk chunk = new Chunk("", chapterFont);
        Chapter chapter = new Chapter(new Paragraph(chunk), 0);
        chapter.setNumberDepth(0);
        Paragraph title = new Paragraph("出货单 \n", chapterFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        chapter.add(title);
        chapter.add(new Paragraph("店铺： "+wf.getShop().getName()+" \n", chapterFont));
        chapter.add(new Paragraph("日期： "+sdf1.format(wf.getDatetime())+"\n", chapterFont));
        chapter.add(new Paragraph("地址： "+wf.getShop().getAddress()+ " \n\n", chapterFont));
        
        Font chapterFont1 = new Font(bf, 11);
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
        
        
        List<Item> list = wf.getItemList();
        int count = list.size();
        double sum = 0;
        for(int aw = 0; aw < count; aw++){
        	WFDelivery.Item  item = list.get(aw);
        	table.addCell(new PdfPCell( new Phrase((aw + 1)+"", chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getGoods().getName(), chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getGoods().getCate().getName(), chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getGoods().getUnit() , chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getRealCount()+"", chapterFont1)));
        	table.addCell( new PdfPCell( new Phrase(item.getPrice()+"", chapterFont1)));
        	table.addCell( new PdfPCell( new Phrase(String.format("%.2f", item.getPrice() * item.getRealCount()) , chapterFont1)));
        	sum += (item.getPrice() * item.getRealCount());
        	logger.info(item.getGoods().getName()+"  : " + item.getPrice() + "  "  +item.getRealCount() +"  sum:" + sum);
        } 
       
        
        chapter.add(table);
        logger.info( String.format("%.2f", sum));
        PdfPTable table1 = new PdfPTable(1);
        table1.addCell(new Phrase("总计： " + String.format("%.2f", sum), chapterFont1));
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
        

        Runtime rt = Runtime.getRuntime();
        try {
			rt.exec("chmod 777 " + file.getAbsolutePath());
			 logger.info(" Update file permisson:" + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
        return file;
	}
	
	
	public File export(WFIncoming incoming, WFOperationCost monthlyCost, WFOperationCost other) {
		return null;
	}

}
