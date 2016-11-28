package gess;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MainWindow extends Application {

	EmailConnectionService emailService = new EmailConnectionService("none", "none");

	Scene loginScene;
	Scene mainScene;
	Stage primaryStage;

	GridPane centerPane;

	private Button newRuleButton;
	private Button exportCSVButton;
	private Button applyRules;

	private Text actionTarget;

	private Button loginButton;

	private Button downloadEmailsButton;

	private static TextArea logArea;

	public static final boolean DEBUG_MODE = false;
	public static final boolean SHOW_GRIDS = false;

	private static Label headerInfoLabel;
	private static HBox header;
	
	private List<TextField> regexFields = new ArrayList<TextField>();

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("GESS - Gmail E-mail Scrape Service");

		mainScene = setMainScene();
		loginScene = setLoginScene();
		
		primaryStage.setScene(loginScene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

	private Scene setLoginScene() {

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text sceneTitle = new Text("GESS");
		sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		sceneTitle.setTextAlignment(TextAlignment.CENTER);
		grid.add(sceneTitle, 0, 0, 2, 1);

		Label emailLabel = new Label("E-mail:");
		grid.add(emailLabel, 0, 1);
		TextField emailTextField = new TextField();
		emailTextField.setTooltip(new Tooltip("Enter your Gmail e-mail e.g \"bob@gmail.com\""));

		grid.add(emailTextField, 1, 1);

		Label passwordLabel = new Label("Senha:");
		grid.add(passwordLabel, 0, 2);
		PasswordField passwordTextField = new PasswordField();
		passwordTextField.setTooltip(new Tooltip("Enter your Gmail password"));
		grid.add(passwordTextField, 1, 2);

		loginButton = new Button("Login");
		HBox hboxButton = new HBox(10);
		hboxButton.setAlignment(Pos.BOTTOM_RIGHT);
		hboxButton.getChildren().add(loginButton);
		grid.add(hboxButton, 1, 4);

		actionTarget = new Text();
		grid.add(actionTarget, 1, 6);

		loginButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				onLoginButtonClick(emailTextField.getText(), passwordTextField.getText());
			}
		});

		grid.setGridLinesVisible(SHOW_GRIDS);
		Scene scene = new Scene(grid, 450, 275);
		return scene;

	}

	private void onLoginButtonClick(String email, String password) {
		emailService = new EmailConnectionService(email, password);

		final Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				boolean result = false;

				try {
					loginButton.setDisable(true);

					actionTarget.setFill(Color.DODGERBLUE);
					actionTarget.setText("Connecting...");
					if (DEBUG_MODE) {
						result = true;
					} else {
						result = emailService.Login();
					}
					if (result) {
						actionTarget.setFill(Color.FORESTGREEN);
						actionTarget.setText("Login was successful!");
						Thread.sleep(300);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								primaryStage.setScene(mainScene);
								primaryStage.centerOnScreen();
								addToLog("Application started.");
							}
						});
					} else {
						actionTarget.setFill(Color.FIREBRICK);
						actionTarget.setText("Failed to login.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (result == false) {
					loginButton.setDisable(false);
				}
				return null;
			}

		};

		new Thread(task).start();

	}

	private Scene setMainScene() {

		BorderPane pane = new BorderPane();

		// Header
		header = new HBox();
		header.setPadding(new Insets(5, 10, 5, 10));
		header.setSpacing(10);
		header.setAlignment(Pos.CENTER);
		headerInfoLabel = new Label("Connected.");
		headerInfoLabel.setStyle("-fx-font: 15px Tahoma");
		header.getChildren().add(headerInfoLabel);
		header.setStyle("-fx-background-color: " + GESSColor.NEUTRAL_BLUE);

		// Left
		GridPane leftPane = new GridPane();
		leftPane.setAlignment(Pos.CENTER);
		leftPane.setPadding(new Insets(15, 15, 15, 15));
		leftPane.setVgap(15);

		downloadEmailsButton = new Button("Download all E-mails");
		downloadEmailsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onDownloadEmailsButtonClick();
			}
		});
		leftPane.add(downloadEmailsButton, 0, 0);

		applyRules = new Button("Apply Rules");
		applyRules.setDisable(true);
		applyRules.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onApplyRulesButtonClick();
			}
		});
		leftPane.add(applyRules, 0, 1);

		exportCSVButton = new Button("Export to CSV");
		exportCSVButton.setDisable(true);
		exportCSVButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onExportCSVButtonClick();
			}
		});
		leftPane.add(exportCSVButton, 0, 2);

		leftPane.setStyle("-fx-background-color: #d7e4e5");

		logArea = new TextArea();
		logArea.setPrefSize(250, 160);
		logArea.setEditable(false);
		logArea.setText("---LOG---");
		logArea.setWrapText(true);

		leftPane.add(logArea, 0, 3);

		// Center
		ScrollPane scrollPane = new ScrollPane();

		centerPane = new GridPane();
		centerPane.setAlignment(Pos.TOP_LEFT);
		centerPane.setPadding(new Insets(10, 15, 10, 15));
		centerPane.setVgap(15);

		Label rulesHeader = new Label("Rules");
		rulesHeader.setStyle("-fx-font: 20px Tahoma;");
		centerPane.add(rulesHeader, 0, 0);

		newRuleButton = new Button("Add Rule");
		newRuleButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onAddRuleButtonClick();
			}
		});

		centerPane.add(newRuleButton, 0, 1);

		scrollPane.setContent(centerPane);
		pane.setTop(header);
		pane.setLeft(leftPane);
		pane.setCenter(scrollPane);

		// leftPane.setGridLinesVisible(true);
		// centerPane.setGridLinesVisible(true);
		return new Scene(pane, 800, 400);

	}

	private void onApplyRulesButtonClick() {
		final Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				MainWindow.setHeaderInfo("Applying rules...", GESSColor.DOWNLOADING_ORANGE);
				emailService.ResetRules();
				for (TextField field : regexFields){
					if (field != null){
						emailService.AddRule(new FieldRule(field.getText()));
					}
				}
				emailService.ApplyAllRules();
				MainWindow.setHeaderInfo("Rules appplied. Ready to export to CSV.", GESSColor.SUCCESS_GREEN);
				exportCSVButton.setDisable(false);
				return null;
			}
		};

		new Thread(task).start();

	}
	
	private void onExportCSVButtonClick(){
		final Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				MainWindow.setHeaderInfo("Exporting to CSV...", GESSColor.DOWNLOADING_ORANGE);
				emailService.ExportCSV();
				MainWindow.setHeaderInfo("Exported to CSV successfully!", GESSColor.SUCCESS_GREEN);
				return null;
			}
		};

		new Thread(task).start();
		
	}

	public static void addToLog(String message) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String currentTime = sdf.format(calendar.getTime());

		message = "[" + currentTime + "] " + message;
		logArea.appendText("\n");
		logArea.appendText(message);
	}

	public static void setHeaderInfo(String message, String hexColor) {
		header.setStyle("-fx-background-color: " + hexColor);
		MainWindow.setHeaderInfo(message);
	}

	public static void setHeaderInfo(String message) {
		headerInfoLabel.setText(message);
	}

	private void onDeleteRuleButtonClick(HBox ruleBox) {
		centerPane.getChildren().remove(ruleBox);
	}

	private void onDownloadEmailsButtonClick() {
		final Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				downloadEmailsButton.setDisable(true);
				try {
					newRuleButton.setDisable(true);
					emailService.ReadEmails();
					MainWindow.setHeaderInfo("Ready to apply rules.", GESSColor.SUCCESS_GREEN);
					applyRules.setDisable(false);
				} catch (Exception e) {
					MainWindow.setHeaderInfo("Failed to download e-mails. Try again.", GESSColor.ERROR_RED);
					// e.printStackTrace();
				}
				newRuleButton.setDisable(false);
				downloadEmailsButton.setDisable(false);				
				return null;
			}

		};

		new Thread(task).start();
	}

	private void onAddRuleButtonClick() {
		HBox ruleBox = new HBox(10);
		ruleBox.setPadding(new Insets(0, 10, 0, 10));
		ruleBox.setAlignment(Pos.CENTER);

		Label ruleName = new Label("Rule");
		ruleBox.getChildren().add(ruleName);

		TextField regexField = new TextField();
		regexField.setTooltip(new Tooltip("The desired Regex for this rule."));
		regexField.setPromptText("Regex (example: \"Name: (.*)$\"");
		regexField.setPrefSize(250, 30);
		regexField.setFocusTraversable(false);
		ruleBox.getChildren().add(regexField);

		Button deleteButton = new Button("Delete");
		ruleBox.getChildren().add(deleteButton);
		centerPane.addColumn(0, ruleBox);
		
		regexFields.add(regexField);

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onDeleteRuleButtonClick(ruleBox);
			}
		});
	}

}