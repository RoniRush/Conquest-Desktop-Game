package JavaFXUI.RightInfo;

import JavaFXUI.ConqueredTile.ConqueredTileController;
import JavaFXUI.ConqueredTile.IdTileController;
import JavaFXUI.Model;
import JavaFXUI.NotificationTileController.NotificationTileController;
import JavaFXUI.PlayerTile.PlayerTileController;
import JavaFXUI.ShopTile.ShopTileController;
import bottom.Player;
import bottom.Unit;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class RightController {

    @FXML
    private TabPane rightInfoTab;

    @FXML
    private Tab playerTab;

    @FXML
    private ScrollPane playerScroll;

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
    private Tab unitTab;

    @FXML
    private ScrollPane unitScroll;

    @FXML
    private VBox unitShopBox;

    @FXML
    private Tab myTerritoriesTab;

    @FXML
    private ScrollPane TerritoryScroll;

    @FXML
    private VBox territoryVbox;

    @FXML
    private Tab notificationsTab;

    @FXML
    private ScrollPane notificationScroll;

    @FXML
    private VBox notificationsBox;

    private Model model;
    private String[] images;



    public void setModel(Model model) {
        this.model = model;
    }

    public void initRight() {
        images = new String[4];
        images[0] = "JavaFXUI/RightInfo/cersei.png";
        images[1] = "JavaFXUI/RightInfo/jon.png";
        images[2] = "JavaFXUI/RightInfo/daenerys.png";
        images[3] = "JavaFXUI/RightInfo/nightKing.png";
        model.getXMLloadedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (model.getXMLloadedProperty().get()) {
                    createPlayersTab();
                    createUnitsTab();
                    if (!model.isFromUndo()) {
                        createMyTerritoriesTab();
                        createNotificationsTab();
                    }
                } else {
                    clearRight(0);
                    removeTiles(notificationsBox.getChildren());
                    removeTiles(territoryVbox.getChildren());
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("Whoops " + e.getMessage());
                alert.showAndWait();
            }
        });
        model.getSurrenderProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                try {
                    clearRight(1);
                    createPlayersTab();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText(null);
                    alert.setContentText("Whoops " + e.getMessage());
                    alert.showAndWait();
                }
                model.setSurrender(false);
            }
        });
        model.getGameEndedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                clearRight(0);
                removeTiles(notificationsBox.getChildren());
                removeTiles(territoryVbox.getChildren());
            }
        });
        model.getStyleChooserProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (model.getCSS().equals("Camelot.css")) {
                    images[0] = "JavaFXUI/RightInfo/cersei.png";
                    images[1] = "JavaFXUI/RightInfo/jon.png";
                    images[2] = "JavaFXUI/RightInfo/daenerys.png";
                    images[3] = "JavaFXUI/RightInfo/nightKing.png";
                } else if (model.getCSS().equals("Atlantis.css")) {
                    images[0] = "JavaFXUI/RightInfo/eric.png";
                    images[1] = "JavaFXUI/RightInfo/ariel.png";
                    images[2] = "JavaFXUI/RightInfo/triton.png";
                    images[3] = "JavaFXUI/RightInfo/ursula.png";
                } else {
                    images[0] = "JavaFXUI/RightInfo/blondKen.png";
                    images[1] = "JavaFXUI/RightInfo/africanKen.png";
                    images[2] = "JavaFXUI/RightInfo/africanBarbie.png";
                    images[3] = "JavaFXUI/RightInfo/blondBarbie.png";
                }
                if (model.getXMLloadedProperty().get()) {
                    p1Img.setImage(new Image(images[0]));
                    p2Img.setImage(new Image(images[1]));
                    p3Img.setImage(new Image(images[2]));
                    p4Img.setImage(new Image(images[3]));
                }
            }
        });
    }

    public void createNotificationsTab()
    {
        model.getStartTurnProperty().addListener((observable, oldValue, newValue) -> {
            FXMLLoader fxmlLoader;
            URL url;
            Parent tile;
            NotificationTileController c;
            if(newValue)
            {
                try {
                    Player player = model.getGameManager().getCurrPlayer();
                    if(player!=null) {
                        for (int i = 0; i < player.getNotifications().size(); i++) {
                            fxmlLoader = new FXMLLoader();
                            url = getClass().getResource("notificationTile.fxml");
                            fxmlLoader.setLocation(url);
                            tile = fxmlLoader.load(url.openStream());
                            c = fxmlLoader.getController();
                            c.initTile(player.getNotifications().get(i));
                            notificationsBox.getChildren().add(tile);
                        }
                    }
                }
                catch (IOException e)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText(null);
                    alert.setContentText("Whoa " + e.getMessage());
                    alert.showAndWait();
                }
            }
            else
                removeTiles(notificationsBox.getChildren());
        });

    }

    public void clearRight(int indicator)
    {
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
        if(indicator==0)
            removeTiles(unitShopBox.getChildren());
    }

    public void removeTiles(ObservableList<Node> children)
    {
        Node node;
        for(int i=0;i<children.size();i++)
        {
            node = children.get(i);
            children.remove(node);
            i--;
        }
    }


    public void removePlayerTiles()
    {
        ObservableList<Node> children = playerGrid.getChildren();
        Node node;
        for(int i=0;i<children.size();i++) {
            node = children.get(i);
            if(node instanceof VBox && (GridPane.getRowIndex(node) == 0 ||GridPane.getRowIndex(node) == 1||
                    GridPane.getRowIndex(node) == 2|| GridPane.getRowIndex(node) == 3)
                    && GridPane.getColumnIndex(node) == 1) {
                playerGrid.getChildren().remove(node);
                i--;
            }
        }
    }

    public void createPlayersTab() throws Exception
    {
        FXMLLoader fxmlLoader;
        URL url;
        Parent tile;
        PlayerTileController c;
        try {
            int num=model.getGameManager().getNumOfRound();
            if (model.getGameManager().getRounds().size()==1)
                num--;
            for (int i = 0; i <model.getGameManager().getRounds().get(num).getPlayers().size() ; i++) {
                fxmlLoader = new FXMLLoader();
                url = getClass().getResource("playerTile.fxml");
                fxmlLoader.setLocation(url);
                tile = fxmlLoader.load(url.openStream());
                c = fxmlLoader.getController();
                c.initTile(model,model.getGameManager().getRounds().get(num).getPlayers().get(i));
                playerGrid.add(tile, 1, i);
                updateNameAndImage(model.getGameManager().getRounds().get(num).getPlayers().get(i), i);
            }
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    public void createMyTerritoriesTab()
    {
        model.getStartTurnProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
            {
                try {
                    Separator separator;
                    FXMLLoader fxmlLoader;
                    URL url;
                    Parent IDtile;
                    Parent conqueredTile;
                    ConqueredTileController conqueredTileController;
                    IdTileController idTileController;
                    HBox hBox;
                    VBox vbox;
                    Player player = model.getGameManager().getCurrPlayer();
                    List<Unit> occupyingArmy;
                    if (player!=null)
                    {
                        for (int i = 0; i < player.getConqueredSize(); i++) {
                            if (i!=0)
                            {
                                separator = new Separator();
                                territoryVbox.getChildren().add(separator);
                            }
                            hBox= new HBox();
                            hBox.setSpacing(10);
                            fxmlLoader = new FXMLLoader();
                            url = getClass().getResource("idTile.fxml");
                            fxmlLoader.setLocation(url);
                            IDtile = fxmlLoader.load(url.openStream());
                            idTileController = fxmlLoader.getController();
                            idTileController.initTile(player.getConquered().get(i));
                            hBox.getChildren().add(IDtile);
                            occupyingArmy = player.getConquered().get(i).getOccupyingArmy();
                            vbox= new VBox();
                            for (int j=0; j<occupyingArmy.size();j++)
                            {
                                fxmlLoader = new FXMLLoader();
                                url = getClass().getResource("conqueredTile.fxml");
                                fxmlLoader.setLocation(url);
                                conqueredTile= fxmlLoader.load(url.openStream());
                                conqueredTileController = fxmlLoader.getController();
                                conqueredTileController.initTile(occupyingArmy.get(j));
                                vbox.getChildren().add(conqueredTile);
                            }
                            hBox.getChildren().add(vbox);
                            hBox.prefWidth(290);
                            territoryVbox.getChildren().add(hBox);
                        }
                    }
                }
                catch (IOException e)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText(null);
                    alert.setContentText("Whoa " + e.getMessage());
                    alert.showAndWait();
                }
            }
            else {
                removeTiles(territoryVbox.getChildren());
            }
        });
    }

    public void createUnitsTab() throws Exception
    {
        FXMLLoader fxmlLoader;
        URL url;
        Parent tile;
        ShopTileController c;
        HBox hBox;
        try {
            for (int i = 0; i < model.getGameManager().getRounds().get(0).getModel().size(); i++) {
                fxmlLoader = new FXMLLoader();
                url = getClass().getResource("shopTile.fxml");
                fxmlLoader.setLocation(url);
                tile = fxmlLoader.load(url.openStream());
                c = fxmlLoader.getController();
                c.initTile(model.getGameManager().getRounds().get(0).getModel().get(i), model);
                hBox= new HBox();
                hBox.getChildren().add(tile);
                unitShopBox.getChildren().add(hBox);
            }

        }
        catch (Exception e)
        {
            throw e;
        }
    }

    public void updateNameAndImage(Player player, int i)
    {
        switch (i)
        {
            case 0:
            {
                player1Name.setText(player.getName());
                p1Img.setImage(new Image(images[0]));
                break;
            }
            case 1:
            {
                player2Name.setText(player.getName());
                p2Img.setImage(new Image(images[1]));
                sep00.setVisible(true);
                sep10.setVisible(true);
                break;
            }
            case 2:
            {
                player3Name.setText(player.getName());
                p3Img.setImage(new Image(images[2]));
                sep01.setVisible(true);
                sep11.setVisible(true);
                break;
            }
            case 3:
            {
                player4Name.setText(player.getName());
                p4Img.setImage(new Image(images[3]));
                sep02.setVisible(true);
                sep12.setVisible(true);
                break;
            }
        }
    }
}
