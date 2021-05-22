import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.org.apache.xpath.internal.operations.Operation;
import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class FcfsAlg {
	/*
	@author Fatma Erkan -20150807029
	*/

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String baseFileName;
        System.out.println("Please enter filename with location.\n For example: C:\\Users\\erkan\\Desktop\\test.txt");
        baseFileName = scanner.next();
        //baseFileName = "C:\\Users\\erkan\\Desktop\\test.txt";

        generateFcfsAlgorithm(baseFileName);
    }

    private static void generateFcfsAlgorithm(String fileName) throws FileNotFoundException {
        FileOperation fileOperation = new FileOperation(fileName);
        List<Process> processes = fileOperation.getProcessList();
        ArrayList<Integer> time = new ArrayList<>();
        ArrayList<Integer> returnTime = new ArrayList<>();
        ArrayList<Boolean> isReturnCompleted = new ArrayList<>();
        ArrayList<Integer> resultProcess = new ArrayList<>();
        int[] countBurstLocation = new int[processes.size()];
        ArrayList<Integer>processTurnaroundTimeList = new ArrayList<>();
        int[] processWaitingTimeList = new int[processes.size()];
        int processLocation = 0;

        //region Header of Table
        System.out.println("Time\tProcess Id\tBursts\tReturn Time\t");
        //endregion

        //region Set Default Values
        time.add(0,0);
        processLocation = 0;
        for (int i = 0; i < countBurstLocation.length; i++) {
            countBurstLocation[i] = 0;
        }
        //endregion

        int counter = 1;
        for (int i = 0; i <counter; i++) {
            //TO DO: find process location in list -- which process is available
            if (i != 0 && returnTime.size() != 0) {
                ArrayList<Integer> availableProcesses = new ArrayList<>();
                boolean isReturnOnline = false;
                for (int j = 0; j < returnTime.size(); j++) {
                    //TO DO: compare time and returnTime to select next process
                    if (time.get(i) >= returnTime.get(j) && isReturnCompleted.get(j) != true) {
                        isReturnOnline = true;
                        processLocation = resultProcess.get(j);
                        //isReturnCompleted.set(j, true);
                        if (!availableProcesses.contains(j)){
                            availableProcesses.add(j);
                        }
                    }
                    if (isReturnOnline  == false){
                        for (int k = 0; k < processes.size(); k++) {
                            availableProcesses.add(k);
                        }
                        //Delete if process run at least one
                        for (int m = 0; m < resultProcess.size(); m++) {
                            for (int n = 0; n < availableProcesses.size(); n++) {
                                if (resultProcess.get(m) == availableProcesses.get(n)) {
                                    availableProcesses.remove(n);
                                }
                            }
                        }
                    }
                }
                if (availableProcesses.size() > 0) {
                    List<Integer> newList = availableProcesses.stream().distinct().collect(Collectors.toList());
                    availableProcesses.clear();
                    for (Integer item: newList) {
                        availableProcesses.add(item);
                    }
                    Collections.sort(availableProcesses); 
                    processLocation = availableProcesses.get(0);
                    //region Set if return time done
                    for (int j = 0; j < resultProcess.size(); j++) {
                        if (resultProcess.get(j) == processLocation){
                            isReturnCompleted.set(j, true);
                        }
                    }
                    //endregion
                }
            }
            //

            //region Find current process
            Process currentProcess = processes.get(processLocation);
            //endregion

            int getBurst = countBurstLocation[processLocation];

            //region calculate returnTime
            if (currentProcess.getIOBursts()[getBurst] == -1) {
                returnTime.add(time.get(i) + currentProcess.getCpuBursts()[getBurst]);
                isReturnCompleted.add(true);
                processTurnaroundTimeList.add(returnTime.get(i));
            }else{
                returnTime.add(time.get(i) + currentProcess.getCpuBursts()[getBurst] + currentProcess.getIOBursts()[getBurst]);
                isReturnCompleted.add(false);
            }

            //set result process
            resultProcess.add(processLocation);
            //endregion

            //region calculate time
            time.add(time.get(i) + currentProcess.getCpuBursts()[getBurst]);
            //endregion

            //region set process burst location
            if (countBurstLocation[processLocation] < processes.get(resultProcess.get(i)).getCpuBursts().length){
                countBurstLocation[processLocation] ++;
            }
            //endregion

            //region Print Table Element
            System.out.println("\t" +time.get(i) + "\t\t" + processes.get(resultProcess.get(i)).getProcessId() + "\t\t(" +
                    processes.get(resultProcess.get(i)).getCpuBursts()[getBurst] +"," +
                    processes.get(resultProcess.get(i)).getIOBursts()[getBurst] + ")\t\t" + returnTime.get(i));
            //endregion

            //region Control if counter will be increased
            boolean controlReturns = false;
            for (int j = 0; j < isReturnCompleted.size(); j++) {
                if (isReturnCompleted.get(j) == false){
                    controlReturns = true;
                }
            }
            if (controlReturns == true){
                ++counter;
            }
            //endregion


        }

        //region TurnaroundTime - Waiting Time

        //region
        for (int i = 0; i < resultProcess.size(); i++) {
            // TO DO ::

        }
        //endregion

        System.out.println("Average Turn Around Time is : " + findAverageTime(processTurnaroundTimeList));
        //System.out.println("Average Waiting Time is : " + findWaitingTime(processWaitingTimeList));
        //endregion
    }
    
    public static double findAverageTime(ArrayList<Integer> processTurnAroundTime){
        int total = 0;
        for (int i = 0; i < processTurnAroundTime.size(); i++) {
            total += processTurnAroundTime.get(i);
        }
        return Double.valueOf(total)/Double.valueOf(processTurnAroundTime.size());
    }

    public static double findWaitingTime(int[] processWaitingTime){
        int total = 0;
        for (int i = 0; i < processWaitingTime.length; i++) {
            total += processWaitingTime[i];
        }
        return Double.valueOf(total)/Double.valueOf(processWaitingTime.length);
    }
}
