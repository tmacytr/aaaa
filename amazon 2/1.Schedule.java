package amazon;

import java.util.*;

class Schedule {
	public static void main(String[] args) {
		//s
		ScheduleRequestInputStream input = new ScheduleRequestInputStream();
		ScheduleRequest req1 = new ScheduleRequest(1, "area1", 1, 4);
		ScheduleRequest req2 = new ScheduleRequest(2, "area1", 7, 12);
		ScheduleRequest req3 = new ScheduleRequest(3, "area1", 13, 17);
		ScheduleRequest req4 = new ScheduleRequest(1, "area1", 19, 19);
		ScheduleRequest req5 = new ScheduleRequest(1, "area1", 3, 5);
		ScheduleRequest req6 = new ScheduleRequest(6, "area1", 10, 20);
		ScheduleRequest req7 = new ScheduleRequest(7, "area1", 1, 7);
		ScheduleRequest req8 = new ScheduleRequest(1, "area1", 10, 12);
		ScheduleRequest req9 = new ScheduleRequest(8, "area1", 15, 20);
		ScheduleRequest req10 = new ScheduleRequest(9, "area1", 2, 5);
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
		schedule(input);
	}
	
	public static void schedule(ScheduleRequestInputStream input){
		if (input.size == 0) {
			System.out.println("Input is Empty!");
			return;
		}
		/*
		HashMap<String, HashMap<Integer, ArrayList<String>>>,外面的HashMap的key是location,里面HashMap的key是time
		ArrayList是某个时间点已经存入的content
		读一个content，如果是从时间1到3，那么在HashMap里key为1 2 3的时间都加入这个content
		如果有冲突就不put进去
*/
		HashMap<String, HashMap<Integer, HashSet<String>>> area = new HashMap<String, HashMap<Integer, ArrayList<String>>>();
		while (input.hasNext()) {
			ScheduleRequest req = input.next();
			if (area.containsKey(req.location)) {
				HashMap<Integer, ArrayList<ScheduleRequest>> cur = area.get(req.location);
				if (isValid(req, cur)) {
					for (int t = req.startTime; t <= req.endTime; t++) {
						if (cur.containsKey(t)) {
							cur.get(t).add(req);
						} else {
							ArrayList<ScheduleRequest> newList = new ArrayList<ScheduleRequest>();
							newList.add(req);
							cur.put(t,newList);
						}
					}
					System.out.println("This request is acceptted, request id: " + req.id);
				} else {
					System.out.println("This request is rejected, request id: " + req.id);
				}
			} else {
				HashMap<Integer, ArrayList<ScheduleRequest>> temp = new HashMap<Integer, ArrayList<ScheduleRequest>>();
				for (int t = req.startTime; t <= req.endTime; t++){
					ArrayList<ScheduleRequest> newList = new ArrayList<ScheduleRequest>();
					newList.add(req);
					temp.put(t,newList);
				}
				area.put(req.location, temp);
	        	System.out.println("This request is acceptted, request id: " + req.id);
			}
				
		}
		HashMap<Integer, ArrayList<ScheduleRequest>> area1 = area.get("area1");
		for (ScheduleRequest tmp : area1.get(19)) {
			System.out.println(tmp.id);
		}	
	}
	
	private static boolean isValid(ScheduleRequest req, HashMap<Integer, ArrayList<ScheduleRequest>> cur) {
		for (int t = req.startTime; t <= req.endTime; t++) {
			if (cur.containsKey(t)) {
				if (cur.get(t).size() == 3) {
					return false;
				}
				for (ScheduleRequest iter : cur.get(t)) {
					if (req.id == iter.id) {
						return false;
					}
				}
			}
		}
		return true;
	}
}