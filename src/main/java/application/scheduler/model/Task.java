package application.scheduler.model;

import java.util.UUID;

import application.model.Data;

public class Task implements Data {

	public static final Task createInstance() {
		Task task = new Task(UUID.randomUUID().toString());
		return task;
	}

	private String id;
	private String content;
	private String option;

	Task(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getOption() {
		return this.option;
	}
}
