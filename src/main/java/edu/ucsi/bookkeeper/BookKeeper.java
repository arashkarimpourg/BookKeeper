package edu.ucsi.bookkeeper;

import javafx.application.Application;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.net.URL;

public class BookKeeper extends Application {

    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isUserEditPaneVisible = false;
    private boolean isBookEditPaneVisible = false;

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
        Pane mainPane = new Pane();
        mainPane.setId("main-pane");

        // Create user pane
        StackPane userPane = new StackPane();
        userPane.setId("user-pane");

        // Create user content pane
        VBox userContentPane = new VBox();
        userContentPane.setId("user-content-pane");

        // Create user content pane title
        Label userContentPaneTitle = new Label("Users");
        userContentPaneTitle.getStyleClass().add("main-pane-title");

        // Create user edit button
        Button userEditButton = new Button("EDIT USER");
        userEditButton.setOnAction(e -> toggleUserEditPane(userPane));

        // Add user content pane and user edit title to user content pane
        userContentPane.getChildren().addAll(userContentPaneTitle, userEditButton);

        // Create user edit pane
        VBox userEditPane = new VBox();
        userEditPane.setId("user-edit-pane");
        userEditPane.setTranslateX(userContentPane.getWidth());
        userEditPane.setVisible(false);

        // Add user content pane and user edit pane to user pane
        userPane.getChildren().addAll(userContentPane, userEditPane);

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
        backButton.setOnAction(e -> toggleUserEditPane(userPane));

        // Set action for user tab
        userTab.setOnAction(event -> {
            mainPane.getChildren().clear();
            mainPane.getChildren().add(userPane);
            userTab.getStyleClass().setAll("sidebar-button", "sidebar-button-selected");
            bookTab.getStyleClass().setAll("sidebar-button");

        });

        // Set action for book tab
        bookTab.setOnAction(event -> {
            mainPane.getChildren().clear();
            mainPane.getChildren().add(bookPane);
            bookTab.getStyleClass().setAll("sidebar-button", "sidebar-button-selected");
            userTab.getStyleClass().setAll("sidebar-button");
        });

        // Add user pane to main pane
        mainPane.getChildren().addAll(userPane);

        // Add window controls pane and main pane to right pane
        rightPane.getChildren().addAll(windowControlsPane, mainPane);

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
    }

    private void toggleUserEditPane(StackPane userPane) {
        VBox userEditPane = (VBox) userPane.getChildren().get(1);

        if (!isUserEditPaneVisible) {
            userEditPane.setVisible(true);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), userEditPane);
            slideIn.setFromX(userEditPane.getWidth());
            slideIn.setToX(0);
            slideIn.play();
            isUserEditPaneVisible = true;
        } else {
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(1000), userEditPane);
            slideOut.setFromX(0);
            slideOut.setToX(userEditPane.getWidth());
            slideOut.setOnFinished(e -> userEditPane.setVisible(false));
            slideOut.play();
            isUserEditPaneVisible = false;
        }
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

    public static void main(String[] args) {
        launch(args);
    }
}