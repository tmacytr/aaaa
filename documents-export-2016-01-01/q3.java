
package amazon_interview_q3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * class description
 * @author wangbo
 * @time 9:47:28 AM
 */
public class q3 {
	/**
	 * method description
	 * @author wangbo
	 * @time 9:59:18 AM
	 * @param dict
	 * @param startTime
	 * @param endTime
	 * @param insertCtnt
	 * @return int
	 */
	public static int getFirstFitInterval(HashMap<Integer, List<Content>> dict,Integer startTime, Integer endTime, Content insertCtnt){
		int indexStart = startTime;
		int length=0;
		for(int j=startTime;j<endTime;j++){
			if(length==insertCtnt.getCtntLength())
				return indexStart;
			List<Content> temp= new ArrayList<Content>(dict.get(j));
			length++;
			if(temp.size()>=3){
				indexStart = j+1;
				length = 0;
			}else{
			    for(Content item:temp){	
			    	if(item.getCtntID()==insertCtnt.getCtntID()){
			    		indexStart = j+1;
						length = 0;
			    		break;
			    	}
			    }
			}
		}
		return indexStart;
	}
	
	public static void main(String args[]) {
        List<Content> list = new ArrayList<Content>();    
        
    	//test case
        list.add(new Content("id1",4,24,3));
        list.add(new Content("id4",4,24,3));
        list.add(new Content("id5",6,9,10));
        list.add(new Content("id3",3,24,7));
        list.add(new Content("id7",4,15,2));
        list.add(new Content("id2",1,9,1));
        Content insertCtnt = new Content("id4",20,9,-1);
        
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
        System.out.println(getFirstFitInterval(dict,timeLineStart,timeLineEnd,insertCtnt));
        for(Entry<Integer, List<Content>> entry : dict.entrySet()) {
        	System.out.print(entry.getKey()+":");
        	for(Content item:entry.getValue()){
        		System.out.print(item.getCtntID());
        	}
        	System.out.println();
		}
	}
}
/**
 * class description
 * @author wangbo
 * @time 9:47:47 AM
 */
class Content{

	private String contentID;        // description
	private int contentLength;
	private int contentValue;
	private int startTime;
	/**
	 * construtor description
	 * @param Content params description
	 * @param id
	 * @param length
	 * @param value
	 * @param time
	 */
	public Content(String id,int length,int value, int time){
		this.contentID = id;
		this.contentLength = length;
		this.contentValue = value;
		this.startTime = time;
	}
	/**
	 * @return
	 */
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
