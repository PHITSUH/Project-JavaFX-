import javafx.application.Application;
import javafx.stage.Stage;
import menus.LoginPage;

public class Main extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		LoginPage login = new LoginPage(primaryStage);
		login.loadLogin();
	}

}