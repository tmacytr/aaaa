package oj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;


public class oj{	
	public static int weight1 = 0;
	public static int weight2 = 0;
	public static int weight3 = 0;
	public static List<Integer> findTrack(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(weight1);
		list.add(weight2);
		list.add(weight3);
		Collections.sort(list);
		return list;
	}
	public static String checkOverlap(TreeSet<Content> ts,Content ctnt){
		if(ts.isEmpty()) 
			return "no";
		Content minL = (Content) ts.ceiling(ctnt);
		if(minL == null){
			Content maxS = (Content) ts.floor(ctnt);
			if(maxS.getStartTime() + maxS.getCtntLength()<= ctnt.getStartTime())
			    return "no";
			return maxS.getCtntID();
		}
		if(minL.getStartTime() < ctnt.getStartTime()+ctnt.getCtntLength())
		    return minL.getCtntID();
		return "no";
	}
    public static void main(String args[]) {
    	List<Content> list = new ArrayList<Content>();
        TreeSet<Content> ts1 = new TreeSet<Content>(new StartTimeComp());
        TreeSet<Content> ts2 = new TreeSet<Content>(new StartTimeComp());
        TreeSet<Content> ts3 = new TreeSet<Content>(new StartTimeComp());      
        //test case
//        list.add(new Content("id1",4,24,3));
//        list.add(new Content("id2",3,15,6));
//        list.add(new Content("id3",3,9,2));
//        list.add(new Content("id1",4,16,4));
//        list.add(new Content("id4",4,24,3));
//        list.add(new Content("id5",10,15,6));
//        list.add(new Content("id5",6,9,10));
//        list.add(new Content("id6",7,16,6));
//        list.add(new Content("id3",3,24,7));
//        list.add(new Content("id7",4,15,2));
//        list.add(new Content("id2",1,9,1));
//        list.add(new Content("id8",5,16,0));
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
        
        Collections.sort(list,new RateComp());
        //test case
        for(Content c:list){
        	System.out.print(c.getCtntID());
        }
        
        for(Content e:list){
        	String overlapID1 = checkOverlap(ts1,e);
        	String overlapID2 = checkOverlap(ts2,e);
        	String overlapID3 = checkOverlap(ts3,e);
        	if(e.getCtntID().equals(overlapID1)||e.getCtntID().equals(overlapID2)||e.getCtntID().equals(overlapID3))
        		continue;
            List<Integer> tracklist = findTrack();
            for(int i:tracklist){
            	if(weight1 == i){
            		if(overlapID1.equals("no")){
            			ts1.add(e);
            			weight1 += e.getCtntLength();
            			break;
            		}
            	}else if(weight2 == i){
            		if(overlapID2.equals("no")){
            			ts2.add(e);
            			weight2 += e.getCtntLength();
            			break;
            		}
            	}else{
            		if(overlapID3.equals("no")){
            			ts3.add(e);
            			weight3 += e.getCtntLength();
            			break;
            		}
            	}
            }
        }
        //test case
        System.out.println();
        for ( Content ctnt :ts1) {
        	System.out.print(ctnt.getCtntID()+ctnt.getStartTime());
        }    
        System.out.println();
        for ( Content ctnt :ts2) {
        	System.out.print(ctnt.getCtntID()+ctnt.getStartTime());
        }
        System.out.println();
        for ( Content ctnt :ts3) {
        	System.out.print(ctnt.getCtntID()+ctnt.getStartTime());
        }
        return ;
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
class StartTimeComp implements Comparator<Content>{
	@Override
	public int compare(Content arg0, Content arg1) {
		// TODO Auto-generated method stub
			if(arg0.getStartTime() > arg1.getStartTime())
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
