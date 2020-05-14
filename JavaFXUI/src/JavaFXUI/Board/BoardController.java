package JavaFXUI.Board;

import JavaFXUI.BoardTile.BoardTileController;
import JavaFXUI.Model;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import top.Round;

import java.net.URL;

public class BoardController {

    @FXML
    private ScrollPane boardScroll;

    private GridPane boardGrid;
    private Model model;

    public void setModel(Model model) {
        this.model = model;
    }

    public void initBoard() {
        model.getXMLloadedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (model.getXMLloadedProperty().get())
                {
                    FXMLLoader fxmlLoader;
                    URL url;
                    Parent tile;
                    BoardTileController c;
                    Round round = model.getGameManager().getRounds().get(0);
                    boardGrid = new GridPane();
                    for(int i=0;i<round.getRows();i++)
                    {
                        for(int j=0;j<round.getColumns();j++)
                        {
                            fxmlLoader = new FXMLLoader();
                            url = getClass().getResource("boardTile.fxml");
                            fxmlLoader.setLocation(url);
                            tile = fxmlLoader.load(url.openStream());
                            c = fxmlLoader.getController();
                            c.initTile(round.getBoard()[i][j],model,i,j);
                            boardGrid.add(tile, j, i);
                        }
                    }
                    boardGrid.setGridLinesVisible(true);
                    boardScroll.setContent(boardGrid);
                } else
                    clearBoard();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("Whoops " + e.getMessage());
                alert.showAndWait();
            }
        });
        model.getGameEndedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                clearBoard();
                HBox hBox= new HBox();
                hBox.setAlignment(Pos.CENTER);
                Label label = new Label();
                label.setText("Press Replay to relive the magical experience as a viewer");
                hBox.getChildren().add(label);
                label.setFont(Font.font(20));
                boardScroll.setContent(hBox);
            }
        });
    }


    public void clearBoard()
    {
        ObservableList<Node> children = boardGrid.getChildren();
        Node node;
        for (int i = 0; i < children.size(); i++) {
            node = children.get(i);
            children.remove(node);
            i--;
        }
        boardScroll.setContent(null);
    }


}
