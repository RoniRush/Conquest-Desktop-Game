package JavaFXUI.ShopTile;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

import JavaFXUI.Model;
import bottom.Player;
import bottom.Slot;
import bottom.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ShopTileController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox shopTile;

    @FXML
    private VBox unitNameBox;

    @FXML
    private Label unitName;

    @FXML
    private VBox unitFinalBox;

    @FXML
    private VBox unitModBox;

    @FXML
    private Label rank;

    @FXML
    private Label totalCost;

    @FXML
    private Label costSingle;

    @FXML
    private Label maxMight;

    @FXML
    private Label fatigue;

    @FXML
    private Label totalOnBoard;

    public void initTile(Unit unit, Model model) {
        unitName.setText(unit.getType());
        rank.setText(Integer.toString(unit.getRank()));
        totalCost.setText(Integer.toString(unit.getCost()));
        maxMight.setText(Integer.toString(unit.getMaximumMight()));
        fatigue.setText(Integer.toString(unit.getFatigueFactor()));
        Double num= (double) unit.getCost() / unit.getMaximumMight();
        costSingle.setText(new DecimalFormat("0.00").format(num));
        List<Unit> units= model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound()-1).getModel();
        model.getStartTurnProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                for (int i = 0; i < units.size(); i++) {
                    if (units.get(i).getRank() == unit.getRank())
                        totalOnBoard.setText(Integer.toString(model.getGameManager().getRounds().get(model.getGameManager().
                                getNumOfRound()).getUnitsOnBoard()[i].get()));
                }
            }
        });

    }
}
