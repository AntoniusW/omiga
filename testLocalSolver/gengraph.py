#!/usr/bin/env python

import getopt, sys
import optparse
import string
import os.path
import random

def copy_content(source, target):
    content = ''
    with open (source, 'r') as s:
        content = s.readlines()
    s.closed
    
    for line in content:
        target.write(line)

def main(argv):
    # Run me: python --gengraph.py --nodes=N --density=D --test=T
    # test = 1: generate cutdege testcase
    # test = 2: generate 3col testcase
    parser = optparse.OptionParser()
    parser.add_option('-n', '--nodes', dest="nodes")
    parser.add_option('-d', '--density', dest="density")
    parser.add_option('-t', '--testcase', dest="testcase")

    (options,args) = parser.parse_args()

    nodes = string.atoi(options.nodes)
    density = string.atoi(options.density)
    testcase = string.atoi(options.testcase)

    absolute_path = os.path.abspath( __file__ )
    print absolute_path
    last_slash = absolute_path.rfind('/')
    absolute_path = absolute_path[:last_slash]

    rules_filename = ''
    testname = ''
    if (testcase == 1):
        testname = 'cutedge'
    else:
        testname = '3col'

    rules_filename = absolute_path + '/encoding/' + testname + '.txt'

    filename = absolute_path + '/input/' + testname + '/' + testname + '-' + str(nodes) + '-' + str(density) + '.txt'
    filename_hide = absolute_path + '/input/' + testname + '/' + testname + '-' + str(nodes) + '-' + str(density) + '-hide.txt'

    with open(filename, 'w') as f: 
        copy_content(rules_filename, f)
        
        edge = []
        for i in range(nodes):
            links = []
            for j in range(nodes):                
                links.append(0)
            edge.append(links)

        for i in range(nodes):
            for j in range(nodes):
                if (i != j):
                    prob = random.randint(0,100)
                    if (prob < density):
                        edge[i][j] = 1

        if (testcase == 1):
            check = random.randint(0,nodes-1)
            f.write('reachable(X,' + str(check) + ') :- reachable(X,Z),reachable(Z,' + str(check) + ').\n')
        else:
            for i in range(nodes):
                f.write('node(' + str(i) + ').\n')

        for i in range(nodes):
            for j in range(nodes):
                if (edge[i][j] == 1):
                    f.write('edge(' + str(i) + ',' + str(j) + ').\n')
    f.closed

    # file with #hide. for ASPeRix
    with open(filename_hide, 'w') as f:
        f.write('#hide.\n')
        copy_content(filename, f)
    f.closed

if __name__ == "__main__":
    import sys
    main(sys.argv[1:])
