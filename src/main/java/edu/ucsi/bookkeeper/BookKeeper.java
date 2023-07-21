package edu.ucsi.bookkeeper;

import javafx.application.Application;
import javafx.animation.TranslateTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class BookKeeper extends Application {

    private final ArrayList<Item> importedItems = new ArrayList<>();
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isUserEditPaneVisible = false;
    private boolean isBookEditPaneVisible = false;
    private VBox tablePane;
    private StackPane userPane;
    private VBox userEditPane;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField idField;
    private TextField emailField;
    private TextField genderField;

    private int generateId() {
        // Generate a unique ID based on the current timestamp
        return (int) System.currentTimeMillis();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // Create main window
        BorderPane root = new BorderPane();
        root.getStyleClass().add("round-corners");

        // Create left pane
        VBox leftPane = new VBox();
        leftPane.setPrefWidth(200);
        leftPane.setId("left-pane");

        // Create title bar
        HBox titleBar = new HBox();
        titleBar.setId("title-bar");

        // Create back button
        Button backButton = new Button();
        backButton.setId("back-button");

        // Null check for back button icon
        InputStream imageStreamBack = getClass().getResourceAsStream("images/title-bar/back.png");
        if (imageStreamBack != null) {
            Image backImage = new Image(imageStreamBack);
            ImageView backIcon = new ImageView(backImage);
            backIcon.setFitWidth(17);
            backIcon.setFitHeight(17);
            backButton.setGraphic(backIcon);
        } else {
            System.err.println("Unable to load back.png");
        }

        // Create title
        Label title = new Label("Book Keeper");
        title.setId("title");

        // Add back button and title to title bar
        titleBar.getChildren().addAll(backButton, title);

        // Create sidebar
        VBox sidebar = new VBox();
        sidebar.setAlignment(Pos.CENTER);
        sidebar.setSpacing(4);
        sidebar.setId("sidebar");

        // Create user tab
        Button userTab = new Button("Users");
        userTab.getStyleClass().setAll("sidebar-button", "sidebar-button-selected");
        userTab.setMaxWidth(Double.MAX_VALUE);

        // Null check for user tab icon
        InputStream imageStreamUser = getClass().getResourceAsStream("images/sidebar/user.png");
        if (imageStreamUser != null) {
            Image userImage = new Image(imageStreamUser);
            ImageView userIcon = new ImageView(userImage);
            userIcon.setFitWidth(26);
            userIcon.setFitHeight(26);
            userTab.setGraphic(userIcon);
        } else {
            System.err.println("Unable to load user.png");
        }

        // Create book tab
        Button bookTab = new Button("Books");
        bookTab.getStyleClass().add("sidebar-button");
        bookTab.setMaxWidth(Double.MAX_VALUE);

        // Null check for book tab icon
        InputStream imageStreamBook = getClass().getResourceAsStream("images/sidebar/book.png");
        if (imageStreamBook != null) {
            Image bookImage = new Image(imageStreamBook);
            ImageView bookIcon = new ImageView(bookImage);
            bookIcon.setFitWidth(26);
            bookIcon.setFitHeight(26);
            bookTab.setGraphic(bookIcon);
        } else {
            System.err.println("Unable to load book.png");
        }

        // Add user & book tabs to sidebar
        sidebar.getChildren().addAll(userTab, bookTab);

        // Add title bar & sidebar to left pane
        leftPane.getChildren().addAll(titleBar, sidebar);

        // Create right pane
        VBox rightPane = new VBox();
        rightPane.setId("right-pane");

        // Create window controls pane
        HBox windowControlsPane = new HBox();
        windowControlsPane.setId("window-controls-pane");
        windowControlsPane.setAlignment(Pos.TOP_RIGHT);

        // Create close window button
        Button closeButton = new Button();
        closeButton.setId("close-button");
        closeButton.getStyleClass().add("window-controls-button");
        closeButton.setOnAction(event -> primaryStage.close());

        // Null check for close window icon
        InputStream imageStreamClose = getClass().getResourceAsStream("images/window-controls-pane/close.png");
        if (imageStreamClose != null) {
            Image closeImage = new Image(imageStreamClose);
            ImageView closeIcon = new ImageView(closeImage);
            closeIcon.setFitWidth(14);
            closeIcon.setFitHeight(14);
            closeButton.setGraphic(closeIcon);
        } else {
            System.err.println("Unable to load close.png");
        }

        // Create maximize window button
        Button maximizeButton = new Button();
        maximizeButton.getStyleClass().add("window-controls-button");
        maximizeButton.setOnAction(event -> primaryStage.setMaximized(!primaryStage.isMaximized()));

        // Null check for maximize window icon
        InputStream imageStreamMaximize = getClass().getResourceAsStream("images/window-controls-pane/maximize.png");
        if (imageStreamMaximize != null) {
            Image maximizeImage = new Image(imageStreamMaximize);
            ImageView maximizeIcon = new ImageView(maximizeImage);
            maximizeIcon.setFitWidth(14);
            maximizeIcon.setFitHeight(14);
            maximizeButton.setGraphic(maximizeIcon);
        } else {
            System.err.println("Unable to load maximize.png");
        }

        // Create minimize window button
        Button minimizeButton = new Button();
        minimizeButton.getStyleClass().add("window-controls-button");
        minimizeButton.setOnAction(event -> primaryStage.setIconified(true));

        // Null check for minimize window icon
        InputStream imageStreamMinimize = getClass().getResourceAsStream("images/window-controls-pane/minimize.png");
        if (imageStreamMinimize != null) {
            Image minimizeImage = new Image(imageStreamMinimize);
            ImageView minimizeIcon = new ImageView(minimizeImage);
            minimizeIcon.setFitWidth(14);
            minimizeIcon.setFitHeight(14);
            minimizeButton.setGraphic(minimizeIcon);
        } else {
            System.err.println("Unable to load minimize.png");
        }

        // Add buttons to window controls pane
        windowControlsPane.getChildren().addAll(minimizeButton, maximizeButton, closeButton);

        // Create main pane
        StackPane mainPane = new StackPane();
        mainPane.setId("main-pane");

        // Create user pane
        this.userPane = new StackPane();

        // Create user content pane
        VBox userViewPane = new VBox();
        userViewPane.getStyleClass().add("user-content-pane");
        userViewPane.setFillWidth(true);

        // Create title for user view
        Label userViewPaneTitle = new Label("Users");
        userViewPaneTitle.getStyleClass().add("main-title");

        // Create Add button
        Button addButton = new Button("Add");
        addButton.getStyleClass().add("important-button");
        addButton.setOnAction(event -> addItem());

        // Create HBox for Add button
        HBox addPane = new HBox();
        addPane.getStyleClass().add("button-pane");
        addPane.getChildren().add(addButton);
        addPane.setAlignment(Pos.CENTER_LEFT);

        // Create import icon
        ImageView importIcon = null;
        InputStream imageStreamImport = getClass().getResourceAsStream("images/import.png");
        if (imageStreamImport != null) {
            Image importImage = new Image(imageStreamImport);
            importIcon = new ImageView(importImage);
            importIcon.setFitWidth(26);
            importIcon.setFitHeight(26);
            importIcon.getStyleClass().add("fila-pane-icon");
        } else {
            System.err.println("Unable to load import.png");
        }

        // Create labels for text lines
        Label importLabel = new Label("Import");
        importLabel.getStyleClass().add("file-pane-label");
        Label importInfoLabel = new Label("From text file");
        importInfoLabel.getStyleClass().add("file-pane-info-label");

        // Create HBox to hold icon and labels
        HBox importButtonContent = new HBox();
        importButtonContent.getChildren().addAll(importIcon, new VBox(importLabel, importInfoLabel));
        importButtonContent.setAlignment(Pos.CENTER_LEFT);

        // Create button and set its content to the HBox
        Button importButton = new Button();
        importButton.getStyleClass().add("file-button");
        importButton.setGraphic(importButtonContent);
        importButton.setPrefWidth(150);
        importButton.setOnAction(event -> openImportDialog());

        // Create export icon
        ImageView exportIcon = null;
        InputStream imageStreamExport = getClass().getResourceAsStream("images/export.png");
        if (imageStreamExport != null) {
            Image exportImage = new Image(imageStreamExport);
            exportIcon = new ImageView(exportImage);
            exportIcon.setFitWidth(26);
            exportIcon.setFitHeight(26);
            exportIcon.getStyleClass().add("fila-pane-icon");
        } else {
            System.err.println("Unable to load export.png");
        }

        // Create labels for text lines
        Label exportLabel = new Label("Export");
        exportLabel.getStyleClass().add("file-pane-label");
        Label exportInfoLabel = new Label("To text file");
        exportInfoLabel.getStyleClass().add("file-pane-info-label");

        // Create HBox to hold icon and labels
        HBox exportButtonContent = new HBox();
        exportButtonContent.getChildren().addAll(exportIcon, new VBox(exportLabel, exportInfoLabel));
        exportButtonContent.setAlignment(Pos.CENTER_LEFT);

        // Create button and set its content to the HBox
        Button exportButton = new Button();
        exportButton.getStyleClass().add("file-button");
        exportButton.setGraphic(exportButtonContent);
        exportButton.setPrefWidth(150);

        exportButton.setOnAction(event -> exportToFile());

        // Create HBox for Import and Export buttons
        HBox filePane = new HBox();
        filePane.getStyleClass().add("file-pane");
        filePane.getChildren().addAll(importButton, exportButton);
        filePane.setAlignment(Pos.BASELINE_RIGHT);

        // Create table
        tablePane = new VBox();
        tablePane.getStyleClass().add("table-pane");

        // Adds empty space after table
        Region emptySpace = new Region();
        emptySpace.setPrefHeight(16); // Same height as scrollbar

        // Create scroll pane
        VBox inScrollPane = new VBox(filePane, addPane, tablePane, emptySpace);
        inScrollPane.setStyle("-fx-background-color: #1B1F30;"); // Hides background
        ScrollPane scrollPane = new ScrollPane(inScrollPane);
        scrollPane.setFitToWidth(true); // Match parent width

        // Adds scrollPane to userViewPane
        userViewPane.getChildren().addAll(userViewPaneTitle, scrollPane);

        // Create user edit pane
        userEditPane = new VBox();
        userEditPane.getStyleClass().add("user-edit-pane");
        userEditPane.setFillWidth(true);
        userEditPane.setVisible(false);

        // Add user content pane and user edit pane to user pane
        userPane.getChildren().addAll(userViewPane, userEditPane);

        // Create book pane
        StackPane bookPane = new StackPane();
        bookPane.setId("book-pane");

        // Create book content pane
        VBox bookContentPane = new VBox();
        bookContentPane.setId("book-content-pane");

        // Create book content pane title
        Label bookContentPaneTitle = new Label("Books");
        bookContentPaneTitle.getStyleClass().add("main-title");

        // Create book edit button
        Button bookEditButton = new Button("EDIT BOOK");
        bookEditButton.setOnAction(e -> toggleBookEditPane(bookPane));

        // Add book content pane and book edit title to book content pane
        bookContentPane.getChildren().addAll(bookContentPaneTitle, bookEditButton);

        // Create book edit pane
        VBox bookEditPane = new VBox();
        bookEditPane.setId("book-edit-pane");
        bookEditPane.setTranslateX(bookContentPane.getWidth());
        bookEditPane.setVisible(false);

        // Add book content pane and book edit pane to book pane
        bookPane.getChildren().addAll(bookContentPane, bookEditPane);

        // Set action for back button
        backButton.setOnAction(event -> {
            if (isUserEditPaneVisible) {
                toggleUserEditPane(userPane);
            }
            // Otherwise, no action is needed
        });

        // Set action for user tab
        userTab.setOnAction(event -> {
            mainPane.getChildren().clear();
            mainPane.getChildren().add(userPane);
            userTab.getStyleClass().setAll("sidebar-button", "sidebar-button-selected");
            bookTab.getStyleClass().setAll("sidebar-button");
            VBox.setVgrow(userPane, Priority.ALWAYS);
        });

        // Set action for book tab
        bookTab.setOnAction(event -> {
            mainPane.getChildren().clear();
            mainPane.getChildren().add(bookPane);
            bookTab.getStyleClass().setAll("sidebar-button", "sidebar-button-selected");
            userTab.getStyleClass().setAll("sidebar-button");
        });

        StackPane.setAlignment(userPane, Pos.TOP_LEFT);

        // Add user pane to main pane
        mainPane.getChildren().addAll(userPane);

        // Add window controls pane and main pane to right pane
        rightPane.getChildren().addAll(windowControlsPane, mainPane);

        // Add left pane & right pane to main window
        root.setLeft(leftPane);
        root.setCenter(rightPane);

        // Create scene
        Scene scene = new Scene(root, 800, 650);
        scene.setFill(Color.TRANSPARENT);

        // Null checker for styles.css
        URL cssUrl = getClass().getResource("styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Unable to find styles.css");
        }

        // Set scene
        primaryStage.setScene(scene);
        primaryStage.show();

        // Dragging the window
        windowControlsPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        windowControlsPane.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        // Import items from file on application startup

        File defaultFile = new File("src/main/resources/edu/ucsi/bookkeeper/files/users.txt"); // Specify the path to your default text file
        importFromFile(defaultFile);
    }

    private static class Item {
        private final StringProperty firstName;
        private final StringProperty lastName;
        private final IntegerProperty id;
        private final StringProperty email;
        private final StringProperty gender;

        public Item(String firstName, String lastName, int id, String email, String gender) {
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

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), userEditPane);
            slideIn.setFromX(userViewPane.getWidth());
            slideIn.setToX(0);
            slideIn.play();
            isUserEditPaneVisible = true;

            // Clear existing text fields
            clearUserEditFields(userEditPane);
        } else {
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(1000), userEditPane);
            slideOut.setFromX(0);
            slideOut.setToX(userViewPane.getWidth());
            slideOut.setOnFinished(e -> {
                userEditPane.setVisible(false);

                // Clear text fields after animation finishes
                clearUserEditFields(userEditPane);
            });
            slideOut.play();
            isUserEditPaneVisible = false;
            updateTablePane(importedItems);
        }
    }

    private void clearUserEditFields(VBox userEditPane) {
        for (Node node : userEditPane.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).clear();
            }
        }
    }

    private void populateUserEditFields(VBox userEditFieldsPane, Item item) {

        // Create user edit title label
        Label userEditTitleLabel = new Label("Edit User");
        userEditTitleLabel.getStyleClass().add("main-title");

        // Create button pane for OK button
        HBox userEditButtonPane = new HBox();
        userEditButtonPane.setAlignment(Pos.CENTER_LEFT);
        userEditButtonPane.getStyleClass().add("button-pane");

        // Create OK button
        Button okButton = new Button("OK");
        okButton.getStyleClass().add("important-button");
        okButton.setOnAction(event -> {
            // Update the item with the edited values
            item.setFirstName(firstNameField.getText());
            item.setLastName(lastNameField.getText());
            item.setId(Integer.parseInt(idField.getText()));
            item.setEmail(emailField.getText());
            item.setGender(genderField.getText());

            // FIXME Refresh the rowBox to reflect the changes
            // populateTableRowBox(userEditFieldsPane, item);

            // Hide the user edit pane
            toggleUserEditPane(userPane);
        });


        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            // Restore the original content of the rowBox
//            populateTableRowBox(userEditFieldsPane, item);

            // Hide the user edit pane without saving any changes
            toggleUserEditPane(userPane);
        });

        // Add OK button to button pane
        userEditButtonPane.getChildren().addAll(okButton, cancelButton);

        // Create delete icon
        ImageView deleteIcon = null;
        InputStream imageStreamDelete = getClass().getResourceAsStream("images/delete.png");
        if (imageStreamDelete != null) {
            Image deleteImage = new Image(imageStreamDelete);
            deleteIcon = new ImageView(deleteImage);
            deleteIcon.setFitWidth(26);
            deleteIcon.setFitHeight(26);
            deleteIcon.getStyleClass().add("file-pane-icon");
        } else {
            System.err.println("Unable to load delete.png");
        }

        // Create labels for text lines
        Label deleteLabel = new Label("Delete");
        deleteLabel.getStyleClass().add("file-pane-label");
        Label deleteInfoLabel = new Label("Irreversible action");
        deleteInfoLabel.getStyleClass().add("file-pane-info-label");

        // Create HBox to hold icon and labels
        HBox deleteButtonContent = new HBox();
        deleteButtonContent.getChildren().addAll(deleteIcon, new VBox(deleteLabel, deleteInfoLabel));
        deleteButtonContent.setAlignment(Pos.CENTER_LEFT);

        // Create button and set its content to the HBox
        Button deleteButton = new Button();
        deleteButton.getStyleClass().add("file-button");
        deleteButton.setGraphic(deleteButtonContent);
        deleteButton.setPrefWidth(169);
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Item");
            alert.setContentText("Are you sure you want to delete this item?");
        });

        // Create HBox for delete button
        HBox filePane = new HBox();
        filePane.getStyleClass().add("file-pane");
        filePane.getChildren().addAll(deleteButton);
        filePane.setAlignment(Pos.BASELINE_RIGHT);

        // Clear the existing content
        userEditFieldsPane.getChildren().clear();

        // Create first name icon
        ImageView firstNameIcon = null;
        InputStream imageStreamFirstName = getClass().getResourceAsStream("images/first-name.png");
        if (imageStreamFirstName != null) {
            Image firstNameImage = new Image(imageStreamFirstName);
            firstNameIcon = new ImageView(firstNameImage);
            firstNameIcon.setFitWidth(24);
            firstNameIcon.setFitHeight(24);
            firstNameIcon.getStyleClass().add("edit-icon");
        } else {
            System.err.println("Unable to load first-name.png");
        }

        // Create first name label
        Label firstNameLabel = new Label("First Name");
        firstNameLabel.getStyleClass().add("main-label");
        firstNameLabel.setPrefWidth(210);

        // Create first name field
        TextField firstNameField = new TextField();
        firstNameField.getStyleClass().add("text-field");
        firstNameField.setText(item.getFirstName());

        // Create first name pane
        HBox firstNamePane = new HBox(firstNameIcon, firstNameLabel, firstNameField);
        firstNamePane.getStyleClass().add("edit-row");
        firstNamePane.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(firstNameField, Priority.ALWAYS);

        // Create last name icon
        ImageView lastNameIcon = null;
        InputStream imageStreamLastName = getClass().getResourceAsStream("images/last-name.png");
        if (imageStreamLastName != null) {
            Image lastNameImage = new Image(imageStreamLastName);
            lastNameIcon = new ImageView(lastNameImage);
            lastNameIcon.setFitWidth(24);
            lastNameIcon.setFitHeight(24);
            lastNameIcon.getStyleClass().add("edit-icon");
        } else {
            System.err.println("Unable to load last-name.png");
        }

        // Create last name label
        Label lastNameLabel = new Label("Last Name");
        lastNameLabel.getStyleClass().add("main-label");
        lastNameLabel.setPrefWidth(210);

        // Create last name field
        TextField lastNameField = new TextField();
        lastNameField.getStyleClass().add("text-field");
        lastNameField.setText(item.getLastName());

        // Create last name pane
        HBox lastNamePane = new HBox(lastNameIcon, lastNameLabel, lastNameField);
        lastNamePane.getStyleClass().add("edit-row");
        lastNamePane.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(lastNameField, Priority.ALWAYS);

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
        idLabel.setPrefWidth(210);

        // Create id details label
        Label idDetailLabel = new Label("Use official ID provided by university");
        idDetailLabel.getStyleClass().add("detail-label");

        // Create email label pane
        VBox idLabelPane = new VBox(idLabel, idDetailLabel);

        // Create id name field
        TextField idField = new TextField();
        idField.getStyleClass().add("text-field");
        idField.setText(String.valueOf(item.getId()));

        // Create id name pane
        HBox idPane = new HBox(idIcon, idLabelPane, idField);
        idPane.getStyleClass().add("edit-row");
        idPane.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(idField, Priority.ALWAYS);

        // Create email icon
        ImageView emailIcon = null;
        InputStream imageStreamEmail = getClass().getResourceAsStream("images/email.png");
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
        emailLabel.setPrefWidth(210);

        // Create email details label
        Label emailDetailLabel = new Label("Use official university email address");
        emailDetailLabel.getStyleClass().add("detail-label");

        // Create email label pane
        VBox emailLabelPane = new VBox(emailLabel, emailDetailLabel);

        // Create email name field
        TextField emailField = new TextField();
        emailField.getStyleClass().add("text-field");
        emailField.setText(String.valueOf(item.getEmail()));

        // Create email name pane
        HBox emailPane = new HBox(emailIcon, emailLabelPane, emailField);
        emailPane.getStyleClass().add("edit-row");
        emailPane.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(emailField, Priority.ALWAYS);

        // Create gender icon
        ImageView genderIcon = null;
        InputStream imageStreamGender = getClass().getResourceAsStream("images/gender.png");
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
        Label genderLabel = new Label("Gender");
        genderLabel.getStyleClass().add("main-label");
        genderLabel.setPrefWidth(210);

        // Create gender choice box
        ChoiceBox<String> genderChoiceBox = new ChoiceBox<>();
        genderChoiceBox.getItems().addAll("Male", "Female");
        if (item.getGender().equals("Male")) { // Set default selection
            genderChoiceBox.setValue("Male");
        } else {
            genderChoiceBox.setValue("Female");
        }

        // Create gender pane
        HBox genderPane = new HBox(genderIcon, genderLabel, genderChoiceBox);
        genderPane.getStyleClass().add("edit-row");
        genderPane.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(genderChoiceBox, Priority.ALWAYS);


        // Create row pane
        VBox fieldsPane = new VBox(firstNamePane, lastNamePane, idPane, emailPane, genderPane);
        fieldsPane.getStyleClass().add("fields-pane");
        fieldsPane.setAlignment(Pos.CENTER_LEFT); // Vertically centers forward icon

        userEditFieldsPane.getChildren().addAll(userEditTitleLabel, filePane, userEditButtonPane, fieldsPane);
        userEditFieldsPane.getStyleClass().add("edit-pane");
    }

    private void toggleBookEditPane(StackPane bookPane) {
        VBox bookEditPane = (VBox) bookPane.getChildren().get(1);

        if (!isBookEditPaneVisible) {
            bookEditPane.setVisible(true);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), bookEditPane);
            slideIn.setFromX(bookEditPane.getWidth());
            slideIn.setToX(0);
            slideIn.play();
            isBookEditPaneVisible = true;
        } else {
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(1000), bookEditPane);
            slideOut.setFromX(0);
            slideOut.setToX(bookEditPane.getWidth());
            slideOut.setOnFinished(e -> bookEditPane.setVisible(false));
            slideOut.play();
            isBookEditPaneVisible = false;
        }
    }

    private void importFromFile(File file) {
        importedItems.clear(); // Clear the ArrayList before importing

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
                    importedItems.add(new Item(firstName, lastName, id, email, gender));
                }
            }
            updateTablePane(importedItems);

        } catch (IOException e) {
            e.printStackTrace();
            openImportDialog();
        }
    }

    private void openImportDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Items");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            importFromFile(file);
        }
    }

    private void exportToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Items");
        fileChooser.setInitialFileName("Users.txt");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Item item : importedItems) {
                    String line = String.format("%s,%s,%d,%s,%s",
                            item.getFirstName(),
                            item.getLastName(),
                            item.getId(),
                            item.getEmail(),
                            item.getGender());
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addItem() {
        Item item = new Item("First Name", "Last Name", generateId(), "Email", "Gender");
        editItem(item);
    }

    private void editItem(Item item) {
//        tableView.getSelectionModel().select(item);

        toggleUserEditPane(userPane);
        populateUserEditFields(userEditPane, item);
    }
    private void updateTablePane(ArrayList<Item> items) {
        // Clear existing rows
        tablePane.getChildren().clear();

        for (Item item : items) {

            // Create male icon
            ImageView maleIcon = null;
            InputStream imageStreamMale = getClass().getResourceAsStream("images/male.png");
            if (imageStreamMale != null) {
                Image maleImage = new Image(imageStreamMale);
                maleIcon = new ImageView(maleImage);
                maleIcon.setFitWidth(32);
                maleIcon.setFitHeight(32);
                maleIcon.getStyleClass().add("gender-icon");
            } else {
                System.err.println("Unable to load male.png");
            }

            // Create female icon
            ImageView femaleIcon = null;
            InputStream imageStreamFemale = getClass().getResourceAsStream("images/female.png");
            if (imageStreamFemale != null) {
                Image femaleImage = new Image(imageStreamFemale);
                femaleIcon = new ImageView(femaleImage);
                femaleIcon.setFitWidth(32);
                femaleIcon.setFitHeight(32);
                femaleIcon.getStyleClass().add("gender-icon");
            } else {
                System.err.println("Unable to load female.png");
            }

            // Create gender icon
            ImageView genderIcon;
            if (item.getGender().equals("Male")) {
                genderIcon = maleIcon;
            } else {
                genderIcon = femaleIcon;
            }

            // Create forward icon
            ImageView forwardIcon = null;
            InputStream imageStreamForward = getClass().getResourceAsStream("images/forward.png");
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
            Label firstNameLabel = new Label(item.getFirstName());
            firstNameLabel.getStyleClass().add("main-label");
            Label lastNameLabel = new Label(item.getLastName());
            lastNameLabel.getStyleClass().add("detail-label");
            VBox nameField = new VBox(firstNameLabel, lastNameLabel);
            nameField.setPrefWidth(180); // Custom width

            // Create id & email field
            Label idLabel = new Label(String.valueOf(item.getId()));
            idLabel.getStyleClass().add("main-label");
            Label emailLabel = new Label(item.getEmail());
            emailLabel.getStyleClass().add("detail-label");
            VBox idEmailField = new VBox(idLabel, emailLabel);

            // Add spacer field to right-align forward icon
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Create row pane
            HBox rowPane = new HBox(genderIcon, nameField, idEmailField, spacer, forwardIcon);
            rowPane.getStyleClass().add("row-pane");
            rowPane.setAlignment(Pos.CENTER_LEFT); // Vertically centers forward icon

            // Add click handler to row pane
            rowPane.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    editItem(item);
                }
            });

            // Add row pane to table pane
            tablePane.getChildren().add(rowPane);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}