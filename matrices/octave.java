
import java.awt.*;

public class octave extends BufferedApplet
{
   int w = 0, h = 0, fontHeight, mx = -100, my, kw, kh, id = 0, I = -1;
   Color hoverColor = new Color(120,180,255);
   Color bgColor = new Color(200,230,255);
   int[] scale = {0,2,4,5,7,9,11,12};
   Instrument instrument;
   boolean isMouseDown;
   Font font;
   { setInstrument(0); }

   public void render(Graphics g) {
      if (w == 0) {
         w = getWidth();
         h = getHeight();
	 kw = w / 10;
	 kh = w / 5;
	 font = new Font("Sanserif", Font.BOLD, fontHeight = w / 20);
      }
      g.setFont(font);
      g.setColor(bgColor);
      g.fillRect(0, 0, w, h);
      for (int i = 0 ; i < scale.length ; i++) {
         g.setColor(i == I ? isMouseDown ? Color.blue : hoverColor
	                   : Color.white);
         g.fillRect(x(i) - kw/2, h/2 - kh/2, kw, kh);
         g.setColor(Color.black);
         g.drawRect(x(i) - kw/2, h/2 - kh/2, kw, kh);
	 g.drawString("CDEFGABC".substring(i,i+1),
	              x(i) - fontHeight / 3, h/2 + fontHeight / 3);
      }
      g.drawString(Instrument.name(id), fontHeight / 6, fontHeight);
   }

   public boolean keyUp(Event e, int key) {
      System.err.println(key);
      switch (key) {
      case 1006: // LEFT ARROW
      case '-':
         setInstrument(Math.max(0, id - 1));
	 return true;
      case 1007: // RIGHT ARROW
      case '+':
      case '=':
         setInstrument(id + 1);
	 return true;
      }
      return false;
   }

   public boolean mouseMove(Event e, int x, int y) {
      xy2I(x,y);
      return true;
   }

   public boolean mouseDown(Event e, int x, int y) {
      isMouseDown = true;
      if (I >= 0)
         press(I);
      return true;
   }

   public boolean mouseDrag(Event e, int x, int y) {
      int J = I;
      xy2I(x,y);
      if (J >= 0 && I != J)
         release(J);
      if (I >= 0 && I != J)
	 press(I);
      return true;
   }

   public boolean mouseUp(Event e, int x, int y) {
      isMouseDown = false;
      if (I >= 0)
         release(I);
      return true;
   }

   int x(int i) {
      return (int)((i + 0.5) * w / scale.length);
   }

   void setInstrument(int i) {
      instrument = new Instrument(id = i);
   }

   void xy2I(int x, int y) {
      mx = x;
      my = y;
      for (I = scale.length - 1 ; I >= 0 ; I--)
         if(y >= h/2 - kh/2 && y < h/2 + kh/2 &&
	    x >= x(I) - kw/2 && x < x(I) + kw/2)
	    return;
   }

   void press(int I) {
      instrument.press(64 + scale[I], 0.5);
   }

   void release(int I) {
      instrument.release(64 + scale[I]);
   }
}

