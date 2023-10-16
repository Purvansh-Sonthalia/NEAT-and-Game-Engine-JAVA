import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;

public class CarTarget extends Game
{
    GameObject p;

    GameObject g;

    //ArrayList<Ray> rays = new ArrayList<>();

    double velocity = 20;

    public CarTarget()
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
                CarTarget.this.movePlayer();
            }
        };
        p.isCircle = false;
        p.color = Color.RED;
        


        g = new GameObject();
        g.isCircle = true;
        g.color = Color.YELLOW;
        //g.fill = false;
        g.shape.clear();
        g.shape.add(new Vector2(30, -30));
        g.shape.add(new Vector2(30, 30));
        g.shape.add(new Vector2(-30, 30));
        g.shape.add(new Vector2(-30, -30));
        
        reset();
        resetTarget();

        

        children.add(p);
        children.add(g);
    }

    public void reset()
    {
        p.position = new Vector2(getWidth()*Math.random(), getHeight()*Math.random());
        p.shape.clear();
        p.shape.add(new Vector2(0, -15));
        p.shape.add(new Vector2(15, 0));
        p.shape.add(new Vector2(0, 15));
        p.shape.add(new Vector2(-15, 0));
        p.forward = new Vector2(0, -1);
        p.rotateClockwise(Math.random()*2*Math.PI);


        dead=false;
        score = 0;
        p.radius = 15;
        g.radius = 30;

        
    }

    void resetTarget()
    {
        g.position = new Vector2(getWidth()*Math.random(), getHeight()*Math.random());
    }

    void movePlayer()
    {
        double in[] = new double[6];
        for (int i = -2; i <= 2; i++)
        {
            Ray ray = new Ray(p.position, p.forward.rotateClockwise(i*Math.PI/6), 100);
            Vector2 cp = ray.checkCollision(g);
            if(cp==null)
            {
                cp = ray.checkCollision(getWidth(), getHeight());
                if(cp==null)
                {
                    in[i+2] = 0;
                }
                else
                {
                    in[i+2]=-p.position.distanceFrom(cp)/ray.length;
                }
            }
            else
            {
                in[i+2]=p.position.distanceFrom(cp)/ray.length;
            }
        }

        in[5]=1;
        
        double[] out = agent.calculate(in);

        p.rotateClockwise(0.125*out[0]);

        p.position = p.position.add(p.forward.multiply(velocity*(1+out[1])/2));
        
        if(p.position.x < 0 || p.position.x>getWidth() || p.position.y<0 || p.position.y>getHeight())
        {
            playerDie();
        }

        CollisionPoint collp = p.collisionDetection(g);
        if(collp!=null)
        {
            score += 5;
            resetTarget();
        }

        //score+=(g.position.subtract(p.position).normalized().dotProduct(p.forward))*0.0001;
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

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        g.setColor(Color.GREEN);
        g.drawLine((int)p.position.x, (int)p.position.y, (int)(p.position.x+p.forward.x*100), (int)(p.position.y+p.forward.y*100));
        
        /*for (Ray r : rays)
        {
            Vector2 coll = r.checkCollision(this.g);
            if(coll == null)
                coll = r.checkCollision(getWidth(), getHeight());
            if(coll == null)
            {
                g.setColor(Color.GREEN);
                g.drawLine((int)r.startPoint.x, (int)r.startPoint.y, (int)(r.startPoint.x+r.direction.x*r.length), (int)(r.startPoint.y+r.direction.y*r.length));
            }
            else
            {
                g.setColor(Color.BLUE);
                g.drawLine((int)r.startPoint.x, (int)r.startPoint.y, (int)(coll.x), (int)(coll.y));
            }
        }*/
    }
}
