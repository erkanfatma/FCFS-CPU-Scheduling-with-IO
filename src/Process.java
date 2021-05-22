public class Process {
    int ProcessId;
    int[] CpuBursts;
    int[] IOBursts;

    public int getProcessId(){
        return ProcessId;
    }

    public void setProcessId(int id){
        this.ProcessId = id;
    }

    public int[] getCpuBursts(){
        return CpuBursts;
    }

    public void setCpuBursts(int[] cpuBursts){
        this.CpuBursts = cpuBursts;
    }

    public int[] getIOBursts(){
        return IOBursts;
    }

    public void setIOBursts(int[] ioBursts){
        this.IOBursts = ioBursts;
    }

}
