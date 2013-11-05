package com.quesity.models.tests;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import android.content.res.AssetFileDescriptor;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.models.QuestPage;
import com.quesity.models.tests.utils.JSONReader;

import junit.framework.TestCase;

public class QuestModelJSONTester extends InstrumentationTestCase{
	private Gson gson;
	private String _json="";
	protected void setUp() throws Exception{

		InputStream stream = getInstrumentation().getContext().getResources().getAssets().open("json/quests.json");
		_json = JSONReader.getJSONString(stream);
	}
	public void testQuestModel() throws Exception {
		Quest[] quests = ModelsFactory.getInstance().getQuestsFromJson(_json);
		assertEquals(4, quests.length);
		assertEquals(quests[3].getTitle(), "test quest");
		assertEquals(quests[0].getTitle(), "Untitled Quest");
		assertTrue(quests[0].getPages().size() > 0);
		
		QuestPage page_1 = quests[0].getPages().get(0);
		assertEquals(1,page_1.getPageNumber());
		assertEquals("Untitled",page_1.getPageName());
	}

}
