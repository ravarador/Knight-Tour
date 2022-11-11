import javafx.animation.Animation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import static java.lang.Double.MAX_VALUE;

public class Main extends Application {
    Board board = new Board();
    KnightTour tour = new KnightTour();

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        board.drawChessboard();

        Scene sceneBoard = new Scene(board);
        Stage stageBoard = new Stage();
        stageBoard.setTitle("Chessboard - JAC444 Bonus Workshop");
        stageBoard.setScene(sceneBoard);

        StackPane paneMenu = new StackPane();

        // Will contain everything for chessboard pane
        VBox vBox = new VBox();

        // Will contain all buttons in the menu
        VBox vboxButtons = new VBox();

        // Shows and hides the board
        Button btnToggleBoard = new Button("Show/Hide Board");
        btnToggleBoard.setMaxWidth(MAX_VALUE);

        // Button for starting the tour animation
        Button btnStartTour = new Button("Start Tour");
        btnStartTour.setMaxWidth(MAX_VALUE);

        // Container for play and pause buttons
        HBox hboxToggleTour = new HBox();

        // Button for playing the tour animation
        Button btnPlayTour = new Button("Play Tour");
        btnPlayTour.setDisable(true);
        btnPlayTour.setMaxWidth(MAX_VALUE);
        HBox.setHgrow(btnPlayTour, Priority.ALWAYS);

        // Button for pausing the tour animation
        Button btnPauseTour = new Button("Pause Tour");
        btnPauseTour.setDisable(true);
        btnPauseTour.setMaxWidth(MAX_VALUE);
        HBox.setHgrow(btnPauseTour, Priority.ALWAYS);

        Button btnResetBoard = new Button("Reset Board");
        btnResetBoard.setMaxWidth(MAX_VALUE);
        btnResetBoard.setDisable(true);

        VBox vboxSettings = new VBox();
        vboxSettings.setPadding(new Insets(10));

        Label lblSettings = new Label("SETTINGS");
        lblSettings.setFont(new Font("Arial", 8));
        lblSettings.setPadding(new Insets(10));
        lblSettings.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(lblSettings, 0.0);
        AnchorPane.setRightAnchor(lblSettings, 0.0);
        lblSettings.setAlignment(Pos.CENTER);

        HBox hBoxSpeed = new HBox();
        Label lblSpeed = new Label("Set speed: ");
        lblSpeed.setMinWidth(80);

        Tooltip tooltipSpeed = new Tooltip("Speed should be between 0 and 999999. Value should be in milliseconds.");
        tooltipSpeed.setShowDelay(Duration.millis(1));

        TextField txtSpeed = new TextField(Integer.toString(board.getPlotSpeed()));
        txtSpeed.setTooltip(tooltipSpeed);

        HBox hBoxRow = new HBox();
        Label lblRow = new Label("Set start row: ");
        lblRow.setMinWidth(80);

        Tooltip tooltipRow = new Tooltip("Row should be between 0 and 7.");
        tooltipRow.setShowDelay(Duration.millis(1));

        TextField txtRow = new TextField(Integer.toString(tour.getStartRow()));
        txtRow.setTooltip(tooltipRow);

        HBox hBoxCol = new HBox();
        Label lblCol = new Label("Set start col: ");
        lblCol.setMinWidth(80);

        Tooltip tooltipCol = new Tooltip("Col should be between 0 and 7.");
        tooltipCol.setShowDelay(Duration.millis(1));

        TextField txtCol = new TextField(Integer.toString(tour.getStartCol()));
        txtCol.setTooltip(tooltipCol);

        btnToggleBoard.setOnAction(actionEvent -> {
            if (stageBoard.isShowing()) {
                stageBoard.hide();
            }
            else {
                stageBoard.show();
            }
        });

        btnStartTour.setOnAction(actionEvent -> {
            tour.startTour(Integer.parseInt(txtRow.getText()), Integer.parseInt(txtCol.getText()));
            board.startPlottingMoves(tour);
            btnStartTour.setDisable(true);
            btnResetBoard.setDisable(false);
            btnPlayTour.setDisable(true);
            btnPauseTour.setDisable(false);

        });

        btnPlayTour.setOnAction(actionEvent -> {
            if (board.getStatusPlottingMoves() == Animation.Status.PAUSED) {
                board.resumePlottingMoves();
                btnPauseTour.setDisable(false);
                btnPlayTour.setDisable(true);
            }
        });

        btnPauseTour.setOnAction(actionEvent -> {
            if (board.getStatusPlottingMoves() == Animation.Status.RUNNING) {
                board.pausePlottingMoves();
                btnPlayTour.setDisable(false);
                btnPauseTour.setDisable(true);
            }
        });

        btnResetBoard.setOnAction(actionEvent -> {
            board.Reset();
            tour.Reset();
            btnStartTour.setDisable(false);
            btnPlayTour.setDisable(true);
            btnPauseTour.setDisable(true);
            btnResetBoard.setDisable(true);
        });

        txtSpeed.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.length() > 6) {
                txtSpeed.setText(oldValue);
                txtSpeed.positionCaret(txtSpeed.getLength());
            }
        });

        txtSpeed.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue && (txtSpeed.getLength() == 0 || txtSpeed.getText().equals("0"))) {
                txtSpeed.setText(Integer.toString(board.getPlotSpeed()));
            }
            else {
                board.setPlotSpeed(Integer.parseInt(txtSpeed.getText()));
            }
        });

        txtRow.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.length() > 1) {
                txtRow.setText(oldValue);
                txtRow.positionCaret(txtRow.getLength());
            }
        });

        txtRow.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue && (txtRow.getLength() == 0 || Integer.parseInt(txtRow.getText()) > 7)) {
                txtRow.setText(Integer.toString(tour.getStartRow()));
            }
            else {
                tour.setStartRow(Integer.parseInt(txtRow.getText()));
            }
        });

        txtCol.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.length() > 1) {
                txtCol.setText(oldValue);
                txtCol.positionCaret(txtCol.getLength());
            }
        });

        txtCol.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue && (txtCol.getLength() == 0 || Integer.parseInt(txtCol.getText()) > 7)) {
                txtCol.setText(Integer.toString(tour.getStartRow()));
            }
            else {
                tour.setStartRow(Integer.parseInt(txtCol.getText()));
            }
        });

        hboxToggleTour.getChildren().addAll(btnPlayTour, btnPauseTour);

        vboxButtons.getChildren().addAll(btnToggleBoard, btnStartTour, hboxToggleTour, btnResetBoard);

        hBoxSpeed.getChildren().addAll(lblSpeed, txtSpeed);

        hBoxRow.getChildren().addAll(lblRow, txtRow);

        hBoxCol.getChildren().addAll(lblCol, txtCol);

        vboxSettings.getChildren().addAll(lblSettings, hBoxSpeed, hBoxRow, hBoxCol);

        vBox.getChildren().addAll(vboxButtons, vboxSettings);

        paneMenu.getChildren().addAll(vBox);


        Scene sceneMenu = new Scene(paneMenu);

        primaryStage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
        });

        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setTitle("Menu - JAC444 Bonus Workshop");
        primaryStage.setScene(sceneMenu);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}