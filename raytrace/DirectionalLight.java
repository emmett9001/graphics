public class DirectionalLight{
    private double power;
    private Vec3 direction;
    private Vec3 color;

    public DirectionalLight(double x, double y, double z, double r,
                            double g, double b, double p){
        this.direction = new Vec3(x, y, z);
        this.color = new Vec3(r, g, b);
        this.power = p;
    }

    public Vec3 getDirection(){
        return this.direction;
    }

    public double getPower(){
        return this.power;
    }

    public void setPower(double p){
        this.power = p;
    }
}
