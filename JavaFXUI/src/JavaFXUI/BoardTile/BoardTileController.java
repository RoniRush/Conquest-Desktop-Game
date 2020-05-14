package JavaFXUI.BoardTile;

import java.net.URL;
import java.util.List;

import JavaFXUI.BoardTile.BoardTileControllers.AttackController;
import JavaFXUI.BoardTile.BoardTileControllers.ErrorPopUpController;
import JavaFXUI.BoardTile.BoardTileControllers.NeutralController;
import JavaFXUI.BoardTile.BoardTileControllers.OnMyOwnController;
import JavaFXUI.Model;
import bottom.Player;
import bottom.Slot;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BoardTileController {

    @FXML
    private VBox boardTile;

    @FXML
    private Label serial;

    @FXML
    private HBox minMightBox;

    @FXML
    private Label minMight;

    @FXML
    private HBox rewardsBox;

    @FXML
    private Label rewards;

    protected Model model;
    protected int serialNum;
    protected int row;
    protected int col;
    private ChangeListener<Boolean> nameListener;


    @FXML
    void chooseTerritoryOnAction(MouseEvent event) {
        if (model.getStartTurnProperty().get()) {
            Slot slot = obtainSlot();
            if (slot != null) {
                Player player = slot.getOwner();
                if (player == null)
                    conquerNeutral(model.getGameManager().getCurrPlayer(), slot);
                else if (player.getId() == model.getGameManager().getCurrPlayer().getId())
                    actionOnOwned(player, slot);
                else {
                    attackPopUp(model.getGameManager().getCurrPlayer(), slot);
                }
            }
        }
    }

    public void attackPopUp(Player player,Slot slot) {
        try {
            FXMLLoader fxmlLoader;
            URL url;
            Parent tile;
            AttackController c;
            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("attack.fxml");
            fxmlLoader.setLocation(url);
            tile = fxmlLoader.load(url.openStream());
            c = fxmlLoader.getController();
            c.initPopUp(model, player, slot,boardTile.getScene().getWindow());
            Scene scene = new Scene(tile, 612, 503);
            Stage stage = new Stage();
            stage.setTitle("ATTACK!");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(boardTile.getScene().getWindow());
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Whoops " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void actionOnOwned(Player player, Slot slot) {
        try {
            FXMLLoader fxmlLoader;
            URL url;
            Parent tile;
            OnMyOwnController c;
            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("ownTerritory.fxml");
            fxmlLoader.setLocation(url);
            tile = fxmlLoader.load(url.openStream());
            c = fxmlLoader.getController();
            c.initPopUp(model, player, slot,boardTile.getScene().getWindow());
            Scene scene = new Scene(tile, 600, 400);
            Stage stage = new Stage();
            stage.setTitle("ADVANCE!");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(boardTile.getScene().getWindow());
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Whoops " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void conquerNeutral(Player player, Slot slot)
    {
        try {
            FXMLLoader fxmlLoader;
            URL url;
            Parent tile;
            NeutralController c;
            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("neutral.fxml");
            fxmlLoader.setLocation(url);
            tile = fxmlLoader.load(url.openStream());
            c = fxmlLoader.getController();
            c.initPopUp(model,player,slot,boardTile.getScene().getWindow());
            Scene scene = new Scene(tile, 600, 400);
            Stage stage = new Stage();
            stage.setTitle("ADVANCE!");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(boardTile.getScene().getWindow());
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Whoops " + e.getMessage());
            alert.showAndWait();
        }
    }

    public Slot obtainSlot()
    {
        try {
            FXMLLoader fxmlLoader;
            URL url;
            Parent tile;
            ErrorPopUpController c;
            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("errorPopUp.fxml");
            fxmlLoader.setLocation(url);
            tile = fxmlLoader.load(url.openStream());
            c = fxmlLoader.getController();
            c.initPopUp(0,model);
            Player player = model.getGameManager().getCurrPlayer();
            Slot slot;
            if (player.getConqueredSize() != 0) {
                if (!model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound()).isThisActionPossible(serialNum, player))
                    c.display("Don't you know the rules?!", "You can only move 1 slot vertically or horizontally", "From a territory you occupy");
                else {
                    slot = model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound()).getBoard()[row][col];
                    return slot;
                }
            } else {
                if (!model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound()).noNeutral()) {
                    slot = model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound()).getBoard()[row][col];
                    if (slot.getOwner() == null)
                        return slot;
                    else
                        c.display("Don't you know the rules?!", "You can only select a neutral territory", "When you have no land of your own");
                } else
                    c.display("Looks like the entire board is occupied.. and not by you", "Your only hope is to wait and see if your competitors", "Display the same ineptitude");
            }
            Scene scene = new Scene(tile, 600, 400);
            Stage stage = new Stage();
            stage.setTitle("NO!");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(boardTile.getScene().getWindow());
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
         catch (Exception e) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("ERROR");
             alert.setHeaderText(null);
             alert.setContentText("Whoops " + e.getMessage());
             alert.showAndWait();
         }
        return null;
    }


    public void initTile(Slot slot, Model model,int i,int j)
    {
        this.model=model;
        serial.setText(Integer.toString(slot.getSerialNumber()));
        serialNum=slot.getSerialNumber();
        minMight.setText(Integer.toString(slot.getMinimumMight()));
        rewards.setText(Integer.toString(slot.getRoundRewards()));
        row=i;
        col=j;
        nameListener= (observable, oldValue, newValue) -> {
            if(newValue)
            {
                int numOfRound= model.getGameManager().getNumOfRound();
                Slot newslot = model.getGameManager().getRounds().get(numOfRound).getBoard()[row][col];
                updateSlotColor(newslot, model.getGameManager().getRounds().get(numOfRound).getPlayers());
            }
        };
        model.getCheckColorProperty().addListener(nameListener);
        /*model.getCheckColorProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                int numOfRound= model.getGameManager().getNumOfRound();
                Slot newslot = model.getGameManager().getRounds().get(numOfRound).getBoard()[row][col];
                updateSlotColor(newslot, model.getGameManager().getRounds().get(numOfRound).getPlayers());
            }
        });*/
        model.getXMLloadedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
            {
                model.getCheckColorProperty().removeListener(nameListener);
            }
        });
    }

    public void updateSlotColor(Slot slot, List<Player>players)
    {
        int i;
        boolean flag=false;
        if (slot.getOwner()==null)
            boardTile.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
        else
        {
            for (i=0; i<players.size()&&!flag; i++)
            {
                if (players.get(i).getId()==slot.getOwner().getId())
                    flag=true;
            }
            i--;
            double[] color = players.get(i).getColor();
            boardTile.setBackground(new Background(new BackgroundFill(Color.color(color[0],color[1],color[2]),null,null)));
        }
    }



}

