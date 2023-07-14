package edu.ucsi.bookkeeper;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
        titleBar.setPadding(new Insets(10));

        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("window-button");
        closeButton.setOnAction(event -> primaryStage.close());

        Button maximizeButton = new Button("[]");
        maximizeButton.getStyleClass().add("window-button");
        maximizeButton.setOnAction(event -> primaryStage.setMaximized(!primaryStage.isMaximized()));

        Button minimizeButton = new Button("_");
        minimizeButton.getStyleClass().add("window-button");
        minimizeButton.setOnAction(event -> primaryStage.setIconified(true));

        titleBar.getChildren().addAll(closeButton, maximizeButton, minimizeButton);

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