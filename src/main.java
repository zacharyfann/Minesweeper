import javax.swing.JFrame;

public class main {
     public static void main(String[] args) {
        JFrame frame = new JFrame("Minesweeper");
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    // Board will be 18 by 14 with alternating tile colors
}
// The board will be a 2D array of buttons