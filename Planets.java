import java.awt.*;
import java.util.Random;
import java.lang.Math;

public class Planets extends BufferedApplet{
    boolean started = false;
    final int numPlanets = 1000;
    Random gen = new Random();

    Circle planet2 = new Circle(600, 350, 60, Color.BLUE);
    Circle[] planets = new Circle[numPlanets];

    public void render(Graphics g){
        if(!started){
            started = true;
            planet2.SetAccel(new Vec2(0, 0));
            planet2.SetDensity(200);

            for(int i = 0; i < numPlanets; i++){
                Color ballColor = new Color(Math.abs(gen.nextInt()) % 200, Math.abs(gen.nextInt()) % 200, Math.abs(gen.nextInt()) % 200);
                planets[i] = new Circle(200, 100,
                    13 + Math.abs(gen.nextInt() % 10), ballColor);
                planets[i].SetDensity(1);
                planets[i].SetVelocity(new Vec2(1 + gen.nextInt() % 3, 1 + gen.nextInt() % 3));
            }
        }

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, bounds().width, bounds().height);

        planet2.update();
        planet2.draw(g);

        for(int i = 0; i < numPlanets; i++){
            planets[i].update();
            planets[i].draw(g);

            Vec2 dispFrom1To2 = planet2.GetCenter().sub(planets[i].GetCenter());

            Vec2 force = dispFrom1To2.normalize().mul(planet2.GetMass()).mul(planets[i].GetMass());
            force = force.div(dispFrom1To2.mag()*dispFrom1To2.mag());

            planets[i].SetAccel(new Vec2(force.x, force.y));

            if(dispFrom1To2.mag() < planets[i].GetRadius() + planet2.GetRadius()){
                double dot_product = planets[i].GetVelocity().dot(dispFrom1To2.normalize());
                if(dot_product < 1){
                    dot_product = 1;
                }
                Vec2 v = dispFrom1To2.normalize().mul(dot_product * 2);
                planets[i].SetVelocity(planets[i].GetVelocity().sub(v));
            }
        }
    }
}
