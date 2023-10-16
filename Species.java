import java.util.Comparator;

public class Species
{
    private RandomAccessSet<Agent> agents = new RandomAccessSet<>();
    private Agent leader;
    private double score;

    public Species(Agent lead)
    {
        this.leader = lead;
        this.leader.setSpecies(this);
        agents.add(leader);
    }

    public RandomAccessSet<Agent> getAgents()
    {return agents;}

    public Agent getLeader()
    {return leader;}

    public double getScore()
    {return score;}

    public boolean put(Agent a)
    {
        if(a.distance(leader) < Neat.MAX_DISTANCE_SPECIES)
        {
            a.setSpecies(this);
            agents.add(a);
            return true;
        }
        return false;
    }

    public void forcePut(Agent a)
    {
        a.setSpecies(this);
        agents.add(a);
    }

    public void goExtinct()
    {
        for (Agent a : agents.getDataList()) 
        {
            a.setSpecies(null);
        }
    }

    public void findScore()
    {
        double s=0;
        for (Agent a : agents.getDataList())
        {
            s+=a.getScore();
        }
        this.score = s / agents.size();
    }

    public void reset()
    {
        for (Agent a : agents.getDataList())
        {
            a.setSpecies(null);
        }
        leader = agents.getRandomElement();
        agents.clear();

        leader.setSpecies(this);
        agents.add(leader);

        score = 0;
    }

    public void killPercentage(double percentage)
    {
        agents.getDataList().sort(new Comparator<Agent>() {

            @Override
            public int compare(Agent o1, Agent o2) {
                return Double.compare(o1.getScore(), o2.getScore());
            }
            
        });

        double temp = percentage*agents.size();
        for (int i = 0; i < temp; i++)
        {
            agents.getDataAt(0).setSpecies(null);
            agents.removeDataAt(0);
        }
    }

    public Genome breed()
    {
        //System.out.println(size());
        Agent a = agents.getRandomElement();
        Agent b = agents.getRandomElement();

        if(a!=null && b!=null)
        {
            if(a.getScore()<b.getScore())
            {
                Agent temp = a;
                a=b;
                b=temp;
            }
        }

        return Genome.crossBreed(a.getGenome(), b.getGenome());
    }

    public int size()
    {return agents.size();}


}
