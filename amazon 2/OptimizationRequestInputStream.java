package amazon;

import java.util.LinkedList;
import java.util.Queue;
import amazon.Content;

public class OptimizationRequestInputStream {
	Queue<Content> inputQueue;
	int size;
	
	public OptimizationRequestInputStream() {
		inputQueue = new LinkedList<Content>();
		this.size = 0;
	}
	
	public void push(Content content) {
		inputQueue.offer(content);
		size++;
	}
	
	public boolean hasNext() {
		return size > 0;
	}
	
	public Content next() {
		if (size == 0) {
			return null;
		}
		size--;
		return inputQueue.poll();
	}
}
