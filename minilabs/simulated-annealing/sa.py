################################################################################
# SIMULATED ANNEALING MINILAB
#
import os
import copy
import random
import numpy
import logging
from collections import defaultdict


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
# Parse a text file formatted for easy knapsack object retrieval
#
def parse_knapsack(filename):
    """
    Parses a text file formatted for easy knapsack object retrieval. The
    first line is the maximum weight of the knapsack. Each subsequent line is a
    space-delimited weight-value pair

    :param filename: The name of the file to parse
    :return: A tuple containing the knapsack max_weight and a list of objects
    """
    if os.path.exists(filename):
        objects = list()
        with open(filename, "r") as input_file:
            max_weight = int(input_file.readline().rstrip())
            for line in input_file:
                parts = line.split()
                objects.append(int(parts[0]), int(parts[1]))
            input_file.close()
        return max_weight, objects


# ------------------------------------------------------------------------------
# Algorithm implementation
#
def sa_knapsack(objects, max_weight):
    """
    Solves the knapsack problem using the simulated annealing algorithm.

    :param objects: A list of objects that can be included in the knapsack. Each
                    object is  a weight, value pair
    :param max_weight: The maximum weight of the knapsack
    :return: A knapsack object containing the solution
    """

    return None


################################################################################
################################################################################
# RUN THE PROGRAM
#
def main():
    max_weight, objects = parse_knapsack("./data.txt")
    solution = sa_knapsack(objects, max_weight)
    print str(solution)


if __name__ == "__main__":
    main()
