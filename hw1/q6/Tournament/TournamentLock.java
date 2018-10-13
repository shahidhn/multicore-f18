/* Shahid's implementation */

//package q6.Tournament;
//
//import java.util.Arrays;
//
//public class TournamentLock implements Lock {
//	int n, levels;
//	volatile PLock[] locks;
//	volatile int[][] pIDs;
//
//	public static double log2(int n){
//		return Math.log(n)/Math.log(2);
//	}
//	
//	public TournamentLock(int numThreads){
//		this.n = numThreads;
//		this.levels = (int) Math.ceil(log2(n)); 
//		this.locks = new PLock[n];
//		this.pIDs = new int[numThreads][levels];
//		Arrays.fill(locks, new PLock());
//		for (int i = 0; i < numThreads; i ++){
//			for (int j = 0; j < levels; j ++){
//				pIDs[i][j] = -1;
//			}
//		}
//	}
//	
//	public void lock(int i) {
//		int nodeID = i + n;
//		
//		for (int j = 0; j < levels; j ++){
//			pIDs[i][j] = nodeID % 2;
//			nodeID = nodeID / 2;
//			System.out.println("Process " + i + " requesting lock " + nodeID + " with pID " + pIDs[i][j]);
//			locks[nodeID].requestCS(pIDs[i][j]);;
//		}
//	}
//	
//	public void unlock(int i) {
//		int nodeID = 1;
//		for (int j = levels-1; j >= 0; j --){
//			System.out.println("Process " + i + " releasing lock " + nodeID + " with pID " + pIDs[i][j]);
//			locks[nodeID].releaseCS(pIDs[i][j]);
//			nodeID = 2 * nodeID + pIDs[i][j];
//		}
//	}
//	
//	class PLock {
//		volatile boolean[] wantCS = new boolean[2];
//		volatile int turn;
//		
//		public PLock(){
//			Arrays.fill(this.wantCS, false);
//			turn = 1;
//		}
//		
//		public void requestCS(int i){
//			int j = 1 - i;
//			wantCS[i] = true;
//			turn = j;
//			while (wantCS[j] && turn == j);
//		}
//		
//		public void releaseCS(int i){
//			wantCS[i] = false;
//		}
//		
//	}
//	
//}

/* Jacob's implementation */

//package q6.Tournament;
//
//import java.util.Arrays;
//
//public class TournamentLock implements Lock {
//	int N;
//	static volatile int[] gate; 
//	static volatile int[] last;
//	public TournamentLock(int numThreads){
//		N = numThreads;
//		gate = new int[N];//We only use gate[1]..gate[N-1]; gate[0] is unused
//		for (int i = 0; i < N; i++)
//			gate[i] = -1;
//		last = new int[N];
//		Arrays.fill(last, 0);
//	}
//	public void lock(int i) {
//		int k = i/2;
//		gate[i] = k; 
//		last[k] = i;
//		for (int j = 0; j < N; j++) {
//			while ((j != i) &&  // there is some other process
//					(gate[j] == k) &&  // that is ahead or at the same level
//					(last[k] == i)) // and I am the last to update last[k]
//			{};// busy wait
//		}
//
//		k= 4+i/4;
//		gate[i] = k; 
//		last[k] = i;
//		for (int j = 0; j < N; j++) {
//			while ((j != i) &&  // there is some other process
//					(gate[j] == k) &&  // that is ahead or at the same level
//					(last[k] == i)) // and I am the last to update last[k]
//			{};// busy wait
//		}
//
//		k=6;
//		gate[i] = k; 
//		last[k] = i;
//		for (int j = 0; j < N; j++) {
//			while ((j != i) &&  // there is some other process
//					(gate[j] == k) &&  // that is ahead or at the same level
//					(last[k] == i)) // and I am the last to update last[k]
//			{};// busy wait
//		}
//	}
//	
//	public void unlock(int i) {
//		gate[i] = -1;
//	}
//	
//}

/* Taken from https://github.com/shriroopjoshi/mutual-exclusion/blob/master/tournament.cpp */

package q6.Tournament;

import java.util.Arrays;
import java.util.concurrent.atomic.*;

public class TournamentLock implements Lock {
	int q, n, p, z, levels;
	
	volatile AtomicInteger[] gate;
	volatile AtomicBoolean[] flag;
	volatile AtomicInteger[] last;
	
	public static double log2(int n){
		return Math.log(n)/Math.log(2);
	}
	
	public TournamentLock(int numThreads){
		this.q = (int) Math.pow(2, (int) Math.ceil(log2(numThreads)));
		this.n = q + q - 1;
		this.p = q - 1;
		this.z = numThreads;
		this.levels = (int) Math.ceil((log2(z)));
		
		this.gate = new AtomicInteger[numThreads * z];
		this.flag = new AtomicBoolean[n];
		this.last = new AtomicInteger[p];
		
//		Arrays.fill(this.gate, -1);
//		Arrays.fill(this.flag, false);
		Arrays.fill(this.gate, new AtomicInteger(-1));
		Arrays.fill(this.flag, new AtomicBoolean(false));
		Arrays.fill(this.last, new AtomicInteger());
	}
	public void lock(int i) {
		i ++;
		int ipid = p + i - 1;
		
		for (int j = 0; j < levels; j ++){
//			flag[ipid] = true;
//			last[(ipid-1)/2] = i;
//			
//			if (ipid % 2 == 0){
//				while (flag[ipid-1] && last[(ipid-1)/2] == i);
//			} else {
//				while (flag[ipid+1] && last[(ipid-1)/2] == i);
//			}
//			
//			gate[(i-1)*z+j] = ipid;
//			ipid = (ipid-1)/2;
			
			flag[ipid].set(true);
			last[(ipid-1)/2].set(i);
			
			if (ipid % 2 == 0){
				while (flag[ipid-1].get() && last[(ipid-1)/2].get() == i);
			} else {
				while (flag[ipid+1].get() && last[(ipid-1)/2].get() == i);
			}
			
			gate[(i-1)*z+j].set(ipid);
			ipid = (ipid-1)/2;
		}
	}
	
	public void unlock(int i) {
		for (int j = 0; j < levels; j ++){
			flag[gate[i*z+j].get()].set(false);
		}
	}
	
}
