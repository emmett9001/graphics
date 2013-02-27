import java.awt.*;

public class Sphere{
    private Matrix transform;
    private Renderer renderer;
    private Geometry geometry;

    private double radius;

    private Vec3 position, velocity, acceleration, rotation, scale;

    public Sphere(Renderer r){
        renderer = r;
        geometry = new Geometry(20, 20);
        geometry.buildSphere();

        transform = new Matrix();

        position = new Vec3(0, 0, 0);
        velocity = new Vec3(0, 0, 0);
        acceleration = new Vec3(0, 0, 0);

        rotation = new Vec3(0, 0, 0);
        scale = new Vec3(1, 1, 1);
    }

    public Sphere(Renderer r, double radius){
        this(r);
        this.radius = radius;
        scale = new Vec3(radius, radius, radius);
    }

    public Sphere(Renderer r, int longi, int lati, double radius){
        this(r, radius);
        geometry = new Geometry(longi, lati);
        geometry.buildSphere();
    }

    public void setVelocity(Vec3 v){
        this.velocity = v;
    }

    public Vec3 getVelocity(){
        return this.velocity;
    }

    public void setAccel(Vec3 v){
        this.acceleration = v;
    }

    public Vec3 getAccel(){
        return this.acceleration;
    }
    public void setPosition(Vec3 pos){
        this.position = pos;
    }

    public Vec3 getPosition(){
        return this.position;
    }

    public double getRadius(){
        return this.radius;
    }

    public void rotate(Vec3 rot){
        this.rotation = rot;
    }

    public void update(){
        velocity = velocity.add(acceleration);
        position = position.add(velocity);
    }

    public void draw(){
        transform.identity();
        transform.rotate(rotation);
        transform.scale(scale);
        transform.translate(position);
        renderer.renderGeometry(geometry, transform);
    }
}
