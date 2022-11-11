import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Board extends GridPane {
    private Move oldMove;
    private Timeline timeline = null;
    private int plotSpeed = 500; // in millis
    public Board() {

    }

    public int getPlotSpeed() {
        return plotSpeed;
    }
    public void setPlotSpeed(int plotSpeed) {
        this.plotSpeed = plotSpeed;
    }
    public void Reset() {
        this.stopPlottingMoves();
        this.getChildren().clear();
        this.drawChessboard();
    }
    public void drawChessboard() {
        // Create 64 rectangles and add to pane
        int count = 0;
        double s = 100; // side of rectangle
        for (int i = 0; i < 8; i++) {
            count++;
            for (int j = 0; j < 8; j++) {
                Rectangle r = new Rectangle(s, s, s, s);
                if (count % 2 == 0)
                    r.setFill(Color.ANTIQUEWHITE);
                else {
                    r.setFill(Color.CHOCOLATE);
                }

                this.add(r, j, i);
                count++;
            }
        }
    }
    public void startPlottingMoves(KnightTour tour) {
        AtomicInteger counter = new AtomicInteger();
        timeline = new Timeline(
                new KeyFrame(Duration.millis(this.getPlotSpeed()), (ActionEvent event) -> {
                    Move currentMove = tour.getMoves().get(counter.getAndIncrement());

                    this.push(currentMove);

                    if (currentMove.getMoveNo() == tour.getMoves().size()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);

                        if (tour.getMoves().size() == 64) {
                            Move firstMove = tour.getMoves().get(0);
                            Move lastMove = tour.getMoves().get(tour.getMoves().size() - 1);

                            alert.setTitle("Victory!");

                            if (tour.isClosedTour(lastMove.getRow(), lastMove.getCol(), firstMove.getRow(), firstMove.getCol())) {
                                alert.setHeaderText("It is a closed tour!");
                            }
                            else {
                                alert.setHeaderText("It is an open tour!");
                            }

                            alert.setContentText("A knight's tour can be closed, which means that the knight ends up one move away from the beginning square, or open, which means that the knight ends up somewhere else.");
                        }
                        else {
                            alert.setTitle("Defeat!");
                            alert.setHeaderText("Total moves: " + tour.getMoves().size());
                            alert.setContentText("Better luck next time.");
                        }

                        alert.show();
                    }
                }));

        timeline.setCycleCount(tour.getMoves().size());
        timeline.play();
    }
    public void pausePlottingMoves() {
        if (timeline != null) {
            timeline.pause();
        }
    }
    public void resumePlottingMoves() {
        if (timeline != null) {
            timeline.play();
        }
    }
    public void stopPlottingMoves() {
        if (timeline != null) {
            timeline.stop();
        }
    }
    public Animation.Status getStatusPlottingMoves() {
        return timeline.getStatus();
    }
    public void push(Move currentMove) {
        Label lblMoveNo = new Label(Integer.toString(currentMove.getMoveNo() - 1));
        lblMoveNo.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(lblMoveNo, 0.0);
        AnchorPane.setRightAnchor(lblMoveNo, 0.0);
        lblMoveNo.setAlignment(Pos.CENTER);
        lblMoveNo.setFont(new Font("Arial", 45));
        lblMoveNo.setAccessibleRole(AccessibleRole.TEXT);
        lblMoveNo.setId("N" + Integer.toString(currentMove.getMoveNo()));

        Label lblKnight = new Label("♞");
        lblKnight.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(lblKnight, 0.0);
        AnchorPane.setRightAnchor(lblKnight, 0.0);
        lblKnight.setAlignment(Pos.CENTER);
        lblKnight.setFont(new Font("Arial", 45));
        lblKnight.setAccessibleRole(AccessibleRole.TEXT);
        lblKnight.setId("K" + Integer.toString(currentMove.getMoveNo()));

        if (oldMove == null) {
            oldMove = new Move(currentMove);
        }

        if (oldMove != null && currentMove.getMoveNo() > 1) {
            Node nodeToRemove = null;
            for (Node n : this.getChildren()) {
                if (n.getAccessibleRole() == AccessibleRole.TEXT) {
                    if (n.getId().contains("K" + Integer.toString(oldMove.getMoveNo()))) {
                        nodeToRemove = n;
                    }
                }
            }

            this.getChildren().remove(nodeToRemove);
            this.add(lblMoveNo, oldMove.getCol(), oldMove.getRow());
        }

        this.add(lblKnight, currentMove.getCol(), currentMove.getRow());

        oldMove = currentMove;
    }
}
