package amazon;

import java.util.*;
/*
 * For the first part of this problem, we assume input data is a list of areas:
 * 				List<#Area> input
 * 
 * Our first step is convert this to our own class(data structure): Point.
 * 'Point' represent a time stamp when we have a 'Content' start or stop.
 * It plays a role that make us can easily sort all the 'Content' properly.
 * 
 */

class Point {
	private int timeStamp;
	private RealContent content;
	private boolean startEnd;

	public Point(int timeStamp, RealContent content, boolean startEnd) {
		this.timeStamp = timeStamp;
		this.content = content;
		this.startEnd = startEnd;
	}

	public double getTimeStamp() {
		return this.timeStamp;
	}

	public RealContent getContent() {
		return this.content;
	}

	public boolean isStart() {
		return this.startEnd;
	}
}

public class Schedule {
	// Step 1: Convert to 'Point' class.
	private static List<Point> convertInputToTimePoint(List<RealContent> contents) {
		List<Point> points = new ArrayList<>();
		for (RealContent content : contents) {
			points.add(new Point(content.getTimeStamp(), content, true));
			points.add(new Point(content.getTimeStamp() + content.getLength(), content, false));
		}
		sortPoints(points);

		return points;
	}

	// Step 2: Sort 'Points' array.
	private static void sortPoints(List<Point> points) {
		for (int i = 1; i < points.size(); i++) {
			for (int j = i; j > 0 && points.get(j).getTimeStamp() < points.get(j - 1).getTimeStamp(); j--) {
				Point tmp = points.get(j);
				points.set(j, points.get(j - 1));
				points.set(j - 1, tmp);
			}
		}
	}

	// Step 3: Using a HashSet which size is 3, and traversal whole 'Point'
	// list.
	private static List<RealContent> getValidSchedule(List<Point> points) {
		List<RealContent> validSchedule = new ArrayList<>();
		Set<String> space = new HashSet<>(3);
		for (Point point : points) {
			String contentId = point.getContent().getId();
			if (point.isStart() && space.size() != 3 && !space.contains(contentId)) {
				space.add(contentId);
				validSchedule.add(point.getContent());
			} else if (!point.isStart() && space.contains(contentId)) {
				space.remove(contentId);
			}
		}

		return validSchedule;
	}

	// Final function: Get in Area, update area's validSchedule.
	public static void schedule(List<Area> areas) {
		System.out.println("First step: Get Valid Schedule.");
		for (Area area : areas) {
			System.out.print("Area" + area.getId() + ": ");
			List<Point> points = convertInputToTimePoint(area.getInitialSchedule());
			List<RealContent> validSchedule = getValidSchedule(points);
			area.setValidSchedule(validSchedule);
			for (int i = 0; i < area.getValidSchedule().size(); i++) {
				System.out.print(area.getValidSchedule().get(i).getId() + " ");
			}
			System.out.println();
		}
	}
}
