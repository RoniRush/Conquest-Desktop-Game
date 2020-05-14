package JavaFXUI.TopInfo.TopControllers;

import JavaFXUI.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PassTurnController {


    @FXML
    private BorderPane passTurnBoarder;

    @FXML
    private Button confirm;

    @FXML
    private ImageView passTurnImage;

    protected Model model;

    @FXML
    void confirmOnAction(ActionEvent event) {
        model.setTurnEnd(true);
        Stage stage = (Stage)confirm.getScene().getWindow();
        stage.close();
    }

    public void initPopUp(Model model)
    {
        passTurnImage.setImage(new Image("JavaFXUI/TopInfo/TopControllers/waiting.jpeg"));
        this.model=model;
    }
}
