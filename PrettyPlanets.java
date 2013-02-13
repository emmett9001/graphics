import java.awt.*;
import java.util.Random;
import java.lang.Math;

public class PrettyPlanets extends BufferedApplet{
    boolean started = false;
    final int numPlanets = 200;
    final int numStars = 800;
    final int numSunDisks = 5;
    Random gen = new Random();

    Color sunColor = new Color(255, 255, 0);
    Circle planet2 = new Circle(600, 350, 55, sunColor);
    Circle[] planets = new Circle[numPlanets];
    Circle[] stars = new Circle[numStars];
    Color bgColor = new Color(0, 0, 20);
    int lum = 195 + Math.abs(gen.nextInt()) % 60;
    Color starColor = new Color(lum, lum, lum);

    Polygon[] sunGlare = new Polygon[numSunDisks];

    long startTime = System.currentTimeMillis();

    public void render(Graphics g){
        if(!started){
            started = true;
            planet2.SetAccel(new Vec2(0, 0));
            planet2.SetDensity(300);

            for(int i = 0; i < numPlanets; i++){
                Color ballColor = new Color(Math.abs(gen.nextInt()) % 200,
                                            Math.abs(gen.nextInt()) % 200,
                                            Math.abs(gen.nextInt()) % 200);
                planets[i] = new Circle(200, 100,
                    8 + Math.abs(gen.nextInt() % 5), ballColor);
                planets[i].SetDensity(2 + gen.nextInt() % 3);
                planets[i].SetVelocity(new Vec2(1 + gen.nextInt() % 2, 3));
            }

            for(int i = 0; i < numStars; i++){
                stars[i] = new Circle(Math.abs(gen.nextInt()) % bounds().width,
                                      Math.abs(gen.nextInt()) % bounds().height,
                                      1 + Math.abs(gen.nextInt()) % 3, starColor);
            }

            for(int i = 0; i < numSunDisks; i++){
                Color glareRingColor = new Color((40 * i) + 80, (40 * i) + 80, 0);
                sunGlare[i] = new Polygon(glareRingColor, new Vec2(655, 405), 30 * (5 - i));
            }
        }

        double ttime = (System.currentTimeMillis() - startTime) / 1000.0;

        g.setColor(bgColor);
        g.fillRect(0, 0, bounds().width, bounds().height);

        for(int i = 0; i < numStars; i++){
            stars[i].SetRadius(Math.abs(gen.nextInt() % 3));
            stars[i].draw(g);
        }

        for(int i = 0; i < numSunDisks; i++){
            sunGlare[i].build(
                (int)((10 + (gen.nextInt() % 5))*(2+i)),
                Math.sin(ttime),
                57 + 15*(numSunDisks-i));
            sunGlare[i].draw(g);
        }

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
