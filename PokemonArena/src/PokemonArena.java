/*
-PokemonArena.java
-Jana Jandal Alrifai
-Here is where  the battles happen, here starting turns are randomly chosen and each round player and Computer get to
have one move.
Some Methods Include:
    -screen/load-draws the Pokemon title and loads the pokemon from file
    -choose/Battleprep-allow the user to choose the good guys and which good Poke they will be using-each pokemon gets 50
    energy restored
    -ComputerTurn and PlayerTurn-where player can pick what do they wanna do in their turn and the move from computer's
    are randomized
*/
import java.util.*;
import java.io.*;

public class PokemonArena {
    private static ArrayList<Pokemon> gdPokes = new ArrayList<Pokemon>();
    private static ArrayList<Pokemon> badPokes = new ArrayList<Pokemon>();
    private static Pokemon computer;
    private static Pokemon player;
    public static Scanner kb= new Scanner(System.in);
    public static Random random = new Random();

    public static void main(String[] args) {
        screen();
        load();
        choose();
        battlePrep();
        while(computer.getHp()>0 && gdPokes.size()>0){
            battle();
        }
    }
    private static void screen(){ //load screen
        System.out.println("                                  ,'\\\n" +
                "    _.----.        ____         ,'  _\\   ___    ___     ____\n" +
                "_,-'       `.     |    |  /`.   \\,-'    |   \\  /   |   |    \\  |`.\n" +
                "\\      __    \\    '-.  | /   `.  ___    |    \\/    |   '-.   \\ |  |\n" +
                " \\.    \\ \\   |  __  |  |/    ,','_  `.  |          | __  |    \\|  |\n" +
                "   \\    \\/   /,' _`.|      ,' / / / /   |          ,' _`.|     |  |\n" +
                "    \\     ,-'/  /   \\    ,'   | \\/ / ,`.|         /  /   \\  |     |\n" +
                "     \\    \\ |   \\_/  |   `-.  \\    `'  /|  |    ||   \\_/  | |\\    |\n" +
                "      \\    \\ \\      /       `-.`.___,-' |  |\\  /| \\      /  | |   |\n" +
                "       \\    \\ `.__,'|  |`-._    `|      |__| \\/ |  `.__,'|  | |   |\n" +
                "        \\_.-'       |__|    `-._ |              '-.|     '-.| |   |\n" +
                "                                `'                            '-._|");
        System.out.println();
    }
    public static String startTurn(){ //chooses who starts turn
        int num = random.nextInt(1);
        if (num==0){
            return "Player";
        }
        return "Computer";
    }
    private static void load() { //loads file and catches if file is not there
        try {
            Scanner file = new Scanner(new File("pokemon.txt"));
            int n = Integer.parseInt(file.nextLine());
            for (int i = 0; i < n; i++) {
                String line = file.nextLine();
                badPokes.add(new Pokemon(line));
            }

            file.close();
        }
        catch (IOException ex) {
            System.out.println("oopsie,it seems like that file either doesnt exist or is in the wrong place");
        }
    }
    public static void displayAll(){ //displays all pokemon to allow the user to choose from them
        int colcount=0;
        System.out.println("Choose a pokemon to add to your team by typing in its number:");
        for(int i=0;i<badPokes.size();i++){
            if(colcount==4){
                colcount=0;
                System.out.println(" ");
            }
            Pokemon poke=badPokes.get(i);
            System.out.print((i+1)+"."+poke.getName()+" ");
            colcount++;
        }
        System.out.println(" ");
    }
    private static int getNum(int usernum, int min, int max) { //makes sure no input is out of bounds
        if (usernum >= min && usernum <= max) {
            return usernum;
        } else {
            System.out.println("Please pick a valid number");
            usernum = kb.nextInt();
            while (!(usernum >= min && usernum <= max)) { //keeps looping until a number in range is entered
                System.out.println("Please pick a valid number");
                usernum = kb.nextInt();
            }
            return usernum;
        }
    }

    public static void choose(){ //allows the user to choose pokemon to add to their team
        while(gdPokes.size()<4){
            displayAll();
            int choice=getNum(kb.nextInt(),1,badPokes.size());
            Pokemon poke=badPokes.get(choice-1);
            gdPokes.add(poke);
            poke.display();
            badPokes.remove(choice-1);
        }
    }

    public static void battlePrep(){
        //randomly chooses a bad pokemon and sets energy at 50
        int num = random.nextInt(badPokes.size());
        computer=badPokes.get(num);
        badPokes.remove(computer);
        computer.setEnergy(50); //sets energy to 50 at start of battle
        System.out.println("----------------------");
        System.out.println("Lets Start the Battles");
        System.out.println("----------------------");
        System.out.println("Choose your fighter this round:");
        goodChoose(); //allows the user to choose a fighter for this battle
        for(int j=0;j<gdPokes.size();j++){
            gdPokes.get(j).setEnergy(50); //sets all good pokemons' energy to 50
        }
        System.out.println(player.getName()+",I choose you");
        System.out.println();
        System.out.println("Computer enemy found! "+computer.getName());
        System.out.println();

    }
    public static void goodChoose(){ //loops through all the good pokes
        for(int i=0;i<gdPokes.size();i++){
            Pokemon poke=gdPokes.get(i);
            System.out.println((i+1)+"."+poke.getName());
        }
        int chosen=getNum(kb.nextInt(),1,gdPokes.size());
        player=gdPokes.get(chosen-1);
        player.display();
    }

    public static void recharge() { //adds 10 to each pokemon's energy after each round
        for(int j=0;j<gdPokes.size();j++){
            Pokemon poke=gdPokes.get(j);
            poke.setEnergy(Math.min(50,poke.getEnergy()+10));
        }
        for(int i=0;i<badPokes.size();i++){
            Pokemon poke=badPokes.get(i);
            poke.setEnergy(Math.min(50,poke.getEnergy()+10));
        }
    }
    public static void battle(){
        String turn = startTurn();
        while(computer.getHp()>0 && gdPokes.size()>0){ //keeps looping until enemy looses or good pokemons all die
            recharge();
            if(turn=="Computer"){
                computerTurn(); //makes computer make a move during their turn
                turn = "Player";
            }
            else{
                playerTurn(); //make player move during their turn
                turn = "Computer";
            }
        }

        //checks that no team has lost yet
        computerStatus();
        playerStatus();
        for(int j=0;j<gdPokes.size();j++){
            Pokemon poke=gdPokes.get(j);
            poke.setHp(poke.getHp()+20);
        }

    }
    public static void playerStatus(){ //checks that if player died and if they are no more good pokemons
        if(player.getHp()<=0){
            gdPokes.remove(player);
            if(gdPokes.size()==0){
                System.out.println("Computer Pokemons have won, COMPUTER is TRAINER SUPREME");
                return;
            }
            else{
                System.out.println("Your Pokemon has died,pick a new pokemon"); //allows the user to pick a new pokemon
                goodChoose();
            }
        }
    }
    public static void computerStatus(){ //sees if current enemy is dead and if all his team died
        if(computer.getHp()<=0){
            badPokes.remove(computer);
            System.out.println(computer.getName()+" lost this round");

            if(badPokes.size()==0){
                System.out.println("Good Pokemons have won, PLAYER is TRAINER SUPREME");
                return;
            }
            battlePrep();
        }
    }
    public static ArrayList<Attack> posAttacks(Pokemon poke){ //sees what attacks can a certain pokemon afford
        ArrayList<Attack>retAttacks= new ArrayList<>();
        for(int i=0;i<poke.attacklist.size();i++){
            Attack curAttack=poke.attacklist.get(i);
            if(curAttack.cost<poke.getEnergy()){
                retAttacks.add(curAttack);
            }
        }
        return retAttacks;
    }
    public static void choice1(Pokemon poke){ //displays attacks the player could afford and allows the user to attack
        System.out.println("Enter your chosen attack's number:");
        ArrayList<Attack>possibleAttack=posAttacks(poke);
        for(int i=0;i<possibleAttack.size();i++){
            System.out.println((i+1)+".");
            possibleAttack.get(i).display();
        }
        int attchoi=getNum(kb.nextInt(),1,possibleAttack.size());
        player.attack(computer,player.attacklist.get(attchoi-1));
    }

    public static void playerTurn(){
        int range=1; //is used when checking if number is range and changes if the player cannot attack
        if(player.getStun()){
            System.out.println(player.getName()+" is stunned, they can't do anything"); //doesnt allow the user to attack when stunned
            player.setStun(false);
        }
        else{
            if(player.getHp()>0){ // if player is alive he could do a move
                System.out.println("What's your next move?");
                if(!(posAttacks(player).isEmpty())){
                    System.out.println("1. Attack");

                }
                else{
                     range=2;
                }
                System.out.println("2. Retreat");
                System.out.println("3. Pass");
                int action=getNum(kb.nextInt(),range,3);
                if(action==1){
                    choice1(player);
                }
                else if(action==2){ //retreats the pokemon and allow the user to choose another
                    System.out.println(player.getName()+" has retreated");
                    System.out.println("Choose another pokemon");
                    goodChoose();
                }
                else if(action==3){
                    System.out.println(player.getName()+" has passed their turn");
                }
            }
            else{
                playerStatus(); //checks if the player is still alive
            }
        }
    }

    public static void computerTurn(){
        if(computer.getStun()){ //if computer is stunned he cant do anything
            System.out.println(computer.getName()+" is stunned, they can't do anything");
            computer.setStun(false);
        }
        else{
            ArrayList<Attack>cattack= posAttacks(computer); //checks what the computer could do

            if(cattack.isEmpty()){
                System.out.println(computer.getName()+" has passed their turn"); //if computer cannot do anything, turn is passed
            }
            else{ //randomly chooses an attack from the list
                int randint= random.nextInt(cattack.size());
                Attack chosenAttack=cattack.get(randint);
                computer.attack(player,chosenAttack);
            }
        }
    }
}

