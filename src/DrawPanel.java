import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.awt.Font;
import java.util.Objects;

class DrawPanel extends JPanel implements MouseListener {

    private Deck d;
    private Card[][] cards;

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
    }
    public Boolean JQK ()
    {
        int count = 0;
        String[] royals = {"J", "Q", "K"};
        String[] JQK = new String[3];
        JQK[0] = "";
        JQK[1] = "";
        JQK[2] = "";
        boolean isJQKTrue = false;
        for (Card[] card : cards) {
                for (int i = 0; i < JQK.length; i++) {
                    if (card[i].getHighlight()) {
                        JQK[i] = card[i].getValue();
                        if (JQK[0].equals(royals[i])) {
                            royals[i] = "";
                            count++;
                            if (count == 2) {
                                isJQKTrue = true;
                            }
                        } else if (JQK[1].equals(royals[i])) {
                            royals[i] = "";
                            count++;
                            if (count == 2) {
                                isJQKTrue = true;
                            }
                        } else if (JQK[2].equals(royals[i])) {
                            royals[i] = "";
                            count++;
                            if (count == 2) {
                                isJQKTrue = true;
                            }
                        }
                    }
                }
            }
        return isJQKTrue;
    }
    public void mousePressed(MouseEvent e) {

        ArrayList<Card> cardValues = new ArrayList<>(0);
        Point p = e.getPoint();
        int button = e.getButton();

        for (int r = 0; r < cards.length; r++) {
            for (int c = 0; c < cards.length; c++) {
                if (d.getDeck().size() != 0 && button == 1) {
                    if (cards[r][c].getHitbox().contains(p)) {
                        cards[r][c] = d.getRandomCard();
                    }
                }

                if (button == 3 && cards[r][c].getHitbox().contains(p)) {
                    cards[r][c].flipHighlight();
                    cardValues.add(cards[r][c]);
                }
                if (button == 3 && JQK()) {
                }

                for (int i = 0; i < cards.length; i++) {
                    if (JQK()) {
                            for (Card[] cardPlace : cards)
                            {
                                if (cards[r][c].getHighlight())
                                {
                                    cards[r][c] = d.getRandomCard();
                                }
                            }

                    }
                }
            }
        }



    }

    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
}