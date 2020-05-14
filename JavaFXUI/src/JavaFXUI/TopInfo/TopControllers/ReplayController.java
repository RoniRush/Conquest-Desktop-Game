package JavaFXUI.TopInfo.TopControllers;

import JavaFXUI.BoardTile.BoardTileController;
import JavaFXUI.Model;
import JavaFXUI.PlayerTile.PlayerTileController;
import bottom.Player;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import top.Round;

import java.net.URL;

public class ReplayController {

    @FXML
    private Button close;

    @FXML
    private Button prev;

    @FXML
    private Button next;

    @FXML
    private ScrollPane boardScroll;

    @FXML
    private GridPane playerGrid;

    @FXML
    private Label player1Name;

    @FXML
    private Label player2Name;

    @FXML
    private Label player3Name;

    @FXML
    private Label player4Name;

    @FXML
    private Separator sep00;

    @FXML
    private Separator sep01;

    @FXML
    private Separator sep10;

    @FXML
    private Separator sep02;

    @FXML
    private Separator sep12;

    @FXML
    private Separator sep11;

    @FXML
    private ImageView p1Img;

    @FXML
    private ImageView p2Img;

    @FXML
    private ImageView p4Img;

    @FXML
    private ImageView p3Img;

    @FXML
    void closeOnAction(ActionEvent event) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    @FXML
    void nextOnAction(ActionEvent event) {
        try {
            curRound.set(curRound.get() + 1);
            clearRight();
            clearBoard();
            initBoard();
            createPlayersGrid();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Whoops " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void prevOnAction(ActionEvent event) {
        try {
            curRound.set(curRound.get() - 1);
            clearRight();
            clearBoard();
            initBoard();
            createPlayersGrid();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Whoops " + e.getMessage());
            alert.showAndWait();
        }

    }

    private Model model;
    private SimpleIntegerProperty curRound;
    private GridPane boardGrid;
    private String[] images;


    public void initPopUp(Model model) {
        this.model = model;
        next.setDisable(true);
        images = new String[4];
        if (model.getCSS().equals("Camelot.css")) {
            images[0] = "JavaFXUI/TopInfo/TopControllers/cersei.png";
            images[1] = "JavaFXUI/TopInfo/TopControllers/jon.png";
            images[2] = "JavaFXUI/TopInfo/TopControllers/daenerys.png";
            images[3] = "JavaFXUI/TopInfo/TopControllers/nightKing.png";
        } else if (model.getCSS().equals("Atlantis.css")) {
            images[0] = "JavaFXUI/TopInfo/TopControllers/eric.png";
            images[1] = "JavaFXUI/TopInfo/TopControllers/ariel.png";
            images[2] = "JavaFXUI/TopInfo/TopControllers/triton.png";
            images[3] = "JavaFXUI/TopInfo/TopControllers/ursula.png";
        } else {
            images[0] = "JavaFXUI/TopInfo/TopControllers/blondKen.png";
            images[1] = "JavaFXUI/TopInfo/TopControllers/africanKen.png";
            images[2] = "JavaFXUI/TopInfo/TopControllers/africanBarbie.png";
            images[3] = "JavaFXUI/TopInfo/TopControllers/blondBarbie.png";
        }
        curRound = new SimpleIntegerProperty(model.getGameManager().getMaxRounds());
        curRound.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() < model.getGameManager().getMaxRounds())
                next.setDisable(false);
            if (newValue.intValue() >= model.getGameManager().getMaxRounds())
                next.setDisable(true);
            if (newValue.intValue() <= 0)
                prev.setDisable(true);
            if (newValue.intValue() > 0)
                prev.setDisable(false);
        });
        try {
            initBoard();
            createPlayersGrid();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Whoops " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void initBoard() throws Exception {
        try {
            FXMLLoader fxmlLoader;
            URL url;
            Parent tile;
            BoardTileController c;
            Round round = model.getGameManager().getRounds().get(curRound.get());
            boardGrid = new GridPane();
            for (int i = 0; i < round.getRows(); i++) {
                for (int j = 0; j < round.getColumns(); j++) {
                    fxmlLoader = new FXMLLoader();
                    url = getClass().getResource("boardTile.fxml");
                    fxmlLoader.setLocation(url);
                    tile = fxmlLoader.load(url.openStream());
                    c = fxmlLoader.getController();
                    c.initTile(round.getBoard()[i][j], model, i, j);
                    c.updateSlotColor(round.getBoard()[i][j],round.getPlayers());
                    boardGrid.add(tile, j, i);
                }
            }
            boardGrid.setGridLinesVisible(true);
            boardScroll.setContent(boardGrid);
        } catch (Exception e) {
            throw e;
        }
    }

    public void createPlayersGrid() throws Exception {
        FXMLLoader fxmlLoader;
        URL url;
        Parent tile;
        PlayerTileController c;
        try {
            for (int i = 0; i < model.getGameManager().getRounds().get(curRound.get()).getPlayers().size(); i++) {
                fxmlLoader = new FXMLLoader();
                url = getClass().getResource("playerTile.fxml");
                fxmlLoader.setLocation(url);
                tile = fxmlLoader.load(url.openStream());
                c = fxmlLoader.getController();
                c.initTile(model, model.getGameManager().getRounds().get(curRound.get()).getPlayers().get(i));
                playerGrid.add(tile, 1, i);
                updateNameAndImage(model.getGameManager().getRounds().get(curRound.get()).getPlayers().get(i), i);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateNameAndImage(Player player, int i) {
        switch (i) {
            case 0: {
                player1Name.setText(player.getName());
                p1Img.setImage(new Image(images[0]));
                break;
            }
            case 1: {
                player2Name.setText(player.getName());
                p2Img.setImage(new Image(images[1]));
                sep00.setVisible(true);
                sep10.setVisible(true);
                break;
            }
            case 2: {
                player3Name.setText(player.getName());
                p3Img.setImage(new Image(images[2]));
                sep01.setVisible(true);
                sep11.setVisible(true);
                break;
            }
            case 3: {
                player4Name.setText(player.getName());
                p4Img.setImage(new Image(images[3]));
                sep02.setVisible(true);
                sep12.setVisible(true);
                break;
            }
        }
    }

    public void clearBoard() {
        ObservableList<Node> children = boardGrid.getChildren();
        Node node;
        for (int i = 0; i < children.size(); i++) {
            node = children.get(i);
            children.remove(node);
            i--;
        }
        boardScroll.setContent(null);
    }

    public void clearRight() {
        player1Name.setText("");
        player2Name.setText("");
        player3Name.setText("");
        player4Name.setText("");
        p1Img.setImage(null);
        p2Img.setImage(null);
        p3Img.setImage(null);
        p4Img.setImage(null);
        sep00.setVisible(false);
        sep10.setVisible(false);
        sep01.setVisible(false);
        sep11.setVisible(false);
        sep02.setVisible(false);
        sep12.setVisible(false);
        removePlayerTiles();
    }

    public void removePlayerTiles() {
        ObservableList<Node> children = playerGrid.getChildren();
        Node node;
        for (int i = 0; i < children.size(); i++) {
            node = children.get(i);
            if (node instanceof VBox && (GridPane.getRowIndex(node) == 0 || GridPane.getRowIndex(node) == 1 ||
                    GridPane.getRowIndex(node) == 2 || GridPane.getRowIndex(node) == 3)
                    && GridPane.getColumnIndex(node) == 1) {
                playerGrid.getChildren().remove(node);
                i--;
            }
        }
    }
}
