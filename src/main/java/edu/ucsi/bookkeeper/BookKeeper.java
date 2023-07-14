package edu.ucsi.bookkeeper;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.InputStream;
import java.net.URL;

public class BookKeeper extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // Left Sidebar
        VBox leftSidebar = new VBox();
        leftSidebar.getStyleClass().add("left-sidebar");
        leftSidebar.setPrefWidth(200);

        // Right Pane
        VBox rightPane = new VBox();
        rightPane.getStyleClass().add("right-pane");

        // Title Bar
        HBox titleBar = new HBox();
        titleBar.getStyleClass().add("title-bar");
        titleBar.setAlignment(Pos.TOP_RIGHT);

        Button closeButton = new Button();
        closeButton.setId("close-button");
        closeButton.getStyleClass().add("window-button");
        closeButton.setOnAction(event -> primaryStage.close());

        // Imports the PNG file while avoiding NullPointerException
        InputStream imageStreamClose = getClass().getResourceAsStream("images/title-bar/close.png");
        if (imageStreamClose != null) {
            Image closeImage = new Image(imageStreamClose);
            ImageView closeIcon = new ImageView(closeImage);
            closeIcon.setFitWidth(14);
            closeIcon.setFitHeight(14);
            closeButton.setGraphic(closeIcon);
        } else {
            System.err.println("Unable to load close.png");
        }

        Button maximizeButton = new Button();
        maximizeButton.getStyleClass().add("window-button");
        maximizeButton.setOnAction(event -> primaryStage.setMaximized(!primaryStage.isMaximized()));

        // Imports the PNG file while avoiding NullPointerException
        InputStream imageStreamMaximize = getClass().getResourceAsStream("images/title-bar/maximize.png");
        if (imageStreamMaximize != null) {
            Image maximizeImage = new Image(imageStreamMaximize);
            ImageView maximizeIcon = new ImageView(maximizeImage);
            maximizeIcon.setFitWidth(14);
            maximizeIcon.setFitHeight(14);
            maximizeButton.setGraphic(maximizeIcon);
        } else {
            System.err.println("Unable to load maximize.png");
        }

        Button minimizeButton = new Button();
        minimizeButton.getStyleClass().add("window-button");
        minimizeButton.setOnAction(event -> primaryStage.setIconified(true));

        // Imports the PNG file while avoiding NullPointerException
        InputStream imageStreamMinimize = getClass().getResourceAsStream("images/title-bar/minimize.png");
        if (imageStreamMinimize != null) {
            Image minimizeImage = new Image(imageStreamMinimize);
            ImageView minimizeIcon = new ImageView(minimizeImage);
            minimizeIcon.setFitWidth(14);
            minimizeIcon.setFitHeight(14);
            minimizeButton.setGraphic(minimizeIcon);
        } else {
            System.err.println("Unable to load minimize.png");
        }

        titleBar.getChildren().addAll(minimizeButton, maximizeButton, closeButton);

        // Content Pane
        Pane contentPane = new Pane();
        contentPane.getStyleClass().add("content-pane");

        rightPane.getChildren().addAll(titleBar, contentPane);

        // Main Layout
        BorderPane root = new BorderPane();
        root.getStyleClass().add("rounded-corner-pane");
        root.setLeft(leftSidebar);
        root.setCenter(rightPane);

        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.TRANSPARENT);

        // Imports the CSS file while avoiding NullPointerException
        URL cssUrl = getClass().getResource("styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Unable to find styles.css");
        }

        primaryStage.setScene(scene);
        primaryStage.show();

        // Dragging the window
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}