package edu.ucsi.bookkeeper;

import javafx.application.Application;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class BookKeeper extends Application {
	private static final double RESIZE_SCALE_FACTOR = 0.005;

	private final ArrayList<UserItem> userRecords = new ArrayList<>();
	private final ArrayList<BookItem> bookRecords = new ArrayList<>();

	private boolean isUserEditPaneVisible = false;
	private boolean isBookEditPaneVisible = false;

	private StackPane userPane;
	private VBox userTablePane;
	private VBox userEditPane;

	private StackPane bookPane;
	private VBox bookTablePane;
	private VBox bookEditPane;

	ImageView userIcon;
	ImageView activeUserIcon;
	ImageView inactiveUserIcon;

	ImageView bookIcon;
	ImageView activeBookIcon;
	ImageView inactiveBookIcon;

	private double xOffset;
	private double yOffset;

	// This will hold a reference to the main stage
	public static Stage mainStage;

	private int generateId() {
		return  (int) (Math.random() * 900000) + 100000;
	}

	@Override
	public void start(Stage primaryStage) {
		// Remove default window decorations
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		mainStage = primaryStage;

		// Create back button
		Button backButton = new Button();
		backButton.getStyleClass().add("back-button");

		// Import back button icon
		InputStream imageStreamBack =
				getClass().getResourceAsStream("images/back.png");
		if (imageStreamBack != null) {
			Image backImage = new Image(imageStreamBack);
			ImageView backIcon = new ImageView(backImage);
			backIcon.setFitWidth(18);
			backIcon.setFitHeight(18);
			backButton.setGraphic(backIcon);
		} else {
			System.err.println("Unable to load back.png");
		}

		// Create main window title
		Label mainWindowTitle = new Label("Book Keeper");
		mainWindowTitle.getStyleClass().add("window-title");

		// Create title bar
		HBox titleBar = new HBox();
		titleBar.getStyleClass().add("title-bar");
		titleBar.getChildren().addAll(backButton, mainWindowTitle);

		// Import active tab icon for user tab
		InputStream imageStreamActiveUser =
				getClass().getResourceAsStream("images/active.png");
		if (imageStreamActiveUser != null) {
			Image activeUserImage = new Image(imageStreamActiveUser);
			activeUserIcon = new ImageView(activeUserImage);
			activeUserIcon.setFitWidth(10);
			activeUserIcon.setFitHeight(16);
		} else {
			System.err.println("Unable to load active.png");
		}

		// Import inactive tab icon for user tab
		InputStream imageStreamInactiveUser =
				getClass().getResourceAsStream("images/inactive.png");
		if (imageStreamInactiveUser != null) {
			Image inactiveUserImage = new Image(imageStreamInactiveUser);
			inactiveUserIcon = new ImageView(inactiveUserImage);
			inactiveUserIcon.setFitWidth(10);
			inactiveUserIcon.setFitHeight(16);
		} else {
			System.err.println("Unable to load inactive.png");
		}

		// Import user tab icon
		InputStream imageStreamUser =
				getClass().getResourceAsStream("images/user.png");
		if (imageStreamUser != null) {
			Image userImage = new Image(imageStreamUser);
			userIcon = new ImageView(userImage);
			userIcon.setFitWidth(25);
			userIcon.setFitHeight(25);
		} else {
			System.err.println("Unable to load user.png");
		}

		// Create user tab label
		Label userTabLabel = new Label("  Users");

		// Create user tab
		HBox userTab = new HBox();
		userTab.setAlignment(Pos.CENTER_LEFT); // Centers label vertically
		userTab.getStyleClass().addAll("sidebar-tab", "active-tab");
		userTab.getChildren().addAll(activeUserIcon, userIcon, userTabLabel);

		// Import active tab icon for book tab
		InputStream imageStreamActiveBook =
				getClass().getResourceAsStream("images/active.png");
		if (imageStreamActiveBook != null) {
			Image activeBookImage = new Image(imageStreamActiveBook);
			activeBookIcon = new ImageView(activeBookImage);
			activeBookIcon.setFitWidth(10);
			activeBookIcon.setFitHeight(16);
		} else {
			System.err.println("Unable to load active.png");
		}

		// Import inactive tab icon for book tab
		InputStream imageStreamInactiveBook =
				getClass().getResourceAsStream("images/inactive.png");
		if (imageStreamInactiveBook != null) {
			Image inactiveBookImage = new Image(imageStreamInactiveBook);
			inactiveBookIcon = new ImageView(inactiveBookImage);
			inactiveBookIcon.setFitWidth(10);
			inactiveBookIcon.setFitHeight(16);
		} else {
			System.err.println("Unable to load inactive.png");
		}

		// Import book tab icon
		InputStream imageStreamBook =
				getClass().getResourceAsStream("images/book.png");
		if (imageStreamBook != null) {
			Image bookImage = new Image(imageStreamBook);
			bookIcon = new ImageView(bookImage);
			bookIcon.setFitWidth(25);
			bookIcon.setFitHeight(25);
		} else {
			System.err.println("Unable to load book.png");
		}

		// Create book tab label
		Label bookTabLabel = new Label("  Books");

		// Create book tab
		HBox bookTab = new HBox();
		bookTab.setAlignment(Pos.CENTER_LEFT); // Centers label vertically
		bookTab.getStyleClass().add("sidebar-tab");
		bookTab.getChildren().addAll(inactiveBookIcon, bookIcon, bookTabLabel);

		// Create sidebar
		VBox sideBar = new VBox();
		sideBar.getStyleClass().add("side-bar");
		sideBar.getChildren().addAll(userTab, bookTab);

		// Create left pane
		VBox leftPane = new VBox();
		leftPane.getStyleClass().add("left-pane");
		leftPane.getChildren().addAll(titleBar, sideBar);

		// Create border window
		StackPane root = new StackPane();

		// Create main window in border window
		HBox appWindow = new HBox();
		appWindow.getStyleClass().addAll("main-window", "round-corners");

		// Create close window button
		Button closeButton = new Button();
		closeButton.getStyleClass().addAll("window-button", "close-button");
		
		// Create minimize window button
		Button minimizeButton = new Button();
		minimizeButton.getStyleClass().add("window-button");

		// Import minimize window button icon
		InputStream imageStreamMinimize =
				getClass().getResourceAsStream("images/minimize.png");
		if (imageStreamMinimize != null) {
			Image minimizeImage = new Image(imageStreamMinimize);
			ImageView minimizeIcon = new ImageView(minimizeImage);
			minimizeIcon.setFitWidth(14);
			minimizeIcon.setFitHeight(14);
			minimizeButton.setGraphic(minimizeIcon);
		} else {
			System.err.println("Unable to load minimize.png");
		}

		// Set action for minimize window button
		minimizeButton.setOnAction(event -> primaryStage.setIconified(true));

		// Create maximize window button
		Button maximizeButton = new Button();
		maximizeButton.getStyleClass().add("window-button");
		InputStream imageStreamMaximize =
				getClass().getResourceAsStream("images/maximize.png");
		if (imageStreamMaximize != null) {
			Image maximizeImage = new Image(imageStreamMaximize);
			ImageView maximizeIcon = new ImageView(maximizeImage);
			maximizeIcon.setFitWidth(14);
			maximizeIcon.setFitHeight(14);
			maximizeButton.setGraphic(maximizeIcon);
		} else {
			System.err.println("Unable to load maximize.png");
		}

		// Set action for maximize button
		maximizeButton.setOnAction(event -> {
			if (primaryStage.isMaximized()) {
				primaryStage.setMaximized(false);
				primaryStage.setWidth(950);
				primaryStage.setHeight(702);
				root.setStyle("-fx-padding: 10;"); // Adds resizing border
				appWindow.getStyleClass().add("round-corners");
				closeButton.getStyleClass().remove("close-maximized");
			} else {
				primaryStage.setMaximized(true);
				primaryStage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
				root.setStyle("-fx-padding: 0;"); // Removes resizing border
				appWindow.getStyleClass().remove("round-corners");
				closeButton.getStyleClass().add("close-maximized");
			}
		});

		// Import close window button icon
		InputStream imageStreamClose =
				getClass().getResourceAsStream("images/close.png");
		if (imageStreamClose != null) {
			Image closeImage = new Image(imageStreamClose);
			ImageView closeIcon = new ImageView(closeImage);
			closeIcon.setFitWidth(14);
			closeIcon.setFitHeight(14);
			closeButton.setGraphic(closeIcon);
		} else {
			System.err.println("Unable to load close.png");
		}
		closeButton.setOnAction(event -> primaryStage.close());

		// Create window controls pane
		HBox windowControlsPane = new HBox();
		windowControlsPane.setAlignment(Pos.TOP_RIGHT); // Aligns window controls to top right
		windowControlsPane.getChildren().addAll(minimizeButton, maximizeButton, closeButton);

		// Create main pane
		StackPane mainPane = new StackPane();
		mainPane.getStyleClass().add("main-pane");

		// Create user pane
		this.userPane = new StackPane();

		// Create user view pane
		VBox userViewPane = new VBox();
		userViewPane.getStyleClass().add("user-view-pane");
		userViewPane.setFillWidth(true);

		// Create title for user view
		Label userViewPaneTitle = new Label("Users");
		userViewPaneTitle.getStyleClass().add("main-title");
		HBox userViewTitlePane = new HBox(userViewPaneTitle);

		// Create import icon
		ImageView importUserIcon = null;
		InputStream imageStreamImportUser =
				getClass().getResourceAsStream("images/import.png");
		if (imageStreamImportUser != null) {
			Image importUserImage = new Image(imageStreamImportUser);
			importUserIcon = new ImageView(importUserImage);
			importUserIcon.setFitWidth(26);
			importUserIcon.setFitHeight(26);
			importUserIcon.getStyleClass().add("file-pane-icon");
		} else {
			System.err.println("Unable to load import.png");
		}

		// Create labels for text lines
		Label importUserLabel = new Label("Import");
		importUserLabel.getStyleClass().add("file-pane-label");
		Label importUserInfoLabel = new Label("From text file");
		importUserInfoLabel.getStyleClass().add("file-pane-info-label");

		// Create HBox to hold icon and labels
		HBox importUserButtonContent = new HBox();
		importUserButtonContent.getChildren().addAll(
				importUserIcon,
				new VBox(importUserLabel, importUserInfoLabel)
		);
		importUserButtonContent.setAlignment(Pos.CENTER_LEFT);

		// Create button and set its content to the HBox
		Button importUserButton = new Button();
		importUserButton.getStyleClass().add("file-button");
		importUserButton.setGraphic(importUserButtonContent);
		importUserButton.setOnAction(event -> openImportUserDialog());

		// Create export icon
		ImageView exportUserIcon = null;
		InputStream imageStreamExportUser =
				getClass().getResourceAsStream("images/export.png");
		if (imageStreamExportUser != null) {
			Image exportUserImage = new Image(imageStreamExportUser);
			exportUserIcon = new ImageView(exportUserImage);
			exportUserIcon.setFitWidth(26);
			exportUserIcon.setFitHeight(26);
			exportUserIcon.getStyleClass().add("fila-pane-icon");
		} else {
			System.err.println("Unable to load export.png");
		}

		// Create labels for text lines
		Label exportUserLabel = new Label("Export");
		exportUserLabel.getStyleClass().add("file-pane-label");
		Label exportUserInfoLabel = new Label("To text file");
		exportUserInfoLabel.getStyleClass().add("file-pane-info-label");

		// Create HBox to hold icon and labels
		HBox exportUserButtonContent = new HBox();
		exportUserButtonContent.getChildren().addAll(
				exportUserIcon,
				new VBox(exportUserLabel, exportUserInfoLabel)
		);
		exportUserButtonContent.setAlignment(Pos.CENTER_LEFT);

		// Create button and set its content to the HBox
		Button exportUserButton = new Button();
		exportUserButton.getStyleClass().add("file-button");
		exportUserButton.setGraphic(exportUserButtonContent);

		exportUserButton.setOnAction(event -> exportToUserFile());

		// Create HBox for Import and Export buttons
		HBox userFilePane = new HBox();
		userFilePane.getStyleClass().add("file-pane");
		userFilePane.getChildren().addAll(importUserButton, exportUserButton);
		userFilePane.setAlignment(Pos.BASELINE_RIGHT);

		// Create table
		userTablePane = new VBox();
		userTablePane.getStyleClass().add("table-pane");

		Region plusEmptySpace = new Region();
		plusEmptySpace.setPrefHeight(3);

		// Create plus pane
		HBox plusButtonPane = new HBox();
		plusButtonPane.getStyleClass().add("view-row");

		// Create plus button
		Button plusButton = new Button();
		plusButton.getStyleClass().add("plus-button");

		// Create add icon
		ImageView AddIcon;
		InputStream imageStreamAdd =
				getClass().getResourceAsStream("images/add.png");
		if (imageStreamAdd != null) {
			Image AddImage = new Image(imageStreamAdd);
			AddIcon = new ImageView(AddImage);
			AddIcon.setFitWidth(26);
			AddIcon.setFitHeight(26);
			AddIcon.getStyleClass().add("plus-button");
			plusButton.setGraphic(AddIcon);
		} else {
			System.err.println("Unable to load add.png");
		}

		plusButton.setStyle("-fx-background-color: transparent;");

		// Add plus button to plus button pane
		plusButtonPane.getChildren().add(plusButton);
		plusButtonPane.setAlignment(Pos.CENTER);
		plusButtonPane.setOnMouseClicked(event -> {
			if (event.getClickCount() == 1) { addUser(); }
		});

		// Adds empty space after table
		Region userEmptySpace = new Region();
		userEmptySpace.setPrefHeight(40);

		// Create scroll pane
		VBox inUserScrollPane = new VBox(
				userFilePane,
				plusButtonPane,
				plusEmptySpace,
				userTablePane,
				userEmptySpace
		);
		inUserScrollPane.getStyleClass().add("scroll-content");
		ScrollPane userScrollPane = new ScrollPane(inUserScrollPane);
		userScrollPane.setFitToWidth(true); // Match parent width

		// Adds scrollPane to userViewPane
		userViewPane.getChildren().addAll(userViewTitlePane, userScrollPane);

		// Create user edit pane
		userEditPane = new VBox();
		userEditPane.getStyleClass().add("edit-pane");
		userEditPane.setFillWidth(true);
		userEditPane.setVisible(false);

		// Add user content pane and user edit pane to user pane
		userPane.getChildren().addAll(userViewPane, userEditPane);

		// Set action for user tab
		userTab.setOnMouseClicked(event -> {
			mainPane.getChildren().clear();
			mainPane.getChildren().add(userPane);
			userTab.getStyleClass().setAll("sidebar-tab", "active-tab");
			bookTab.getStyleClass().setAll("sidebar-tab");
			userTab.getChildren().clear();
			userTab.getChildren().addAll(activeUserIcon, userIcon, userTabLabel);
			bookTab.getChildren().clear();
			bookTab.getChildren().addAll(inactiveBookIcon, bookIcon, bookTabLabel);
		});

		// Create book pane
		this.bookPane = new StackPane();

		// Create book content pane
		VBox bookViewPane = new VBox();
		bookViewPane.getStyleClass().add("user-view-pane");
		bookViewPane.setFillWidth(true);

		// Create title for book view
		Label bookViewPaneTitle = new Label("Books");
		bookViewPaneTitle.getStyleClass().add("main-title");
		HBox bookViewTitlePane = new HBox(bookViewPaneTitle);

		// Create Add button
		Button addBookButton = new Button("Add book");
		addBookButton.getStyleClass().add("important-button");
		addBookButton.setOnAction(event -> addBook());

		// Create HBox for Add button
		HBox addBookPane = new HBox();
		addBookPane.getStyleClass().add("button-pane");
		addBookPane.getChildren().add(addBookButton);
		addBookPane.setAlignment(Pos.CENTER_LEFT);

		// Create import icon
		ImageView importBookIcon = null;
		InputStream imageStreamImportBook =
				getClass().getResourceAsStream("images/import.png");
		if (imageStreamImportBook != null) {
			Image importBookImage = new Image(imageStreamImportBook);
			importBookIcon = new ImageView(importBookImage);
			importBookIcon.setFitWidth(26);
			importBookIcon.setFitHeight(26);
			importBookIcon.getStyleClass().add("file-pane-icon");
		} else {
			System.err.println("Unable to load import.png");
		}

		// Create labels for text lines
		Label importBookLabel = new Label("Import");
		importBookLabel.getStyleClass().add("file-pane-label");
		Label importBookInfoLabel = new Label("From text file");
		importBookInfoLabel.getStyleClass().add("file-pane-info-label");

		// Create HBox to hold icon and labels
		HBox importBookButtonContent = new HBox();
		importBookButtonContent.getChildren().addAll(
				importBookIcon,
				new VBox(importBookLabel, importBookInfoLabel));
		importBookButtonContent.setAlignment(Pos.CENTER_LEFT);

		// Create button and set its content to the HBox
		Button importBookButton = new Button();
		importBookButton.getStyleClass().add("file-button");
		importBookButton.setGraphic(importBookButtonContent);
		importBookButton.setOnAction(event -> openImportBookDialog());

		// Create export icon
		ImageView exportBookIcon = null;
		InputStream imageStreamExportBook =
				getClass().getResourceAsStream("images/export.png");
		if (imageStreamExportBook != null) {
			Image exportBookImage = new Image(imageStreamExportBook);
			exportBookIcon = new ImageView(exportBookImage);
			exportBookIcon.setFitWidth(26);
			exportBookIcon.setFitHeight(26);
			exportBookIcon.getStyleClass().add("fila-pane-icon");
		} else {
			System.err.println("Unable to load export.png");
		}

		// Create labels for text lines
		Label exportBookLabel = new Label("Export");
		exportBookLabel.getStyleClass().add("file-pane-label");
		Label exportBookInfoLabel = new Label("To text file");
		exportBookInfoLabel.getStyleClass().add("file-pane-info-label");

		// Create HBox to hold icon and labels
		HBox exportBookButtonContent = new HBox();
		exportBookButtonContent.getChildren().addAll(
				exportBookIcon,
				new VBox(exportBookLabel, exportBookInfoLabel)
		);
		exportBookButtonContent.setAlignment(Pos.CENTER_LEFT);

		// Create button and set its content to the HBox
		Button exportBookButton = new Button();
		exportBookButton.getStyleClass().add("file-button");
		exportBookButton.setGraphic(exportBookButtonContent);
		exportBookButton.setOnAction(event -> exportToBookFile());

		// Create HBox for Import and Export buttons
		HBox bookFilePane = new HBox();
		bookFilePane.getStyleClass().add("file-pane");
		bookFilePane.getChildren().addAll(importBookButton, exportBookButton);
		bookFilePane.setAlignment(Pos.BASELINE_RIGHT);

		// Create table
		bookTablePane = new VBox();
		bookTablePane.getStyleClass().add("table-pane");

		// Adds empty space after table
		Region bookEmptySpace = new Region();
		bookEmptySpace.setPrefHeight(40);

		// Create scroll pane
		VBox inBookScrollPane = new VBox(bookFilePane, addBookPane,
				bookTablePane, bookEmptySpace);
		inBookScrollPane.getStyleClass().add("scroll-content");
		ScrollPane bookScrollPane = new ScrollPane(inBookScrollPane);
		bookScrollPane.setFitToWidth(true); // Match parent width

		// Adds scrollPane to bookViewPane
		bookViewPane.getChildren().addAll(bookViewTitlePane, bookScrollPane);

		// Create book edit pane
		bookEditPane = new VBox();
		bookEditPane.getStyleClass().add("edit-pane");
		bookEditPane.setFillWidth(true);
		bookEditPane.setVisible(false);

		// Add book content pane and book edit pane to book pane
		bookPane.getChildren().addAll(bookViewPane, bookEditPane);

		// Set action for book tab
		bookTab.setOnMouseClicked(event -> {
			mainPane.getChildren().clear();
			mainPane.getChildren().add(bookPane);
			bookTab.getStyleClass().setAll("sidebar-tab", "active-tab");
			userTab.getStyleClass().setAll("sidebar-tab");
			bookTab.getChildren().clear();
			bookTab.getChildren().addAll(activeBookIcon, bookIcon, bookTabLabel);
			userTab.getChildren().clear();
			userTab.getChildren().addAll(inactiveUserIcon, userIcon, userTabLabel);
		});

		StackPane.setAlignment(userPane, Pos.TOP_LEFT);
		StackPane.setAlignment(bookPane, Pos.TOP_LEFT);

		// App starts with user pane visible
		mainPane.getChildren().add(userPane);

		// Create right pane
		VBox rightPane = new VBox();
		rightPane.getStyleClass().add("right-pane");
		rightPane.getChildren().addAll(windowControlsPane, mainPane);


		appWindow.getChildren().addAll(leftPane, rightPane);
		HBox.setHgrow(rightPane, Priority.ALWAYS);

		// Create border window
		root.getChildren().add(appWindow);

		// Create scene
		Scene scene = new Scene(root, 950, 702);
		scene.setFill(Color.TRANSPARENT);

		// Null checker for styles.css
		URL cssUrl = getClass().getResource("styles.css");
		if (cssUrl != null) {
			scene.getStylesheets().add(cssUrl.toExternalForm());
		} else {
			System.err.println("Unable to find styles.css");
		}

		// Import main app icon
		String[] iconFiles = new String[]{
				"images/app-24.png",
				"images/app-32.png",
				"images/app-48.png",
				"images/app-64.png",
				"images/app-72.png"
		};

		// Null checker for main app icon
		for (String iconFile : iconFiles) {
			InputStream imageStream = getClass().getResourceAsStream(iconFile);
			if (imageStream != null) {
				Image appIcon = new Image(imageStream);
				primaryStage.getIcons().add(appIcon);
			} else {
				System.err.println("Unable to load " + iconFile);
			}
		}

		// Set scene
		primaryStage.setScene(scene);
		primaryStage.show();

		// Set minimum window size
		primaryStage.setMinWidth(850);
		primaryStage.setMinHeight(298);

		// Resize window
		ResizeHelper.ResizeListener resizeListener =
				new ResizeHelper.ResizeListener(primaryStage);
		root.setOnMousePressed(resizeListener::onMousePressed);
		root.setOnMouseDragged(resizeListener::onMouseDragged);
		root.setOnMouseMoved(resizeListener::onMouseMoved);
		root.setOnMouseReleased(resizeListener::onMouseReleased);

		windowControlsPane.setOnMousePressed(resizeListener::onMousePressed);
		windowControlsPane.setOnMouseDragged(resizeListener::onMouseDragged);
		windowControlsPane.setOnMouseMoved(resizeListener::onMouseMoved);

		// Drag window
		windowControlsPane.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});

		windowControlsPane.setOnMouseDragged(event -> {
			if (ResizeHelper.isNotResizing()) {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			}
		});

		leftPane.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});

		leftPane.setOnMouseDragged(event -> {
			if (ResizeHelper.isNotResizing()) {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			}
		});

		userViewTitlePane.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});

		userViewTitlePane.setOnMouseDragged(event -> {
			if (ResizeHelper.isNotResizing()) {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			}
		});

		bookViewTitlePane.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});

		bookViewTitlePane.setOnMouseDragged(event -> {
			if (ResizeHelper.isNotResizing()) {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			}
		});

		// Import items from files on application startup
		String userFilePath = "src/main/resources/edu/ucsi/bookkeeper/files/Users.txt";
		File defaultUserFile = new File(userFilePath);
		importFromUserFile(defaultUserFile);

		String bookFilePath = "src/main/resources/edu/ucsi/bookkeeper/files/Books.txt";
        File defaultBookFile = new File(bookFilePath);
        importFromBookFile(defaultBookFile);
	}

	private static class UserItem {
		private final StringProperty firstName;
		private final StringProperty lastName;
		private final IntegerProperty id;
		private final StringProperty email;
		private final StringProperty gender;

		public UserItem(String firstName, String lastName, int id,
		                String email, String gender) {
			this.firstName = new SimpleStringProperty(firstName);
			this.lastName = new SimpleStringProperty(lastName);
			this.id = new SimpleIntegerProperty(id);
			this.email = new SimpleStringProperty(email);
			this.gender = new SimpleStringProperty(gender);
		}

		public String getFirstName() {
			return firstName.get();
		}
		public String getLastName() {
			return lastName.get();
		}
		public int getId() {
			return id.get();
		}
		public String getEmail() {
			return email.get();
		}
		public String getGender() { return gender.get(); }

		public void setFirstName(String firstName) {
			this.firstName.set(firstName);
		}
		public void setLastName(String lastName) {
			this.lastName.set(lastName);
		}
		public void setId(int id) {
			this.id.set(id);
		}
		public void setEmail(String email) {
			this.email.set(email);
		}
		public void setGender(String gender) { this.gender.set(gender); }
	}

	private void toggleUserEditPane(StackPane userPane) {
		VBox userViewPane = (VBox) userPane.getChildren().get(0);
		VBox userEditPane = (VBox) userPane.getChildren().get(1);

		if (!isUserEditPaneVisible) {
			userEditPane.setVisible(true);

			TranslateTransition slideIn = new TranslateTransition(
					Duration.millis(1000), userEditPane);
			slideIn.setFromX(userViewPane.getWidth());
			slideIn.setToX(0);
			slideIn.play();
			isUserEditPaneVisible = true;

			// Clear existing text fields
			clearUserEditFields(userEditPane);
		} else {
			TranslateTransition slideOut = new TranslateTransition(
					Duration.millis(1000), userEditPane);
			slideOut.setFromX(0);
			slideOut.setToX(userViewPane.getWidth());
			slideOut.setOnFinished(e -> {
				userEditPane.setVisible(false);

				// Clear text fields after animation finishes
				clearUserEditFields(userEditPane);
			});
			slideOut.play();
			isUserEditPaneVisible = false;
			updateUserTablePane(userRecords);
		}
	}

	private void clearUserEditFields(VBox userEditPane) {
		for (Node node : userEditPane.getChildren()) {
			if (node instanceof TextField) {
				((TextField) node).clear();
			}
		}
	}

	private void populateUserEditFields(
			VBox userEditFieldsPane,
			UserItem userItem,
			boolean isEdit) {
		// Create text field for first name
		TextField firstNameField = new TextField();
		firstNameField.getStyleClass().add("text-field");
		firstNameField.setText(userItem.getFirstName());
		
		// Create text field for last name
		TextField lastNameField = new TextField();
		lastNameField.getStyleClass().add("text-field");
		lastNameField.setText(userItem.getLastName());

		// Create text field for user ID
		TextField userIdField = new TextField();
		userIdField.getStyleClass().add("text-field");
		userIdField.setText(String.valueOf(userItem.getId()));

		// Create text field for email
		TextField emailField = new TextField();
		emailField.getStyleClass().add("text-field");
		emailField.setText(String.valueOf(userItem.getEmail()));

		// Create gender dropdown label
		Label genderDropdownLabel = new Label();
		if (isEdit) {
			genderDropdownLabel.setText(userItem.getGender());
		} else {
			genderDropdownLabel.setText("Male");
		}

		// Create spacer between gender dropdown label & button
		Region genderDropdownSpacer = new Region();
		HBox.setHgrow(genderDropdownSpacer, Priority.ALWAYS);

		// Import arrow icon for gender dropdown button
		ImageView arrowIcon = null;
		InputStream imageStreamArrow =
				getClass().getResourceAsStream("images/arrow.png");
		if (imageStreamArrow != null) {
			Image arrowImage = new Image(imageStreamArrow);
			arrowIcon = new ImageView(arrowImage);
			arrowIcon.setFitWidth(10);
			arrowIcon.setFitHeight(10);
		} else {
			System.err.println("Unable to load arrow.png");
		}

		// Create gender dropdown button
		HBox genderButton = new HBox();
		genderButton.setAlignment(Pos.CENTER_LEFT);
		genderButton.getStyleClass().add("dropdown-button");
		genderButton.getChildren().addAll(genderDropdownLabel, genderDropdownSpacer, arrowIcon);

		// Display gender dropdown list on click
		genderButton.setOnMouseClicked(e -> {
			// Create popup for dropdown list
			Popup popup = new Popup();
			popup.setAutoHide(true);

			// Create male option in dropdown list
			Button maleOption = new Button("Male");
			maleOption.setAlignment(Pos.CENTER_LEFT); // Default is centered
			maleOption.getStyleClass().addAll("dropdown-option");

			// Import active icon for male option
			ImageView activeMaleOptionIcon = null;
			InputStream imageStreamActiveMaleOption
					= getClass().getResourceAsStream("images/active.png");
			if (imageStreamActiveMaleOption != null) {
				Image activeMaleOptionImage = new Image(imageStreamActiveMaleOption);
				activeMaleOptionIcon = new ImageView(activeMaleOptionImage);
				activeMaleOptionIcon.setFitWidth(10);
				activeMaleOptionIcon.setFitHeight(16);
			} else {
				System.err.println("Unable to load active.png");
			}

			// Import inactive icon for male option
			ImageView inactiveMaleOptionIcon = null;
			InputStream imageStreamInactiveMaleOption
					= getClass().getResourceAsStream("images/inactive.png");
			if (imageStreamInactiveMaleOption != null) {
				Image inactiveMaleOptionImage = new Image(imageStreamInactiveMaleOption);
				inactiveMaleOptionIcon = new ImageView(inactiveMaleOptionImage);
				inactiveMaleOptionIcon.setFitWidth(10);
				inactiveMaleOptionIcon.setFitHeight(16);
			} else {
				System.err.println("Unable to load inactive.png");
			}

			// Set icon for male option in dropdown list
 			if (genderDropdownLabel.getText().equals("Male")) {
				maleOption.setGraphic(activeMaleOptionIcon);
				maleOption.getStyleClass().add("dropdown-selected");
			} else {
				maleOption.setGraphic(inactiveMaleOptionIcon);
			}

			// Create female option in dropdown list
			Button femaleOption = new Button("Female");
			femaleOption.setAlignment(Pos.CENTER_LEFT); // Default is centered
			femaleOption.getStyleClass().add("dropdown-option");

			// Import active icon for female option
			ImageView activeFemaleOptionIcon = null;
			InputStream imageStreamActiveFemaleOption
					= getClass().getResourceAsStream("images/active.png");
			if (imageStreamActiveFemaleOption != null) {
				Image activeFemaleOptionImage = new Image(imageStreamActiveFemaleOption);
				activeFemaleOptionIcon = new ImageView(activeFemaleOptionImage);
				activeFemaleOptionIcon.setFitWidth(10);
				activeFemaleOptionIcon.setFitHeight(16);
			} else {
				System.err.println("Unable to load active.png");
			}

			// Import inactive icon for female option
			ImageView inactiveFemaleOptionIcon = null;
			InputStream imageStreamInactiveFemaleOption
					= getClass().getResourceAsStream("images/inactive.png");
			if (imageStreamInactiveFemaleOption != null) {
				Image inactiveFemaleOptionImage = new Image(imageStreamInactiveFemaleOption);
				inactiveFemaleOptionIcon = new ImageView(inactiveFemaleOptionImage);
				inactiveFemaleOptionIcon.setFitWidth(10);
				inactiveFemaleOptionIcon.setFitHeight(16);
			} else {
				System.err.println("Unable to load inactive.png");
			}

			// Set icon for female option in dropdown list
			if (genderDropdownLabel.getText().equals("Female")) {
				femaleOption.setGraphic(activeFemaleOptionIcon);
				femaleOption.getStyleClass().add("dropdown-selected");
			} else {
				femaleOption.setGraphic(inactiveFemaleOptionIcon);
			}

			// Create dropdown list for gender
			VBox dropDownList = new VBox();
			dropDownList.getStyleClass().add("dropdown-list");
			dropDownList.getChildren().addAll(maleOption, femaleOption);

			maleOption.setOnMouseClicked(event -> {
				genderDropdownLabel.setText("Male");
				popup.hide();
			});

			femaleOption.setOnMouseClicked(event -> {
				genderDropdownLabel.setText("Female");
				popup.hide();
			});

			// Add dropdown list to popup
			popup.getContent().add(dropDownList);
			popup.show(genderButton,
					genderButton.localToScreen(genderButton.getBoundsInLocal()).getMinX(),
					genderButton.localToScreen(
							genderButton.getBoundsInLocal()).getMinY() - 5
			);
		});

		// Clear the existing content
		userEditFieldsPane.getChildren().clear();

		// Create "Add/Edit user" title
		Label userEditTitle = new Label();
		userEditTitle.getStyleClass().add("main-title");
		if (!isEdit) {
			userEditTitle.setText("New user");
		} else {
			userEditTitle.setText("Edit user");
		}

		// Import icon for "Delete" button
		ImageView deleteUserIcon = null;
		InputStream imageStreamDeleteUser =
				getClass().getResourceAsStream("images/delete.png");
		if (imageStreamDeleteUser != null) {
			Image deleteUserImage = new Image(imageStreamDeleteUser);
			deleteUserIcon = new ImageView(deleteUserImage);
			deleteUserIcon.setFitWidth(26);
			deleteUserIcon.setFitHeight(26);
		} else {
			System.err.println("Unable to load delete.png");
		}

		// Create "Delete" label
		Label deleteUserLabel = new Label("Delete");
		deleteUserLabel.getStyleClass().add("file-pane-label");

		// Create "Delete" details label
		Label deleteUserInfoLabel = new Label("Irreversible action");
		deleteUserInfoLabel.getStyleClass().add("file-pane-info-label");

		// Create pane for "Delete" labels
		VBox deleteUserLabelPane = new VBox();
		deleteUserLabelPane.getChildren().addAll(deleteUserLabel, deleteUserInfoLabel);

		// Create pane for contents of "Delete" button
		HBox inDeleteUserButton = new HBox();
		inDeleteUserButton.setAlignment(Pos.CENTER_LEFT);
		inDeleteUserButton.getChildren().addAll(deleteUserIcon, deleteUserLabelPane);

		// Create "Delete" button
		Button deleteUserButton = new Button();
		deleteUserButton.getStyleClass().addAll("file-button", "delete-button");
		deleteUserButton.setGraphic(inDeleteUserButton);

		// Create pane for "Delete" button
		HBox userEditFilePane = new HBox();
		userEditFilePane.setAlignment(Pos.BASELINE_RIGHT);
		userEditFilePane.getStyleClass().add("file-pane");
		userEditFilePane.getChildren().addAll(deleteUserButton);

		ImageView userEditIcon = null;
		VBox userEditLabelPane;
		if (isEdit) {
			// Import icon for editing user
			InputStream imageStreamUserEdit =
					getClass().getResourceAsStream("images/edit.png");
			if (imageStreamUserEdit != null) {
				Image editImage = new Image(imageStreamUserEdit);
				userEditIcon = new ImageView(editImage);
				userEditIcon.setFitWidth(24);
				userEditIcon.setFitHeight(24);
			} else {
				System.err.println("Unable to load edit.png");
			}

			// Create "Edit user" label
			Label userEditLabel = new Label("  Edit user");
			userEditLabel.getStyleClass().add("main-label");

			// Create "Edit user" details label
			Label userEditDetailLabel =
					new Label("  Proceed with caution: No undo or history");
			userEditDetailLabel.getStyleClass().add("detail-label");

			// Create pane for "Edit user" labels
			userEditLabelPane = new VBox(userEditLabel, userEditDetailLabel);

		} else {
			// Import icon for adding user
			InputStream imageStreamUserEdit =
					getClass().getResourceAsStream("images/add.png");
			if (imageStreamUserEdit != null) {
				Image editImage = new Image(imageStreamUserEdit);
				userEditIcon = new ImageView(editImage);
				userEditIcon.setFitWidth(24);
				userEditIcon.setFitHeight(24);
			} else {
				System.err.println("Unable to load add.png");
			}

			// Create "Add user" label
			Label userEditLabel = new Label("  Add user");
			userEditLabel.getStyleClass().add("main-label");

			// Create "Add user" details label
			Label userEditDetailLabel =
					new Label("  Caution: Data will not be saved automatically");
			userEditDetailLabel.getStyleClass().add("detail-label");

			// Create pane for "Edit user" labels
			userEditLabelPane = new VBox(userEditLabel, userEditDetailLabel);
		}

		// Create spacer between "Edit user" labels & buttons
		Region userCancelApplySpacer = new Region();
		HBox.setHgrow(userCancelApplySpacer, Priority.ALWAYS);

		// Create "Cancel" button
		Button userCancelButton = new Button("Cancel");
		userCancelButton.getStyleClass().add("normal-button");

		// Create "Add/Apply" button
		Button userOkButton = new Button();
		userOkButton.getStyleClass().add("important-button");
		if (isEdit) {
			// Set button text to "Apply" for editing
			userOkButton.setText("Apply");

			// Set action for "Apply" button
			userOkButton.setOnAction(event -> {
				userItem.setFirstName(firstNameField.getText());
				userItem.setLastName(lastNameField.getText());
				userItem.setId(Integer.parseInt(userIdField.getText()));
				userItem.setEmail(emailField.getText());
				userItem.setGender(genderDropdownLabel.getText());
				toggleUserEditPane(userPane);
			});
		} else {
			// Set button text to "Add" for adding
			userOkButton.setText("Add");

			// Set action for "Add" button
			userOkButton.setOnAction(event -> {
				// Create a new UserItem with the data from the fields
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				int id = Integer.parseInt(userIdField.getText());
				String email = emailField.getText();
				String gender = genderDropdownLabel.getText();

				// Create a new UserItem object
				UserItem newUser = new UserItem(firstName, lastName, id, email, gender);

				// Add the new user to the userRecords ArrayList
				userRecords.add(newUser);
				toggleUserEditPane(userPane);
			});
		}

		// Create pane for "Cancel" & "Add/Apply" buttons
		HBox userCancelOKPane = new HBox(userCancelButton, userOkButton);
		userCancelOKPane.getStyleClass().add("cancel-apply-spacing");

		// Create row for edit user
		HBox userEditPane = new HBox(userEditIcon, userEditLabelPane,
				userCancelApplySpacer, userCancelOKPane);
		userEditPane.setAlignment(Pos.CENTER_LEFT);
		userEditPane.getStyleClass().add("edit-row");

		// Import icon for first name
		ImageView firstNameIcon = null;
		InputStream imageStreamFirstName =
				getClass().getResourceAsStream("images/first-name.png");
		if (imageStreamFirstName != null) {
			Image firstNameImage = new Image(imageStreamFirstName);
			firstNameIcon = new ImageView(firstNameImage);
			firstNameIcon.setFitWidth(24);
			firstNameIcon.setFitHeight(24);
			firstNameIcon.getStyleClass().add("edit-icon");
		} else {
			System.err.println("Unable to load first-name.png");
		}

		// Create "First name" label
		Label firstNameLabel = new Label("  First name");
		firstNameLabel.getStyleClass().add("main-label");

		// Create spacer between "First name" label & text field
		Region firstNameSpacer = new Region();
		HBox.setHgrow(firstNameSpacer, Priority.ALWAYS);

		// Create row for first name
		HBox firstNamePane = new HBox(firstNameIcon, firstNameLabel,
				firstNameSpacer, firstNameField);
		firstNamePane.setAlignment(Pos.CENTER_LEFT);
		firstNamePane.getStyleClass().add("edit-row");

		// Import icon for last name
		ImageView lastNameIcon = null;
		InputStream imageStreamLastName =
				getClass().getResourceAsStream("images/last-name.png");
		if (imageStreamLastName != null) {
			Image lastNameImage = new Image(imageStreamLastName);
			lastNameIcon = new ImageView(lastNameImage);
			lastNameIcon.setFitWidth(24);
			lastNameIcon.setFitHeight(24);
			lastNameIcon.getStyleClass().add("edit-icon");
		} else {
			System.err.println("Unable to load last-name.png");
		}

		// Create "Last name" label
		Label lastNameLabel = new Label("  Last name");
		lastNameLabel.getStyleClass().add("main-label");

		// Create spacer between "Last name" label & text field
		Region lastNameSpacer = new Region();
		HBox.setHgrow(lastNameSpacer, Priority.ALWAYS);

		// Create row for last name
		HBox lastNamePane = new HBox(lastNameIcon, lastNameLabel,
				lastNameSpacer, lastNameField);
		lastNamePane.setAlignment(Pos.CENTER_LEFT);
		lastNamePane.getStyleClass().add("edit-row");

		// Import icon for user ID
		ImageView userIdIcon = null;
		InputStream imageStreamUserId =
				getClass().getResourceAsStream("images/id.png");
		if (imageStreamUserId != null) {
			Image userIdImage = new Image(imageStreamUserId);
			userIdIcon = new ImageView(userIdImage);
			userIdIcon.setFitWidth(24);
			userIdIcon.setFitHeight(24);
		} else {
			System.err.println("Unable to load id.png");
		}

		// Create "ID" label
		Label userIdLabel = new Label("  ID");
		userIdLabel.getStyleClass().add("main-label");

		// Create "ID" details label
		Label userIdDetailLabel =
				new Label("  Use the official ID provided by the university");
		userIdDetailLabel.getStyleClass().add("detail-label");

		// Create pane for "ID" labels
		VBox userIdLabelPane = new VBox(userIdLabel, userIdDetailLabel);

		// Create spacer between "ID" labels & text field
		Region userIdSpacer = new Region();
		HBox.setHgrow(userIdSpacer, Priority.ALWAYS);

		// Create row for user ID
		HBox userIdPane = new HBox(userIdIcon, userIdLabelPane, userIdSpacer, userIdField);
		userIdPane.setAlignment(Pos.CENTER_LEFT);
		userIdPane.getStyleClass().add("edit-row");

		// Import icon for email
		ImageView emailIcon = null;
		InputStream imageStreamEmail = getClass().getResourceAsStream("images/email.png");
		if (imageStreamEmail != null) {
			Image emailImage = new Image(imageStreamEmail);
			emailIcon = new ImageView(emailImage);
			emailIcon.setFitWidth(24);
			emailIcon.setFitHeight(24);
		} else {
			System.err.println("Unable to load email.png");
		}

		// Create "Email" label
		Label emailLabel = new Label("  Email");
		emailLabel.getStyleClass().add("main-label");

		// Create "Email" details label
		Label emailDetailLabel =
				new Label("  Use the official email provided by the university");
		emailDetailLabel.getStyleClass().add("detail-label");

		// Create pane for "Email" labels
		VBox emailLabelPane = new VBox(emailLabel, emailDetailLabel);

		// Create spacer between "Email" labels & text field
		Region emailSpacer = new Region();
		HBox.setHgrow(emailSpacer, Priority.ALWAYS);

		// Create row for email
		HBox emailPane = new HBox(emailIcon, emailLabelPane, emailSpacer, emailField);
		emailPane.setAlignment(Pos.CENTER_LEFT);
		emailPane.getStyleClass().add("edit-row");

		// Import icon for gender
		ImageView genderIcon = null;
		InputStream imageStreamGender =
				getClass().getResourceAsStream("images/gender.png");
		if (imageStreamGender != null) {
			Image genderImage = new Image(imageStreamGender);
			genderIcon = new ImageView(genderImage);
			genderIcon.setFitWidth(24);
			genderIcon.setFitHeight(24);
		} else {
			System.err.println("Unable to load gender.png");
		}

		// Create "Gender" label
		Label genderLabel = new Label("Gender");
		genderLabel.getStyleClass().add("main-label");

		// Create spacer between "Gender" label and choice box
		Region genderSpacer = new Region();
		HBox.setHgrow(genderSpacer, Priority.ALWAYS);

		// Create row for "Gender" label and choice box
		HBox genderPane = new HBox(genderIcon, genderLabel, genderSpacer, genderButton);
		genderPane.setAlignment(Pos.CENTER_LEFT);
		genderPane.getStyleClass().add("edit-row");

		// Create table for user edit rows
		VBox userEditTable = new VBox(
				userEditPane,
				firstNamePane,
				lastNamePane,
				userIdPane,
				emailPane,
				genderPane
		);
		userEditTable.setAlignment(Pos.CENTER_LEFT);
		userEditTable.getStyleClass().add("fields-pane");

		if (isEdit) {
			// Create pane for user edit
			userEditFieldsPane.getChildren().addAll(userEditTitle,
					userEditFilePane, userEditTable);
		} else {
			// Create pane for user edit
			userEditFieldsPane.getChildren().addAll(userEditTitle, userEditTable);
		}
		userEditFieldsPane.getStyleClass().add("edit-pane");

		// Set action for "Delete" button
		deleteUserButton.setOnAction(event -> {
			Stage dialogStage = new Stage();

			// Specify the main window as the owner of the dialog
			dialogStage.initOwner(mainStage);

			dialogStage.initStyle(StageStyle.TRANSPARENT);
			dialogStage.setWidth(360);
			dialogStage.setHeight(200);

			double centerXPosition = mainStage.getX() + mainStage.getWidth() / 2d;
			double centerYPosition = mainStage.getY() + mainStage.getHeight() / 2d;

			dialogStage.setX(centerXPosition - dialogStage.getWidth() / 2d);
			dialogStage.setY(centerYPosition - dialogStage.getHeight() / 2d);

			Label dialogTitle = new Label("Delete User");
			dialogTitle.getStyleClass().add("dialog-title");

			Label dialogText = new Label("Are you sure you want to delete this user?");
			dialogText.getStyleClass().add("dialog-text");

			VBox dialogTextPane = new VBox();
			dialogTextPane.getStyleClass().add("dialog-text-pane");
			dialogTextPane.getChildren().addAll(dialogTitle, dialogText);

			Button delButton = new Button("Delete");
			delButton.getStyleClass().add("confirm-button");
			delButton.setOnMouseClicked(e -> {
				// Set confirmation to yes
				// Remove user
				userRecords.remove(userItem);

				// Hide user edit pane
				toggleUserEditPane(userPane);

				// Close dialog
				dialogStage.close();
			});

			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);

			Button cancelButton = new Button("Cancel");
			cancelButton.getStyleClass().add("normal-button");
			cancelButton.setOnAction(e -> dialogStage.close());

			HBox delConfirmButtons = new HBox(delButton, spacer, cancelButton);
			delConfirmButtons.getStyleClass().add("dialog-button-pane");

			VBox delConfirmRoot = new VBox(dialogTextPane, delConfirmButtons);
			delConfirmRoot.getStyleClass().add("confirm-dialog");

			Scene delConfirmScene = new Scene(delConfirmRoot, Color.TRANSPARENT);
			dialogStage.setScene(delConfirmScene);

			// After dialogStage is created
			mainStage.xProperty().addListener((obs, oldVal, newVal)
					-> dialogStage.setX(newVal.doubleValue()
					+ mainStage.getWidth() / 2d
					- dialogStage.getWidth() / 2d
			));

			mainStage.yProperty().addListener((obs, oldVal, newVal)
					-> dialogStage.setY(newVal.doubleValue()
					+ mainStage.getHeight() / 2d
					- dialogStage.getHeight() / 2d
			));

			// Null checker for styles.css
			URL cssUrl = getClass().getResource("styles.css");
			if (cssUrl != null) {
				delConfirmScene.getStylesheets().add(cssUrl.toExternalForm());
			} else {
				System.err.println("Unable to find styles.css");
			}

			dialogStage.show();
		});

		// Set action for "Cancel" button
		userCancelButton.setOnAction(event -> {
			firstNameField.setText(userItem.getFirstName());
			lastNameField.setText(userItem.getLastName());
			userIdField.setText(String.valueOf(userItem.getId()));
			emailField.setText(userItem.getEmail());
			genderDropdownLabel.setText(userItem.getGender());

			// Hide user edit pane
			toggleUserEditPane(userPane);
		});
	}


	private static class BookItem {
		private final StringProperty title;
		private final StringProperty author;
		private final IntegerProperty isbn;
		private final StringProperty info;
		private final StringProperty subject;

		public BookItem(String title, String author, int isbn, String info, String subject) {
			this.title = new SimpleStringProperty(title);
			this.author = new SimpleStringProperty(author);
			this.isbn = new SimpleIntegerProperty(isbn);
			this.info = new SimpleStringProperty(info);
			this.subject = new SimpleStringProperty(subject);
		}

		public String getTitle() { return title.get(); }
		public String getAuthor() { return author.get(); }
		public int getIsbn() { return isbn.get(); }
		public String getInfo() { return info.get(); }
		public String getSubject() { return subject.get(); }

		public void setTitle(String title) { this.title.set(title); }
		public void setAuthor(String author) {
			this.author.set(author);
		}
		public void setIsbn(int isbn) {
			this.isbn.set(isbn);
		}
		public void setInfo(String info) { this.info.set(info); }
		public void setGender(String subject) { this.subject.set(subject); }
	}

	private void toggleBookEditPane(StackPane bookPane) {
		VBox bookViewPane = (VBox) bookPane.getChildren().get(0);
		VBox bookEditPane = (VBox) bookPane.getChildren().get(1);

		if (!isBookEditPaneVisible) {
			bookEditPane.setVisible(true);

			TranslateTransition slideIn = new TranslateTransition(
					Duration.millis(1000), bookEditPane);
			slideIn.setFromX(bookViewPane.getWidth());
			slideIn.setToX(0);
			slideIn.play();
			isBookEditPaneVisible = true;

			// Clear existing text fields
			clearBookEditFields(bookEditPane);
		} else {
			TranslateTransition slideOut = new TranslateTransition(
					Duration.millis(1000), bookEditPane);
			slideOut.setFromX(0);
			slideOut.setToX(bookViewPane.getWidth());
			slideOut.setOnFinished(e -> {
				bookEditPane.setVisible(false);

				// Clear text fields after animation finishes
				clearBookEditFields(bookEditPane);
			});
			slideOut.play();
			isBookEditPaneVisible = false;
			updateBookTablePane(bookRecords);
		}
	}

	private void clearBookEditFields(VBox bookEditPane) {
		for (Node node : bookEditPane.getChildren()) {
			if (node instanceof TextField) {
				((TextField) node).clear();
			}
		}
	}

	private void populateBookEditFields(VBox bookEditFieldsPane, BookItem bookItem) {
		// Create title field
		TextField titleField = new TextField();
		titleField.getStyleClass().add("text-field");
		titleField.setText(bookItem.getTitle());

		// Create author field
		TextField authorField = new TextField();
		authorField.getStyleClass().add("text-field");
		authorField.setText(bookItem.getAuthor());

		// Create isbn field
		TextField isbnField = new TextField();
		isbnField.getStyleClass().add("text-field");
		isbnField.setText(String.valueOf(bookItem.getIsbn()));

		// Create email name field
		TextField infoField = new TextField();
		infoField.getStyleClass().add("text-field");
		infoField.setText(String.valueOf(bookItem.getInfo()));
		
		// Create gender choice box
		ChoiceBox<String> subjectField = new ChoiceBox<>();
		subjectField.getItems().addAll("Male", "Female");

		
		
		
		
		

		// Create book edit title label
		Label bookEditTitleLabel = new Label("Edit Book");
		bookEditTitleLabel.getStyleClass().add("main-title");

		// Create button pane for OK button
		HBox bookEditButtonPane = new HBox();
		bookEditButtonPane.setAlignment(Pos.CENTER_LEFT);
		bookEditButtonPane.getStyleClass().add("button-pane");

		// Create delete icon
		ImageView deleteBookIcon = null;
		InputStream imageStreamDeleteBook =
				getClass().getResourceAsStream("images/delete.png");
		if (imageStreamDeleteBook != null) {
			Image deleteBookImage = new Image(imageStreamDeleteBook);
			deleteBookIcon = new ImageView(deleteBookImage);
			deleteBookIcon.setFitWidth(26);
			deleteBookIcon.setFitHeight(26);
			deleteBookIcon.getStyleClass().add("file-pane-icon");
		} else {
			System.err.println("Unable to load delete.png");
		}

		// Create labels for text lines
		Label deleteBookLabel = new Label("Delete");
		deleteBookLabel.getStyleClass().add("file-pane-label");
		Label deleteBookInfoLabel = new Label("Irreversible action");
		deleteBookInfoLabel.getStyleClass().add("file-pane-info-label");

		// Create HBox to hold icon and labels
		HBox deleteButtonContent = new HBox();
		deleteButtonContent.getChildren().addAll(
				deleteBookIcon,
				new VBox(deleteBookLabel, deleteBookInfoLabel)
		);
		deleteButtonContent.setAlignment(Pos.CENTER_LEFT);

		// Create button and set its content to the HBox
		Button deleteBookButton = new Button();
		deleteBookButton.getStyleClass().addAll("file-button", "delete-button");
		deleteBookButton.setGraphic(deleteButtonContent);
		deleteBookButton.setOnAction(event -> {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Confirm Delete");
			alert.setHeaderText("Delete BookItem");
			alert.setContentText("Are you sure you want to delete this bookItem?");
		});

		// Create HBox for delete button
		HBox bookFilePane = new HBox();
		bookFilePane.getStyleClass().add("file-pane");
		bookFilePane.getChildren().addAll(deleteBookButton);
		bookFilePane.setAlignment(Pos.BASELINE_RIGHT);

		// Create OK button
		Button okBookButton = new Button("OK");
		okBookButton.getStyleClass().add("important-button");
		okBookButton.setOnAction(event -> {
			// Update the bookItem with the edited values
			bookItem.setTitle(titleField.getText());
			bookItem.setAuthor(authorField.getText());
			bookItem.setIsbn(Integer.parseInt(isbnField.getText()));
			bookItem.setInfo(infoField.getText());
			bookItem.setGender(subjectField.getValue());

			// Hide the book edit pane
			toggleBookEditPane(bookPane);
		});

		Button cancelBookButton = new Button("Cancel");
		cancelBookButton.getStyleClass().add("important-button");
		cancelBookButton.setOnAction(event -> {
			// Restore the original content of the rowBox
//            populateTableRowBox(bookEditFieldsPane, bookItem);

			// Hide the book edit pane without saving any changes
			toggleBookEditPane(bookPane);
		});

		// Add OK button to button pane
		bookEditButtonPane.getChildren().addAll(okBookButton, cancelBookButton);

		// Clear the existing content
		bookEditFieldsPane.getChildren().clear();

		// Create first name icon
		ImageView titleIcon = null;
		InputStream imageStreamTitle =
				getClass().getResourceAsStream("images/first-name.png");
		if (imageStreamTitle != null) {
			Image titleImage = new Image(imageStreamTitle);
			titleIcon = new ImageView(titleImage);
			titleIcon.setFitWidth(24);
			titleIcon.setFitHeight(24);
			titleIcon.getStyleClass().add("edit-icon");
		} else {
			System.err.println("Unable to load first-name.png");
		}

		// Create first name label
		Label titleLabel = new Label("Title");
		titleLabel.getStyleClass().add("main-label");

		// Create first name pane
		HBox titlePane = new HBox(titleIcon, titleLabel, titleField);
		titlePane.getStyleClass().add("edit-row");
		titlePane.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(titleField, Priority.ALWAYS);

		// Create last name icon
		ImageView authorIcon = null;
		InputStream imageStreamAuthor =
				getClass().getResourceAsStream("images/last-name.png");
		if (imageStreamAuthor != null) {
			Image authorImage = new Image(imageStreamAuthor);
			authorIcon = new ImageView(authorImage);
			authorIcon.setFitWidth(24);
			authorIcon.setFitHeight(24);
			authorIcon.getStyleClass().add("edit-icon");
		} else {
			System.err.println("Unable to load last-name.png");
		}

		// Create last name label
		Label authorLabel = new Label("Last Name");
		authorLabel.getStyleClass().add("main-label");

		// Create last name pane
		HBox authorPane = new HBox(authorIcon, authorLabel, authorField);
		authorPane.getStyleClass().add("edit-row");
		authorPane.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(authorField, Priority.ALWAYS);

		// Create id icon
		ImageView idIcon = null;
		InputStream imageStreamId = getClass().getResourceAsStream("images/id.png");
		if (imageStreamId != null) {
			Image idImage = new Image(imageStreamId);
			idIcon = new ImageView(idImage);
			idIcon.setFitWidth(24);
			idIcon.setFitHeight(24);
			idIcon.getStyleClass().add("edit-icon");
		} else {
			System.err.println("Unable to load id.png");
		}

		// Create id label
		Label idLabel = new Label("ID");
		idLabel.getStyleClass().add("main-label");

		// Create id details label
		Label idDetailLabel = new Label("Use official ID provided by university");
		idDetailLabel.getStyleClass().add("detail-label");

		// Create email label pane
		VBox idLabelPane = new VBox(idLabel, idDetailLabel);

		// Create id name pane
		HBox idPane = new HBox(idIcon, idLabelPane, isbnField);
		idPane.getStyleClass().add("edit-row");
		idPane.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(isbnField, Priority.ALWAYS);

		// Create email icon
		ImageView emailIcon = null;
		InputStream imageStreamEmail =
				getClass().getResourceAsStream("images/email.png");
		if (imageStreamEmail != null) {
			Image emailImage = new Image(imageStreamEmail);
			emailIcon = new ImageView(emailImage);
			emailIcon.setFitWidth(24);
			emailIcon.setFitHeight(24);
			emailIcon.getStyleClass().add("edit-icon");
		} else {
			System.err.println("Unable to load email.png");
		}

		// Create email label
		Label emailLabel = new Label("Email");
		emailLabel.getStyleClass().add("main-label");

		// Create email details label
		Label emailDetailLabel = new Label("Use official university email address");
		emailDetailLabel.getStyleClass().add("detail-label");

		// Create email label pane
		VBox emailLabelPane = new VBox(emailLabel, emailDetailLabel);

		// Create email name pane
		HBox emailPane = new HBox(emailIcon, emailLabelPane, infoField);
		emailPane.getStyleClass().add("edit-row");
		emailPane.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(infoField, Priority.ALWAYS);

		// Create gender icon
		ImageView genderIcon = null;
		InputStream imageStreamGender =
				getClass().getResourceAsStream("images/gender.png");
		if (imageStreamGender != null) {
			Image genderImage = new Image(imageStreamGender);
			genderIcon = new ImageView(genderImage);
			genderIcon.setFitWidth(24);
			genderIcon.setFitHeight(24);
			genderIcon.getStyleClass().add("edit-icon");
		} else {
			System.err.println("Unable to load gender.png");
		}

		// Create gender label
		Label genderLabel = new Label("   Gender");
		genderLabel.getStyleClass().add("main-label");

		if (bookItem.getSubject().equals("Male")) { // Set default selection
			subjectField.setValue("Male");
		} else {
			subjectField.setValue("Female");
		}

		// Create gender pane
		HBox genderPane = new HBox(genderIcon, genderLabel, subjectField);
		genderPane.getStyleClass().add("edit-row");
		genderPane.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(subjectField, Priority.ALWAYS);


		// Create row pane
		VBox fieldsPane = new VBox(titlePane, authorPane, idPane, emailPane, genderPane);
		fieldsPane.getStyleClass().add("fields-pane");
		fieldsPane.setAlignment(Pos.CENTER_LEFT);

		bookEditFieldsPane.getChildren().addAll(
				bookEditTitleLabel,
				bookFilePane,
				bookEditButtonPane,
				fieldsPane
		);
		bookEditFieldsPane.getStyleClass().add("edit-pane");
	}

	private void importFromUserFile(File file) {
		userRecords.clear(); // Clear the ArrayList before importing

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 5) {
					String firstName = parts[0];
					String lastName = parts[1];
					int id = Integer.parseInt(parts[2]);
					String email = parts[3];
					String gender = parts[4];
					userRecords.add(new UserItem(firstName, lastName, id, email, gender));
				}
			}
			updateUserTablePane(userRecords);
		} catch (IOException e) {
			System.err.println("Unable to import user items.");
			openImportUserDialog();
		}
	}

	private void openImportUserDialog() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Import UserItems");
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			importFromUserFile(file);
		}
	}

	private void exportToUserFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export UserItems");
		fileChooser.setInitialFileName("Users.txt");
		File file = fileChooser.showSaveDialog(null);
		if (file != null) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				for (UserItem userItem : userRecords) {
					String line = String.format("%s,%s,%d,%s,%s",
							userItem.getFirstName(),
							userItem.getLastName(),
							userItem.getId(),
							userItem.getEmail(),
							userItem.getGender());
					writer.write(line);
					writer.newLine();
				}
			} catch (IOException e) {
				System.err.println("Unable to export user items to file.");
			}
		}
	}

	private void addUser() {
		UserItem userItem = new UserItem(
				"First Name",
				"Last Name",
				generateId(),
				"Email",
				"Gender"
		);
		openUserEditPane(userItem, false);
	}

	private void editUser(UserItem userItem) {
		openUserEditPane(userItem, true);
	}

	private void openUserEditPane(UserItem userItem, boolean isEdit) {
		toggleUserEditPane(userPane);
		populateUserEditFields(userEditPane, userItem, isEdit);
	}

	private void updateUserTablePane(ArrayList<UserItem> userItems) {
		// Clear existing rows
		userTablePane.getChildren().clear();

		for (UserItem userItem : userItems) {

			// Create male icon
			ImageView maleIcon = null;
			InputStream imageStreamMale =
					getClass().getResourceAsStream("images/male.png");
			if (imageStreamMale != null) {
				Image maleImage = new Image(imageStreamMale);
				maleIcon = new ImageView(maleImage);
				maleIcon.setFitWidth(24);
				maleIcon.setFitHeight(24);
			} else {
				System.err.println("Unable to load male.png");
			}

			// Create female icon
			ImageView femaleIcon = null;
			InputStream imageStreamFemale =
					getClass().getResourceAsStream("images/female.png");
			if (imageStreamFemale != null) {
				Image femaleImage = new Image(imageStreamFemale);
				femaleIcon = new ImageView(femaleImage);
				femaleIcon.setFitWidth(24);
				femaleIcon.setFitHeight(24);
			} else {
				System.err.println("Unable to load female.png");
			}

			// Create gender icon
			ImageView genderIcon;
			if (userItem.getGender().equals("Male")) {
				genderIcon = maleIcon;
			} else {
				genderIcon = femaleIcon;
			}

			// Create forward icon
			ImageView forwardIcon = null;
			InputStream imageStreamForward =
					getClass().getResourceAsStream("images/forward.png");
			if (imageStreamForward != null) {
				Image forwardImage = new Image(imageStreamForward);
				forwardIcon = new ImageView(forwardImage);
				forwardIcon.setFitWidth(12);
				forwardIcon.setFitHeight(12);
				forwardIcon.getStyleClass().add("forward-icon");
			} else {
				System.err.println("Unable to load forward.png");
			}

			Region userImageSpacer = new Region();
			userImageSpacer.getStyleClass().add("image-spacer");

			// Create name field
			Label firstNameLabel = new Label(userItem.getFirstName());
			firstNameLabel.getStyleClass().add("main-label");
			Label lastNameLabel = new Label(userItem.getLastName());
			lastNameLabel.getStyleClass().add("detail-label");

			// Add first & last names to name field
			VBox nameField = new VBox(firstNameLabel, lastNameLabel);
			nameField.getStyleClass().add("first-column");

			// Create id & email field
			Label idLabel = new Label(String.valueOf(userItem.getId()));
			idLabel.getStyleClass().add("main-label");
			Label emailLabel = new Label(userItem.getEmail());
			emailLabel.getStyleClass().add("detail-label");
			VBox idEmailField = new VBox(idLabel, emailLabel);

			// Add spacer field to right-align forward icon
			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);

			// Create row pane
			HBox rowPane = new HBox(
					genderIcon,
					userImageSpacer,
					nameField,
					idEmailField,
					spacer,
					forwardIcon
			);
			rowPane.getStyleClass().add("view-row");
			rowPane.setAlignment(Pos.CENTER_LEFT); // Vertically centers forward icon

			// Add click handler to row pane
			rowPane.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1) {
					editUser(userItem);
				}
			});

			// Add row pane to table pane
			userTablePane.getChildren().add(rowPane);
		}
	}

	private void importFromBookFile(File file) {
		bookRecords.clear(); // Clear the ArrayList before importing

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 5) {
					String firstName = parts[0];
					String lastName = parts[1];
					int id = Integer.parseInt(parts[2]);
					String email = parts[3];
					String gender = parts[4];
					bookRecords.add(new BookItem(firstName, lastName, id, email, gender));
				}
			}
			updateBookTablePane(bookRecords);
		} catch (IOException e) {
			System.err.println("Unable to import book items from file.");
			openImportBookDialog();
		}
	}

	private void openImportBookDialog() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Import BookItems");
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			importFromBookFile(file);
		}
	}

	private void exportToBookFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export BookItems");
		fileChooser.setInitialFileName("Books.txt");
		File file = fileChooser.showSaveDialog(null);
		if (file != null) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				for (BookItem bookItem : bookRecords) {
					String line = String.format("%s,%s,%d,%s,%s",
							bookItem.getTitle(),
							bookItem.getAuthor(),
							bookItem.getIsbn(),
							bookItem.getInfo(),
							bookItem.getSubject());
					writer.write(line);
					writer.newLine();
				}
			} catch (IOException e) {
				System.err.println("Unable to export book items to file.");
			}
		}
	}

	private void addBook() {
		BookItem bookItem = new BookItem(
				"First Name",
				"Last Name",
				generateId(),
				"Email",
				"Gender");
		editBookItem(bookItem);
	}

	private void editBookItem(BookItem bookItem) {
//        tableView.getSelectionModel().select(bookItem);

		toggleBookEditPane(bookPane);
		populateBookEditFields(bookEditPane, bookItem);
	}
	private void updateBookTablePane(ArrayList<BookItem> bookItems) {
		// Clear existing rows
		bookTablePane.getChildren().clear();

		for (BookItem bookItem : bookItems) {
			// Create male icon
			ImageView maleIcon = null;
			InputStream imageStreamMale =
					getClass().getResourceAsStream("images/male.png");
			if (imageStreamMale != null) {
				Image maleImage = new Image(imageStreamMale);
				maleIcon = new ImageView(maleImage);
				maleIcon.setFitWidth(32);
				maleIcon.setFitHeight(32);
			} else {
				System.err.println("Unable to load male.png");
			}

			// Create female icon
			ImageView femaleIcon = null;
			InputStream imageStreamFemale =
					getClass().getResourceAsStream("images/female.png");
			if (imageStreamFemale != null) {
				Image femaleImage = new Image(imageStreamFemale);
				femaleIcon = new ImageView(femaleImage);
				femaleIcon.setFitWidth(32);
				femaleIcon.setFitHeight(32);
			} else {
				System.err.println("Unable to load female.png");
			}

			// Create gender icon
			ImageView genderIcon;
			if (bookItem.getSubject().equals("Male")) {
				genderIcon = maleIcon;
			} else {
				genderIcon = femaleIcon;
			}

			// Create forward icon
			ImageView forwardIcon = null;
			InputStream imageStreamForward =
					getClass().getResourceAsStream("images/forward.png");
			if (imageStreamForward != null) {
				Image forwardImage = new Image(imageStreamForward);
				forwardIcon = new ImageView(forwardImage);
				forwardIcon.setFitWidth(12);
				forwardIcon.setFitHeight(12);
				forwardIcon.getStyleClass().add("forward-icon");
			} else {
				System.err.println("Unable to load forward.png");
			}

			// Create name field
			Label firstNameLabel = new Label(bookItem.getTitle());
			firstNameLabel.getStyleClass().add("main-label");
			Label lastNameLabel = new Label(bookItem.getAuthor());
			lastNameLabel.getStyleClass().add("detail-label");
			VBox nameField = new VBox(firstNameLabel, lastNameLabel);

			// Create id & email field
			Label idLabel = new Label(String.valueOf(bookItem.getIsbn()));
			idLabel.getStyleClass().add("main-label");
			Label emailLabel = new Label(bookItem.getInfo());
			emailLabel.getStyleClass().add("detail-label");
			VBox idEmailField = new VBox(idLabel, emailLabel);

			// Add spacer field to right-align forward icon
			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);

			// Create row pane
			HBox rowPane = new HBox(genderIcon, nameField, idEmailField, spacer, forwardIcon);
			rowPane.getStyleClass().add("view-row");
			rowPane.setAlignment(Pos.CENTER_LEFT); // Vertically centers forward icon

			// Add click handler to row pane
			rowPane.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1) {
					editBookItem(bookItem);
				}
			});

			// Add row pane to table pane
			bookTablePane.getChildren().add(rowPane);
		}

	}

	private static class ResizeHelper {

		private static final int RESIZE_MARGIN = 10;
		private static boolean resizing = false;

		public static boolean isNotResizing() {
			return !resizing;
		}

		static class ResizeListener {
			private final Stage stage;
			private Cursor cursor;
			private double initialX;
			private double initialY;

			private ResizeListener(Stage stage) {
				this.stage = stage;
			}

			void onMousePressed(MouseEvent event) {
				if (cursor != null) {
					resizing = true;
					initialX = event.getScreenX();
					initialY = event.getScreenY();
				}
			}

			void onMouseDragged(MouseEvent event) {
				if (resizing) {
					double offsetX = event.getScreenX() - initialX;
					double offsetY = event.getScreenY() - initialY;

					if (cursor.equals(Cursor.NW_RESIZE)) {
						resizeNW(offsetX, offsetY);
					} else if (cursor.equals(Cursor.N_RESIZE)) {
						resizeN(offsetY);
					} else if (cursor.equals(Cursor.NE_RESIZE)) {
						resizeNE(offsetX, offsetY);
					} else if (cursor.equals(Cursor.W_RESIZE)) {
						resizeW(offsetX);
					} else if (cursor.equals(Cursor.E_RESIZE)) {
						resizeE(offsetX);
					} else if (cursor.equals(Cursor.SW_RESIZE)) {
						resizeSW(offsetX, offsetY);
					} else if (cursor.equals(Cursor.S_RESIZE)) {
						resizeS(offsetY);
					} else if (cursor.equals(Cursor.SE_RESIZE)) {
						resizeSE(offsetX, offsetY);
					}

					initialX = event.getScreenX(); // Update initial position for next drag
					initialY = event.getScreenY(); // Update initial position for next drag
				}
			}

			void onMouseMoved(javafx.scene.input.MouseEvent event) {
				if (isResizeZone(event)) {
					stage.getScene().setCursor(cursor);
				} else {
					stage.getScene().setCursor(Cursor.DEFAULT);
				}
			}

			void onMouseReleased(MouseEvent event) {
				resizing = false;
			}

			private boolean isResizeZone(javafx.scene.input.MouseEvent event) {
				double x = event.getX();
				double y = event.getY();
				double width = stage.getWidth();
				double height = stage.getHeight();

				// Get the bounds of the appWindow layout in local coordinates

				cursor = null;

				if (x < RESIZE_MARGIN) {
					if (y < RESIZE_MARGIN) {
						cursor = Cursor.NW_RESIZE;
					} else if (y > height - RESIZE_MARGIN) {
						cursor = Cursor.SW_RESIZE;
					} else {
						cursor = Cursor.W_RESIZE;
					}
				} else if (x > width - RESIZE_MARGIN) {
					if (y < RESIZE_MARGIN) {
						cursor = Cursor.NE_RESIZE;
					} else if (y > height - RESIZE_MARGIN) {
						cursor = Cursor.SE_RESIZE;
					} else {
						cursor = Cursor.E_RESIZE;
					}
				} else if (y < RESIZE_MARGIN) {
					cursor = Cursor.N_RESIZE;
				} else if (y > height - RESIZE_MARGIN) {
					cursor = Cursor.S_RESIZE;
				}

				return cursor != null;
			}

			private void resizeNW(double offsetX, double offsetY) {
				double scalingFactor = calculateScalingFactor();
				double newWidth = stage.getWidth() - (offsetX * scalingFactor);
				double newHeight = stage.getHeight() - (offsetY * scalingFactor);
				double newX = stage.getX() + stage.getWidth() - newWidth;
				double newY = stage.getY() + stage.getHeight() - newHeight;

				resize(newWidth, newHeight, newX, newY);
			}

			private void resizeN(double offsetY) {
				double newHeight = stage.getHeight() - offsetY;
				double newY = stage.getY() + stage.getHeight() - newHeight;

				resize(stage.getWidth(), newHeight, stage.getX(), newY);
			}

			private void resizeNE(double offsetX, double offsetY) {
				double newWidth = stage.getWidth() + offsetX;
				double newHeight = stage.getHeight() - offsetY;
				double newY = stage.getY() + stage.getHeight() - newHeight;

				resize(newWidth, newHeight, stage.getX(), newY);
			}

			private void resizeW(double offsetX) {
				double newWidth = stage.getWidth() - offsetX;
				double newX = stage.getX() + stage.getWidth() - newWidth;

				resize(newWidth, stage.getHeight(), newX, stage.getY());
			}

			private void resizeE(double offsetX) {
				double newWidth = stage.getWidth() + offsetX;

				resize(newWidth, stage.getHeight(), stage.getX(), stage.getY());
			}

			private void resizeSW(double offsetX, double offsetY) {
				double newWidth = stage.getWidth() - offsetX;
				double newHeight = stage.getHeight() + offsetY;

				resize(newWidth, newHeight, stage.getX(), stage.getY());
			}

			private void resizeS(double offsetY) {
				double newHeight = stage.getHeight() + offsetY;

				resize(stage.getWidth(), newHeight, stage.getX(), stage.getY());
			}

			private void resizeSE(double offsetX, double offsetY) {
				double newWidth = stage.getWidth() + offsetX;
				double newHeight = stage.getHeight() + offsetY;

				resize(newWidth, newHeight, stage.getX(), stage.getY());
			}

			private double calculateScalingFactor() {
				// Calculate a scaling factor based on the current window size
				double scalingFactor = Math.max(
						RESIZE_SCALE_FACTOR * stage.getWidth(),
						RESIZE_SCALE_FACTOR * stage.getHeight()
				);

				// Limit the scaling factor to a maximum of 1.0
				return Math.min(scalingFactor, 1.0);
			}

			private void resize(double width, double height, double x, double y) {
				double minWidth =
						stage.getMinWidth() > 0 ? stage.getMinWidth() : 0;
				double minHeight =
						stage.getMinHeight() > 0 ? stage.getMinHeight() : 0;
				double maxWidth =
						stage.getMaxWidth() > 0 ? stage.getMaxWidth() : Double.MAX_VALUE;
				double maxHeight =
						stage.getMaxHeight() > 0 ? stage.getMaxHeight() : Double.MAX_VALUE;

				width = Math.min(Math.max(width, minWidth), maxWidth);
				height = Math.min(Math.max(height, minHeight), maxHeight);

				stage.setX(x);
				stage.setY(y);
				stage.setWidth(width);
				stage.setHeight(height);
			}
		}
	}

	public static void main(String[] args) { launch(args); }
}