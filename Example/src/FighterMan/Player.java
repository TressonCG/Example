package FighterMan;

import java.util.*;
import java.util.Random;
/**
 * A player/character that will perform actions on the stage
 */
public class Player implements PlayerInterface{


    private int HP  = 100;
    private int longAttackDamage = 10;
    private int attackDamage = 20;
    private int spAttackDamage = 30;
    private int longAttackRange = 3;
    private int attackRange = 2;
    private int spAttackRange = 1;
    private boolean dead = false;

    public int attack(){
        return this.attackDamage;
    }

    public int getAttackRange(){
        return this.attackRange;
    }

    public int spAttack(){
        return this.spAttackDamage;
    }

    public int getSpAttackRange(){
        return this.spAttackRange;
    }

    public int longAttack(){
        return this.longAttackDamage;
    }

    public int getLongAttackRange(){
        return this.longAttackRange;
    }

    public int getHP(){
        return this.HP;
    }
    /**
     * Reduces the HP of a character who has taken damage
     * updates the instance variable
     * @param  damage
     */
    public void takeDamage(int damage){
        this.HP = this.HP - damage;
        if(this.HP <= 0){
            this.dead = true;
            this.HP = 0;
        }
    }

    public boolean isDead(){
        return this.dead;
    }
}
