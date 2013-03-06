import java.awt.*;
import java.util.Random;

public class Spider extends BufferedApplet{
    int w = 1280;
    int h = 700;
    int numLegs = 10;

    Renderer rnd;
    World world = new World();
    Geometry root, body, base, legbase, legupper, legmidjoint, leglower;
    Arm[] legs = new Arm[numLegs];
    Vec3 baseTranslation = new Vec3(0, 0, 0);
    Vec3 baseRotation = new Vec3(0, 0, 0);
    Vec3 lowerRotation = new Vec3(0, 0, 0);
    double bodyScale = 2;

    long startTime = System.currentTimeMillis();
    boolean setupDone = false;

    public void render(Graphics g){
        if(!setupDone){
            setupDone = true;
            rnd = new Renderer(w, h, g, world);
            root = world.getRoot();

            base = new Geometry(0, 0);
            root.add(base);

            body = new Geometry(20, 20);
            body.buildSphere();
            base.add(body);

            for(int i = 0; i < numLegs; i++){
                legs[i] = new Arm();
                base.add(legs[i].getRoot());
            }

        }
        double ttime = (System.currentTimeMillis() - startTime) / 1000.0;

        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);

        base.getMatrix().identity();
        base.getMatrix().rotateY(.2*ttime);
        base.getMatrix().translate(0, Math.sin(ttime), -8);

        body.getMatrix().identity();
        body.getMatrix().rotateX(Math.PI/2);
        body.getMatrix().scale(bodyScale, bodyScale, bodyScale);

        for(int i = 0; i < numLegs; i++){
            double slice = (i*((2*Math.PI)/numLegs));
            baseTranslation.init(2*Math.sin(slice), 0, 2*Math.cos(slice));
            baseRotation.init(0, slice+Math.PI/2, Math.PI/2+Math.sin(ttime));
            lowerRotation.init(0, 0, Math.sin(ttime));
            legs[i].transform(ttime, baseTranslation, baseRotation, lowerRotation);
        }

        rnd.render();
    }
}
