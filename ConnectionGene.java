public class ConnectionGene extends Gene
{
    private NodeGene start;
    private NodeGene end;

    private double weight;
    private boolean enabled=true;

    public int childNodeInnovationNumber=-1;

    public ConnectionGene(NodeGene from, NodeGene to)
    {
        this.start = from;
        this.end = to;

        this.weight = 4*Math.random()-2;
    }

    public NodeGene getStart()
    {return start;}

    public void setStart(NodeGene from)
    {this.start = from;}

    public NodeGene getEnd()
    {return end;}

    public void setEnd(NodeGene to)
    {this.end = to;}

    public double getWeight()
    {return weight;}

    public void setWeight(double w)
    {this.weight = w;}

    public boolean isEnabled()
    {return enabled;}

    public void setEnabled(boolean e)
    {this.enabled = e;}


    public boolean equals(Object obj)
    {
        if(obj instanceof ConnectionGene)
        {
            ConnectionGene cg = (ConnectionGene)obj;
            return (cg.getStart().equals(this.start)) && (cg.getEnd().equals(this.end));
        }
        return false;
    }

    public int hashCode()
    {
        return start.getInnovationNumber() * Neat.MAX_NODES + end.getInnovationNumber();
    }
}
