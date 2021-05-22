import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileOperation {
    String FileName;
    List<Process> processList;
    public FileOperation(String fileName){
        this.FileName = fileName;
    }

    public int countProcesses() throws FileNotFoundException {
        File file = new File(FileName);
        if (!file.exists()) {
            System.out.println("File not found!");
        }
        Scanner scanner = new Scanner(file);

        int count = 0 ;
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            count ++;
        }
        scanner.close();
        return count;
    }

    public void generateProcesses(List<Process> processes) throws FileNotFoundException {
        File file = new File(FileName);
        Scanner scanner = new Scanner(file);
        int count = countProcesses();
        processList = new ArrayList<Process>(); //
        for (int i = 0; i< count; i++){
            Process process = new Process();

            String line = scanner.nextLine();
            String[] splitedLine = line.split(":", 0);

            process.setProcessId(Integer.parseInt(splitedLine[0]));

            String[] bursts = splitedLine[1].split(";", 0);
            int[] cpubursts = new int[bursts.length];
            int[] iobursts = new int[bursts.length];
            for (int j = 0; j < bursts.length; j++) {
                String[] splitedBurst = (bursts[j].replace("(", "").replace(")","")).split(",", 0);
                cpubursts[j] =  Integer.parseInt(splitedBurst[0]);
                iobursts[j] = Integer.parseInt(splitedBurst[1]);
            }
            process.setCpuBursts(cpubursts);
            process.setIOBursts(iobursts);
            processList.add(process);
        }
        scanner.close();
    }

    public List<Process> getProcessList() throws FileNotFoundException {
        generateProcesses(processList);
        return this.processList;
    }

   /* public void getProcesses(int[] processes, int[][] cpubursts, int[][] iobursts) throws FileNotFoundException, IOException {
        File file = new File(FileName);
        Scanner scanner = new Scanner(file);
        int count = countProcesses();
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();
            String[] splitedLine = line.split(":", 0);
            processes[i] = Integer.parseInt(splitedLine[0]);
            String[] bursts = splitedLine[1].split(";", 0);
            for (int j = 0; j < bursts.length; j++) {
                String[] splitedBurst = (bursts[j].replace("(", "").replace(")","")).split(",", 0);
                cpubursts[i][j] = Integer.parseInt(splitedBurst[0]);
                iobursts[i][j] = Integer.parseInt(splitedBurst[1]) ;
            }
        }
        scanner.close();
    }*/
}
