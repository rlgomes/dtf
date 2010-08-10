#!/bin/bash

DATE=`date +%d-%m-%Y.%H:%M:%S`
BASE=/home/dtftest

rm -fr $BASE/build

mkdir $BASE/build
mkdir $BASE/build/logs

rm -fr $BASE/gh-pages
echo "Pulling gh-pages..."
git clone ssh://git@github.com/rlgomes/dtf.git -b gh-pages gh-pages > $BASE/build/logs/gh-pages-pull.log 2>&1

cd $BASE/build
echo "Pulling DTF..."
git clone ssh://git@github.com/rlgomes/dtf.git > $BASE/build/logs/clone.log 2>&1

echo "Building DTF..."
cd dtf
ant clean > $BASE/build/logs/clean.log 2>&1
ant build >> $BASE/build/logs/build.log 2>&1

echo "Running JUnit tests..."
ant junit > $BASE/build/logs/junit.log 2>&1

cd build/dtf/dist
echo "Running DTF unit tests..."
./ant.sh run_ut > $BASE/build/logs/ut.log 2>&1

cd $BASE/build/dtf

echo "Generating test reports..."
ant junit-reports > $BASE/build/logs/junit-reports.log 2>&1

echo "Copying logs/results to results repository..."
cp -fr $BASE/build/logs $BASE/gh-pages/results/
cp -fr $BASE/build/dtf/build/dtf/reports/* $BASE/gh-pages/results/
cp -fr $BASE/build/dtf/build/dtf/junit_results $BASE/gh-pages/results/
cp $BASE/build/dtf/build/dtf/dist/tests/ut/output/TEST_ut_results.xml $BASE/gh-pages/results/
cp $BASE/build/dtf/build/dtf/dist/tests/ut/output/script-ut.out $BASE/gh-pages/results/

echo "Uploading results to github..."
cd $BASE/gh-pages
git add results > $BASE/build/logs/upload.log 2>&1
git commit -m "Test results for $DATE" >> $BASE/build/logs/upload.log 2>&1
git push origin gh-pages >> $BASE/build/logs/upload.log 2>&1

cd $BASE
echo "Downloading tools..."
wget http://github.com/rlgomes/ec2-tools/raw/master/src/ec2-api.py -O ec2-api.py
chmod +x ec2-api.py

echo "Sleeping for 10 minutes in case someone wants to login to do maintenance"
sleep 600
./stop.sh
