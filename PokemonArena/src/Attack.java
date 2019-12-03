/*
-Attack.java
-Jana Jandal Alrifai
-Here the Attack object is stored and it includes its name,cost,damage and special attack
*/
public class Attack {
    //public fields are chosen insteead of private since its a simple programs and there are less things to change
    public String name;
    public int cost;
    public float damage;
    public String special;
    public Attack(String name,int cost,float damage, String special){
        this.name=name;
        this.cost=cost;
        this.damage=damage;
        this.special=special;
        if(special.equals(" ")){ //when displayed its is not left blank but displayed as N/A
            this.special="N/A";
        }
    }
    public void display(){ //prints out fields in the attack class in Arena to allow the user to choose an attack

        System.out.println("╔═══════════════════════════════╗");
        System.out.printf ("| Name:%-11s Cost:%-7d |\n",name,cost);
        System.out.printf ("| Special:%-8s Damage:%.2f |\n",special,damage );
        System.out.println("╚═══════════════════════════════╝");
    }
}