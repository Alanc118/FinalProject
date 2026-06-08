import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

class DrawPanel extends JPanel implements MouseListener {
    private Deck d;
    private static Card[][] cards;
    ArrayList<int[]> selectedCoordinates = new ArrayList<>(2);

    private final int boxX = 350;
    private final int boxY = 10;
    private final int width = 130;
    private final int height = 200;

    public DrawPanel() {
        cards = new Card[3][3];
        d = new Deck();
        for (int r = 0; r < cards.length; r++) {
            for (int c = 0; c < cards.length; c++) {
                cards[r][c] = d.getRandomCard();
            }
        }
        this.addMouseListener(this);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 50;
        int y = 10;
        for (int r = 0; r < cards.length; r++) {
            for (int c = 0; c < cards.length; c++) {
                g.drawImage(cards[r][c].getImage(), x, y, null);
                Rectangle cardHitBox = new Rectangle(x, y, cards[r][c].getImage().getWidth(), cards[r][c].getImage().getHeight());
                cards[r][c].setHitbox(cardHitBox);
                if (cards[r][c].getHighlight()) {
                    g.drawRect(x, y, (int)cardHitBox.getWidth(), (int)cardHitBox.getHeight());
                }
                x += 80;
            }
            y += 100;
            x = 50;
        }
        g.drawString("Number of cards left: " + d.getDeck().size(), x, y + 100);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(boxX, boxY, width, height);
        g.setColor(Color.WHITE);
        g.drawRect(boxX, boxY, width, height);
        g.drawString("REPLACE", boxX + 35, boxY + 90);
        g.drawString("CARDS", boxX + 42, boxY + 110);
    }

    public void playTurn(ArrayList<int[]> selectedCoordinates) {
        if (selectedCoordinates.size() == 2 && containsPairSum11(cards)) {
            int[] c1 = selectedCoordinates.get(0);
            int[] c2 = selectedCoordinates.get(1);

            cards[c1[0]][c1[1]] = d.getRandomCard();
            cards[c2[0]][c2[1]] = d.getRandomCard();

            selectedCoordinates.clear();

        } else if (selectedCoordinates.size() == 3 && containsJQK(cards)) {
            int[] c1 = selectedCoordinates.get(0);
            int[] c2 = selectedCoordinates.get(1);
            int[] c3 = selectedCoordinates.get(2);

            boolean hasJ = false, hasQ = false, hasK = false;
            Card[] select = {cards[c1[0]][c1[1]], cards[c2[0]][c2[1]], cards[c3[0]][c3[1]]};

            for (Card choice : select) {
                if (choice.getValue().equals("J")) { hasJ = true; }
                if (choice.getValue().equals("Q")) { hasQ = true; }
                if (choice.getValue().equals("K")) { hasK = true; }
            }
            if (hasJ && hasQ && hasK) {
                cards[c1[0]][c1[1]] = d.getRandomCard();
                cards[c2[0]][c2[1]] = d.getRandomCard();
                cards[c3[0]][c3[1]] = d.getRandomCard();
                selectedCoordinates.clear();
            }

        }
    }


    private int getCardNumericValue(Card card) {
        if (card == null) return -1;
        String val = card.getValue();


        if (val.equals("A")) {
            return 1;
        }

        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean containsPairSum11(Card[][] board) {
        boolean is11 = false;
        for (int r1 = 0; r1 < board.length; r1++) {
            for (int c1 = 0; c1 < board[r1].length; c1++) {
                for (int r2 = 0; r2 < board.length; r2++) {
                    for (int c2 = 0; c2 < board[r2].length; c2++) {
                        if (!(r1 == r2 && c1 == c2)) {
                            Card card1 = board[r1][c1];
                            Card card2 = board[r2][c2];

                            if (card1 != null && card2 != null) {
                                int val1 = getCardNumericValue(card1);
                                int val2 = getCardNumericValue(card2);

                                // Both values must be valid card numbers (1 through 10)
                                if (val1 != -1 && val2 != -1) {
                                    if (val1 + val2 == 11) {
                                        is11 = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return is11;
    }

    public boolean containsJQK(Card[][] board) {
        boolean foundJack = false;
        boolean foundQueen = false;
        boolean foundKing = false;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Card card = board[row][col];
                if (card != null) {
                    if (card.getValue().equals("J")) { foundJack = true; }
                    if (card.getValue().equals("Q")) { foundQueen = true; }
                    if (card.getValue().equals("K")) { foundKing = true; }
                }
            }
        }
        return foundJack && foundQueen && foundKing;
    }

    public void mousePressed(MouseEvent e) {
        ArrayList<Card> cardValues = new ArrayList<>(0);
        Point p = e.getPoint();
        int button = e.getButton();

        if (p.x >= boxX && p.x <= boxX + width &&
                p.y >= boxY && p.y <= boxY + height) {

            selectedCoordinates.clear();
            for (int r = 0; r < cards.length; r++) {
                for (int c = 0; c < cards[r].length; c++) {
                    if (cards[r][c].getHighlight()) {
                        int[] coord = {r, c};
                        selectedCoordinates.add(coord);
                    }
                }
            }

            playTurn(selectedCoordinates);
            if (selectedCoordinates.isEmpty()) {
                for (int r = 0; r < cards.length; r++) {
                    for (int c = 0; c < cards[r].length; c++) {
                        if (cards[r][c].getHighlight()) {
                            cards[r][c].flipHighlight();
                        }
                    }
                }
            }
            repaint();
            return;
        }

        for (int r = 0; r < cards.length; r++) {
            for (int c = 0; c < cards.length; c++) {
                if (button == 3 && cards[r][c].getHitbox().contains(p)) {
                    cards[r][c].flipHighlight();
                    cardValues.add(cards[r][c]);
                    repaint();
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
}