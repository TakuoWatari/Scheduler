package application.scheduler.controller;

import application.contoller.ControllerBase;
import application.scheduler.model.Day;
import application.scheduler.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ScheduleController extends ControllerBase {

	@FXML
	private Label date;
	@FXML
	private TextField event;
	@FXML
	private CheckBox holidayBox;
	@FXML
	private TableView<Task> plan;
	@FXML
	private TableColumn<Task, String> contentColumn;
	@FXML
	private TableColumn<Task, String> optionColumn;
	@FXML
	private TextArea memo;

	@FXML
	private void initialize() {
		this.date.setText("");
		this.event.setText("");
		this.holidayBox.setSelected(false);
		this.memo.setText("");
	};

	public void setup(Day day) {

	}

	@FXML
	private void addTask() {
	}

	@FXML
	private void deleteTask() {
	}

	@FXML
	private void editNameCommited() {
	}

	@FXML
	private void editCommentCommited() {
	}

	@FXML
	private void closeWindow() {
		this.close();
	}
}
