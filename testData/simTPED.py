# -*- coding: utf-8 -*-
"""
Created on Fri Dec 18 17:42:16 2015

@author: felixfan
"""

import random

num_ind = 100
num_snp = 200
chrom = 1

snps = []
for i in range(num_snp):
    a1 = random.choice(['A', 'C', 'G', 'T'])
    a2 = random.choice(['A', 'C', 'G', 'T'])
    while a1 == a2 :
        a2 = random.choice(['A', 'C', 'G', 'T'])
    print chrom, "snp"+str(i+1), 0, i+1,
    for j in range(num_ind):
        print random.choice([a1, a2]), random.choice([a1, a2]),
    print
