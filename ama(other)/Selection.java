package amazon;

import java.util.*;

public class Selection {
	public static ArrayList<RealContent> selectSchedule(int moment, List<Area> areaTotal) {
		ArrayList<ArrayList<RealContent>> resultSchedule = new ArrayList<ArrayList<RealContent>>();
		ArrayList<RealContent> list1 = new ArrayList<RealContent>();
		ArrayList<RealContent> list2 = new ArrayList<RealContent>();
		ArrayList<RealContent> list3 = new ArrayList<RealContent>();
		ArrayList<RealContent> list4 = new ArrayList<RealContent>();
		ArrayList<RealContent> list5 = new ArrayList<RealContent>();
		ArrayList<RealContent> list6 = new ArrayList<RealContent>();
		resultSchedule.add(list1);
		resultSchedule.add(list2);
		resultSchedule.add(list3);
		resultSchedule.add(list4);
		resultSchedule.add(list5);
		resultSchedule.add(list6);

		for (int i = 0; i < areaTotal.size(); i++) {
			for (int j = 0; j < areaTotal.get(i).getValidSchedule().size(); j++) {
				RealContent cur = areaTotal.get(i).getValidSchedule().get(j);
				if (cur.getTimeStamp() <= moment && cur.getTimeStamp() + cur.getLength() > moment) {
					resultSchedule.get(i).add(cur);
				}
			}
		}
		int curRes = 0;
		ArrayList<RealContent> curItem = new ArrayList<RealContent>();
		ArrayList<RealContent> result = new ArrayList<RealContent>();
		dfs(0, curRes, areaTotal, resultSchedule, visited, curItem, result);

		System.out.println("\nSecond step: Get Final(Max Valued) Schedule.");
		for (int i = 0; i < result.size(); i++) {
			System.out.print("Area" + (i + 1) + ": ");
			System.out.println(result.get(i).getId() + " ");
		}
		System.out.println("\nMax Value: " + max);
		return result;
	}

	private static int max = 0;
	private static HashSet<String> visited = new HashSet<String>();

	private static void dfs(int start, int curRes, List<Area> areaTotal,
			ArrayList<ArrayList<RealContent>> resultSchedule, HashSet<String> visited, ArrayList<RealContent> curItem,
			ArrayList<RealContent> result) {
		if (start == 6) {
			if (curRes > max) {
				max = curRes;
				result.clear();
				result.addAll(curItem);
			}
			return;
		}
		ArrayList<RealContent> list = resultSchedule.get(start);
		if (list.size() == 0) {
			dfs(start + 1, curRes, areaTotal, resultSchedule, visited, curItem, result);
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (!visited.contains(list.get(i).getId())) {
					RealContent cur = list.get(i);
					curRes += cur.getValue() * areaTotal.get(start).getWeight();
					curItem.add(cur);
					visited.add(cur.getId());

					dfs(start + 1, curRes, areaTotal, resultSchedule, visited, curItem, result);

					curRes -= cur.getValue() * areaTotal.get(start).getWeight();
					curItem.remove(curItem.size() - 1);
					visited.remove(cur.getId());
				}
			}
		}
	}
}
