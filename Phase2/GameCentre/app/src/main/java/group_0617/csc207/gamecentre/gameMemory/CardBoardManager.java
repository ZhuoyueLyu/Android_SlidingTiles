package group_0617.csc207.gamecentre.gameMemory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import group_0617.csc207.gamecentre.GenericBoardManager;

/**
 * Manage CardBoard
 */
public class CardBoardManager extends GenericBoardManager {

    /**
     * The moves that has been made
     */
    private int moves = 0;

    /**
     * The card chosen
     */
    private List<Card> chosenCards;

    /**
     * Create a CardBoardManager given a new CardBoard with specified complexity
     *
     * @param complexity the complexity desired
     */
    public CardBoardManager(int complexity) {
        super();
        setBoard(new CardBoard(complexity));
        this.chosenCards = new ArrayList<>();
    }

    /**
     * Create a CardBoardManager given a CardBoard
     *
     * @param inCardBoard the CardBoard given
     */
    public CardBoardManager(CardBoard inCardBoard) {
        setBoard(inCardBoard);
        this.chosenCards = new ArrayList<>();
    }


    @Override
    public boolean puzzleSolved() {
        Iterator<Card> iter = ((CardBoard) getBoard()).iterator();
        boolean re = true;
        while (iter.hasNext()) {
            Card curCard = iter.next();
            if (curCard.getIsCovered()) {
                re = false;
            }
        }
        return re;
    }

    @Override
    public boolean isValidTap(int pos) {
        Card cardTapped = getCardAtPos(pos);
        return cardTapped.getIsCovered() && !isFull();
    }

    @Override
    public void touchMove(int pos) {
        if (isValidTap(pos)) {
            ((CardBoard) getBoard()).flipCard(pos);
            chosenCards.add(getCardAtPos(pos));
            this.moves++;
        }
        updateChosenCards();
    }

    /**
     * Check whether the two cards with same background is identified
     *
     * @return whether the two cards with same background is identified
     */
    private boolean hasIdentified() {
        boolean re = false;

        if (isFull() && getFirst().getBackground() == getSecond().getBackground()) {
            re = true;
        }
        return re;
    }

    /**
     * Return whether or not number of chosen cards is at maximum.
     *
     * @return whether or not number of chosen cards is at maximum.
     */
    private boolean isFull() {
        return chosenCards.size() == 2;
    }

    /**
     * update the chosen cards
     */
    private void updateChosenCards() {
        if (hasIdentified()) {
            this.chosenCards = new ArrayList<>();
        } else {
            if (isFull()) {
                for (Card cards : chosenCards) {
                    cards.flip();
                    ((CardBoard) getBoard()).update();
                }
                this.chosenCards = new ArrayList<>();
            }
        }
    }

    /**
     * Return the first Card flipped
     *
     * @return the first Card flipped
     */
    private Card getFirst() {
        return chosenCards.get(0);
    }

    /**
     * Return the second Card flipped
     *
     * @return the second Card flipped
     */
    private Card getSecond() {
        return chosenCards.get(1);
    }

    /**
     * Get the Card at specified position
     *
     * @param pos the specified position
     * @return the Card at specified position
     */
    public Card getCardAtPos(int pos) {
        int complexity = getBoard().getComplexity();
        return (Card) getBoard().getGenericTile(pos / complexity,pos % complexity);
    }

    @Override
    public int getScore() {
        return -this.moves;
    }

    /**
     * The method which return the abbreviation of the Card Memory game, i.e., "CARD"
     *
     * @return "CARD"
     */
    @Override
    public String getCurrentGame() {
        return "card";
    }
}