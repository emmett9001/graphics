import java.awt.*;
import java.util.Random;

public class HierarchyTest extends BufferedApplet{
    int w = 1280;
    int h = 700;

    Renderer rnd;
    World world = new World();
    Geometry root, bjoint, base, joint, mid, fjoint, end;

    long startTime = System.currentTimeMillis();
    boolean setupDone = false;

    public void render(Graphics g){
        if(!setupDone){
            setupDone = true;
            rnd = new Renderer(w, h, g, world);
            root = world.getRoot();

            bjoint = new Geometry(0, 0);
            root.add(bjoint);

            base = new Geometry(0, 0);
            base.buildCube();
            bjoint.add(base);

            joint = new Geometry(0, 0);
            bjoint.add(joint);

            mid = new Geometry(0, 0);
            mid.buildCube();
            joint.add(mid);

            fjoint = new Geometry(0, 0);
            joint.add(fjoint);

            end = new Geometry(0, 0);
            end.buildCube();
            fjoint.add(end);
        }
        double ttime = (System.currentTimeMillis() - startTime) / 1000.0;

        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);

        bjoint.getMatrix().identity();
        bjoint.getMatrix().rotateY(ttime);
        bjoint.getMatrix().translate(0, -2, -4);

        base.getMatrix().identity();
        base.getMatrix().scale(.3, 2, .3);

        joint.getMatrix().identity();
        joint.getMatrix().rotateZ(Math.sin(ttime));
        joint.getMatrix().translate(0, 1.7, 0);

        mid.getMatrix().identity();
        mid.getMatrix().translate(0, 1, 0);
        mid.getMatrix().scale(.3, 2, .3);

        fjoint.getMatrix().identity();
        fjoint.getMatrix().rotateZ(Math.sin(ttime));
        fjoint.getMatrix().translate(0, 3.9, 0);

        end.getMatrix().identity();
        end.getMatrix().translate(0, 1, 0);
        end.getMatrix().scale(.3, 1.5, .3);

        rnd.render();
    }
}
