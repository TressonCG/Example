package FighterMan;

import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controller implements EventHandler<KeyEvent> {

    @FXML private View view;
    @FXML private ImageView boardImageView;
    @FXML private Label scoreLabel;
    @FXML private Label messageLabel;
    @FXML private Label alertLabel;
    private StageModel stageModel;

    public Controller(){

    }
    
    public void initialize() {
        this.stageModel = new StageModel(this.view.getRowCount(), this.view.getColumnCount());
        this.boardImageView.setFitWidth(this.view.getLayoutBounds().getWidth());
        this.boardImageView.setFitHeight(this.view.getLayoutBounds().getHeight());
        this.update();
    }

    public double getBoardWidth() {
        return view.CELL_WIDTH * this.view.getColumnCount();
    }

    public double getBoardHeight() {
        return view.CELL_WIDTH * this.view.getRowCount();
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        boolean keyRecognized = true;
        KeyCode code = keyEvent.getCode();
        if (code == KeyCode.LEFT || code == KeyCode.A) {
            this.stageModel.moveCharacter(0, -1);
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            this.stageModel.moveCharacter(0, 1);
        } else if (code == KeyCode.UP || code == KeyCode.W) {
            this.stageModel.moveCharacter(-1, 0);
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            this.stageModel.moveCharacter(1, 0);
        } else if (code == KeyCode.E) {
            this.stageModel.attack();
        } else if (code == KeyCode.F) {
            this.stageModel.spAttack();
        }  else if (code == KeyCode.R) {
            this.stageModel.longAttack();
        }  else if (code == KeyCode.Q) {
            this.stageModel.endTurn();
        } else if (code == KeyCode.G) {
            this.stageModel.startNewGame();
        } else {
            keyRecognized = false;
        }

        if (keyRecognized) {
            this.update();
            keyEvent.consume();
        }
    }

    private void update(){
        this.view.updateStage(this.stageModel);
        this.updateDisplay();
    }

    /**
     * Updates the Border Displays according to current game status
     */
    private void updateDisplay(){
        this.scoreLabel.setText(String.format("HP: %d" + "|| AC: %d" + "\n Turn: %d", this.stageModel.getPlayerHP(),
                this.stageModel.getPlayerActionCredits(), this.stageModel.getTurn()));
        if (this.stageModel.isWinner()){
            if(this.stageModel.getPlayerHP() == 0){
                this.alertLabel.setText("Player 1 Wins!");
            }
            else if(this.stageModel.getEnemyHP() == 0){
                this.alertLabel.setText("Player 2 Wins!");
            }
        }else if (this.stageModel.getInsufficientCredits() == true) {
            this.alertLabel.setText("Insufficient Credits");
        } else {
            if(this.stageModel.getTurn() == 0){
                this.alertLabel.setText("Player 1's Turn");
            }
            else if(this.stageModel.getTurn() == 1){
                this.alertLabel.setText("Player 2's Turn");
            }
        }
        this.messageLabel.setText(String.format("Player 2 HP: %d", this.stageModel.getEnemyHP()));
    }

}
