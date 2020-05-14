package JavaFXUI.TopInfo.TopControllers;

import JavaFXUI.Model;
import bottom.Slot;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import top.Round;

import java.util.ArrayList;
import java.util.List;

public class undoController {

    @FXML
    private BorderPane undoBoarderPane;

    @FXML
    private ImageView image;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;

    @FXML
    private Button yesButton;

    protected Model model;

    @FXML
    void yesOnAction(ActionEvent event) {
        if(model.isFromUndo())
            model.getGameManager().deleteRound();
        model.getGameManager().setNumOfRound(model.getGameManager().getNumOfRound() - 1);
        model.getGameManager().deleteRound();
        createNewRound();
        model.setFromUndo(true);
        model.setXMLloaded(false);
        model.setXMLloaded(true);
        model.setWentBack(true);
        model.setCheckColor(true);
        model.setCheckColor(false);
        Stage stage = (Stage) yesButton.getScene().getWindow();
        stage.close();
        model.setWentBack(false);
    }

    public void initPopUp(Model model)
    {
        image.setImage(new Image("JavaFXUI/TopInfo/TopControllers/resilient.jpeg"));
        this.model = model;
    }

    public void createNewRound() {
        if (model.getGameManager().createNewRound()) {
            Round round = model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound());
            List<Slot> deadAndBuried;
            List<Slot> totalDead = new ArrayList<>();
            for (int i = 0; i < round.getNumOfPlayers(); i++) {
                deadAndBuried = round.calcFatigue(i);
                if (deadAndBuried.size() > 0)
                    totalDead.addAll(deadAndBuried);
            }
            if (totalDead.size() > 0) {
                for (int i = 0; i < round.getNumOfPlayers(); i++) {
                    String dead = "The following Territories have turned neutral:\n";
                    for (int j = 0; j < totalDead.size(); j++) {
                        if (!dead.equals("The following Territories have turned neutral:"))
                            dead = dead.concat(", ");
                        dead = dead.concat(totalDead.get(j).getSerialNumber().toString());
                    }
                    dead = dead.concat("\n");
                    dead = dead.concat("tsk-tsk, All those poor dead soldiers, sigh");
                    round.getPlayers().get(i).getNotifications().add(dead);
                }
            }
            for(int i=0;i<round.getPlayers().size();i++)
            {
                List<Slot> conquered = round.getPlayers().get(i).getConquered();
                for(int j=0;j<conquered.size();j++)
                    conquered.get(j).calcHealPerSlot();
            }
        }
    }


}
