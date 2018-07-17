package application.scheduler.model;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import application.exception.SystemException;

public class Month {
	private int year;
	private int month;

	private DayDAO dayDAO;

	public Month(int year, int month) {
		try {
			this.year = year;
			this.month = month;
			this.dayDAO = new DayDAO(year, month);
		} catch (IOException e) {
			throw new SystemException("データ読み込みに失敗しました。");
		}
	}

	public Month getPrevYear() {
		return new Month(this.year - 1, month);
	}

	public Month getNextYear() {
		return new Month(this.year + 1, month);
	}

	public Month getNextMonth() {
		return getMonth(1);
	}

	public Month getPrevMonth() {
		return getMonth(-1);
	}

	private Month getMonth(int amountMonthValue) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(this.year, (this.month - 1), 1, 0, 0);
		calendar.add(Calendar.MONTH, amountMonthValue);
		return new Month(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
	}

	public int getYear() {
		return this.year;
	}

	public int getMonth() {
		return this.month;
	}

	public Day[] getDays() {
		return this.dayDAO.getDays();
	}

	public Day getDay(int date) {
		if (date < 1) {
			throw new IllegalArgumentException("該当する日は存在しません。");
		}

		Day value = this.dayDAO.getDay(date);
		if (value == null) {
			throw new IllegalArgumentException("該当する日は存在しません。");
		}

		return value;
	}
}
