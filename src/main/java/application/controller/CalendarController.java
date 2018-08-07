package application.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.myapp.sheduler.data.Day;
import org.myapp.sheduler.data.Month;
import org.myapp.sheduler.data.Week;

import application.contoller.ControllerBase;
import application.util.CommonUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

public class CalendarController extends ControllerBase {

	@FXML
	private ChoiceBox<Integer> year;
	@FXML
	private ChoiceBox<Integer> month;
	@FXML
	private Label dispYearMonth;
	@FXML
	private TableView<Week> calendar;
	@FXML
	private TableColumn<Week, String> sunDayColumn;
	@FXML
	private TableColumn<Week, String> monDayColumn;
	@FXML
	private TableColumn<Week, String> tuesDayColumn;
	@FXML
	private TableColumn<Week, String> wednesDayColumn;
	@FXML
	private TableColumn<Week, String> thursDayColumn;
	@FXML
	private TableColumn<Week, String> friDayColumn;
	@FXML
	private TableColumn<Week, String> saturDayColumn;

	private static final Calendar CALENDAR = GregorianCalendar.getInstance();

	private ObservableList<Integer> yearList;
	private ObservableList<Integer> monthList;
	private ObservableList<Week> weekList;

	private Integer dispYear;
	private Integer dispMonth;

	private static int sysYear;
	private static int sysMonth;
	private static int sysDay;

	private Month monthMng;

	private final Callback<TableColumn<Week,String>,TableCell<Week,String>> cellFactory = new Callback<TableColumn<Week,String>,TableCell<Week,String>>() {
		@Override
		public TableCell<Week, String> call(TableColumn<Week, String> item) {
			return new TableCell<Week, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					getStyleClass().remove("today-column-cell");
					getStyleClass().remove("holiday-column-cell");
					getStyleClass().remove("sunday-column-cell");
					getStyleClass().remove("satureday-column-cell");
					getStyleClass().remove("weekday-column-cell");
					getStyleClass().remove("none-column-cell");

					if (item == null || empty) {
						setText(null);
						setTooltip(null);
						getStyleClass().add("none-column-cell");
					} else {
						Week targetWeek = calendar.getItems().get(getTableRow().getIndex());
						TableColumn<Week, String> col = getTableColumn();
						Day targetDay = null;
						if (col.equals(sunDayColumn)) {
							targetDay = targetWeek.getSunDay();
						} else if (col.equals(monDayColumn)) {
							targetDay = targetWeek.getMonDay();
						} else if (col.equals(tuesDayColumn)) {
							targetDay = targetWeek.getTuesDay();
						} else if (col.equals(wednesDayColumn)) {
							targetDay = targetWeek.getWednesDay();
						} else if (col.equals(thursDayColumn)) {
							targetDay = targetWeek.getThursDay();
						} else if (col.equals(friDayColumn)) {
							targetDay = targetWeek.getFriDay();
						} else if (col.equals(saturDayColumn)) {
							targetDay = targetWeek.getSaturDay();
						}

						if (targetDay != null) {
							int year = targetDay.getYear();
							int month = targetDay.getMonth();
							int day = targetDay.getDay();
							if (year == sysYear && month == sysMonth && day == sysDay) {
								getStyleClass().add("today-column-cell");
							} else {
								if (targetDay.isHoliday()) {
									getStyleClass().add("holiday-column-cell");
								} else if (targetDay.isSunday()) {
									getStyleClass().add("sunday-column-cell");
								} else if (targetDay.isSaturday()) {
									getStyleClass().add("satureday-column-cell");
								} else {
									getStyleClass().add("weekday-column-cell");
								}
							}
							setText(targetDay.scheduleProperty().get());
							setTooltip(new Tooltip(targetDay.toString()));
						} else {
							getStyleClass().add("none-column-cell");
						}
					}
				}
			};
		}
	};

	@FXML
	private void initialize() {

		sysYear = CommonUtil.getSystemYear();
		sysMonth = CommonUtil.getSystemMonth();
		sysDay = CommonUtil.getSystemDay();

		this.yearList = this.year.getItems();
		this.monthList = this.month.getItems();

		int maxYear = sysYear + 10;
		for (int i = 2000; i <= maxYear; i++) {
			this.yearList.add(i);
		}
		for (int i = 1; i <= 12; i++) {
			this.monthList.add(i);
		}

		this.year.setValue(Integer.valueOf(sysYear));
		this.month.setValue(Integer.valueOf(sysMonth));

		this.weekList = this.calendar.getItems();

		this.calendar.getSelectionModel().cellSelectionEnabledProperty().set(true);

		this.calendar.setOnMousePressed(event -> {
			if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
				openDayScheduleDialog();
			}
		});
		this.calendar.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				openDayScheduleDialog();
			}
		});

		this.sunDayColumn.setCellFactory(cellFactory);
		this.monDayColumn.setCellFactory(cellFactory);
		this.tuesDayColumn.setCellFactory(cellFactory);
		this.wednesDayColumn.setCellFactory(cellFactory);
		this.thursDayColumn.setCellFactory(cellFactory);
		this.friDayColumn.setCellFactory(cellFactory);
		this.saturDayColumn.setCellFactory(cellFactory);

		dispSchedule();
	}

	@SuppressWarnings("rawtypes")
	private void openDayScheduleDialog() {
		ObservableList<TablePosition> positionList = calendar.getSelectionModel().getSelectedCells();
		for (TablePosition position : positionList) {
			int rowIndex = position.getRow();
			Week selectedWeek = this.calendar.getItems().get(rowIndex);
			Day selectedDay = null;

			TableColumn selectedColumn = position.getTableColumn();
			if (selectedColumn != null) {
				if (selectedColumn == this.sunDayColumn) {
					selectedDay = selectedWeek.getSunDay();
				} else if (selectedColumn == this.monDayColumn) {
					selectedDay = selectedWeek.getMonDay();
				} else if (selectedColumn == this.tuesDayColumn) {
					selectedDay = selectedWeek.getTuesDay();
				} else if (selectedColumn == this.wednesDayColumn) {
					selectedDay = selectedWeek.getWednesDay();
				} else if (selectedColumn == this.thursDayColumn) {
					selectedDay = selectedWeek.getThursDay();
				} else if (selectedColumn == this.friDayColumn) {
					selectedDay = selectedWeek.getFriDay();
				} else if (selectedColumn == this.saturDayColumn) {
					selectedDay = selectedWeek.getSaturDay();
				}
			}
			if (selectedDay != null) {
				try {
					ScheduleController.show(this.getStage(), selectedDay);
					this.monthMng.commit();

				} catch (IOException e) {
					this.showSystemErrorMessage("データ読み込みに失敗しました。", e);
				}
				setCalendar();
				break;
			}
		}
	}

	@FXML
	private void dispSchedule() {
		this.dispYear = this.year.getValue();
		this.dispMonth = this.month.getValue();
		setCalendar();
	}

	@FXML
	private void prevYear() {
		this.dispYear--;
		setCalendar();
	}

	@FXML
	private void prevMonth() {
		if (!this.dispMonth.equals(Integer.valueOf(1))) {
			this.dispMonth--;
		} else {
			this.dispYear--;
			this.dispMonth = Integer.valueOf(12);
		}
		setCalendar();
	}
	@FXML
	private void nextMonth() {
		if (!this.dispMonth.equals(Integer.valueOf(12))) {
			this.dispMonth++;
		} else {
			this.dispYear++;
			this.dispMonth = Integer.valueOf(1);
		}
		setCalendar();
	}
	@FXML
	private void nextYear() {
		this.dispYear++;
		setCalendar();
	}

	private void setCalendar() {

		try {
			sysYear = CommonUtil.getSystemYear();
			sysMonth = CommonUtil.getSystemMonth();
			sysDay = CommonUtil.getSystemDay();

			this.weekList.clear();

			this.dispYearMonth.setText(this.dispYear + "年" + this.dispMonth + "月");

			CALENDAR.set(Calendar.YEAR, this.dispYear);
			CALENDAR.set(Calendar.MONTH, this.dispMonth - 1);

			this.monthMng = Month.getInstance(this.dispYear, this.dispMonth);

			Day sunDay = null;
			Day monDay = null;
			Day tuesDay = null;
			Day wednesDay = null;
			Day thursDay = null;
			Day friDay = null;
			Day staurDay = null;

			int maxDay = CommonUtil.getMonthMaxDay(this.dispYear, this.dispMonth);

			for (int i = 1; i <= maxDay; i++) {
				Day day = this.monthMng.get(i);

				CALENDAR.set(Calendar.DAY_OF_MONTH, i);
				int weekDay = CALENDAR.get(Calendar.DAY_OF_WEEK);
				switch (weekDay) {
					case Calendar.SUNDAY :
						sunDay = day;
						break;
					case Calendar.MONDAY :
						monDay = day;
						break;
					case Calendar.TUESDAY :
						tuesDay = day;
						break;
					case Calendar.WEDNESDAY :
						wednesDay = day;
						break;
					case Calendar.THURSDAY :
						thursDay = day;
						break;
					case Calendar.FRIDAY :
						friDay = day;
						break;
					case Calendar.SATURDAY :
						staurDay = day;
						this.weekList.add(
								new Week(sunDay, monDay, tuesDay, wednesDay,
										thursDay, friDay, staurDay));
						sunDay = null;
						monDay = null;
						tuesDay = null;
						wednesDay = null;
						thursDay = null;
						friDay = null;
						staurDay = null;
						break;
				}
			}
			if (sunDay != null) {
				this.weekList.add(
						new Week(sunDay, monDay, tuesDay, wednesDay,
								thursDay, friDay, staurDay));
			}
		} catch (IOException e) {
			this.showSystemErrorMessage("カレンダー情報の読み込みに失敗しました。", e);
		}
	}
}
