package bottom;

import generated.GameDescriptor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player implements Serializable {
    transient protected SimpleIntegerProperty totalTuring;
    protected int tempTotalTuring;
    protected List<Slot> conquered;
    protected final char sign;
    protected final String name;
    protected List<String> casualties;
    protected final int id;
    protected List<String> notifications;
    protected double[] color; // red, green, blue

    public Player(char sign, String name, int initialFunds) {
        this.sign = sign;
        this.name = name;
        color = new double[3];
        id = 0;
        tempTotalTuring=0;
        totalTuring = new SimpleIntegerProperty();
        if (initialFunds>=0)
            totalTuring.set(initialFunds);
        else
            totalTuring.set(1000);
        conquered = new ArrayList<>(10);
        casualties = new ArrayList<>(10);
        notifications = new ArrayList<>(10);
    }

    public Player(GameDescriptor gameDes,int index)
    {
        totalTuring = new SimpleIntegerProperty();
        color = new double[3];
        chooseColor(index);
        sign = (char)(index+'#');
        name = gameDes.getPlayers().getPlayer().get(index).getName();
        id = gameDes.getPlayers().getPlayer().get(index).getId().intValue();
        if (gameDes.getGame().getInitialFunds().intValue()>=0)
            totalTuring.set(gameDes.getGame().getInitialFunds().intValue());
        else
            totalTuring.set(1000);
        tempTotalTuring=totalTuring.get();
        conquered = new ArrayList<>(10);
        casualties = new ArrayList<>(10);
        notifications = new ArrayList<>(10);
    }

    public double[] getColor() {
        return color;
    }

    public Player(Player player)
    {
        this.sign = player.sign;
        this.name = player.name;
        this.totalTuring=player.getTotalTuringProperty();
        this.id = player.id;
        casualties = new ArrayList<>(10);
        conquered = new ArrayList<>(10);
        notifications = new ArrayList<>(10);
        this.tempTotalTuring=player.tempTotalTuring;
        color = new double[3];
        for(int i=0;i<color.length;i++)
            color[i]=player.color[i];
    }

    public void chooseColor(int index)
    {
        switch (index)
        {
            case 0:
            {
                color[0]=(1.0);
                color[1]=(1.0);
                color[2]= (0.6);
                break;
            }
            case 1:
            {
                color[0]=(0.4);
                color[1]=(1.0);
                color[2]= (0.7);
                break;
            }
            case 2:
            {
                color[0]=1.0;
                color[1]=0.4;
                color[2]=0.4;
                break;
            }
            case 3:
            {
                color[0]=0.8;
                color[1]=0.4;
                color[2]=1.0;
                break;
            }
        }
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public char getSign() {
        return sign;
    }

    public String getName() {
        return name;
    }

    public int getTotalTuring() {
        return totalTuring.intValue();
    }

    public SimpleIntegerProperty getTotalTuringProperty(){return totalTuring;}

    public void setTotalTuring(int totalTuring) {
        if (this.totalTuring==null)
            this.totalTuring = new SimpleIntegerProperty();
        this.totalTuring.set(totalTuring);
        tempTotalTuring=totalTuring;
    }

    public int getTempTotalTuring() {
        return tempTotalTuring;
    }

    public int getConqueredSize() {
        return conquered.size();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name + "'s stats\n" +
                "Turing: " + totalTuring + "\n" +
                "Conquered Territories: ";
    }

    public List<Slot> getConquered() {
        return conquered;
    }

    public List<String> getCasualties() {
        return casualties;
    }

    public int totalValOfTerritories() {
        int sum = 0;
        for (int i = 0; i < conquered.size(); i++)
            sum = sum + conquered.get(i).roundRewards;
        return sum;
    }

    public void addRewards()
    {
        setTotalTuring(totalValOfTerritories()+getTotalTuring());
    }

     public List<Slot> calcFatiguePerPlayer(SimpleIntegerProperty[]unitsOnboard, List<Unit> model)
     {
        List<Slot> deadAndBuried = new ArrayList<>(10);
        boolean marker;
        int k;
        for(int i=0;i<conquered.size();i++)
        {
            Slot s= conquered.get(i);
            int sum=0;
            int[] flags = new int[model.size()];
            for(int r=0;r<flags.length;r++)
                flags[r]=0;
            String notify = "The units: ";
            for (int j=0; j<s.occupyingArmy.size(); j++) {
                marker=false;
                s.occupyingArmy.get(j).HP -= s.occupyingArmy.get(j).fatigueFactorMulti;
                int num= s.occupyingArmy.get(j).getMaximumMightMulti()/s.occupyingArmy.get(j).getMaximumMight();
                for(k=0; k<model.size()&&!marker; k++) {
                    if (model.get(k).getRank() == s.occupyingArmy.get(j).rank) {
                        flags[k] = num;
                        marker = true;
                    }
                }
                k--;
                if (s.occupyingArmy.get(j).HP<=0) {
                    String str = "";
                    str = str.concat(conquered.get(i).serialNumber.toString());
                    str = str.concat(" : ");
                    if (!notify.equals("The units: "))
                        notify = notify.concat(", ");
                    notify = notify.concat(s.occupyingArmy.get(j).type);
                    str = str.concat(s.occupyingArmy.remove(j).type);
                    casualties.add(str);
                    unitsOnboard[k].set(unitsOnboard[k].get() - num);
                    flags[k] = 0;
                    j--; //check if subtracted twice!
                }
                else
                    sum+=s.occupyingArmy.get(j).HP;
            }
            if (!notify.equals("The units: "))
            {
                notify= notify.concat("\n");
                notify=notify.concat("in Territory "+conquered.get(i).serialNumber.toString() + "are dead\n");
                notify = notify.concat("Since you're such a great warlord");
                notifications.add(notify);
            }
            if (sum<conquered.get(i).minimumMight)
            {
                for(int r=0;r<flags.length;r++)
                {
                    if(flags[r]>0)
                        unitsOnboard[r].set(unitsOnboard[r].get() - flags[r]);
                }
                deadAndBuried.add(conquered.remove(i));
                i--;
            }
        }
        return deadAndBuried;
     }

     public void addConquered(Slot slot)
     {
         conquered.add(slot);
     }


     public void removeFromConquered(Slot slot)
     {
         conquered.remove(slot);
     }


}
