package amazon;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class Block {
	private int start, end;

	public Block(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return this.start;
	}

	public int getEnd() {
		return this.end;
	}

}

public class Insertion {
	public static void insertion(List<Content> contents, List<Area> areas) {
		List<Set<String>> moments = getMoments(areas);
		for (Area area : areas) {
			List<Block> blocks = getBlocks(area);
			for (Block block : blocks) {
				int blockStart = block.getStart(), blockEnd = block.getEnd();
				boolean finished = false;
				for (int index = 0; index < contents.size() && finished == false; index++) {
					Content content = contents.get(index);
					for (int start = blockStart, end = start + content.getLength() - 1; end <= blockEnd; start++, end++) {
						if (insertable(start, end, content, moments)) {
							// Add this content to this area's final schedule.
							RealContent realContent = new RealContent(contents.get(index), start);
							area.getFinalSchedule().add(realContent);
							// Update moments info, add new content to moments.
							for (int moment = start; moment <= end; moment++) {
								moments.get(moment).add(contents.get(index).getId());
							}
							// Remove content from contents list.
							contents.remove(index);
							// Update blocks info.
							blocks.remove(block);
							if (blockStart < start) {
								blocks.add(new Block(blockStart, start - 1));
							}
							if (blockEnd > end) {
								blocks.add(new Block(end + 1, blockEnd));
							}
							finished = true;
							break;
						}
					}
				}
			}
		}
	}

	private static List<Block> getBlocks(Area area) {
		List<Block> blocks = new ArrayList<>();
		List<RealContent> realContents = area.getFinalSchedule();
		int lastEndTimeStamp = 0;
		for (RealContent realContent : realContents) {
			if (realContent.getTimeStamp() > lastEndTimeStamp + 1) {
				Block block = new Block(lastEndTimeStamp + 1, realContent.getTimeStamp() - 1);
				blocks.add(block);
			}
			lastEndTimeStamp = realContent.getTimeStamp() + realContent.getLength() - 1;
		}
		return blocks;
	}
	
	private static boolean insertable(int start, int end, Content content, List<Set<String>> moments) {
		for (int i = start; i <= end; i++) {
			if (moments.get(i).contains(content.getId())) {
				return false;
			}
		}
		return true; 
	}

	private static List<Set<String>> getMoments(List<Area> areas) {
		List<Set<String>> moments = new ArrayList<>();
		for (Area area : areas) {
			List<RealContent> realContents = area.getFinalSchedule();
			for (RealContent realContent : realContents) {
				int realContentEnd = realContent.getTimeStamp() + realContent.getLength() - 1;
				for (int timeStamp = realContent.getTimeStamp(); timeStamp <= realContentEnd; timeStamp++) {
					moments.get(timeStamp).add(realContent.getId());
				}
			}
		}
		return moments;
	}
}

/*
 * DFS code
 * // Assuming global List<HashSet<String id>> moments

HashSet<String> contentVisited = new HashSet<String>();

// curIdx = index of inputContents
public void dfs(int numInsertedContent, 
				ArrayList<Content> inputContents, 
				ArrayList<RealContent> interim,
				HashSet<String> contentVisited,
				boolean completed,
				ArrayList<RealContent> outputSchedule) {

	if (completed) {
		return;
	}

	if (numInsertedContent = inputContents.size()) {	
		// All input contents have been inserted
		outputSchedule.addAll(interim);
		completed = true;
		return;
	}

	// Traverse every content starting at curIdx
	for (int i = 0; i < inputContents.size(); i++) {
		// Find a empty block, get startMoment and length of it
		//

		Content curContent = inputContents.get(i);
		// Get a uninserted content
		if (contentVisited.Contains(curContent.getId())) {
			continue;
		}
		// Get info of current content
		String id = curContent.getId();
		int contentLen = curContent.getLength();

		if (contentLen > blockLen) {
			continue;
		}
		// Check whether content can be inserted in startMoment to startMoment+Len
		boolean insertable = true;
		for (int moment = startMoment; moment <= startMoment+contentLen; moment++) {
			// Content with same id overlap at the moment
			if (moments.get(moment).Contains(id)) {
				insertable = false;
			}
		}
		if (!insertable) {
			continue;
		}
			
		// content+timeStamp->realContent
		realContent rc = new realContent(curContent.getId(),
			   							 curContent.getLength(),
										 curContent.getValue(),
										 startMoment);
			
		// insert()
		//

		contentVisited.add(id);
		interim.add(rc);
		
		// modify block list
		//

		dfs(numInsertedContent+1, 
			inputContents, 
			interim, 
			contentVisited, 
			completed,
			outputSchedule);

		// de-insert()
		//
		contentVisited.remove(id);
		interim.remove(interim.size()-1);

		// re-modify block list

	}
}

*/
