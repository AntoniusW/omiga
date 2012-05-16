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

def create_script(script_fn, 
                  n_org, n_provider, n_student, 
                  org_file_name, provider_file_name, student_file_name, 
                  absolute_path,
                  start_template_fn, main_entry):

    # reading in templates ********************
    script_input = ''
    with open(absolute_path + '/templates/script-input-files.tpl', 'r') as script_input_file:
        script_input = script_input_file.readline()
    script_input_file.closed

    script_start_node = ''
    with open(absolute_path + '/' + start_template_fn, 'r') as script_start_node_file:
        script_start_node = script_start_node_file.readlines()
    script_start_node_file.closed

    script_main_entry = ''
    with open(absolute_path + '/templates/script-main-entry.tpl', 'r') as script_main_entry_file:
        script_main_entry = script_main_entry_file.readline()
    script_main_entry_file.closed

    # writing to files
    with open(absolute_path + '/' + script_fn, 'w') as f:
        copy_content(absolute_path + '/templates/script-header.tpl', f)

        # export input file name
        for i in range(0,n_org):
            f.write(script_input.format(i+1, org_file_name[i]))

        for i in range(0,n_provider):
            f.write(script_input.format(i + n_org + 1, provider_file_name[i]))

        for i in range(0,n_student):
            f.write(script_input.format(i + n_org + n_provider + 1, student_file_name[i]))

        # copy middle to f
        copy_content(absolute_path + '/templates/script-middle.tpl', f)

        # start node
        for i in range(0,n_org + n_provider + n_student):
            for line in script_start_node:
                f.write(line.format(i+1, i, n_org + n_provider + n_student))

        copy_content(absolute_path + '/templates/script-second-middle.tpl', f)

        f.write(script_main_entry.format(main_entry, n_org + n_provider + n_student))

        copy_content(absolute_path + '/templates/script-footer.tpl', f)

        f.closed

def create_local_script(absolute_path, 
                        local_program_file_name,
                        local_script_file_name):
    temp = ''
    with open(absolute_path + '/templates/script-local.tpl', 'r') as f:
        temp = f.readlines()
    f.closed

    with open(absolute_path + '/' + local_script_file_name, 'w') as f:
        for line in temp:
            f.write(line.format(local_program_file_name))
    f.closed

def create_one_test_case(n_org, n_provider, n_student, density, org_provider_density, instance, absolute_path):
    # setting up the nodes' name and file names *************************
    org_node_name = []
    provider_node_name = []
    student_node_name = []

    org_file_name = []
    provider_file_name = []
    student_file_name = []

    str_instance = 'instance-' + str(n_org) + '-' + str(n_provider) + '-' + str(n_student) + '-' + str(density) + '-'

    central_script_file_name = str_instance + instance + '-central.sh'
    distributed_script_file_name = str_instance + instance + '-dis.sh'
    local_program_file_name = str_instance + instance + '.lp'
    local_script_file_name = str_instance + instance + '-local.sh'

    name_count = 1
    for i in range(0,n_org):
        org_node_name.append('n' + str(name_count))
        org_file_name.append(str_instance + instance + '-org-' + org_node_name[i] + '.lp')
        name_count = name_count + 1

    for i in range(0,n_provider):
        provider_node_name.append('n' + str(name_count))
        provider_file_name.append(str_instance + instance + '-provider-' + provider_node_name[i] + '.lp')
        name_count = name_count + 1

    for i in range(0,n_student):
        student_node_name.append('n' + str(name_count))
        student_file_name.append(str_instance + instance + '-student-' + student_node_name[i] + '.lp')
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

    # write to org files ****************************************************************************************************
    constraint = ''

    with open(absolute_path + '/templates/org-constraint.tpl', 'r') as constraint_file:
        constraint = constraint_file.readline()
    constraint_file.closed

    for i in range(0,n_org):
        n_child_providers = len(org_funds_providers[i])
        with open(absolute_path + '/' + org_file_name[i], 'w') as f:
            for j in range(0, n_child_providers-1):
                for k in range(j+1, n_child_providers):
                    f.write(constraint.format(provider_node_name[j], provider_node_name[k]))
        f.closed

    # write to provider files ***********************************************************************************************
    provider_template = ''

    with open(absolute_path + '/templates/provider.tpl', 'r') as provider_file:
        provider_template = provider_file.readlines()
    provider_file.closed

    for i in range(0,n_provider):
        with open(absolute_path + '/' + provider_file_name[i], 'w') as f:
            for s in provider_got_student_apps[i]:
                for line in provider_template:
                    f.write(line.format(student_node_name[s]))

            copy_content(absolute_path + '/templates/provider-constraint.tpl', f)

            for s in provider_got_student_apps[i]:
                for t in provider_got_student_apps[i]:
                    if (s != t):
                        f.write('different_student(' + student_node_name[s] + ',' + student_node_name[t] + ').\n')
            f.write('scholarship(s' + str(i) + ').\n')
        f.closed

    # write to student files ***********************************************************************************************
    student_template = ''

    with open(absolute_path + '/templates/student.tpl', 'r') as student_file:
        student_template = student_file.readlines()
    student_file.closed    

    for i in range(0,n_student):
        with open(absolute_path + '/' + student_file_name[i], 'w') as f:
            for p in student_applies_providers[i]:
                for line in student_template:
                    f.write(line.format(provider_node_name[p], student_node_name[i]))

            for s in student_applies_providers[i]:
                for t in student_applies_providers[i]:
                    if (s != t):
                        f.write('different_scholarship(s' + str(s) + ',s' + str(t) + ').\n')

        f.closed

    # write to local program files *****************************************************************************************
    with open(absolute_path + '/' + local_program_file_name, 'w') as f:
        copy_content(absolute_path + '/templates/local-program.tpl', f)

        for i in range(0,n_student):
            f.write('student(' + student_node_name[i] + ').\n')
        f.write('\n')

        for i in range(0,n_provider):
            f.write('scholarship(s' + str(i) + ').\n')
        f.write('\n')

        for i in range(0, len(student_node_name)):
            for j in range(0, len(student_node_name)):
                if (i != j):
                    f.write('different_student(' + student_node_name[i] + ',' + student_node_name[j] + ').\n')
        f.write('\n')

        for i in range(0,n_provider):
            for j in range(0,n_provider):
                if (i != j):
                    f.write('different_scholarship(s' + str(i) + ',s' + str(j) + ').\n') 

    f.closed

    # write to script files ************************************************************************************************

    create_script(central_script_file_name, 
                  n_org, n_provider, n_student, 
                  org_file_name, provider_file_name, student_file_name, 
                  absolute_path,
                  'templates/script-start-node.tpl', 'AController')

    create_script(distributed_script_file_name, 
                  n_org, n_provider, n_student, 
                  org_file_name, provider_file_name, student_file_name, 
                  absolute_path,
                  'templates/script-start-dis-controller.tpl', 'Client')

    create_local_script(absolute_path, 
                        local_program_file_name, 
                        local_script_file_name)

def main(argv):
    # to run, for example: 
    #    python gen_test.py --org=N1 --provider=N2 --student=N3 --density=N4 --num=N5
    #    python gen_test.py -o=N1 -p=N2 -s=N3 -d=N4 -n=N5
    parser = optparse.OptionParser()
    parser.add_option('-o', '--org',      dest="org")      # number of organizations
    parser.add_option('-p', '--provider', dest="provider") # number of scholarship providers
    parser.add_option('-s', '--student',  dest="student")  # number of students
    parser.add_option('-d', '--density',  dest="density")  # desity of the graph. The total connection is density*(provider*(student+1))/100
    parser.add_option('-n', '--num',      dest="num")      # number of test cases a..z

    (options, args) = parser.parse_args()

    n_org = string.atoi(options.org)
    n_provider = string.atoi(options.provider)
    n_student = string.atoi(options.student)
    density = string.atoi(options.density)
    org_provider_density = 3*density / 4
    n_instance = string.atoi(options.num)

    absolute_path = os.path.abspath( __file__ )
    print absolute_path
    last_slash = absolute_path.rfind('/')
    absolute_path = absolute_path[:last_slash]
    print absolute_path

    for i in range(0,n_instance):
        create_one_test_case(n_org, n_provider, n_student, density, org_provider_density, chr(97+i), absolute_path)

if __name__ == "__main__":
    import sys
    main(sys.argv[1:])
