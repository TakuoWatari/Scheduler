package org.myapp.sheduler.data;

import java.io.File;
import java.io.IOException;

import org.myapp.sheduler.constant.ScheduleConst;

import application.constant.CommonConst;
import application.dao.BaseDAO;
import application.util.ApplicationUtil;
import application.util.CommonUtil;

public class Month extends BaseDAO<Day> {

	public static final Month getInstance(Integer year, Integer month) throws IOException {
		return new Month(year, month);
	}

	private int year;
	private int month;
	private int maxDay;

	private TaskManager taskMng;

	protected Month(Integer year, Integer month) throws IOException {
		super(new File(ApplicationUtil.getDataDir(),
				String.format(ScheduleConst.SCHEDULE_SAVE_FILE_NAME, year, month)));

		this.year = year;
		this.month = month;

		this.maxDay = CommonUtil.getMonthMaxDay(year, month);
		this.taskMng = TaskManager.getInstance(year, month, this.maxDay);

		this.load();

		if (this.getDataValues().isEmpty()) {
			for (int i = 0; i < this.maxDay; i++) {
				Day data = new Day(year, month, i+1, false, null, null, taskMng.get(i+1));
				this.insert(data);
			}
		}
	}

	public int getYear() {
		return this.year;
	}

	public int getMonth() {
		return this.month;
	}

	public Day get(int day) {
		return this.getData(String.format("%1$02d", day));
	}

	@Override
	protected Day parse(String data) throws IOException {
		try {
			String[] datas = data.split(CommonConst.TAB);

			if (datas.length != ScheduleConst.SCHEDULE_DATA_COLUMNS) {
				throw new IllegalStateException("スケジュール情報保存用ファイルを読み込みに失敗しました。");
			}

			Integer year = Integer.valueOf(datas[0]);
			Integer month = Integer.valueOf(datas[1]);
			Integer day = Integer.valueOf(datas[2]);
			String memo = parseColumnData(datas[3]);
			String holidayName = parseColumnData(datas[4]);
			boolean holiday = Boolean.parseBoolean(datas[5]);

			Day dataValue = new Day(
					year, month, day, holiday, holidayName, memo, this.taskMng.get(day));

			return dataValue;

		} catch (NumberFormatException e) {
			throw new IllegalStateException("データ保存用ファイルの中身が壊れている可能性があります。");
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
						throw new IllegalStateException("データ保存用ファイルの中身が壊れている可能性があります。");
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
							throw new IllegalStateException("データ保存用ファイルの中身が壊れている可能性があります。");
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
	protected String format(Day data) {
		StringBuilder buf = new StringBuilder();

		buf.append(data.getYear());
		buf.append(CommonConst.TAB);
		buf.append(data.getMonth());
		buf.append(CommonConst.TAB);
		buf.append(data.getDay());
		buf.append(CommonConst.TAB);
		buf.append(this.formatColumnData(
				data.getMemo()));
		buf.append(CommonConst.TAB);
		buf.append(this.formatColumnData(
				data.getHolidayName()));
		buf.append(CommonConst.TAB);
		buf.append(data.isHoliday());

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
		this.taskMng.commit();
		this.save();
	}
}
