package FighterMan;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A class that creates the view for the game
 */


public class View extends Group {

    public final static double CELL_WIDTH = 50.0;
    @FXML private int rowCount;
    @FXML private int columnCount;
    private ImageView[][] cellViews;
    private Image user;
    private Image enemy;
    private Image attack;
    private Image lAttack;

    /**
     * retrieves the images we created for the view of our characters
     */
    public View() {
        this.user = new Image(getClass().getResourceAsStream("/res/Boxer.png"));
        this.enemy = new Image(getClass().getResourceAsStream("/res/BoxerE.png"));
        this.attack = new Image(getClass().getResourceAsStream("/res/BoxerATK.png"));
        this.lAttack = new Image(getClass().getResourceAsStream("/res/BoxerATKL.png"));
    }
    public int getRowCount() {
        return this.rowCount;
    }
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.initializeGrid();
    }
    public int getColumnCount() {
        return this.columnCount;
    }
    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        this.initializeGrid();
    }

    /**
     * initializes stage graphic
     */
    private void initializeGrid() {
        if (this.rowCount > 0 && this.columnCount > 0) {
            this.cellViews = new ImageView[this.rowCount][this.columnCount];
            for (int row = 0; row < this.rowCount; row++) {
                for (int column = 0; column < this.columnCount; column++) {
                    ImageView imageView = new ImageView();
                    imageView.setX((double)column * CELL_WIDTH);
                    imageView.setY((double)row * CELL_WIDTH);
                    imageView.setFitWidth(CELL_WIDTH);
                    imageView.setFitHeight(CELL_WIDTH);
                    this.cellViews[row][column] = imageView;
                    this.getChildren().add(imageView);

                }
            }
        }

    }
    /**
     * Updates graphical changes of stage
     */
    public void updateStage(StageModel model) {
        assert model.getRowCount() == this.rowCount && model.getColumnCount() == this.columnCount;
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                StageModel.CellValue cellValue = model.getCellValue(row, column);
                /**
                 * Displays the version of player 1 in reference to the health as well as the special attack and if the
                 * player is hit
                 */
                if (cellValue == StageModel.CellValue.USER) {
                    if (model.isAttack()) {
                        if (model.getTurn() == 0) {
                            Image normalAttack = new Image(getClass().getResourceAsStream("/res/BoxerPunch.png"));
                            this.cellViews[row][column].setImage(normalAttack);
                        }
                    } else if (model.isHit()) {
                        Image hit = new Image(getClass().getResourceAsStream("/res/BoxerHurt.png"));
                        this.cellViews[row][column].setImage(hit);
                    } else {
                        if (model.getPlayerHP() > 0 && model.getPlayerHP() <= 50) {
                            Image bruised = new Image(getClass().getResourceAsStream("/res/BoxerBruised.png"));
                            this.cellViews[row][column].setImage(bruised);
                        } else if (model.getPlayerHP() == 0) {
                            Image dead = new Image(getClass().getResourceAsStream("/res/BoxerDead.png"));
                            this.cellViews[row][column].setImage(dead);
                        } else {
                            this.cellViews[row][column].setImage(this.user);
                        }

                    }

                } /**
                 * Displays the version of player 2 in reference to the health as well as the sp attack and if the player
                 * is hit
                 */
                else if (cellValue == StageModel.CellValue.ENEMY) {
                    if (model.isEnemyAttack()) {
                        if (model.getTurn() == 1) {
                            Image eNormalAttack = new Image(getClass().getResourceAsStream("/res/BoxerPunchE.png"));
                            this.cellViews[row][column].setImage(eNormalAttack);
                        }
                    } else if (model.isEnemyHit()) {
                        Image eHit = new Image(getClass().getResourceAsStream("/res/BoxerHurtE.png"));
                        this.cellViews[row][column].setImage(eHit);
                    } else {
                        if (model.getEnemyHP() > 0 && model.getEnemyHP() <= 50) {
                            Image bruised = new Image(getClass().getResourceAsStream("/res/BoxerBruisedE.png"));
                            this.cellViews[row][column].setImage(bruised);
                        } else if (model.getEnemyHP() == 0) {
                            Image dead = new Image(getClass().getResourceAsStream("/res/BoxerDeadE.png"));
                            this.cellViews[row][column].setImage(dead);
                        } else {
                            this.cellViews[row][column].setImage(this.enemy);
                        }
                    }
                }
                /**
                 * Displays the regular attack depending on whose turn
                 */
                else if (cellValue == StageModel.CellValue.ATTACK) {
                    if (model.isDisplayAttack()) {
                        if (model.getTurn() == 0) {
                            this.cellViews[row][column].setImage(this.attack);
                            model.setEmpty(row, column);
                        } else {
                            Image eAttack = new Image(getClass().getResourceAsStream("/res/BoxerATKE.png"));
                            this.cellViews[row][column].setImage(eAttack);
                            model.setEmpty(row, column);
                        }

                    }

                }
                /**
                 * Displays the long range attack depending on whose turn
                 */
                else if (cellValue == StageModel.CellValue.LONGATTACK) {
                    if (model.isLongAttack()) {
                        if (model.getTurn() == 0) {
                            this.cellViews[row][column].setImage(this.lAttack);
                            model.setEmpty(row, column);
                        } else {
                            Image eAttack = new Image(getClass().getResourceAsStream("/res/BoxerATKLE.png"));
                            this.cellViews[row][column].setImage(eAttack);
                            model.setEmpty(row, column);
                        }
                    }
                } else {
                    this.cellViews[row][column].setImage(null);
                }
            }
        }
    }
}
