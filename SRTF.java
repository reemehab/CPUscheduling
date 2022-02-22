/**
 *
 * @author Ahmed Tarek
 */

import java.util.ArrayList;

public final class SRTF {

    int numOfProcesses, chartSize = 0, process = 1;
    int arrivalTime[];
    int remainingBT[];// remaining burst time
    int copy[];
    int waitingList[];
    int TurnRoundTimeList[];
    ArrayList<Integer> chart = new ArrayList<>();
    ArrayList<Integer> delayList = null;// for Starvation Handling
    int contextSwitch;

    public SRTF(int numOfProcesses, Process processes[],SGUI gui, int contextSwitch) {
        this.contextSwitch = contextSwitch;
        this.numOfProcesses = numOfProcesses;
        arrivalTime = new int[numOfProcesses];
        remainingBT = new int[numOfProcesses];
        copy = new int[numOfProcesses];
        waitingList = new int[numOfProcesses];
        TurnRoundTimeList = new int[numOfProcesses];
        for (int i = 0; i < numOfProcesses; i++) {
            arrivalTime[i] = processes[i].getArrivalTime();
            remainingBT[i] = processes[i].getBurstTime();
            copy[i] = processes[i].getBurstTime();
        }
        for (int i = 0; i < remainingBT.length; i++) {
            chartSize += remainingBT[i];
        }
        if (delayList != null) {
            for (int k = 0; k < delayList.size(); k++) {
                while (remainingBT[delayList.get(k)] != 0) {
                    chart.add((delayList.get(k)) + 1);
                    gui.updateProcess(delayList.get(k), 1*10);
                    remainingBT[delayList.get(k)]--;
                }
            }
            delayList = null;

        }
        int  prevProcess=(selectShortestJob(chart.size())) + 1;
        for (int k = 0; k < chartSize; k++) {
            if(prevProcess!=(selectShortestJob(chart.size())) + 1){
            	gui.updateProcess(null,contextSwitch*10);
                for(int g=0;g<contextSwitch;g++){
                    chart.add(0);//Zero represent switching
                }
            }
           prevProcess=(selectShortestJob(chart.size())) + 1;
            chart.add((selectShortestJob(chart.size())) + 1);
            gui.updateProcess(selectShortestJob(chart.size()-1),1*10);
            remainingBT[selectShortestJob(chart.size()-1)]--;
            
        }
        for (int k = 0; k < numOfProcesses; k++) {
            if (remainingBT[k] != 0) {
                delayList.add(k);

            }
        }

        for (int i = 1; i <= numOfProcesses; i++) {
            if((determineEnd(i) + 1)==chart.size()){
                TurnRoundTimeList[i - 1] = (determineEnd(i) + 1) - arrivalTime[i - 1];
                 waitingList[i - 1] = (determineEnd(i) + 1) - arrivalTime[i - 1] - copy[i - 1];
            }
            else{
                TurnRoundTimeList[i - 1] = ((determineEnd(i) + 1)+ contextSwitch) - arrivalTime[i - 1];
                 waitingList[i - 1] = ((determineEnd(i) + 1)+ contextSwitch) - arrivalTime[i - 1] - copy[i - 1];
                
            }
         
        }

        for (int i = 0; i < numOfProcesses; i++) {
            System.out.println(processes[i].name + " :");
            System.out.println("waiting time = " + waitingList[i]);
            System.out.println("TurnRound Time =" + TurnRoundTimeList[i]);
        }
        System.out.println("Average waiting time = " + calcAverageWaiting(waitingList));
        System.out.println("Average turnround time = " + calcAverageTurnRoundTime(TurnRoundTimeList));
    }

    int selectShortestJob(int time) {
        int ShortestJobIndex = 0;
        for (int i = 0; i < numOfProcesses; i++) {
            if (remainingBT[ShortestJobIndex] == 0) {
                for (int g = 0; g < numOfProcesses; g++) {
                    if (remainingBT[g] != 0) {
                        ShortestJobIndex = g;
                        break;
                    }
                }
            }
            if ((arrivalTime[i] <= time) && (remainingBT[i] < remainingBT[ShortestJobIndex]) && (remainingBT[i] != 0)) {
                ShortestJobIndex = i;
            } else if ((arrivalTime[i] <= time) && (remainingBT[i] == remainingBT[ShortestJobIndex]) && (remainingBT[i] != 0)) {
                if (arrivalTime[i] < arrivalTime[ShortestJobIndex]) {
                    ShortestJobIndex = i;
                }

            }
        }
        return ShortestJobIndex;

    }

    int determineEnd(int process) {
        int end = 0;
        for (int i = 0; i < chart.size(); i++) {
            if (chart.get(i) == process) {
                end = i;
            }
        }
        return end;
    }

    float calcAverageWaiting(int waitingList[]) {
        float waitingTimeSum = 0;
        for (int i = 0; i < waitingList.length; i++) {
            waitingTimeSum += waitingList[i];
        }
        return waitingTimeSum / waitingList.length;
    }

    float calcAverageTurnRoundTime(int turnRoundTimeList[]) {
        float turnRoundTimeSum = 0;
        for (int i = 0; i < TurnRoundTimeList.length; i++) {
            turnRoundTimeSum += TurnRoundTimeList[i];
        }
        return turnRoundTimeSum / TurnRoundTimeList.length;
    }

}

/*
import java.util.ArrayList;

public final class SRTF {

    int numOfProcesses, chartSize = 0, process = 1;
    int arrivalTime[];
    int remainingBT[];// remaining burst time
    int copy[];
    int waitingList[];
    int TurnRoundTimeList[];
    int chart[];
    int contextSwitch;
    ArrayList<Integer> delayList = null;

    public SRTF(int numOfProcesses, Process processes[], SGUI gui, int contextSwitch) {
    	this.contextSwitch = contextSwitch;
        this.numOfProcesses = numOfProcesses;
        arrivalTime = new int[numOfProcesses];
        remainingBT = new int[numOfProcesses];
        copy = new int[numOfProcesses];
        waitingList = new int[numOfProcesses];
        TurnRoundTimeList = new int[numOfProcesses];
        for (int i = 0; i < numOfProcesses; i++) {
            arrivalTime[i] = processes[i].getArrivalTime();
            remainingBT[i] = processes[i].getBurstTime();
            copy[i] = processes[i].getBurstTime();
        }

        for (int i = 0; i < remainingBT.length; i++) {
            chartSize += remainingBT[i];
        }

        chart = new int[chartSize];
        int currentJob;
        int g = 0;
        if (delayList != null) { // Starvation Handling
            for (int k = 0; k < delayList.size(); k++) {
                while (g < chartSize) { 
                    while (remainingBT[delayList.get(k)] != 0) {
                        chart[g] = (delayList.get(k)) + 1;
                        //gui.updateProcess(delayList.get(k),1*10);
                        remainingBT[delayList.get(k)]--;
                    }
                    g++;
                }
            }
            delayList = null;
        }
        while (g < chartSize) {
            chart[g] = selectShortestJob(g) + 1;
            //gui.updateProcess(selectShortestJob(g),1*10);
            remainingBT[selectShortestJob(g)]--;
            g++;
        }

        for (int k = 0; k < numOfProcesses; k++) {
            if (remainingBT[k] != 0) {
                delayList.add(k);
            }
        }

        for (int i = 1; i <= numOfProcesses; i++) {
            TurnRoundTimeList[i - 1] = (determineEnd(i) + 1) - arrivalTime[i - 1];
            waitingList[i - 1] = (determineEnd(i) + 1) - arrivalTime[i - 1] - copy[i - 1];
        }

        for (int i = 0; i < numOfProcesses; i++) {
            System.out.println(processes[i].name + " :");
            System.out.println("waiting time = " + waitingList[i]);
            System.out.println("TurnRound Time =" + TurnRoundTimeList[i]);
        }
        System.out.println("Average waiting time = " + calcAverageWaiting(waitingList));
        System.out.println("Average turnround time = " + calcAverageTurnRoundTime(TurnRoundTimeList));

    }

    int selectShortestJob(int time) {
        int ShortestJobIndex = 0;
        for (int i = 0; i < numOfProcesses; i++) {
            if (remainingBT[ShortestJobIndex] == 0) {
                for (int g = 0; g < numOfProcesses; g++) {
                    if (remainingBT[g] != 0) {
                        ShortestJobIndex = g;
                        break;
                    }
                }
            }
            if ((arrivalTime[i] <= time) && (remainingBT[i] < remainingBT[ShortestJobIndex]) && (remainingBT[i] != 0)) {
                ShortestJobIndex = i;
            } else if ((arrivalTime[i] <= time) && (remainingBT[i] == remainingBT[ShortestJobIndex]) && (remainingBT[i] != 0)) {
                if (arrivalTime[i] < arrivalTime[ShortestJobIndex]) {
                    ShortestJobIndex = i;
                }

            }
        }
        return ShortestJobIndex;

    }

    int determineEnd(int process) {
        int end = 0;
        for (int i = 0; i < chart.length; i++) {
            if (chart[i] == process) {
                end = i;
            }
        }
        return end;
    }

    float calcAverageWaiting(int waitingList[]) {
        float waitingTimeSum = 0;
        for (int i = 0; i < waitingList.length; i++) {
            waitingTimeSum += waitingList[i];
        }
        return waitingTimeSum / waitingList.length;
    }

    float calcAverageTurnRoundTime(int turnRoundTimeList[]) {
        float turnRoundTimeSum = 0;
        for (int i = 0; i < TurnRoundTimeList.length; i++) {
            turnRoundTimeSum += TurnRoundTimeList[i];
        }
        return turnRoundTimeSum / TurnRoundTimeList.length;
    }

}*/