# -*- coding: utf-8 -*-


import sys
import estimate_ld

dat = sys.argv[1]

geno = []
f = open(dat)
for r in f:
	r = r.strip()
	arr = r.split()
	arr = map(int, arr)
	geno.append(arr)
f.close()

for i in range(len(geno)-1):
	for j in range(len(geno)):
			t = estimate_ld.esem_r(geno[i], geno[j]) * estimate_ld.esem_r(geno[i], geno[j])
			print "%.3f" % t,
	print
