package JavaFXUI.BoardTile.BoardTileControllers;

import JavaFXUI.Model;
import bottom.Player;
import bottom.Slot;
import bottom.Unit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static top.Round.*;

public class PurchaseController {

    @FXML
    private GridPane shopGrid;

    @FXML
    private Label totalMight;

    @FXML
    private Label totalTuring;

    @FXML
    private Button buy;

    @FXML
    void buyOnAction(ActionEvent event) throws Exception {
        boolean flag = purchaseUnits();
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
            c.display(m1,m2,m3);
            Scene scene = new Scene(tile, 600,400);
            Stage stage = new Stage();
            if (flag) {
                c.initPopUp(2,model);
                stage.setTitle("VICTORY!");
            }
            else {
                c.initPopUp(1,model);
                stage.setTitle("NO!");
            }
            if(!model.isWasBattle())
            {
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(main);
                scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
                stage.setScene(scene);
                stage.show();
            }
            else
                model.setWasBattle(false);
        }
        catch (Exception e) {
            throw e;
        }
        if (flag) {
            Stage stage1 = (Stage)buy.getScene().getWindow();
            stage1.close();
            model.setTurnEnd(true);
        }
    }

    protected Model model;
    protected Player player;
    protected Slot slot;
    protected List<TextField> textFields= new ArrayList<>();
    protected int action;
    protected List<Unit>units;
    protected String m1="";
    protected String m2="";
    protected String m3="";
    protected int might1=0;
    protected int purchase1=0;
    protected Window main;

    public void initPopUp(Model model, Player player, Slot slot, int action,Window window)
    {
        main=window;
        this.action=action;
        this.model=model;
        this.slot=slot;
        this.player=player;
        HBox hBox1, hBox2, hBox3, hBox4;
        Label label1, label2, label3;
        units= model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound()).getModel();
        for(int i=0;i<units.size();i++) {
            hBox1 = new HBox();
            hBox2 = new HBox();
            hBox3 = new HBox();
            hBox4 = new HBox();
            label1 = new Label();
            label2 = new Label();
            label3 = new Label();
            label1.getStyleClass().add("popLabel");
            label2.getStyleClass().add("popLabel");
            label3.getStyleClass().add("popLabel");
            hBox1.setAlignment(Pos.CENTER);
            hBox2.setAlignment(Pos.CENTER);
            hBox3.setAlignment(Pos.CENTER);
            hBox4.setAlignment(Pos.CENTER);
            label1.setText(units.get(i).getType());
            label2.setText(Integer.toString(units.get(i).getMaximumMight()));
            label3.setText(Integer.toString(units.get(i).getCost()));
            textFields.add(new TextField());
            hBox1.getChildren().add(label1);
            hBox1.setPrefHeight(40);
            hBox2.getChildren().add(label2);
            hBox2.setPrefHeight(40);
            hBox3.getChildren().add(label3);
            hBox3.setPrefHeight(40);
            hBox4.getChildren().add(textFields.get(i));
            hBox4.setPrefHeight(40);
            shopGrid.add(hBox1, 0, i+1);
            shopGrid.add(hBox2, 1, i+1);
            shopGrid.add(hBox3, 2, i+1);
            shopGrid.add(hBox4, 3, i+1);
        }
    }

    public void addListenerText(TextField textField,Unit unit)
    {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean flag=true;
            try{
                Integer num = Integer.parseInt(newValue);
            }catch (NumberFormatException e)
            {
                flag = false;
            }
            if (flag)
            {
                purchase1-=unit.getCost()*Integer.parseInt(oldValue);
                might1-=unit.getMaximumMight()*Integer.parseInt(oldValue);
                purchase1+=unit.getCost()*Integer.parseInt(newValue);
                might1+=unit.getMaximumMight()*Integer.parseInt(newValue);
            }
        });
    }

    public boolean purchaseUnits() throws Exception {
        int purchase = 0;
        int might = 0;
        int[] numOfUnitsToBuy = new int[units.size()];
        int num;
        for (int i = 0; i < numOfUnitsToBuy.length; i++)
            numOfUnitsToBuy[i] = 0;
        for (int i = 0; i < numOfUnitsToBuy.length; i++) {
            if (!textFields.get(i).getText().equals("")) {
                try {
                    num = Integer.parseInt(textFields.get(i).getText());
                    if (num > 0) {
                        numOfUnitsToBuy[i] = num;
                        purchase += (num * units.get(i).getCost());
                        might += (num * units.get(i).getMaximumMight());
                    } else {
                        editError();
                        return false;
                    }
                } catch (NumberFormatException e) {
                    editError();
                    return false;
                }
            }
        }
        switch (action) {
            case BUY: {
                if (model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound()).purchaseUnits(player, slot, action, purchase, might, numOfUnitsToBuy)) {
                    m1 = "I'm so EXCITED!";
                    m2 = "You are now the proud owner of Territory ";
                    m2 = m2.concat(Integer.toString(slot.getSerialNumber()));
                    m3 = "Congrats! You are on the right path!";
                    return true;
                }
                if (might < slot.getMinimumMight())
                    m1 = "That puny army is to weak. You need more might";
                if (purchase > player.getTotalTuring()) {
                    m2 = "You can barely afford a slither of that";
                    m3 = "You need more Turing";
                }
                break;
            }
            case STRENGTHEN: {
                if (model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound()).purchaseUnits(player, slot, action, purchase, might, numOfUnitsToBuy)) {
                    m1 = "Units were purchased successfully";
                    m2 = "Look at all your shiny new toys";
                    return true;
                } else if (purchase == 0) {
                    m1 = "Are you making fun of me?!";
                    m2 = "Stop wasting my time";
                } else {
                    m1 = "You can barely afford a slither of that";
                    m2 = "You need more Turing";
                }
                break;
            }
            case LUCKY: {
                if (model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound()).purchaseUnits(player, slot, CONQUER, purchase, might, numOfUnitsToBuy)) {
                    attackOccupied(player, slot, might, numOfUnitsToBuy);
                    model.setWasBattle(true);
                    return true;
                } else if (purchase == 0) {
                    m1 = "Are you making fun of me?!";
                    m2 = "Stop wasting my time";
                } else {
                    m1 = "You can barely afford a slither of that";
                    m2 = "You need more Turing";
                }
                break;
            }
            case ORCHESTRATED: {
                if (model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound()).purchaseUnits(player, slot, CONQUER, purchase, might, numOfUnitsToBuy)) {
                    attackOccupied(player, slot, might, numOfUnitsToBuy);
                    model.setWasBattle(true);
                    return true;
                } else if (purchase == 0) {
                    m1 = "Are you making fun of me?!";
                    m2 = "Stop wasting my time";
                } else {
                    m1 = "You can barely afford a slither of that";
                    m2 = "You need more Turing";
                }
                break;
            }
        }
        return false;
    }

    public void attackOccupied(Player player,Slot slot,int attackerMight,int[] numOfUnitsToBuy) throws Exception
    {
        try {
            FXMLLoader fxmlLoader;
            URL url;
            Parent tile;
            ReviewController c;
            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("opponetArmy.fxml");
            fxmlLoader.setLocation(url);
            tile = fxmlLoader.load(url.openStream());
            c = fxmlLoader.getController();
            c.initPopUp(model,player,slot, attackerMight, numOfUnitsToBuy, action,main);
            Scene scene = new Scene(tile, 600,400);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(main);
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            throw e;
        }
    }

    public void editError()
    {
        m1 = "No!";
        m2 = "Is it so hard to choose a positive number?";
        m3 = "";
    }
}
