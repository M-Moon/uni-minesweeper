import java.io.*;

public class BombSquare extends GameSquare
{
    private GameBoard board;                            // Object reference to the GameBoard this square is part of.
    private boolean hasBomb;                            // True if this square contains a bomb. False otherwise.
    private boolean checked = false;
    private boolean flagged = false;

	public static final int MINE_PROBABILITY = 10;

	public BombSquare(int x, int y, GameBoard board)
	{
		super(x, y, "images/blank.png");

        this.board = board;
        this.hasBomb = ((int) (Math.random() * MINE_PROBABILITY)) == 0;
    }

    public void leftClicked()
    {
        if (this.hasBomb)
        {
            this.setImage("images/bomb.png");
            this.checked = true;
            //System.out.println("test");
            //System.exit(1);
        }
        else
        {
            this.checkSquare(this.getXLocation(), this.getYLocation());
        }
    }

    public void rightClicked()
    {
        if (!flagged && !checked)
        {
            this.setImage("images/flag.png");
            flagged = true;
        }
        else if (flagged && !checked)
        {
            this.setImage("images/blank.png");
            flagged = false;
        }
        //System.out.println("Test2");
    }

    public boolean getHasBomb() // method to check if the square has a bomb
    {
        if (this.hasBomb) { return true; }
        return false;
    }

    public boolean getChecked() // method to see if square has already been checked
    {
        return checked;
    }

    public void setChecked() // method to set that square is checked
    {
        this.checked = true;
    }

    public void checkSquare(int x, int y) // recursive method to check a square, called also for surrounding squares
    {
        if (board.getSquareAt(x, y) == null) // check square is valid
            return;

        BombSquare accessedSquare = (BombSquare) board.getSquareAt(x, y); // get square as ref

        if (accessedSquare.getChecked()) // check if square has already been checked, return if so
            return;

        int mines = countSurroundingMines(x, y); // count mines surrounding square

        accessedSquare.setChecked(); // set that current square has been checked

        if (mines > 0) { this.assignMineAmount(mines, accessedSquare); } // if any mines surround the square, assign relevant icon
        else
        {
            accessedSquare.setImage("images/0.png"); // no mines surrounding the square

            // recursive calling of checkSquare for all 8 surrounding spaces
            checkSquare(x-1, y-1);
            checkSquare(x-1, y);
            checkSquare(x-1, y+1);
            checkSquare(x, y-1);
            checkSquare(x, y+1);
            checkSquare(x+1, y-1);
            checkSquare(x+1, y);
            checkSquare(x+1, y+1);
        }
    }

    private void assignMineAmount(int mines, BombSquare accessedSquare) // set correct icon for referenced square
    {
        //System.out.println("Image is set to " + mines + ".png");
        accessedSquare.setImage("images/" + mines + ".png");
    }

    public int countSurroundingMines(int x, int y) // method to count how many mines surround a square
    {
        int mineCount = 0;

        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dy = -1; dy <= 1; dy++)
            {
                BombSquare accessedSquare = (BombSquare) board.getSquareAt(dx + x, dy + y);
                if ((dx != 0 || dy != 0) && (accessedSquare != null) && (accessedSquare.getHasBomb()))
                {
                    mineCount++;
                    //System.out.println(mineCount + "\n");
                }
            }
        }

        return mineCount;
    }
}
