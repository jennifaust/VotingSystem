// Store the votes

import java.util.Random;
import java.io.*;
import java.util.*;
import java.util.Map.*;

public class StoreVotes {
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
			candidateId = map.get(contactId);
			
			// find the candidate Id and add to the vote count
			int idx = findIndex(sCandidateId, candidateId);
			
			if ( idx >= 0 ) {
				sCount[idx]++;
				System.out.println("CandidateId: " + candidateId + " Count: " + sCount[idx]);
			}
			else {
				System.out.println("Candidate does not exist");
			}
		}
	}
	
	
	// Linear-search function to find the index of an element 
    public static int findIndex(String arr[], String t) 
    {
        // if array is Null 
        if (arr == null) { 
            return -1; 
        } 
  
        // find length of array 
        int len = arr.length; 
        int i = 0; 
  
        // traverse in the array 
        while (i < len) { 
  
			//System.out.println("Looking for: " + t + " index: " + i);
  
            // if the i-th element is t 
            // then return the index 
            if (arr[i].equals(t)) { 
                return i; 
            } 
            else { 
                i = i + 1; 
            } 
        } 
        return -1; 
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
	public void displayResults(){
		String[] sId = sCandidateId.clone();
		int[] sCnt = sCount.clone();
		
		int max, temp = 0, index = 0;
		String tempStr = "";
		for (int i = 0; i < sCnt.length; i++) {
			int counter = 0;
			max = sCnt[i];
			for (int j = i + 1; j < sCnt.length; j++) {

				if (sCnt[j] > max) {
					max = sCnt[j];
					index = j;
					counter++;
				}
			}
			
			if (counter > 0) {
				temp = sCnt[index];
				sCnt[index] = sCnt[i];
				sCnt[i] = temp;
				tempStr = sId[index];
				sId[index] = sId[i];
				sId[i] = tempStr;
			}
		}
		
		System.out.println("Candidate Table:");
		for (int i = 0; i < sCnt.length; i++) {
			if(sId[i] != "NULL") {
				System.out.println("CandidateId: " + sId[i] + " Total Votes: " + sCnt[i]);
			}
		}
	}

} //end
