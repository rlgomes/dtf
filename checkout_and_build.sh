#!/bin/bash

DATE=`date +%d-%m-%Y.%H:%M:%S`
BASE=/home/dtftest

#rm -fr $BASE/build

mkdir -p $BASE/build
mkdir -p $BASE/build/logs

cd $BASE
rm -fr $BASE/dtf.wiki
echo "Pulling DTF Wiki Pages..."
git clone ssh://git@github.com/rlgomes/dtf.wiki.git > $BASE/build/logs/dtf.wiki.clone.log 2>&1

rm -fr $BASE/gh-pages
echo "Pulling gh-pages..."
git clone ssh://git@github.com/rlgomes/dtf.git -b gh-pages gh-pages > $BASE/build/logs/gh-pages-pull.log 2>&1

cd $BASE/build
echo "Pulling DTF..."
git clone ssh://git@github.com/rlgomes/dtf.git > $BASE/build/logs/dtf.clone.log 2>&1

echo "Building DTF..."
cd $BASE/build/dtf
ant clean > $BASE/build/logs/clean.log 2>&1
ant build >> $BASE/build/logs/build.log 2>&1

echo "Running JUnit tests..."
ant junit > $BASE/build/logs/junit.log 2>&1

cd $BASE/build/dtf/build/dtf/dist/

echo "Running DTF unit tests..."
./ant.sh run_ut > $BASE/build/logs/ut.log 2>&1

echo "Running DTF deployment verification tests..."
./ant.sh run_dvt > $BASE/build/logs/dvt.log 2>&1

echo "Running DTF performance verification tests..."
cd $BASE/build/dtf/build/dtf/dist/
mkdir -p $BASE/gh-pages/results/perf
./ant.sh run_pvt -Ddtf.perf.path=$BASE/gh-pages/results/perf > $BASE/build/logs/pvt.log 2>&1

cp $BASE/build/dtf/src/python/*.py $BASE/gh-pages/results/perf

echo "Generating performance charts"
cd $BASE/gh-pages/results/perf
chmod +x *.sh
chmod +x *.py
./pullcharts.sh

PERFWIKI=$BASE/dtf.wiki/Performance-test-results.md
echo "Performance Results for critical parts of DTF" > $PERFWIKI
echo "" >> $PERFWIKI
for I in $BASE/gh-pages/results/perf/*.png
do
	IMG=`basename $I`
	echo "[[$IMG]]" >> $PERFWIKI
	echo "" >> $PERFWIKI
done

echo "Pushing performance results back to wiki"
mv $BASE/gh-pages/results/perf/*.png $BASE/dtf.wiki/
cd $BASE/dtf.wiki
git add *
git commit -m "pushing performance results"
git push 

echo "Generating test reports..."
cd $BASE/build/dtf
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
wget http://github.com/rlgomes/ec2-tools/raw/master/src/ec2-api.py -O ec2-api.py --no-check-certificate
chmod +x ec2-api.py

echo "Sleeping for 10 minutes in case someone wants to login to do maintenance"
sleep 600
./stop.sh
