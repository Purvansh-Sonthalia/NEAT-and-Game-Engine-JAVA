import java.util.ArrayList;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;


public class FlappyBird extends JFrame
{
    Agent agent;
    double ax,ay;
    double r = 25;
    Pipe[] pipes=new Pipe[2];
    int score=0;
    int currentPipe=0;
    double velocity = 0;

    boolean dead = false;

    public class Pipe
    {
        double px,py;
        double width = 50;
        double heightSpace = 75;
        FlappyBird parent;
        double velocity = 20;

        boolean disabled = false;

        void reset()
        {
            px = parent.getWidth()+width;
            py = Math.random()*parent.getHeight()/2.0 + parent.getHeight()/4.0;
            disabled = false;
        }

        boolean checkCollision()
        {
            boolean flag =false;

            if(parent.ax+parent.r > px-width && parent.ax-parent.r < px+width)
            {
                if(!(parent.ay-parent.r>py-heightSpace && parent.ay+parent.r<py+heightSpace))
                {
                    flag = true;
                }
            }

            return flag;
        }

        public Pipe(FlappyBird p)
        {
            parent = p;
            px = parent.getWidth()+width;
            py = Math.random()*parent.getHeight()/2.0 + parent.getHeight()/4.0;
        }

        void draw(Graphics g)
        {
            if(!disabled)
            {
                g.setColor(Color.GREEN);
            g.fillRect((int)(px-width), 0, (int)(2*width), (int)(py-heightSpace));
            g.fillRect((int)(px-width), (int)(py+heightSpace), (int)(2*width), parent.getHeight());
            }
        }

        void move()
        {
            if(!disabled)
                px-=velocity;
        }
    }

    public FlappyBird()
    {
        this.setSize(850,650);
        this.setVisible(true);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        reset();
    }

    void reset()
    {
        ax = getWidth()/2.0;
        ay = getWidth()/2.0;
        this.score=0;

        pipes[0] = new Pipe(this);
        pipes[1] = new Pipe(this);
        pipes[1].disabled = true;

        currentPipe = 0;

        dead = false;
        velocity = 0;
    }

    void Update()
    {
        
        score++;
        for (int j = 0; j < 2; j++)
        {
            
            pipes[j].move();

            if(pipes[j].px<-pipes[j].width)
            {
                pipes[j].reset();
            }

        }

        if(pipes[currentPipe].px+pipes[currentPipe].width<ax-r)
            {
                currentPipe = 1-currentPipe;
                pipes[currentPipe].disabled = false;
            }

            movePlayer();
    }

    void movePlayer()
    {
        velocity += 5;
        double out[] = agent.calculate(ay/getHeight(),
                                        ax/getWidth(),
                                        velocity/20.0,
                                        pipes[currentPipe].py/getHeight(),
                                        pipes[currentPipe].px/getWidth(),
                                        1.0
                                        );
        
        if(out[0]>0)
            velocity += -10*(1+out[0]);
        ay+=velocity;
        

        if(ay<r || ay>getHeight()-r)
            playerDie();

        
        for (Pipe p : pipes)
        {
            if(p.checkCollision())
            {
                playerDie();
                return;
            } 
        }
        
        }

    void playerDie()
    {
        dead = true;
        agent.setScore(score);
    }

    @Override
    public void paint(Graphics g) {
        // TODO Auto-generated method stub
        super.paint(g);

        g.setColor(Color.BLUE);
        g.fillOval((int)(ax-r), (int)(ay-r), 2*(int)r, 2*(int)r);

        for (Pipe pipe : pipes)
        {
            pipe.draw(g);
        }
    }
}