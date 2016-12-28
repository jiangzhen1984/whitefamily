package com.whitefamily.exporter;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.whitefamily.service.vo.WFDelivery;
import com.whitefamily.service.vo.WFIncoming;
import com.whitefamily.service.vo.WFOperationCost;

public class WFExcelExporter implements WFExporter {

	private static Log logger = LogFactory.getLog(WFExcelExporter.class);
	
	public WFExcelExporter() {
		//TODO load export template
		logger.info(" Start to load template ");
	}
	
	@Override
	public File export(WFDelivery wf) {
		if (wf == null) {
			throw new NullPointerException("wf is null ");
		}
		//TODO read from template
		
		//TODO update template
		
		return null;
	}

	
	public File export(WFIncoming incoming, WFOperationCost monthlyCost, WFOperationCost other) {
		return null;
	}
}
