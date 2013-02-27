import java.awt.*;
import java.util.Random;

public class ObjTest extends BufferedApplet{
    int w = 700;
    int h = 700;
    int numBalls = 13;

    Random gen = new Random();

    Renderer rnd;
    Sphere largeSphere;
    Sphere[] balls = new Sphere[numBalls];

    long startTime = System.currentTimeMillis();
    boolean setupDone = false;

    public void render(Graphics g){
        if(!setupDone){
            setupDone = true;
            rnd = new Renderer(w, h, g);
            largeSphere = new Sphere(rnd, 20, 25, 3);
            for(int i = 0; i < numBalls; i++){
                balls[i] = new Sphere(rnd, 10, 10, .09 + (.2 - .09) * gen.nextDouble());
                balls[i].setPosition(largeSphere.getPosition());
                balls[i].setVelocity(new Vec3(
                    (double)(gen.nextInt() % 20),
                    (double)(gen.nextInt() % 20),
                    (double)(gen.nextInt() % 20)
                ));
                balls[i].setAccel(new Vec3(0, -100, 0));
            }
        }
        double ttime = (System.currentTimeMillis() - startTime) / 1000.0;

        g.setColor(Color.black);
        g.fillRect(0, 0, w, h);

        for(int i = 0; i < numBalls; i++){
            balls[i].update();
            balls[i].draw();

            double maxDisp = largeSphere.getRadius() - balls[i].getRadius();
            Vec3 disp = balls[i].getPosition().sub(largeSphere.getPosition());
            if(disp.mag() > maxDisp){
                Vec3 norm_disp = disp.normalize();
                double dot_product = balls[i].getVelocity().dot(norm_disp);
                if(dot_product < 2){
                    dot_product = 2;
                }
                Vec3 v = norm_disp.mul(dot_product * 2);
                balls[i].setVelocity(balls[i].getVelocity().sub(v));
            }

        }

        largeSphere.rotate(new Vec3(ttime/10, ttime/9, 0));
        largeSphere.update();
        largeSphere.draw();
    }
}
