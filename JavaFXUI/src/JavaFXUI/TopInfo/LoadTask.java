package JavaFXUI.TopInfo;

import JavaFXUI.Model;
import generated.GameDescriptor;
import javafx.concurrent.Task;
import top.GameManager;

import java.io.File;

public class LoadTask extends Task<Model> {

    private GameDescriptor gameDes;
    private GameManager gameManager;
    private File XMLfile;
    private Model model;

    protected LoadTask(File file)
    {
        model=new Model();
        XMLfile = file;
        model.setGameManager(null);
    }

    public Model call() throws Exception{
        try
        {
            if (getFile())
            {
                gameDes = GameManager.load();
                if (checkXmlInfo())
                {
                    model.setMessageP("File loaded successfully :)");
                    gameManager = new GameManager(gameDes);
                    gameManager.createRoundZero(gameDes);
                    model.setGameManager(new GameManager(gameManager));
                    return model;
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return model;
    }

    public boolean checkXmlInfo()
    {
        int report = GameManager.checkXmlInfo(gameDes);
        switch (report)
        {
            case 1: {
                model.setMessageP("Illegal game info. Columns must be between 3-30");
                break;
            }
            case 2:
            {
                model.setMessageP("Illegal game info. Rows must be between 2-30");
                break;
            }
            case 3:
            {
                model.setMessageP("Illegal game info. File must supply a full definition for each Territory or supply default values");
                break;
            }
            case 4:
            {
                model.setMessageP("Illegal game info. Territory id number must be between 0-"+gameDes.getGame().getBoard().getRows().intValue()*
                          gameDes.getGame().getBoard().getColumns().intValue());
                break;
            }
            case 5:
            {
                model.setMessageP("Illegal game info. Territory id must be unique and cannot repeat");
                break;
            }
            case 6:
            {
                model.setMessageP("Illegal game info. Number of players must be between 2-4");
                break;
            }
            case 7:
            {
                model.setMessageP("Illegal game info. Player id must be unique and cannot repeat");
                break;
            }
            case 8:
            {
                model.setMessageP("Illegal game info. Unit rank must be between 1-"+gameDes.getGame().getArmy().getUnit().size());
                break;
            }
            case 9:
            {
                model.setMessageP("Illegal game info. Unit name must be unique and cannot repeat");
                break;
            }
            case 10:
            {
                model.setMessageP("Illegal game info. Unit rank must be unique and cannot repeat");
                break;
            }
            case 11:
                return true;
        }
        GameManager.setCurrentFile(null);
        return false;
    }


    public boolean getFile() {
        int report = GameManager.getFile(XMLfile.getPath());
        switch (report)
        {
            case 1: {
                model.setMessageP("File doesn't exist");
                break;
            }
            case 2:
            {
                model.setMessageP("File isn't an xml file");
                break;
            }
            case 3:
            {
                model.setMessageP("File name is too short.");
                break;
            }
            case 4:
                return true;
        }
        return false;
    }
}
