package amazon;

import java.util.ArrayList;

public class SelectionRequest {
	int time;
	ArrayList<String> locations;
	public SelectionRequest(int time, ArrayList<String> locations) {
		this.time = time;
		this.locations = locations;
	}
}
