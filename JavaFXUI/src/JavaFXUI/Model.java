package JavaFXUI;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import top.GameManager;

public class Model {

    protected SimpleBooleanProperty turnEnd;
    protected SimpleBooleanProperty roundEnd;
    protected SimpleBooleanProperty XMLloaded;
    protected GameManager gameManager;
    protected SimpleStringProperty messageP;
    protected SimpleBooleanProperty startTurn;
    protected SimpleBooleanProperty wentBack;
    protected Boolean cleared;
    protected boolean fromUndo;
    protected String CSS;
    protected SimpleBooleanProperty styleChooser;
    protected SimpleBooleanProperty surrender;
    protected SimpleBooleanProperty gameEnded;
    protected SimpleBooleanProperty gameAlmostEnded;
    protected SimpleBooleanProperty checkColor;
    protected boolean wasBattle;

    public Model()
    {
        XMLloaded = new SimpleBooleanProperty(false);
        turnEnd = new SimpleBooleanProperty(false);
        roundEnd = new SimpleBooleanProperty(false);
        messageP = new SimpleStringProperty();
        cleared=false;
        fromUndo=false;
        gameEnded = new SimpleBooleanProperty(false);
        startTurn = new SimpleBooleanProperty(false);
        wentBack= new SimpleBooleanProperty(false);
        surrender = new SimpleBooleanProperty(false);
        checkColor = new SimpleBooleanProperty(false);
        gameAlmostEnded = new SimpleBooleanProperty(false);
        wasBattle= false;
        styleChooser = new SimpleBooleanProperty(false);
        CSS="Camelot.css";
    }

    public void setCSS(int style) {
        switch (style)
        {
            case 0:
            {
                CSS="Camelot.css";
                break;
            }
            case 1:
            {
                CSS="Atlantis.css";
                break;
            }
            case 2:
            {
                CSS="Barbie.css";
                break;
            }
        }
    }

    public void setStyleChooser(boolean styleChooser) {
        this.styleChooser.set(styleChooser);
    }

    public String getCSS() {
        return CSS;
    }

    public SimpleBooleanProperty getStyleChooserProperty() {
        return styleChooser;
    }

    public void setGameAlmostEnded(boolean gameAlmostEnded) {
        this.gameAlmostEnded.set(gameAlmostEnded);
    }

    public SimpleBooleanProperty getGameAlmostEndedProperty() {
        return gameAlmostEnded;
    }

    public boolean isWasBattle() {
        return wasBattle;
    }

    public void setWasBattle(boolean wasBattle) {
        this.wasBattle = wasBattle;
    }

    public boolean isFromUndo() {
        return fromUndo;
    }

    public void setFromUndo(boolean fromUndo) {
        this.fromUndo = fromUndo;
    }

    public void setCheckColor(boolean checkColor) {
        this.checkColor.set(checkColor);
    }

    public SimpleBooleanProperty getCheckColorProperty() {
        return checkColor;
    }

    public SimpleBooleanProperty getGameEndedProperty() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded.set(gameEnded);
    }

    public Boolean getCleared() {
        return cleared;
    }

    public void setCleared(Boolean cleared) {
        this.cleared = cleared;
    }

    public SimpleBooleanProperty getSurrenderProperty() {
        return surrender;
    }

    public void setSurrender(boolean surrender) {
        this.surrender.set(surrender);
    }

    public void setWentBack(boolean wentBack) {
        this.wentBack.set(wentBack);
    }

    public SimpleBooleanProperty getwentBackProperty() {
        return wentBack;
    }

    public void setRoundEnd(boolean roundEnd) {
        this.roundEnd.set(roundEnd);
    }

    public SimpleBooleanProperty getRoundEnd() {
        return roundEnd;
    }

    public void setTurnEnd(boolean turnEnd) {
        this.turnEnd.set(turnEnd);
    }

    public void setXMLloaded(boolean XMLloaded) {
        this.XMLloaded.set(XMLloaded);
    }

    public SimpleBooleanProperty getturnEndProperty() {
        return turnEnd;
    }

    public SimpleBooleanProperty getXMLloadedProperty() {
        return XMLloaded;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setMessageP(String messageP) {
        this.messageP.set(messageP);
    }

    public SimpleStringProperty getMessagePProperty() {
        return messageP;
    }

    public SimpleBooleanProperty getStartTurnProperty() {
        return startTurn;
    }

    public void setStartTurn(boolean startTurn) {
        this.startTurn.set(startTurn);
    }

}
