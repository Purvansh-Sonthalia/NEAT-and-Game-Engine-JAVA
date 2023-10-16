import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;

public class Luxor extends JFrame
{

    Agent agent;
    double px;
    

    double gx,gy;

    double r;
    double gr;

    double velocity = 25;

    double score = 0;
    boolean dead = false;

    int count=0;

    public Luxor()
    {
        this.setSize(850,650);
        this.setVisible(true);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        reset();
        resetTarget();
    }

    void reset()
    {
        px = getWidth()/2.0;
        dead=false;
        score = 0;
        r = 80;
        gr = 30;
        
        count=0;
    }

    void resetTarget()
    {
        gx = getWidth()*Math.random()/2.0 + getWidth()/4.0;
        gy = getHeight()*Math.random()/2.0;
    }

    void movePlayer()
    {
        double[] out = agent.calculate(px/getWidth(),
                                        gx/getWidth(),
                                        gy/getHeight(),
                                        1.0
                                        );
        px+=velocity*out[0];
        if(out[0]>0.6 || out[0]<-0.6)
            score+=0.2;
        if(px<r)
        {
            px=0;
            score -= 0.2;
        }
        if(px>getWidth()-r)
        {
            px=getWidth();
            score -= 0.2;
        }

        if(out[1]>0)
        {
            shoot();
        }
    }

    void move()
    {
        if(!dead && agent!=null)
        {
            movePlayer();
        }
        count++;
    }

    void shoot()
    {
        //System.out.println("Shot");
        if(px>gx-gr && px<gx+gr)
        {
            score+=7;
            resetTarget();
        }
        else
        {
            score-=0.01;
        }
    }

    void playerDie()
    {
        dead = true;
        agent.setScore(score);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        g.setColor(Color.BLUE);
        g.fillOval((int)(px-r), getHeight()-(int)r, 2*(int)r, 2*(int)r);
        g.setColor(Color.GREEN);
        g.fillOval((int)(gx-gr), (int)(gy-gr), 2*(int)gr, 2*(int)gr);
    }
}
