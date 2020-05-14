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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;

import static top.Round.BUY;

public class NeutralController {

    @FXML
    private Label minMight;

    @FXML
    private Label turing;

    @FXML
    private ImageView image;

    @FXML
    private Button purchase;

    @FXML
    void purchaseOnAction(ActionEvent event) throws Exception
    {
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
            c.initPopUp(model,player,slot, BUY,main);
            Scene scene = new Scene(tile, 570, 472);
            Stage stage = new Stage();
            stage.setTitle("ADVANCE!");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(main);
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            throw e;
        }
        Stage stage1 = (Stage)purchase.getScene().getWindow();
        stage1.close();
    }

    protected Model model;
    protected Player player;
    protected Slot slot;
    protected Window main;

    public void initPopUp(Model model, Player player, Slot slot,Window window)
    {
        main = window;
        this.model=model;
        image.setImage(new Image("JavaFXUI/BoardTile/BoardTileControllers/silly.jpeg"));
        turing.setText(Integer.toString(player.getTotalTuring()));
        minMight.setText(Integer.toString(slot.getMinimumMight()));
        this.slot=slot;
        this.player=player;
    }
}
