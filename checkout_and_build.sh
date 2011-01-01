#!/bin/bash

DATE=`date +%d-%m-%Y.%H:%M:%S`
BASE=/home/dtftest
DIST=$BASE/build/dtf/build/dtf/dist

function pullsource() { 
    rm -fr $BASE/build
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
}

function buildDTF() { 
    cd $BASE/build/dtf
    echo "Building DTF..."
    ant clean > $BASE/build/logs/clean.log 2>&1
    ant build >> $BASE/build/logs/build.log 2>&1
}

function buildjavadoc() { 
    cd $BASE/build/dtf
    ant doc >> $BASE/build/logs/javadoc.log 2>&1
    cp -fr $DIST/doc/html/dtf/* $BASE/gh-pages/
}

function runjunit() { 
    cd $DIST
    echo "Running JUnit tests..."
    ant junit > $BASE/build/logs/junit.log 2>&1
}

function runut() { 
    cd $DIST
    echo "Running DTF unit tests..."
    ./ant.sh run_ut > $BASE/build/logs/ut.log 2>&1
}

function rundvt() { 
    cd $DIST
    echo "Running DTF deployment verification tests..."
    ./ant.sh run_dvt > $BASE/build/logs/dvt.log 2>&1
}

function runpvt() { 
    cd $DIST
    echo "Running DTF performance verification tests..."
    mkdir -p $BASE/gh-pages/results/perf
#    OPS="-Diterations.small=100 -Diterations.medium=100 -Diterations.large=100 -Diterations.huge=100"
    ./ant.sh run_pvt -Ddtf.perf.path=$BASE/gh-pages/results/perf -Dbuild=$BUILDID $OPS > $BASE/build/logs/pvt.log 2>&1
}

function calccharts() {
    cp $BASE/build/dtf/src/python/*.py $BASE/gh-pages/results/perf

    echo "Generating performance charts"
    cd $BASE/gh-pages/results/perf
    chmod +x *.sh
    chmod +x *.py
    ./pullcharts.sh
    rm *.py

    PERFWIKI=$BASE/dtf.wiki/Performance-test-results.md
    echo -n "Performance results for critical parts of DTF that we need to make " > $PERFWIKI
    echo -n "sure are never affected by changes to the source code. Each of these " >> $PERFWIKI
    echo -n "tests only executes the same operation in a loop over and over from a  " >> $PERFWIKI
    echo -n "a single thread to get a stable number of how many times this operation " >> $PERFWIKI
    echo "can be executed per second." >> $PERFWIKI

    echo "" >> $PERFWIKI
    for I in $BASE/gh-pages/results/perf/*.png
    do
        IMG=`basename $I`
        echo "[[$IMG]]" >> $PERFWIKI
        echo "" >> $PERFWIKI
    done
}

function pushresults() { 

    cd $BASE/dtf.wiki
    for F in *.png
    do 
        git rm $F
    done
    git commit -m "cleaning up previous images"
    git push

    echo "Pushing performance results back to wiki"
    mv -v $BASE/gh-pages/results/perf/*.png $BASE/dtf.wiki/
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
    git add * > $BASE/build/logs/upload.log 2>&1
    git commit -m "Test results for commit $BUILDID on $DATE" >> $BASE/build/logs/upload.log 2>&1
    git push >> $BASE/build/logs/upload.log 2>&1
}

pullsource

cd $BASE/build/dtf
export BUILDID=`git --no-pager log --max-count=1 | grep commit | awk '{print $2}'`
export BUILDID="${BUILDID}_${DATE}"
buildDTF
buildjavadoc
runjunit
runut
#rundvt
runpvt
calccharts
pushresults

cd $BASE
echo "Downloading tools..."
wget http://github.com/rlgomes/ec2-tools/raw/master/src/ec2-api.py -O ec2-api.py --no-check-certificate
chmod +x ec2-api.py

echo "Sleeping for 10 minutes in case someone wants to login to do maintenance"
sleep 600
./stop.sh
