package org.cms.client.controllers;

import com.jfoenix.controls.JFXRadioButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.cms.client.framework.config.Config;
import org.cms.client.framework.globals.Globals;
import org.cms.client.framework.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController implements Initializable {

	public static final String ROOT_LAYOUT = "/fxml/root.fxml";
	public TextField usernameField;
	public PasswordField passwordField;
	public JFXRadioButton studentRadio;
	public JFXRadioButton instructorRadio;
	public JFXRadioButton adminRadio;

	private FXMLLoader mainViewLoader;
	private Parent mainView;

	private static final Session session = Session.getInstance();
	private final ToggleGroup toggleGroup = new ToggleGroup();

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			mainViewLoader = new FXMLLoader(getClass().getResource(ROOT_LAYOUT));
			mainView = mainViewLoader.load();
			studentRadio.setToggleGroup(toggleGroup);
			instructorRadio.setToggleGroup(toggleGroup);
			adminRadio.setToggleGroup(toggleGroup);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loginOnAction(ActionEvent actionEvent) throws Exception {
		initializeSession();
		boolean authStatus = session.getRestClient().authenticate();
		if (!authStatus) {
			logger.error("Authentication failed");
			// TODO: Coloring of text fields......validity
			return;
		}
		logger.info("Auth success");

		updateUIAfterSuccessfulLogin();
	}

	private void updateUIAfterSuccessfulLogin() {
		Stage mainStage = Globals.getStage();
		mainStage.getScene().setRoot(mainView);
		mainStage.setHeight(800);
		mainStage.setWidth(1200);

		RootController rootController = mainViewLoader.getController();
		rootController.getHomeController().populateCoursesTable();
	}

	private String extractHostNameFromConfig() {
		return Config.get(Config.CMS_HOST);
	}

	private void initializeSession() {
		String hostName = extractHostNameFromConfig();
		RadioButton selectedRadio = (RadioButton) toggleGroup.getSelectedToggle();
		String userType = selectedRadio.getText().toLowerCase();
		session.initialize(hostName, usernameField.getText(), passwordField.getText(), userType);
		logger.info("Session initialized successfully");
	}
}
