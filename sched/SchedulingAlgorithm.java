// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.
/*
  javac -nowarn *.java
  javac -nowarn SchedulingAlgorithm.java
  java Scheduling scheduling.conf
*/
/*
  tomar en cuenta
  process.cpudone -----> el total de tiempo acumulado
  process.ioblocking --> el tiempo max a ejecutar dicha funcion
  process.cputime -----> el tiempo signado a ejecutar (se hace aleatorio)
*/

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

  public static Results Run(int runtime, Vector processVector, Results result) {
    int i = 0; int j1=0; int j2=0; int k=0; int tiempo_minimo=999999;  int candidato=(-1);
    int tiempo_compilacion = 0;
    int proseso_actual = 0;
    int[][] tiempo = new int[2][99999];
    //int proseso_anterior = 0;
    //size # of Process
    int size = processVector.size();
    int completados = 0;
    String resultsFile = "Summary-Processes";

    result.schedulingType = "Preemptive";
    result.schedulingName = "SRTF";
    try
    {
      //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
      //OutputStream out = new FileOutputStream(resultsFile);
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      sProcess process = (sProcess) processVector.elementAt(proseso_actual);

      //enproceso
      out.println("inicio tamano: " + size + "tiempo: " + runtime);
      for (i=0;i<size;i++)
      {
        process = (sProcess) processVector.elementAt(i);
        tiempo[0][i]=process.ioblocking;
        tiempo[1][i]=0;
      }
      proseso_actual=0;
      process = (sProcess) processVector.elementAt(proseso_actual);
      out.println("Process:: " + proseso_actual + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone +")");

      while (tiempo_compilacion < runtime)  //tiempo a hacer la operacion
      {
            //proseso completado//////////////////////////////////
            if (process.cpudone == process.cputime)
            {
              completados++;
              out.println("Process: " + proseso_actual + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + ")");

              if (completados == size)
              {
                result.compuTime = tiempo_compilacion;
                out.close();
                return result;
              }
              if (j1!=size)
              {
                j1++;
              }
              else
              {
                j1=1;
                out.println("------------------------------------------");
                out.println("reset");
                out.println();
              }
              //for (i = size - 1; i >= 0; i--)
              // se asigna la nueva funcion
              tiempo_minimo=999999;
              candidato=size;
              for (i = 0; i <size ; i++)
              {
                //out.println("i: "+i+" j2: "+j2);
                process = (sProcess) processVector.elementAt(i); //elementAt es el  desplazador;
                //out.println("--ub: " + i + " process.cpudone " + process.cpudone + "<" + process.cputime +" process.cputime");
                if (process.cpudone <= process.cputime)
                {
                  if ((process.cputime-process.cpudone) == tiempo_minimo && (process.cputime-process.cpudone) >0)
                  {
                    if (i<candidato)
                    {
                      candidato=i;
                      tiempo_minimo=process.cpudone;
                      proseso_actual = i;
                      out.println("-toma a " + proseso_actual);
                    }

                  }
                  else if ((process.cputime-process.cpudone) < tiempo_minimo && (process.cputime-process.cpudone) >0)
                  {
                    candidato=i;
                    tiempo_minimo=process.cpudone;
                    proseso_actual = i;
                    out.println("-toma a " + proseso_actual);
                  }
                  //proseso_actual = i;
                  //out.println("-toma a " + proseso_actual);
                }
              }
              process = (sProcess) processVector.elementAt(proseso_actual);
              out.println("Process: " + proseso_actual + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + ")");
            }

            //despacho de proseso///////////////////////////
            if (process.ioblocking == process.ionext)
            {
              out.println("Process: " + proseso_actual + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + ")");
              process.numblocked++;
              process.ionext = 0;
              //proseso_anterior = proseso_actual;

              //desplace de
              if (j2!=size)
              {
                j2++;
              }
              else
              {
                j2=1;
                out.println("//////////////////////////////////////////");
                out.println("reset");
                out.println();
              }
              //for (i = size - 1; i >= 0; i--)
              /*
                tomar en cuenta
                process.cpudone -----> el total de tiempo acumulado
                process.ioblocking --> el tiempo max a ejecutar dicha funcion
                process.cputime -----> el tiempo signado a ejecutar (se hace aleatorio)
              */
              // se asigna la nueva funcion
              tiempo_minimo=999999;
              candidato=size;
              for (i = 0; i <size ; i++)
              {
                //out.println("i: "+i+" j2: "+j2);
                process = (sProcess) processVector.elementAt(i);
                //out.println("-ub: " + i + " process.cpudone " + process.cpudone + "<" + process.cputime +" process.cputime");
                //if (process.cpudone < process.cputime && proseso_anterior != i)
                if (process.cpudone <= process.cputime )
                {
                  if ((process.cputime-process.cpudone) == tiempo_minimo && (process.cputime-process.cpudone) >0)
                  {
                    if (i<candidato)
                    {
                      candidato=i;
                      tiempo_minimo=process.cpudone;
                      proseso_actual = i;
                      out.println("-toma a " + proseso_actual);
                    }
                  }
                  else if ((process.cputime-process.cpudone) < tiempo_minimo && (process.cputime-process.cpudone) >0)
                  {
                    candidato=i;
                    tiempo_minimo=process.cpudone;
                    proseso_actual = i;
                    out.println("-toma a " + proseso_actual);
                  }
                }
              }

              process = (sProcess) processVector.elementAt(proseso_actual);

              out.println("Process: " + proseso_actual + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + ")");
            }
            process.cpudone++;
            if (process.ioblocking > 0) {
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
