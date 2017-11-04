################################################################################
# Algorithm for solving the Knapsack Problem using a Genetic Algorithm
#
import os
import re
import random
import time


ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_DIR = os.path.join(ROOT_DIR, "data/")


class Knapsack:

    def __init__(self, objs=list(), weight=0, value=0, max_weight=0):
        self.__objects = objs
        self.__weight = weight
        self.__value = value
        self.__max_weight = max_weight

    def objects(self, objs=None):
        if objs is None:
            return self.__objects
        else:
            self.__objects = objs

    def weight(self, w=None):
        if w is None:
            return self.__weight
        else:
            self.__weight = w

    def max_weight(self,mw=None):
        if mw is None:
            return self.__max_weight
        else:
            self.__max_weight = mw

    def value(self, v=None):
        if v is None:
            return self.__value
        else:
            self.__value = v

    def cross(self, other_objs):
        # Determine which list is smaller and pick random position based on
        # that list
        if len(other_objs) < len(self.objects()):
            cross = random.randint(0, int(3 * len(other_objs) / 2))
        else:
            cross = random.randint(0, int(3 * len(self.objects()) / 2))

        # Cross the two knapsacks
        self.objects(self.objects()[:cross] + other_objs[cross:])
        # Correct self weights and values
        val = 0
        wt = 0
        pos = 0
        too_full = False
        for o in self.objects():
            pos += 1
            if wt + o[0] > self.max_weight():
                too_full = True
                break
            wt += o[0]
            val += o[1]
        if too_full:
            self.objects(self.objects()[:pos])
        self.weight(wt)
        self.value(val)

    def mutate(self, objs):
        pos = random.randint(0,len(self.objects())-1)
        old_obj = self.objects()[pos]
        obj = random.choice(objs)
        while (self.weight() - old_obj[0] + obj[0]) > self.max_weight():
            pos = random.randint(0,len(self.objects())-1)
            old_obj = self.objects()[pos]
            obj = random.choice(objs)
        self.objects()[pos] = obj
        self.weight(self.weight() - old_obj[0] + obj[0])
        self.value(self.value() - old_obj[1] + obj[1])


def parse(filename):
    if os.path.exists(filename):
        with open(filename, "r") as file:
            o = list()
            mw = 0
            pair_pattern = re.compile("\([0-9]?[0-9] \. [0-9]?[0-9]\)")
            weight_pattern = re.compile("\(setq MaxWeight [0-9][0-9][0-9]\)")
            for line in file:
                sline = line.strip()
                match = pair_pattern.match(sline)
                if match is not None:
                    str_val = match.group(0)[1:-1]
                    vals = str_val.split(" . ")
                    obj = (int(vals[0]),int(vals[1]))
                    o.append(obj)
                else:
                    match = weight_pattern.match(sline)
                    if match is not None:
                        s = match.group(0)
                        mw = int(s.split(" ")[-1][:-1])

            file.close()
    return o, mw


def genetic_knapsack(objs, maxw, popsize=100, timelimit=30):
    population = list()
    # Randomly select a populations of knapsacks
    for i in range(popsize):
        sack = list()
        result_weight = 0
        sval = 0
        while result_weight < maxw:
            obj = random.choice(objs)
            if obj[0] + result_weight > max_weight:
                break
            sack.append(obj)
            result_weight += obj[0]
            sval += obj[1]
        population.append(Knapsack(sack, result_weight, sval, maxw))
        population.sort(key=lambda ks: ks.value())

    # Select which collections to keep
    fitsize = popsize / 4

    start = time.time()
    cur_time = time.time()

    # Repeat several times to find optimal solution...
    while population[0].weight() < maxw and cur_time - start < timelimit:
        population = population[:fitsize]
        for i in range(fitsize, popsize):
            population.append(random.choice(population))

        # Cross the collection and mutate, recalculate
        for i in range(0,popsize,2):
            tmp = population[i].objects()
            population[i].cross(population[i+1].objects())
            population[i+1].cross(tmp)
            if random.randint(0,100) < 10:
                population[i].mutate(objs)
                population[i+1].mutate(objs)

        # Check the state of the knapsack
        population.sort(key=lambda ks: ks.value() + ks.weight())

        cur_time = time.time()

    return population[0]


################################################################################
# RUN THE PROGRAM
#
objects, max_weight = parse(DATA_DIR + "testfile.txt")
knapsack = genetic_knapsack(objects, max_weight)
print "Maxweight",max_weight
print "Value",knapsack.value()
print "Weight",knapsack.weight()
for p in knapsack.objects():
    print p
