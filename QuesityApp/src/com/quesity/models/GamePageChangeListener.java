package com.quesity.models;

public interface GamePageChangeListener {
	public void pageChanged(QuestPage prev_page, QuestPage currentPage, QuestPageLink link);
	public void previousPage(QuestPage page);
}
