package JavaFXUI.BoardTile.BoardTileControllers;

import JavaFXUI.Model;
import bottom.Player;
import bottom.Slot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import static top.Round.LUCKY;
import static top.Round.ORCHESTRATED;

public class ReviewController {

    @FXML
    private Label m1;

    @FXML
    private Label m2;

    @FXML
    private Label m3;

    @FXML
    private Button reviewButton;

    @FXML
    private VBox opArmyBox;

    @FXML
    private VBox mainVbox;

    @FXML
    private ScrollPane scroll;

    private Player player;
    private Slot slot;
    private int report;
    private int action;
    private Window main;

    @FXML
    void reviewOnAction(ActionEvent event) {
        reviewButton.setVisible(false);
        reviewButton.setDisable(true);
        mainVbox.getChildren().removeAll(scroll);
        mainVbox.setAlignment(Pos.TOP_CENTER);
        Label label1=new Label();
        Label label2=new Label();
        Label label3=new Label();
        Label label4=new Label();
        Label label5=new Label();
        label1.getStyleClass().add("popLabel");
        label2.getStyleClass().add("popLabel");
        label3.getStyleClass().add("popLabel");
        label4.getStyleClass().add("popLabel");
        label5.getStyleClass().add("popLabel");
        String st1;
        String st2;
        String st3;
        m1.setText("");
        m2.setText("");
        m3.setText("");
        switch (report)
        {
            case 1:
            {
                m1.setText("VICTORY! Your troops have defeated the rival army!");
                m2.setText("Here is the final report of the attack: ");
                if (slot.getOwner()==null)
                {
                    m3.setText("You lack sufficient power to hold control of this territory");
                    label1.setText("You have no choice but to pull back");
                    label2.setText("It's not all bad though, The troops who survived have brought some Turing with them");
                    st1="Here are your spoils: ";
                    st1= st1.concat(Integer.toString(slot.getHeal()));
                    slot.setHeal(0);
                    label3.setText(st1);
                    st2= "Your total Turing is now: ";
                    st2=st2.concat(Integer.toString(player.getTotalTuring()));
                    label4.setText(st2);
                    st3= "Territory number";
                    st3= st3.concat(Integer.toString(slot.getSerialNumber()));
                    st3= st3.concat(" is now neutral");
                    label5.setText(st3);
                    mainVbox.setPadding(new Insets(0,0,5,0));
                    mainVbox.getChildren().addAll(label1,label2,label3,label4,label5);
                }
                else {
                    m3.setText("Splendid! you have sufficient power to maintain hold of this territory");
                    st3= "Territory number ";
                    st3= st3.concat(Integer.toString(slot.getSerialNumber()));
                    st3= st3.concat(" is under your control");
                    label1.setText(st3);
                    label2.setText("Army stationed is:");
                    mainVbox.getChildren().addAll(label1,label2);
                    displaySlotArmy(mainVbox);
                }
                break;
            }
            case 2:
            {
                m1.setText("BATTLE LOST");
                m2.setText("Poor planning has led to your troops' demise, The Defender stands victorious");
                if (slot.getOwner()==null)
                {
                    m3.setText("But.. The Defender lacks sufficient power to hold control of this territory");
                    label1.setText("Looks like he's retreating with his spoils");
                    mainVbox.getChildren().addAll(label1);
                }
                else
                    m3.setText("And remains in possession of territory");
            }
        }

    }

    public void initPopUp(Model model, Player player, Slot slot,int attackerMight,int[] numOfUnitsToBuy, int action, Window window)
    {
        this.player=player;
        this.slot=slot;
        this.action= action;
        main = window;
        displaySlotArmy(opArmyBox);
        int report=0;
        switch (action)
        {
            case LUCKY:
                report = model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound()).attackOccupied(player,slot,attackerMight,numOfUnitsToBuy);
                break;
            case ORCHESTRATED:
                report = model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound()).calculatedAttack(player,slot,numOfUnitsToBuy);
                break;
        }
        this.report=report;
    }

    public void displaySlotArmy(VBox vBox)
    {
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
            vBox.getChildren().addAll(hBox, sep2);
        }
    }
}
