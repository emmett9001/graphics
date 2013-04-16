public class Sphere extends Shape{
    private double radius;

    public Sphere(double x, double y, double z, Material m, double r){
        super(x, y, z, m);
        this.radius = r;
    }
}
