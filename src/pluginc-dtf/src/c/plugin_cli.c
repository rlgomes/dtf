
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#include <unistd.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <sys/time.h>
#include <time.h>

#include <errno.h>

static char* CMD_SEP = " \n";

static char* TEST_EVENT 		= "cli_test";
static char* GETTIMEOFDAY_EVENT = "cli_gettimeofday";
static char* DUMMY_EVENT        = "cli_dummyevent";

// default don't return events to the PluginCLI Java code
static int return_events = 0;

typedef long long LONG;

typedef struct op_struct {
	char* name;

	LONG start;
    LONG stop;

    char* arg1;
    char* arg2;

    LONG result;

    int succeeded;
} op;

FILE* output = NULL;

static void (*printOp)(op) = NULL;

static char* TRUE = "true";
static char* FALSE = "true";

void printHumanReadable(op operation)
{
	if ( operation.succeeded ) {
		fprintf(output,
			    "operation %s succeeded in %lld ms.\n",
			    operation.name,
			    (operation.stop-operation.start));
	} else {
		fprintf(output,
			    "operation %s failed in %lld ms.\n",
			    operation.name,
			    (operation.stop-operation.start));
	}
}

/*
 * Here is where we output events in a format that DTF can understand easily.
 */
void printDTF(op operation)
{
	fprintf(output,
	        "%s.start=%lld\n%s.stop=%lld\n%s.succeeded=%s\n%s.result=%lld\n\n",
	        operation.name, operation.start,
	        operation.name, operation.stop,
	        operation.name, (operation.succeeded ? TRUE : FALSE),
	        operation.name, operation.result);

	// should we return the events to the Java PluginCLI
	if ( return_events ) {
		fprintf(stdout,
		        "%s.start=%lld\n%s.stop=%lld\n%s.succeeded=%s\n%s.result=%lld\n\n",
		        operation.name, operation.start,
		        operation.name, operation.stop,
		        operation.name, (operation.succeeded ? TRUE : FALSE),
		        operation.name, operation.result);
		fflush(stdout);
	}
}

LONG getMilliseconds()
{
	struct timeval time;

	if ( gettimeofday(&time, NULL) )
	{
		fprintf(stderr,
				"Error getting time of day %d: %s",
				errno,
				strerror(errno));
		exit(-1);
	}

	LONG seconds = time.tv_sec;
	LONG microseconds = time.tv_usec;
	return 1000L*seconds + microseconds/1000L;
}


int main(int argc, char **argv)
{
	int errfd = -1;

	int len = 0,
	    read = 0;

	char* line = NULL;
	char* command = NULL;
	op op;

    printOp = printHumanReadable;
	// simple command line interpretation
    if (argc > 1) {
        if (strcmp(argv[1],"-dtf") == 0) {
            printOp = printDTF;
        }
    }

    output = stdout;
    if (argc > 3) {
        if (strcmp(argv[2],"-o") == 0) {
            output = fopen(argv[3], "w");
        }
    }

    if (argc > 5) {
        if (strcmp(argv[4],"-e") == 0) {
            // opening with O_SYNC because we want to be certain that
            // everytime an error occurs the logging is safely kept
            // on the output log file (even though this could hurt performance)
            errfd = open(argv[5],
                         O_WRONLY | O_TRUNC | O_CREAT,
                         S_IRUSR | S_IRGRP | S_IWGRP | S_IWUSR);
            dup2(errfd,STDERR_FILENO);
        }
    }

	while ((read = getline(&line, &len, stdin)) != -1) {
		// comment found just skip this line
		if (line[0] == '#')
			continue;

		command = strtok(line, CMD_SEP);

		if (command == NULL)
			continue;

		if (strcmp(command,"quit") == 0) {
			break;
		} else if ( strcmp(command,"test" ) == 0) {
			op.name = TEST_EVENT;
			op.arg1 = strtok(line, CMD_SEP);
			op.arg2 = strtok(line, CMD_SEP);
			op.start = getMilliseconds();
			// do something;
			op.stop = getMilliseconds();
			op.succeeded = 1;
			printOp(op);
		} else if ( strcmp(command,"gettimeofday" ) == 0) {
			struct timeval tv;
			op.name = GETTIMEOFDAY_EVENT;
			op.start = getMilliseconds();
			op.succeeded = (gettimeofday(&tv,NULL) == 0);
			op.stop = getMilliseconds();
			op.result = tv.tv_sec;
			printOp(op);
		} else if ( strcmp(command,"return_events_on") == 0) {
			return_events = 1;
		} else if ( strcmp(command,"return_events_off") == 0 ) {
			return_events = 0;
		} else {
			fprintf(stderr,"Invalid command [%s]\n",command);
			exit(-1);
		}
	}

	if ( line != NULL) {
		free(line);
	}

    if (output != NULL) {
        fflush(output);
        fclose(output);
    }

    if (errfd != -1) {
        close(errfd);
    }

    return 0;
}
