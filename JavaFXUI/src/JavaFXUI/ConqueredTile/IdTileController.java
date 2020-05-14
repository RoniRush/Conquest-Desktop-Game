package JavaFXUI.ConqueredTile;

import bottom.Player;
import bottom.Slot;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class IdTileController {
    @FXML
    private VBox territoryBox;

    @FXML
    private Label idLabel;

    @FXML
    private Label healLabel;

    public void initTile(Slot slot)
    {
        idLabel.setText(Integer.toString(slot.getSerialNumber()));
        healLabel.setText(Integer.toString(slot.getHeal()));
    }
}
