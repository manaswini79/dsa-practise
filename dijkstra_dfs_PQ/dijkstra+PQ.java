package dsaRoundPrep;

import java.util.*;

class day1 {

    public static void main(String[] args) {
    	int N = 5;
    	List<List<Integer>> roads = Arrays.asList(
    	    Arrays.asList(0, 1, 2),
    	    Arrays.asList(1, 2, 2),
    	    Arrays.asList(2, 3, 2),
    	    Arrays.asList(3, 4, 2),
    	    Arrays.asList(0, 4, 10)
    	);
    	List<List<Integer>> emergencies = Arrays.asList(
    	    Arrays.asList(0, 4, 8),  // Priority 8
    	    Arrays.asList(1, 3, 6)   // Priority 6
    	);
    	int[][] blockedTimes = {
    	    {}, {}, {4}, {}, {}
    	};
    	int K = 2;

        List<Integer> result = scheduleEmergencies(N, roads, emergencies, blockedTimes, K);
        System.out.println(result);  // Example Output: [9, 4]
    }

    public static List<Integer> scheduleEmergencies(
        int N,
        List<List<Integer>> roads,
        List<List<Integer>> emergencies,
        int[][] blockedTimes,
        int K
    ) {
    	Map<Integer, List<int[]>> graph = new HashMap<>();
    	for(List<Integer> r:roads) {
    		if(!graph.containsKey(r.get(0))) graph.put(r.get(0), new ArrayList<>());
    		graph.get(r.get(0)).add(new int[] {r.get(1), r.get(2)});
    		
    		if(!graph.containsKey(r.get(1))) graph.put(r.get(1), new ArrayList<>());
    		graph.get(r.get(1)).add(new int[] {r.get(0), r.get(2)});
    	}
    	
    	emergencies.sort((a,b)->b.get(2)-b.get(2));
    	
    	List<List<Integer>> blocked = new ArrayList<>();
    	for(int i=0;i<blockedTimes.length;i++) {
    		blocked.add(new ArrayList<>());
    		for(int j=0;j<blockedTimes[i].length;j++) {
    			blocked.get(i).add(blockedTimes[i][j]);
    		}
    	}
    	
    	List<Integer> res = new ArrayList<>();
    	for(int i=0;i<N && i<K;i++) {
    		int src = emergencies.get(i).get(0);
    		int dest = emergencies.get(i).get(1);
    		res.add(find(graph, blocked, src, dest, N));
    	}
    	return res;
    }
    public static int find(Map<Integer, List<int[]>> graph, List<List<Integer>> blocked, int src, int dest, int N) {
    	PriorityQueue<int[]> pq = new PriorityQueue<>((a,b)->a[1]-b[1]);
    	boolean vis[][] = new boolean[N][1000];
    	pq.add(new int[] {src, 0});
    	
    	while(!pq.isEmpty()) {
    		int x[] = pq.poll();
    		int cur = x[0];
    		int time = x[1];
    		
    		if(vis[cur][time])continue;
        	vis[cur][time]=true;

    		
    		if(cur==dest)return time;
    		
    		if(graph.containsKey(cur)) {
    			for(int y[]:graph.get(cur)) {
    				int t=time+y[1];
    				while(blocked.get(y[0]).contains(t)) t++;
    				if(!vis[y[0]][t]) pq.add(new int[] {y[0], t});
    			}
    		}
    	}
    	return -1;
    }
}
