package JavaFXUI.PlayerTile;

import JavaFXUI.Model;
import bottom.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

public class PlayerTileController {
    @FXML
    private VBox playerTile;

    @FXML
    private HBox colorBox;

    @FXML
    private Label color;

    @FXML
    private HBox idBox;

    @FXML
    private Label playerID;

    @FXML
    private HBox turingBox;

    @FXML
    private Label totalTuring;

    public void initTile(Model model, Player player) {
        playerID.setText(Integer.toString(player.getId()));
        double []playerColor= player.getColor();
        color.setBackground(new Background(new BackgroundFill(Color.color(playerColor[0],playerColor[1],playerColor[2]),null,null)));
        if(model.isFromUndo())
            player.setTotalTuring(player.getTempTotalTuring());
        totalTuring.textProperty().bind(player.getTotalTuringProperty().asString());
    }
}
