
/**
 *
 * @author Ahmed Abdelnaser
 * @author Omar Abdelah
 */
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class AGAT {

    public ArrayList<Process> AllProcess = new ArrayList<Process>();
    public ArrayList<Process> readyProcess = new ArrayList<Process>();
    public ArrayList<Process> deadList = new ArrayList<Process>();
    public int processNumber = 0, processPriority = 0, processArrivalTime = 0, processBrustTime = 0, quantum = 0, maxRemainingBrust;
    public String processName;
    public int time = 0, sumOfBrust = 0;
    public float last = 0;
    public boolean change = false;
    public SGUI gui;

	public void setV1() {
        for (int i = 0; i < AllProcess.size(); i++) {
            AllProcess.get(i).setv1(last);
        }

    }
    
    public void setAttributes() {
        for (int i = 0; i < readyProcess.size(); i++) {
            readyProcess.get(i).setv2(maxBrust());
            readyProcess.get(i).setFactor(readyProcess.get(i).v1, readyProcess.get(i).v2);
        }

    }

    public void FillReadyQueue() {
        while (true && !(AllProcess.isEmpty())) {
            if (AllProcess.get(0).getArrivalTime() <= time) {

                readyProcess.add(AllProcess.get(0));
                AllProcess.remove(0);
                setAttributes();
            } else {
                break;
            }

        }

    }

    public void finishedProcess(Process p, int time) {
        p.quantum_time = 0;
        p.setTurnaroundTime(time - (p.getArrivalTime()));
        p.setWaitingTime(p.getTurnaroundTime() - p.getBurstTime());
        deadList.add(p);
        readyProcess.remove(p);
        System.out.println("Quantum Time: " + '0');
    }

    public void betterAGAT(Process p, int act) {
        readyProcess.remove(p);
        readyProcess.add(p);
        p.quantum_time += (p.quantum_time - act);
    }

    public float maxBrust() {
        float max = -1;
        for (int i = 0; i < readyProcess.size(); i++) {
            if (readyProcess.get(i).remaining_brust_time > max) {
                max = readyProcess.get(i).remaining_brust_time;
            }
        }
        return max;
    }

    public Process miniAgat() {
        float mini = 9999;
        int k = 0;
        for (int i = 0; i < readyProcess.size(); i++) {
            if (readyProcess.get(i).agat_factor <= mini) {
                mini = readyProcess.get(i).agat_factor;
                k = i;
            }

        }
        return readyProcess.get(k);

    }

    public void execution() {
        Process process1;
        if (time < sumOfBrust) {
            int active = 0; // TO KEEP TRACK EACH CYCLE
            FillReadyQueue();
            if (!change) {
                process1 = readyProcess.get(0);   //PICK THE FIRST PROCESS
                System.out.println("Runing: " + process1.name);
            } else {
                process1 = miniAgat();
                System.out.println("Runing: " + process1.name);
                change = false;
            }

            System.out.println("Quantum Time: " + process1.quantum_time);

            time += process1.quantum40();
            active += process1.quantum40();
            process1.remaining_brust_time -= active;
            gui.updateProcess(process1, active*10);
            if (process1.remaining_brust_time == 0) /// CASE 3 "HAS FINISHED ITS JOP"
            {
                finishedProcess(process1, time);

            } else {
                FillReadyQueue();
                if (miniAgat() != process1) /// CASE 2 "BETTER AGAT"
                {
                    betterAGAT(process1, active);
                    change = true;
                } else /// CASE 1 "CURRENT PROCESS STILL RUNING"
                {
                    for (int i = time; i < sumOfBrust; i++) {
                        if (process1.remaining_brust_time == 0) {
                            finishedProcess(process1, time);
                            break;
                        }
                        time++;
                        active++;
                        process1.remaining_brust_time--;
                        gui.updateProcess(process1, 10);
                        if (process1.quantum_time - active == 0) {
                            readyProcess.remove(process1);
                            readyProcess.add(process1);
                            process1.quantum_time += 2;
                            break;
                        } else {
                            FillReadyQueue();
                            if (miniAgat() != process1) {
                                change = true;
                                betterAGAT(process1, active);
                                break;
                            }
                        }

                    }
                }
            }
            System.out.println("Remaining Brust: " + process1.remaining_brust_time);
            System.out.println("Current Process's AGAT factor: " + process1.agat_factor);
            System.out.println("--------------------------------------------------");

            execution();
        }
    }

    public AGAT(Process processes[],SGUI gui) {
    	this.gui = gui;
        float avgWaiting = 0,
		avgTurnAround = 0;
        processNumber = processes.length;
        
        for (Process process : processes) {
			sumOfBrust += process.getBurstTime();
			AllProcess.add(process);
        }
        
        last = AllProcess.get(processNumber - 1).getArrivalTime();
        setV1();
        execution();
        
        System.out.println("--------------------------------------------------" + "\n" + "\n");
        for (int i = 0; i < deadList.size(); i++) {
            avgWaiting += deadList.get(i).getWaitingTime();
            avgTurnAround += deadList.get(i).getTurnaroundTime();
            System.out.println("Turnaround Time For " + deadList.get(i).name + " : " + deadList.get(i).getTurnaroundTime());
            System.out.println("Waiting Time For " + deadList.get(i).getname() + " : " + (deadList.get(i).getWaitingTime()));
            System.out.println("--------------------------------------------------" + "\n");
        }
        avgWaiting /= deadList.size();
        avgTurnAround /= deadList.size();
        System.out.println("Total Average Waiting Time : " + avgWaiting);
        System.out.println("Total Average Turn Around Time : " + avgTurnAround);
    }
}
