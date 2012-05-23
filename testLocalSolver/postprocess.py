#!/usr/bin/env python

import getopt, sys
import optparse
import string

# Check whether the test got PASS or FAILED
def check_status(log_status):
    with open(log_status, 'r') as s:
        for line in s:
            status_sign = line.find('PASS')
            if status_sign != -1:
                s.closed
                return 'PASS'
            else:
                status_sign = line.find('FAILED')
                if status_sign != -1:
                    s.closed
                    return 'FAILED'
    s.closed
    return 'UNKNOWN'


def get_running_time(time_file):
    # Watch out: we only expect time of the form MM:SS.SS
    str_time = 'Notime'
    with open(time_file, 'r') as l:
        for timeline in l:
            time_sign = timeline.find('Elapsed (wall clock) time')
            if time_sign != -1:
                last_colon = timeline.rfind(':')
                last_dot = timeline.rfind('.')

                str_minutes = timeline[last_colon-2 : last_colon]
                str_secs = timeline[last_colon+1 : last_dot]
                str_psecs = timeline[last_dot+1 : len(timeline)-1]

                minutes = string.atoi(str_minutes)
                secs = string.atoi(str_secs)

                secs += 60*minutes
                str_time = str(secs) + '.' + str_psecs

                break
    l.closed
    return str_time


def get_status(out_file):
    status = 'No status'

    with open(out_file, 'r') as l:
        for line in l:
            if line.find('std::bad_alloc') != -1:
                status = 'M' # memout for C++
                break
            if line.find('Could not reserve enough space for object heap') != -1:
                status = 'M' # memout for Java
                break
            if line.find('timelimit: sending warning signal 1') != -1:
                status = '---' # timeout
                break

    return status

# Return '---' in case of timeout, 'M' in case of memout
def get_outcome(outname):
    log_status = outname + '.log'
    status = check_status(log_status)

    mess = ''
    if (status == 'PASS'):
        time_file = outname + '.time'
        mess = get_running_time(time_file)
    else:
        out_file = outname + '.out'
        mess = get_status(out_file)

    return mess



def main(argv):
    
    # Run me: python tables.py --file=testcases.txt

    # Get file that contains all test cases
    parser = optparse.OptionParser()
    parser.add_option('-f', '--file', dest="filename")

    (options, args) = parser.parse_args()

    # copy header
    tex_output = open('./table.tex', 'w')
    #copy_text(tex_output, 'template/tex_header.tpl')

    # read row separation template
    with open('template/tex_row_separation.tpl', 'r') as rs:
        row_sep = rs.readlines()
    rs.closed

    engines = ['dlv', 'asperix', 'woc']
    testnames = []
    final_table = [] # this will be constructed as a 2-dimensional table, 
                     # each row corresponding to a testname and 
                     # each column to an engine
    dir_name = ''

    with open(options.filename, 'r') as f:
        for line in f:
            # Get rid of the last '\n' character
            line = line[:len(line)-1]

            # Format of testcases
            # __directory
            # test1 size_11 ... size_1n
            # test2 size_21 ... size_2m
            #
            # For example:
            #
            # __birds
            # birds10000 0 100 1000
            # birds200000 0 1000

            # Now extract the directory name, testname, and all sizes
            find_dir = line.find('__')
            if find_dir != -1:
                dir_name = line[find_dir+2:]
            else:
                find_blank = line.find(' ')
                testname = line[:find_blank]

                sizes = []

                next_blank = line.find(' ', find_blank+1)
                while (next_blank != -1):
                    s = line[find_blank+1 : next_blank]
                    sizes.append(s)
                    find_blank = next_blank
                    next_blank = line.find(' ', find_blank+1)

                # The last size. (There should be at least one size)
                s = line[find_blank+1:]
                sizes.append(s)

                # Iterate over tests cases. For example: birds1000-100-engine.log/.time/.out
                for si in sizes:
                    testnames.append(testname + '-' + si)
                    timing = []

                    for en in engines:
                        outname = 'output/' + dir_name + '/' + testname + '-' + si + '-' + en
                        outcome = get_outcome(outname)
                        timing.append(outcome)
                        #print "reading from " + outname + ". got outcome = " + outcome

                    final_table.append(timing)

    # Every outcome is now in the final_table
    i = 0
    for test in testnames:
        sys.stdout.write(test + ' ')
        print final_table[i]
        i = i+1

if __name__ == "__main__":
    import sys
    main(sys.argv[1:])
