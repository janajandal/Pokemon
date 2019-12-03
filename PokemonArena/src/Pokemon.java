/*
-Pokemon.java
-Jana Jandal Alrifai
-Here is the Pokemon class that includes private fields entered from file. There are also Getter and Setter methods,
attack methods that take care of damage done in case of attack, and display method that shows all the pokemon information
*/
import java.util.*;
public class Pokemon{
    //fields used and are important to Pokemon
    public ArrayList<Attack> attacklist= new ArrayList<Attack>();
    private String name;
    private int hp;
    private int energy;
    private boolean stun;
    private String type;
    private String resistance;
    private String state;
    private String weakness;
    public Pokemon(String line) {
        String [] stats = line.split(","); //splits line from file
        name = stats[0];
        hp = Integer.parseInt(stats[1]);
        energy=50;
        type=stats[2];
        resistance = stats[3];
        weakness = stats[4];
        stun=false; //boolean that can be true or false
        state="alive"; //can be "disabled" "dead" or "alive"
        int p=6; //pass through from start of Attack descriptions
        for(int i=0; i<Integer.parseInt(stats[5]);i+=4){ //loops the number of attacks present
            attacklist.add(new Attack(stats[p],Integer.parseInt(stats[p+1]),Float.parseFloat(stats[p+2]),stats[p+3])); //adds the attacks to a list for Pokemons
            p+=4; //increase by 4 to bypass the description of the past attack
        }
    }
    public static void main(String[]args){
    }

    public static boolean coin(){
        //decides if something is true or not
        Random random = new Random();
        return random.nextBoolean(); //used to determine if pokemon gets stunned when attacked
    }
    public void attack(Pokemon enemy,Attack selectedAttack){ //deals with all scenarios in an attack
        float curdamage=selectedAttack.damage; //local variable that is affected by stun,state and resistance
        if(state=="disabled"){
            curdamage=Math.min(0,selectedAttack.damage-10);//subtracts damage out of supposed attack damage to min of 0
        }
        if(enemy.resistance==type){
            curdamage*=0.5; //if enemy pokemon is resistant to you then you are attack damage is decreased by 0.5
        }
        if(enemy.weakness==type){
            curdamage*=2; //if enemy is weak to your type then damage is increased by 2
        }
        energy-=selectedAttack.cost; //takes cost of attack out of player energy
        System.out.println(name+" used "+selectedAttack.name); //prints out attack used
        if(selectedAttack.special.equals("N/A")){
            enemy.hp-=curdamage; //subtracts damage from pokemon if there is no special
        }

        if(selectedAttack.special.equals("stun")){
            enemy.hp-=curdamage;
            if(coin()){ //determines if enemy gets stunned
                System.out.println(enemy.name+" is stunned");
                enemy.stun=true;
            }
        }
        else if(selectedAttack.special.equals("wild card")){
            if (coin()){ //determined if wild card works or not
                enemy.hp-=curdamage;
                System.out.println("Wild Card has worked!");
            }
            else{
                System.out.println("Wild Card has failed,better luck next time");
            }


        }
        else if(selectedAttack.special.equals("wild storm")){
            while(coin()){ //wild storm will keep damaging until enemy dies or coin return false
                System.out.println(name+" activated wild storm");
                enemy.hp-=curdamage;
                if(enemy.hp<=0){
                    return;
                }
            }
            System.out.println(name+"'s storm finally quited down");

        }
        else if(selectedAttack.special.equals("disable")){
            enemy.hp-=curdamage;
            enemy.state="disabled"; //disables pokemon
            System.out.println(enemy.name+" is now disabled");
        }
        else if(selectedAttack.special.equals("recharge")){ //recharges player pokemon for a maximum of 50
            enemy.hp-=curdamage;
            Math.min(50,energy+50);
        }

        System.out.println(enemy.name+" lost "+curdamage);
        System.out.println("Stats:");
        enemy.display();
        display();
    }
    public void display(){
        //displays all Pokemon information in nice way
        System.out.println("╔════════════════════════════════╗");
        System.out.printf ("| Name:%-10s Type:%-9s |\n",name,type);
        System.out.printf ("| Energy:%-8d HP:%-11d |\n",energy,hp );
        System.out.println("╚════════════════════════════════╝");
    }
    //getters and setters
    public String getName(){
        return this.name;
    }
    public void setEnergy(int energy){
        this.energy=energy;
    }
    public boolean getStun(){
        return this.stun;
    }
    public void setStun(boolean stun){
        this.stun=stun;
    }
    public int getHp(){
        return this.hp;
    }
    public void setHp(int hp){
        this.hp=hp;
    }
    public int getEnergy(){
        return this.energy;
    }
}