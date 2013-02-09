import java.awt.*;
import java.lang.Math;
import java.util.Random;

public class Reflection extends BufferedApplet{
    Color boundsColor = new Color(0,0,0);
    Random gen = new Random();
    final int numBalls = 100;
    final double bounce = .9999;

    Circle cBounds = new Circle(0, 0, 250, boundsColor);
    Circle[] balls = new Circle[numBalls];

    long startTime = System.currentTimeMillis();
    boolean started = false;

    public void render(Graphics g){
        if(!started){
            started = true;
            for(int i = 0; i < numBalls; i++){
                Color ballColor = new Color(Math.abs(gen.nextInt()) % 200, Math.abs(gen.nextInt()) % 200, Math.abs(gen.nextInt()) % 200);
                balls[i] = new Circle(cBounds.GetCenter().x, cBounds.GetCenter().y,
                    25, ballColor);
                balls[i].SetVelocity(new Vec2(gen.nextInt() % 20, gen.nextInt() % 20));
            }
        }

        double time = (System.currentTimeMillis() - startTime) / 1000.0;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, bounds().width, bounds().height);

        cBounds.draw(g);

        // TODO - collide the balls against each other

        for(int i = 0; i < numBalls; i++){
            balls[i].update();
            balls[i].draw(g);
            Vec2 displacement = balls[i].GetCenter().sub(cBounds.GetCenter());

            if(displacement.mag() > cBounds.GetRadius() - balls[i].GetRadius()){
                Vec2 norm_disp = displacement.normalize();

                double dot_product = balls[i].GetVelocity().dot(norm_disp);
                // clamp dot product to 1 to avoid balls moving through wall
                if(dot_product < 1){
                    dot_product = 1;
                }
                Vec2 v = norm_disp.mul(dot_product * 2 * bounce);

                balls[i].SetVelocity(balls[i].GetVelocity().sub(v));
            }
        }
    }
}
