package com.github.mituba.crawler;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

public class SearcherTest{
	@Test
	public void testSearch(){
		assertNotNull(new Searcher(null, null, null)
				.getProductURL(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
	}
}