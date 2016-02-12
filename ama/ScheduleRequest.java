//package amazon;

public class ScheduleRequest {
	int id;
	String location;
	int startTime;
	int endTime;
	ScheduleRequest(int id, String location, int startTime, int endTime) {
		this.id = id;
		this.location = location;
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
