#!/bin/bash


# chart for xml.xpath.complex_xpath.access
echo "getting chart for xml.xpath.complex_xpath.access"
./createlc.py '[("xml.xpath.complex_xpath.access",[555,555,625])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' xml.xpath.complex_xpath.access.png

# chart for xml.xpath.simple_xpath.access
echo "getting chart for xml.xpath.simple_xpath.access"
./createlc.py '[("xml.xpath.simple_xpath.access",[2777,2777,2941])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' xml.xpath.simple_xpath.access.png

# chart for http.delete
echo "getting chart for http.delete"
./createlc.py '[("http.delete",[2051.703,2040.400,2018.978])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' http.delete.png

# chart for http.head
echo "getting chart for http.head"
./createlc.py '[("http.head",[2123.593,2081.166,2064.836])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' http.head.png

# chart for http.get
echo "getting chart for http.get"
./createlc.py '[("http.get",[2008.435,2041.233,2039.152])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' http.get.png

# chart for http.put
echo "getting chart for http.put"
./createlc.py '[("http.put",[1128.286,1102.171,1230.164])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' http.put.png

# chart for http.post
echo "getting chart for http.post"
./createlc.py '[("http.post",[943.663,910.498,1094.930])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' http.post.png

# chart for rendezvous.remote.visits
echo "getting chart for rendezvous.remote.visits"
./createlc.py '[("rendezvous.remote.visits",[285,294,370])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' rendezvous.remote.visits.png

# chart for rendezvous.local.visits
echo "getting chart for rendezvous.local.visits"
./createlc.py '[("rendezvous.local.visits",[11111,10526,13333])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' rendezvous.local.visits.png

# chart for share.cumulative.local.syncs
echo "getting chart for share.cumulative.local.syncs"
./createlc.py '[("share.cumulative.local.syncs",[4166,4166,5000])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' share.cumulative.local.syncs.png

# chart for share.cumulative.remote.syncs
echo "getting chart for share.cumulative.remote.syncs"
./createlc.py '[("share.cumulative.remote.syncs",[294,285,357])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' share.cumulative.remote.syncs.png

# chart for share.queue.local.syncs
echo "getting chart for share.queue.local.syncs"
./createlc.py '[("share.queue.local.syncs",[4166,4166,5000])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' share.queue.local.syncs.png

# chart for share.queue.remote.syncs
echo "getting chart for share.queue.remote.syncs"
./createlc.py '[("share.queue.remote.syncs",[294,285,357])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' share.queue.remote.syncs.png

# chart for share.single.local.syncs
echo "getting chart for share.single.local.syncs"
./createlc.py '[("share.single.local.syncs",[4166,4166,5000])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' share.single.local.syncs.png

# chart for share.single.remote.syncs
echo "getting chart for share.single.remote.syncs"
./createlc.py '[("share.single.remote.syncs",[256,256,322])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' share.single.remote.syncs.png

# chart for range.access
echo "getting chart for range.access"
./createlc.py '[("range.access",[45454,45454,55555])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' range.access.png

# chart for random.gaussianLong.generator
echo "getting chart for random.gaussianLong.generator"
./createlc.py '[("random.gaussianLong.generator",[25000,25000,31250])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' random.gaussianLong.generator.png

# chart for random.randomDouble.generator
echo "getting chart for random.randomDouble.generator"
./createlc.py '[("random.randomDouble.generator",[20833,20833,25000])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' random.randomDouble.generator.png

# chart for random.randomLong.generator
echo "getting chart for random.randomLong.generator"
./createlc.py '[("random.randomLong.generator",[26315,25000,33333])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' random.randomLong.generator.png

# chart for random.randomInt.generator
echo "getting chart for random.randomInt.generator"
./createlc.py '[("random.randomInt.generator",[26315,26315,33333])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' random.randomInt.generator.png

# chart for property.resolution
echo "getting chart for property.resolution"
./createlc.py '[("property.resolution",[38461,38461,50000])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' property.resolution.png

# chart for function.calls
echo "getting chart for function.calls"
./createlc.py '[("function.calls",[22317.444,21817.865,23488.513])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' function.calls.png

# chart for choices
echo "getting chart for choices"
./createlc.py '[("choices",[31595.577,30797.658,33487.376])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' choices.png

# chart for stats.txt.calcs
echo "getting chart for stats.txt.calcs"
./createlc.py '[("stats.txt.calcs",[62500,62500,62500])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' stats.txt.calcs.png

# chart for query.txt.queries
echo "getting chart for query.txt.queries"
./createlc.py '[("query.txt.queries",[13000,13000,14000])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' query.txt.queries.png

# chart for recorder.csv.writes
echo "getting chart for recorder.csv.writes"
./createlc.py '[("recorder.csv.writes",[40000,40000,50000])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' recorder.csv.writes.png

# chart for recorder.txt.writes
echo "getting chart for recorder.txt.writes"
./createlc.py '[("recorder.txt.writes",[40000,40000,40000])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' recorder.txt.writes.png

# chart for events.local
echo "getting chart for events.local"
./createlc.py '[("events.local",[13495.277,13102.725,14744.913])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' events.local.png

# chart for events.remote
echo "getting chart for events.remote"
./createlc.py '[("events.remote",[116.098,116.474,178.336])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' events.remote.png

# chart for arithmetic.abs
echo "getting chart for arithmetic.abs"
./createlc.py '[("arithmetic.abs",[14697.237,14204.546,15417.823])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' arithmetic.abs.png

# chart for arithmetic.mul
echo "getting chart for arithmetic.mul"
./createlc.py '[("arithmetic.mul",[16030.779,15318.628,16739.203])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' arithmetic.mul.png

# chart for arithmetic.div
echo "getting chart for arithmetic.div"
./createlc.py '[("arithmetic.div",[15542.431,14961.101,16700.067])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' arithmetic.div.png

# chart for arithmetic.sub
echo "getting chart for arithmetic.sub"
./createlc.py '[("arithmetic.sub",[15146.925,15019.525,16501.650])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' arithmetic.sub.png

# chart for arithmetic.mod
echo "getting chart for arithmetic.mod"
./createlc.py '[("arithmetic.mod",[15752.993,15060.241,16644.474])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' arithmetic.mod.png

# chart for arithmetic.add
echo "getting chart for arithmetic.add"
./createlc.py '[("arithmetic.add",[13724.952,13147.515,14628.438])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317","113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011031123","5d24f07a5015053c8d94d87f326102ce0118bc3a_08012011190222"]' arithmetic.add.png
