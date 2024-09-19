package menus;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import data.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.Connect;
import util.Factory;

public class LoginPage {
	private Stage primaryStage;
	ArrayList<User> users = new ArrayList<>();

	public LoginPage(Stage primaryStage) {
		// TODO Auto-generated constructor stub
		getData();
		this.primaryStage = primaryStage;
	}

	private Connect connect = Connect.getInstance();

	public void getData() {
		users.removeAll(users);

		String query = "SELECT * FROM msuser";
		connect.rs = connect.execQuery(query);
		try {
			while (connect.rs.next()) {

				String username = connect.rs.getString("Username");
				String password = connect.rs.getString("Password");
				String role = connect.rs.getString("Role");
				users.add(new User(username, password, role));
			}
			System.out.println(connect.rsm.getColumnCount());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addUser(String username, String password) {
		String query = "INSERT INTO msuser VALUES(?, ?, ?)";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, "Customer");
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	BorderPane root;
	MenuBar menuBar;
	Menu menu;
	MenuItem loginMenu;
	MenuItem registerMenu;

	HBox horiBox;
	VBox mainBox;
	Factory factory = new Factory();
	Text login;
	Text name;
	VBox insertBox;
	HBox usernameBox;
	HBox usernameTextBox;
	Label usernameLabel;
	TextField username;
	HBox passwordBox;
	PasswordField password;
	HBox passwordTextBox;
	Label passwordLabel;
	Label alert;
	Button loginButton;

	public void initLogin() {
		root = new BorderPane();
		menuBar = new MenuBar();
		menu = new Menu("Dashboard");
		loginMenu = new MenuItem("Login");
		registerMenu = new MenuItem("Register");
		menu.getItems().addAll(loginMenu, registerMenu);
		menuBar.getMenus().add(menu);

		horiBox = new HBox(menuBar);
		mainBox = new VBox();
		root.setTop(horiBox);

		horiBox.setAlignment(Pos.TOP_LEFT);
		mainBox.setAlignment(Pos.TOP_CENTER);
		horiBox.setHgrow(menuBar, Priority.ALWAYS);
		horiBox.setMaxWidth(1000);
		login = factory.createText("Login");
		name = factory.createText("NJuice");
		mainBox.getChildren().addAll(login, name);
		mainBox.setSpacing(15);

		insertBox = new VBox();
		usernameBox = new HBox();
		usernameTextBox = new HBox();
		usernameLabel = factory.createLabel("Username");
		usernameTextBox.getChildren().add(usernameLabel);
		username = new TextField();
		usernameBox.getChildren().add(username);
		passwordBox = new HBox();
		password = new PasswordField();
		passwordBox.getChildren().add(password);
		passwordTextBox = new HBox();
		passwordLabel = factory.createLabel("Password");
		passwordTextBox.getChildren().add(passwordLabel);

		alert = new Label("Credentials Failed!");
		loginButton = new Button("Login");
		insertBox.getChildren().addAll(usernameTextBox, usernameBox, passwordTextBox, passwordBox, alert);
		root.setCenter(mainBox);

		mainBox.getChildren().addAll(insertBox, loginButton);
	}

	public void handleLoginButton() {
		if (username.getText().isEmpty() || password.getText().isEmpty()) {
			alert.setVisible(true);
		} else {
			String usernameInput = username.getText();
			String passwordInput = password.getText();

			int checkUsername = 0, checkPassword = 0;
			int index = 0;
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getUsername().equals(usernameInput)) {
					checkUsername++;
					index = i;
					break;
				}
			}
			if (checkUsername == 0) {
				alert.setVisible(true);
			} else {
				if (users.get(index).getPassword().equals(passwordInput)) {
					checkPassword++;
				}
				if (checkPassword == 0) {
					alert.setVisible(true);
				} else {
					if (users.get(index).getRole().equals("Admin")) {
						AdminPage admin = new AdminPage(users.get(index), primaryStage);
						admin.showViewTransaction();
					} else {
						CustomerPage customer = new CustomerPage(primaryStage, users.get(index));
						customer.ShowCustomerPage();
					}
				}
			}
		}
	}

	public void loadLogin() {
		initLogin();
		login.setFont(Font.font("Arial", FontWeight.BOLD, 50));
		name.setStyle("-fx-font: 25 arial;");
		usernameTextBox.setMargin(usernameLabel, new Insets(0, 0, 0, 250));
		username.setPromptText("Enter Username");
		usernameBox.setHgrow(username, Priority.ALWAYS);
		username.setMaxWidth(320);
		usernameBox.setMargin(username, new Insets(0, 0, 25, 250));
		password.setPromptText("Enter Password");
		password.setMaxWidth(320);
		passwordBox.setHgrow(password, Priority.ALWAYS);
		passwordBox.setMargin(password, new Insets(0, 0, 10, 250));
		passwordTextBox.setMargin(passwordLabel, new Insets(0, 0, 0, 250));
		alert.setVisible(false);
		alert.setTextFill(Color.RED);
		insertBox.setMargin(alert, new Insets(0, 0, 30, 250));
		insertBox.setMargin(login, new Insets(100, 0, 0, 0));

		Scene scene = new Scene(root, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

		registerMenu.setOnAction(e -> {
			loadRegister();
		});

		loginButton.setOnAction(e -> {
			handleLoginButton();
		});
	}

	CheckBox agree;
	Text terms;
	HBox termsBox;
	Button registerButton;
	Text register;

	public void initRegister() {
		root = new BorderPane();
		menuBar = new MenuBar();
		menu = new Menu("Dashboard");
		loginMenu = new MenuItem("Login");
		registerMenu = new MenuItem("Register");
		menu.getItems().addAll(loginMenu, registerMenu);
		menuBar.getMenus().add(menu);
		horiBox = new HBox(menuBar);
		mainBox = new VBox();
		root.setTop(horiBox);
		register = factory.createText("Register");
		name = factory.createText("NJuice");
		mainBox.getChildren().addAll(register, name);
		insertBox = new VBox();
		usernameBox = new HBox();
		usernameTextBox = new HBox();
		usernameLabel = factory.createLabel("Username");
		username = new TextField();
		passwordBox = new HBox();
		password = new PasswordField();
		passwordTextBox = new HBox();
		passwordLabel = factory.createLabel("Password");
		agree = new CheckBox();
		terms = factory.createText("I agree to the terms and conditions of NJuice!");
		termsBox = new HBox();
		termsBox.getChildren().addAll(agree, terms);
		alert = new Label("Credentials Failed!");
		insertBox.getChildren().addAll(usernameTextBox, usernameBox, passwordTextBox, passwordBox, termsBox, alert);
		registerButton = new Button("Register");
		root.setCenter(mainBox);
		mainBox.getChildren().addAll(insertBox, registerButton);
	}

	public void handleRegisterButton() {
		if (username.getText().isEmpty() || password.getText().isEmpty() || !agree.isSelected()) {
			alert.setText("Please Input all the field");
			alert.setVisible(true);
		} else {
			String usernameInput = username.getText();
			String passwordInput = password.getText();
			int checkUsername = 0;
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getUsername().equals(usernameInput)) {
					checkUsername++;
					break;
				}
			}
			if (checkUsername != 0) {
				alert.setText("Username is already taken");
				alert.setVisible(true);
			} else {
				users.add(new User(usernameInput, passwordInput, "Customer"));
				addUser(usernameInput, passwordInput);
				CustomerPage customer = new CustomerPage(primaryStage, users.get(users.size() - 1));
				customer.ShowCustomerPage();
			}
		}
	}

	public void loadRegister() {
		initRegister();
		horiBox.setAlignment(Pos.TOP_LEFT);
		mainBox.setAlignment(Pos.TOP_CENTER);
		horiBox.setHgrow(menuBar, Priority.ALWAYS);
		horiBox.setMaxWidth(1000);

		register.setFont(Font.font("Arial", FontWeight.BOLD, 50));
		name.setStyle("-fx-font: 25 arial;");
		mainBox.setSpacing(15);

		usernameTextBox.getChildren().add(usernameLabel);
		usernameTextBox.setMargin(usernameLabel, new Insets(0, 0, 0, 250));
		username.setPromptText("Enter Username");
		usernameBox.setHgrow(username, Priority.ALWAYS);
		username.setMaxWidth(300);
		usernameBox.getChildren().add(username);
		usernameBox.setMargin(username, new Insets(0, 0, 25, 250));
		password.setPromptText("Enter Password");
		password.setMaxWidth(300);
		passwordBox.getChildren().add(password);
		passwordBox.setHgrow(password, Priority.ALWAYS);
		passwordBox.setMargin(password, new Insets(0, 0, 10, 250));
		passwordTextBox.getChildren().add(passwordLabel);
		passwordTextBox.setMargin(passwordLabel, new Insets(0, 0, 0, 250));

		termsBox.setHgrow(terms, Priority.ALWAYS);
		termsBox.setMargin(agree, new Insets(0, 10, 0, 0));
		alert.setVisible(false);
		alert.setTextFill(Color.RED);
		insertBox.setMargin(alert, new Insets(0, 0, 30, 250));
		insertBox.setMargin(termsBox, new Insets(0, 0, 0, 250));
		mainBox.setMargin(login, new Insets(100, 0, 0, 0));

		Scene scene = new Scene(root, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
		loginMenu.setOnAction(e -> {
			loadLogin();
		});

		registerButton.setOnAction(e -> {
			handleRegisterButton();
		});

	}
}
