package JavaFXUI.TopInfo.TopControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.util.List;

public class WinnerPopUp {

    @FXML
    private BorderPane borderPop;

    @FXML
    private Label headerLabel;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;

    @FXML
    private Label winnerLabel;

    @FXML
    private ImageView crown;

    @FXML
    private ImageView image;


    public void initPopUp(List<String>names)
    {
        image.setImage(new Image("JavaFXUI/TopInfo/TopControllers/excited.jpeg"));
        crown.setImage(new Image("JavaFXUI/TopInfo/TopControllers/crown2.jpg"));
        if (names.size()==1)
            winnerLabel.setText(names.get(0));
        else
        {
            String str="";
            headerLabel.setText("HURRAY! The Game is OVER!");
            label1.setText("OH MY! It seems we have a tie!");
            label2.setText("You shall be joining my Harem!");
            label3.setText("Welcome my new boy toys");
            for(int i=0;i<names.size();i++)
            {
                if(i!=0)
                    str=str.concat(", ");
                str=str.concat(names.get(i));
            }
            winnerLabel.setText(str);
        }
    }
}
