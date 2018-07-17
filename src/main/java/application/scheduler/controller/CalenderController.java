package application.scheduler.controller;

import java.io.IOException;
import java.util.Calendar;

import application.content.PopupWindow;
import application.contoller.ControllerBase;
import application.scheduler.model.Day;
import application.scheduler.model.Month;
import application.util.CommonUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class CalenderController extends ControllerBase {
	@FXML
	private ChoiceBox<Integer> searchYear;
	@FXML
	private ChoiceBox<Integer> searchMonth;
	@FXML
	private Label dispYearMonth;
	@FXML
	private Label sun1;
	@FXML
	private Label mon1;
	@FXML
	private Label tue1;
	@FXML
	private Label wed1;
	@FXML
	private Label thu1;
	@FXML
	private Label fri1;
	@FXML
	private Label sat1;
	@FXML
	private Label sun2;
	@FXML
	private Label mon2;
	@FXML
	private Label tue2;
	@FXML
	private Label wed2;
	@FXML
	private Label thu2;
	@FXML
	private Label fri2;
	@FXML
	private Label sat2;
	@FXML
	private Label sun3;
	@FXML
	private Label mon3;
	@FXML
	private Label tue3;
	@FXML
	private Label wed3;
	@FXML
	private Label thu3;
	@FXML
	private Label fri3;
	@FXML
	private Label sat3;
	@FXML
	private Label sun4;
	@FXML
	private Label mon4;
	@FXML
	private Label tue4;
	@FXML
	private Label wed4;
	@FXML
	private Label thu4;
	@FXML
	private Label fri4;
	@FXML
	private Label sat4;
	@FXML
	private Label sun5;
	@FXML
	private Label mon5;
	@FXML
	private Label tue5;
	@FXML
	private Label wed5;
	@FXML
	private Label thu5;
	@FXML
	private Label fri5;
	@FXML
	private Label sat5;
	@FXML
	private Label sun6;
	@FXML
	private Label mon6;
	@FXML
	private Label tue6;
	@FXML
	private Label wed6;
	@FXML
	private Label thu6;
	@FXML
	private Label fri6;
	@FXML
	private Label sat6;
	@FXML
	private GridPane calendar;
	@FXML
	private Label message;

	private Label[] dayLabels;

	private Month dispData;

	private PopupWindow scheduleSubWindow;

	@FXML
	private void initialize() {
		this.dayLabels = new Label[] {
			sun1, mon1, tue1, wed1, thu1, fri1, sat1,
			sun2, mon2, tue2, wed2, thu2, fri2, sat2,
			sun3, mon3, tue3, wed3, thu3, fri3, sat3,
			sun4, mon4, tue4, wed4, thu4, fri4, sat4,
			sun5, mon5, tue5, wed5, thu5, fri5, sat5,
			sun6, mon6, tue6, wed6, thu6, fri6, sat6
		};

		int sysYear = CommonUtil.getSystemYear();
		int sysMonth = CommonUtil.getSystemMonth();

		int maxYear = sysYear + 10;

		int startYear = 2015;
		if (sysYear < startYear) {
			startYear = sysYear;
		}

		for (int i = startYear; i < maxYear; i ++) {
			this.searchYear.getItems().add(i);
		}
		this.searchYear.setValue(sysYear);

		for (int i = 1; i <= 12; i ++) {
			this.searchMonth.getItems().add(i);
		}
		this.searchMonth.setValue(sysMonth);

		this.dispData = new Month(sysYear, sysMonth);
		setupCalendar();
	}

	@FXML
	private void dispSchedule() {
		int year = this.searchYear.getValue();
		int month = this.searchMonth.getValue();
		this.dispData = new Month(year, month);
		setupCalendar();
	}

	@FXML
	private void prevYear() {
		this.dispData = this.dispData.getPrevYear();
		setupCalendar();
	}

	@FXML
	private void nextYear() {
		this.dispData = this.dispData.getNextYear();
		setupCalendar();
	}

	@FXML
	private void prevMonth() {
		this.dispData = this.dispData.getPrevMonth();
		setupCalendar();
	}

	@FXML
	private void nextMonth() {
		this.dispData = this.dispData.getNextMonth();
		setupCalendar();
	}

	private void showSchedule(Day showDay) {
		try {
			if (this.scheduleSubWindow == null) {
				this.scheduleSubWindow = new PopupWindow(this.getStage(), StageStyle.UNDECORATED,
						"スケジュール", "Schedule.fxml", Modality.APPLICATION_MODAL, true, false, "application.css");
			}
			ScheduleController controller = (ScheduleController) this.scheduleSubWindow.getController();
			controller.setup(showDay);
			this.scheduleSubWindow.show();
		} catch (IOException e) {
			this.showErrorMessage("エラーが発生しました。", "子画面表示に失敗しました。", e);
		}
	}

	private void setupCalendar() {
		int year = this.dispData.getYear();
		int month = this.dispData.getMonth();

		this.dispYearMonth.setText(year + "年" + month + "月");
		Day[] dayData = this.dispData.getDays();

		int weekDay = CommonUtil.getWeekDay(year, month, 1);
		int startIndex = -1;
		switch (weekDay) {
			case Calendar.SUNDAY :
				startIndex = 0;
				break;
			case Calendar.MONDAY :
				startIndex = 1;
				break;
			case Calendar.TUESDAY :
				startIndex = 2;
				break;
			case Calendar.WEDNESDAY :
				startIndex = 3;
				break;
			case Calendar.THURSDAY :
				startIndex = 4;
				break;
			case Calendar.FRIDAY :
				startIndex = 5;
				break;
			case Calendar.SATURDAY :
				startIndex = 6;
				break;
		}
		int endIndex = startIndex + dayData.length;

		int sysYear = CommonUtil.getSystemYear();
		int sysMonth = CommonUtil.getSystemMonth();
		int sysDate = CommonUtil.getSystemDay();

		int dayCount = 0;
		for (int i = 0; i < this.dayLabels.length; i++) {
			this.dayLabels[i].getStyleClass().remove("none");
			this.dayLabels[i].getStyleClass().remove("holiday");
			this.dayLabels[i].getStyleClass().remove("today");
			this.dayLabels[i].setText(null);

			if (i < startIndex || i >= endIndex) {
				this.dayLabels[i].getStyleClass().add("none");
				this.dayLabels[i].setOnMouseClicked(null);
			} else {
				Day day = dayData[dayCount];
				if (day.isHoliday()) {
					this.dayLabels[i].getStyleClass().add("holiday");
				}
				if (year == sysYear && month == sysMonth && day.getDate() == sysDate) {
					this.dayLabels[i].getStyleClass().add("today");
				}
				this.dayLabels[i].setText(day.getDispValue());
				this.dayLabels[i].setTooltip(new Tooltip(day.toString()));
				dayCount++;
				this.dayLabels[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						// TODO 自動生成されたメソッド・スタブ
						showSchedule(day);
					}
				});
			}
		}
	}
}
