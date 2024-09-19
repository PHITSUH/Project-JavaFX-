package menus;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import data.Join;
import data.Juice;
import data.TransactionDetail;
import data.TransactionHeader;
import data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import util.Connect;
import util.Factory;

public class AdminPage {
	User user;
	Stage primaryStage;
	ArrayList<TransactionDetail> tdList = new ArrayList<>();
	ArrayList<TransactionHeader> thList = new ArrayList<>();
	ArrayList<Juice> juiceList = new ArrayList<>();
	ArrayList<Join> joinList = new ArrayList<>();

	public AdminPage(User user, Stage primaryStage) {
		this.user = user;
		this.primaryStage = primaryStage;
		getTransactionHeader();
		getTransactionDetail();
		getJuice();
		getJoinList();
	}

	private Connect connect = Connect.getInstance();

	public void getTransactionHeader() {

		String query = "SELECT * FROM transactionheader";
		connect.rs = connect.execQuery(query);
		try {
			while (connect.rs.next()) {

				String username = connect.rs.getString("Username");
				String id = connect.rs.getString("TransactionId");
				String paymentType = connect.rs.getString("PaymentType");
				thList.add(new TransactionHeader(id, username, paymentType));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getTransactionDetail() {
		String query = "SELECT * FROM transactiondetail";
		connect.rs = connect.execQuery(query);
		try {
			while (connect.rs.next()) {

				String transactionId = connect.rs.getString("TransactionId");
				String juiceId = connect.rs.getString("JuiceId");
				int paymentType = Integer.parseInt(connect.rs.getString("Quantity"));
				tdList.add(new TransactionDetail(transactionId, juiceId, paymentType));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getJuice() {
		String query = "SELECT * FROM msjuice";
		connect.rs = connect.execQuery(query);
		try {
			while (connect.rs.next()) {

				String name = connect.rs.getString("JuiceName");
				String juiceId = connect.rs.getString("JuiceId");
				String desc = connect.rs.getString("JuiceDescription");
				int price = Integer.parseInt(connect.rs.getString("Price"));
				juiceList.add(new Juice(juiceId, name, desc, price));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getJoinList() {
		String query = "SELECT TransactionId, mj.JuiceId, JuiceName, Quantity FROM transactiondetail td JOIN msjuice mj ON mj.JuiceId = td.JuiceId ";
		connect.rs = connect.execQuery(query);
		try {
			while (connect.rs.next()) {
				String transactionId = connect.rs.getString("TransactionId");
				String juiceId = connect.rs.getString("JuiceId");
				String name = connect.rs.getString("JuiceName");
				int quantity = Integer.parseInt(connect.rs.getString("Quantity"));
				joinList.add(new Join(transactionId, juiceId, name, quantity));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addJuice(String id, String name, String desc, int price) {
		String query = "INSERT INTO msjuice VALUES(?, ?, ?, ?)";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setString(1, id);
			ps.setString(2, name);
			ps.setInt(3, price);
			ps.setString(4, desc);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateJuice(String id, int price) {
		String query = "UPDATE msjuice Price = ? WHERE JuiceId = ?";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setInt(1, price);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteJuice(String id) {
		String query = "DELETE FROM msjuice WHERE JuiceId = ?";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setString(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteTransactionDetail(String id) {
		String query = "DELETE FROM transactiondetail WHERE JuiceId = ?";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setString(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	ObservableList<Join> joinObs;

	BorderPane parent;
	MenuBar menuBar;
	Menu dashboard;
	Menu logout;
	MenuItem logoutMenu;
	MenuItem viewTransaction;
	MenuItem manageProducts;
	HBox horiBox;
	VBox mainBox;
	Factory f = new Factory();
	Label viewLabel;
	TableView<TransactionHeader> tableTH;
	ObservableList<TransactionHeader> thObs;
	TableColumn<TransactionHeader, String> idCol;
	TableColumn<TransactionHeader, String> paymentTypeCol;
	TableColumn<TransactionHeader, String> usernameCol;
	TableView<Join> tableJoin;
	TableColumn<Join, String> transactionIdCol;
	TableColumn<Join, String> juiceIdCol;
	TableColumn<Join, String> juiceNameCol;
	TableColumn<Join, Integer> quantityCol;

	public void initViewTransaction() {
		parent = new BorderPane();
		menuBar = new MenuBar();
		dashboard = new Menu("Admin's Dashboard");
		logout = new Menu("Logout");
		logoutMenu = new MenuItem("Logout from admin");
		viewTransaction = new MenuItem("View Transaction");
		manageProducts = new MenuItem("Manage Products");
		dashboard.getItems().addAll(viewTransaction, manageProducts);
		logout.getItems().add(logoutMenu);
		menuBar.getMenus().addAll(dashboard, logout);
		horiBox = new HBox(menuBar);
		mainBox = new VBox();
		parent.setTop(horiBox);
		parent.setCenter(mainBox);
		viewLabel = f.createLabel("View Transaction");

	}

	public void initTableView() {
		tableTH = new TableView<>();
		thObs = FXCollections.observableArrayList(thList);

		idCol = new TableColumn<>("Transaction Id");
		idCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
		idCol.setMinWidth(100);

		paymentTypeCol = new TableColumn<>("Payment Type");
		paymentTypeCol.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
		paymentTypeCol.setMinWidth(100);

		usernameCol = new TableColumn<>("Username");
		usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
		usernameCol.setMinWidth(100);

		tableTH.getColumns().addAll(idCol, paymentTypeCol, usernameCol);
		tableTH.setMaxHeight(200);
		tableTH.setMaxWidth(300);
		tableTH.setItems(thObs);
		tableTH.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tableTH.setEditable(false);

		tableJoin = new TableView<>();

		transactionIdCol = new TableColumn<>("Transaction Id");
		transactionIdCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
		transactionIdCol.setMinWidth(100);

		juiceIdCol = new TableColumn<>("Juice Id");
		juiceIdCol.setCellValueFactory(new PropertyValueFactory<>("juiceId"));
		juiceIdCol.setMinWidth(100);

		juiceNameCol = new TableColumn<>("Juice Name");
		juiceNameCol.setCellValueFactory(new PropertyValueFactory<>("juiceName"));
		juiceNameCol.setMinWidth(100);

		quantityCol = new TableColumn<>("Quantity");
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantityCol.setMinWidth(100);

		tableJoin.getColumns().addAll(transactionIdCol, juiceIdCol, juiceNameCol, quantityCol);
		tableJoin.setMaxHeight(200);
		tableJoin.setMaxWidth(400);
		tableJoin.setPlaceholder(f.createLabel("Click on a transation header to view the transaction detail"));

	}

	public void handleTableTH() {
		int index = tableTH.getSelectionModel().getSelectedIndex();
		TransactionHeader th = tableTH.getItems().get(index);
		ArrayList<Join> result = new ArrayList<>();
		for (int i = 0; i < joinList.size(); i++) {
			if (joinList.get(i).getTransactionId().equals(th.getTransactionId())) {
				result.add(joinList.get(i));
			}
		}
		joinObs = FXCollections.observableArrayList(result);
		tableJoin.setItems(joinObs);
	}

	public void showViewTransaction() {

		initViewTransaction();
		initTableView();
		horiBox.setAlignment(Pos.TOP_LEFT);
		mainBox.setAlignment(Pos.TOP_CENTER);
		horiBox.setHgrow(menuBar, Priority.ALWAYS);
		horiBox.setMaxWidth(1000);

		viewLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));

		mainBox.getChildren().addAll(viewLabel, tableTH, tableJoin);
		mainBox.setMargin(viewLabel, new Insets(100, 0, 20, 0));
		mainBox.setMargin(tableTH, new Insets(0, 0, 20, 0));

		Scene scene = new Scene(parent, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

		tableTH.setOnMouseClicked(e -> {
			handleTableTH();
		});

		logoutMenu.setOnAction(e -> {
			LoginPage lm = new LoginPage(primaryStage);
			lm.loadLogin();
			return;
		});

		manageProducts.setOnAction(e -> {
			showManageProducts();
		});
	}

	TableView<Juice> tableJuice;
	ObservableList<Juice> juiceObs;

	Label manageLabel;
	HBox editBox;
	VBox textBox;
	Label comboLabel;
	Label priceLabel;
	Label productLabel;
	VBox inputBox;
	ComboBox<String> juiceCombo;
	Spinner<Integer> priceSpinner;
	TextField nameField;
	TextArea descField;
	VBox buttonBox;
	Button insertButton;
	Button updateButton;
	Button deleteButton;

	public void initManageProducts() {
		parent = new BorderPane();
		menuBar = new MenuBar();
		dashboard = new Menu("Admin's Dashboard");
		logout = new Menu("Logout");
		logoutMenu = new MenuItem("Logout from admin");
		viewTransaction = new MenuItem("View Transaction");
		manageProducts = new MenuItem("Manage Products");
		dashboard.getItems().addAll(viewTransaction, manageProducts);
		logout.getItems().add(logoutMenu);
		menuBar.getMenus().addAll(dashboard, logout);
		horiBox = new HBox(menuBar);
		mainBox = new VBox();
		parent.setTop(horiBox);
		parent.setCenter(mainBox);
		viewLabel = f.createLabel("View Transaction");

		manageLabel = f.createLabel("Manage Products");
		editBox = new HBox();
		textBox = new VBox();
		comboLabel = new Label("Product ID to delete/update: ");
		priceLabel = new Label("Price: ");
		productLabel = new Label("Product Name");
		textBox.getChildren().addAll(comboLabel, priceLabel, productLabel);
		inputBox = new VBox();
		juiceCombo = new ComboBox<>();

		for (int i = 0; i < juiceList.size(); i++) {
			juiceCombo.getItems().add(juiceList.get(i).getJuiceId());
		}
		priceSpinner = new Spinner<>(0, 100000, 10000, 100);
		nameField = new TextField();
		descField = new TextArea();
		inputBox.getChildren().addAll(juiceCombo, priceSpinner, nameField, descField);
		buttonBox = new VBox();
		insertButton = new Button("Insert Juice");
		updateButton = new Button("Update Price");
		deleteButton = new Button("Remove Juice");
		buttonBox.getChildren().addAll(insertButton, updateButton, deleteButton);

		editBox.getChildren().addAll(textBox, inputBox, buttonBox);
		mainBox.getChildren().addAll(manageLabel, tableJuice, editBox);
	}

	public void handleDeleteButton() {
		if (juiceCombo.getValue().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Please fill all the required field");
			alert.show();
			alert.setOnCloseRequest(event -> {
	            System.out.println("Alert closed!");
	        });
		} else {
			String id = juiceCombo.getValue();
			System.out.println(id);
			for (int i = 0; i < juiceList.size(); i++) {
				if (juiceList.get(i).getJuiceId().equals(id)) {
					deleteTransactionDetail(id);
					deleteJuice(id);
					juiceList.remove(i);
					showManageProducts();
					break;
				}
			}
		}
	}

	public void handleUpdateButton() {
		if (juiceCombo.getValue().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Please fill all the required field");
			alert.show();
		} else {
			String id = juiceCombo.getValue();
			int price = priceSpinner.getValue();
			updateJuice(id, price);
			for (int i = 0; i < juiceList.size(); i++) {
				if (juiceList.get(i).getJuiceId().equals(id)) {
					juiceList.get(i).setPrice(price);
					break;
				}
			}
			showManageProducts();
		}
	}

	public void handleInsertButton() {
		if (nameField.getText().isEmpty() || descField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Please fill all the required field");
			alert.show();
		} else if (descField.getText().length() < 10 || descField.getText().length() > 100) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Description is not right");
			alert.show();
		} else {
			String name = nameField.getText();
			String desc = descField.getText();
			int price = priceSpinner.getValue();
			String id = String.format("JU%03d", juiceList.size() + 1);
			addJuice(id, name, desc, price);
			juiceList.add(new Juice(id, name, desc, price));
			showManageProducts();
		}
	}

	public void showManageProducts() {
		tableJuice = new TableView<>();
		juiceObs = FXCollections.observableArrayList(juiceList);
		TableColumn<Juice, String> juiceIdCol = new TableColumn<>("Juice Id");
		idCol.setCellValueFactory(new PropertyValueFactory<>("juiceId"));
		idCol.setMinWidth(150);

		TableColumn<Juice, String> juiceNameCol = new TableColumn<>("Juice Name");
		juiceNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		juiceNameCol.setMinWidth(150);

		TableColumn<Juice, Integer> priceCol = new TableColumn<>("Price");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceCol.setMinWidth(150);

		TableColumn<Juice, String> descCol = new TableColumn<>("Juice Description");
		descCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
		descCol.setMinWidth(150);

		tableJuice.getColumns().addAll(juiceIdCol, juiceNameCol, priceCol, descCol);
		tableJuice.setMaxHeight(200);
		tableJuice.setMaxWidth(600);
		tableJuice.setItems(juiceObs);
		initManageProducts();

		horiBox.setAlignment(Pos.TOP_LEFT);
		mainBox.setAlignment(Pos.TOP_CENTER);
		horiBox.setHgrow(menuBar, Priority.ALWAYS);
		horiBox.setMaxWidth(1000);
		parent.setCenter(mainBox);

		manageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

		textBox.setMaxWidth(200);
		comboLabel.setWrapText(true);

		textBox.setMargin(comboLabel, new Insets(0, 0, 20, 0));
		textBox.setMargin(priceLabel, new Insets(0, 0, 20, 0));
		textBox.setMargin(productLabel, new Insets(0, 0, 20, 0));

		nameField.setPromptText("Insert product name to be created");
		descField.setPromptText("Insert the new product's text description, min. 10 & max. 100");
		nameField.setPrefWidth(300);
		descField.setPrefWidth(300);
		descField.setPrefHeight(100);
		descField.setWrapText(true);
		inputBox.setMargin(juiceCombo, new Insets(0, 0, 10, 0));
		inputBox.setMargin(priceSpinner, new Insets(0, 0, 10, 0));
		inputBox.setMargin(nameField, new Insets(0, 0, 10, 0));

		insertButton.setPrefHeight(30);
		updateButton.setPrefHeight(30);
		deleteButton.setPrefHeight(30);

		buttonBox.setMargin(insertButton, new Insets(0, 0, 10, 0));
		buttonBox.setMargin(updateButton, new Insets(0, 0, 10, 0));

		editBox.setMargin(textBox, new Insets(0, 20, 0, 0));
		editBox.setMargin(inputBox, new Insets(0, 20, 0, 0));
		editBox.setAlignment(Pos.CENTER);

		mainBox.setMargin(manageLabel, new Insets(100, 0, 10, 0));
		mainBox.setMargin(tableJuice, new Insets(0, 0, 10, 0));

		Scene scene = new Scene(parent, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

		deleteButton.setOnAction(e -> {
			handleDeleteButton();
		});

		updateButton.setOnAction(e -> {
			handleUpdateButton();
		});

		insertButton.setOnAction(e -> {
			handleInsertButton();
		});

		logoutMenu.setOnAction(e -> {
			LoginPage lm = new LoginPage(primaryStage);
			lm.loadLogin();
			return;
		});

		viewTransaction.setOnAction(e -> {
			showViewTransaction();
		});
	}
}
