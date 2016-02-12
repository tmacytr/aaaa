//package amazon;

import java.util.LinkedList;
import java.util.Queue;
import amazon.ScheduleRequest;

public class ScheduleRequestInputStream {
	Queue<ScheduleRequest> inputQueue;
	int size;
	
	public ScheduleRequestInputStream() {
		inputQueue = new LinkedList<ScheduleRequest>();
		this.size = 0;
	}
	
	public void push(ScheduleRequest req) {
		inputQueue.offer(req);
		size++;
	}
	
	public boolean hasNext() {
		return size > 0;
	}
	
	public ScheduleRequest next() {
		if (size == 0) {
			return null;
		}
		size--;
		return inputQueue.poll();
	}
}