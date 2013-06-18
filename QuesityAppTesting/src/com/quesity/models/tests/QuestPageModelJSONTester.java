package com.quesity.models.tests;

import java.io.InputStream;

import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageLink;
import com.quesity.models.QuestPageLocationLink;
import com.quesity.models.QuestPageQuestionLink;
import com.quesity.models.QuestPageStall;
import com.quesity.models.tests.utils.JSONReader;

import android.test.InstrumentationTestCase;

public class QuestPageModelJSONTester extends InstrumentationTestCase{
	private String _json;
	private QuestPage[] _quest_pages;
	protected void setUp() throws Exception{

		InputStream stream = getInstrumentation().getContext().getResources().getAssets().open("json/quest_pages.json");
		_json = JSONReader.getJSONString(stream);
		_quest_pages = ModelsFactory.getInstance().getQuestPageArrayFromJson(_json);
	}
	
	public void testQuestPageAnswersOnly() throws Exception  {
		QuestPage answers = _quest_pages[0];
		assertEquals(answers.getId(),"51b070bc809e54e318000004");
		assertEquals(answers.getPageContent(),"<p>ABCD ???&nbsp;</p>\n<p>&nbsp;</p>");
		assertEquals(answers.getPageName(),"Untitled");
		assertEquals(answers.getPageNumber(),1);
		assertEquals(answers.getPageType(),"question");
		assertEquals(answers.getLinks().length,2);
		
	}
	
	public void testQuestPageAnswersOnlyLinks() throws Exception {
		QuestPage answers = _quest_pages[0];
		
		QuestPageLink[] links = answers.getLinks();
		assertTrue(links[0] instanceof QuestPageQuestionLink );
		assertTrue(links[1] instanceof QuestPageQuestionLink );
		
		QuestPageQuestionLink link_1 = (QuestPageQuestionLink) links[0];
		assertEquals(link_1.getId(),"51b070cf809e54e318000009");
		assertEquals(link_1.getAnswerTxt(),"1");
		assertEquals(link_1.getLinksToPage(),"51b070bd809e54e318000005");
		
		QuestPageQuestionLink link_2 = (QuestPageQuestionLink) links[1];
		assertEquals(link_2.getId(),"51b070d3809e54e31800000a");
		assertEquals(link_2.getAnswerTxt(),"2");
		assertEquals(link_2.getLinksToPage(),"51b070be809e54e318000006");
		
	}
	
	public void testQuestPageLocation() throws Exception { 
		QuestPage location_page = _quest_pages[1];
		assertEquals(location_page.getId(),"51b2f316d7b1deac1e000008");
		assertEquals(location_page.getPageContent(),"");
		assertEquals(location_page.getPageName(),"Untitled");
		assertEquals(location_page.getPageNumber(),1);
		assertEquals(location_page.getPageType(),"location");
		assertEquals(location_page.getLinks().length,2);
	}
	
	public void testQuestPageLocationLinks() throws Exception { 
		QuestPage location_page = _quest_pages[1];
		
		QuestPageLink[] links = location_page.getLinks();
		assertTrue( links[0] instanceof QuestPageLocationLink );
		assertTrue( links[0] instanceof QuestPageLocationLink );
		
		QuestPageLocationLink link_1 = (QuestPageLocationLink) links[0];
		assertEquals(link_1.getId(),"51b2f32cd7b1deac1e00000d");
		assertEquals(link_1.getRadius(),99);
		assertEquals(link_1.getTxtStreet(),"Pinsker, Tel Aviv, Israel");
		assertEquals(link_1.getLat(),32.077811083779714);
		assertEquals(link_1.getLng(),34.77396011352539);
		assertEquals(link_1.getLinksToPage(),"51b2f31ad7b1deac1e000009");
		
		QuestPageLocationLink link_2 = (QuestPageLocationLink) links[1];
		assertEquals(link_2.getId(),"51b2f335d7b1deac1e00000e");
		assertEquals(link_2.getRadius(),78);
		assertEquals(link_2.getTxtStreet(),"He Be'Iyar, Tel Aviv, Israel");
		assertEquals(link_2.getLat(),32.08784681959754);
		assertEquals(link_2.getLng(),34.790096282958984);
		assertEquals(link_2.getLinksToPage(),"51b2f31bd7b1deac1e00000a");
	}
	public void checkGeneralPageProperties(QuestPage page, String id, String content,String name,int number,String type) throws Exception{
		assertEquals(page.getId(), id);
		assertEquals(page.getPageContent(),content);
		assertEquals(page.getPageName(), name);
		assertEquals(page.getPageNumber(),number);
		assertEquals(page.getPageType(),type);
	}
	public void testQuestPageStatic() throws Exception {

		QuestPage page = _quest_pages[2];
		
		checkGeneralPageProperties(page, "51b877d183cea14d34000008", "abc", "Untitled", 7, "static");

		QuestPageLink[] links = page.getLinks();
		assertEquals(links.length,1);
		QuestPageLink link = links[0];
		assertTrue(link instanceof QuestPageLink);
		assertFalse(link instanceof QuestPageQuestionLink);
		assertFalse(link instanceof QuestPageLocationLink);
		assertEquals(link.getId(), "51b8784b83cea14d34000009");
		assertEquals(link.getLinksToPage(), "51b070be809e54e318000006");
		assertEquals(link.getType(), "regular");
		

		
	}
	
	public void testQuestPageStall() throws Exception {
		QuestPageStall page = (QuestPageStall) _quest_pages[3];
		checkGeneralPageProperties(page, "51c04a02d845f4091b000006", "<p>Stall page</p>\n<p>&nbsp;</p>", "You are being stalled", 9, "stall");
		assertEquals(page.getStallTime(), 1);
		QuestPageLink[] links = page.getLinks();
		assertEquals(links.length,1);
		assertEquals(links[0].getLinksToPage(), "51b070bf809e54e318000007");
		
		
	}
}
