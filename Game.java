import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

public abstract class Game extends JFrame
{
    ArrayList<GameObject> children = new ArrayList<>();
    Agent agent;
    double score = 0;
    boolean dead = false;
    
    public abstract void Update();
    public abstract void reset();
    public abstract void playerDie();

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        for (GameObject gameObject : children)
        {
            gameObject.draw(g);
        }
    }
}
