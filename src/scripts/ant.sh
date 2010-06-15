#!/bin/bash

export PATH=`pwd`/apache-ant-1.6.0/bin:$PATH

export CLASSPATH=$CLASSPATH:lib

ant $@