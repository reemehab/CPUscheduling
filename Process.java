
/**
 *
 * @author Reem ,AhmedTarek , Abdelaziz, Omar Fawzy, Ahmed Abdelnaser ,Omar Abdelah
 */
import java.awt.Color;
import javax.swing.*;

public class Process extends Thread{
    private int arrivalTime;
    private int burstTime;
    private PriorityScheduler PS;
    public String name;
    public String type;
    public Color color;
    private boolean check;
    public int remaining_brust_time;
    public int priority;
    public int quantum_time;
    public int agat_factor;
    private int completionTime;
    private int startTime;
    private int waitingTime;
    private int TurnaroundTime;
    float v1, v2;
    
    public Process(String name, Color color, int arrival_time, int brust_time, int priority, int quantum) {
		super();
		this.name = name;
		this.color = color;
		this.arrivalTime = arrival_time;
		this.burstTime = brust_time;
		remaining_brust_time = brust_time;
		this.priority = priority;
		this.quantum_time = quantum;
	}
    
    public Process(String name, int priority, int arrival_time, int brust_time, int quantum) {
        this.name = name;
        this.priority = priority;
        this.arrivalTime = arrival_time;
        this.burstTime = brust_time;
        remaining_brust_time = brust_time;
        this.quantum_time = quantum;
    }

    public Process(String name) {
        this.name = name;
    }

    public Process(String name, int arrival_time, int brust_time) {
        super();
        this.name = name;
        this.arrivalTime = arrival_time;
        this.burstTime = brust_time;
        this.check = false;

    }

    public void setFactor(float v1, float v2) { // v1 and v2 are inputs from the AGATScheduler

        agat_factor = (int) ((10 - priority) + Math.ceil(arrivalTime / v1) + Math.ceil(remaining_brust_time / v2));

    }

    public void setv1(float last) {
        float res;
        if (last > 10) {
            res = (last / 10);
        } else {
            res = 1;
        }
        v1 = res;
    }

    public void setv2(float max) {
        float res;
        if (max > 10) {
            res = (max / 10);
        } else {
            res = 1;
        }
        v2 = res;
    }

    public int quantum40() { // returns the Quantum -> round(40%)
        return (int) Math.round(quantum_time * 0.4);
    }

    public void startProcess(PriorityScheduler prioritys) {
        try {
            Thread.sleep(arrivalTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //gui.write(getName() + ": Arrival time ended and moving to the scheduler.");
        System.out.println(getname() + ": Arrival time ended and moving to the scheduler.");
        prioritys.add(this);
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public boolean getCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return TurnaroundTime;
    }

    
    public int getpriority() {
		return priority;
	}

	public void setpriority(int priority) {
		this.priority = priority;
	}

	public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public void setTurnaroundTime(int TurnaroundTime) {
        this.TurnaroundTime = TurnaroundTime;
    }

    @Override
    public String toString() {
    	return String.format("Process %-10s Turn Around Time %-5d Wating Time %-5d", name , TurnaroundTime, waitingTime);
        //return "Process{" + "name=" + name + ", startTime=" + startTime + ", completionTime=" + completionTime + ", TAT= " + TurnaroundTime + ", wt= " + waitingTime + '}';
    }

}
