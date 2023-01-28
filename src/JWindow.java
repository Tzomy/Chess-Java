import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;

public class JWindow extends JFrame
{
    private String[][] chessBoard= {
            {"B_r", "B_n", "B_b", "B_q", "B_k", "B_b", "B_n", "B_r"},
            {"B_p", "B_p", "B_p", "B_p", "B_p", "B_p", "B_p", "B_p"},
            {"---", "---", "---", "---", "---", "---", "---", "---"},
            {"---", "---", "---", "---", "---", "---", "---", "---"},
            {"---", "---", "---", "---", "---", "---", "---", "---"},
            {"---", "---", "---", "---", "---", "---", "---", "---"},
            {"W_p", "W_p", "W_p", "W_p", "W_p", "W_p", "W_p", "W_p"},
            {"W_r", "W_n", "W_b", "W_q", "W_k", "W_b", "W_n", "W_r"}
    };

    private int[] sq_selected = {0, 0};
    private ArrayList<int[]> move = new ArrayList<int[]>();
    private Color backColor = new Color(234, 233, 210); // creo il colore di sfondo
    private Dimension dim = new Dimension(807, 838); // creo la dimensione standard della finestra
    private Font font = new Font("TimesRoman", Font.PLAIN, 80);
    private Color sqColor = new Color(75, 115, 153);
    private boolean whiteToMove = true; //TODO
    private ArrayList<Move> moves = new ArrayList<Move>();


    public JWindow()
    {
        setSize(dim); // setto la dimensione della finestra
        setMinimumSize(dim); // setto dimensione minima e massima per la finestra

        addMouseListener(new MouseAdapter()
        { // riusciamo a prendere come informazione
            // le coordinate dove il mouse va a cliccare

            public void mouseClicked(MouseEvent e)
            {
                Point pos = e.getPoint();
                sq_selected[0] = (pos.x) / 100;
                sq_selected[1] = (pos.y - 30) / 100;


                move.add(Arrays.copyOf(sq_selected, sq_selected.length));
                /*
                per evitare bug strani tipo modifiche della casella presa in considerazione
                andiamo ad utilizzare Arrays.copyOf(array, lenght) per passare una copia
                dell'array che ci interessa
                System.out.println("SIUM " + sq_selected[0] + "," + sq_selected[1]);
                */

                if (move.size() == 1 && (chessBoard[sq_selected[1]][sq_selected[0]]).equals("---"))
                {
                    // se il primo quadrato selezionato è vuoto non lo selezioniamo per evitare
                    // bug come la scomparsa di alcuni pezzi all'interno della nostra scacchiera
                    move.remove(0);
                }
                if (move.size() == 2)
                {
                    makeMove(move.get(0), move.get(1), false);

                    move.remove(move.size() - 1);
                    move.remove(0); // non andava bene -2 perchè la dimensione
                    //veniva cambiata nel mentre
                }

            }

        });

        addKeyListener(new KeyAdapter()
        {
            public void keyTyped(KeyEvent e)
            {
                super.keyTyped(e);
                //System.out.println(e.getKeyChar());
                //stampa il bottone che abbiamo premuto
                if (e.getKeyChar() == 'z') undoMove();
            }
        });

        /*
        String a = KeyEvent.getKeyText(KeyEvent.VK_Z);

        System.out.println(a);

        System.out.println(KeyEvent.VK_Z); // il valore della lettera z quando viene premuta sarebbe 90? si
         */
        // keyPressed(KeyEvent.VK_Z);

        getContentPane().setBackground(backColor); // colora lo sfondo di un certo colore
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // chiude la finestra
        setLocationRelativeTo(null); // setta la posizione di creazione su null
        setVisible(true); // rende visibile la finestra
    }

    private void drawBoard(Graphics g)
    {
        for (int i = 0; i < 8; i++) // creo la scacchiera
        {
            for (int j = i % 2; j < 8; j += 2)
            {
                g.fillRect(i * 100, j * 100 + 31, 100, 100);
            }
        }
        //g.fillRect(50, 50, 100, 100); crea un rettangolo pieno
    }

    private void drawPieces(Graphics g)
    {
        for (int i = 0; i < 8; i++) // disegna tutti i pezzi nella scacchiera
        {
            for (int j = 0; j < 8; j++)
            {
                if (chessBoard[j][i].charAt(0) == 'B') g.setColor(Color.BLACK);
                else g.setColor(Color.GRAY);
                //System.out.println(String.valueOf(board[i][j].charAt(2)).toUpperCase() + ": " + i + ", j: " + j);
                String p = String.valueOf(chessBoard[j][i].charAt(2)).toUpperCase();
                if (!(p.equals("-"))) g.drawString(p, i * 100 + 20, j * 100 + 110);
            }
        }
    }

    public void paint(Graphics g)
    {
        super.paint(g); // non so cosa faccia
        g.setFont(font);
        g.setColor(sqColor);

        drawBoard(g);
        drawPieces(g);
    }

    public void makeMove(int[] start_sq, int[] finish_sq, boolean isUndo)
    {
        /*
        System.out.println(start_sq[0] + "," + start_sq[1]);
        System.out.println(finish_sq[0] + "," + finish_sq[1]);
        System.out.println("piece: " + chessBoard[start_sq[1]][start_sq[0]]);
         */

        String mvPiece = chessBoard[start_sq[1]][start_sq[0]];
        chessBoard[start_sq[1]][start_sq[0]] = "---";
        chessBoard[finish_sq[1]][finish_sq[0]] = mvPiece;
        paint(getGraphics());
        if (!isUndo) moves.add(new Move(start_sq, finish_sq));
        //TODO

        // System.out.println(Arrays.deepToString(chessBoard));

    }

    public void undoMove() //TODO
                           //FIX
    {
        if (moves.size() != 0)
        {
            Move move = moves.get(moves.size() - 1);
            String mvPiece = chessBoard[move.initialSquare[1]][move.initialSquare[0]];
            String cpPiece = chessBoard[move.finalSquare[1]][move.finalSquare[0]];
            moves.remove(move);
            chessBoard[move.initialSquare[1]][move.initialSquare[0]] = cpPiece;
            chessBoard[move.finalSquare[1]][move.finalSquare[0]] = mvPiece;
            paint(getGraphics());
            System.out.println(Arrays.toString(move.initialSquare) + " , " + Arrays.toString(move.finalSquare));
            System.out.println(cpPiece + " , " + mvPiece);
            System.out.println(Arrays.deepToString(chessBoard));
        }
    }

    public void getValidMoves()
    {
        //TODO
    }

    // public


    public static void main(String[] args)
    {
        JWindow window = new JWindow();
    }
}
