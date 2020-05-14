package JavaFXUI.BoardTile.BoardTileControllers;

import JavaFXUI.Model;
import bottom.Player;
import bottom.Slot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;

import static top.Round.STRENGTHEN;

public class OnMyOwnController {

    @FXML
    private Button healButton;

    @FXML
    private Button strengthenButton;

    @FXML
    private VBox myUnitsVBox;

    @FXML
    private Label CurTuringLabel;

    @FXML
    private Label curPowerLabel;

    @FXML
    private Label reqPowerLabel;

    @FXML
    private ScrollPane myUnitsScrollPane;

    @FXML
    private VBox armyVbox;


    protected String m1="";
    protected String m2="";
    protected String m3="";

    @FXML
    void healOnAction(ActionEvent event) throws Exception {
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
            Scene scene = new Scene(tile, 600,400);
            Stage stage = new Stage();
            if(slot.getHeal()<=player.getTotalTuring())
            {
                model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound()).healUnitsInSlot(player,slot);
                c.initPopUp(2,model);
                stage.setTitle("CURE!");
                model.setTurnEnd(true);
                m1 = "Get ready to see my magic in action";
                m2 = "Holy Light! Heal!";
                m3= "All units in territory ";
                m3= m3.concat(Integer.toString(slot.getSerialNumber()));
                m3= m3.concat(" are back to full form!");
            }
            else
            {
                c.initPopUp(3,model);
                stage.setTitle("SHAME!");
                m1= "Looks like you're too poor to help your units";
                m2= "You truly lead by example, don't you?";
            }
            c.display(m1,m2,m3);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(main);
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            throw e;
        }
        Stage stage1 = (Stage)healButton.getScene().getWindow();
        stage1.close();
    }

    @FXML
    void strenthenOnAction(ActionEvent event) throws Exception{
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
            c.initPopUp(model,player,slot, STRENGTHEN,main);
            Scene scene = new Scene(tile, 570, 472);
            Stage stage = new Stage();
            stage.setTitle("FORTIFY");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(main);
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            throw e;
        }
        Stage stage1 = (Stage)strengthenButton.getScene().getWindow();
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
        this.player=player;
        this.slot=slot;
        reqPowerLabel.setText(Integer.toString(slot.getMinimumMight()));
        CurTuringLabel.setText(Integer.toString(player.getTotalTuring()));
        Tooltip healTip = new Tooltip();
        Tooltip strengthenTip = new Tooltip();
        healTip.setText("Heal ALL units stationed here to full form");
        strengthenTip.setText("purchase additional units to strengthen hold of\n" +
                "this territory");
        healButton.setTooltip(healTip);
        strengthenButton.setTooltip(strengthenTip);
        int sum=0;
        for(int i=0; i<slot.getOccupyingArmy().size();i++)
            sum+= slot.getOccupyingArmy().get(i).getHP();
        curPowerLabel.setText(Integer.toString(sum));
        armyVbox.setAlignment(Pos.TOP_CENTER);
        HBox hBox;
        Label label1;
        Label label2;
        Label label3;
        Label label4;
        Label label5;
        Separator sep2;
        for(int i=0; i<slot.getOccupyingArmy().size();i++)
        {
            hBox= new HBox();
            label1 =new Label();
            label2=new Label();
            label3= new Label();
            label4= new Label();
            label5= new Label();
            label1.getStyleClass().add("popLabel");
            label2.getStyleClass().add("popLabel");
            label3.getStyleClass().add("popLabel");
            label4.getStyleClass().add("popLabel");
            label5.getStyleClass().add("popLabel");
            label5.setText("  ,  ");
            label3.setText("TYPE:");
            label4.setText("HP/MAX:");
            sep2= new Separator();
            label1.setText(slot.getOccupyingArmy().get(i).getType());
            String st =Integer.toString(slot.getOccupyingArmy().get(i).getHP());
            st= st.concat("/");
            st= st.concat(Integer.toString(slot.getOccupyingArmy().get(i).getMaximumMightMulti()));
            label2.setText(st);
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(5);
            hBox.getChildren().addAll(label3, label1, label5, label4, label2);
            armyVbox.getChildren().addAll(hBox, sep2);
        }
    }

}
