/**
 * @author Omar Fawzy
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SchedulerGUI {
	SGUI f;
	public void start() {
		FirstPage fp  = new FirstPage();
		fp.setVisible(true);
	}
	
	public void addProcess(Process p) {
		f.addProcess(p);
	}
	
	public synchronized void update(Process process, int len){
		f.updateProcess(process, len);
	}
	
	public synchronized void update(Process process, int from, int to){
		f.updateProcess(process, from, to);
	}

	public void updateProcess(int index, int len) {
		f.updateProcess(index, len);
	}
	
	public class FirstPage extends JFrame {
		public FirstPage() {
			this.setTitle("Setting Page");
			setLocationRelativeTo(null);
		    String Scheduluers[]={"SRTF", "AGAT", "Shortest Job First", "Priority Scheduling"};        
		    JComboBox com = new JComboBox(Scheduluers); 
		    JTextField contextSwitch = new JTextField();
		    JLabel clabel = new JLabel("Context Switch");
		    JTextField numProcesses = new JTextField();
		    JLabel plabel = new JLabel("Number Of Processes");
		    JLabel schduler = new JLabel("Schduler");
		    JButton next = new JButton();
		    next.setText("Continue");
		    schduler.setBounds(50, 50,150,20);
		    com.setBounds(200, 50,150,20);
		    
		    clabel.setBounds(50, 100,150,20);
		    contextSwitch.setBounds(200, 100,150,20);
		    
		    plabel.setBounds(50, 150,150,20);
		    numProcesses.setBounds(200, 150,150,20);
		    
		    next.setBounds(150, 200, 100, 30);
		    next.addActionListener(new ActionListener() {  
		        public void actionPerformed(ActionEvent e) {       
					int index = com.getSelectedIndex();
					int numberOfProcesses = Integer.parseInt(numProcesses.getText());
					int contexts = Integer.parseInt(contextSwitch.getText());
					String schduler_title = (String) com.getItemAt(index);
					SecondPage sp = new SecondPage(contexts,numberOfProcesses, index,schduler_title);
					
					end();
				}  
			});           
		    this.add(next);
		    this.add(com);this.add(schduler);this.add(plabel);this.add(numProcesses);this.add(clabel);this.add(contextSwitch);
		    this.setLayout(null);    
		    this.setSize(400,300);    
		}
		public void end() {
			this.setVisible(false);
		}
	}
	
public class SecondPage extends JFrame{
	
	
	public SecondPage(int contextSwitch,int numberOfProcesses, int schduler_number,String schduler_title){
		setTitle("Setting Page");
		InputPanel sp = new InputPanel(contextSwitch,numberOfProcesses, schduler_number, schduler_title);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setSize(400,300);
		if(schduler_number == 1)
			setSize(490,300);
		
		add(sp , BorderLayout.CENTER);
		setVisible(true);
	}
	
	public void end() {
		this.setVisible(false);
	}
	
	public class InputPanel extends JPanel {
		Process[] processes;
		ArrayList<JTextField> pnames;
		ArrayList<JButton> pcolors;
		ArrayList<JTextField> ppriority;
		ArrayList<JTextField> parrival;
		ArrayList<JTextField> pbrust;
		ArrayList<JTextField> pquantum;
		String schduler_title;
		int contextSwitch;
		int numberOfProcesses;
		int schduler_number; 
		int n;
		public InputPanel(int contextSwitch,int numberOfProcesses, int schduler_number, String schduler_title) {
			//this.setBackground();
			n = numberOfProcesses;
			this.schduler_title = schduler_title;
			this.contextSwitch = contextSwitch;
			this.numberOfProcesses = numberOfProcesses;
			this.schduler_number = schduler_number;
			
			pnames = new ArrayList<JTextField>();
			pcolors = new ArrayList<JButton>();
			ppriority = new ArrayList<JTextField>();
			parrival = new ArrayList<JTextField>();
			pbrust = new ArrayList<JTextField>();
			pquantum = new ArrayList<JTextField>();
			
		    JButton finish = new JButton("finish");
		    GridBagLayout layout = new GridBagLayout();
		    GridBagConstraints constrains = new GridBagConstraints();
		    constrains.insets = new Insets(5,5,5,5);
		    constrains.anchor = GridBagConstraints.WEST;
		    layout.columnWidths = new int[] {50,70,80,80,80,100,0};
		    layout.columnWeights = new double[] {0.0,0.0,0.0,0.0,0.0,0.0,1.0f};
		    if(schduler_number != 1) {
		    	layout.columnWidths[5] = 0;
		    	
		    }
		    processes = new Process[n];
		    double[] rw = new double[n+2];
		    int[] h =  new int[n+2];
		    
		    for(int i  = 0; i < n + 2;i++) {
		    	h[i] = 40;
		    	rw[i] = 0.0;
		    }
		    
		    h[0] = 40;
		    rw[n+1] = 1.0;
		    
		    layout.rowHeights = h;
		    layout.rowWeights = rw;
		    this.setLayout(layout);
		    constrains.gridx = 0;
		    constrains.gridy = 0;
		    constrains.fill = GridBagConstraints.HORIZONTAL;
		    
		    
		    this.add(new JLabel("Name"), constrains);
		    constrains.gridx++;
		    this.add(new JLabel("Color"), constrains);
		    constrains.gridx++;
		    this.add(new JLabel("Arrival Time"), constrains);
		    constrains.gridx++;
		    this.add(new JLabel("Burst Time"), constrains);
		    constrains.gridx++;
		    this.add(new JLabel("Priority Number"), constrains);
		    constrains.gridx++;
		    if(schduler_number == 1)
		    	this.add(new JLabel("Quantum Time"), constrains);
		    
		    
		    JButton[] cbuttons = new JButton[n];

		    for(int i = 0; i < numberOfProcesses; i++) {
		    	cbuttons[i] = new JButton();
		    	cbuttons[i].setPreferredSize(new Dimension(40, 17));
		    	cbuttons[i].setText(String.valueOf(i));		
		    	cbuttons[i].addActionListener(new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			        	JButton source = (JButton) e.getSource();	
			        	Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
			        	source.setBackground(newColor);
					}  
				}); 
		    	
		    	constrains.gridy = i+1;
		    	constrains.gridx = 0;
		    	pnames.add(new JTextField());
		    	this.add(pnames.get(i), constrains);
		    	pcolors.add(cbuttons[i]);
			    constrains.gridx++;
			    this.add(cbuttons[i], constrains);
			    constrains.gridx++;
			    
			    parrival.add(new JTextField());
			    this.add(parrival.get(i), constrains);
			    constrains.gridx++;
			    
			    pbrust.add(new JTextField());
			    this.add(pbrust.get(i), constrains);
			    constrains.gridx++;
			    
			    ppriority.add(new JTextField());
			    this.add(ppriority.get(i), constrains);
			    constrains.gridx++;
			    pquantum.add(new JTextField());
			    
			    if(schduler_number == 1) {	
				    this.add(pquantum.get(i), constrains);
			    }
			    else {
			    	pquantum.get(i).setText("0");
			    }
			    
		    }
		    
		    
			
		    finish.addActionListener(new ActionListener() {  
		        public void actionPerformed(ActionEvent e) {       
		        	fin();
		        	SGUI gui = new mainFrame(schduler_title,contextSwitch,n);
		        	for (Process x : processes) {
		        		gui.addProcess(x);
	        		}
		        	
		        	
		        	switch (schduler_number) {
		        		case 0: // priority Scheduling
			            	SRTF srtf = new SRTF(processes.length, processes,gui,contextSwitch);
	
			                break;
			            case 1: // Shortest Job First
			                AGAT agat = new AGAT(processes, gui);
			                
			                break;
			            //Shortest-Remaining Time First (SRTF)
			            case 2:
			            	SJF sjf = new SJF(processes, gui);
			            	
			                break;
			            case 3:
			            	// AGAT Scheduling
			            	
			            	PriorityScheduler ps = new PriorityScheduler(gui, contextSwitch, processes.length);
			            	for (Process process : processes) {
			                    process.startProcess(ps);
			                }
			            	ps.start();
			            	
			            	/*
				            	GUI gui = new GUI();
				                int np = Integer.parseInt(JOptionPane.showInputDialog(gui.getFrame(), "Enter Number of process."));
				                PriorityScheduler priorityScheduler = new PriorityScheduler(gui, np);
				                for (int i = 0; i < np; i++) {
				                    //processes.add(new Process(gui, priorityScheduler));
				                }
				                for (Process process : processes) {
				                    process.startProcess();
				                }
				                priorityScheduler.start();
			                */
			            	break;
			        	default:
			            	//System.out.println("choose numbers from 1 -> 4");
			            	break;
			        }
		        	
		        	
		        	
		        	
		        	
		        	
		        	gui.setVisible(true);
		        	end();
		        	// start The Schedulers
				}  
			});   
					
		    constrains.anchor = GridBagConstraints.SOUTHEAST;
		    constrains.gridy = n+1;
		    constrains.gridx = 4;
		    if(schduler_number == 1)
		    	constrains.gridx = 5;
		    
		    this.add(finish, constrains);	 
		}
		
			public void fin() {
				for(int i = 0; i < n;i++) {
					
		    		String name = pnames.get(i).getText();
		    		Color color = pcolors.get(i).getBackground();
		    		int arrival_time = Integer.parseInt(parrival.get(i).getText());
		    		int brust_time = Integer.parseInt(pbrust.get(i).getText());
		    		int priority = Integer.parseInt(ppriority.get(i).getText());
		    		int quantum = Integer.parseInt(pquantum.get(i).getText());
		    		processes[i] = new Process(name, color,  arrival_time, brust_time, priority, quantum);
		    	}
			}
		}
		
		
	}
}

