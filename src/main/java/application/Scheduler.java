package application;

import java.net.URL;

import application.util.ApplicationUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;

public class Scheduler extends ApplicationEx {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public StageStyle getStageStyle() {
		return StageStyle.DECORATED;
	}

	@Override
	public Image getIcon() {
		return ApplicationUtil.getImage("icon.bmp");
	}

	@Override
	public FXMLLoader getLoader() {
		return ApplicationUtil.getFXMLLoader("Calendar.fxml");
	}

	@Override
	public URL getStyleSheetUrl() {
		return ApplicationUtil.getStyleSheetURL("application.css");
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public double getMinHeight() {
		return 300.0;
	}

	@Override
	public double getMinWeight() {
		return 500.0;
	}
}
