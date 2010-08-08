#!/bin/bash

DATE=`date +%d-%m-%Y.%H:%M:%S`

rm -fr build
# none of those files are required to upload the new ones
rm -fr results/*

mkdir build
mkdir build/logs

cd build

echo "Pulling DTF..."
git clone ssh://git@github.com/rlgomes/dtf.git > ~/build/logs/clone.log 2>&1

echo "Building DTF..."
cd dtf
ant clean > ~/build/logs/clean.log 2>&1
ant build >> ~/build/logs/build.log 2>&1

echo "Running JUnit tests..."
ant junit > ~/build/logs/junit.log 2>&1

cd build/dtf/dist
echo "Running DTF unit tests..."
./ant.sh run_ut > ~/build/logs/ut.log 2>&1

cd ~/build/dtf

echo "Generating test reports..."
ant junit-reports > ~/build/logs/junit-reports.log 2>&1

echo "Copying logs/results to results repository..."

cp -fr ~/build/logs ~/gh-pages/results/
cp -fr ~/build/dtf/build/dtf/reports/* ~/gh-pages/results/
cp -fr ~/build/dtf/build/dtf/junit_results ~/gh-pages/results/
cp ~/build/dtf/build/dtf/dist/tests/ut/output/TEST_ut_results.xml ~/gh-pages/results/
cp ~/build/dtf/build/dtf/dist/tests/ut/output/script-ut.out ~/gh-pages/results/

cd ~/gh-pages
git add results
git commit -m "test results for $DATE"
git push origin gh-pages


