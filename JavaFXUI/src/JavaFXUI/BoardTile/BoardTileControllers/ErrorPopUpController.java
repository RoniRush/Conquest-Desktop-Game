package JavaFXUI.BoardTile.BoardTileControllers;

import JavaFXUI.Model;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ErrorPopUpController {

    @FXML
    private Label headerLabel;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;

    @FXML
    private ImageView image;

    protected Model model;
    protected int indicator;

    public void initPopUp(int indicator, Model model)
    {
        this.model = model;
        this.indicator=indicator;
        if(indicator==0)
            image.setImage(new Image("JavaFXUI/BoardTile/BoardTileControllers/confused.jpeg"));
        if (indicator==1)
            image.setImage(new Image("JavaFXUI/BoardTile/BoardTileControllers/angry.jpeg"));
        if (indicator==2) {
            headerLabel.setText("O-M-G!");
            image.setImage(new Image("JavaFXUI/BoardTile/BoardTileControllers/happy.jpeg"));
        }
        if(indicator==3) {
            image.setImage(new Image("JavaFXUI/BoardTile/BoardTileControllers/whatever.jpeg"));
            headerLabel.setText("Meh");
        }
    }

    public void display(String m1,String m2,String m3)
    {
        label1.setText(m1);
        label2.setText(m2);
        label3.setText(m3);
    }
}
