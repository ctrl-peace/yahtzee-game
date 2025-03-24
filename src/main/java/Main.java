// import static org.junit.jupiter.api.Assertions.assertEquals;

// import org.junit.jupiter.api.Test;

public class Main {
  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
          Yahtzee.runGUI();
      }
    });
  }

  // @Test
  // void addition() {
  //     assertEquals(2, 1 + 1);
  // }
}