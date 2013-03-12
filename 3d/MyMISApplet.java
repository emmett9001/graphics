public class MyMISApplet extends MISApplet {
    double t = 0;
    Geometry cube = new Geometry(0, 0);
    Renderer rnd = new Renderer();

    public void initialize(){
        cube.buildCube();
    }

    public void initFrame(double time) {
       t = 5 * time;

       cube.getMatrix().identity();

       rnd.renderScanConvertedGeometry(cube, cube.getMatrix());
    }

    public void setPixel(int x, int y, int rgb[]) {
       double fx = ((double)x - W/2) / W;
       double fy = ((double)y - H/2) / H;
       for (int j = 0 ; j < 3 ; j++){
          rgb[j] = (int)((90 * j) + 128 * Math.sin(20 * fx) * Math.cos(10 * fy));
       }
    }
}
