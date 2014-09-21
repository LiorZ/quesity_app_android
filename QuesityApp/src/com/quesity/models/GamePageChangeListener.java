package com.quesity.models;

public interface GamePageChangeListener {
	public void pageChanged(QuestPage page, QuestPage currentPage, QuestPageLink link);
	public void previousPage(QuestPage page);
}
