public class RayTrace extends MISApplet {
    double t = 0;
    RaytraceRenderer rnd;
    int[][][] pixels;
    double[][] zbuffer;
    LeanSphere s, s2, s3, s4;
    Material mat = new Material();
    Material mat2 = new Material();
    DirectionalLight light = new DirectionalLight(8, 3, 2, 1, 1, 1, .3);

    public void initialize(){
        rnd = new RaytraceRenderer(W, H);

        mat.setAmbient(.1, .1, 0);
        mat.setDiffuse(.2, .2, 0);
        mat.setSpecular(.8, .8, .8, .1);
        mat.setSpecularFocus(30);

        mat2.setAmbient(.1, 0, 0);
        mat2.setDiffuse(.2, .2, 0);
        mat2.setSpecular(.8, .8, 0, .1);
        mat2.setSpecularFocus(10);

        rnd.addLight(light);

        s = new LeanSphere(0, 0, .1, mat, .9);
        rnd.addSphere(s);

        s2 = new LeanSphere(0, 0, 0, mat2, .008);
        rnd.addSphere(s2);

        s3 = new LeanSphere(.1, 0, 1, mat2, .05);
        rnd.addSphere(s3);

        s4 = new LeanSphere(0, 0, 0, mat2, .015);
        rnd.addSphere(s4);

        pixels = new int[H][W][3];
        zbuffer = new double[H][W];
    }

    public void initFrame(double time) {
        t = 5 * time;

        s2.setPosition(.3*Math.sin(.1*time), s2.getPosition().y, 1.6*Math.cos(.1*time));

        light.setDirection(16*Math.sin(time), 5*Math.sin(.5*time), 4);

        for(int i = 0; i < H; i++){
            for(int j = 0; j < W; j++){
                pixels[i][j][0] = 40;
                pixels[i][j][1] = 40;
                pixels[i][j][2] = 40;
                zbuffer[i][j] = Double.MAX_VALUE;
            }
        }

        rnd.render(pixels, zbuffer);
    }

    public void setPixel(int x, int y, int rgb[]) {
        for (int j = 0 ; j < 3 ; j++){
            rgb[j] = pixels[y][x][j];
        }
    }
}
