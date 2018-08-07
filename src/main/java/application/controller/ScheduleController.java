package application.controller;

import java.io.IOException;

import org.myapp.sheduler.data.Day;
import org.myapp.sheduler.data.Task;

import application.content.PopupWindow;
import application.contoller.ControllerBase;
import application.util.ApplicationUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class ScheduleController extends ControllerBase {


	private static PopupWindow dialog;

	public static final synchronized void show(Window owner, Day dispData) throws IOException {
		setup(owner, dispData);
		dialog.show();
	}

	private static void setup(Window owner, Day dispData) throws IOException {
		if (dialog == null) {
			dialog = new PopupWindow(
					owner, StageStyle.DECORATED, "スケジュール",
					"Schedule.fxml",
					Modality.WINDOW_MODAL,
					false, false,
					ApplicationUtil.getStyleSheetURL("application.css").toExternalForm());
		}

		ScheduleController controller = (ScheduleController) dialog.getController();
		controller.setData(dispData);
	}

	@FXML
	private Label date;
	@FXML
	private CheckBox holidayBox;
	@FXML
	private TextField holidayName;
	@FXML
	private TableView<Task> schedule;
	@FXML
	private TableColumn<Task, String> nameColumn;
	@FXML
	private TableColumn<Task, String> commentColumn;
	@FXML
	private TextArea memo;

	private ObservableList<Task> taskList;

	private Day dispData;

	@FXML
	private void initialize() {

		this.taskList = this.schedule.getItems();

		this.nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.commentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	}

	private void setData(Day dispData) {
		this.dispData = dispData;

		Integer year = dispData.getYear();
		Integer month = dispData.getMonth();
		Integer day = dispData.getDay();

		this.date.setText(year + "年" + month + "月" + day + "日" + " （" + dispData.getWeekday() + "）");

		this.holidayBox.setSelected(dispData.isHoliday());
		this.holidayName.setText(dispData.getHolidayName());
		holidayChanged();

		this.taskList.clear();
		Task[] taskDatas = dispData.getTaskList();
		for (Task task : taskDatas) {
			this.taskList.add(task);
		}

		this.memo.setText(dispData.getMemo());
	}

	@FXML
	private void holidayChanged() {
		if (this.holidayBox.isSelected()) {
			this.holidayName.setEditable(true);
			this.holidayName.setDisable(false);
		} else {
			this.holidayName.setEditable(false);
			this.holidayName.setDisable(true);
		}
	}

	@FXML
	private void addTask() {
		Task newTask = new Task(this.dispData.getYear(), this.dispData.getMonth(), this.dispData.getDay());
		this.dispData.addTask(newTask);
		this.taskList.add(newTask);
	}

	@FXML
	private void deleteTask() {
		Task targetTask = schedule.getSelectionModel().getSelectedItem();
		if (targetTask != null) {
			this.dispData.removeTask(targetTask);
			this.taskList.remove(targetTask);
		}
	}

	@FXML
	private void editNameCommited(CellEditEvent<Task, String> event) {
		Task target = event.getRowValue();
		target.setName(event.getNewValue());
		this.dispData.refresh();
	}

	@FXML
	private void editCommentCommited(CellEditEvent<Task, String> event) {
		Task target = event.getRowValue();
		target.setComment(event.getNewValue());
		this.dispData.refresh();
	}

	@FXML
	private void closeWindow() {
		this.dispData.setHoliday(this.holidayBox.isSelected());
		if (this.dispData.isHoliday()) {
			this.dispData.setHolidayName(this.holidayName.getText());
		} else {
			this.dispData.setHolidayName(null);
		}
		this.dispData.setMemo(this.memo.getText());

		this.close();
	}
}
