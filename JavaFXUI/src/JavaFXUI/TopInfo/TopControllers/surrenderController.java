package JavaFXUI.TopInfo.TopControllers;

import JavaFXUI.Model;
import bottom.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import top.Round;

public class surrenderController {

    @FXML
    private BorderPane leaveGamePane;

    @FXML
    private Button yesButton;

    @FXML
    private ImageView image;

    protected Model model;
    protected Window main;

    @FXML
    void yesOnAction(ActionEvent event) {
        boolean flag = false;
        Round round = model.getGameManager().getRounds().get(model.getGameManager().getNumOfRound());
        for(int j=0; j<round.getPlayers().size()&&!flag; j++)
        {
            if (round.getPlayers().get(j).getId()== model.getGameManager().getCurrPlayer().getId())
            {
                Player player = round.getPlayers().get(j);
                for(int i=0;i<player.getConquered().size();i++)
                {
                    player.getConquered().get(i).setOwner(null);
                    player.getConquered().get(i).clearOccupyingArmy();
                }
                round.getPlayers().remove(j);
                round.setNumOfPlayers(round.getNumOfPlayers()-1);
                flag=true;
            }
        }
        model.setSurrender(true);
        Stage stage = (Stage)yesButton.getScene().getWindow();
        stage.close();
        justGo();
    }

    public void justGo()
    {
        model.getGameManager().setTurnIndicator(model.getGameManager().getTurnIndicator()-1);
        model.setTurnEnd(true);
        BorderPane borderPane=new BorderPane();
        borderPane.setId("popUpBack");
        Label label= new Label();
        HBox hbox= new HBox();
        HBox hbox2= new HBox();
        hbox.setMaxWidth(600);
        hbox2.setMaxHeight(250);
        hbox2.setMaxWidth(250);
        hbox.getChildren().add(label);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20,10,10,10));
        label.setText("Just go already..");
        label.getStyleClass().add("popLabel");
        borderPane.setTop(hbox);
        ImageView image2 = new ImageView();
        image2.setImage(new Image("JavaFXUI/TopInfo/TopControllers/justGo.jpeg"));
        image2.setFitHeight(300);
        image2.setFitWidth(290);
        hbox2.getChildren().add(image2);
        hbox2.setAlignment(Pos.CENTER);
        borderPane.setCenter(hbox2);
        Scene scene= new Scene(borderPane, 600, 400);
        Stage stage1= new Stage();
        stage1.setTitle("Good-Bye");
        stage1.initModality(Modality.WINDOW_MODAL);
        stage1.initOwner(main);
        scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
        stage1.setScene(scene);
        stage1.show();
    }

    public void initPopUp(Model model,Window window)
    {
        image.setImage(new Image("JavaFXUI/TopInfo/TopControllers/sad.jpeg"));
        this.model=model;
        main=window;
    }

}
