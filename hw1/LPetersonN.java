import java.util.Arrays;

class LPetersonN implements Lock {
    int N;
    int L;
    int[] gate; 
    int[] last; 

    public LPetersonN(int numProc, int l_exclusion) {
        N = numProc;
        L = l_exclusion;
        gate = new int[N-L+1];//We only use gate[1]..gate[N-1]; gate[0] is unused
        Arrays.fill(gate, 0);
        last = new int[N-L+1];
        Arrays.fill(last, 0);
    }

    public void requestCS(int i) {
	      for (int k = 1; k < N-L+1; k++) { 
           gate[i] = k; 
           last[k] = i;
           for (int j = 0; j < N-L+1; j++) {
               while ((j != i) &&  // there is some other process
	              (gate[j] >= k) &&  // that is ahead or at the same level
                      (last[k] == i)) // and I am the last to update last[k]
               {};// busy wait
           }
        }
    }

    public void releaseCS(int i) {
        gate[i] = 0;
    }
}