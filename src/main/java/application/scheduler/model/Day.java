package application.scheduler.model;

import java.util.ArrayList;
import java.util.List;

import application.model.Data;
import application.scheduler.consts.SchedulerConstant;
import application.util.CommonUtil;

public class Day implements Data {

	private String id;

	private int year;
	private int month;
	private int date;

	private boolean holidayFlag;
	private String event;

	private List<Task> taskList = new ArrayList<Task>();

	private String memo;

	/**
	 * コンストラクタ
	 * 同一パッケージ内でのみインスタンスの生成を可能とするため、修飾子なし（デフォルト)
	 * @param date
	 */
	Day(int year, int month, int date) {
		this.id = String.format(SchedulerConstant.DAY_ID_FORMAT, year, month, date);
		this.year = year;
		this.month = month;
		this.date = date;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public int getYear() {
		return this.year;
	}

	public int getMonth() {
		return this.month;
	}

	public int getDate() {
		return this.date;
	}

	public String getEvent() {
		return this.event;
	}

	public void setEventName(String event) {
		this.event = event;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setHolidayFlag(boolean holidayFlag) {
		this.holidayFlag = holidayFlag;
	}

	public boolean isHoliday() {
		return this.holidayFlag;
	}

	public List<Task> getTaskList() {
		return this.taskList;
	}

	public void addTask(Task task) {
		this.taskList.add(task);
	}

	public void removeTask(Task task) {
		if (task == null) {
			throw new IllegalArgumentException("parameter[task] is invalid. task is null.");
		}
		if (this.taskList.contains(task)) {
			taskList.remove(task);
		}
	}

	public String getDispValue() {
		StringBuilder buf = new StringBuilder();

		buf.append(this.date);
		if (!CommonUtil.isEmpty(this.event)) {
			buf.append(" ");
			buf.append(this.event);
		}
		if (!CommonUtil.isEmpty(this.memo)) {
			buf.append(" ★");
		}
		if (!this.taskList.isEmpty()) {
			for (Task task : this.taskList) {
				buf.append("\r\n");
				buf.append(task.toString());
			}
		}
		return buf.toString();
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();

		buf.append(String.format("%d/%d/%d", this.year, this.month, this.date));
		if (!CommonUtil.isEmpty(this.event)) {
			buf.append("\r\n");
			buf.append(this.event);
		}
		buf.append("\r\n[Plan]\r\n");
		if (!this.taskList.isEmpty()) {
			for (Task task : this.taskList) {
				buf.append(task.toString());
				buf.append("\r\n");
			}
		} else {
			buf.append("-");
		}
		buf.append("\r\n[Memo]\r\n");
		if (!CommonUtil.isEmpty(this.memo)) {
			buf.append(this.memo);
		} else {
			buf.append("-");
		}
		return buf.toString();
	}
}
