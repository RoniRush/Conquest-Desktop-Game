package top;

import bottom.Player;
import generated.GameDescriptor;
import generated.Unit;
import javafx.beans.property.SimpleStringProperty;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GameManager implements Serializable {
    protected int numOfRound;
    protected int turnIndicator;
    protected final int maxRounds;
    protected Player currPlayer;
    protected List<Round> rounds;
    protected static File currentFile=null;
    protected boolean betweenRounds;

    public GameManager(GameDescriptor gameDes) {
        numOfRound =turnIndicator= 0;
        maxRounds = gameDes.getGame().getTotalCycles().intValue();
        rounds= new ArrayList<>(maxRounds+1);
        betweenRounds=false;
    }

    public GameManager(GameManager other) {
        numOfRound = other.numOfRound;
        maxRounds = other.maxRounds;
        turnIndicator = other.turnIndicator;
        currPlayer = other.currPlayer;
        rounds= new ArrayList<>(maxRounds+1);
        Round round = new Round(other.rounds.get(0));
        rounds.add(round);
        this.betweenRounds=other.betweenRounds;
    }


    public void setBetweenRounds(boolean betweenRounds) {
        this.betweenRounds = betweenRounds;
    }

    public boolean isBetweenRounds() {
        return betweenRounds;
    }

    public static File getCurrentFile() {
        return currentFile;
    }

    public static void setCurrentFile(File currentFile) {
        GameManager.currentFile = currentFile;
    }

    public int getTurnIndicator() {
        return turnIndicator;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public static GameManager loadManagerFromFile(File file) throws IOException, ClassNotFoundException
    {
        GameManager manager;
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(file))) {
            manager = (GameManager) in.readObject();
            return manager;

        } catch (IOException | ClassNotFoundException e) {
            throw e;
        }
    }

    public static boolean isCurrFile()
    {
        if (currentFile==null)
            return false;
        return true;
    }

    public static File makeFileFromPath(String filePath)
    {
        Path path = Paths.get(filePath);
        return path.toFile();
    }

    public static int getBinFile(String filePath) throws InvalidPathException
    {
        File file;
        String ending;
        try
        {
            file = makeFileFromPath(filePath);
            if (file.exists())
            {
                String fileName = file.getName();
                int length = fileName.length();
                if (length>3)
                {
                    ending = fileName.substring(length - 3);
                    if (!ending.equals("dat"))
                        return 2;
                    else
                        return 1;
                }
                else
                    return 3;
            }
            else
                return 4;
        }
        catch (InvalidPathException e)
        {
            throw e;
        }
    }

    public int getNumOfRound() {
        return numOfRound;
    }



    public static int checkXmlInfo(GameDescriptor gameDes)
    {
        BigInteger c = gameDes.getGame().getBoard().getColumns();
        BigInteger r = gameDes.getGame().getBoard().getRows();
        int [] bucket;
        if(c.intValue()<3 || c.intValue()>30)
            return 1;
        if (r.intValue()<2 || r.intValue()>30)
            return 2;
        bucket= new int [(r.intValue()*c.intValue())+1];
        for (int i=0; i<bucket.length; i++)
            bucket[i]=0;
        int length = gameDes.getGame().getTerritories().getTeritory().size();
        if (length<c.intValue()*r.intValue() &&
                (gameDes.getGame().getTerritories().getDefaultArmyThreshold()==null ||
                        gameDes.getGame().getTerritories().getDefaultProfit()==null ))
            return 3;
        for (int i=0; i< length; i++)
        {
            int id= gameDes.getGame().getTerritories().getTeritory().get(i).getId().intValue();
            if (id<0|| id>(r.intValue()*c.intValue()))
                return 4;
            else
                bucket[id]++;
        }
        for (int i=0; i<bucket.length; i++)
        {
            if (bucket[i]>1)
                return 5;
        }
        if (gameDes.getGameType().equals("MultiPlayer")) {
            List<generated.Player> generatedP = gameDes.getPlayers().getPlayer();
            if (generatedP.size() > 4 || generatedP.size() < 2)
                return 6;
            for (int i = 0; i < generatedP.size(); i++) {
                for (int j = i + 1; j < generatedP.size(); j++)
                    if (generatedP.get(i).getId().intValue() == generatedP.get(j).getId().intValue())
                        return 7;
            }
            List<Unit> generatedU = gameDes.getGame().getArmy().getUnit();
            bucket= new int [generatedU.size()+1];
            Set<String> names = new HashSet<>();
            for (int i=0; i<bucket.length; i++)
                bucket[i]=0;
            for (int i=0;i<generatedU.size();i++)
            {
                if (generatedU.get(i).getRank()>=bucket.length ||generatedU.get(i).getRank()<=0)
                    return 8;
                bucket[generatedU.get(i).getRank()]++;
                names.add(generatedU.get(i).getType());
            }
            if (names.size()!=generatedU.size())
                return 9;
            for (int i=1;i<bucket.length;i++)
                if (bucket[i]!=1)
                    return 10;
        }
        return 11;
    }

    public void setNumOfRound(int numOfRound) {
        this.numOfRound = numOfRound;
    }

    public void deleteRound()
    {
        rounds.remove(numOfRound);
    }

    public void setTurnIndicator(int turnIndicator) {
        this.turnIndicator = turnIndicator;
    }

    public int getNumOfPlayers()
    {
        if (rounds.size()==1)
            return rounds.get(0).getNumOfPlayers();
        Round round= rounds.get(numOfRound);
        return round.getNumOfPlayers();
    }

    public void addNewPlayer(String name, GameDescriptor gameDes, int i)
    {
        rounds.get(0).players.add(new Player((char)('"'+i),name,gameDes.getGame().getInitialFunds().intValue()));
    }

    public boolean saveGame(String filePath) throws IOException
    {
        File file = new File(filePath);
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(file))) {
            out.writeObject(this);
            out.flush();
            return true;
        } catch (IOException e) {
            throw e;
        }
    }

    public void createRoundZero(GameDescriptor gameDes) {
        rounds.add(new Round(gameDes));
        numOfRound++;
    }


    public void setCurrPlayer() {
        currPlayer = rounds.get(numOfRound).players.get(turnIndicator);
    }

    public boolean createNewRound()
    {
        rounds.add(new Round(rounds.get(numOfRound - 1)));
        if (numOfRound>1)
        {
            for(int i=0; i<rounds.get(numOfRound).getPlayers().size();i++)
            {
                for(int j=0; j<rounds.get(numOfRound).getPlayers().get(i).getConquered().size();j++)
                    rounds.get(numOfRound).getPlayers().get(i).getConquered().get(j).setOwner(rounds.get(numOfRound).getPlayers().get(i));
            }
            return true;
        }
        return false;
    }

    public static int getFile(String FILE_PATH) {
        int length;
        File file = new File(FILE_PATH);
        if (!file.exists())
            return 1;
        length = FILE_PATH.length();
        if (length > 3) {
            String ending = FILE_PATH.substring(length - 3);
            if (!ending.equals("xml"))
                return 2;
        }
        else
            return 3;
        currentFile = file;
        return 4;
    }


    public static GameDescriptor load() throws JAXBException
    {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (GameDescriptor) jaxbUnmarshaller.unmarshal(currentFile);
        } catch (JAXBException e) {
            throw e;
        }
    }

    public List<Player> whoWon() {
        int max = rounds.get(maxRounds).players.get(0).totalValOfTerritories();
        List<Player> winners = new ArrayList<>();
        boolean flag;
        winners.add(rounds.get(maxRounds).players.get(0));
        int cur;
        for (int i = 1; i < rounds.get(maxRounds).numOfPlayers; i++) {
            flag=false;
            cur = rounds.get(maxRounds).players.get(i).totalValOfTerritories();
            if (cur > max) {
                winners.clear();
                winners.add(rounds.get(maxRounds).players.get(i));
                max = cur;
                flag=true;
            }
            if (cur == max &&!flag) {
                winners.add(rounds.get(maxRounds).players.get(i));
            }
        }
        return winners;
    }


}


