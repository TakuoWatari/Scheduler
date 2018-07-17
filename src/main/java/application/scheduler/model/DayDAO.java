package application.scheduler.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import application.constant.CommonConst;
import application.dao.BaseDAO;
import application.scheduler.consts.SchedulerConstant;
import application.util.ApplicationUtil;
import application.util.CommonUtil;

public class DayDAO extends BaseDAO<Day> {

	private int year;
	private int month;
	private TaskDAO taskDAO;

	DayDAO(int year, int month) throws IOException {
		super(new File(
				ApplicationUtil.getDataDir(),
				String.format("daily-info_%1$04d-%2$02d.dat", year, month)));

		this.taskDAO = new TaskDAO(year, month);
		int maxDate = CommonUtil.getMonthMaxDay(year, month);
		for (int i = 1; i <= maxDate; i++) {
			this.insert(new Day(year, month, i));
		}
		this.load();
		this.taskDAO.deleteAll();

		this.year = year;
		this.month = month;
	}

	public Day[] getDays() {
		Day[] days = this.getDataValues().toArray(new Day[]{});
		return days;
	}

	public Day getDay(int date) {
		String id = String.format(SchedulerConstant.DAY_ID_FORMAT, this.year, this.month, date);

		Day value = null;
		if (contains(id)) {
			value = this.getData(id);
		}
		return value;
	}

	@Override
	protected Day parse(String data) throws IOException {

		String[] datas = data.split(CommonConst.TAB, 7);

		int year = Integer.parseInt(datas[0]);
		int month = Integer.parseInt(datas[1]);
		int date = Integer.parseInt(datas[2]);
		boolean holidayFlag = Boolean.parseBoolean(datas[3]);
		String event = CommonUtil.parseNullValue(datas[4],
				SchedulerConstant.NULL_SAVE_VALUE);
		String memo = CommonUtil.parseMulitiLineData(datas[5],
				SchedulerConstant.NULL_SAVE_VALUE);
		String taskIds = datas[6];

		Day value = new Day(year, month, date);
		value.setHolidayFlag(holidayFlag);
		value.setEventName(event);
		value.setMemo(memo);

		if (!CommonUtil.isEmpty(taskIds)) {
			String[] ids = taskIds.split(CommonConst.COMMA);
			for (String taskId : ids) {
				Task task = taskDAO.getTask(taskId);
				if (task != null) {
					value.addTask(task);
				}
			}
		}
		return value;
	}

	@Override
	protected String format(Day data) throws IOException {

		if (isEmpty(data)) {
			return null;
		}

		StringBuilder value = new StringBuilder();

		value.append(data.getYear());
		value.append(CommonConst.TAB);
		value.append(data.getMonth());
		value.append(CommonConst.TAB);
		value.append(data.getDate());
		value.append(CommonConst.TAB);
		value.append(data.isHoliday());
		value.append(CommonConst.TAB);
		value.append(CommonUtil.formatNullValue(data.getEvent(),
				SchedulerConstant.NULL_SAVE_VALUE));
		value.append(CommonConst.TAB);
		value.append(CommonUtil.formatMulitiLineData(data.getMemo(),
				SchedulerConstant.NULL_SAVE_VALUE));
		value.append(CommonConst.TAB);

		int taskCount = 0;
		List<Task> taskList = data.getTaskList();
		for (Task task : taskList) {
			if (!CommonUtil.isEmpty(task.getContent())) {
				this.taskDAO.addTask(task);
				if (taskCount > 0) {
					value.append(CommonConst.COMMA);
				}
				value.append(task.getId());
				taskCount++;
			}
		}

		return value.toString();
	}

	private boolean isEmpty(Day day) {
		boolean result = true;
		if (day.isHoliday()
				|| !CommonUtil.isEmpty(day.getEvent())
				|| !CommonUtil.isEmpty(day.getMemo())
				|| !day.getTaskList().isEmpty()) {
			result = false;
		}
		return result;
	}

	@Override
	public void commit() throws IOException {
		this.save();
		this.taskDAO.commit();
	}
}
