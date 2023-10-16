import java.awt.Color;
import java.awt.Graphics;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class Test extends JFrame
{
    GameObject go = null;
    Ray r = null;

    @Override
    public void paint(Graphics g)
    {
        // TODO Auto-generated method stub
        super.paint(g);

        if(go!=null)
            go.draw(g);

        if(r!=null)
        {
            Vector2 coll = r.checkCollision(go);
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
                System.out.println(coll.distanceFrom(r.startPoint));
            }
        }
    }

    public Test()
    {
        this.setSize(850,650);
        this.setVisible(true);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public static void main(String[] args)
    {
        Test t = new Test();
        t.go = new GameObject();
        t.go.position = new Vector2(t.getWidth()/2.0, t.getHeight()/2.0);
        t.go.isCircle = true;
        t.go.radius = 100;

        t.go.shape.add(new Vector2(-100, -100));
        t.go.shape.add(new Vector2(0, -200));
        t.go.shape.add(new Vector2(100, -100));
        t.go.shape.add(new Vector2(100, 100));
        t.go.shape.add(new Vector2(0, 200));
        t.go.shape.add(new Vector2(-100, 100));

        t.go.color = Color.RED;

        t.go.forward = new Vector2(0, 1);

        t.r = new Ray(new Vector2(t.getWidth()/2.0, 9*t.getHeight()/10.0), new Vector2(0,-1), 200);

        Timer timer = new Timer(20, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e)
            {
                t.go.rotateClockwise(0.05);
                t.repaint();
                t.r.rotateClockwise(0.015);
                
            }
            
        });
        timer.start();
    }
}
