
import java.awt.*;
import java.awt.event.*;

public class listener extends BufferedApplet implements KeyListener,
                                                        MouseListener,
							MouseMotionListener
{
   int w = 0, h = 0;

   // KEY LISTENER METHODS

   public void keyPressed(KeyEvent e) {
      System.err.println("key pressed " + (char)e.getKeyChar());
   }
   public void keyTyped(KeyEvent e) {
      System.err.println("key typed " + (char)e.getKeyChar());
   }
   public void keyReleased(KeyEvent e) {
      System.err.println("key released " + (char)e.getKeyChar());
   }

   // MOUSE LISTENER METHODS

   public void mouseEntered(MouseEvent e) {
      System.err.println("mouse entered " + e.getX() + " " + e.getY());
   }
   public void mouseExited(MouseEvent e) {
      System.err.println("mouse exited " + e.getX() + " " + e.getY());
   }
   public void mousePressed(MouseEvent e) {
      System.err.println("mouse pressed " + e.getX() + " " + e.getY());
   }
   public void mouseReleased(MouseEvent e) {
      System.err.println("mouse released " + e.getX() + " " + e.getY());
   }
   public void mouseClicked(MouseEvent e) {
      System.err.println("mouse clicked " + e.getX() + " " + e.getY());
   }

   // MOUSE MOTION LISTENER METHODS

   public void mouseDragged(MouseEvent e) {
      System.err.println("mouse dragged " + e.getX() + " " + e.getY());
   }
   public void mouseMoved(MouseEvent e) {
      System.err.println("mouse moved " + e.getX() + " " + e.getY());
   }

   public void render(Graphics g) {
      if (w == 0) {
         w = getWidth();
         h = getHeight();

	 addKeyListener(this);
	 addMouseListener(this);
	 addMouseMotionListener(this);

	 requestFocus();
      }

      g.setColor(Color.white);
      g.fillRect(0, 0, w, h);

      g.setColor(Color.black);
      g.fillOval(w / 2 - w / 8, h / 2 - h / 8, w / 2, h / 2);
   }
}

