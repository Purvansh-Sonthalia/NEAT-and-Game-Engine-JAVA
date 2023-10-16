public class Genome
{
    private RandomAccessSet<ConnectionGene> connections = new RandomAccessSet<>();
    private RandomAccessSet<NodeGene> nodes = new RandomAccessSet<>();

    private Neat neat;

    public Genome(Neat n)
    {
        this.neat = n;
    }

    public double distance(Genome g2)
    {
        Genome g1 = this;

        int temp1=0,temp2=0;
        if(g1.getConnections().size()!=0)
            temp1 = g1.getConnections().getDataAt(g1.getConnections().size()-1).getInnovationNumber();
        if(g2.getConnections().size()!=0)
            temp2 = g2.getConnections().getDataAt(g2.getConnections().size()-1).getInnovationNumber();

        if(temp1<temp2)
        {
            g1 = g2;
            g2 = this;
        }

        int index1 = 0;
        int index2 = 0;

        int disjoint = 0;
        int excess = 0;
        double weightDifference = 0;
        int similar = 0;

        while(index1 < g1.getConnections().size() && index2 < g2.getConnections().size())
        {
            ConnectionGene gene1 = g1.getConnections().getDataAt(index1);
            ConnectionGene gene2 = g2.getConnections().getDataAt(index2);

            int in1 = gene1.getInnovationNumber();
            int in2 = gene2.getInnovationNumber();

            if(in1==in2)
            {
                index1++;
                index2++;
                similar++;
                weightDifference += Math.abs(gene1.getWeight() - gene2.getWeight());
            }
            else if(in1>in2)
            {
                index2++;
                disjoint++;
            }
            else
            {
                index1++;
                disjoint++;
            }
        }
        excess = g1.getConnections().size() - index1;
        weightDifference /= similar==0?1:similar;

        double N = Math.max(g1.getConnections().size(),g2.getConnections().size());

        N = N<20?1:N;

        return (Neat.C1*disjoint+Neat.C2*excess)/N + Neat.C3*weightDifference;
    }

    public static Genome crossBreed(Genome g1, Genome g2)
    {
        Neat neat = g1.getNeat();
        Genome child = neat.createEmptyGenome();

        int index1 = 0;
        int index2 = 0;
        
        while(index1 < g1.getConnections().size() && index2 < g2.getConnections().size())
        {          
            ConnectionGene gene1 = g1.getConnections().getDataAt(index1);
            ConnectionGene gene2 = g2.getConnections().getDataAt(index2);

            int in1 = gene1.getInnovationNumber();
            int in2 = gene2.getInnovationNumber();

            if(in1==in2)
            {
                index1++;
                index2++;

                ConnectionGene tmp = Neat.cloneConnectionGene(Math.random()>0.5?gene1:gene2);
                tmp.setWeight(Lerp(gene1.getWeight(),gene2.getWeight(),Math.random()));
                child.getConnections().add(tmp);
            }
            else if(in1>in2)
            {
                index2++;
            }
            else
            {
                child.getConnections().add(Neat.cloneConnectionGene(gene1));
                index1++;
            }
        }
        
        while(index1 < g1.getConnections().size())
        {
            ConnectionGene gene1 = g1.getConnections().getDataAt(index1);
            child.getConnections().add(Neat.cloneConnectionGene(gene1));
            index1++;
        }

        for(ConnectionGene c:child.getConnections().getDataList())
        {
            child.getNodes().add(c.getStart());
            child.getNodes().add(c.getEnd());
        }

        return child;
    }

    public RandomAccessSet<ConnectionGene> getConnections()
    {
        return connections;
    }

    public RandomAccessSet<NodeGene> getNodes()
    {
        return nodes;
    }

    public Neat getNeat()
    {
        return neat;
    }

    public void mutate()
    {
        if(Math.random()<Neat.PROBABILITY_NEW_CONNECTION)
            mutateLink();
        if(Math.random()<Neat.PROBABILITY_NEW_NODE)
            mutateNode();
        if(Math.random()<Neat.PROBABILITY_WEIGHT_SHIFT)
            mutateWeightShift();
        if(Math.random()<Neat.PROBABILITY_WEIGHT_RANDOMIZE)
            mutateWeightRandom();
        if(Math.random()<Neat.PROBABILITY_TOGGLE_CONNECTION)
            mutateToggleLink();
    }

    public void mutateLink()
    {
        for (int i = 0; i < 100; i++)
        {
            NodeGene a = nodes.getRandomElement();
            NodeGene b = nodes.getRandomElement();

            if(a.getX() == b.getX())
                continue;
            
            ConnectionGene c;
            if(a.getX()<b.getX())
                c = new ConnectionGene(a, b);
            else
                c = new ConnectionGene(b, a);

            if(connections.contains(c))
                continue;
            
            c = neat.getConnection(c.getStart(), c.getEnd());
            c.setWeight((2*Math.random()-1)*Neat.WEIGHT_RANGE);

            connections.addSorted(c);
            return;
        }
    }

    public void mutateNode()
    {
        ConnectionGene c = connections.getRandomElement();
        if(c == null)
            return;
        
        NodeGene from = c.getStart();
        NodeGene to = c.getEnd();

        NodeGene mid = neat.getChildNode(c);
        mid.setX((from.getX()+to.getX())/2);
        mid.setY((from.getY()+to.getY())/2 + 0.1*Math.random()-0.05);
        nodes.add(mid);

        ConnectionGene c1 = neat.getConnection(from, mid);
        ConnectionGene c2 = neat.getConnection(mid,to);

        c1.setWeight(1);
        c2.setWeight(c.getWeight());
        c2.setEnabled(c.isEnabled());

        connections.removeElement(c);
        connections.add(c1);
        connections.add(c2);
    }

    public void mutateWeightShift()
    {
        ConnectionGene c = connections.getRandomElement();
        if(c!=null)
        {
            c.setWeight(c.getWeight()+(2*Math.random()-1)*Neat.WEIGHT_SHIFT_RANGE);
        }
    }

    public void mutateWeightRandom()
    {
        ConnectionGene c = connections.getRandomElement();
        if(c!=null)
        {
            c.setWeight((2*Math.random()-1)*Neat.WEIGHT_RANGE);
        }
    }

    public void mutateToggleLink()
    {
        ConnectionGene c = connections.getRandomElement();
        if(c!=null)
        {
            c.setEnabled(!c.isEnabled());
        }
    }

    private static double Lerp(double a,double b, double r)
    {
        return a + (b-a)*r;
    }
}
