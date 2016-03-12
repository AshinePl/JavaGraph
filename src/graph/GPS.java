package graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import java.util.*;


public class GPS extends JFrame implements ActionListener {
    ArrayList<Graph> graph;
    MyPanel drawing;
    JComboBox<String> from, to;
    JButton send;
    ButtonGroup group;
    JRadioButton fastest, shortest;
    JLabel path;
    int panelX = 460;
    int panelY = 460;
    
    GPS(String s){
        super(s);
        BuildGraph();
        CalculateGraph();
        setSize(800, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
        
        add(Box.createHorizontalStrut(5));
        
        drawing = new MyPanel(graph);
        drawing.setPreferredSize(new Dimension(panelX, panelY) );
        drawing.setMaximumSize(new Dimension(panelX, panelY));
        drawing.setBorder(BorderFactory.createEtchedBorder());
        add(drawing);
        
        add(Box.createHorizontalStrut(5) );
        JPanel pan = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        pan.setBorder(BorderFactory.createEtchedBorder());
        pan.setPreferredSize(new Dimension(800 - panelX - 20, 460));
        pan.setMaximumSize(new Dimension(800 - panelX - 20, 460));
        
       // pan.add(Box.createVerticalStrut(100));
        from = new JComboBox<>();
        to = new JComboBox<>();
        
        for(Graph x : graph)
        {
            from.addItem(x.getName());
            to.addItem(x.getName());
        }
        
        pan.add(new JLabel("Miasto startowe: "));
       
        pan.add(from);
       
        pan.add(new JLabel("Miasto końcowe: "));
        pan.add(to);
        
        group  = new ButtonGroup();
        fastest = new JRadioButton("Najszybsza");
        fastest.setSelected(true);
        group.add(fastest);
        shortest = new JRadioButton("Najkrótsza");
        group.add(shortest);
        
        JPanel btngrp = new JPanel();
        btngrp.setLayout(new BoxLayout(btngrp, BoxLayout.Y_AXIS));
        
        btngrp.add(fastest);
        btngrp.add(shortest);
        
        pan.add(btngrp);
        
        JPanel butt = new JPanel();
        send = new JButton("Szukaj");
        send.addActionListener(this);
        butt.setPreferredSize(new Dimension(800 - panelX - 30, 40) );
        butt.add(send);
        
        pan.add(butt);
        
        path = new JLabel();
        //label.setBorder(BorderFactory.createEtchedBorder());
        path.setPreferredSize(new Dimension(800 - panelX - 36, 220) );
        pan.add(path);
        
        add(pan);
       // add(Box.createHorizontalStrut(5));
       // pack();
        
        setVisible(true);
    }
    

    public static void main(String[] args) {
            GPS frame = new GPS("GPS");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int cityA = 0;
        int cityB = 0;
        for(int i = 0; i < graph.size(); i++)
        {
            if(graph.get(i).getName().equals(((String)from.getSelectedItem()))) cityA = i;
            if(graph.get(i).getName().equals(((String)to.getSelectedItem()))) cityB = i;
            for(GraphConnection x : graph.get(i).getConnections())
            {
                x.setColor(Color.black);
            }
        }
        if(shortest.isSelected())
        {
            GetShortestPath(graph, cityB, cityA, graph.size());
        }
        else 
        {
            GetFastestPath(graph, cityB, cityA, graph.size());      
        }
    }
    
    public void BuildGraph()
    {
        graph = new ArrayList<>();
        
      
        try {
            Connection conn;
            Statement stat;
            
            Class.forName("org.postgresql.Driver");  
            
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "123");

            stat = conn.createStatement();
            ResultSet r = stat.executeQuery("SELECT * FROM graphCities");

            while (r.next()) 
            {
                Graph v = new Graph(r.getString(2), r.getDouble(4), r.getDouble(3));
                graph.add(v);
            }
            r.close();
//            stat.close();
//            
//            stat = conn.createStatement();
            r = stat.executeQuery("SELECT * FROM graphConnections");
            while(r.next())
            {
                int cityA = r.getInt(2);
                int cityB = r.getInt(3);
                graph.get(cityA - 1).AddConnection(cityB - 1, r.getDouble(4), r.getInt(5));
            }
            
            conn.close();

        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

        
    }

    private void CalculateGraph() 
    {
        double maxX = -1800;
        double maxY = -900;
        double minX = 1800;
        double minY = 900;
        
        for(Graph x : graph)
        {
            if (x.getX() > maxX) maxX = x.getX();
            if (x.getX() < minX) minX = x.getX();
            if (x.getY() > maxY) maxY = x.getY();
            if (x.getY() < minY) minY = x.getY();
        }
        double aX = (panelX - 20)/(maxX - minX);
        double aY = (panelY - 20)/(maxY - minY);
        
        for(Graph x : graph)
        {
           //x.setPos(aX, -aY, minX, maxY, 10, 10);
            if(aX > aY)
            {
                int val =(int)(aY*(maxX - minX));
                x.setPos(aY, -aY, minX, maxY, 10 + (panelX - val)/2, 10 );
            }
            else 
            {
                int val = (int)(aX*(maxY - minY));
                x.setPos(aX, -aX, minX, maxY, 10, 10 + (panelY - val)/2);
            }
        }
    }
    public void GetShortestPath(ArrayList<Graph> g, int cityA, int cityB, int size)
    {
        PriorityQueue<QueueItem> q = new PriorityQueue<>();
        QueueItem p;

	int city = -1;

	

	Double odleglosci[] = new Double[size];
	Integer prev[] = new Integer[size];

	for (int i = 0; i < size; i++)
	{
		odleglosci[i] = Double.MAX_VALUE;
		prev[i] = -1;
	}

	odleglosci[cityA] = 0.0;
        
        q.add(new QueueItem(cityA, 0.0));

	while ((p = q.peek()) != null)
	{
		city = p.getCity();
		if (city == cityB) break;
	
		double dist = p.getPrior();
		q.poll();

		
		for(GraphConnection wsk : g.get(city).getConnections() )
		{
			if (odleglosci[wsk.getCity()] > wsk.getDistance() + dist)
			{
				odleglosci[wsk.getCity()] = wsk.getDistance() + dist;
				prev[wsk.getCity()] = city;
                                q.add(new QueueItem(wsk.getCity(), wsk.getDistance() + dist));
			}
		}
	}
        
        path.setText("<HTML>" + "Calkowita droga: " + odleglosci[city] +" km" + "<br>" );

	while (prev[city] != -1)
	{
            graph.get(city).getConnection(prev[city]).setColor(Color.red);
            graph.get(prev[city]).getConnection(city).setColor(Color.red);
            path.setText(path.getText() + g.get(city).getName() + " -> " + g.get(prev[city]).getName() + "<br>");
		city = prev[city];
	}
        path.setText(path.getText() + "</HTML>");
        drawing.repaint();
    }
    public void GetFastestPath(ArrayList<Graph> g, int cityA, int cityB, int size)
    {
        PriorityQueue<QueueItem> q = new PriorityQueue<>();
        QueueItem p;

	int city = -1;

	

	Double odleglosci[] = new Double[size];
	Integer prev[] = new Integer[size];

	for (int i = 0; i < size; i++)
	{
		odleglosci[i] = Double.MAX_VALUE;
		prev[i] = -1;
	}

	odleglosci[cityA] = 0.0;
        
        q.add(new QueueItem(cityA, 0.0));

	while ((p = q.peek()) != null)
	{
		city = p.getCity();
		if (city == cityB) break;
	
		double dist = p.getPrior();
		q.poll();

		
		for(GraphConnection wsk : g.get(city).getConnections() )
		{
			if (odleglosci[wsk.getCity()] > wsk.getTime()+ dist)
			{
				odleglosci[wsk.getCity()] = wsk.getTime() + dist;
				prev[wsk.getCity()] = city;
                                q.add(new QueueItem(wsk.getCity(), wsk.getTime() + dist));
			}
		}
	}
        int hours = odleglosci[city].intValue();
        Double min = odleglosci[city] - hours;
        min *= 60;
        path.setText("<HTML>" + "Calkowity czas: " + hours +" h " + min.intValue() + " min"+ "<br>" );

	while (prev[city] != -1)
	{
            graph.get(city).getConnection(prev[city]).setColor(Color.red);
            graph.get(prev[city]).getConnection(city).setColor(Color.red);
            path.setText(path.getText() + g.get(city).getName() + " -> " + g.get(prev[city]).getName() + "<br>");
            city = prev[city];
	}
       // path.setText(path.getText() + "</HTML>");
        drawing.repaint();
    }
}
