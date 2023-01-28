import com.sun.tools.javac.Main;

public class Move
{
    public String piece = "";
    public int[] initialSquare = new int[2];
    public int[] finalSquare = new int[2];
    public int[] pieceMoved = {initialSquare[0], initialSquare[1]};
    public int[] pieceCaptured = {finalSquare[0], finalSquare[1]};

    public Move(int[] initialSquare, int[] finalSquare)
    {
        this.initialSquare = initialSquare;
        this.finalSquare = finalSquare;
    }

    public String getPiece()
    {
        return piece;
    }

    public int[] getInitialSquare()
    {
        return initialSquare;
    }

    public int[] getFinalSquare()
    {
        return finalSquare;
    }
}
