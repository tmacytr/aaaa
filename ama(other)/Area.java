package amazon;

import java.util.ArrayList;
import java.util.List;

class Content {
	private String id; // Unique id for each content.
	private int length; // Length of content.
	private int value; // Value of content, [0, 100].

	public Content(String id, int length, int value) {
		this.id = id;
		this.length = length;
		this.value = value;
	}
	
	public Conteng(Content content) {
		this.id = content.getId();
		this.length = content.getLength();
		this.value = content.getValue();
	}

	public String getId() {
		return this.id;
	}

	public int getLength() {
		return this.length;
	}

	public int getValue() {
		return this.value;
	}
}

class RealContent extends Content{
	private int timeStamp; // Start time of this content.

	public RealContent(String id, int length, int value, int timeStamp) {
		super(id, length, value);
		this.timeStamp = timeStamp;
	}
	
	public RealContent(Content content, int timeStamp) {
		super(content);
		this.timeStamp = timeStamp;
	}

	public int getTimeStamp() {
		return this.timeStamp;
	}
}

public class Area {
	private String id; // Unique id for each area.
	private double weight; // Weight of this area.
	// Initial schedule for this area. First step's input.
	private List<RealContent> initialSchedule = new ArrayList<>();
	// Schedule which is legal after the rule. First step's output and second's
	// input.
	private List<RealContent> validSchedule = new ArrayList<>();
	// Final schedule for this area. Second step for the second step, and
	// third's input.
	private List<RealContent> finalSchedule = new ArrayList<>();

	public Area(String id, double weight, List<RealContent> initialSchedule) {
		this.id = id;
		this.weight = weight;
		this.initialSchedule = initialSchedule;
	}

	public String getId() {
		return this.id;
	}

	public double getWeight() {
		return this.weight;
	}

	public List<RealContent> getInitialSchedule() {
		return this.initialSchedule;
	}

	public List<RealContent> getValidSchedule() {
		return this.validSchedule;
	}

	public List<RealContent> getFinalSchedule() {
		return this.finalSchedule;
	}

	public void setInitialSchedule(List<RealContent> initialSchedule) {
		this.initialSchedule = initialSchedule;
	}

	public void setValidSchedule(List<RealContent> validSchedule) {
		this.validSchedule = validSchedule;
	}

	public void setFinalSchedule(List<RealContent> finalSchedule) {
		this.finalSchedule = finalSchedule;
	}
}
