import java.awt.*;
import java.util.Random;

public class GeometryTest extends BufferedApplet{
    int w = 1280;
    int h = 700;

    Geometry torus = new Geometry(30, 10);
    Geometry sphere = new Geometry(20, 20);
    Matrix ball = new Matrix();
    Matrix planet = new Matrix();
    Matrix ring = new Matrix();
    Renderer rnd;

    long startTime = System.currentTimeMillis();
    boolean setupDone = false;

    public void render(Graphics g){
        if(!setupDone){
            setupDone = true;
            rnd = new Renderer(w, h, g);
            sphere.buildSphere();
            torus.buildTorus(15, .5);
        }
        double ttime = (System.currentTimeMillis() - startTime) / 1000.0;

        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);

        ball.identity();
        ball.rotateY(7*ttime);
        ball.rotateZ(5*ttime);
        ball.translate(2*Math.sin(.1*ttime), 0, 0);
        rnd.renderGeometry(sphere, ball);

        planet.identity();
        planet.scale(.1, .1, .1);
        planet.parent(ball);
        planet.translate(2*Math.sin(ttime), 0, 2*Math.cos(ttime));
        planet.rotateX(1.2);
        planet.rotateY(1);
        rnd.renderGeometry(sphere, planet);

        for(int i = 1; i <= 4; i++){
            ring.identity();
            ring.scale(.1+(.03*i), .1+(.03*i), .1+(.03*i));
            ring.rotateX((8 - i) * .01 * ttime);
            ring.rotateY((8 - i) * .05 * ttime);
            ring.rotateZ((8 - i) *.02 * ttime);
            ring.parent(ball);
            rnd.renderGeometry(torus, ring);
        }
    }
}
