#include <unistd.h>
#include <stdio.h>
#include <sys/ptrace.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <errno.h>


int newProcess(const char * exec_path, char ** arguments) {
	int childDetect = fork();

	if (childDetect == 0) {
		ptrace(PTRACE_TRACEME, 0, NULL, NULL);
		execv(exec_path, arguments);
	} else {
		wait(NULL);
		long trace = ptrace(PTRACE_DETACH, childDetect, NULL, NULL);

		if (trace < 0) {
			return errno;
		}

	}

	return childDetect;
}

long attachProcess(long pid) {
	long result = ptrace(PTRACE_ATTACH, pid, NULL, NULL);
	ptrace(PTRACE_CONT, pid, NULL, NULL);
	wait(NULL);
	return result;
}

long detachProcess(long pid) {
	return ptrace(PTRACE_DETACH, pid, NULL, NULL);
}

long readMemory(long pid, long pointer) {
	return ptrace(PTRACE_PEEKDATA, pid, pointer, NULL);
}

int writeMemory(long pid, long pointer, int data) {
	ptrace(PTRACE_POKEDATA, pid, pointer, data);
	return errno;
}

int getErrorCode() {
	int error = errno;
	errno = 0;
	return error;
}
