

/**
 * @author Reem Ehab
 *
 */
public class SJF //shortest job first scheduler
{

    Process GanttChart[];
    private double AverageWaitingTime = 0.0;
    private double AverageTurnAroundTime = 0.0;
    public static int currentTime = 0;
    public static int index = 0;
    public Process smallestProcess;
    public SGUI gui;
    public SJF(Process processes[],SGUI gui) {
    	this.gui = gui;
        GanttChart = new Process[processes.length];
        int pool = 5;
        int size = processes.length;
        int counter = 0;
        sortBurstTime(processes);
        sortArrive(processes);
        if (processes.length > 5) // to handle starvation  case if number processes are bigger than 10
        {
            while (size > 0) {
                pool = 5;
                if (size < 5) {
                    pool = size;
                }
                System.out.println("pool =" + pool);
                Process[] temporary = new Process[pool];
                for (int i = 0; i < pool; i++, counter++) // to copy the 10 elements that are going to enter the pool
                {
                    temporary[i] = processes[counter];
                    System.out.println(" no. " + i + " " + temporary[i].toString());
                }
                if (counter == pool) // to get the minimum from the first array only
                {
                	currentTime = getMInimumTime(temporary);
                	gui.updateProcess(smallestProcess, currentTime*10);
                }
                this.sortBurstTime(temporary);
                this.schedule(temporary);
                size = size - 5;
            }
        } else {
            currentTime = getMInimumTime(processes);
            gui.updateProcess(smallestProcess, currentTime*10);
            this.sortBurstTime(processes); // sort the array ascendingly according to their burst time
            this.schedule(processes);
        }
        System.out.println("Processes are executed in this Order");
        for (int i = 0; i < GanttChart.length; i++) {
            System.out.println((i + 1) + " - " + GanttChart[i].toString());
        }
        System.out.println("AverageTurnAroundTime =" + (AverageTurnAroundTime / GanttChart.length));
        System.out.println("AverageWaitingTime =" + (AverageWaitingTime / GanttChart.length));
    }

    public void schedule(Process processes[]) ///scheduling without starvation
    {
        for (int i = 0; i < processes.length; i++, index++) {
            for (int j = 0; j < processes.length; j++) {
                if (currentTime >= processes[j].getArrivalTime() && processes[j].getCheck() == false) {
                    GanttChart[index] = processes[j];
                    processes[j].setCheck(true);
                    GanttChart[index].setStartTime(currentTime);
                    currentTime += processes[j].getBurstTime();
                    gui.updateProcess(processes[j], processes[j].getBurstTime()*10);
                    GanttChart[index].setCompletionTime(currentTime);
                    GanttChart[index].setTurnaroundTime(GanttChart[index].getCompletionTime() - GanttChart[index].getArrivalTime());
                    AverageTurnAroundTime += GanttChart[index].getTurnaroundTime();
                    GanttChart[index].setWaitingTime(GanttChart[index].getTurnaroundTime() - GanttChart[index].getBurstTime());
                    AverageWaitingTime += GanttChart[index].getWaitingTime();
                    break;
                }
            }
        }

    }

    public void sortBurstTime(Process processes[]) // sort the array depending on the burst Time
    {
        for (int i = 0; i < processes.length; i++) {
            for (int j = i + 1; j < processes.length; j++) {
                if (processes[i].getBurstTime() > processes[j].getBurstTime()) {
                    Process temp = processes[i];
                    processes[i] = processes[j];
                    processes[j] = temp;
                }
            }
        }
    }

    public void sortArrive(Process processes[]) //this.sortArrive
    {
        for (int i = 0; i < processes.length; i++) {
            for (int j = i + 1; j < processes.length; j++) {
                if (processes[i].getArrivalTime() > processes[j].getArrivalTime()) {
                    Process temp = processes[i];
                    processes[i] = processes[j];
                    processes[j] = temp;
                }
            }
        }

    }

    public int getMInimumTime(Process processes[]) //get minimum of garrival time
    {
        int small = processes[0].getArrivalTime();
        for (int i = 1; i < processes.length; i++) {
            if (processes[i].getArrivalTime() < small) {
            	smallestProcess = processes[i];
                small = processes[i].getArrivalTime();
            }
        }
        return small;
    }
}
