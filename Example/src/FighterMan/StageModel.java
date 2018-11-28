package FighterMan;

import javafx.scene.control.Cell;

import java.util.ArrayList;
import java.util.Random;

/**
 * Model of the Stage and characters within it
 */
public class StageModel  {

    public enum CellValue {
        EMPTY, USER, ENEMY, ATTACK, LONGATTACK
    };
    private CellValue[][] cells;
    private ArrayList<Player> combatants= new ArrayList<Player>();
    private int userRow;
    private int userColumn;
    private int enemyRow;
    private int enemyColumn;

    private boolean hit;
    private boolean eHit;
    private boolean attack;
    private boolean eAttack;
    private boolean displayAttack;
    private boolean displayLongAttack;

    private int turn;
    private int actionCredit;
    private boolean insufficientCredits;

    private boolean gameOver;
    private boolean endedTurn;

    public StageModel(int rowCount, int columnCount){
        assert rowCount > 0 && columnCount > 0;
        this.cells = new CellValue[rowCount][columnCount];
        this.startNewGame();
    }
    /**
     * Starts a game for the user
     */
    public void startNewGame(){
        this.gameOver = false;
        this.initialize();
    }

    /**
     * Sets up the stage
     */
    private void initialize(){

        int rowCount = this.cells.length;
        int columnCount = this.cells[0].length;

        // Empty all the cells
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                this.cells[row][column] = CellValue.EMPTY;
            }
        }

        this.combatants.clear();
        Player user = new Player();
        this.actionCredit = 10;
        this.turn = 0;
        this.userRow = 5;
        this.userColumn = 3;
        this.cells[this.userRow][this.userColumn] = CellValue.USER;
        combatants.add(user);

        Player enemy = new Player();
        this.enemyRow = 5;
        this.enemyColumn = 6;
        this.cells[this.enemyRow][this.enemyColumn] = CellValue.ENEMY;
        combatants.add(enemy);
    }

    /**
     * Moves character to a different position
     * @param  rowChange
     * @param  columnChange
     */
    private void movePlayer(int rowChange, int columnChange) {
        if (isTurnOver() == false) {

            int newRow = this.userRow + rowChange;
            if (newRow < 4) {
                newRow = 4;
            }
            if (newRow > 6) {
                newRow = 6;
            }

            int newColumn = this.userColumn + columnChange;
            if (newColumn < 2) {
                newColumn = 2;
            }
            if (newColumn > 4) {
                newColumn = 4;
            }


            this.cells[this.userRow][this.userColumn] = CellValue.EMPTY;
            if (!(this.userRow == newRow && this.userColumn == newColumn)) {
                this.actionCredit--;
            }
            this.userRow = newRow;
            this.userColumn = newColumn;
            this.cells[this.userRow][this.userColumn] = CellValue.USER;
        }
    }
    /**
     * Moves Enemy to a different position
     * @param  rowChange
     * @param  columnChange
     */
    private void moveEnemy(int rowChange, int columnChange) {
        if (isTurnOver() == false) {

            int newRow = this.enemyRow + rowChange;
            if (newRow < 4) {
                newRow = 4;
            }
            if (newRow > 6) {
                newRow = 6;
            }

            int newColumn = this.enemyColumn + columnChange;
            if (newColumn < 5) {
                newColumn = 5;
            }
            if (newColumn > 7) {
                newColumn = 7;
            }


            this.cells[this.enemyRow][this.enemyColumn] = CellValue.EMPTY;
            if (!(this.enemyRow == newRow && this.enemyColumn == newColumn)) {
                this.actionCredit--;
            }
            this.enemyRow = newRow;
            this.enemyColumn = newColumn;
            this.cells[this.enemyRow][this.enemyColumn] = CellValue.ENEMY;
        }
    }
    /**
     * decides who's turn and it is and runs the move method for that character
     * @param  row
     * @param  column
     */
    public void moveCharacter(int row, int column){
        if(!isWinner()){
            if(this.turn == 0){
                movePlayer(row, column);
            }
            else if(this.turn == 1){
                moveEnemy(row, column);
            }
        }
    }
    /**
     * determines if turn is over
     */
    public boolean isTurnOver() {
        if (this.gameOver || this.actionCredit == 0 || this.endedTurn) {
            if(this.endedTurn){
                this.endedTurn = false;
            }
            else if (this.actionCredit == 0){
                this.endTurn();
            }
            this.actionCredit = 10;
            return true;
        }
        return false;
    }
    /**
     * Makes player attack
     */
    private void playerAttack() {
        if (isTurnOver() == false && !this.combatants.get(0).isDead()) {
            int player = this.turn;
            Player attacker = this.combatants.get(player);
            if ((this.actionCredit - 3) > -1) {
                int damage = attacker.attack();
                this.attack = true;
                int range = attacker.getAttackRange();
                int receiver = this.userColumn + range;
                CellValue locationHit = getCellValue(this.userRow, receiver);
                CellValue attackDisplay = getCellValue(this.userRow, receiver - 1);
                if (attackDisplay == CellValue.EMPTY) {
                    this.cells[this.userRow][receiver - 1] = CellValue.ATTACK;
                    this.displayAttack = true;
                }
                if (locationHit == CellValue.ENEMY) {
                    this.eHit = true;
                    this.combatants.get(1).takeDamage(damage);
                }
                this.actionCredit = this.actionCredit - 3;
            } else {
                this.insufficientCredits = true;
            }
        }
    }
    /**
     * Makes enemy attack
     */
    private void enemyAttack() {
        if (isTurnOver() == false && !this.combatants.get(1).isDead()) {
            int player = this.turn;
            Player attacker = this.combatants.get(player);
            if ((this.actionCredit - 3) > -1) {
                int damage = attacker.attack();
                this.eAttack = true;
                int range = -attacker.getAttackRange();
                int receiver = this.enemyColumn + range;
                CellValue locationHit = getCellValue(this.enemyRow, receiver);
                CellValue attackDisplay = getCellValue(this.enemyRow, receiver + 1);
                if (attackDisplay == CellValue.EMPTY) {
                    this.cells[this.enemyRow][receiver + 1] = CellValue.ATTACK;
                    this.displayAttack = true;
                }
                if (locationHit == CellValue.USER) {
                    this.hit = true;
                    this.combatants.get(0).takeDamage(damage);
                }
                this.actionCredit = this.actionCredit - 3;
            } else {
                this.insufficientCredits = true;
            }
        }
    }
    /**
     * Makes player use special attack
     */
    private void playerSpAttack() {
        if (isTurnOver() == false && !this.combatants.get(0).isDead()) {
            int player = this.turn;
            Player attacker = this.combatants.get(player);
            if ((this.actionCredit - 5) > -1) {
                int damage = attacker.spAttack();
                this.attack = true;
                int range = attacker.getSpAttackRange();
                int receiver = this.userColumn + range;
                CellValue locationHit = getCellValue(this.userRow, receiver);
                if (locationHit == CellValue.ENEMY) {
                    this.combatants.get(1).takeDamage(damage);
                    this.eHit = true;
                }
                this.actionCredit = this.actionCredit - 5;
            } else {
                this.insufficientCredits = true;
            }
        }
    }
    /**
     * makes enemy use special attack
     */
    private void enemySpAttack() {
        if (isTurnOver() == false && !this.combatants.get(1).isDead()) {
            int player = this.turn;
            Player attacker = this.combatants.get(player);
            if ((this.actionCredit - 5) > -1) {
                this.eAttack = true;
                int damage = attacker.spAttack();
                int range = -attacker.getSpAttackRange();
                int receiver = this.enemyColumn + range;
                CellValue locationHit = getCellValue(this.enemyRow, receiver);
                if (locationHit == CellValue.USER) {
                    this.hit = true;
                    this.combatants.get(0).takeDamage(damage);
                }
                this.actionCredit = this.actionCredit - 5;
            } else {
                this.insufficientCredits = true;
            }
        }
    }
    /**
     * makes player use long attack
     */
    private void playerLongAttack() {
        if (isTurnOver() == false && !this.combatants.get(0).isDead()) {
            int player = this.turn;
            Player attacker = this.combatants.get(player);
            if ((this.actionCredit - 2) > -1) {
                int damage = attacker.longAttack();
                this.attack = true;
                int range = attacker.getLongAttackRange();
                int receiver = this.userColumn + range;
                CellValue locationHit = getCellValue(this.userRow, receiver);
                CellValue attackDisplay = getCellValue(this.userRow, receiver - 1);
                if (attackDisplay == CellValue.EMPTY) {
                    this.cells[this.userRow][receiver - 1] = CellValue.LONGATTACK;
                    this.displayLongAttack = true;
                }
                if (locationHit == CellValue.ENEMY) {
                    this.eHit = true;
                    this.combatants.get(1).takeDamage(damage);
                }
                this.actionCredit = this.actionCredit - 2;
            } else {
                this.insufficientCredits = true;
            }
        }
    }
    /**
     * makes enemy use long attack
     */
    private void enemyLongAttack() {
        if (isTurnOver() == false && !this.combatants.get(1).isDead()) {
            int player = this.turn;
            Player attacker = this.combatants.get(player);
            if ((this.actionCredit - 2) > -1) {
                int damage = attacker.longAttack();
                this.eAttack = true;
                int range = -attacker.getLongAttackRange();
                int receiver = this.enemyColumn + range;
                CellValue locationHit = getCellValue(this.enemyRow, receiver);
                CellValue attackDisplay = getCellValue(this.enemyRow, receiver + 1);
                if (attackDisplay == CellValue.EMPTY) {
                    this.cells[this.enemyRow][receiver + 1] = CellValue.LONGATTACK;
                    this.displayLongAttack = true;
                }
                if (locationHit == CellValue.USER) {
                    this.hit = true;
                    this.combatants.get(0).takeDamage(damage);
                }
                this.actionCredit = this.actionCredit - 2;
            } else {
                this.insufficientCredits = true;
            }
        }
    }
    /**
     * determines who used attack
     */
    public void attack(){

        if(this.turn == 0){
            playerAttack();
        }
        else if(this.turn == 1){
            enemyAttack();
        }
    }
    /**
     * determines who used special attack
     */
    public void spAttack(){
        if(this.turn == 0){
            playerSpAttack();
        }
        else if(this.turn == 1){
            enemySpAttack();
        }
    }
    /**
     * determines who used long attack
     */
    public void longAttack(){
        if(this.turn == 0){
            playerLongAttack();
        }
        else if(this.turn == 1){
            enemyLongAttack();
        }
    }

    /**
     * determines winner
     */
    public boolean isWinner(){
        if(this.combatants.get(0).isDead() || this.combatants.get(1).isDead()){
            return true;
        }
        else{
            return false;
        }
    }
    public int getRowCount() {
        return this.cells.length;
    }

    public int getColumnCount() {
        assert this.cells.length > 0;
        return this.cells[0].length;
    }

    public CellValue getCellValue(int row, int column) {
        assert row >= 0 && row < this.cells.length && column >= 0 && column < this.cells[0].length;
        return this.cells[row][column];
    }

    public int getPlayerHP(){
        return this.combatants.get(0).getHP();
    }

    public int getEnemyHP(){
        return this.combatants.get(1).getHP();
    }

    public int getPlayerActionCredits() {
        return this.actionCredit;
    }
    public boolean getInsufficientCredits() {
        if (this.insufficientCredits == true) {
            this.insufficientCredits = false;
            return true;
        } else {
            return false;
        }
    }
    public int getTurn(){
        return this.turn;
    }
    /**
     * ends the turn
     */
    public boolean endTurn(){
        this.endedTurn = true;
        this.turn++;
        if(this.turn > this.combatants.size()-1){
            this.turn = 0;
        }
        this.actionCredit = 10;
        return this.endedTurn;
    }
    public boolean isAttack() {
        if(this.attack) {
            this.attack = false;
            return true;
        }
        return false;
    }

    public boolean isEnemyAttack() {
        if(this.eAttack) {
            this.eAttack = false;
            return true;
        }
        return false;
    }

    public boolean isHit(){
        if(this.hit){
            this.hit = false;
            return true;
        }
        return false;
    }
    public boolean isEnemyHit(){
        if(this.eHit){
            this.eHit = false;
            return true;
        }
        return false;
    }
    public boolean isDisplayAttack() {
        if (this.displayAttack) {
            this.displayAttack = false;
            return true;
        }
        return false;
    }
    public boolean isLongAttack(){
        if(this.displayLongAttack){
            this.displayLongAttack = false;
            return true;
        }
        return false;
    }
    public void setEmpty(int row, int column) {
        this.cells[row][column] = CellValue.EMPTY;
    }

}
