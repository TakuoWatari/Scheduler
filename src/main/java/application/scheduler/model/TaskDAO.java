package application.scheduler.model;

import java.io.File;
import java.io.IOException;

import application.constant.CommonConst;
import application.dao.BaseDAO;
import application.scheduler.consts.SchedulerConstant;
import application.util.ApplicationUtil;
import application.util.CommonUtil;

class TaskDAO extends BaseDAO<Task> {

	TaskDAO(int year, int month) throws IOException {
		super(new File(
				ApplicationUtil.getDataDir(),
				String.format("task-info-%1$04d-%2$02d", year, month)));

		this.load();
	}

	public void addTask(Task task) {
		if (task == null) {
			throw new IllegalArgumentException("parameter[task] is invalid. task is null.");
		}
		this.insert(task);
	}

	public Task getTask(String taskId) {
		Task value = null;
		if (this.contains(taskId)) {
			value = this.getData(taskId);
		}
		return value;
	}

	@Override
	protected Task parse(String data) throws IOException {
		String[] datas = data.split(CommonConst.TAB, 3);

		String id = CommonUtil.parseNullValue(datas[0],
				SchedulerConstant.NULL_SAVE_VALUE);
		String content = CommonUtil.parseNullValue(datas[1],
				SchedulerConstant.NULL_SAVE_VALUE);
		String option = CommonUtil.parseNullValue(datas[2],
				SchedulerConstant.NULL_SAVE_VALUE);

		Task value = new Task(id);
		value.setContent(content);
		value.setOption(option);

		return value;
	}

	@Override
	protected String format(Task data) throws IOException {

		StringBuilder value = new StringBuilder();

		value.append(data.getId());
		value.append(CommonConst.TAB);
		value.append(CommonUtil.formatNullValue(data.getContent(),
				SchedulerConstant.NULL_SAVE_VALUE));
		value.append(CommonConst.TAB);
		value.append(CommonUtil.formatNullValue(data.getOption(),
				SchedulerConstant.NULL_SAVE_VALUE));

		return value.toString();
	}

	@Override
	public void commit() throws IOException {
		this.save();
	}
}
