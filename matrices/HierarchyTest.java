import java.awt.*;
import java.util.Random;

public class HierarchyTest extends BufferedApplet{
    int w = 1280;
    int h = 700;

    Renderer rnd;
    World world = new World();
    Geometry root, ball, box, ball2;

    long startTime = System.currentTimeMillis();
    boolean setupDone = false;

    public void render(Graphics g){
        if(!setupDone){
            setupDone = true;
            rnd = new Renderer(w, h, g, world);
            root = world.getRoot();

            ball = new Geometry(12, 12);
            ball.buildSphere();
            root.add(ball);

            box = new Geometry(0, 0);
            box.buildCube();
            ball.add(box);

            ball2 = new Geometry(5, 5);
            ball2.buildSphere();
            ball.add(ball2);
        }
        double ttime = (System.currentTimeMillis() - startTime) / 1000.0;

        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);

        ball.getMatrix().rotateY(.1);

        box.getMatrix().identity();

        ball2.getMatrix().identity();
        ball2.getMatrix().translate(4, Math.sin(50*ttime), 0);

        rnd.render();
    }
}
