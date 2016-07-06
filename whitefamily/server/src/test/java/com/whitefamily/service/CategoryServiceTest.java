package com.whitefamily.service;


import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.whitefamily.service.vo.WFCategory;

public class CategoryServiceTest extends TestCase {
	
	ICategoryService service;

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		service = ServiceFactory.getCategoryService();
	}

	@Test
	public void testAddCategory() {
		WFCategory wf = new WFCategory();
		wf.setName("sss");
		service.addCategory(wf);
		assertTrue(wf.getId() > 0);
	}

	@Test
	public void testRemoveCategory() {
		WFCategory wf = new WFCategory();
		wf.setName("sss");
		service.addCategory(wf);
		assertTrue(wf.getId() > 0);
		service.removeCategory(wf);
		
	}

	@Test
	public void testUpdateCategory() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateCategoryParent() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTopCategory() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSortedCategory() {
		WFCategory wf = new WFCategory();
		wf.setName("s");
		service.addCategory(wf);
		assertTrue(wf.getId() > 0);
		
		WFCategory wf1 = new WFCategory();
		wf1.setName("s1");
		wf1.setParent(wf);
		service.addCategory(wf1);
		assertTrue(wf1.getId() > 0);
		
		
		WFCategory wf2 = new WFCategory();
		wf2.setName("s12");
		wf2.setParent(wf1);
		service.addCategory(wf2);
		assertTrue(wf2.getId() > 0);
		
		
		WFCategory wf3 = new WFCategory();
		wf3.setName("a");
		service.addCategory(wf3);
		assertTrue(wf3.getId() > 0);
		
		List<WFCategory> list = service.getSortedCategory();
		assertEquals(list.size(), 2);
		assertEquals(list.iterator().next().getSubs().size(), 1);
		assertEquals(list.iterator().next().getSubs().iterator().next().getSubs().size(), 1);
	}

	@Test
	public void testAddCategoryBrand() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveCategoryBrand() {
		fail("Not yet implemented");
	}

}
