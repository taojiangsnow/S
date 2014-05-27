/*
 * @author xuejiang 
 */
package Main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import DataInput.Filters.*;
import DataInput.Loaders.*;
import DataInput.Loaders.SensitiveContext.TxtSensitiveContextSetLoader;
import DataInput.Loaders.TrainData.DataLoader;
import DataInput.Loaders.TrainData.TxtLoader;
import Model.MarkovModel.FirstOrderMarkovModel;
import Model.MarkovModel.MarkovModel;
import Check.ProbabilisticPC;
import Check.SimulatableCheck;
import Check.Utility.*;
import Context.*;
import SuppressionVector.*;
import SuppressionVectorTrain.GreedyTrain;
import SuppressionVectorTrain.SupVecTrain;
import Utils.*;

public class Maskit {
	public static TimeMapContext mapping;
	public static int T;
	public static int start_index = 0;
	public static int end_index;
	public static int start_T = 0;
	public static int end_T;
	public static HashMap<String,Integer> scontext_id;
	public static ArrayList<Integer> locids; //a list of different location id
	public static int[] sensitive_context_set;  //sensitive contexts' indexes
 	
	public Maskit(int x, double y, int c, Filter f, Utility u) {
		PreferredSettings.k = x;
		PreferredSettings.delta = y;
		PreferredSettings.cycle = c;
		PreferredSettings.filter = f;
		PreferredSettings.utility = u;
	}

	public void convertSensitiveContextSet(SensitiveContextSet s) {
		int i = 0;
		sensitive_context_set = new int[s.getSize()];
		Iterator<String> it = s.getSet().iterator();
		while (it.hasNext()) {
			Integer in = Maskit.scontext_id.get(it.next());
			System.out.println("id "+in);
			sensitive_context_set[i] = Maskit.locids.indexOf(in);
			i++;
		}
	}
	
	public static void main(String[] args) throws IOException, MatlabConnectionException, MatlabInvocationException {
		Maskit m = new Maskit(3,1.0/3,Cycle.DAY,new SampleFrequentPointByTimeInterval(3600,0,24),new NumberOfReleasedContext());
		DataLoader t = new TxtLoader("F:\\test\\test3b.txt");
		t.getDataSet();
		t.output();
		//System.out.println("Done data loading");
		//****
		SensitiveContextSet set = new SensitiveContextSet(new TxtSensitiveContextSetLoader("F:\\test\\t3b.txt"));
        set.getSensitiveSet();
        m.convertSensitiveContextSet(set);

        //*****
        for (int i: sensitive_context_set) {
        	System.out.print(i+" ");
        }
        System.out.println();
        
		PreferredSettings.model = new FirstOrderMarkovModel(t.getObservation(),PreferredSettings.cycle, Maskit.T);
        PreferredSettings.model.train();
        ((MarkovModel) PreferredSettings.model).outputTransition("F:\\m.txt");
        ((MarkovModel) PreferredSettings.model).output();
        
        //****
        System.out.println("**********fdsfsuppression vector training*************");
        SupVec.train(new GreedyTrain());

        //******/
        //************Hybrid checking******************        
        Utility utility = new NumberOfReleasedContext();
        double utility_prob = utility.getUtility(SupVecTrain.p_best);
       
        System.out.println("probabilistic utility "+utility_prob);
        //************************
        double utility_simulatable = utility.getUtility();
        System.out.println("Simulatable utility" + utility_simulatable);
        
        /****************
        int size_observation = t.getObservation().size(); 
        Sequence stemp;
        ArrayList<Integer> out_of_Maskit;     
        
        
        if (utility_prob > utility_simulatable) {
            //Probabilistic checking the other half sequences
        	   
            for (int i = size_observation/2; i < size_observation; i++) {
            	stemp = t.getObservation().get(i);
            	out_of_Maskit = new ArrayList<Integer>();
            	for (int j = 0; j < stemp.getLength(); j++) {
            		if (ProbabilisticPC.okayToRelease(stemp.getContext(j), SupVecTrain.p_best)) {
            			out_of_Maskit.add(1);
            		} else {
            			out_of_Maskit.add(0);
            		}
            	}
            	
            	//write the output sequence of Maskit to a file named "F:\\iout.txt" i means the sequence's index
            	File f = new File("F:\\"+i+"out.txt");
            	FileWriter output = new FileWriter(f);
            	BufferedWriter writer = new BufferedWriter(output);
            	
            	StringBuffer bf;
            	for (int l = 0; l < out_of_Maskit.size(); l++) {
            		bf = new StringBuffer("");
            		bf.append(out_of_Maskit.get(l));
            		bf.append("\n");
            		writer.write(bf.toString());
            	}        	
            	writer.close();
            	out_of_Maskit.clear();
            	out_of_Maskit = null;
            }
        }else {
           // Simulatable checking for the other half contexts
            Sequence currentOutSeq = new Sequence(); //record the current output contexts
            for (int i = size_observation/2; i < size_observation; i++) {
            	stemp = t.getObservation().get(i);
            	out_of_Maskit = new ArrayList<Integer>(); //record 
            	for (int j = 0; j < stemp.getLength(); j++) {
            		if (SimulatableCheck.okayToRelease(stemp.getContext(j).getT(), currentOutSeq)) {
            			((OutputContext)stemp.getContext(j)).setSuppressed(false);
            			currentOutSeq.addContext(stemp.getContext(j));
            			out_of_Maskit.add(1);
            		} else {
            			((OutputContext)stemp.getContext(j)).setSuppressed(true);
            			currentOutSeq.addContext(stemp.getContext(j));
            			out_of_Maskit.add(0);
            		}
            	}
            	
            	//write the output sequence of Maskit to a file named "F:\\iout.txt" i means the sequence's index
            	File f = new File("F:\\"+i+"out.txt");
            	FileWriter output = new FileWriter(f);
            	BufferedWriter writer = new BufferedWriter(output);
            	
            	StringBuffer bf;
            	for (int l = 0; l < out_of_Maskit.size(); l++) {
            		bf = new StringBuffer("");
            		bf.append(out_of_Maskit.get(l));
            		bf.append("\n");
            		writer.write(bf.toString());
            	}        	
            	writer.close();
            }
        }
        ******************************/
	}
}
