package JavaFXUI.TopInfo;

import JavaFXUI.Model;
import JavaFXUI.TopInfo.TopControllers.*;
import bottom.Player;
import bottom.Slot;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import top.GameManager;
import top.Round;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TopController {

    private GameManager gameManager;
    private Model model;
    private String message;
    private Boolean fromFile;

    @FXML
    private Button passTurnButton;

    @FXML
    private VBox undoAndMoreVbox;

    @FXML
    private Button startRound;

    @FXML
    private Button saveGame;

    @FXML
    private Button undo;

    @FXML
    private HBox loadAndstartHbox;

    @FXML
    private Button loadXML;

    @FXML
    private Button loadSavedGame;

    @FXML
    private Button surrender;

    @FXML
    private Button history;

    @FXML
    private Button startGame;

    @FXML
    private VBox roundAndPlayerVbox;

    @FXML
    private HBox RoundHbox;

    @FXML
    private Label currRoundLabel;

    @FXML
    private Label sepLabel;

    @FXML
    private Label maxRoundLabel;

    @FXML
    private HBox currentPlayerHbox;

    @FXML
    private Label currentPLayerLabel;

    @FXML
    private HBox styleHbox;

    @FXML
    private ToggleButton animToggButt;


    @FXML
    private ChoiceBox<String> styleSelector;

    @FXML
    void surrenderOnAction(ActionEvent event) {
        FXMLLoader fxmlLoader;
        URL url;
        Parent tile;
        surrenderController c;
        try {
            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("surrender.fxml");
            fxmlLoader.setLocation(url);
            tile = fxmlLoader.load(url.openStream());
            c = fxmlLoader.getController();
            c.initPopUp(model,surrender.getScene().getWindow());
            Scene scene = new Scene(tile,600,400);
            Stage pop = new Stage();
            pop.initModality(Modality.WINDOW_MODAL);
            pop.initOwner(surrender.getScene().getWindow());
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            pop.setScene(scene);
            pop.setTitle("Surrender");
            pop.show();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Something went wrong "+e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void animationOnAction(ActionEvent event) {

    }

    @FXML
    void historyOnAction(ActionEvent event) {
        FXMLLoader fxmlLoader;
        URL url;
        Parent tile;
        ReplayController c;
        try {
            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("replay.fxml");
            fxmlLoader.setLocation(url);
            tile = fxmlLoader.load(url.openStream());
            c = fxmlLoader.getController();
            c.initPopUp(model);
            Scene scene = new Scene(tile,1000,600);
            Stage pop = new Stage();
            pop.setMinWidth(1000);
            pop.setMinHeight(600);
            pop.initModality(Modality.WINDOW_MODAL);
            pop.initOwner(history.getScene().getWindow());
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            pop.setScene(scene);
            pop.setTitle("Replay");
            pop.show();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Something went wrong "+e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void loadSavedFileOnAction(ActionEvent event) {
        model.setGameEnded(false);
        history.setDisable(true);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select your saved file");
        File file = fileChooser.showOpenDialog(loadSavedGame.getScene().getWindow());
        if (file != null)
          loadSavedFile(file);
    }

    public void loadSavedFile(File file)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Welcome");
        alert.setContentText("Loading. Please wait");
        alert.setHeaderText(null);
        alert.show();
        model.setXMLloaded(false);
        int length = file.getName().length();
        String ending = file.getName().substring(length - 3);
        if (!ending.equals("dat")) {
            alert.close();
            clearData();
            model.setXMLloaded(false);
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("File is not a .dat file.");
            alert.showAndWait();
        }
        else
        {
            try
            {
                gameManager = GameManager.loadManagerFromFile(file);
                model.setGameManager(gameManager);
                restoreTransient();
                currRoundLabel.setText(Integer.toString(gameManager.getNumOfRound()));
                maxRoundLabel.setText(Integer.toString(gameManager.getMaxRounds()));
                if (!gameManager.isBetweenRounds())
                    currentPLayerLabel.setText(gameManager.getCurrPlayer().getName());
                else
                    currentPLayerLabel.setText("");
                if(!model.getCleared())
                {
                    addListeners();
                    model.setCleared(true);
                }
                model.setXMLloaded(true);
                startGame.setDisable(false);
                Alert alert2 = new Alert(AlertType.INFORMATION);
                alert2.setHeaderText(null);
                alert2.setTitle("Welcome Back");
                alert2.setContentText("File loaded successfully :)");
                alert2.showAndWait();
                fromFile = true;
                model.setCheckColor(true);
                model.setCheckColor(false);
            }
            catch (Exception e)
            {
                alert.setAlertType(AlertType.ERROR);
                alert.setContentText("Something went wrong "+e.getMessage());
                alert.showAndWait();
            }
            alert.close();
        }
    }

    public void restoreTransient()
    {
        for(int i=0;i<gameManager.getRounds().size();i++)
        {
            Round round = gameManager.getRounds().get(i);
            for(int j=0;j<round.getNumOfPlayers();j++)
                round.getPlayers().get(j).setTotalTuring(round.getPlayers().get(j).getTempTotalTuring());
            round.createUnitsOnBoard();
        }
    }

    @FXML
    void holdPositionOnAction(ActionEvent event) {
        FXMLLoader fxmlLoader;
        URL url;
        Parent tile;
        PassTurnController c;
        try {
            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("passTurnPopUp.fxml");
            fxmlLoader.setLocation(url);
            tile = fxmlLoader.load(url.openStream());
            c = fxmlLoader.getController();
            c.initPopUp(model);
            Scene scene = new Scene(tile,611,421);
            Stage pop = new Stage();
            pop.initModality(Modality.WINDOW_MODAL);
            pop.initOwner(passTurnButton.getScene().getWindow());
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            pop.setScene(scene);
            pop.setTitle("Hold Position");
            pop.show();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Something went wrong "+e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void loadXMLOnAction(ActionEvent event) {
        model.setGameEnded(false);
        history.setDisable(true);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an XML file");
        File file = fileChooser.showOpenDialog(loadXML.getScene().getWindow());
        if (file != null)
            loadXML(file);
    }

    public void loadXML(File file)
    {
        LoadTask task = new LoadTask(file);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Welcome");
        alert.setContentText("Loading. Please wait");
        alert.setHeaderText(null);
        try {
            model.setXMLloaded(false);
            Thread loadThread = new Thread(task);
            alert.show();
            Thread.sleep(2000L);
            alert.close();
            task.setOnSucceeded(event -> {
                message= task.getValue().getMessagePProperty().get();
                Alert alert2 = new Alert(AlertType.INFORMATION);
                if(task.getValue().getGameManager()!=null) {
                    gameManager = new GameManager(task.getValue().getGameManager());
                    gameManager.setCurrentFile(file);
                    initTop();
                    if(!model.getCleared()) {
                        addListeners();
                        model.setCleared(true);
                    }
                    model.setGameManager(gameManager);
                    model.setXMLloaded(true);
                    startGame.setDisable(false);
                }
                else {
                    clearData();
                    model.setXMLloaded(false);
                    alert2.setAlertType(AlertType.ERROR);
                }
                alert2.setTitle("Welcome");
                alert2.setContentText(message);
                alert2.setHeaderText(null);
                alert2.show();
            });
            loadThread.start();
        }
        catch (Exception e)
        {
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("Something went wrong "+e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void initialize()
    {
        startGame.setDisable(true);
        startRound.setDisable(true);
        saveGame.setDisable(true);
        undo.setDisable(true);
        passTurnButton.setDisable(true);
        history.setDisable(true);
        surrender.setDisable(true);
        fromFile=false;
    }

    public void setCheckBox()
    {
        String[] strings = {"Camelot","Atlantis","Doll House"};
        styleSelector.getItems().addAll(strings);
        styleSelector.getSelectionModel().select(0);
        styleSelector.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            model.setCSS(newValue.intValue());
            model.setStyleChooser(true);
            model.setStyleChooser(false);
        });
    }

    @FXML
    void saveGameOnAction(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
            fileChooser.getExtensionFilters().add(filter);
            fileChooser.setTitle("Save Game");
            File file = fileChooser.showSaveDialog(saveGame.getScene().getWindow());
            if (file != null) {
                for(int i=0;i<gameManager.getRounds().size();i++)
                {
                    Round round = gameManager.getRounds().get(i);
                    for(int j=0;j<round.getUnitsOnBoard().length;j++)
                        round.getTempUnitsOnBoard()[j] = round.getUnitsOnBoard()[j].get();
                }
                gameManager.saveGame(file.getPath());
            }
        }
        catch(Exception e)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Something went wrong "+e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void startGameOnAction(ActionEvent event) {
        if (startGame.getText().equals("Start Game"))
        {
            Stage stage = new Stage();
            stage.setTitle("Opening");
            stage.setWidth(700);
            stage.setHeight(400);
            VBox vbox = new VBox();
            vbox.setSpacing(20);
            String pic;
            final Text text = new Text(10, 20, "");
            text.setFont(Font.font("Elephant", FontWeight.BOLD,16));
            if(model.getCSS().equals("Camelot.css"))
            {
                pic = "JavaFXUI/TopInfo/camelotCastle.jpeg";
                text.setFill(Color.DARKGREEN);
            }
            else if(model.getCSS().equals("Atlantis.css"))
            {
                pic = "JavaFXUI/TopInfo/AtlantisCastle.jpeg";
                text.setFill(Color.DARKBLUE);
            }
            else
            {
                pic = "JavaFXUI/TopInfo/barbieCastle.jpeg";
                text.setFill(Color.DEEPPINK);
            }
            BackgroundImage myBI= new BackgroundImage(new Image(pic,700,400,false,true),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT);
            vbox.setBackground(new Background(myBI));
            final String content = "Hello!\nWelcome to Medieval Ronru, The most magical kingdom that ever was\n" +
                    "I'm Princess Rushkin, but you can call me Your Highness.\nI'll be your guide, tee-hee\n\n" +
                    "You've chosen the absolutely bestest time in the world to visit Ronru!\n" +
                    "It's our annual CONQUEST fair and we welcome all who wish to participate\n" +
                    "OMG, I'm so excited!\nI hope you're ready!\n\n" +
                    "Each of you will be competing in rounds based on your turn order\n" +
                    "Remember! your goal is to own the most lucrative territories come the final round\n" +
                    "To win the ultimate reward!\n\nMy HAND of course, tee-hee";

            final Animation animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(10000));
                }

                protected void interpolate(double frac) {
                    final int length = content.length();
                    final int n = Math.round(length * (float) frac);
                    text.setText(content.substring(0, n));
                }

            };
            animation.play();
            vbox.getChildren().add(text);
            Scene scene = new Scene(vbox,700,400);
            stage.setScene(scene);
            stage.show();

            history.setDisable(true);
            if(model.getGameEndedProperty().get())
                loadXML(GameManager.getCurrentFile());
            model.setGameEnded(false);
            startGame.setText("End Game");
            loadXML.setDisable(true);
            loadSavedGame.setDisable(true);
            if(fromFile) {
                fromFile=false;
                if(!gameManager.isBetweenRounds()) {
                    passTurnButton.setDisable(false);
                    surrender.setDisable(false);
                    startRound.setDisable(true);
                }
                else {
                    passTurnButton.setDisable(true);
                    surrender.setDisable(true);
                    startRound.setDisable(false);
                }
            }
            else
                startRound.setDisable(false);
        }
        else if(startGame.getText().equals("End Game"))
        {
            startGame.setText("Start Game");
            startGame.setDisable(true);
            startRound.setDisable(true);
            saveGame.setDisable(true);
            loadXML.setDisable(false);
            loadSavedGame.setDisable(false);
            surrender.setDisable(true);
            history.setDisable(true);
            passTurnButton.setDisable(true);
            undo.setDisable(true);
            clearData();
            model.setCleared(true);
            model.setXMLloaded(false);
        }
        else
        {
            startGame.setText("Start Game");
            startGame.setDisable(false);
            startRound.setDisable(true);
            passTurnButton.setDisable(true);
            surrender.setDisable(true);
            history.setDisable(false);
            saveGame.setDisable(true);
            loadSavedGame.setDisable(false);
            loadXML.setDisable(false);
            undo.setDisable(true);
            model.setGameAlmostEnded(false);
            model.setGameEnded(true);
        }
    }

    @FXML
    void startRoundOnAction(ActionEvent event) {
        startRound.setDisable(true);
        passTurnButton.setDisable(false);
        surrender.setDisable(false);
        undo.setDisable(true);
        saveGame.setDisable(false);
        if (!model.isFromUndo())
            createNewRound();
        model.setFromUndo(false);
        model.setRoundEnd(false);
        gameManager.setCurrPlayer();
        initTop();
        addRewards(gameManager.getNumOfPlayers());
        model.setStartTurn(true);
        gameManager.setBetweenRounds(false);
        model.setCheckColor(true);
        model.setCheckColor(false);
    }

    public void addRewards(int numOfPlayers)
    {
        Player player;
        if(gameManager.getNumOfRound()>1) {
            for (int i = 0; i < numOfPlayers; i++) {
                player = gameManager.getRounds().get(gameManager.getNumOfRound()).getPlayers().get(i);
                player.addRewards();
                int reward = player.totalValOfTerritories();
                player.getNotifications().add("The previous round has earned you:\n A whopping amount of " +
                        reward + " Turing\n" +
                        "There might be hope for you, yet");
            }
        }
    }

    public void addListeners()
    {
        model.getturnEndProperty().addListener((observable, oldValue, newValue) -> {
            if (model.getturnEndProperty().get())
            {
                model.setStartTurn(false);
                model.setCheckColor(true);
                model.setCheckColor(false);
                if (gameManager.getRounds().get(gameManager.getNumOfRound()).getNumOfPlayers()<=1)
                    model.setGameEnded(true);
                else {
                    if (gameManager.getTurnIndicator() >= gameManager.getNumOfPlayers() - 1)
                        model.setRoundEnd(true);
                    else {
                        gameManager.setTurnIndicator(gameManager.getTurnIndicator() + 1);
                        gameManager.setCurrPlayer();
                        currentPLayerLabel.setText(gameManager.getCurrPlayer().getName());
                        model.setStartTurn(true);
                    }
                    model.setTurnEnd(false);
                }
            }
        });
        model.getStartTurnProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                model.setCheckColor(true);
                model.setCheckColor(false);
            }
        });
        model.getRoundEnd().addListener((observable, oldValue, newValue) -> {
            if (model.getRoundEnd().get())
            {
                gameManager.setTurnIndicator(0);
                gameManager.setNumOfRound(gameManager.getNumOfRound() + 1);
                startRound.setDisable(false);
                currentPLayerLabel.setText("");
                undo.setDisable(false);
                passTurnButton.setDisable(true);
                gameManager.setBetweenRounds(true);
                surrender.setDisable(true);
                if(gameManager.getNumOfRound()>gameManager.getMaxRounds())
                    model.setGameAlmostEnded(true);
            }
        });
        model.getwentBackProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (model.getXMLloadedProperty().get()) {
                    currRoundLabel.setText(Integer.toString(gameManager.getNumOfRound()));
                    maxRoundLabel.setText(Integer.toString(gameManager.getMaxRounds()));
                    model.setGameAlmostEnded(false);
                    startRound.setDisable(false);
                    saveGame.setDisable(false);
                    startGame.setText("End Game");
                }
                if (gameManager.getNumOfRound() == 1)
                    undo.setDisable(true);
            }
            else
            {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("WOW");
                alert.setHeaderText("SHAZAM!");
                alert.setContentText("Now we're back at the beginning of round " + gameManager.getNumOfRound());
                alert.showAndWait();
            }
        });
        model.getGameAlmostEndedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                startGame.setText("Finish Game");
                startGame.setDisable(false);
                startRound.setDisable(true);
                passTurnButton.setDisable(true);
                surrender.setDisable(true);
                saveGame.setDisable(true);
                loadSavedGame.setDisable(true);
                loadXML.setDisable(true);
                undo.setDisable(false);
            }
        });
        model.getGameEndedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                List<Player> winners = gameManager.whoWon();
                List<String> names = new ArrayList<>();
                for(int i=0;i<winners.size();i++)
                    names.add(winners.get(i).getName());
                FXMLLoader fxmlLoader;
                URL url;
                Parent tile;
                WinnerPopUp c;
                try {
                    fxmlLoader = new FXMLLoader();
                    url = getClass().getResource("winnerPopUp.fxml");
                    fxmlLoader.setLocation(url);
                    tile = fxmlLoader.load(url.openStream());
                    c = fxmlLoader.getController();
                    c.initPopUp(names);
                    Scene scene = new Scene(tile,600,400);
                    Stage pop = new Stage();
                    pop.initModality(Modality.WINDOW_MODAL);
                    pop.initOwner(startGame.getScene().getWindow());
                    scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
                    pop.setScene(scene);
                    pop.setTitle("GAME OVER");
                    pop.show();
                }
                catch (Exception e)
                {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText("Something went wrong "+e.getMessage());
                    alert.showAndWait();
                }
            }
        });
    }


    public void createNewRound() {
        if (gameManager.createNewRound()) {
            Round round = gameManager.getRounds().get(gameManager.getNumOfRound());
            List<Slot> deadAndBuried;
            List<Slot> totalDead = new ArrayList<>();
            for (int i = 0; i < round.getNumOfPlayers(); i++) {
                deadAndBuried = round.calcFatigue(i);
                if (deadAndBuried.size() > 0)
                    totalDead.addAll(deadAndBuried);
            }
            if (totalDead.size() > 0) {
                for (int i = 0; i < round.getNumOfPlayers(); i++) {
                    String dead = "The following Territories have turned neutral:\n";
                    for (int j = 0; j < totalDead.size(); j++) {
                        if (!dead.equals("The following Territories have turned neutral:\n"))
                            dead = dead.concat(", ");
                        dead = dead.concat(totalDead.get(j).getSerialNumber().toString());
                    }
                    dead = dead.concat("\n");
                    dead = dead.concat("tsk-tsk, All those poor dead soldiers, sigh");
                    round.getPlayers().get(i).getNotifications().add(dead);
                }
            }
            for(int i=0;i<round.getPlayers().size();i++)
            {
                List<Slot> conquered = round.getPlayers().get(i).getConquered();
                for(int j=0;j<conquered.size();j++)
                    conquered.get(j).calcHealPerSlot();
            }
        }
    }

    @FXML
    void undoOnAction(ActionEvent event)
    {
        FXMLLoader fxmlLoader;
        URL url;
        Parent tile;
        undoController c;
        try {
            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("undo.fxml");
            fxmlLoader.setLocation(url);
            tile = fxmlLoader.load(url.openStream());
            c = fxmlLoader.getController();
            c.initPopUp(model);
            Scene scene = new Scene(tile,600,400);
            Stage pop = new Stage();
            pop.initModality(Modality.WINDOW_MODAL);
            pop.initOwner(undo.getScene().getWindow());
            scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
            pop.setScene(scene);
            pop.setTitle("Turn Back Time");
            pop.show();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Something went wrong "+e.getMessage());
            alert.showAndWait();
        }
    }

    public void setModel(Model model)
    {
        this.model=model;
    }

    public void initTop()
    {
        currRoundLabel.setText(Integer.toString(gameManager.getNumOfRound()));
        maxRoundLabel.setText(Integer.toString(gameManager.getMaxRounds()));
        currentPLayerLabel.setText(gameManager.getRounds().get(0).getPlayers().get(0).getName());
    }

    public void clearData()
    {
        currRoundLabel.setText("");
        maxRoundLabel.setText("");
        currentPLayerLabel.setText("");
        startGame.setDisable(true);
    }

}





