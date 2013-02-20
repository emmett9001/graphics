import java.awt.*;

public abstract class BufferedApplet extends java.applet.Applet implements Runnable
{
   public boolean damage = true;
   public abstract void render(Graphics g);

   Image bufferImage = null;
   protected Graphics bufferGraphics = null;
   private Thread t;
   private Rectangle r = new Rectangle(0,0,0,0);

   public void start() { if (t == null) { t = new Thread(this); t.start(); } }
   public void stop()  { if (t != null) { t.stop(); t = null; } }
   public void run() {
      try {
         while (true) { repaint(); t.sleep(30); }
      }
      catch(InterruptedException e){};
   }

   public void update(Graphics g) {
      if (r.width != bounds().width || r.height != bounds().height) {
         bufferImage    = createImage(bounds().width, bounds().height);
         bufferGraphics = bufferImage.getGraphics();
         r = bounds();
         damage = true;
      }
      render(bufferGraphics);
      damage = false;
      paint(g);
   }

   public void paint(Graphics g) {
      if (bufferImage != null)
         g.drawImage(bufferImage,0,0,this);
   }
}
