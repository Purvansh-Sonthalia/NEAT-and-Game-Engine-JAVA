import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    private Genome genome;

    public Panel() {
    }

    public Genome getGenome() {
        return genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0,0,10000,10000);
        g.setColor(Color.black);
        g.fillRect(0,0,10000,10000);

        for(ConnectionGene c:genome.getConnections().getDataList()){
            paintConnection(c, (Graphics2D) g);
        }


        for(NodeGene n:genome.getNodes().getDataList()) {
            paintNode(n, (Graphics2D) g);
        }

    }

    private void paintNode(NodeGene n, Graphics2D g){
        g.setColor(Color.gray);
        g.setStroke(new BasicStroke(3));
        g.drawOval((int)(this.getWidth() * n.getX()) - 10,
                (int)(this.getHeight() * n.getY()) - 10,20,20);
    }

    private void paintConnection(ConnectionGene c, Graphics2D g){
        //g.setColor(c.isEnabled() ? Color.green:Color.red);
        if(!c.isEnabled())
            g.setColor(Color.RED);
        else
        {
            double temp = c.getWeight()/Neat.WEIGHT_RANGE;
            temp = (1+temp)/2;
            if(temp>0.995)
                temp = 0.995;
            if(temp<0.005)
                temp = 0.005;
            g.setColor(new Color(0,(int)(255*temp),(int)(255*(1-temp))));
        }
        g.setStroke(new BasicStroke(3));
        g.drawString(new String(c.getWeight() + "       ").substring(0,7),
                (int)((c.getEnd().getX() + c.getStart().getX())* 0.5 * this.getWidth()),
                (int)((c.getEnd().getY() + c.getStart().getY())* 0.5 * this.getHeight()) +15);
        g.drawLine(
                (int)(this.getWidth() * c.getStart().getX()),
                (int)(this.getHeight() * c.getStart().getY()),
                (int)(this.getWidth() * c.getEnd().getX()),
                (int)(this.getHeight() * c.getEnd().getY()));
    }

}