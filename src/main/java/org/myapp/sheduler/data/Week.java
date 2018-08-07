package org.myapp.sheduler.data;

import javafx.beans.property.StringProperty;

public class Week {
	private Day sunDay = null;
	private Day monDay = null;
	private Day tuesDay = null;
	private Day wednesDay = null;
	private Day thursDay = null;
	private Day friDay = null;
	private Day saturDay = null;

	public Week(Day sunDay, Day monDay, Day tuesDay, Day wednesDay,
			Day thursDay, Day friDay, Day saturDay) {
		this.sunDay = sunDay;
		this.monDay = monDay;
		this.tuesDay = tuesDay;
		this.wednesDay = wednesDay;
		this.thursDay = thursDay;
		this.friDay = friDay;
		this.saturDay = saturDay;
	}

	public StringProperty sunDayProperty() {
		StringProperty value = null;
		if (this.sunDay != null) {
			value = this.sunDay.scheduleProperty();
		}
		return value;
	}

	public StringProperty monDayProperty() {
		StringProperty value = null;
		if (this.monDay != null) {
			value = this.monDay.scheduleProperty();
		}
		return value;
	}

	public StringProperty tuesDayProperty() {
		StringProperty value = null;
		if (this.tuesDay != null) {
			value = this.tuesDay.scheduleProperty();
		}
		return value;
	}

	public StringProperty wednesDayProperty() {
		StringProperty value = null;
		if (this.wednesDay != null) {
			value = this.wednesDay.scheduleProperty();
		}
		return value;
	}

	public StringProperty thursDayProperty() {
		StringProperty value = null;
		if (this.thursDay != null) {
			value = this.thursDay.scheduleProperty();
		}
		return value;
	}

	public StringProperty friDayProperty() {
		StringProperty value = null;
		if (this.friDay != null) {
			value = this.friDay.scheduleProperty();
		}
		return value;
	}

	public StringProperty saturDayProperty() {
		StringProperty value = null;
		if (this.saturDay != null) {
			value = this.saturDay.scheduleProperty();
		}
		return value;
	}

	public Day getSunDay() {
		return this.sunDay;
	}

	public Day getMonDay() {
		return this.monDay;
	}

	public Day getTuesDay() {
		return this.tuesDay;
	}

	public Day getWednesDay() {
		return this.wednesDay;
	}

	public Day getThursDay() {
		return this.thursDay;
	}

	public Day getFriDay() {
		return this.friDay;
	}

	public Day getSaturDay() {
		return this.saturDay;
	}
}
