import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Scanner;

public class Frame extends JFrame {

    private Panel panel;
    private Genome genome;
    private Agent ag;

    public Frame(Agent a) {
        this();
        this.ag = a;
        setGenome(this.ag.getGenome());
        this.repaint();
    }

    public void setGenome(Genome genome){
        panel.setGenome(genome);
        this.genome = genome;
    }

    public Frame() throws HeadlessException {
        Scanner sc = new Scanner(System.in);
        
        this.setDefaultCloseOperation(3);

        this.setTitle("NEAT");
        this.setMinimumSize(new Dimension(1000,700));
        this.setPreferredSize(new Dimension(1000,700));

        this.setLayout(new BorderLayout());


        UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
        try {
            UIManager.setLookAndFeel(looks[3].getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }


        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(1000,100));
        menu.setLayout(new GridLayout(1,6));

        JButton buttonF = new JButton("New random agent");
        buttonF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame.this.ag = genome.getNeat().getAgent((int)(Math.random()*genome.getNeat().agents));
                setGenome(ag.getGenome());
                panel.repaint();
            }
        });
        menu.add(buttonF);

        JButton buttonG = new JButton("Calculate");
        buttonG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ag.createCalculator();
                double[] in = new double[genome.getNeat().inputSize];
                System.out.println("Enter the input values");
                for (int i = 0; i < in.length; i++)
                {
                    in[i] = sc.nextDouble();
                }
                System.out.println(Arrays.toString(ag.calculate(in)));
                repaint();
            }
        });
        menu.add(buttonG);


        this.add(menu, BorderLayout.NORTH);

        this.panel = new Panel();
        this.add(panel, BorderLayout.CENTER);

        this.setVisible(true);
    }

}