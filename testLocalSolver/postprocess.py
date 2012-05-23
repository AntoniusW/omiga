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
    status = '---' # No status

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
    
    # Run me: python postprocess

    # copy header
    #tex_output = open('./table.tex', 'w')
    #copy_text(tex_output, 'template/tex_header.tpl')

    # read row separation template
    #with open('template/tex_row_separation.tpl', 'r') as rs:
    #    row_sep = rs.readlines()
    #rs.closed

    engines = ['clingo', 'dlv', 'asperix', 'omiga']
    #testnames = ['reach', 'birds', 'party', '3col', 'stratProg']
    testnames = ['reach', 'birds']
    count_testruns = [0, 0]
    count_instances = []
    final_table = [] # this will be constructed as a 2-dimensional table, 
                     # each row corresponding to a testname and 
                     # each column to an engine
    final_shade = []

    dir_name = ''

    testruns = []
    with open('instances.txt', 'r') as f:
        for line in f:
            # Get rid of the last '\n' character
            line = line[:len(line)-1]
            testruns.append(line)
    f.closed

    instances = []
        
    for testname in testnames:
        for testrun in testruns:
            if (testrun.find(testname) != -1):
                print 'Found ' + testname + ' in ' + testrun

                test_id = testnames.index(testname)
                count_testruns[test_id] = count_testruns[test_id] + 1

                last_slash = testrun.rfind('/')
                last_dash = testrun.rfind('-')
                instance = testrun[last_slash+1 : last_dash]
                if (instances.count(instance) == 0):
                    count_instances.append(1)
                    instances.append(instance)
                    print 'Add instance: ' + instance
                else:
                    ins_id = instances.index(instance)
                    count_instances[ins_id] = count_instances[ins_id] + 1;

    for instance in instances:
        for testrun in testruns:
            if (testrun.find(instance) != -1):
                timing = []
                shade = []

                for en in engines:
                    # shade if it's not for 1 answer
                    last_dash = testrun.rfind('-')
                    num_answer = string.atoi(testrun[last_dash+1:])
                    if (num_answer == 1):
                        shade.append(0)
                    else:
                        shade.append(1)

                    # get running time
                    outname = testrun + '-' + en
                    outcome = get_outcome(outname)
                    timing.append(outcome)
                    print "reading from " + outname + ". got outcome = " + outcome

                final_table.append(timing)
                final_shade.append(shade)

    print instances
    print count_instances
    print count_testruns

    

    # Every outcome is now in the final_table
    i = 0

    for testrun in testruns:
        print '   ' + testrun + ':'
        sys.stdout.write('   ')
        print final_table[i]
        i = i+1

    total_run = 0
    for run in count_testruns:
        total_run = total_run + run;

    with open('table.tex', 'w') as f:
        f.write('\\begin{table}[t]\n')
        f.write('\\centering\n')
        f.write('\\caption{Evaluation of the solver.}\n')
        f.write('\\label{tab:experiment}\n')
        f.write('\\footnotesize\n')

        f.write('\\begin{tabular}{l')
        for i in range(total_run):
            f.write('c')
        f.write('}\n')

        f.write('\\toprule\n')
        f.write('Solver')

        for testname in testnames:
            test_id = testnames.index(testname)
            f.write(' & \\multicolumn{' + str(count_testruns[test_id]) + '}{c}{' + testname + '}')
        f.write('\\\\\n')

        for en in engines:
            f.write('\\midrule\n')
            en_id = engines.index(en)
            f.write(en)
            for testrun in testruns:
                test_id = testruns.index(testrun)
                if (final_shade[test_id][en_id] == 0):
                    f.write(' & ' + str(final_table[test_id][en_id]))
                else:
                    f.write(' & xxx' + str(final_table[test_id][en_id]))
            f.write('\\\\\n')

        f.write('\\bottomrule\n')
        f.write('\\end{tabular}\n')
        f.write('\\end{table}\n')

    f.closed

if __name__ == "__main__":
    import sys
    main(sys.argv[1:])
