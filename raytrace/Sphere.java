public class Sphere extends Shape{
    private double radius;
    private double[] arr;

    public Sphere(double x, double y, double z, Material m, double r){
        super(x, y, z, m);
        this.radius = r;
        this.arr = new double[4];
    }

    public void setPosition(double x, double y, double z){
        this.getPosition().init(x, y, z);
    }

    public double[] getArray(){
        this.arr[0] = this.getPosition().x;
        this.arr[1] = this.getPosition().y;
        this.arr[2] = this.getPosition().z;
        this.arr[3] = this.radius;
        return this.arr;
    }
}
