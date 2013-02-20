
import java.awt.*;
import java.awt.event.*;

public class keylistener extends BufferedApplet implements KeyListener
{
   int w = 0, h = 0;

   // KEY LISTENER METHODS

   public void keyPressed(KeyEvent e) {
      System.err.println("pressed " + e.getKeyChar());
   }

   public void keyTyped(KeyEvent e) {
      System.err.println("typed " + e.getKeyChar());
   }

   public void keyReleased(KeyEvent e) {
      System.err.println("released " + e.getKeyChar());
   }

   public void render(Graphics g) {
      if (w == 0) {
         w = getWidth();
         h = getHeight();
	 addKeyListener(this);
	 requestFocus();
      }

      g.setColor(Color.white);
      g.fillRect(0, 0, w, h);

      g.setColor(Color.black);
      g.fillOval(100, 200, 200, 100);
   }
}

