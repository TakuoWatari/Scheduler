package org.myapp.sheduler.data;

import java.util.UUID;

import application.model.Data;
import application.util.CommonUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task implements Data {

	private static final String createNewID() {
		return UUID.randomUUID().toString();
	}

	private Integer year;
	private Integer month;
	private Integer day;

	private String id;
	private StringProperty name = new SimpleStringProperty();
	private StringProperty comment = new SimpleStringProperty();

	public Task(Integer year, Integer month, Integer day) {
		this.id = createNewID();
		this.year = year;
		this.month = month;
		this.day = day;
	}

	Task(String id, Integer year, Integer month, Integer day, String name, String comment) {
		this.id = id;
		this.year = year;
		this.month = month;
		this.day = day;
		this.name.set(name);
		this.comment.set(comment);
	}

	@Override
	public String getId() {
		return this.id;
	}

	public Integer getYear() {
		return this.year;
	}
	public Integer getMonth() {
		return this.month;
	}
	public Integer getDay() {
		return this.day;
	}

	public StringProperty nameProperty() {
		return this.name;
	}

	public StringProperty commentProperty() {
		return this.comment;
	}

	public String getName() {
		return this.name.get();
	}

	public String getComment() {
		return this.comment.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public void setComment(String comment) {
		this.comment.set(comment);
	}

	@Override
	public void disable() {
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.name.get());
		String comment = this.comment.get();
		if (!CommonUtil.isEmpty(comment)) {
			buf.append(" (");
			buf.append(comment);
			buf.append(")");
		}
		return buf.toString();
	}
}
