// Archive the votes

import java.util.Random;
import java.io.*;
import java.util.*;
import java.util.Map.*;

public class ArchiveVotes {
	Map<String, String> map = new HashMap<String, String>();
	
	//public static String sCandidateId[] = new String[] { "1000", "1001", "1002", "1003", "1004", "1005", "1006", "1007", "1008", "1009" };
	//public static int sCount[] = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	
	public static String sCandidateId[] = new String[20];
	public static int sCount[] = new int[20];

	public static void main(String[] args) {
		System.out.println("Successfully compiled");
	}//end main

	// archive the yearId and the candidateId
	public void archive(String yearId, String candidateId, int votes){
		//String candidateId = Integer.toString(id);
		
		//if(map.containsKey(yearId)){
		//	System.out.println("YearId[" + yearId + "] - Year already recorded and archived.");
		//	/*for(Map.Entry<String, String> entry : map.entrySet()){
		//		String key = entry.getKey();
		//		String value = entry.getValue();
		//		System.out.println("Key: " + key + " Value: " + value);
		//	}*/
		//}
		//else {
			map.put(yearId, candidateId);
			candidateId = map.get(yearId);
			
			// find the candidate Id and add to the vote count
			int idx = findIndex(sCandidateId, candidateId);
			
			if ( idx >= 0 ) {
				//sCount[idx]++;
				// add to previous votes archived
				sCount[idx] = sCount[idx] + votes;
				//System.out.println("Year:" + yearId + " CandidateId: " + candidateId + " Count: " + sCount[idx]);
				System.out.println("Year:" + yearId + " CandidateId: " + candidateId + " Count: " + votes);
			}
			else {
				System.out.println("Candidate does not exist");
			}
		//}
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

	public static void initArchive(){
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
	
	public Map<String, String> getMyMap(){
		return map;
	}
	
	public void clear(Map<String, String> myMap){
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
	
		// get predicted winner
	public String getPredictedWinner(){
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
	
	// get predicted winner percent
	public String getPredictedWinnerPercent(){
		int max = sCount[0];
		int index = 0;
		int totalVotes = 0;
		
		for (int i = 0; i < sCount.length; i++){
			if (max < sCount[i]) {
				max = sCount[i];
				index = i;
			}
		}
		
		//winning percent
		//System.out.println("winning votes: " + sCount[index] + " Total Votes: " + getArchiveVotes());
		double percent = (((double)sCount[index] / (double)getArchiveVotes() ) + .0005) * 1000; 
		int pctInt = (int)percent;	// 549
		double pctDbl = pctInt;	//549
		double pct = pctDbl / 10;	// 54.9
		return String.valueOf(pct); 
	}
	
	// get trend
	public static int getArchiveVotes(){
		String[] sId = sCandidateId.clone();
		int[] sCnt = sCount.clone();
		int totalVotes = 0;
		
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
		
		System.out.println("Archive Table: (accumulated votes)");
		for (int i = 0; i < sCnt.length; i++) {
			if(sId[i] != "NULL") {
				//System.out.println("CandidateId: " + sId[i] + " Total Votes: " + sCnt[i]);
				totalVotes = totalVotes + sCnt[i];
			}
		}
		
		double percent = 0;
		int pctInt = 0;
		double pctDbl = 0;
		double pct = 0;
		
		for (int i = 0; i < sCnt.length; i++) {
			if(sId[i] != "NULL") {
				percent = (((double)sCnt[i] / (double)totalVotes )+.0005) * 1000;	// 549
				pctInt = (int)percent;	// 549
				pctDbl = pctInt;	//549
				pct = pctDbl / 10;	// 54.9
				System.out.println("CandidateId: " + sId[i] + " Total Votes: " + sCnt[i] + " with " + String.valueOf(pct) + "%");
			}
		}

		return totalVotes;
	}

} //end
