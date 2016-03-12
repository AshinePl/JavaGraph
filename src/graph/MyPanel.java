package graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.*;

public class MyPanel extends javax.swing.JPanel {

    ArrayList<Graph> graph;
    int size = 3;
    
    public MyPanel(ArrayList<Graph> g) {
        graph = g;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        g.setColor(Color.black);
        Font f = new Font("Arial", Font.PLAIN, 12);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        for(Graph x : graph)
        {
            for(GraphConnection y : x.getConnections())
            {
                g.setColor(y.getColor());
                g.drawLine(x.getPX(), x.getPY(), graph.get(y.getCity()).getPX(), graph.get(y.getCity()).getPY());
            }
            g.setColor(Color.BLACK);
            g.fillRect(x.getPX() - size, x.getPY() - size , size+size, size+size);
            if(x.getPX() + fm.stringWidth(x.getName()) < this.getWidth())
                g.drawString(x.getName(), x.getPX() + size, x.getPY() + 2);
            else
                g.drawString(x.getName(), x.getPX() - fm.stringWidth(x.getName()) - size, x.getPY() + 10);

        }
    }
}
    
    
