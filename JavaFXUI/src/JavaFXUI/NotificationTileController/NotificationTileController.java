package JavaFXUI.NotificationTileController;

import bottom.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class NotificationTileController {


    @FXML
    private HBox notificationHbox;

    @FXML
    private TextArea notification;

    public void initTile(String str)
    {
        notification.setText(str);
        notification.setEditable(false);
    }
}
