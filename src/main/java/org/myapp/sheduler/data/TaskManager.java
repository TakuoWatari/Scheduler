package org.myapp.sheduler.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.myapp.sheduler.constant.ScheduleConst;

import application.constant.CommonConst;
import application.dao.BaseDAO;
import application.util.ApplicationUtil;
import application.util.CommonUtil;

class TaskManager extends BaseDAO<Task> {

	public static final TaskManager getInstance(int year, int month, int maxDay) throws IOException {
		return new TaskManager(year, month, maxDay);
	}

	private List<Task>[] dalilyTask;

	@SuppressWarnings("unchecked")
	TaskManager(int year, int month, int maxDay) throws IOException {
		super(new File(ApplicationUtil.getDataDir(),
				String.format(ScheduleConst.TASK_SAVE_FILE_NAME, year, month)));

		this.dalilyTask = new List[maxDay];
		for (int i = 0; i < maxDay; i++) {
			this.dalilyTask[i] = new ArrayList<>();
		}

		this.load();
		this.afterLoadProcess();
		this.deleteAll();
	}

	public List<Task> get(int day) {
		return this.dalilyTask[day - 1];
	}

	private void afterLoadProcess() {
		Collection<Task> datas = this.getDataValues();
		for (Task data : datas) {
			Integer day = data.getDay();
			int index = day.intValue() - 1;
			this.dalilyTask[index].add(data);
		}
		this.deleteAll();
	}

	@Override
	protected Task parse(String data) throws IOException {
		try {
			String[] datas = data.split(CommonConst.TAB);

			if (datas.length != ScheduleConst.TASK_DATA_COLUMNS) {
				throw new IllegalStateException("スケジュール情報保存用ファイルを読み込みに失敗しました。");
			}

			Integer year = Integer.valueOf(datas[0]);
			Integer month = Integer.valueOf(datas[1]);
			Integer day = Integer.valueOf(datas[2]);
			String name = parseColumnData(datas[3]);
			String comment = parseColumnData(datas[4]);
			String id = datas[5];

			Task task = new Task(
					id, year, month, day, name, comment);

			return task;

		} catch (NumberFormatException e) {
			throw new IllegalStateException("明細情報保存用ファイルの中身が壊れている可能性があります。");
		}
	}

	private String parseColumnData(String columData) {
		String parseValue = CommonUtil.parseNullValue(
				columData, ScheduleConst.NULL_FORMAT_VALUE);
		if (!CommonUtil.isEmpty(columData)) {
			int escapeIndex = columData.indexOf('\\');
			int commentLength = columData.length();
			if (escapeIndex != -1) {
				StringBuilder newValue = new StringBuilder(
						columData.substring(0, escapeIndex));

				while (escapeIndex != -1) {
					if (escapeIndex == commentLength) {
						throw new IllegalStateException("明細情報保存用ファイルの中身が壊れている可能性があります。");
					}
					int escapeNextIndex = escapeIndex + 1;
					char nextChar = columData.charAt(escapeNextIndex);
					switch (nextChar) {
						case '\\' :
							newValue.append('\\');
							break;
						case 'r' :
							newValue.append('\r');
							break;
						case 'n' :
							newValue.append('\n');
							break;
						case 't' :
							newValue.append('\t');
							break;
						default :
							throw new IllegalStateException("明細情報保存用ファイルの中身が壊れている可能性があります。");
					}
					int startIndex = escapeNextIndex + 1;
					escapeIndex = columData.indexOf('\\', startIndex);
					if (escapeIndex == -1) {
						newValue.append(columData.substring(startIndex));
					} else if (escapeIndex != escapeNextIndex + 1){
						newValue.append(columData.substring(startIndex, escapeIndex));
					}
				}

				parseValue = newValue.toString();
			}
		}
		return parseValue;
	}

	@Override
	protected String format(Task data) {
		StringBuilder buf = new StringBuilder();

		buf.append(data.getYear());
		buf.append(CommonConst.TAB);
		buf.append(data.getMonth());
		buf.append(CommonConst.TAB);
		buf.append(data.getDay());
		buf.append(CommonConst.TAB);
		buf.append(this.formatColumnData(
				data.nameProperty().get()));
		buf.append(CommonConst.TAB);
		buf.append(this.formatColumnData(
				data.commentProperty().get()));
		buf.append(CommonConst.TAB);
		buf.append(data.getId());

		return buf.toString();
	}

	private String formatColumnData(String columnData) {
		String formatData = CommonUtil.formatNullValue(
				columnData, ScheduleConst.NULL_FORMAT_VALUE);
		formatData = formatData.replaceAll("\\\\", "\\\\\\\\");
		formatData = formatData.replaceAll("\\r", "\\\\r");
		formatData = formatData.replaceAll("\\n", "\\\\n");
		formatData = formatData.replaceAll("\\t", "\\\\t");

		return formatData;
	}

	@Override
	public void commit() throws IOException {
		for (List<Task> taskList : this.dalilyTask) {
			if (taskList != null) {
				for (Task task : taskList) {
					this.insert(task);
				}
			}
		}
		this.save();
	}
}
