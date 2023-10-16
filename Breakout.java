import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;

public class Breakout extends Game
{
    GameObject p;
    GameObject b;
    Vector2 v;

    GameObject g;

    double velocity = 25;

    boolean noSizeChange = true;

    public Breakout()
    {
        this.setSize(800,600);
        this.setVisible(true);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        p = new GameObject(){
            @Override
            public void Update()
            {
                Breakout.this.movePlayer();
            }
        };
        p.isCircle = true;
        p.color = Color.BLUE;
        p.shape.add(new Vector2(100, -80));
        p.shape.add(new Vector2(100, 0));
        p.shape.add(new Vector2(-100, 0));
        p.shape.add(new Vector2(-100, -80));
        //p.shape.add(new Vector2(0, -120));

        b = new GameObject(){
            @Override
            public void Update()
            {
                Breakout.this.moveBall();
            }
        };
        b.isCircle = false;
        b.color = Color.RED;
        b.shape.add(new Vector2(0, -15));
        b.shape.add(new Vector2(15, 0));
        b.shape.add(new Vector2(0, 15));
        b.shape.add(new Vector2(-15, 0));

        g = new GameObject();
        g.isCircle = true;
        g.color = Color.GREEN;
        //g.fill = false;
        g.shape.add(new Vector2(30, -30));
        g.shape.add(new Vector2(30, 30));
        g.shape.add(new Vector2(-30, 30));
        g.shape.add(new Vector2(-30, -30));
        
        reset();
        resetTarget();

        children.add(p);
        children.add(b);
        children.add(g);
    }

    public void reset()
    {
        p.position = new Vector2(getWidth()/2.0, getHeight());
        b.position = new Vector2(getWidth()*Math.random(), getHeight()/2.0);
        double theta = (0.5+Math.random()) * Math.PI/2;
        v = (new Vector2(0, -1)).multiply(velocity).rotateClockwise(theta);
        dead=false;
        score = 0;
        p.radius = 100;
        b.radius = 15;
        g.radius = 30;
    }

    void resetTarget()
    {
        g.position = new Vector2(getWidth()*Math.random(), getHeight()*Math.random()/2.0);
    }

    void movePlayer()
    {
        double[] out = agent.calculate(p.position.x/getWidth(),
                                        b.position.x/getWidth(),
                                        b.position.y/getHeight(),
                                        v.x/velocity,
                                        v.y/velocity,
                                        g.position.x/getWidth(),
                                        g.position.y/getHeight(),
                                        1.0
                                        );
        p.position.x+=2*velocity*out[0];
        /*if(out[0]>0.6 || out[0]<-0.6)
            score+=0.01;*/
        if(p.position.x<0)
        {
            p.position.x=0;
            //score -= 0.2;
        }
        if(p.position.x>getWidth())
        {
            p.position.x=getWidth();
            //score -= 0.2;
        }
    }

    void moveBall()
    {
        b.position = b.position.add(v);

        
        if(b.position.x<0)
        {
            b.position.x=0;
            v.x=-v.x;
        }
        if(b.position.x>getWidth())
        {
            b.position.x=getWidth();
            v.x=-v.x;
        }
        if(b.position.y<0)
        {
            b.position.y=0;
            v.y=-v.y;
        }

        CollisionPoint cp = p.collisionDetection(b);
        if(cp!=null)
        {
            int tmp = b.position.subtract(p.position).dotProduct(cp.normal)>0?1:-1;
            
            b.position = cp.position.add(cp.normal.multiply(tmp*b.radius));
            

            /*v = t.mulpiply(velocity);*/

            Vector2 k = cp.normal;

            double c = v.dotProduct(k);

            v = v.subtract(k.multiply(2*c/k.sqMagnitude()));

            v = v.normalized().multiply(velocity);

            v.x += Math.random()*10;

            //score += 5;
        }
        
        cp = g.collisionDetection(b);
        if(cp!=null)
        {
            int tmp = b.position.subtract(g.position).dotProduct(cp.normal)>0?1:-1;
            b.position = cp.position.add(cp.normal.multiply(tmp*b.radius));
            

            /*v = t.mulpiply(velocity);*/

            Vector2 k = cp.normal;

            double c = v.dotProduct(k);

            v = v.subtract(k.multiply(2*c/k.sqMagnitude()));

            v = v.normalized().multiply(velocity);

            score += 7;
            resetTarget();
        }

        if(b.position.y>getHeight())
        {
            playerDie();
        }

        if(Math.abs(v.x)<Math.abs(v.y))
            if(Math.abs(v.x/v.y) < 0.1)
                v.x+=velocity/5;

        if(Math.abs(v.x)>Math.abs(v.y))
            if(Math.abs(v.y/v.x) < 0.1)
                v.y+=velocity/5;

    }

    public void playerDie()
    {
        dead = true;
        agent.setScore(score);
    }

    public void Update()
    {
        if(!dead && agent!=null)
        {
            for (GameObject gObject : children)
            {
                gObject.Update();
            }
        }
    }
}
