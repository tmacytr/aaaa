package amazon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Optimization2 {
	//First available + best fit
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		locationValueMap.put("area1", 1.0);
		locationValueMap.put("area2", 0.8);
		locationValueMap.put("area3", 0.75);
		locationValueMap.put("area4", 0.5);
		locationValueMap.put("area5", 0.3);
		locationValueMap.put("area6", 0.2);
		ScheduleRequestInputStream input = new ScheduleRequestInputStream();
		ScheduleRequest req1 = new ScheduleRequest(1, "area1", 4, 5);
		ScheduleRequest req2 = new ScheduleRequest(2, "area1", 2, 5);
		ScheduleRequest req3 = new ScheduleRequest(3, "area1", 4, 5);
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
		
		Content c1 = new Content(3, 2, 10);
		Content c2 = new Content(4, 100, 15);
		Content c3 = new Content(11, 2, 10);
		OptimizationRequestInputStream inputContent = new OptimizationRequestInputStream();
		//inputContent.push(c1);
		//inputContent.push(c2);
		inputContent.push(c3);
		optimization(input, inputContent);
		for (int time: insertTime) {
			System.out.println("time: " + time + " ");
		}
		for (String str: insertArea) {
			System.out.println("area: " + str + " ");
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
			AvailableTime[] availableTime = findAvailableTime(content, maxTime, area);
			//test block
			for (AvailableTime t: availableTime) {
				System.out.print(t.startTime);
			}
			System.out.println();
			for (AvailableTime t: availableTime) {
				System.out.println(t.availableLength);
			}
			//test bloock
			int minDifference = Integer.MAX_VALUE;
			int index = 0;
			for (int i = 0; i < 6; i++) {
				if (Math.abs(availableTime[i].availableLength - content.length) < minDifference) {
					minDifference = Math.abs(availableTime[i].availableLength - content.length);
					index = i;
				}
			}
			insertTime.add(availableTime[index].startTime);
			insertArea.add(areaIndex[index]);
		}
	}
	
	private static AvailableTime[] findAvailableTime(Content content, int maxTime, HashMap<String, HashMap<Integer, HashSet<ScheduleRequest>>> areas) {
		AvailableTime[] res = new AvailableTime[6];
		for(int i = 0; i < 6; i++) {
			String area = areaIndex[i];
			res[i] = findValid(content, maxTime, area, areas);
		}
		return res;
	}
	
	private static class AvailableTime {
		int startTime;
		int availableLength;
		AvailableTime(int time, int len) {
			this.startTime = time;
			this.availableLength = len;
		}
	}
	
	private static AvailableTime findValid(Content content, int maxTime, String area, HashMap<String, HashMap<Integer, HashSet<ScheduleRequest>>> areas) {
		int len = content.length;
		int time = 1;
		int availableLength = 0;
		while (time <= maxTime) {
			int t = 0;
			for (t = 0; t < len; t++) {
				if (!isValid(content, area, time + t, areas)) {
					time = time + t + 1;
					break;
				}
			}
			if (t == len) {
				break;
			}
		}
		if (time == maxTime + 1) {
			availableLength = Integer.MAX_VALUE;
			AvailableTime res = new AvailableTime(time, availableLength);
			return res;
		}
		for (int t = time + 1; t <= maxTime; t++) {
			if (!isValid(content, area, t, areas)) {
				availableLength = t - time;
				AvailableTime res = new AvailableTime(time, availableLength);
				return res;
			}
		}
		AvailableTime res = new AvailableTime(time, Integer.MAX_VALUE);
		return res;
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
