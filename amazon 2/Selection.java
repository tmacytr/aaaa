package amazon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import amazon.SelectionRequestInputStream;

class Selection {
	public static void main(String[] args) {
		ScheduleRequestInputStream input = new ScheduleRequestInputStream();
		ScheduleRequest req1 = new ScheduleRequest(1, "area1", 1, 2);
		ScheduleRequest req2 = new ScheduleRequest(2, "area1", 2, 5);
		ScheduleRequest req3 = new ScheduleRequest(3, "area1", 3, 3);
		ScheduleRequest req4 = new ScheduleRequest(1, "area2", 2, 5);
		ScheduleRequest req5 = new ScheduleRequest(3, "area2", 3, 5);
		ScheduleRequest req6 = new ScheduleRequest(5, "area3", 1, 2);
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
		
		SelectionRequestInputStream inputTime = new SelectionRequestInputStream();
		ArrayList<String> locations = new ArrayList<String>();
		locations.add("area1");
		locations.add("area3");
		locations.add("area5");
		SelectionRequest sel1 = new SelectionRequest(1, locations);
		inputTime.push(sel1);
		
		locationValueMap.put("area1", 1.0);
		locationValueMap.put("area2", 0.8);
		locationValueMap.put("area3", 0.75);
		locationValueMap.put("area4", 0.5);
		locationValueMap.put("area5", 10.0);
		locationValueMap.put("area6", 0.2);
		contentScoreMap.put(1, 100);
		contentScoreMap.put(2, 60);
		contentScoreMap.put(3, 70);
		contentScoreMap.put(4, 90);
		contentScoreMap.put(5, 80);
		selection(input, inputTime);
	}

	private static HashMap<Integer, Integer> contentScoreMap = new HashMap<Integer, Integer>();
	private static HashMap<String, Double> locationValueMap = new HashMap<String,Double>();	
	private static class MaxScore {
		double max; 
		MaxScore() {}
	}
	
	public static void selection(ScheduleRequestInputStream input, SelectionRequestInputStream inputTime) {
		if (input.size == 0 || inputTime.size == 0) {
			System.out.println("Input is Empty!");
			return;
		}
		HashMap<String, HashMap<Integer, ArrayList<ScheduleRequest>>> area = new HashMap<String, HashMap<Integer, ArrayList<ScheduleRequest>>>();
		while(input.hasNext()){
			ScheduleRequest req = input.next();
			if (area.containsKey(req.location)){
				HashMap<Integer,ArrayList<ScheduleRequest>> cur = area.get(req.location);
				for (int t = req.startTime; t <= req.endTime; t++) {
					if (cur.containsKey(t)) {
						cur.get(t).add(req);
					} else {
						ArrayList<ScheduleRequest> newList = new ArrayList<ScheduleRequest>();
						newList.add(req);
						cur.put(t, newList);                  
					}
				}
			} else {
				HashMap<Integer, ArrayList<ScheduleRequest>> temp = new HashMap<Integer, ArrayList<ScheduleRequest>>();
				for(int t = req.startTime; t <= req.endTime; t++){
					ArrayList<ScheduleRequest> newList = new ArrayList<ScheduleRequest>();
					newList.add(req);
					temp.put(t,newList);
				}
				area.put(req.location, temp);
			}
		}
		while (inputTime.hasNext()) {
			SelectionRequest requestInput = inputTime.next();
			int t = requestInput.time;
			ArrayList<String> locations = requestInput.locations;
			ArrayList<ArrayList<ScheduleRequest>> candidate = new ArrayList<ArrayList<ScheduleRequest>>();
			String[] areas = {"area1", "area2", "area3", "area4", "area5", "area6"};
			for (int i = 0; i < 6 ; i++) {
				if (area.get(areas[i]).get(t) != null){
					candidate.add(area.get(areas[i]).get(t));
				} else {
					candidate.add(new ArrayList<ScheduleRequest>());
				}
			}
			/**for (int i = 0; i < candidate.size(); i++) {
				System.out.println("i = " + i);
				for (ScheduleRequest tmp: candidate.get(i)) {
					System.out.print(tmp.id + " ");
				}
				System.out.println();
			}*/
			ArrayList<ScheduleRequest> path = new ArrayList<ScheduleRequest>();
			ArrayList<ScheduleRequest> result = new ArrayList<ScheduleRequest>();
			HashSet<Integer> set = new HashSet<Integer>();
			MaxScore maxScore = new MaxScore();
			helper(candidate, 0, path, 0, maxScore, result, set);
			/**for (ScheduleRequest res: result) {
				System.out.println(res.id);
			}
			System.out.println(maxScore.max);*/
			output(t, locations, result, maxScore);
		}
	}

	private static void helper(ArrayList<ArrayList<ScheduleRequest>> candidate, int index, ArrayList<ScheduleRequest> path, double totalScore, MaxScore maxScore, ArrayList<ScheduleRequest> result, HashSet<Integer> set){
		if (index == candidate.size()) {
			if (totalScore > maxScore.max) {
				maxScore.max = totalScore;
				result.clear();
				result.addAll(new ArrayList<ScheduleRequest>(path));
			}
			return;
		}
		
		if (candidate.get(index).size() == 0) {
			path.add(new ScheduleRequest(0, "", 0, 0));
			helper(candidate, index + 1, path, totalScore, maxScore, result, set);
			path.remove(path.size() - 1);
		} else {
			for (ScheduleRequest cur : candidate.get(index)) {
				double curScore = 0;
				if (set.contains(cur.id)) {
					cur = new ScheduleRequest(0, cur.location, 0, 0);
				} else {
					set.add(cur.id);
					curScore = (double)contentScoreMap.get(cur.id) * locationValueMap.get(cur.location);
				}
				path.add(cur);
				helper(candidate, index + 1, path, totalScore + curScore, maxScore, result, set);
				path.remove(path.size() - 1);
				if (cur.id != 0) {
					set.remove(cur.id);
				}
			}
		}
	}
	
	private static void output(int time, ArrayList<String> locations, ArrayList<ScheduleRequest> result, MaxScore maxScore) {
		String[] areas = {"area1", "area2", "area3", "area4", "area5", "area6"};
		if (result == null || result.size() == 0) {
			System.out.println("empty");
			return;
		}
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < 6; i++) {
			map.put(areas[i], i);
		}
		System.out.print("Time " + time + ": ");
		for (String str: locations) {
			int index = map.get(str);
			if (result.get(index) == null) {
				continue;
			}
			System.out.print(str + ": " + result.get(index).id + " ");
		}
		System.out.print("Max Score: " + maxScore.max);
	}
}