import java.awt.*;
import java.util.Random;

public class Circle{
    double x, y, radius, density;
    Vec2 pos, vel, accel;
    Color color;
    String text;
    int textOpacity = 0;

    public Circle(double x, double y, Color c){
        pos = new Vec2(x, y);
        vel = new Vec2(0,0);
        accel = new Vec2(0,0);
        radius = 20;
        density = 1;
        this.color = c;
        this.text = "";
    }

    public Circle(double x, double y, double radius, Color c){
        pos = new Vec2(x, y);
        vel = new Vec2(0,0);
        accel = new Vec2(0,.9);
        density = 1;
        this.radius = radius;
        this.color = c;
        this.text = "";
    }

    public void SetDensity(double den){
        this.density = den;
    }

    public void SelectRandomString(){
        String[] ilus = {
            "I love you", "I love you","I love you","I love you","I love you",
            "Ich liebe dich", "Te dua", "Maite zaitut", "Wo ie ni",
            "Jeg elsker dig", "S'agapo", "Ya lyublyu tebya ", "Te quiero",
            "Phom Rak Khun "
        };
        Random gen = new Random();
        this.text = ilus[Math.abs(gen.nextInt()) % ilus.length];
    }

    public void SetString(String s){
        this.text = s;
    }

    public double GetMass(){
        return this.density*this.radius*.2;
    }

    public Vec2 GetPosition(){
        return pos;
    }

    public void SetPosition(Vec2 pos){
        this.pos = pos;
    }

    public void SetVelocity(Vec2 vel){
        this.vel = vel;
    }

    public Vec2 GetVelocity(){
        return this.vel;
    }

    public void SetAccel(Vec2 acc){
        this.accel = acc;
    }

    public Vec2 GetAccel(){
        return this.accel;
    }

    public Vec2 GetCenter(){
        Vec2 cen = new Vec2(pos.x + this.radius, pos.y + this.radius);
        return cen;
    }

    public void SetRadius(double r){
        this.radius = r;
    }

    public void SetTextOpacity(int op){
        textOpacity = op;
        if(textOpacity < 0){
            textOpacity = 0;
        }
    }

    public int GetTextOpacity(){
        return textOpacity;
    }

    public double GetRadius(){
        return this.radius;
    }

    public double GetDiam(){
        return this.radius*2;
    }

    public void update(){
        vel.x += accel.x;
        vel.y += accel.y;

        pos.x += vel.x;
        pos.y += vel.y;
    }

    public void draw(Graphics g){
        g.setColor(color);
        g.fillOval((int)pos.x, (int)pos.y, (int)radius*2, (int)radius*2);

        Color tColor = new Color(255, 255, 255, this.textOpacity);
        g.setColor(tColor);
        Font f = new Font("Courier", Font.BOLD, 25);
        g.setFont(f);
        g.drawString(text, (int)(pos.x+radius), (int)pos.y);
    }
}
