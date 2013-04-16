public class Shape{
    private Vec3 position;
    private Material material;

    public Shape(double x, double y, double z, Material m){
        this.position = new Vec3(x, y, z);
        this.material = m;
    }
}
