package DataInput.Loaders.TrainData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import DataInput.Filters.Filter;
import Utils.Sequence;

public abstract class DataLoader {
	protected String filename;
	protected ArrayList<Sequence> observation;

	public void output() {
		if (!observation.isEmpty()) {
			for (int i = 0; i < observation.size(); i++) {
				System.out.println("Sequence " + i);
				observation.get(i).output();
				System.out.println();
			}
		} else {
			System.out.println("The observation is null");
		}
	}
	
	public ArrayList<Sequence> getObservation() {
		return observation;
	}
	
	public abstract void getDataSet() throws IOException, MatlabConnectionException, MatlabInvocationException;
	
	public abstract Sequence getLongestTrace();
	
	public int getNumOfData() throws IOException {
		int count = 0;
		BufferedReader br;
		br = new BufferedReader(new FileReader(filename));

		String line = br.readLine();
		while(line !=null){
		  count++;
		  line = br.readLine();
		}
		br.close();
		return count;
	}
}

