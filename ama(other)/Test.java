package amazon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
	public static int max = 0;

	public static void main(String args[]) {
		// Assuming class Area, RealContent are defined
		// Input is List<Area> Areas;

		// RealContent(id, length, value, timeStamp)
		// Area1
		RealContent rc1 = new RealContent("1", 2, 10, 1);
		RealContent rc2 = new RealContent("2", 2, 10, 1);
		RealContent rc3 = new RealContent("3", 5, 10, 1);
		RealContent rc4 = new RealContent("4", 3, 10, 2);
		RealContent rc5 = new RealContent("7", 3, 10, 4);
		RealContent rc6 = new RealContent("1", 4, 10, 5);
		RealContent rc7 = new RealContent("5", 4, 10, 6);
		RealContent rc8 = new RealContent("1", 3, 10, 8);
		Area Area1 = new Area("1", 1.0, Arrays.asList(rc1, rc2, rc3, rc4, rc5, rc6, rc7, rc8));

		// Area2
		RealContent rc9 = new RealContent("3", 2, 10, 1);
		RealContent rc10 = new RealContent("7", 3, 10, 6);
		RealContent rc11 = new RealContent("1", 3, 10, 6);
		Area Area2 = new Area("2", 0.8, Arrays.asList(rc1, rc2, rc9, rc10, rc11));

		// Area3
		Area Area3 = new Area("3", 0.75, Arrays.asList(rc1, rc2, rc9, rc7));

		// Area4
		RealContent rc12 = new RealContent("8", 2, 100, 1);
		RealContent rc13 = new RealContent("9", 2, 80, 1);
		Area Area4 = new Area("4", 0.5, Arrays.asList(rc12, rc13));

		// Area5
		Area Area5 = new Area("5", 0.3, Arrays.asList(rc12));

		// Area6
		RealContent rc14 = new RealContent("10", 2, 10, 1);
		Area Area6 = new Area("6", 0.2, Arrays.asList(rc14));

		// Area1-6
		List<Area> areas = Arrays.asList(Area1, Area2, Area3, Area4, Area5, Area6);

		// Test for first step, Schedule.
		Schedule.schedule(areas);

		// Test for second step, Selection.
		int timeStamp = 3;
		Selection.selectSchedule(timeStamp, areas);
		
		// Test for third step, Insertion.
		List<Content> contents = new ArrayList<>();
		Insertion.insertion(contents, areas);
	}
}