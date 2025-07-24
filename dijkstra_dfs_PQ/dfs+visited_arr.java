/*✅ Problem 1: Emergency Routing in SmartCity
The city of Byteopolis has installed smart sensors across N intersections, numbered 0 to N-1. The roads between intersections are directed and have associated travel times.

The SmartCity control center is handling a batch of emergencies, each needing to dispatch a vehicle from a source to a destination as fast as possible.

You're given:

A list roads, where each element is [from, to, time].

A list emergencies, where each emergency is [source, destination, urgency].

An array blockedTimes where blockedTimes[i] contains times when intersection i is blocked.

An integer K — number of most urgent emergencies to process.

Rules:

If an intersection is blocked at time t, the vehicle must wait and can only pass after t+1.

You may wait at any node to avoid blocked times.

You must compute the minimum arrival time from source to destination.

If the destination is unreachable, return -1.

Input:
java
Copy
Edit
int N
List<List<Integer>> roads      // each: [from, to, time]
List<List<Integer>> emergencies // each: [source, destination, urgency]
int[][] blockedTimes           // blockedTimes[i] = list of times when node i is blocked
int K
Output:
Return a list of integers of length K – each being the minimum time to complete the emergency.

Example:
java
Copy
Edit
N = 4
roads = [[0,1,2], [1,2,2], [0,2,4], [2,3,3]]
emergencies = [[0,3,5], [0,2,9], [1,3,2]]
blockedTimes = [
    [],        // 0
    [2],       // 1 is blocked at time 2
    [4,5],     // 2 is blocked at time 4 and 5
    []         // 3
]
K = 2
Output:
[6, 9]
*/
package dsaRoundPrep;

import java.util.*;

class day1 {

    public static void main(String[] args) {
//        int N = 4;
//
//        List<List<Integer>> roads = Arrays.asList(
//            Arrays.asList(0, 1, 2),
//            Arrays.asList(1, 2, 2),
//            Arrays.asList(0, 2, 4),
//            Arrays.asList(2, 3, 3)
//        );
//
//        List<List<Integer>> emergencies = Arrays.asList(
//            Arrays.asList(0, 3, 5),
//            Arrays.asList(0, 2, 9),
//            Arrays.asList(1, 3, 2)
//        );
//
//        int[][] blockedTimes = {
//            {},          // 0
//            {2},         // 1 is blocked at time 2
//            {4, 5},      // 2
//            {}           // 3
//        };
//
//        int K = 2;
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
        Collections.sort(emergencies, (a,b)->{
        	return b.get(2)-a.get(2);
        });
        
        
        
        List<List<Integer>> blocked = new ArrayList<>();
        for(int i=0;i<blockedTimes.length;i++) {
        	blocked.add(new ArrayList<>());
        	for(int j=0;j<blockedTimes[i].length;j++) {
        		blocked.get(i).add(blockedTimes[i][j]);
        	}
        }
        
        
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for(List<Integer> r: roads) {
        	if(!graph.containsKey(r.get(0))) graph.put(r.get(0), new ArrayList<>());
        	graph.get(r.get(0)).add(new int[] {r.get(1), r.get(2)});
        	
        	if(!graph.containsKey(r.get(1))) graph.put(r.get(1), new ArrayList<>());
        	graph.get(r.get(1)).add(new int[] {r.get(0), r.get(2)});
        }        
        
        List<Integer> res = new ArrayList<>();
        int i=0;
        while(K>0) {
        	int source = emergencies.get(i).get(0);
        	int dest = emergencies.get(i).get(1);
        	int minTime[] = new int[1];
        	minTime[0] = Integer.MAX_VALUE;
        	dfs(graph, blocked, source, dest, 0, minTime, new boolean[N]);
        	if(minTime[0]==Integer.MAX_VALUE) res.add(-1);
        	else res.add(minTime[0]);
        	i++;
        	K--;
        }
        
        
        return res;
    }
    
    public static void dfs(Map<Integer, List<int[]>> graph, List<List<Integer>> blocked, int src, int dest, int time, int minTime[], boolean vis[]) {
    	if(src==dest) {
    		minTime[0] = Math.min(minTime[0],  time);
    		return;
    	}
    	vis[src]=true;
    	
    	if(graph.containsKey(src)) {
    		for(int x[]:graph.get(src)) {
    			if(!vis[x[0]]) {
    				int t = time+x[1];
        			while(blocked.get(x[0]).contains(t)) {
        				t+=1;
        			}
        			dfs(graph, blocked, x[0], dest, t, minTime, vis);
    			}
    		}
    	}    
    	vis[src]=false;
    }
}
// but this is not optimal for large test cases as dfs explores all paths
