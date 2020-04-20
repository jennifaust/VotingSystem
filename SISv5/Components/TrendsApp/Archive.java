// Store the votes

import java.util.Random;
import java.io.*;
import java.util.*;
import java.util.Map.*;

public class Archive {
	Map<String, String> map = new HashMap<String, String>();
	public static boolean pollOpen = false;
	
	//public static String sCandidateId[] = new String[] { "1000", "1001", "1002", "1003", "1004", "1005", "1006", "1007", "1008", "1009" };
	//public static int sCount[] = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	
	public static String sCandidateId[] = new String[20];
	public static int sCount[] = new int[20];

	public static void main(String[] args) {
		System.out.println("Successfully compiled");
	}//end main

	// store the contactId and the candidateId
	public void store(String contactId, String candidateId){
		//String candidateId = Integer.toString(id);
		
		if(map.containsKey(contactId)){
			System.out.println("ContactId[" + contactId + "] - You've already voted.");
			/*for(Map.Entry<String, String> entry : map.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				System.out.println("Key: " + key + " Value: " + value);
			}*/
		}
		else {
			map.put(contactId, candidateId);
			for(Map.Entry<String, String> entry : map.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				System.out.println("Key: " + key + " Value: " + value);
			}
			// find the candidate Id and add to the vote count
	
		}
	}
	

	public static void openPoll(){
		pollOpen = true;
		//String sCandidateId[] = new String[] { "1000", "1001", "1002", "1003", "1004", "1005", "1006", "1007", "1008", "1009" };
		//String sCandidateId[] = new String[20];
		
		for (int i = 0; i < sCount.length; i++) {
			sCandidateId[i] = "NULL";
			sCount[i] = 0;
		}
	}
	
	public static void addCandidateId(String newId, int index){
		sCandidateId[index] = newId;
		sCount[index] = 0;
	}
	
	public static void closePoll(){
		pollOpen = false;
	}
	
	public static boolean getPollStatus(){
		return pollOpen;
	}
	
	public Map<String, String> getMyMap(){
		return map;
	}
	
	public void clear(Map<String, String> myMap){
		pollOpen = false;
		//System.out.println("inside clear method");
		//myMap.put("testkey", "testvalue");
		//for(Map.Entry<String, String> entry : myMap.entrySet()){
			//System.out.println("inside clear loop");
			//String key = entry.getKey();
	 		//String value = entry.getValue();
	 		//System.out.println("Key: " + key + " Value: " + value);
		//}
		myMap.clear();
	}
	
	// get winner (first place)
	public String getWinner(){
		int max = sCount[0];
		int index = 0;

		for (int i = 0; i < sCount.length; i++){
			if (max < sCount[i]) {
				max = sCount[i];
				index = i;
			}
		}
		return sCandidateId[index];
	}
	
	// get runner up (second place)
	public String getRunnerUp(){
		int largest = sCount[0];
		int index1 = 0;
		int index2 = 0;
		int secondLargest = 0;
		
		//for (int i = 0; i < sCount.length; i++) {
			//System.out.println(sCount[i]+"\t");
		//}
		for (int i = 0; i < sCount.length; i++) {
 
			if (sCount[i] > largest) {
				secondLargest = largest;
				largest = sCount[i];
				index1 = i;
 			}
			else if (sCount[i] > secondLargest && (index1 != i)) {
				secondLargest = sCount[i];
 				index2 = i;
			}
			
			//System.out.println("Largest: " + largest + " Second: " + "secondLargest: " + secondLargest);
			
		}
		return sCandidateId[index2];
	}
	
	// get trend
	public void displayPrediction(){
		int counter1002 = 0;
		int counter1003 = 0; 
		for(Map.Entry<String, String> entry : map.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			if(value.equals("1002")){
				counter1002 += 1;
			}else{
				counter1003 += 1;
			}
			System.out.println("Counter1002: " + counter1002);
			System.out.println("Counter1003: " + counter1003);
			System.out.println("Key: " + key + " Value: " + value);
		}
		int total = counter1002 + counter1003;
		double predict1002 = ((double) counter1002 / (double) total) * 100;
		double predict1003 = ((double) counter1003 / (double) total) * 100;
		System.out.println("Prediction for 2020");
		System.out.println("Candidate 1002 Prediction: " + predict1002 + "%");
		System.out.println("Candidate 1003 Prediction: " + predict1003 + "%");
	}

} //end
