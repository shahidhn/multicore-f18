f = open("mat_b_big.txt", "w")
f.write("100 100\n")
i = 0
j = 0
while ( i < 100):
	j = 0
	while (j < 100):
		f.write(str(i+j + 1) + " " )
		j += 1
	f.write("\n")
	i += 1
