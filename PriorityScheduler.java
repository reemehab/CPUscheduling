
/**
 *
 * @author Abdelaziz
 */
import javax.swing.*;
import java.util.ArrayList;

public class PriorityScheduler extends Thread{
	private int time = 0;
	private int avgWaitingtime = 0;
	private int avgTurnAroundTime = 0;
	private int contextSwitch;
	private ArrayList<Process> processes = new ArrayList<Process>();
	private SGUI sgui;
	private boolean empty = true;
	private int np = 0;
	

	public PriorityScheduler(SGUI sgui, int contextSwitch, int np){
	        this.np = np;
	        this.sgui = sgui;
	        this.contextSwitch = contextSwitch;
	}
	
	public void add(Process process){
	    processes.add(process);
	    if(empty == true){
	        empty = false;
	        synchronized (this) {
	            notify();
	        }
	    }
	}
	
	private void Aging() // to handle the starvation problem
	{
	    for(Process process : processes){
	        int priority = process.getpriority();
	        if(priority > 1){
	            process.setpriority(priority - 1);
	        }
	    }
	}
	
	private synchronized Process getFirstOne(){
	    Process temp = processes.get(0);
	    processes.remove(0);
	    if(processes.size() == 0){
	        empty = true;
	    }
	    return temp;
	}
	private void arrange(){
	    for(int i =0 ; i < processes.size() ; i++){
	        for(int j = i+1 ; j < processes.size() ; j++){
	            int iPri = processes.get(i).getpriority();
	            int jPri = processes.get(j).getpriority();
	            if(jPri < iPri){
	                Process temp = processes.get(i);
	                processes.set(i, processes.get(j));
	                processes.set(j , temp);
	            }
	        }
	    }
	}
	
	@Override
	public synchronized void run() {
	    int i = np;
	    while(i > 0){
	        try {
	
	            while(empty == true){
	                wait();
	            }
	            arrange();
	            //print();
	            Process p = getFirstOne();
	            //gui.write(p.getName() + " has arrived and on execution.");
	            //int waitTime = 0;
	            if(time == 0){
	                p.setWaitingTime(0);
	                //waitTime = 0;
	            }else {
	                p.setWaitingTime(time - p.getArrivalTime());
	                //waitTime  = time - p.getArrivalTime();
	            }
	            avgWaitingtime += p.getWaitingTime();
	            time += p.getBurstTime();
	            sgui.updateProcess(p, p.getBurstTime()*10);
	            time += contextSwitch;
	            sgui.updateProcess(null, contextSwitch*10);
	            sleep(p.getBurstTime());
	            sleep(contextSwitch);
	            p.setTurnaroundTime(time - contextSwitch - p.getArrivalTime());
	            avgTurnAroundTime += p .getTurnaroundTime();
	            //int turnRoundTime = time - p.getArrivalTime();
	            System.out.println(p.getname() + " wait time = " + p.getWaitingTime());
	            System.out.println(p.getname() + " turn Around time = " + p.getTurnaroundTime());
	            System.out.println(p.getname() + " has ended.");
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        Aging();
	        arrange();
	        i--;
	    }
	    System.out.println("Average waiting time = " +(double) avgWaitingtime/np);
	    System.out.println("Average Turn Around time = " +(double) avgTurnAroundTime/np);
	}

}