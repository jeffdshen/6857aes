import pyAES
import AES
import random
import numpy as np
from copy import copy
from scipy.stats import ks_2samp

def f(M, K, r, p, q):
    aes = AES.AES()
    
    Mcopy = copy(M)
    seen = set()
    for i in xrange(256):
        Mcopy[p] = i
        C = aes.encrypt(Mcopy, K, aes.keySize["SIZE_128"], r)
        seen.add(C[q])
    return len(seen)

def randomByteArray(length):
    array = []
    for i in xrange(length):
        array.append(random.randint(0, 255))
    return array

def expr():
    random.seed(6857)
    
    samples = 10
    dist = np.zeros((11, samples))
    for i in xrange(samples):
        print i
        M = randomByteArray(16)
        K = randomByteArray(16)
        for j in xrange(1, 11):
            dist[j, i] += f(M, K, j, 3, 10)
    for j in xrange(1, 10):
        print(dist[j])
        D, p = ks_2samp(dist[j], dist[10])
        print 'r = ', j, ', p = ', p