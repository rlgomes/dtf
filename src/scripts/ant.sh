#!/bin/bash

export PATH=`pwd`/apache-ant-1.6.0/bin:$PATH

export CLASSPATH=$CLASSPATH:lib

export LD_LIBRARY_PATH=$LD_LIBRARYPATH:/home/y/lib:/home/y/lib64

ant $@