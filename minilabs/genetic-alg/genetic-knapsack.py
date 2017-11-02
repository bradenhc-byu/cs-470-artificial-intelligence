################################################################################
# Algorithm for solving the Knapsack Problem using a Genetic Algorithm
#
import os
import re


ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_DIR = os.path.join(ROOT_DIR, "data/")


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


def genetic_knapsack(objs, maxw):
    result = list()

    return result


################################################################################
# RUN THE PROGRAM
#
objects, max_weight = parse(DATA_DIR + "testfile.txt")
result_set = genetic_knapsack(objects, max_weight)
for p in result_set:
    print p
