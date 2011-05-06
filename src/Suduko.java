
public class Suduko {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
//    Position p = new Position(0,0,4);
//    System.out.println(p.toString());
//    p.setValue(3);
//    p.updateDomain(3);
//    System.out.println(p.toString());

    Board b = new Board(args[0], true);
    System.out.println(b.toString());

    SudukoSolver ss = new SudukoSolver(b);
    ss.solve();
//
    System.out.println("After: ");
    System.out.println(b.toString());
    b.writeToFile("Outfile.txt");
  }

}
