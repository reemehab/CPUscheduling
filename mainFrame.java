import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.*;
/**
 * 
 * @author Omar Fawzy
 *
 */

public class mainFrame extends SGUI {
	private JPanel mainPanel;
	private ProcessInformations processesInformations;
	private Statstics statstics;
	public Simulator simulator;
	protected ArrayList<Process> processes;
	private ArrayList<Integer> width;
	private String schduler_title;
	public mainFrame(String title, int contextSwitch, int n) {
		this.schduler_title = title;
		mainPanel = new JPanel();
		processes = new ArrayList<Process>();
		width = new ArrayList<Integer>();
		width.add(30);
		setSize(500, 300);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBackground(new Color(204, 204, 204));
		setTitle(schduler_title);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(10, 10));
		EmptyBorder emtyborder = new EmptyBorder(5, 5, 5, 5);
		
		mainPanel.setBorder(emtyborder);
		mainPanel.setBackground(new Color(204, 204, 204));
		mainPanel.setLayout(new BorderLayout(10, 10));
		
		processesInformations = new ProcessInformations();
		mainPanel.add(processesInformations, BorderLayout.EAST); // process information 

		statstics = new Statstics(contextSwitch, n);
		mainPanel.add(statstics, BorderLayout.PAGE_END);
		
		simulator = new Simulator();
		mainPanel.add(simulator, BorderLayout.CENTER);
		
		contentPane.add(mainPanel, BorderLayout.CENTER);
		
		setLocationRelativeTo(null);

	}
	
	public void addProcess(Process p) {
		width.add(30);
		processes.add(p);
		simulator.addProcess(p);
		processesInformations.addProcess(p);
		int wi[] = getWidthArray();
		simulator.layout.rowHeights = wi;
		processesInformations.layout.rowHeights = wi;
	}
	
	private int[] getWidthArray()
	{
	    int[] res = new int[width.size()];
	    for (int i=0; i < width.size(); i++)
	    {
	    	res[i] = width.get(i);
	    }
	    return res;
	}
	
	public class Simulator extends JScrollPane{
		private JPanel processPanel;
		int width = 0;
		private GridBagConstraints constrains;
		private ArrayList<Line> lines;
		GridBagLayout layout;
		private int current;
		public Simulator() {
			current = 0;
			processPanel = new JPanel();
			processPanel.setSize(1000,1000);
			
			layout = new GridBagLayout();
			layout.columnWidths = new int[] {30,10};
			layout.rowHeights =  new int[] {30,30,30,30,30,30,30};
			processPanel.setLayout(layout);

			//constrains = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 0), 0, 0);
			constrains = new GridBagConstraints();
			constrains.anchor = GridBagConstraints.NORTH;
			constrains.fill = GridBagConstraints.BOTH;
			constrains.weighty = 1;
			constrains.gridy = 1000;
			processPanel.add(new JPanel(),constrains);
			constrains.weighty = 0;
			constrains.gridy = 0;
			
			//constrains.weightx = 0.1;
			//constrains.gridheight = 50;
			
			//simulatorPanel.add(processPanel);
			lines = new ArrayList<Line>();
			this.setViewportView(processPanel);	
			this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		}
		
		
		
		public void addProcess(Process p){
			constrains.gridy = processes.indexOf(p);
			constrains.weightx = 0;
			constrains.gridx = 0;
			processPanel.add(new JLabel(p.name), constrains);
			constrains.gridx = 1;
			constrains.weightx = 0.9;
			Line line = new Line(p.color,500);
			lines.add(line);
			if(line.width > layout.columnWidths[1]) {
				layout.columnWidths[1] += line.width + 500;
			}
			processPanel.add(line, constrains);
			//statstics.updateProcessesNumber(processes.size());
		}
		
		public void updateProcess(Process p,int len) {
			if(p == null) {
				current += len;
				return;
			}
			Line line = lines.get(processes.indexOf(p));
			//System.out.println(current + " " +p.name + " " + len);
			line.update(current, len);
			current += len;
			line.repaint();
			if(current > layout.columnWidths[1]) {
				processPanel.setSize(1000, 100 + current);
				layout.columnWidths[1] += 100;
			}
		}
		
		public void updateProcess(Process p,int from,int len) {
			if(p == null) {
				return;
			}
			Line line = lines.get(processes.indexOf(p));
			line.update(from, len);
			line.repaint();
			if(current > layout.columnWidths[1]) {
				layout.columnWidths[1] += 50;
			}
		}
		
		
		
		public class Line extends JPanel{
			int width;
			Color color;
			ArrayList<Integer> intrevals;
			public Line(Color c,int width) {
				intrevals = new ArrayList<Integer>();
				color = c;
				this.width = width;
				setSize(width,50);
				repaint();
			}
			
			public void update(int from, int len){
				intrevals.add(from);
				intrevals.add(len);
			}
			
			@Override
		    public void paintComponent(Graphics g){
		        super.paintComponent(g);
		        Graphics2D g2 = (Graphics2D) g;
		        g2.setColor(color);
		        //System.err.println(intrevals.toString());
		        for(int i = 0; i < intrevals.size() - 1; i += 2)
		        	g2.fillRect(intrevals.get(i),5,intrevals.get(i+1),25); 		        
		    }
		}

		public void updateProcess(int index, int len) {
			if(index == -1) {
				current += len;
				return;
			}
			Line line = lines.get(index);
			//System.out.println(current + " " +p.name + " " + len);
			line.update(current, len);
			current += len;
			line.repaint();
			if(current > layout.columnWidths[1]) {
				processPanel.setSize(1000, 100 + current);
				layout.columnWidths[1] += 100;
			}
		}
	}
	
	public class Statstics extends JPanel {
		public JLabel val1;
		public JLabel val2;
		public JLabel val3;
		public JLabel header;
		
		public Statstics(int contextSwitch, int n) {
			this.setBorder(LineBorder.createGrayLineBorder());
			
			GridBagLayout layout = new GridBagLayout();
/*
			layout.columnWidths = new int[] {16, 42, 0, 0};
			layout.rowHeights = new int[] {28, 0, 0, 19, 0};
			layout.columnWeights = new double[] {0.0, 0.0, 0.0, 1};
			layout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1};
			*/

			layout.columnWeights = new double[] {0.1,0.9};
			
			
			setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
			
			this.setLayout(layout);
			// 0 header
			// 0 sname val1
			// 0 info1 val2
			// 0 info2 val3
			// start 1 0
			
			GridBagConstraints constrains = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(2, 15, 2, 0), 0, 0);
			header = new JLabel("Statstics");
			header.setFont(header.getFont().deriveFont(Font.BOLD));
			
			this.add(header, constrains);

			constrains.gridy++;
			this.add(new JLabel("Schedule name:"), constrains);
			
			constrains.gridy++;
			this.add(new JLabel("Context switching:"), constrains);
			
			constrains.gridy++;
			this.add(new JLabel("Number of processes:"), constrains);
			
			val1 = new JLabel(schduler_title);
			val3 = new JLabel(String.valueOf(n));
			val2 = new JLabel(String.valueOf(contextSwitch));
			
			constrains.gridx++;
			this.add(val3, constrains);
			
			constrains.gridy--;
			this.add(val2, constrains);
			
			constrains.gridy--;
			this.add(val1, constrains);
		}
		public void updateContextSwitch(int v){
			val1.setText(String.valueOf(v));
		}
		/*public void updateProcessesNumber(int n){
			val2.setText(String.valueOf(n));
		}*/
	}
	
	public class ProcessInformations extends JScrollPane{
		public JPanel processesTable;
		public JLabel processNumber;
		public JLabel processColor;
		public JLabel processName;
		public JLabel processID;
		public JLabel processPriority;
		private GridBagConstraints constrains;
		public GridBagLayout layout;
		public Color mainColor;
		ProcessInformations() {
			processesTable = new JPanel();
			mainColor = new Color(153, 153, 153);
			processesTable.setBackground(mainColor);
			CompoundBorder br = new CompoundBorder(new TitledBorder("Process Informations"), new EmptyBorder(6, 6, 6, 6));
			processesTable.setBorder(br);
			layout = new GridBagLayout();
			
			layout.columnWidths = new int[] {55, 35, 65, 35, 0, 0};
			//layout.rowHeights = new int[] {1, 1, 1, 1, 1};
			layout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
			//layout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0};
			
			processesTable.setLayout(layout);
			
			processNumber = new JLabel("Process");
			processColor = new JLabel("Color");
			processName = new JLabel("Name");
			processID = new JLabel("PID");
			processPriority = new JLabel("Priority ");

			// starting from 0, 0
			constrains = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 5, 5), 0, 0);
			
			
			processesTable.add(processNumber, constrains);
			constrains.gridx++;
			processesTable.add(processColor, constrains);

			constrains.gridx++;
			processName.setBackground(new Color(204, 204, 204));
			processesTable.add(processName, constrains);

			constrains.gridx++;
			processesTable.add(processID, constrains);

			constrains.gridx++;
			processesTable.add(processPriority,constrains);
			
			constrains.weighty = 1;
			constrains.gridx = 0;
			constrains.gridy = 1000;
			JPanel d = new JPanel();
			d.setBackground(mainColor);
			processesTable.add(d ,constrains);
			constrains.weighty = 0;
			constrains.gridy = 0;
			
			this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			this.setViewportView(processesTable); // scrolling
		}

		public void addProcess(Process p) {
			constrains.gridy = processes.indexOf(p) +1;
			constrains.gridx = 0;
			processesTable.add(new JLabel(String.valueOf(constrains.gridy)), constrains);
			constrains.gridx += 2;
			processesTable.add(new JLabel(String.valueOf(p.name)), constrains);
			constrains.gridx++;
			processesTable.add(new JLabel(String.valueOf(p.getId())), constrains);
			constrains.gridx++;
			processesTable.add(new JLabel(String.valueOf(p.priority)), constrains);
			
			constrains.gridx = 1;
			//constrains.weightx = 50;
			//constrains.gridwidth = 500;
			//constrains.gridheight = 1;
			processesTable.add(new ColoredRectangle(p.color), constrains);
		}
		
		public class ColoredRectangle extends JPanel{
			private Color color;
			public ColoredRectangle(Color color) {
				this.color = color;
				this.setBackground(mainColor);
			}
			@Override
		    public void paintComponent(Graphics g){
		        super.paintComponent(g);
		        Graphics2D g2 = (Graphics2D) g;
		        g2.setColor(color);
		        g2.fillRect(0,5,20,15); 
		    }
		}
	}
	
	@Override
	public void updateProcess(Process process, int len) {
		simulator.updateProcess(process, len);
	}

	@Override
	public void updateProcess(Process process, int from, int to) {
		simulator.updateProcess(process, from, to);
	}

	@Override
	public void updateProcess(int index, int len) {
		simulator.updateProcess(index, len);
	}
}
