#!/usr/bin/env python

import getopt, sys
import optparse
import string
import os.path
import random

def main(argv):
    # to run, for example: 
    #    python gen_test.py --org=N1 --provider=N2 --student=N3 --density=N4
    #    python gen_test.py -o=N1 -p=N2 -s=N3 -d=N4
    parser = optparse.OptionParser()
    parser.add_option('-o', '--org',      dest="org")      # number of organizations
    parser.add_option('-p', '--provider', dest="provider") # number of scholarship providers
    parser.add_option('-s', '--student',  dest="student")  # number of students
    parser.add_option('-d', '--density',  dest="density")  # desity of the graph. The total connection is density*(provider*(student+1))/100

    (options, args) = parser.parse_args()

    n_org = string.atoi(options.org)
    n_provider = string.atoi(options.provider)
    n_student = string.atoi(options.student)
    desity = string.atoi(options.density)

    # setting up the node's name *************************
    org_node_name = []
    provider_node_name = []
    student_node_name = []

    name_count = 1
    for i in range(0,n_org):
        org_node_name.append("n" + str(name_count))
        name_count = name_count + 1

    for i in range(0,n_provider):
        provider_node_name.append("n" + str(name_count))
        name_count = name_count + 1

    for i in range(0,n_student):
        student_node_name.append("n" + str(name_count))
        name_count = name_count + 1

    for i in range(0,n_org):
        print "org[" + str(i) + "]'s name = " + org_node_name[i]

    for i in range(0,n_provider):
        print "provider[" + str(i) + "]'s name = " + provider_node_name[i]

    for i in range(0,n_student):
        print "student[" + str(i) + "]'s name = " + student_node_name[i]

    # setting up flags: whether a provider already belongs to an organization?
    belong_to_org = []
    for i in range(0,n_provider):
        belong_to_org.append(0)

    # setting up lists for storing relationships between organizations/providers and providers/students
    org_funds_providers = []
    for i in range (0,n_org):
        org_funds_providers.append([])

    provider_got_student_apps = []
    for i in range (0,n_provider):
        provider_got_student_apps.append([])

    student_applies_providers = []
    for i in range (0,n_student):
        student_applies_providers.append([])

    # now make sure that each organization funds at least one provider
    for i in range(0,n_org):
        my_provider = random.randint(0,n_provider)
        while (belong_to_org[my_provider] != 0):
            my_provider = random.randint(0,n_provider)
            
        belong_to_org[my_provider] = 1
        print "connect org[" + str(i) + "] and provider[" + str(my_provider) + "]"
        org_funds_providers[i].append(my_provider)

if __name__ == "__main__":
    import sys
    main(sys.argv[1:])
