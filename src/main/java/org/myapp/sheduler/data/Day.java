package org.myapp.sheduler.data;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import application.model.Data;
import application.util.CommonUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Day implements Data {

	private static final String TASK_SEPARATOR = "\r\n";
	private static final int TO_STRING_TASK_LEN = 3;
	private static final TaskComparator COMPARATOR = new TaskComparator();
	private static final Calendar CALENDAR = GregorianCalendar.getInstance();

	private String id;
	private int year;
	private int month;
	private int day;
	private int weekDay;
	private boolean holiday;
	private String holidayName;
	private String memo;

	private List<Task> taskList;

	private StringProperty schedule = new SimpleStringProperty();

	public Day(int year, int month, int day, boolean holiday, String holidayName, String memo, List<Task> taskList) {
		this.year = year;
		this.month = month;
		this.day = day;
		CALENDAR.set(year, month - 1, day);
		this.weekDay = CALENDAR.get(Calendar.DAY_OF_WEEK);
		this.holiday = holiday;
		this.holidayName = holidayName;
		this.memo = memo;
		this.taskList = taskList;
		this.taskList.sort(COMPARATOR);

		this.id = String.format("%1$02d", this.day);
		setSchedule();
	}

	public int getYear() {
		return this.year;
	}

	public int getMonth() {
		return this.month;
	}

	public int getDay() {
		return this.day;
	}

	public boolean isSunday() {
		boolean result = false;
		if (this.weekDay == Calendar.SUNDAY) {
			result = true;
		}
		return result;
	}

	public boolean isMonday() {
		boolean result = false;
		if (this.weekDay == Calendar.MONDAY) {
			result = true;
		}
		return result;
	}

	public boolean isTuesday() {
		boolean result = false;
		if (this.weekDay == Calendar.TUESDAY) {
			result = true;
		}
		return result;
	}

	public boolean isWednesday() {
		boolean result = false;
		if (this.weekDay == Calendar.WEDNESDAY) {
			result = true;
		}
		return result;
	}

	public boolean isThursday() {
		boolean result = false;
		if (this.weekDay == Calendar.THURSDAY) {
			result = true;
		}
		return result;
	}

	public boolean isFriday() {
		boolean result = false;
		if (this.weekDay == Calendar.FRIDAY) {
			result = true;
		}
		return result;
	}

	public boolean isSaturday() {
		boolean result = false;
		if (this.weekDay == Calendar.SATURDAY) {
			result = true;
		}
		return result;
	}

	public boolean isHoliday() {
		return this.holiday;
	}

	public void setHoliday(boolean holiday) {
		this.holiday = holiday;
	}

	public String getHolidayName() {
		return this.holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getWeekday() {
		String weekdayName;
		switch (this.weekDay) {
			case Calendar.SUNDAY :
				weekdayName = "日";
				break;
			case Calendar.MONDAY :
				weekdayName = "月";
				break;
			case Calendar.TUESDAY :
				weekdayName = "火";
				break;
			case Calendar.WEDNESDAY :
				weekdayName = "水";
				break;
			case Calendar.THURSDAY :
				weekdayName = "木";
				break;
			case Calendar.FRIDAY :
				weekdayName = "金";
				break;
			case Calendar.SATURDAY :
				weekdayName = "土";
				break;
			default :
				throw new IllegalStateException("予期せぬ値が設定されています。");
		}
		return weekdayName;
	}

	public Task[] getTaskList() {
		Task[] taskArray = new Task[this.taskList.size()];
		this.taskList.toArray(taskArray);
		return taskArray;
	}

	public StringProperty scheduleProperty() {
		return this.schedule;
	}

	public void addTask(Task task) {
		this.taskList.add(task);
		this.refresh();
	}

	public void removeTask(Task task) {
		this.taskList.remove(task);
		this.setSchedule();
	}

	public void refresh() {
		this.taskList.sort(COMPARATOR);
		this.setSchedule();
	}

	public void setSchedule() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.day);
		if (!CommonUtil.isEmpty(this.memo)) {
			buf.append("★");
		}
		if (this.holiday) {
			buf.append(" ");
			buf.append(CommonUtil.formatNullValue(this.holidayName,""));
		}
		buf.append(TASK_SEPARATOR);
		int taskSize = this.taskList.size();
		for (int i = 0; i < TO_STRING_TASK_LEN; i++) {
			if (i < taskSize) {
				buf.append(this.taskList.get(i));
			}
			buf.append(TASK_SEPARATOR);
		}
		if (taskSize > TO_STRING_TASK_LEN) {
			buf.append("・・・");
		}
		this.schedule.set(buf.toString());
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.year);
		buf.append("/");
		buf.append(this.month);
		buf.append("/");
		buf.append(this.day);
		if (this.holiday) {
			buf.append(" [");
			buf.append(CommonUtil.formatNullValue(this.holidayName,""));
			buf.append("]");
		}
		buf.append(TASK_SEPARATOR);
		buf.append("<スケジュール>");
		buf.append(TASK_SEPARATOR);
		if (this.taskList.isEmpty()) {
			buf.append("なし");
			buf.append(TASK_SEPARATOR);
		} else {
			for (Task task : this.taskList) {
				buf.append(task.toString());
				buf.append(TASK_SEPARATOR);
			}
		}
		buf.append("<メモ>");
		buf.append(TASK_SEPARATOR);
		String memo = this.getMemo();
		if (CommonUtil.isEmpty(memo)) {
			buf.append("なし");
		} else {
			buf.append(this.getMemo());
		}
		return buf.toString();
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void disable() {
	}
}
