import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.Timer;
import java.awt.event.ActionEvent;

public class Neat
{
    public static final int MAX_NODES = (int)Math.pow(2,15);

    private HashMap<ConnectionGene,ConnectionGene> everyConnection = new HashMap<>();
    private ArrayList<NodeGene> everyNode = new ArrayList<>();

    private RandomAccessSet<Agent> everyAgent = new RandomAccessSet<>();
    private RandomAccessSet<Species> allSpecies = new RandomAccessSet<>();

    int inputSize,outputSize,agents;

    public static final double C1=1,C2=1,C3=1;
    public static final double WEIGHT_RANGE = 2;
    public static final double WEIGHT_SHIFT_RANGE = 0.08;

    public static final double PROBABILITY_NEW_CONNECTION = 0.27;
    public static final double PROBABILITY_NEW_NODE = 0.06;
    public static final double PROBABILITY_WEIGHT_SHIFT = 0.18;
    public static final double PROBABILITY_WEIGHT_RANDOMIZE = 0.03;
    public static final double PROBABILITY_TOGGLE_CONNECTION = 0.03;

    public static final double SURVIVORS = 0.4;

    public static final double MAX_DISTANCE_SPECIES = 20;

    public static int count=0;

    public Neat(int inp, int out, int ag)
    {
        this.reset(inp, out, ag);
    }

    public void reset(int inp,int out,int ag)
    {
        this.inputSize = inp;
        this.outputSize = out;
        this.agents = ag;

        everyConnection.clear();
        everyNode.clear();
        everyAgent.clear();
        allSpecies.clear();

        for (int i = 0; i < inputSize; i++)
        {
            NodeGene n = getNewNode();
            n.setX(0.1);
            n.setY((i+1)/(double)(inputSize+1));
        }

        for (int i = 0; i < outputSize; i++)
        {
            NodeGene n = getNewNode();
            n.setX(0.9);
            n.setY((i+1)/(double)(outputSize+1));
        }

        for (int i = 0; i < agents; i++)
        {
            Agent a = new Agent();
            a.setGenome(this.createEmptyGenome());
            a.createCalculator();
            a.setSpecies(null);
            everyAgent.add(a);
        }
    }

    public void Load(String path)
    {
        File f = new File(path);
        try
        {
            Scanner sc = new Scanner(f);

            everyConnection.clear();
            everyNode.clear();
            everyAgent.clear();
            allSpecies.clear();

            this.inputSize = sc.nextInt();
            this.outputSize = sc.nextInt();
            this.agents = sc.nextInt();
            int numberNodes = sc.nextInt();
            for (int i = 0; i < numberNodes; i++)
            {
                NodeGene ng = new NodeGene(i+1);
                ng.setX(sc.nextDouble());
                ng.setY(sc.nextDouble());
                everyNode.add(ng);
            }

            for (int i = 0; i < agents; i++)
            {
                Genome g = this.createEmptyGenome();
                int numberConnections = sc.nextInt();
                for (int j = 0; j < numberConnections; j++)
                {
                    int from = sc.nextInt();
                    int to = sc.nextInt();
                    double w = sc.nextDouble();
                    boolean enabled = (sc.nextInt()==1);
                    int childNode = sc.nextInt();

                    NodeGene start = getNode(from);
                    NodeGene end = getNode(to);

                    ConnectionGene cg = getConnection(start, end);
                    cg.setWeight(w);
                    cg.setEnabled(enabled);
                    cg.childNodeInnovationNumber = childNode;

                    if(!g.getNodes().contains(start))
                        g.getNodes().addSorted(start);
                    if(!g.getNodes().contains(end))
                        g.getNodes().addSorted(end);
                    g.getConnections().add(cg);
                }
                Agent a = new Agent();
                a.setGenome(g);
                a.createCalculator();
                a.setSpecies(null);
                everyAgent.add(a);
            }
            generateSpecies();
            sc.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found.");
        }
    }

    public Agent getAgent(int i)
    {
        return everyAgent.getDataAt(i);
    }

    public NodeGene getNewNode()
    {
        NodeGene n = new NodeGene(everyNode.size()+1);
        everyNode.add(n);
        return n;
    }

    public NodeGene getNode(int id)
    {
        if(id<= everyNode.size())
            return everyNode.get(id-1);
        return null;
    }

    public NodeGene getChildNode(ConnectionGene cg)
    {
        ConnectionGene cgOriginal = everyConnection.get(cg);
        if(cgOriginal.childNodeInnovationNumber == -1)
        {
            NodeGene ng = getNewNode();
            cgOriginal.childNodeInnovationNumber = ng.getInnovationNumber();
            return ng;
        }
        else
        {
            return getNode(cgOriginal.childNodeInnovationNumber);
        }
    }

    public int getChildNodeInnovationNumber(ConnectionGene cg)
    {return everyConnection.get(cg).childNodeInnovationNumber;}

    public Genome createEmptyGenome()
    {
        Genome g = new Genome(this);

        for (int i = 0; i < inputSize+outputSize; i++)
        {
            g.getNodes().add(getNode(i+1));
        }

        return g;
    }

    public ConnectionGene getConnection(NodeGene n1, NodeGene n2)
    {
        ConnectionGene cg = new ConnectionGene(n1, n2);
        if(everyConnection.containsKey(cg))
        {
            cg.setInnovationNumber(everyConnection.get(cg).getInnovationNumber());
        }
        else
        {
            cg.setInnovationNumber(everyConnection.size()+1);
            everyConnection.put(cg, cg);
        }
        return cg;
    }

    public static ConnectionGene cloneConnectionGene(ConnectionGene cg)
    {
        ConnectionGene ncg = new ConnectionGene(cg.getStart(), cg.getEnd());
        ncg.setInnovationNumber(cg.getInnovationNumber());
        ncg.setWeight(cg.getWeight());
        ncg.setEnabled(cg.isEnabled());
        return ncg;
    }

    public void evolve()
    {
        generateSpecies();
        kill();
        removeExtinctSpecies();
        repopulate();
        //mutate();
        for (Agent a : everyAgent.getDataList())
        {
            a.createCalculator();
        }
    }

    private void mutateAll()
    {
        for (Agent a : everyAgent.getDataList())
        {
            a.mutate();
        }
    }

    private void repopulate()
    {
        RandomSelector<Species> rs = new RandomSelector<>();
        for (Species s:allSpecies.getDataList())
        {
            rs.add(s, s.getScore());
            //System.out.println("Species population "+s.size());
        }
        
        for (Agent a : everyAgent.getDataList())
        {
            if(a.getSpecies() == null)
            {
                Species s = rs.getRandomElement();
                a.setGenome(s.breed());
                a.mutate();
                s.forcePut(a);
            }
        }
    }

    private void removeExtinctSpecies()
    {
        for (int i = allSpecies.size()-1; i >= 0; i--)
        {
            if(allSpecies.getDataAt(i).size() <= 1)
            {
                allSpecies.getDataAt(i).goExtinct();
                allSpecies.removeDataAt(i);
            }
        }
        
    }

    private void kill()
    {
        for (Species s : allSpecies.getDataList())
        {
            s.killPercentage(1-SURVIVORS);
        }
    }

    private void generateSpecies()
    {
        for (Species s : allSpecies.getDataList())
        {
            s.reset();
        }

        for (Agent a : everyAgent.getDataList())
        {
            if(a.getSpecies()!=null)
            {
                //System.out.println("Leader found");
                continue;
            }

            boolean found = false;
            for (Species s : allSpecies.getDataList())
            {
                if(s.put(a))
                {
                    found = true;
                    break;
                }
            }
            if(!found)
            {
                allSpecies.add(new Species(a));
            }
        }

        for (Species s : allSpecies.getDataList())
        {
            s.findScore();
        }
    }

    public void saveGeneration(String path)
    {
        File f = new File(path);
        try 
        {
            FileWriter fl = new FileWriter(f);
            String tmp = ""+inputSize+" "+outputSize+" "+agents+" "+everyNode.size()+"\n";
            fl.write(tmp);
            
            for (NodeGene n : everyNode)
            {
                tmp = ""+n.getX()+" "+n.getY()+"\n";
                fl.write(tmp);
            }

            for (Agent a : everyAgent.getDataList())
            {
                RandomAccessSet<ConnectionGene> cons = a.getGenome().getConnections();
                tmp = ""+cons.size()+"\n";
                fl.write(tmp);
                for (ConnectionGene con : cons.getDataList())
                {
                    tmp = ""+con.getStart().getInnovationNumber()+" ";
                    tmp += con.getEnd().getInnovationNumber()+" ";
                    tmp += con.getWeight() + " ";
                    tmp += (con.isEnabled()?1:0) + " ";
                    tmp += getChildNodeInnovationNumber(con) + "\n";
                    fl.write(tmp);
                }
            }

            fl.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void main(String[] args)
    {
        Neat neat = new Neat(6, 2, 1000);
        neat.mutateAll();

        neat.Load("temporary.txt");
        
        double avg = 0;

        Agent bestAgent=null;
        FlappyBird game = new FlappyBird();

        File f = new File("graph.csv");
        FileWriter fl = null;
        try {
            fl=new FileWriter(f);
            fl.write("");
            fl.close();
        } catch (Exception e) {}

        for (int k = 0; k < 25; k++)
        {
            System.out.println("Running Gen "+k);            
            game.setTitle("Gen "+k);

            bestAgent = neat.getAgent(0);
            
            avg=0;

            for (int i=0; i<neat.everyAgent.size(); i++)
            {
                Agent a = neat.everyAgent.getDataAt(i);
                
                game.reset();
                game.agent = a;
                count = 0;
                /*Timer timer = new Timer(33, new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        game.repaint();
                        Neat.count++;
                    }
                });
                timer.start();*/
                while(!game.dead)
                {
                    
                    game.repaint();
                    Neat.count++;
                    

                    game.Update();

                    if(game.score > 3000 || count>3000)
                    {
                        game.playerDie();
                    }
                }
                System.out.println("Dead "+i+" with score: "+a.getScore());
                if(a.getScore()>bestAgent.getScore())
                    bestAgent = a;
                avg+=a.getScore();
            }
            
            try {
                fl=new FileWriter(f,true);
                fl.write(""+k+", "+bestAgent.getScore()+", "+(avg/neat.agents)+"\n");
                fl.close();
            } catch (Exception e) {}

            neat.evolve();
            neat.saveGeneration("temporary.txt");
        }

        if(bestAgent==null)
        {
            bestAgent = neat.everyAgent.getRandomElement();
        }
        Frame fm = new Frame(bestAgent);
        game.reset();
        game.agent = bestAgent;
        count = 0;
        Timer timer = new Timer(15, new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                
                if(game.dead)
                {
                    game.reset();
                    count = 0;
                }
                
                Neat.count++;
                
                if(!game.dead && game.agent!=null)
                {
                    game.Update();
                }
                

                game.repaint();
                game.setTitle(""+game.score);
            }
        });
        timer.start();
        

    }
}
