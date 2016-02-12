package amazon;
import amazon.Content;
import amazon.ScheduleRequestInputStream;
import amazon.OptimizationRequestInputStream;

import java.util.*;

public class Optimization1 {
	//first available + max profit
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		locationValueMap.put("area1", 1.0);
		locationValueMap.put("area2", 0.8);
		locationValueMap.put("area3", 0.75);
		locationValueMap.put("area4", 0.5);
		locationValueMap.put("area5", 0.3);
		locationValueMap.put("area6", 0.2);
		ScheduleRequestInputStream input = new ScheduleRequestInputStream();
		ScheduleRequest req1 = new ScheduleRequest(1, "area1", 1, 2);
		ScheduleRequest req2 = new ScheduleRequest(2, "area1", 2, 5);
		ScheduleRequest req3 = new ScheduleRequest(3, "area1", 3, 3);
		ScheduleRequest req4 = new ScheduleRequest(1, "area2", 1, 5);
		ScheduleRequest req5 = new ScheduleRequest(3, "area2", 1, 5);
		ScheduleRequest req6 = new ScheduleRequest(5, "area2", 1, 2);
		ScheduleRequest req7 = new ScheduleRequest(2, "area3", 4, 5);
		ScheduleRequest req8 = new ScheduleRequest(3, "area3", 2, 4);
		ScheduleRequest req9 = new ScheduleRequest(4, "area4", 3, 5);
		ScheduleRequest req10 = new ScheduleRequest(1, "area5", 1, 4);
		ScheduleRequest req11 = new ScheduleRequest(3, "area6", 3, 5);
		input.push(req1);
		input.push(req2);
		input.push(req3);
		input.push(req4);
		input.push(req5);
		input.push(req6);
		input.push(req7);
		input.push(req8);
		input.push(req9);
		input.push(req10);
		input.push(req11);
		Content c1 = new Content(3, 4, 10);
		Content c2 = new Content(9, 100, 15);
		Content c3 = new Content(3, 5, 10);
		OptimizationRequestInputStream inputContent = new OptimizationRequestInputStream();
		//inputContent.push(c1);
		//inputContent.push(c2);
		inputContent.push(c3);
		optimization(input, inputContent);
		for (int t: insertTime) {
			System.out.println(t);
		}
		for (String str: insertArea) {
			System.out.println(str);
		}
	}
	
	private static String[] areaIndex = {"area1", "area2", "area3", "area4", "area5", "area6"};
	
	private static ArrayList<Integer> insertTime = new ArrayList<Integer>();
	private static ArrayList<String> insertArea = new ArrayList<String>();
	
	private static HashMap<Integer, Integer> contentScoreMap = new HashMap<Integer, Integer>();
	private static HashMap<String, Double> locationValueMap = new HashMap<String,Double>();
	
	public static void optimization(ScheduleRequestInputStream input, OptimizationRequestInputStream inputContent)  {
		int maxTime = 0;
		HashMap<String, HashMap<Integer, HashSet<ScheduleRequest>>> area = new HashMap<String, HashMap<Integer, HashSet<ScheduleRequest>>>();
		while(input.hasNext()){
			ScheduleRequest req = input.next();
			if (req.endTime > maxTime) {
				maxTime = req.endTime;
			}
			if (area.containsKey(req.location)){
				HashMap<Integer,HashSet<ScheduleRequest>> cur = area.get(req.location);
				for (int t = req.startTime; t <= req.endTime; t++) {
					if (cur.containsKey(t)) {
						cur.get(t).add(req);
					} else {
						HashSet<ScheduleRequest> newSet = new HashSet<ScheduleRequest>();
						newSet.add(req);
						cur.put(t, newSet);                  
					}
				}
			} else {
				HashMap<Integer, HashSet<ScheduleRequest>> temp = new HashMap<Integer, HashSet<ScheduleRequest>>();
				for(int t = req.startTime; t <= req.endTime; t++){
					HashSet<ScheduleRequest> newSet = new HashSet<ScheduleRequest>();
					newSet.add(req);
					temp.put(t,newSet);
				}
				area.put(req.location, temp);
			}
		}
		
		while(inputContent.hasNext()) {
			Content content = inputContent.next();
			int[] availableTime = findAvailableTime(content, maxTime, area);
			System.out.println("availableTime");
			for (int i: availableTime) {
				System.out.print(i);
			}
			System.out.println();
			int meanTime = maxTime / 2;
			int timeResult = 0;
			String areaResult = "";
			double maxExpectation = 0;
			for (int i = 0; i < 6; i++) {
				double expectation = calculateExpectation(availableTime[i], areaIndex[i], meanTime, content);
				System.out.println("expectation: " + expectation);
				if (expectation >= maxExpectation) {
					maxExpectation = expectation;
					timeResult = availableTime[i];
					areaResult = areaIndex[i];
				}
			}
			insertTime.add(timeResult);
			insertArea.add(areaResult);
		}
	}
	
	private static double calculateExpectation(int startTime, String area, int meanTime, Content content) {
		double expectation = 0;
		if (startTime < meanTime) {
			expectation = locationValueMap.get(area);
		} else {
			expectation = locationValueMap.get(area) * 0.5;
		}
		return expectation;
	}
	
	private static int[] findAvailableTime(Content content, int maxTime, HashMap<String, HashMap<Integer, HashSet<ScheduleRequest>>> areas) {
		int[] res = new int[6];
		for(int i = 0; i < 6; i++) {
			String area = areaIndex[i];
			res[i] = findValid(content, maxTime, area, areas);
		}
		return res;
	}
	
	private static int findValid(Content content, int maxTime, String area, HashMap<String, HashMap<Integer, HashSet<ScheduleRequest>>> areas) {
		int len = content.length;
		int time = 1;
		while (time <= maxTime) {
			for (int t = 0; t < len; t++) {
				if (!isValid(content, area, time + t, areas)) {
					time = time + t + 1;
					break;
				}
				if (t == len - 1) {
					return time;
				}
			}
		}
		return time;
	}
	
	private static boolean isValid(Content content, String area, int time, HashMap<String, HashMap<Integer, HashSet<ScheduleRequest>>> areas) {
		HashMap<Integer, HashSet<ScheduleRequest>> curArea = areas.get(area);
		//current area current time is empty
		if (!curArea.containsKey(time)) {
			return true;
		} else {
			if (curArea.get(time).size() == 0) {
				return true;
			}
			//check if the size of each area each time is larger than three
			if (curArea.get(time).size() == 3) {
				return false;
			}
			//curArea has the same content as the new content
			for (ScheduleRequest tmpContent: curArea.get(time)) {
				if (tmpContent.id == content.id) {
					return false;
				}
			}
		}
		return true;
	}
}
