#!/usr/bin/env python

import string
import os.path

def getLocalTime(filename):
    print 'Get local time in file ' + filename
    ltime = []

    overall_time = 0
    calculation_time = 0
    total_time = 0

    with open(filename, 'r') as f:
        lines = f.readlines()
    f.closed

    for line in lines:
        pos = line.find('Time needed overAll:')
        if (pos != -1):
            pos = pos + len('Time needed overAll:')
            overall_time = string.atof(line[pos:])

        pos = line.find('Time needed for calculation:')
        if (pos != -1):
            pos = pos + len('Time needed for calculation:')
            calculation_time = string.atof(line[pos:])

        pos = line.find('real	')
        if (pos != -1):
            pos = pos + len('real	')
            str_time = line[pos:]
            total_time = to_second(str_time)

    ltime.append(overall_time)
    ltime.append(calculation_time)
    ltime.append(total_time)

    return ltime

def to_second(str_time):
    pos = str_time.find('m')
    minutes = str_time[:pos]
    secs = str_time[pos+1 : len(str_time)-2]

    total_sec = 60 * string.atoi(minutes)
    total_sec = total_sec + string.atof(secs)

    return total_sec

def getDistributedTime(filename):
    print 'Get distributed time in file ' + filename
    dis_time = []

    startup_time = 0
    solving_time = 0
    total_time = 0

    with open(filename, 'r') as f:
        lines = f.readlines()
    f.closed

    for line in lines:
        pos = line.find('INFO: startup time =')
        if (pos != -1):
            pos = pos + len('INFO: startup time =')
            str_time = line[pos:]
            startup_time = string.atof(str_time) / 1000

        pos = line.find('Solving time =')
        if (pos != -1):
            pos = pos + len('Solving time =')
            str_time = line[pos:]
            solving_time = solving_time + string.atoi(str_time)

        pos = line.find('real	')
        if (pos != -1):
            pos = pos + len('real	')
            str_time = line[pos:]
            total_time = to_second(str_time)

    dis_time.append(startup_time)
    dis_time.append(1.0 * solving_time / 1000)
    dis_time.append(total_time)

    return dis_time


def main(argv):
    with open('instances.txt', 'r') as f:
        instances = f.readlines()
    f.closed

    answers = ['1', '2']
    absolute_path = os.path.abspath( __file__ )
    print absolute_path
    last_slash = absolute_path.rfind('/')
    absolute_path = absolute_path[:last_slash]
    print absolute_path    

    for instance in instances:
        # kill the last \n
        instance = instance[:len(instance)-1]
        for answer in answers:
            local_filename = absolute_path + '/instance-' + instance + '-local-' + answer + '.log'
            central_filename = absolute_path + '/instance-' + instance + '-central-' + answer + '.log'
            dis_filename = absolute_path + '/instance-' + instance + '-dis-' + answer + '.log'

            local_time = getLocalTime(local_filename)
            print local_time

            central_time = getDistributedTime(central_filename)
            print central_time

            dis_time = getDistributedTime(dis_filename)
            print dis_time

if __name__ == "__main__":
    import sys
    main(sys.argv[1:])
