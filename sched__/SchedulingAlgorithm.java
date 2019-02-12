// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

  public static Results Run(int runtime, Vector processVector, Results result) {
    int i = 0;
    int x;
    int y=0;
    int j;
    int comptime = 0;
    int currentProcess = 0;
    int previousProcess = 0;
    int size = processVector.size();
    int completed = 0;
    String resultsFile = "Summary-Processes";
    int[] array = new int[size];
    
    
    result.schedulingType = "Batch (preemptive)";
    result.schedulingName = "Short Remaining Time First"; 
    try {
      //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
      //OutputStream out = new FileOutputStream(resultsFile);
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      sProcess comp1 = (sProcess) processVector.elementAt(currentProcess);
      sProcess comp2 = (sProcess) processVector.elementAt(currentProcess);
      sProcess process = (sProcess) processVector.elementAt(currentProcess);
          
          
          
          
          
          for (i = 0; i <size; i++) {
            process = (sProcess) processVector.elementAt(i);
            array[i] = process.cputime;}
          
          
        
           currentProcess=0;
           comp1 = (sProcess) processVector.elementAt(currentProcess);
           x=comp1.cputime;
           for(i=1; i<=size-1;i++){
           comp2 = (sProcess) processVector.elementAt(i);
           y=comp2.cputime;
           if(y<x){
           x=y;	
           currentProcess=i;}}
           
      
      process = (sProcess) processVector.elementAt(currentProcess);
      out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone +")");
      
      
      while (comptime < runtime) {
      	
        if (process.cpudone == process.cputime) {
          completed++;
          out.println("Process: " + currentProcess + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone +")");
          
          
          if (completed == size) {
            result.compuTime = comptime;
            out.close();
            return result;
          }
          
          
          
          
          for (i = 0; i <size; i++) {
            process = (sProcess) processVector.elementAt(i);
            if(process.cpudone>0&& process.cpudone<process.cputime){
            array[i] = (process.cputime-process.cpudone);}
            else{
            	array[i]=process.cputime;}
            }
     
           x=2000;
           
           
           for(i=0; i<size;i++){
           	 if(array[i]>0 && currentProcess!=i){
              y=array[i];
             if(y<x){
             x=y;	
             currentProcess=i;}}} 
          
          
          
          
          process = (sProcess) processVector.elementAt(currentProcess);
          out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + ")");
        }      
        if (process.ioblocking == process.ionext) {
          out.println("Process: " + currentProcess + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone +")");
          process.numblocked++;
          process.ionext = 0; 
          previousProcess = currentProcess;
          
          
          for (i = 0; i <size; i++) {
            process = (sProcess) processVector.elementAt(i);
            if(process.cpudone>0){
            array[i] = (process.cputime-process.cpudone);}
            else{
            	array[i]=process.cputime;}
            }
          
          
          x=2000;
           
           
           
           for(i=0; i<size;i++){
           	
           	 
              y=array[i];
             if(y<x){
             
             	if(array[i]>0 && previousProcess!=i){
             x=y;	
             currentProcess=i;}}} 
           
           
           
          
          process = (sProcess) processVector.elementAt(currentProcess);
          out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + ")");
        }        
        process.cpudone++;       
        if (process.ioblocking > 0) {
          process.ionext++;
        }
        comptime++;
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
    result.compuTime = comptime;
    return result;
  }
}
