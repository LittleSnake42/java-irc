package launcher;

import java.util.Comparator;
import java.util.Map.Entry;

public class WordCompare implements Comparator<Entry<String, Integer>>{

	public WordCompare() {
		// TODO Auto-generated constructor stub
	}
	
	public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
		if (o1.getValue() > o2.getValue() ) {
			return 1;
		} else {
			return -1;
		}
	}

}
