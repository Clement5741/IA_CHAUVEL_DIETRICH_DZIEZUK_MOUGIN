package ia.problemes;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.function.Function;

import ia.framework.common.Action;
import ia.framework.common.State;
import ia.framework.common.Misc;
import ia.framework.jeux.GameState;

/**
 * Représente un état d'un jeu générique m,n,k Game 
 */

public class MnkGameState extends AbstractMnkGameState {

   
    /**
     * Construire une grille vide de la bonne taille
     *
     * @param r nombre de lignes
     * @param c nombre de colonnes 
     */
    public MnkGameState(int r, int c, int s) {
        super(r,c,s);
    }

    public MnkGameState cloneState() {
        MnkGameState new_s = new MnkGameState(this.rows, this.cols, this.streak);
        new_s.board = this.board.clone();
        new_s.player_to_move = player_to_move;
        new_s.game_value = game_value;
        if(this.last_action != null)
            new_s.last_action = this.last_action.clone();
        for (Pair p: this.winning_move)
            new_s.winning_move.add(p.clone());
        return new_s;
	}
    /**
     * Un fonction d'évaluation pour cet état du jeu. 
     * Permet de comparer différents états dans le cas ou on ne  
     * peut pas développer tout l'arbre. Le joueur 1 (X) choisira les
     * actions qui mènent au état de valeur maximal, Le joueur 2 (O)
     * choisira les valeurs minimal.
     * 
     * Cette fonction dépend du jeu.
     * 
     * @return la valeur du jeux
     **/
//    protected double evaluationFunction() {
//        return Double.NaN;
//    }
    @Override
    protected double evaluationFunction() {
        int scoreX = calculateDangerScore(X);
        int scoreO = calculateDangerScore(O);

        return scoreX - scoreO;
    }

    // Fonction pour évaluer le score avec notion de danger
    private int calculateDangerScore(int player) {
        int score = 0;
        score += evaluateHorizontalDanger(player);
        score += evaluateVerticalDanger(player);
        score += evaluateDiagonalDanger(player);
        return score;
    }

    // Évaluation du danger sur les lignes horizontales
    private int evaluateHorizontalDanger(int player) {
        int res = 0;
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c <= this.cols - this.streak; c++) {
                int count = 0, empty = 0;
                for (int k = 0; k < this.streak; k++) {
                    int value = this.getValueAt(r, c + k);
                    if (value == player) count++;
                    else if (value == EMPTY) empty++;
                }
                if (empty + count == this.streak) {
                    res += Math.pow(2, count); // Augmentation exponentielle en fonction du danger
                }
            }
        }
        return res;
    }

    // Évaluation du danger sur les lignes verticales
    private int evaluateVerticalDanger(int player) {
        int res = 0;
        for (int c = 0; c < this.cols; c++) {
            for (int r = 0; r <= this.rows - this.streak; r++) {
                int count = 0, empty = 0;
                for (int k = 0; k < this.streak; k++) {
                    int value = this.getValueAt(r + k, c);
                    if (value == player) count++;
                    else if (value == EMPTY) empty++;
                }
                if (empty + count == this.streak) {
                    res += Math.pow(2, count);
                }
            }
        }
        return res;
    }

    // Évaluation du danger sur les diagonales
    private int evaluateDiagonalDanger(int player) {
        int res = 0;

        // Diagonale descendante (\)
        for (int r = 0; r <= this.rows - this.streak; r++) {
            for (int c = 0; c <= this.cols - this.streak; c++) {
                int count = 0, empty = 0;
                for (int k = 0; k < this.streak; k++) {
                    int value = this.getValueAt(r + k, c + k);
                    if (value == player) count++;
                    else if (value == EMPTY) empty++;
                }
                if (empty + count == this.streak) {
                    res += Math.pow(2, count);
                }
            }
        }

        // Diagonale montante (/)
        for (int r = this.streak - 1; r < this.rows; r++) {
            for (int c = 0; c <= this.cols - this.streak; c++) {
                int count = 0, empty = 0;
                for (int k = 0; k < this.streak; k++) {
                    int value = this.getValueAt(r - k, c + k);
                    if (value == player) count++;
                    else if (value == EMPTY) empty++;
                }
                if (empty + count == this.streak) {
                    res += Math.pow(2, count);
                }
            }
        }
        return res;
    }

}
