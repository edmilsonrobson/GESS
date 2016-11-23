
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

	private static TextArea logArea;

	public static final boolean DEBUG_MODE = true;
	public static final boolean SHOW_GRIDS = false;
	
	private int ruleOffset = 2;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("GESS - Gmail E-mail Scrape Service");

		mainScene = SetMainScene();
		loginScene = SetLoginScene();

		primaryStage.setScene(loginScene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private Scene SetLoginScene() {

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

		Button button = new Button("Conectar");
		HBox hboxButton = new HBox(10);
		hboxButton.setAlignment(Pos.BOTTOM_RIGHT);
		hboxButton.getChildren().add(button);
		grid.add(hboxButton, 1, 4);

		final Text actionTarget = new Text();
		grid.add(actionTarget, 1, 6);

		button.setOnAction(new EventHandler<ActionEvent>() {
			// FIXME

			@Override
			public void handle(ActionEvent event) {
				String email, password;
				email = emailTextField.getText();
				password = passwordTextField.getText();
				emailService = new EmailConnectionService(email, password);
				boolean result;

				if (DEBUG_MODE)
					result = true;
				else
					result = emailService.Login();

				if (result) {
					actionTarget.setFill(Color.DARKSEAGREEN);
					actionTarget.setText("Login was successful.");
					primaryStage.setScene(mainScene);
				} else {
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("SSS");
					String currentTime = sdf.format(cal.getTime());

					actionTarget.setFill(Color.FIREBRICK);
					actionTarget.setText("Failed to login @ " + currentTime);
				}

			}
		});

		grid.setGridLinesVisible(SHOW_GRIDS);
		Scene scene = new Scene(grid, 450, 275);
		return scene;

	}

	private Scene SetMainScene() {

		BorderPane pane = new BorderPane();

		// Header
		HBox header = new HBox();
		header.setPadding(new Insets(5, 10, 5, 10));
		header.setSpacing(10);
		header.setAlignment(Pos.CENTER);
		Label headerInfoLabel = new Label("Connected.");
		headerInfoLabel.setStyle("-fx-font: 15px Tahoma");
		header.getChildren().add(headerInfoLabel);
		header.setStyle("-fx-background-color: " + GESSColor.NEUTRAL_BLUE);

		// Left
		GridPane leftPane = new GridPane();
		leftPane.setAlignment(Pos.CENTER);
		leftPane.setPadding(new Insets(15, 15, 15, 15));
		leftPane.setVgap(15);

		Button downloadEmailsButton = new Button("Download all E-mails");
		leftPane.add(downloadEmailsButton, 0, 0);
		Button exportCSVButton = new Button("Export to CSV");
		exportCSVButton.setDisable(true);
		leftPane.add(exportCSVButton, 0, 1);
		leftPane.setStyle("-fx-background-color: #d7e4e5");

		logArea = new TextArea();
		logArea.setPrefSize(250, 160);
		logArea.setEditable(false);
		logArea.setText("---LOG---");
		logArea.setWrapText(true);
		leftPane.add(logArea, 0, 2);

		// Center
		centerPane = new GridPane();
		centerPane.setAlignment(Pos.TOP_LEFT);
		centerPane.setPadding(new Insets(15, 15, 15, 15));
		centerPane.setVgap(15);		
		
		Label rulesHeader = new Label("Rules");
		rulesHeader.setStyle("-fx-font: 20px Tahoma;");
		centerPane.add(rulesHeader, 0, 0);
		
		Button newRuleButton = new Button("Add Rule");
		newRuleButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				AddRule();
			}
		});
		centerPane.add(newRuleButton, 0, 1);
		
		
		pane.setTop(header);
		pane.setLeft(leftPane);
		pane.setCenter(centerPane);
		
		//leftPane.setGridLinesVisible(true);
		//centerPane.setGridLinesVisible(true);
		return new Scene(pane, 800, 400);

	}

	public static void AddToLog(String message) {
		logArea.setText(logArea.getText() + "\n" + message);
	}
	
	private void AddRule(){
		VBox ruleBox = new VBox(10);
		ruleBox.setPadding(new Insets(5, 10, 5, 10));
		
		centerPane.add(ruleBox, 0, ruleOffset);
		ruleOffset++;
	}

}