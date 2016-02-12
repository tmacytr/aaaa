package amazon_interview_q2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

public class q2 {
	
	public static float maxValue = 0;
	public static HashMap<Content,Integer> maxSelected;
	static void dfs(int level,List<List<Content>> areas,List<Float> rate,List<Integer> compulsory, HashMap<Content,Integer> selected,HashMap<String,Integer>occupied){
		if(level==areas.size()){
			float value=0;
			for(Entry<Content, Integer> entry : selected.entrySet()) {
				value += entry.getKey().getCtntValue()*rate.get(entry.getValue());
			}
			if(maxValue < value){
				maxValue = value;
				maxSelected = (HashMap<Content, Integer>) selected.clone();
			}
			return;
		}
		boolean existed = true;
		if(compulsory.contains(level))
			existed = false;
		for(Content item:areas.get(level)){
			if(!occupied.containsKey(item.getCtntID())){
				selected.put(item, level);
				occupied.put(item.getCtntID(), 1);
				dfs(level+1,areas,rate,compulsory,selected,occupied);
				selected.remove(item);
				occupied.remove(item.getCtntID());
				existed = true;
			}
		}
		if(existed==true&&!compulsory.contains(level)){
	        dfs(level+1,areas,rate,compulsory,selected,occupied);
		}
	}
	public static void main(String args[]) {
	    HashMap occupied = new HashMap<String,Integer>();
	    HashMap<Content,Integer> selected = new HashMap<Content,Integer>();
	    List<Float> areasRate = new ArrayList<Float>();
	    List<Integer> compulsory = new ArrayList<Integer>();
	    List<List<Content>> areas = new ArrayList<List<Content>>();
	    for (int i = 0; i < 8; i++) {
	    	List<Content> temp = new ArrayList<Content>();
	    	areas.add(temp);
	    }
	    int level = 0;
	    //test case
	    areas.get(0).add(new Content("id1",4,24,3));
	    areas.get(0).add(new Content("id2",3,15,6));
	    areas.get(0).add(new Content("id3",3,9,2));
	    areas.get(1).add(new Content("id1",4,16,4));
	    areas.get(2).add(new Content("id4",4,24,3));
	    areas.get(2).add(new Content("id5",10,15,6));
	    areas.get(3).add(new Content("id5",6,9,10));
	    areas.get(3).add(new Content("id6",7,16,6));
	    areas.get(4).add(new Content("id3",3,24,7));
	    areas.get(4).add(new Content("id7",4,15,2));
	    areas.get(5).add(new Content("id2",1,9,1));
	    areas.get(5).add(new Content("id8",5,16,0));
	    areas.get(6).add(new Content("id5",10,15,6));
	    areas.get(7).add(new Content("id5",6,9,10));
	    areas.get(7).add(new Content("id6",7,16,6));
	    areas.get(7).add(new Content("id3",3,24,7));
	    areasRate.add((float) 1.0);
	    areasRate.add((float) 0.8);
	    areasRate.add((float) 0.6);
	    areasRate.add((float) 0.5);
	    areasRate.add((float) 0.3);
	    areasRate.add((float) 0.2);
	    areasRate.add((float) 0.1);
	    areasRate.add((float) 0.05);
	    compulsory.add(6);
	    compulsory.add(7);
	    dfs(level,areas,areasRate,compulsory,selected,occupied);
	    
	    //test case
	    System.out.println(maxValue);
	    for(Entry<Content, Integer> entry : maxSelected.entrySet())
	        System.out.println(entry.getKey().getCtntID()+":"+entry.getValue());
	}
}
class Content{
	// contentID,contentLength,contentValue stands for content id,length and value
	private String contentID;
	private int contentLength;
	private int contentValue;
	private int startTime;
	public Content(String id,int length,int value, int time){
		this.contentID = id;
		this.contentLength = length;
		this.contentValue = value;
		this.startTime = time;
	}
	public String getCtntID(){
		return contentID;
	}
	public int getCtntLength(){
		return contentLength;
	}
	public int getCtntValue(){
		return contentValue;
	}
	public int getStartTime(){
		return startTime;
	}
}