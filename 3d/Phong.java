public class Phong extends MISApplet {
    double t = 0;
    Geometry planet = new Geometry(20, 20);
    Geometry root;
    Renderer rnd;
    World world = new World();
    Material mat = new Material();
    DirectionalLight light = new DirectionalLight(1, 1, 1, 1, 1, 1, .5);
    int[][][] pixels;
    double[][] zbuffer;

    public void initialize(){
        rnd = new Renderer(W, H, world);
        root = world.getRoot();
        pixels = new int[H][W][3];
        zbuffer = new double[H][W];

        mat.setAmbient(1, 0, 0);
        mat.setDiffuse(1, 0, 0);
        mat.setSpecular(1, 1, 1, .5);

        rnd.addLight(light);

        planet.buildSphere();
        planet.setMaterial(mat);
        root.add(planet);
    }

    public void initFrame(double time) {
        t = 5 * time;

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
