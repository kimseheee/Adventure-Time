package adventure;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class GameScore {
	HashMap<String, Integer> gameScore;	
	Set<String> keys;
	Iterator<String> iterator;
	ValueComparator valueComparator;
    TreeMap<String,Integer> gameRank;
    TreeMap<String, Integer> ranking;
	
	GameScore() {
		gameScore = new HashMap<String, Integer>();
		ranking = new TreeMap<String, Integer>();
	}
	
	public HashMap<String, Integer> getScore() {
		return gameScore;
	}
	
	public void setScore(String name, int score) {
		// �̸��� �Է¹޾Ƽ� ������ �Բ� ����
		gameScore.put(name, score);
	}
	
	public TreeMap<String, Integer > rankScore(HashMap<String, Integer> gameScore) {
		valueComparator =  new ValueComparator(gameScore);
		gameRank = new TreeMap<String,Integer>(valueComparator);
		gameRank.putAll(gameScore);
        int i=1;
        for (Map.Entry<String,Integer> entry : gameRank.entrySet() ) {
            //������ ����Ʈ���� ������ �迭��ȣ�� �����Ͽ� ���� ����Ʈ���� ����
        	ranking.put(entry.getKey(), gameScore.get(entry.getKey()));
            System.out.println(i+"��  " + ranking);
            i++;
        }
        return ranking;
	}

	class ValueComparator implements Comparator<String> {
		 
	    Map<String, Integer> base;
	     
	    public ValueComparator(Map<String, Integer> base) {
	        this.base = base;
	    }
	 
	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) { //�ݴ�� �ϸ� �������� <=
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}

	public void print() {
		keys = gameScore.keySet();
		iterator = keys.iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			int value = gameScore.get(key);
			System.out.println("(" + key + "," + value + ")");
		}
	}
	
	public void printRank() {
		keys = gameRank.keySet();
		iterator = keys.iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			int value = gameRank.get(key);
			System.out.println("(" + key + "," + value + ")");
		}
	}
}
