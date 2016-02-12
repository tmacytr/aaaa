import java.util.*;
public class Solution {
	public static class ScheduleRequest{
		String id;
		String location;
		int startTime;
		int endTime;
		public ScheduleRequest(String content, String area, int start, int end){
			id = content;
			location = area;
			startTime = start;
			endTime = end;
		}
	}
	public static class Area{
		String id;
		double value;
		public Area(String s, double val){
			id = s;
			value = val;
		}
	}
	public static class Content{
		String id;
		int score;
		public Content(String s, int val){
			id = s;
			score = val;
		}
	}
	public static class NewContent{
		String id;
		int score;
		int len;
		public NewContent(String s, int sc, int length){
			id = s;
			score = sc;
			len = length;
		}
	}
	public static class Location{
		String id;
		int startTime;
		public Location(String s, int start){
			id = s;
			startTime = start;
		}
	}
	public static class AreaTimeDuration{
		int startTime;
		int endTime;
		public AreaTimeDuration(int start, int end){
			startTime = start;
			endTime = end;
		}
	}
	public static List<Location> optimazationProblem(List<ScheduleRequest> scheduleRequests, List<NewContent> optimazationContents, Map<String, Integer> contentScoreMap, Map<String, Double> locationValueMap){
		Map<String, AreaTimeDuration> areaDurationTime = new HashMap<String, AreaTimeDuration>();
		Map<Integer, String> areaIDMap = new HashMap<Integer, String>();
		Map<String, List<ScheduleRequest>> areaToContentMap = new HashMap<String, List<ScheduleRequest>>();
		Map<String, Map<Integer, List<String>>> areaToTimeMap = new HashMap<String, Map<Integer, List<String>>>();
		List<int[]> timeLeftArray = new ArrayList<int[]>();
		List<Area> areaList = new ArrayList<Area>();
		for(String areaID : locationValueMap.keySet()){
			Area newArea = new Area(areaID, locationValueMap.get(areaID));
			areaList.add(newArea);
		}
		Collections.sort(areaList, new Comparator<Area>(){
			public int compare(Area a, Area b){
				if(b.value > a.value)
					return 1;
				else if(b.value == a.value)
					return 0;
				else
					return -1;
			}
		});
		for(int i = 0; i < areaList.size(); i++){
			areaIDMap.put(i, areaList.get(i).id);
			//System.out.println(areaList.get(i).id);
		}
		
		for(ScheduleRequest single : scheduleRequests){
			String area = single.location;
			if(areaToContentMap.containsKey(area)){
				Map<Integer, List<String>> timeMap = areaToTimeMap.get(area);
				for(int i = single.startTime; i <= single.endTime; i++){
					if(!timeMap.containsKey(i)){
						List<String> timeToContent = new ArrayList<String>();
						timeMap.put(i, timeToContent);
					}
				}
				areaToContentMap.get(area).add(single);
			}
			else{
				List<ScheduleRequest> contents = new ArrayList<ScheduleRequest>();
				Map<Integer, List<String>> timeMap = new HashMap<Integer, List<String>>();
				for(int i = single.startTime; i <= single.endTime; i++){
					List<String> timeToContent = new ArrayList<String>();
					timeMap.put(i, timeToContent);
				}
				contents.add(single);
				areaToTimeMap.put(area, timeMap);
				areaToContentMap.put(area, contents);
			}
		}
		
		for(String area : areaToContentMap.keySet()){
			List<ScheduleRequest> scheduleList = areaToContentMap.get(area);
//			System.out.println(area);
			int minStartTime = Integer.MAX_VALUE;
			int maxEndTime = Integer.MIN_VALUE;
			Map<Integer, List<String>> timeMap = areaToTimeMap.get(area);
			for(ScheduleRequest single : scheduleList){
				minStartTime = Math.min(minStartTime, single.startTime);
				maxEndTime = Math.max(maxEndTime, single.endTime);
				for(int i = single.startTime; i <= single.endTime; i++){
					//area 1 time map:{1=[1, 7], 2=[1, 1, 7], 3=[1, 1, 7], 4=[1, 1, 7], 5=[1, 7], 6=[7], 7=[2, 7], 8=[2], 9=[2], 10=[2, 6, 1], 11=[2, 6, 1], 12=[2, 6, 1], 13=[3, 6], 14=[3, 6], 15=[3, 6, 8], 17=[3, 6, 8], 16=[3, 6, 8], 19=[1, 6, 8], 18=[6, 8], 20=[6, 8]}
					timeMap.get(i).add(single.id);
				}
			}
			System.out.println(timeMap.keySet().size());
//			areaToTimeMap.put(area, timeMap);
			AreaTimeDuration durTime = new AreaTimeDuration(minStartTime, maxEndTime);
			//{2=Solution$AreaTimeDuration@621a193a, 1=Solution$AreaTimeDuration@34dd0145}
			areaDurationTime.put(area, durTime);
		}
//		for(String temp : areaDurationTime.keySet()){
//			AreaTimeDuration cur = areaDurationTime.get(temp);
//			System.out.println(temp);
//			System.out.println(cur.startTime);
//			System.out.println(cur.endTime);
//		}
	//	for(String temp : areaToTimeMap.keySet())
			//System.out.println('*' + temp);
		for(Area area : areaList){
			if(!areaToTimeMap.containsKey(area.id))
				continue;
			//System.out.println(area.id);
			AreaTimeDuration durTime = areaDurationTime.get(area.id);
			Map<Integer, List<String>> timeToContentMap = areaToTimeMap.get(area.id);
			//0-20
			int[] leftTime = new int[durTime.endTime - durTime.startTime + 2];
			//[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
			leftTime[leftTime.length - 1] = 0;
			int dif = durTime.startTime;
			System.out.println(dif);
			for(int i = leftTime.length - 2; i >= 0; i--){
				if(!timeToContentMap.containsKey(i + dif)){
					leftTime[i] = leftTime[i + 1] + 1;
					//[1, 0, 2, 1, 0, 4, 3, 2, 1, 0, 0, 0, 2, 1, 0, 0, 0, 1, 0, 1, 0]
//					System.out.println(leftTime[]);

				}
				else{
					List<String> contents = timeToContentMap.get(i + dif);
					if(contents.size() == 3)
						leftTime[i] = 0;
					else
						leftTime[i] = leftTime[i + 1] + 1;
				}
			}

			timeLeftArray.add(leftTime);
		}
		
//		for(int i = 0; i < timeLeftArray.size(); i++){
//			int[] temp = timeLeftArray.get(i);
//			for(int j = 0; j < temp.length; j++){
//				System.out.print(temp[j]);
//				System.out.print(" ");
//			}
//			System.out.println("");
//		}
		List<Location> results = new ArrayList<Location>();
		for(NewContent content : optimazationContents){
			int score = Integer.MAX_VALUE;
			int begin = Integer.MAX_VALUE;
			Location newContentLocation = new Location("", 0);
			for(int i = 0; i < timeLeftArray.size(); i++){
				int dif = areaDurationTime.get(areaIDMap.get(i)).startTime;
		//		System.out.println(dif);
				Map<Integer, List<String>> timeToContentMap = areaToTimeMap.get(areaIDMap.get(i));
				List<Integer> notAvaliableTime = new ArrayList<Integer>();
				for(Integer time : timeToContentMap.keySet()){
					List<String> contents = timeToContentMap.get(time);
					if(contents.contains(content.id))
						notAvaliableTime.add(time - dif);
					//[6, 7, 8, 9, 10, 11]
				}
//				for(int k = 0; k < notAvaliableTime.size(); k++){
//					
//					System.out.print(notAvaliableTime.get(k));
//					System.out.print(" ");
//				}
//				System.out.println(" ");
				int[] leftTime = timeLeftArray.get(i);
				for(int j = 0; j < leftTime.length; j++){
					if(leftTime[j] >= content.len){
						int flag = 0;
						for(int k = j; k < (j + content.len); k++)
							if(notAvaliableTime.contains(k)){
								flag = 1;
								break;
							}
						if(flag == 1){
//							System.out.println(0);
							continue;
						}
						if((leftTime[j] / content.len) < score){
							score = leftTime[j] / content.len;
							System.out.println(score);
							begin = j;
							newContentLocation.id = areaIDMap.get(i);
							newContentLocation.startTime = begin + dif;
						}
						else if((leftTime[j] / content.len) == score){
							if(j < begin){
								begin = j;
								newContentLocation.id = areaIDMap.get(i);
								newContentLocation.startTime = begin + dif;
							}
						}
					}
				}	
			}
			results.add(newContentLocation);
		}
		return results;
		
	}
	private static Map<Double, List<String>> resultMap = new HashMap<Double, List<String>>();
	private static double maxScore = 0;
	public static List<List<String>> selectionProblem(List<ScheduleRequest> scheduleRequests, List<Integer> timeList, Map<String, Integer> contentScoreMap, Map<String, Double> locationValueMap){
		//{2={3=[1, 9, 5, 7], 4=[1, 9, 5, 7], 11=[2, 6, 1]}, 1={3=[1, 1, 6, 7, 8], 4=[1, 1, 6, 7, 8], 11=[2, 1, 8]}}
		Map<String, Map<Integer, List<String>>> areaToTimeMap = new HashMap<String, Map<Integer, List<String>>>();
		Map<String, List<ScheduleRequest>> areaToContentMap = new HashMap<String, List<ScheduleRequest>>();
		Map<Integer, String> areaIDMap = new HashMap<Integer, String>();
		List<Area> areaList = new ArrayList<Area>();
		
		for(String areaID : locationValueMap.keySet()){
			Area newArea = new Area(areaID, locationValueMap.get(areaID));
			//copy the location value map, from high to low
			areaList.add(newArea);
		}
		Collections.sort(areaList, new Comparator<Area>(){
			@Override
			public int compare(Area a, Area b){
				if(b.value > a.value)
					return 1;
				else if(b.value == a.value)
					return 0;
				else
					return -1;
			}
		});
		//make a index
		for(int i = 0; i < areaList.size(); i++){
			Area cur = areaList.get(i);
			//0：ID1  1:Id2  corespond table  {0=1, 1=2, 2=3, 3=4, 4=5, 5=6}
			areaIDMap.put(i, cur.id);
		}
		//store the whole information of the request
		for(ScheduleRequest single : scheduleRequests){
			String area = single.location;
			if(areaToContentMap.containsKey(area))
				areaToContentMap.get(area).add(single);
			else{
				List<ScheduleRequest> contentList = new ArrayList<ScheduleRequest>();
				Map<Integer, List<String>> timeToContentMap = new HashMap<Integer, List<String>>();
				for(Integer time : timeList){
					List<String> contents = new ArrayList<String>();
					//contents is null now,  3 = []
					timeToContentMap.put(time, contents);
				}
				contentList.add(single);
				//{1={3=[], 4=[], 11=[]}}	{2={3=[1, 9, 5, 7], 4=[1, 9, 5, 7], 11=[2, 6, 1]}, 1={3=[], 4=[], 11=[]}}
				areaToTimeMap.put(area, timeToContentMap);
				//add the first request
				areaToContentMap.put(area, contentList);
			}
		}
		for(String area : areaToContentMap.keySet()){
			//areaToContentMap after delete the area ID 
			List<ScheduleRequest> scheduleList = areaToContentMap.get(area);
			Map<Integer, List<String>> timeToContentMap = areaToTimeMap.get(area);
			for(ScheduleRequest content : scheduleList){
				for(int i = content.startTime; i <= content.endTime; i++){
					//find content on time 3 4 11
					if(timeToContentMap.containsKey(i))
						timeToContentMap.get(i).add(content.id);
				}
			}
		}
		List<List<String>> results = new ArrayList<List<String>>();
		for(Integer time : timeList){
			maxScore = Integer.MIN_VALUE;
			List<List<Content>> areaContentList = new ArrayList<List<Content>>();
			for(Area area : areaList){
				//{2={3=[1, 9, 5, 7], 4=[1, 9, 5, 7], 11=[2, 6, 1]}, 1={3=[1, 1, 6, 7, 8], 4=[1, 1, 6, 7, 8], 11=[2, 1, 8]}}
				if(!areaToTimeMap.containsKey(area.id))
					continue;
				//{3=[1, 1, 6, 7, 8], 4=[1, 1, 6, 7, 8], 11=[2, 1, 8]}
				Map<Integer, List<String>> timeToContentMap = areaToTimeMap.get(area.id);
				List<String> contents = timeToContentMap.get(time);
				List<Content> contentList = new ArrayList<Content>();
				for(String cur : contents){
					Content temp = new Content(cur, contentScoreMap.get(cur));
					contentList.add(temp);
				}
				//contentList:content value from high to low
				Collections.sort(contentList, new Comparator<Content>(){
					@Override
					public int compare(Content c1, Content c2){
						return c2.score - c1.score;
					}
				});
				areaContentList.add(contentList);
				//76118 7951
			}
			for(int i = 0; i < areaContentList.size(); i++){
				List<Content> cur = areaContentList.get(i);
				System.out.println("*******");
				for(Content s : cur){
					System.out.println(s.id);
				}
			}
			List<String> result = new ArrayList<String>();
			dfs(areaContentList, result, areaList, 0, 0);
			
			results.add(resultMap.get(maxScore));
		}
		return results;
		
	}
	public static int dfs(List<List<Content>> areaContentList, List<String> result, List<Area> areaList, double sum, int index){
		if(index == areaContentList.size()){
			if(sum > maxScore)
				resultMap.put(sum, new ArrayList(result));
			maxScore = Math.max(maxScore, sum);
			System.out.print("MAXScore:");
			System.out.println(maxScore);
			return 0;
		}
//		for(Area area : areaList){
//			System.out.println(area.id);
//		}
		List<Content> curAreaContent = areaContentList.get(index);
		if(curAreaContent.size() == 0){
			result.add("null");
			dfs(areaContentList, result, areaList, sum, index + 1);
			result.remove(result.size() - 1);
			return 0;
		}
		for(int i = 0; i < curAreaContent.size(); i++){
			Content cur = curAreaContent.get(i);
			double curScore = areaList.get(index).value * cur.score;
			
			if(result.contains(cur.id))
				curScore = 0;
			System.out.print(cur.id + ":");
			System.out.println(curScore);
			//predict score
			double predict = sum + curScore;
			for(int j = index + 1; j < areaContentList.size(); j++){
				List<Content> followingContent = areaContentList.get(index);
				if(followingContent.size() == 0)
					continue;
				predict += followingContent.get(0).score * areaList.get(j).value;
			}
			if(predict < maxScore)
				return 1;
			
			if(result.contains(cur.id))
				result.add("null");
			else
				result.add(cur.id);
			if(dfs(areaContentList, result, areaList, sum + curScore, index + 1) == 1)
				return 1;
			result.remove(result.size() - 1);
		}
		return 0;
		
	}
	/**
	 * @parameter scheduleR
	 */
	public static List<ScheduleRequest> scheduleProblem(List<ScheduleRequest> scheduleRequests){
		Map<String, Map<Integer, Set<String>>> areaToTimeMap = new HashMap<String, Map<Integer, Set<String>>>();
		Map<String, List<ScheduleRequest>> areaToContentMap = new HashMap<String, List<ScheduleRequest>>();
		List<ScheduleRequest> validSchedule = new ArrayList<ScheduleRequest>();
		List<ScheduleRequest> inValidSchedule = new ArrayList<ScheduleRequest>();
		PriorityQueue<ScheduleRequest> scheduleQue = new PriorityQueue<ScheduleRequest>(scheduleRequests.size(), new Comparator<ScheduleRequest>(){
			@Override
			public int compare(ScheduleRequest a, ScheduleRequest b){
				return a.endTime - b.endTime;
			}
		});
		
//		Map<Integer, Set<String>> timeToContentMap = new HashMap<Integer, Set<String>>();
		for(ScheduleRequest single : scheduleRequests){
			String areaID = single.location;
			if(areaToTimeMap.containsKey(areaID)){
				Map<Integer, Set<String>> timeMap = areaToTimeMap.get(areaID);
				for(int i = single.startTime; i <= single.endTime; i++){
					if(!timeMap.containsKey(i)){
						Set<String> timeToContent = new HashSet<String>();
						timeMap.put(i, timeToContent);
					}
				}
				areaToContentMap.get(areaID).add(single);
			}
			else{
				Map<Integer, Set<String>> timeMap = new HashMap<Integer, Set<String>>();
				List<ScheduleRequest> contentList = new ArrayList<ScheduleRequest>();
				for(int i = single.startTime; i <= single.endTime; i++){
					Set<String> timeToContent = new HashSet<String>();
					timeMap.put(i, timeToContent);
				}
				contentList.add(single);
				areaToTimeMap.put(areaID, timeMap);
				areaToContentMap.put(areaID, contentList);
			}
		}
		for(String area : areaToContentMap.keySet()){
			List<ScheduleRequest> scheduleList = areaToContentMap.get(area);
//			if(scheduleList.size() != 0)
//				return scheduleList;
			for(ScheduleRequest schedule : scheduleList){
				scheduleQue.offer(schedule);
			}
			Map<String, int[]> contentDuration = new HashMap<String, int[]>();
			while(scheduleQue.size() != 0){
				ScheduleRequest cur = scheduleQue.poll();
				if(contentDuration.containsKey(cur.id)){
					int[] temp = contentDuration.get(cur.id);
					if(cur.startTime < temp[1]){
						inValidSchedule.add(cur);
						scheduleList.remove(cur);
					}
					else{
						temp[0] = cur.startTime;
						temp[1] = cur.endTime;
						contentDuration.put(cur.id, temp);
					}
						
				}
				else{
					int[] temp = new int[2];
					temp[0] = cur.startTime;
					temp[1] = cur.endTime;
					contentDuration.put(cur.id, temp);
				}
			}
		}
		for(String area : areaToContentMap.keySet()){
			List<ScheduleRequest> scheduleList = areaToContentMap.get(area);
//			if(scheduleList.size() != 0)
//				return scheduleList;
			Map<Integer, Set<String>> timeToContentMap = areaToTimeMap.get(area);
			for(ScheduleRequest schedule : scheduleList){
				int flag = 0;
				for(int i = schedule.startTime; i <= schedule.endTime; i++){
					Set<String> contentSet = timeToContentMap.get(i);
					if(contentSet.size() == 3){
//						inValidSchedule.add(schedule);
						flag = 1;
						break;
					}
					else{
						if(contentSet.add(schedule.id))
							continue;
						else{
//							inValidSchedule.add(schedule);
							flag = 1;
							break;
						}
					}
				}
				if(flag == 0)
					validSchedule.add(schedule);
				else
					inValidSchedule.add(schedule);
			}
		}
		return inValidSchedule;
	}
	
	public static void main(String[] argv){
		List<ScheduleRequest> scheduleRequests = new ArrayList<ScheduleRequest>();
		List<Integer> timeList = new ArrayList<Integer>();
		Map<String, Integer> contentScoreMap = new HashMap<String, Integer>();
		Map<String, Double> locationValueMap = new HashMap<String, Double>();
		List<NewContent> optimazationContents = new ArrayList<NewContent>();
		optimazationContents.add(new NewContent("2", 6, 2));
		optimazationContents.add(new NewContent("3", 6, 5));
//		optimazationContents.add(new NewContent("2", 6, 2));
		timeList.add(3);
		timeList.add(4);
		timeList.add(11);
		locationValueMap.put("1", 1.0);
		locationValueMap.put("2", 0.8);
		locationValueMap.put("3", 0.75);
		locationValueMap.put("4", 0.5);
		locationValueMap.put("5", 0.3);
		locationValueMap.put("6", 0.2);
		contentScoreMap.put("1", 2);
		contentScoreMap.put("2", 6);
		contentScoreMap.put("3", 5);
		contentScoreMap.put("4", 8);
		contentScoreMap.put("5", 3);
		contentScoreMap.put("6", 7);
		contentScoreMap.put("7", 9);
		contentScoreMap.put("8", 1);
		contentScoreMap.put("9", 4);
;		scheduleRequests.add(new ScheduleRequest("1", "1", 1, 4));
scheduleRequests.add(new ScheduleRequest("2", "1", 7, 12));
scheduleRequests.add(new ScheduleRequest("3", "1", 13, 17));
scheduleRequests.add(new ScheduleRequest("1", "1", 19, 19));
//scheduleRequests.add(new ScheduleRequest("9", "1", 2, 5));
scheduleRequests.add(new ScheduleRequest("1", "1", 2, 5));
scheduleRequests.add(new ScheduleRequest("6", "1", 10, 20));
scheduleRequests.add(new ScheduleRequest("7", "1", 1, 7));
scheduleRequests.add(new ScheduleRequest("1", "1", 10, 12));
scheduleRequests.add(new ScheduleRequest("8", "1", 15, 20));

scheduleRequests.add(new ScheduleRequest("1", "2", 1, 4));
scheduleRequests.add(new ScheduleRequest("2", "2", 7, 12));
scheduleRequests.add(new ScheduleRequest("3", "2", 13, 17));
scheduleRequests.add(new ScheduleRequest("1", "2", 19, 19));
scheduleRequests.add(new ScheduleRequest("9", "2", 2, 5));
scheduleRequests.add(new ScheduleRequest("5", "2", 3, 5));
scheduleRequests.add(new ScheduleRequest("6", "2", 10, 20));
scheduleRequests.add(new ScheduleRequest("7", "2", 1, 7));
scheduleRequests.add(new ScheduleRequest("1", "2", 10, 12));
scheduleRequests.add(new ScheduleRequest("8", "2", 15, 20));
		List<Location> results = optimazationProblem(scheduleRequests, optimazationContents, contentScoreMap, locationValueMap);
		System.out.println(results.size());
		for(Location result : results){
			System.out.print(result.id);
			System.out.print(" ");
			System.out.println(result.startTime);
		}
//		List<List<String>> result = selectionProblem(scheduleRequests, timeList, contentScoreMap, locationValueMap);
//	for(int i = 0; i < result.size(); i++){
//			List<String> cur = result.get(i);
//			System.out.print("Time:");
//			System.out.println(timeList.get(i));
//			System.out.println(cur.size());
//			for(String s : cur){
//				System.out.println(s);
//			}
//		}
		
//		List<ScheduleRequest> result = scheduleProblem(scheduleRequests);
//		for(ScheduleRequest single : result){
//			System.out.println("ID" + single.id);
//			System.out.println("areaID" + single.location);
//		    System.out.print("Starttime:");
//			System.out.println(single.startTime);
//			System.out.print("Endtime:");
//			System.out.println(single.endTime);
//		}
	}
	
}

