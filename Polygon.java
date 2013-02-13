import java.awt.*;

public class Polygon{
    public enum Shape{
        PENTA, HEXA, SEPTA, OCTO, NONA, DECA
    }
    private int[] xCoords;
    private int[] yCoords;
    private Color color;
    private Vec2 position;
    private int subdivisions;
    private double angle;
    private double radius;

    public Polygon(Color c, Vec2 position, double radius){
        this.color = c;
        this.radius = radius;
        this.position = position;
        build(8, 2.3f, radius);
    }

    public Polygon(){
        this.color = Color.BLUE;
        position = new Vec2(300, 300);
        build(8, 2.3f, radius);
    }

    public void setRadius(double r){
        this.radius = r;
    }

    public void build(int subdivisions, double angle, double radius){
        this.radius = radius;
        this.angle = angle;
        this.subdivisions = subdivisions;
        xCoords = new int[subdivisions];
        yCoords = new int[subdivisions];

        double angleDelta = (2*Math.PI)/subdivisions;

        for(int i = 0; i < subdivisions; i++){
            xCoords[i] = (int)position.x + (int)(this.radius * Math.cos(angle));
            yCoords[i] = (int)position.y + (int)(this.radius * Math.sin(angle));
            angle += angleDelta;
        }
    }

    public void draw(Graphics g){
        g.setColor(color);
        g.fillPolygon(xCoords, yCoords, subdivisions);
    }
}
