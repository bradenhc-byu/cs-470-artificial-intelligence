################################################################################
# Implementation for solving the Knapsack Problem using a Genetic Algorithm
#
import os
import re
import random
import numpy
import time
import copy
from collections import defaultdict

ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_DIR = os.path.join(ROOT_DIR, "data/")


# ------------------------------------------------------------------------------
# Knapsack
#
# Class representing a knapsack in the Knapsack Problem.
#
class Knapsack:

    # Constructor
    def __init__(self, objs=list(), weight=0, value=0, max_weight=0):
        self.__objects = copy.deepcopy(objs)
        self.__weight = weight
        self.__value = value
        self.__max_weight = max_weight

    # Getter/Setter for objects list
    def objects(self, objs=None):
        if objs is None:
            return self.__objects
        else:
            self.__objects = objs

    # Add an object 'obj' to the list of objects
    def add_object(self, obj):
        if obj[0] + self.weight() > self.max_weight():
            return False
        self.__objects.append(obj)
        self.__weight += obj[0]
        self.__value += obj[1]
        return True

    # Remove an object 'obj' from the list of objects
    def remove_object(self, obj):
        if obj not in self.__objects:
            return False
        self.__objects.remove(obj)
        self.__weight -= obj[0]
        self.__value -= obj[1]
        return True

    # Getter/Setter for the knapsack weight
    def weight(self, w=None):
        if w is None:
            return self.__weight
        else:
            self.__weight = w

    # Getter/Setter for the knapsack's maximum weight limit
    def max_weight(self, mw=None):
        if mw is None:
            return self.__max_weight
        else:
            self.__max_weight = mw

    # Getter/Setter for the knapsack's overall value
    def value(self, v=None):
        if v is None:
            return self.__value
        else:
            self.__value = v

    # Fill the knapsack to capacity randomly using the provided objects
    def fill(self, objects, threshold=10, pack=False):
        obj = random.choice(objects)
        while self.add_object(obj):
            obj = random.choice(objects)
        if pack:
            weight_diff = self.max_weight() - self.weight()
            while self.weight() > self.max_weight() - threshold:
                found_none = True
                for o in objects:
                    if o[0] <= weight_diff:
                        self.add_object(o)
                        found_none = False
                        break
                if found_none:
                    obj = random.choice(self.objects())
                    self.remove_object(obj)
                weight_diff = self.max_weight() - self.weight()

    # Cross this instances object list with another list of objects
    # NOTE: This function is genetic algorithm specific
    def cross(self, other_objs):
        # Determine which list is smaller and pick random position based on
        # that list
        if len(other_objs) < len(self.objects()):
            cross = numpy.random.randint(0, int(3 * len(other_objs) / 2))
        else:
            cross = numpy.random.randint(0, int(3 * len(self.objects()) / 2))

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

    # Randomly mutate one of the objects into one provided in the list
    # NOTE: This functions is specific to the genetic algorithm
    def mutate(self, objs):
        pos = numpy.random.randint(0, len(self.objects())-1)
        old_obj = self.objects()[pos]
        index = numpy.random.choice(len(objs))
        obj = objs[index]
        while (self.weight() - old_obj[0] + obj[0]) > self.max_weight():
            pos = numpy.random.randint(0, len(self.objects()) - 1)
            old_obj = self.objects()[pos]
            index = numpy.random.choice(len(objs))
            obj = objs[index]
        self.objects()[pos] = obj
        self.weight(self.weight() - old_obj[0] + obj[0])
        self.value(self.value() - old_obj[1] + obj[1])

    # toString method (used when str() is called on the knapsack object)
    def __str__(self):
        outstr = "---------- KNAPSACK CONTENTS ------------\n"
        outstr += "Weight: {0}/{1}\t\tValue: {2}\n".format(self.weight(),
                                                           self.max_weight(),
                                                           self.value())

        contents = defaultdict(int)
        for o in self.objects():
            contents[o] += 1
        outstr += "Object\t\t\tCount\n"
        for c in contents.keys():
            outstr += "{0}\t\t\t{1}\n".format(c, contents[c])
        return outstr


# ------------------------------------------------------------------------------
# Parse
#
# Parse the lisp formatted contents from the provided file into an object list
# and max_weight value
#
def parse(filename):
    if os.path.exists(filename):
        with open(filename, "r") as input_file:
            o = list()
            mw = 0
            pair_pattern = re.compile("\([0-9]?[0-9] \. [0-9]?[0-9]\)")
            weight_pattern = re.compile("\(setq MaxWeight [0-9][0-9][0-9]\)")
            for line in input_file:
                sline = line.strip()
                match = pair_pattern.match(sline)
                if match is not None:
                    str_val = match.group(0)[1:-1]
                    vals = str_val.split(" . ")
                    obj = (int(vals[0]), int(vals[1]))
                    o.append(obj)
                else:
                    match = weight_pattern.match(sline)
                    if match is not None:
                        s = match.group(0)
                        mw = int(s.split(" ")[-1][:-1])

            input_file.close()
    return o, mw


# ------------------------------------------------------------------------------
# Fitness Function for Knapsack using Values
#
def knapsack_fitness(population):
    fitness = list()
    total_val = 0
    for k in population:
        total_val += k.value()
        fitness.append(k.value())
    for i in range(len(fitness)):
        fitness[i] = float(float(fitness[i]) / float(total_val))
    return fitness


# ------------------------------------------------------------------------------
# Fitness Function for Knapsack using Weights
#
def knapsack_fitness_weight(population):
    fitness = list()
    total_weight = 0
    for k in population:
        total_weight += k.weight()
        fitness.append(k.weight())
    for i in range(len(fitness)):
        fitness[i] = float(float(fitness[i])/float(total_weight))
    return fitness


# ------------------------------------------------------------------------------
# Fitness Function for Knapsack using combination of values and weights
#
def knapsack_fitness_combo(population):
    fitness = list()
    total_weight = 0
    total_value = 0
    for k in population:
        total_weight += k.weight()
        total_value += k.value()
        fitness.append(k.weight() + k.value())
    for i in range(len(fitness)):
        fitness[i] = float(float(fitness[i])/float(total_weight + total_value))
    return fitness


# ------------------------------------------------------------------------------
# Genetic Algorithm for Solving the Knapsack Problem
#
# Takes the following parameters:
#   objs        - The list of available objects to put in the knapsack. Objects
#                 are formatted as (weight, value) pairs
#   maxw        - the maximum weight limit for the knapsack
#   popsize     - the population size to use when reproducing and mutating
#   timelimit   - the timeout in seconds for the algorithm to attemp to find
#                 a solution before terminating
#   fitfunc     - the function that determines the fitness of a knapsack. The
#                 function has only one argument, which is the population (i.e.
#                 the list of knapsacks)
#
# Returns the most 'fit' knapsack object as the best solution
#
def genetic_knapsack(objs, maxw, popsize=1000, timelimit=60,
                     fitfunc=knapsack_fitness):

    print "gk: Generating population of size {0}...".format(popsize)
    population = list()
    # Randomly generate a populations of knapsacks
    for i in range(popsize):
        k = Knapsack()
        k.max_weight(maxw)
        k.fill(objs, pack=True)
        population.append(k)

    # Initialize the timer
    print "gk: Initializing timer..."
    start = time.time()
    cur_time = time.time()

    # Set up some variables to keep track of the most fit solution so far
    best_solution = Knapsack()
    best_solution_count = 0
    best_solution_count_limit = 3
    new_best_found = False

    # Repeat several times to find optimal solution...
    print "gk: Beginning genetic algorithm (timeout={0})".format(timelimit)
    while cur_time - start < timelimit:
        print "gk: Reproducing..."
        new_population = list()
        fit_probabilities = fitfunc(population)
        for i in range(popsize):
            x = numpy.random.choice(population, p=fit_probabilities)
            y = numpy.random.choice(population, p=fit_probabilities)
            x.cross(y.objects())
            if numpy.random.randint(0, 100) < 10:
                x.mutate(objs)
            new_population.append(x)
            if x.value() > best_solution.value():
                best_solution = Knapsack(x.objects(),
                                         x.weight(),
                                         x.value(),
                                         x.max_weight())
                new_best_found = True

        population = new_population

        if new_best_found:
            best_solution_count = 0
            new_best_found = False
            print "gk: Found new best solution!"
        else:
            best_solution_count += 1
            if best_solution_count >= best_solution_count_limit:
                break

        # Update the running time
        cur_time = time.time()

    return best_solution


################################################################################
################################################################################
################################################################################
# RUN THE PROGRAM
#
def knapsack_solution(objects, max_weight):
    v = list()
    w = list()
    for o in objects:
        w.append(o[0])
        v.append(o[1])
    n = len(objects)
    w = max_weight
    m = [[0 for x in range(w)] for y in range(n)]
    for j in range(w):
        m[0][j] = 0

    for i in range(1, n):
        for j in range(w):
            if w[i] > j:
                m[i][j] = m[i - 1][j]
            else:
                m[i][j] = max(m[i - 1][j], m[i - 1][j - w[i]] + v[i])

    print "Solution:", m[n - 1][w - 1]


def main():
    # Run several different types of tests
    objects, max_weight = parse(DATA_DIR + "testfile.txt")

    print "Testing genetic algorithm..."
    knapsack = genetic_knapsack(objects, max_weight,
                                popsize=6000,
                                timelimit=300,
                                fitfunc=knapsack_fitness_combo)
    print str(knapsack)


if __name__ == "__main__":

    main()
