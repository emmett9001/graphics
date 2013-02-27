import java.awt.*;
import java.util.Random;

public class ObjTest extends BufferedApplet{
    int w = 1280;
    int h = 700;
    int numBalls = 9;

    Renderer rnd;
    Sphere largeSphere;
    Sphere[] balls = new Sphere[numBalls];

    long startTime = System.currentTimeMillis();
    boolean setupDone = false;

    public void render(Graphics g){
        if(!setupDone){
            setupDone = true;
            rnd = new Renderer(w, h, g);
            largeSphere = new Sphere(rnd, 3);
            for(int i = 0; i < numBalls; i++){
                balls[i] = new Sphere(rnd, 10, 10, .2);
                balls[i].setPosition(new Vec3(-2.6+(i*.6), 0 , 0));
                balls[i].setVelocity(new Vec3(-.01, .02, .03));
                balls[i].setAccel(new Vec3(0, -.03, 0));
            }
        }
        double ttime = (System.currentTimeMillis() - startTime) / 1000.0;

        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);

        for(int i = 0; i < numBalls; i++){
            balls[i].rotate(new Vec3(0, ttime, 0));

            double maxDisp = largeSphere.getRadius() - balls[i].getRadius();
            Vec3 disp = balls[i].getPosition().sub(largeSphere.getPosition());
            if(disp.mag() > maxDisp){
                Vec3 norm_disp = disp.normalize();
                double dot_product = balls[i].getVelocity().dot(norm_disp);
                if(dot_product < 1){
                    dot_product = 1;
                }
                Vec3 v = norm_disp.mul(dot_product * 2);
                balls[i].setVelocity(balls[i].getVelocity().sub(v.mul(.05)));
            }

            balls[i].update();
            balls[i].draw();
        }

        largeSphere.rotate(new Vec3(ttime/2, 0, 0));
        largeSphere.update();
        largeSphere.draw();
    }
}
