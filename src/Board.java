import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Board {
  private Position[][] board;

  private LinkedList<Position> unassigned; //should be a min heap

  private int xDim;
  private int yDim;

  public void setxDim(int xDim) {
    this.xDim = xDim;
  }

  public int getxDim() {
    return xDim;
  }

  public void setyDim(int yDim) {
    this.yDim = yDim;
  }

  public int getyDim() {
    return yDim;
  }

  public void setBoard(Position[][] board) {
    this.board = board;
  }

  public Position[][] getBoard() {
    return board;
  }

  public Position getPosition(int x, int y) {
    return board[x][y];
  }

  public void setPosition(Position p) {
    board[p.getX()][p.getY()] = p;
  }

  public Board(int x, int y) {
    setBoard(new Position[x][y]);
    setxDim(x);
    setyDim(y);

    for(int i=0; i<xDim; i++) {
      for(int j=0; j<yDim; j++) {
        board[i][j] = new Position(i,j, xDim * yDim, this);
      }
    }

    unassigned = new LinkedList<Position>();
  }

  public Board(String file, boolean useClassSpecs) {
      try {
        FileInputStream fis = new FileInputStream(file);
        Scanner scanner = new Scanner(fis, "UTF-8");

        int dimensions = Integer.valueOf(scanner.nextLine());

        this.board = new Position[dimensions][dimensions];
        this.xDim = dimensions;
        this.yDim = dimensions;

        int i, j;

        if(!useClassSpecs) {
          i = 0;
          j = 0;

          while (scanner.hasNext()) {
            String x = scanner.next();
            if (x.compareTo("x") != 0) {
              board[i][j] = new Position(i,j,xDim,Integer.valueOf(x), this);
              board[i][j].updateDomain(Integer.valueOf(x));
            } else {
              board[i][j] = new Position(i,j,xDim, this);
            }
              j++;
              if (j== yDim) {
                j = 0;
                i ++;
              }
          }
        } else {
          int initialVals = Integer.valueOf(scanner.nextLine());
          while(scanner.hasNext()) {
            i = scanner.nextInt();
            j = scanner.nextInt();
            int value = scanner.nextInt();
            board[i-1][j-1] = new Position(i-1,j-1,xDim,value, this);
            board[i-1][j-1].updateDomain(value);
          }

          for (i=0; i<xDim; i++) {
            for (j=0; j<yDim; j++) {
              if (board[i][j] == null) {
                board[i][j] = new Position(i,j,xDim, this);
              }
            }
          }
        }

        unassigned = new LinkedList<Position>();

        for (i=0; i<xDim; i++) {
          for (j=0; j<yDim; j++) {
            this.updateConstraints(board[i][j]);
            if (board[i][j].getValue() == -1 && !unassigned.contains(board[i][j]))
              unassigned.add(board[i][j]);
          }
        }
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
  }

  public void writeToFile(String file) {
    try {
      Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");

      out.write(new Integer(xDim).toString());
      out.write("\n");

      StringBuilder sb = new StringBuilder();
      for (int i=0; i<xDim; i++) {
        for (int j=0; j<yDim; j++) {
          if (board[i][j] != null) {
            int value = board[i][j].getValue();
            if (value != -1)
              sb.append(value);
            else
              sb.append("x");
          }
          if (j != yDim -1)
            sb.append("\t");
        }
        sb.append("\n");
      }
      out.write(sb.toString());
      out.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public void updateConstraints(Position p) {
    if (p.getValue() == -1) { // a guess has been backtracked
      for (int i=0; i<xDim; i++) {
        board[i][p.getY()].addToDomain(p.getLastGuess());
      }
      for (int j=0; j<yDim; j++) {
        board[p.getX()][j].addToDomain(p.getLastGuess());
      }
    } else { // a guess has been issued
      for (int i=0; i<xDim; i++) {
        board[i][p.getY()].updateDomain(p.getValue());
      }
      for (int j=0; j<yDim; j++) {
        board[p.getX()][j].updateDomain(p.getValue());
      }
    }
  }

  public void updateUnassigned() {
    for (Iterator<Position> itr = unassigned.iterator(); itr.hasNext();) {
      Position p = itr.next();
      if (p.getValue() != -1) {
        itr.remove();
      }
    }

    for (int i=0; i<xDim; i++) {
      for (int j=0; j<yDim; j++) {
        if (board[i][j].getValue()== -1 && !unassigned.contains(board[i][j])) {
//          System.out.println("adding to unassigned: " + i + "," + j);
          unassigned.add(board[i][j]);
        }
      }
    }
  }

  public Position selectUnassignedVariable() {
    return unassigned.pop();
  }


  public boolean isConsistent() {
    updateUnassigned();
    for (Position p : unassigned) {
      if (p.getValue() == -1 && p.isDomainEmpty())
        return false;
    }
    return true;
  }

  public boolean isComplete() {
    updateUnassigned();
    return (unassigned.size()==0);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for(int i=0; i<xDim; i++) {
      for(int j=0; j<yDim; j++) {
        if (board[i][j] != null)
          sb.append(board[i][j].toString());
      }
    }
    sb.append("unassigned: ");
    for (Position p : unassigned)
      sb.append(p.toString());
    return sb.toString();
  }

}

