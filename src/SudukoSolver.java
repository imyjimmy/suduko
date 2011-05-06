import java.util.LinkedList;


public class SudukoSolver {
  private Board b;

  public SudukoSolver(Board b) {
    this.b = b;
  }

  public Board solve() {
    return backtrack(this.b);
  }

  public Board backtrack(Board csp) {
    if (csp.isComplete())
      return csp;
    Position unassigned = csp.selectUnassignedVariable();

//    System.out.println("Selected unassigned variable: " + unassigned.toString());

    LinkedList<Integer> domain = unassigned.getDomain();
    for (int i=0; i<domain.size(); i++) {
      Integer guess = domain.pop();
//      System.out.println("Guessing: " + guess.toString() + "for variable:" + unassigned.toString() + "\n");

      unassigned.setValue(guess);
      if (csp.isConsistent()) {
        Board b = backtrack(csp);

        if (b.isComplete() && b.isConsistent())
          return b;
  //      System.out.println("Undoing: " + guess.toString() + "for variable at: " + unassigned.toString());
      }
      unassigned.undoSetValue(guess);
    }
    return csp;
  }

}
