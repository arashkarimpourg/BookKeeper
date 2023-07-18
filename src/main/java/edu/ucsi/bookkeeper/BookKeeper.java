package edu.ucsi.bookkeeper;

import javafx.application.Application;
import javafx.animation.TranslateTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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
import java.util.Optional;

public class BookKeeper extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isUserEditPaneVisible = false;
    private boolean isBookEditPaneVisible = false;
    private TableView<Item> tableView;
    private ObservableList<Item> items;
    private StackPane userPane;
    private VBox userEditPane;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField idField;

    private int generateId() {
        // Generate a unique ID based on the current timestamp
        return (int) System.currentTimeMillis();
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // Create TableView and ObservableList
        tableView = new TableView<>();
        items = FXCollections.observableArrayList();
        tableView.getStyleClass().add("table-view");


        // Set custom cell factory for the TableView
        tableView.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>();
            row.getStyleClass().add("table-row");
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    Item item = row.getItem();
                    if (item != null) {
                        toggleUserEditPane(userPane);
                        populateUserEditFields(userEditPane, item);
                    }
                }
            });
            return row;
        });

        // Create columns for the TableView
        TableColumn<Item, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());

        TableColumn<Item, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        TableColumn<Item, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        // Add columns to the TableView
        tableView.getColumns().add(firstNameColumn);
        tableView.getColumns().add(lastNameColumn);
        tableView.getColumns().add(idColumn);

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
        userPane.setId("user-pane");

        // Create user content pane
        VBox userContentPane = new VBox();
        userContentPane.setId("user-content-pane");
        userContentPane.setFillWidth(true);

        // Create user content pane title
        Label userContentPaneTitle = new Label("Users");
        userContentPaneTitle.getStyleClass().add("main-pane-title");

        // Create Add button
        Button addButton = new Button("Add");
        addButton.getStyleClass().add("add-button");
        addButton.setOnAction(event -> addItem());

        // Create HBox for Add button
        HBox addPane = new HBox();
        addPane.getStyleClass().add("add-pane");
        addPane.getChildren().add(addButton);
        addPane.setAlignment(Pos.CENTER_LEFT);

        // Create import icon
        ImageView importIcon = null;
        InputStream imageStreamImport = getClass().getResourceAsStream("images/file-pane/import.png");
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

        // Create button and set its content to the HBox
        Button importButton = new Button();
        importButton.getStyleClass().add("file-button");
        importButton.setGraphic(importButtonContent);
        importButton.setPrefWidth(150);

        importButton.setOnAction(event -> importFromFile());

        // Create export icon
        ImageView exportIcon = null;
        InputStream imageStreamExport = getClass().getResourceAsStream("images/file-pane/export.png");
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

        // Add user content pane and user edit title to user content pane
        userContentPane.getChildren().addAll(userContentPaneTitle, filePane, addPane, tableView);

        VBox.setVgrow(tableView, Priority.ALWAYS);

        // Create user edit pane
        userEditPane = new VBox();
        userEditPane.setId("user-edit-pane");
        userEditPane.setVisible(false);

        // Add user content pane and user edit pane to user pane
        userPane.getChildren().addAll(userContentPane, userEditPane);

        userEditPane.setTranslateX(userContentPane.getWidth());

        // Create book pane
        StackPane bookPane = new StackPane();
        bookPane.setId("book-pane");

        // Create book content pane
        VBox bookContentPane = new VBox();
        bookContentPane.setId("book-content-pane");

        // Create book content pane title
        Label bookContentPaneTitle = new Label("Books");
        bookContentPaneTitle.getStyleClass().add("main-pane-title");

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



        BorderPane.setMargin(leftPane, new Insets(0, 10, 0, 0));

        // Add left pane & right pane to main window
        root.setLeft(leftPane);
        root.setCenter(rightPane);

        // Create scene
        Scene scene = new Scene(root, 800, 600);
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
        importFromFile();
    }

    private static class Item {
        private final StringProperty firstName;
        private final StringProperty lastName;
        private final IntegerProperty id;

        public Item(String firstName, String lastName, int id) {
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.id = new SimpleIntegerProperty(id);
        }

        public StringProperty firstNameProperty() {
            return firstName;
        }

        public StringProperty lastNameProperty() {
            return lastName;
        }

        public IntegerProperty idProperty() {
            return id;
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

        public void setFirstName(String firstName) {
            this.firstName.set(firstName);
        }

        public void setLastName(String lastName) {
            this.lastName.set(lastName);
        }

        public void setId(int id) {
            this.id.set(id);
        }
    }

    private void toggleUserEditPane(StackPane userPane) {
        VBox userContentPane = (VBox) userPane.getChildren().get(0);
        VBox userEditPane = (VBox) userPane.getChildren().get(1);

        if (!isUserEditPaneVisible) {
            userEditPane.setVisible(true);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), userEditPane);
            slideIn.setFromX(userContentPane.getWidth());
            slideIn.setToX(0);
            slideIn.play();
            isUserEditPaneVisible = true;

            // Clear existing text fields
            clearUserEditFields(userEditPane);
        } else {
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(1000), userEditPane);
            slideOut.setFromX(0);
            slideOut.setToX(userContentPane.getWidth());
            slideOut.setOnFinished(e -> {
                userEditPane.setVisible(false);

                // Clear text fields after animation finishes
                clearUserEditFields(userEditPane);
            });
            slideOut.play();
            isUserEditPaneVisible = false;
            tableView.refresh();
        }
    }

    private void clearUserEditFields(VBox userEditPane) {
        for (Node node : userEditPane.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).clear();
            }
        }
    }

    private void populateUserEditFields(VBox userEditPane, Item item) {
        // Clear the existing content
        userEditPane.getChildren().clear();

        // Create text fields
        firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        firstNameField.setText(item.getFirstName());

        lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        lastNameField.setText(item.getLastName());

        idField = new TextField();
        idField.setPromptText("ID");
        idField.setText(String.valueOf(item.getId()));

        // Create buttons
        Button okButton = new Button("OK");
        okButton.setOnAction(event -> {
            // Update the item with the edited values
            item.setFirstName(firstNameField.getText());
            item.setLastName(lastNameField.getText());
            item.setId(Integer.parseInt(idField.getText()));

            // Hide the user edit pane
            toggleUserEditPane(userPane);
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            // Hide the user edit pane without saving any changes
            toggleUserEditPane(userPane);
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Item");
            alert.setContentText("Are you sure you want to delete this item?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                items.remove(item);
                toggleUserEditPane(userPane);
            }
        });


        // Create a container for the buttons
        HBox buttonPane = new HBox(10, okButton, cancelButton, deleteButton);

        // Add the components to the user edit pane
        userEditPane.getChildren().addAll(firstNameField, lastNameField, idField, buttonPane);
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

    private void importFromFile() {
        File defaultFile = new File("src/main/resources/edu/ucsi/bookkeeper/files/users.txt"); // Specify the path to your default text file

        if (defaultFile.exists()) {
            importFromFile(defaultFile);
        } else {
            openImportDialog();
        }
    }

    private void importFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            ObservableList<Item> importedItems = FXCollections.observableArrayList();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String firstName = parts[0];
                    String lastName = parts[1];
                    int id = Integer.parseInt(parts[2]);
                    importedItems.add(new Item(firstName, lastName, id));
                }
            }
            items.setAll(importedItems);
            tableView.setItems(items);
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
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Item item : items) {
                    writer.write(item.getFirstName() + "," + item.getLastName() + "," + item.getId());
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addItem() {
        Item item = new Item("First Name", "Last Name", generateId());
        items.add(item);
        tableView.getSelectionModel().select(item);
        editItem(item);
    }


    private void editItem(Item item) {
        tableView.getSelectionModel().select(item);
        toggleUserEditPane(userPane);
    }


    public static void main(String[] args) {
        launch(args);
    }
}