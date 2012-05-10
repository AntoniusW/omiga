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
    density = string.atoi(options.density)
    org_provider_density = 3*density / 4

    # setting up the nodes' name and file names *************************
    org_node_name = []
    provider_node_name = []
    student_node_name = []

    org_file_name = []
    provider_file_name = []
    student_file_name = []

    str_instance = 'instance-' + str(n_org) + '-' + str(n_provider) + '-' + str(n_student) + '-' + str(density) + '-'
    central_script_file_name = str_instance + 'central.sh'
    distributed_script_file_name = str_instance + 'dis.sh'

    name_count = 1
    for i in range(0,n_org):
        org_node_name.append('n' + str(name_count))
        org_file_name.append(str_instance + 'org-' + org_node_name[i] + '.lp')
        name_count = name_count + 1

    for i in range(0,n_provider):
        provider_node_name.append('n' + str(name_count))
        provider_file_name.append(str_instance + 'provider-' + provider_node_name[i] + '.lp')
        name_count = name_count + 1

    for i in range(0,n_student):
        student_node_name.append('n' + str(name_count))
        student_file_name.append(str_instance + 'student-' + student_node_name[i] + '.lp')
        name_count = name_count + 1

    for i in range(0,n_org):
        print 'org[' + str(i) + '] name = ' + org_node_name[i] + '. filename = ' + org_file_name[i]

    for i in range(0,n_provider):
        print 'provider[' + str(i) + '] name = ' + provider_node_name[i] + '. filename = ' + provider_file_name[i]

    for i in range(0,n_student):
        print 'student[' + str(i) + '] name = ' + student_node_name[i] + '. filename = ' + student_file_name[i]

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
        my_provider = random.randint(0,n_provider-1)
        while (belong_to_org[my_provider] != 0):
            my_provider = random.randint(0,n_provider-1)
            
        belong_to_org[my_provider] = 1
        print 'connect org[' + str(i) + '] and provider[' + str(my_provider) + ']'
        org_funds_providers[i].append(my_provider)

    # randomly connect the rest of providers to organizations with a probability of 0.75 * density that there is a connection
    for i in range(0,n_provider):
        if (belong_to_org[i] == 0):
            throw_coin = random.randint(0,100)
            if (throw_coin < org_provider_density):
                parent_org = random.randint(0,n_org-1)
                org_funds_providers[parent_org].append(i)
                belong_to_org[i] = 1
                print 'connect org[' + str(parent_org) + '] and provider[' + str(i) + ']'
            else:
                print 'provider[' + str(i) + '] has no parent organization' 

    # connect providers and students
    for i in range(0,n_provider):
        for j in range(0,n_student):
            throw_coin = random.randint(0,100)
            if (throw_coin < density):
                provider_got_student_apps[i].append(j)
                student_applies_providers[j].append(i)
                print 'connect provider[' + str(i) + '] and student[' + str(j) + ']'

    # read templates to write to files **************************************************************************************
    constraint = ''
    provider_template = ''
    student_template = ''

    with open('templates/constraint.tpl', 'r') as constraint_file:
        constraint = constraint_file.readline()
    constraint_file.closed

    with open('templates/provider.tpl', 'r') as provider_file:
        provider_template = provider_file.readlines()
    provider_file.closed

    with open('templates/student.tpl', 'r') as student_file:
        student_template = student_file.readlines()
    student_file.closed

    # write to org files ****************************************************************************************************
    for i in range(0,n_org):
        n_child_providers = len(org_funds_providers[i])
        with open(org_file_name[i], 'w') as f:
            for j in range(0, n_child_providers-1):
                for k in range(j+1, n_child_providers):
                    f.write(constraint.format(provider_node_name[j], provider_node_name[k]))
        f.closed

    # write to provider files **********************************************************************************************
    for i in range(0,n_provider):
        with open(provider_file_name[i], 'w') as f:
            for s in provider_got_student_apps[i]:
                for line in provider_template:
                    f.write(line.format(student_node_name[s]))
        f.closed
            

    # write to student files ***********************************************************************************************
    for i in range(0,n_student):
        with open(student_file_name[i], 'w') as f:
            for p in student_applies_providers[i]:
                for line in student_template:
                    f.write(line.format(provider_node_name[p], student_node_name[i]))
        f.closed

if __name__ == "__main__":
    import sys
    main(sys.argv[1:])
