import java.util.LinkedList;
/**
 * @author imyjimmy
 *
 */
public class Position implements Comparable<Position>{
  private final int x;
  private final int y;
  private int value;

  private int lastGuess;

  private Board b;

  private LinkedList<Integer> Domain;

  public Position(int x, int y, int range, Board b) {
    this.x = x;
    this.y = y;
    this.value = -1;
    this.b = b;

    this.Domain = new LinkedList<Integer>();
    for (int i=0; i< range; i++) {
      this.Domain.add(new Integer(i+1));
    }

  }

  public Position(int x, int y, int range, int value, Board b) {
    this.x = x;
    this.y = y;
    this.value = value;
    this.b = b;

    Domain = new LinkedList<Integer>();
    for (int i=0; i< range; i++) {
      this.Domain.add(new Integer(i+1));
    }
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setValue(int value) {
    this.value = value;
    b.updateConstraints(this);
    b.updateUnassigned();
  }

  public boolean updateDomain(int value) {
    return Domain.remove(new Integer(value));
  }

  public boolean addToDomain(int value) {
    if (value != 0 && this.value == -1)
      return Domain.add(new Integer(value));
    return false;
  }

  public void undoSetValue(int value) {
    this.value = -1;
    setLastGuess(value);
    Domain.add(new Integer(value));
    b.updateConstraints(this);
    b.updateUnassigned();
  }

  public int compareTo(Position other) {
    return ((this.x - other.x) == 0 && (this.y - other.y)==0) ? 0:1;
  }

  @Override
  public boolean equals(Object o) {
    Position other = (Position) o;
    return ((this.x - other.x) == 0 && (this.y - other.y)==0 && (this.value - other.getValue() == 0));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(" + x + "," + y + "): " + value + "\n" + "Domain: ");
    for (Integer i : Domain)
      sb.append(i + ",");
    sb.append("\n");
    return sb.toString();
  }

  public int getValue() {
    return value;
  }

  public void setBoard(Board b) {
    this.b = b;
  }

  public Board getBoard() {
    return b;
  }

  public LinkedList<Integer> getDomain() {
    return Domain;
  }

  public boolean isDomainEmpty() {
    return (Domain.size() == 0);
  }

  public void setLastGuess(int lastGuess) {
    this.lastGuess = lastGuess;
  }

  public int getLastGuess() {
    return lastGuess;
  }
}
