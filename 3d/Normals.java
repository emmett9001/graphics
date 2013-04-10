public class Normals extends MISApplet {
    double t = 0;
    Geometry cube = new Geometry(50, 30);
    Geometry sphere = new Geometry(20, 20);
    Geometry planet = new Geometry(20, 20);
    Geometry root, ballJoint;
    Renderer rnd;
    World world = new World();
    int[][][] pixels;
    double[][] zbuffer;

    public void initialize(){
        rnd = new Renderer(W, H, world);
        rnd.colorNormals(true);
        root = world.getRoot();
        pixels = new int[H][W][3];
        zbuffer = new double[H][W];

        ballJoint = new Geometry(0, 0);
        root.add(ballJoint);

        cube.buildTorus();
        ballJoint.add(cube);

        sphere.buildSphere();
        ballJoint.add(sphere);

        planet.buildSphere();
        ballJoint.add(planet);
    }

    public void initFrame(double time) {
        t = 5 * time;

        ballJoint.getMatrix().identity();
        ballJoint.getMatrix().translate(0, 0, -3);

        cube.getMatrix().identity();
        cube.getMatrix().rotateY(2*time);
        cube.getMatrix().scale(2, 1, 1);

        sphere.getMatrix().identity();
        sphere.getMatrix().scale(2, 2, 2);
        sphere.getMatrix().rotateY(time);

        planet.getMatrix().identity();
        planet.getMatrix().translate(5, 0, 5);
        planet.getMatrix().scale(.5, .5, .5);
        planet.getMatrix().rotateY(-1*time);

        for(int i = 0; i < H; i++){
            for(int j = 0; j < W; j++){
                pixels[i][j][0] = 0;
                pixels[i][j][1] = 0;
                pixels[i][j][2] = 0;
                zbuffer[i][j] = -1*rnd.getFocalLength();
            }
        }

        rnd.renderZ(pixels, zbuffer);
    }

    public void setPixel(int x, int y, int rgb[]) {
        for (int j = 0 ; j < 3 ; j++){
            rgb[j] = pixels[y][x][j];
        }
    }
}
