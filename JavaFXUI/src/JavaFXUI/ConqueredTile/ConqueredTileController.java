package JavaFXUI.ConqueredTile;

import bottom.Slot;
import bottom.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ConqueredTileController {

    @FXML
    private Label typeLabel;

    @FXML
    private Label hpLabel;

    @FXML
    private Label fatigueLabel;

    @FXML
    private HBox singleUnitBox;

    public void initTile(Unit unit)
    {
        typeLabel.setText(unit.getType());
        String str;
        str = Integer.toString(unit.getHP());
        str = str.concat("/");
        str= str.concat(Integer.toString(unit.getMaximumMightMulti()));
        hpLabel.setText(str);
        fatigueLabel.setText(Integer.toString(unit.getFatigueFactorMulti()));
    }

}
