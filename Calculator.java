import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Calculator
{
    private ArrayList<Node> inputNodes = new ArrayList<>();
    private ArrayList<Node> outputNodes = new ArrayList<>();
    private ArrayList<Node> hiddenNodes = new ArrayList<>();
    
    public Calculator(Genome g)
    {
        RandomAccessSet<NodeGene> nodes = g.getNodes();
        RandomAccessSet<ConnectionGene> cons = g.getConnections();

        HashMap<Integer,Node> nodeMap = new HashMap<>();

        for(NodeGene ng:nodes.getDataList())
        {
            Node node = new Node(ng.getX());
            nodeMap.put(ng.getInnovationNumber(),node);

            if(ng.getX() <= 0.1001)
                inputNodes.add(node);
            else if(ng.getX() >= 0.8999)
                outputNodes.add(node);
            else
                hiddenNodes.add(node);
        }

        hiddenNodes.sort(new Comparator<Node>() {

            @Override
            public int compare(Node o1, Node o2) {
                return o1.compareTo(o2);
            }
            
        });

        for (ConnectionGene cg : cons.getDataList())
        {
            Node start = nodeMap.get(cg.getStart().getInnovationNumber());
            Node end = nodeMap.get(cg.getEnd().getInnovationNumber());
            Connection con = new Connection(start, end);

            con.setWeight(cg.getWeight());
            con.setEnabled(cg.isEnabled());

            end.getConnections().add(con);
        }
    }

    public double[] calculate(double... input)
    {
        if(input.length != inputNodes.size())
            throw new RuntimeException("Input size error");
        for (int i = 0; i < inputNodes.size(); i++)
        {
            inputNodes.get(i).setOutput(input[i]);
        }
        for (Node n : hiddenNodes)
        {
            n.calculate();
        }
        double[] output = new double[outputNodes.size()];
        for (int i = 0; i < outputNodes.size(); i++)
        {
            outputNodes.get(i).calculate();
            output[i]=outputNodes.get(i).getOutput();
        }
        return output;
    }
}
