package com.whitefamily.exporter;

import java.io.File;

import com.whitefamily.service.vo.WFDelivery;
import com.whitefamily.service.vo.WFIncoming;
import com.whitefamily.service.vo.WFOperationCost;

public interface WFExporter {

	
	public File export(WFDelivery wf);
	
	
	
	public File export(WFIncoming incoming, WFOperationCost monthlyCost, WFOperationCost other);
}
