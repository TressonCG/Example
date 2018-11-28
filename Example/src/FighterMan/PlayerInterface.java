package FighterMan;

public interface PlayerInterface{

    public int attack();
    public int getAttackRange();
    public int spAttack();
    public int getSpAttackRange();
    public void takeDamage(int damage);
    public boolean isDead();
}
