import java.util.*;

public class RaytraceRenderer{
    private ArrayList<Sphere> spheres;

    public RaytraceRenderer(){
        this.spheres = new ArrayList<Sphere>();
    }

    public void addSphere(double x, double y, double z, double r){
        Sphere s = new Sphere(x, y, z, (Material)null, r);
        this.spheres.add(s);
    }

    public void render(int[][][] pix){
        for(Sphere s : this.spheres){
            this._render(s, pix);
        }
    }

    private void _render(Sphere s, int[][][] pix){
    }

    private boolean solveQuadraticEquation(double A, double B, double C, double[] t) {
      double discriminant = B * B - 4 * A * C;
      if (discriminant < 0)
         return false;

      double d = Math.sqrt(discriminant);
      t[0] = (-B - d) / (2 * A);
      t[1] = (-B + d) / (2 * A);
      return true;
   }
}
