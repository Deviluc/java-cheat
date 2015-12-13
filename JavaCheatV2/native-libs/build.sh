#!/bin/bash
gcc -o libptrace.so -shared -Wl,-soname,libptrace.so -Wall -fPIC -I/usr/lib/jvm/java-7-oracle/include -I/usr/lib/jvm/java-7-oracle/include/linux ptrace.c
