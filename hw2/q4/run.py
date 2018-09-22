import subprocess
from timeit import default_timer as timer
import os
FNULL = open(os.devnull, 'w')
t = 0
while (t <= 8):
	start = timer()
	retcode = subprocess.call(["./runa", "mat_a_big.txt", "mat_b_big.txt", str(t)] , stdout=FNULL, stderr=subprocess.STDOUT)
	end = timer()
	print t
	print end - start
	t +=1
