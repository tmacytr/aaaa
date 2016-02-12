package amazon;

import java.util.LinkedList;
import java.util.Queue;

public class SelectionRequestInputStream {
	Queue<SelectionRequest> inputQueue;
	int size;
	
	public SelectionRequestInputStream() {
		inputQueue = new LinkedList<SelectionRequest>();
		this.size = 0;
	}
	public void push(SelectionRequest req) {
		inputQueue.offer(req);
		size++;
	}
	
	public boolean hasNext() {
		return size > 0;
	}
	
	public SelectionRequest next() {
		if (size == 0) {
			return null;
		}
		size--;
		return inputQueue.poll();
	}
}
