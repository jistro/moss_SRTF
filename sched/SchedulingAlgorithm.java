// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

  public static Results Run(int runtime, Vector processVector, Results result) {
    int i = 0; int j=0; int desp1 = 0; int desp2 = 0; int k=0; int min_esp=999999999;
    int tiempo_compilacion = 0;
    int proseso_actual = 0;
    int proseso_anterior = 0;
    //size # of Process
    int size = processVector.size();
    int completados = 0;
    int tam_max=runtime;
    String resultsFile = "Summary-Processes";

    result.schedulingType = "Batch (Nonpreemptive)";
    result.schedulingName = "First-Come First-Served";
    try
    {
      //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
      //OutputStream out = new FileOutputStream(resultsFile);
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      sProcess process = (sProcess) processVector.elementAt(proseso_actual);

      //enproceso
      out.println("Process: " + proseso_actual + " registered... ( ++" + process.cputime + " max:" + process.ioblocking + " completado:" + process.cpudone + " " + ")");

      while (tiempo_compilacion < runtime)  //tiempo a hacer la operacion
      {

            //proseso completado//////////////////////////////////
            if (process.cpudone == process.cputime)
            {
              completados++;
              out.println("Process: " + proseso_actual + " completed... ( ++" + process.cputime + " max:" + process.ioblocking + " completado:" + process.cpudone + " " + ")");

              // si ya no se tiene mas que hacer entonces
              if (completados == size)
              {
                result.compuTime = tiempo_compilacion;
                out.close();
                return result;
              }

              //for (i = size - 1; i >= 0; i--)
              tam_max=runtime;
              if (k!=size)
              {
                k++;
                out.println();
                out.println("k "+k+ " size "+size);
              }
              else
              {
                k=0;
                out.println();
                out.println("reset");
              }
              min_esp=999999999;
              for (i = 0; i <k ; i++)
              {
                process = (sProcess) processVector.elementAt(i); //elementAt es el  desplazador;
                out.println("--ub: " + i + " process.cpudone " + process.cpudone + " < " + process.cputime +" process.cputime");
                if (i != proseso_actual)
                {
                  out.println("el proceso " + proseso_actual + "es diferente a "+i);
                }
                if (process.cpudone < process.cputime )
                {
                  if (process.cpudone<min_esp)
                  {
                    min_esp=process.cpudone;
                    proseso_actual = i;
                    out.println("-toma a " + proseso_actual);
                  }

                }
              }
              process = (sProcess) processVector.elementAt(proseso_actual);
              out.println("Process: " + proseso_actual + " registered... ( ++" + process.cputime + " max:" + process.ioblocking + " completado:" + process.cpudone + " " + ")");
            }


            //despacho de proseso///////////////////////////
            if (process.ioblocking == process.ionext)
            {
              out.println("Process: " + proseso_actual + " I/O blocked... ( ++" + process.cputime + " max:" + process.ioblocking + " completado:" + process.cpudone + " " + ")");
              process.numblocked++;
              process.ionext = 0;
              proseso_anterior = proseso_actual;

              //desplace de
              //for (i = size - 1; i >= 0; i--)
              if (j!=size)
              {
                j++;
                out.println();
                out.println("j "+j+ " size "+size);
              }
              else
              {
                j=0;
                out.println();
                out.println("reset");
              }
              min_esp=999999999;
              for (i = 0; i <j ; i++)
              {
                out.println("-ub: " + i + " process.cpudone " + process.cpudone + " < " + process.cputime +" process.cputime");
                process = (sProcess) processVector.elementAt(i);
                if (i != proseso_actual)
                {
                  out.println("el proceso " + proseso_actual + " es diferente a "+i);
                }
                if (process.cpudone < process.cputime && i != proseso_actual)
                {
                  if (process.cpudone<min_esp)
                  {
                    min_esp=process.cpudone;
                    proseso_actual = i;
                    out.println("-toma a " + proseso_actual);
                  }
                }
              }


              process = (sProcess) processVector.elementAt(proseso_actual);

              out.println("Process: " + proseso_actual + " registered... ( ++" + process.cputime + " max:" + process.ioblocking + " completado:" + process.cpudone + " " + ")");
            }
            process.cpudone++;
            if (process.ioblocking > 0)
            {
              process.ionext++;
            }
            tiempo_compilacion++;
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
    result.compuTime = tiempo_compilacion;
    return result;
  }
}
