public class HelloAllJ {
  public static void main(String args[]) {
    int i = 0;
    while (i < args.length) {
      System.out.println("Hello, " + args[i]);
      ++i;
    }
  }
}
