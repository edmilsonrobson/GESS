
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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainWindow extends Application {

	EmailConnectionService emailService;	
	
	@Override
	public void start(Stage primaryStage){
		primaryStage.setTitle("Form");
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25,25,25,25));
		
		Text sceneTitle = new Text("Bem-vindo!");
		sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(sceneTitle, 0, 0, 2, 1);
		
		Label emailLabel = new Label("E-mail:");
		grid.add(emailLabel, 0, 1);
		TextField emailTextField = new TextField();
		grid.add(emailTextField, 1, 1);
		
		Label passwordLabel = new Label("Senha:");
		grid.add(passwordLabel, 0, 2);
		PasswordField passwordTextField = new PasswordField();
		grid.add(passwordTextField, 1, 2);
		
		Button button = new Button("Conectar");
		HBox hboxButton = new HBox(10);
		hboxButton.setAlignment(Pos.BOTTOM_RIGHT);
		hboxButton.getChildren().add(button);
		grid.add(hboxButton, 1, 4);
		
		

		final Text actionTarget = new Text();
		grid.add(actionTarget, 1, 6);
		
		Scene scene2 = new Scene(new GridPane(), 300, 300);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String email, password;
				email = emailTextField.getText();
				password = passwordTextField.getText();
				emailService = new EmailConnectionService(email, password);
				boolean result = emailService.Login();
				if (result){
					actionTarget.setFill(Color.DARKSEAGREEN);
					actionTarget.setText("Login was successful.");	
					primaryStage.setScene(scene2);
				} else{
					Calendar cal = Calendar.getInstance();
			        SimpleDateFormat sdf = new SimpleDateFormat("SSS");
			        String currentTime = sdf.format(cal.getTime());
			        
					actionTarget.setFill(Color.FIREBRICK);
					actionTarget.setText("Failed to login @ " + currentTime);
				}
				
			}
		});
		
		Scene scene = new Scene(grid, 300, 275);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	

	public static void main(String[] args) {
		launch(args);
	}
}