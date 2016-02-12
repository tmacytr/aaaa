package amazon_interview_q1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.Map.Entry;

public class q1 {
	public static void deleteContent(HashMap<Integer,List<Content>> dict,Content ctnt){
		for(int i=ctnt.getStartTime();i<ctnt.getStartTime()+ctnt.getCtntLength();i++){
			List<Content> temp = dict.get(i);
		    if(temp.contains(ctnt)){
		    	temp.remove(ctnt);
		    }
		    dict.put(i, temp);
		}
	}
	
	public static void checkAvailible(HashMap<Integer,List<Content>> dict,int cur){
		HashMap<String,List<Content>> sameID = new HashMap<String, List<Content>>();
		List<Content> curContents = new ArrayList<Content>(dict.get(cur));
		List<Content> selectedContents = new ArrayList<Content>();
		List<Content> remainingContents = new ArrayList<Content>();
		for(Content item :curContents){
			if(sameID.containsKey(item.getCtntID())){
				List<Content> temp = sameID.get(item.getCtntID());
	    		temp.add(item);
				sameID.put(item.getCtntID(), temp);
			}else{
				List<Content> temp = new ArrayList<Content>();
				temp.add(item);
				sameID.put(item.getCtntID(), temp);
			}
		}
		for(List<Content> item:sameID.values()){
			Collections.sort(item,new RateComp());
			selectedContents.add(item.get(0));
		}
		Collections.sort(selectedContents,new RateComp());
		if(selectedContents.size()<=3) remainingContents = selectedContents;
		else{
			for(int i=0;i<3;i++){
				remainingContents.add(selectedContents.get(i));
			}
		}
		for(Content item :curContents){
			if(!remainingContents.contains(item)){
				deleteContent(dict,item);
			}
		}
	}
	
	public static void main(String args[]) {
    	List<Content> list = new ArrayList<Content>();    
        //test case
        list.add(new Content("id1",4,24,3));
        list.add(new Content("id2",3,15,6));
        list.add(new Content("id3",3,9,2));
        list.add(new Content("id1",4,16,4));
        list.add(new Content("id4",4,24,3));
        list.add(new Content("id5",10,15,6));
        list.add(new Content("id5",6,9,10));
        list.add(new Content("id6",7,16,6));
        list.add(new Content("id3",3,24,7));
        list.add(new Content("id7",4,15,2));
        list.add(new Content("id2",1,9,1));
        list.add(new Content("id8",5,16,0));
        
        int timeLineStart = Integer.MAX_VALUE;
        int timeLineEnd = Integer.MIN_VALUE;
        for(Content i:list){
        	timeLineStart = i.getStartTime()<timeLineStart? i.getStartTime():timeLineStart;
        	timeLineEnd = i.getStartTime()+i.getCtntLength()> timeLineEnd?i.getStartTime()+i.getCtntLength(): timeLineEnd;
        }
        HashMap<Integer,List<Content>> dict = new HashMap<Integer, List<Content>>();
        for(int j=timeLineStart;j<timeLineEnd;j++){
        	List<Content> temp = new ArrayList<Content>();
        	dict.put(j, temp);
        }
        for(Content i:list){
        	for(int k=i.getStartTime();k<i.getStartTime()+i.getCtntLength();k++){
        		List<Content> temp = dict.get(k);
        		temp.add(i);
        		dict.put(k, temp);
        	}
        }
        for(int j=timeLineStart;j<timeLineEnd;j++ ){
        	checkAvailible(dict,j);
        }
        for(Entry<Integer, List<Content>> entry : dict.entrySet()) {
        	System.out.print(entry.getKey()+":");
        	for(Content item:entry.getValue()){
        		System.out.print(item.getCtntID());
        	}
        	System.out.println();
		}
	}
}
class RateComp implements Comparator<Content>{
	@Override
	public int compare(Content arg0, Content arg1) {
		// TODO Auto-generated method stub
			float rate1 = (float)arg0.getCtntValue()/arg0.getCtntLength();
			float rate2 = (float)arg1.getCtntValue()/arg1.getCtntLength();
			if(rate1 < rate2)
				return 1;
			else
				return -1;
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

