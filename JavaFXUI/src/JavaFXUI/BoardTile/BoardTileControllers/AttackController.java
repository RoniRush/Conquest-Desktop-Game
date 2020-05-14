package JavaFXUI.BoardTile.BoardTileControllers;

import JavaFXUI.Model;
import bottom.Player;
import bottom.Slot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;

import static top.Round.*;

public class AttackController {
    @FXML
    private Button luckyAttackButton;

    @FXML
    private Button calculatedButton;

    @FXML
    private Label curTuringLabel;

    @FXML
    private Label minMightLabel;

    @FXML
    private ImageView image;

    @FXML
    void calculatedOnAction(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader;
            URL url;
            Parent tile;
            PurchaseController c;
            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("purchase.fxml");
            fxmlLoader.setLocation(url);
            tile = fxmlLoader.load(url.openStream());
            c = fxmlLoader.getController();
            c.initPopUp(model,player,slot, ORCHESTRATED,main);
            Scene scene = new Scene(tile, 570, 472);
            Stage stage = new Stage();
            stage.setTitle("CHARGE");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(main);
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            throw e;
        }
        Stage stage1 = (Stage)calculatedButton.getScene().getWindow();
        stage1.close();
    }


    @FXML
    void luckyOnAction(ActionEvent event) throws Exception{
        try {
            FXMLLoader fxmlLoader;
            URL url;
            Parent tile;
            PurchaseController c;
            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("purchase.fxml");
            fxmlLoader.setLocation(url);
            tile = fxmlLoader.load(url.openStream());
            c = fxmlLoader.getController();
            c.initPopUp(model,player,slot, LUCKY,main);
            Scene scene = new Scene(tile, 570, 472);
            Stage stage = new Stage();
            stage.setTitle("CHARGE");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(main);
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            throw e;
        }
        Stage stage1 = (Stage)luckyAttackButton.getScene().getWindow();
        stage1.close();
    }

    protected Model model;
    protected Player player;
    protected Slot slot;
    protected Window main;

    public void initPopUp(Model model, Player player, Slot slot,Window window)
    {
        main=window;
        this.model=model;
        this.player=player;
        this.slot=slot;
        minMightLabel.setText(Integer.toString(slot.getMinimumMight()));
        curTuringLabel.setText(Integer.toString(player.getTotalTuring()));
        image.setImage(new Image("JavaFXUI/BoardTile/BoardTileControllers/malicious.jpeg"));
        Tooltip luckyTip = new Tooltip();
        Tooltip calculatedTip = new Tooltip();
        luckyTip.setText("Winner will be chosen by the hand of fate.\n" +
                "The stronger the army the higher the chance of being chosen");
        calculatedTip.setText("Victory will be awarded to the one holding the\n" +
                "strongest unit at the end of the battle.");
        luckyAttackButton.setTooltip(luckyTip);
        calculatedButton.setTooltip(calculatedTip);
    }
}
