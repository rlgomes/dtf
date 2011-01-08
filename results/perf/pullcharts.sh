#!/bin/bash


# chart for xml.xpath.complex_xpath.access
echo "getting chart for xml.xpath.complex_xpath.access"
./createlc.py '[("xml.xpath.complex_xpath.access",[555])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' xml.xpath.complex_xpath.access.png

# chart for xml.xpath.simple_xpath.access
echo "getting chart for xml.xpath.simple_xpath.access"
./createlc.py '[("xml.xpath.simple_xpath.access",[2777])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' xml.xpath.simple_xpath.access.png

# chart for http.delete
echo "getting chart for http.delete"
./createlc.py '[("http.delete",[2051.703])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' http.delete.png

# chart for http.head
echo "getting chart for http.head"
./createlc.py '[("http.head",[2123.593])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' http.head.png

# chart for http.get
echo "getting chart for http.get"
./createlc.py '[("http.get",[2008.435])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' http.get.png

# chart for http.put
echo "getting chart for http.put"
./createlc.py '[("http.put",[1128.286])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' http.put.png

# chart for http.post
echo "getting chart for http.post"
./createlc.py '[("http.post",[943.663])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' http.post.png

# chart for rendezvous.remote.visits
echo "getting chart for rendezvous.remote.visits"
./createlc.py '[("rendezvous.remote.visits",[285])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' rendezvous.remote.visits.png

# chart for rendezvous.local.visits
echo "getting chart for rendezvous.local.visits"
./createlc.py '[("rendezvous.local.visits",[11111])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' rendezvous.local.visits.png

# chart for share.cumulative.local.syncs
echo "getting chart for share.cumulative.local.syncs"
./createlc.py '[("share.cumulative.local.syncs",[4166])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' share.cumulative.local.syncs.png

# chart for share.cumulative.remote.syncs
echo "getting chart for share.cumulative.remote.syncs"
./createlc.py '[("share.cumulative.remote.syncs",[294])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' share.cumulative.remote.syncs.png

# chart for share.queue.local.syncs
echo "getting chart for share.queue.local.syncs"
./createlc.py '[("share.queue.local.syncs",[4166])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' share.queue.local.syncs.png

# chart for share.queue.remote.syncs
echo "getting chart for share.queue.remote.syncs"
./createlc.py '[("share.queue.remote.syncs",[294])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' share.queue.remote.syncs.png

# chart for share.single.local.syncs
echo "getting chart for share.single.local.syncs"
./createlc.py '[("share.single.local.syncs",[4166])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' share.single.local.syncs.png

# chart for share.single.remote.syncs
echo "getting chart for share.single.remote.syncs"
./createlc.py '[("share.single.remote.syncs",[256])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' share.single.remote.syncs.png

# chart for range.access
echo "getting chart for range.access"
./createlc.py '[("range.access",[45454])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' range.access.png

# chart for random.gaussianLong.generator
echo "getting chart for random.gaussianLong.generator"
./createlc.py '[("random.gaussianLong.generator",[25000])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' random.gaussianLong.generator.png

# chart for random.randomDouble.generator
echo "getting chart for random.randomDouble.generator"
./createlc.py '[("random.randomDouble.generator",[20833])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' random.randomDouble.generator.png

# chart for random.randomLong.generator
echo "getting chart for random.randomLong.generator"
./createlc.py '[("random.randomLong.generator",[26315])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' random.randomLong.generator.png

# chart for random.randomInt.generator
echo "getting chart for random.randomInt.generator"
./createlc.py '[("random.randomInt.generator",[26315])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' random.randomInt.generator.png

# chart for property.resolution
echo "getting chart for property.resolution"
./createlc.py '[("property.resolution",[38461])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' property.resolution.png

# chart for function.calls
echo "getting chart for function.calls"
./createlc.py '[("function.calls",[22317.444])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' function.calls.png

# chart for choices
echo "getting chart for choices"
./createlc.py '[("choices",[31595.577])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' choices.png

# chart for stats.txt.calcs
echo "getting chart for stats.txt.calcs"
./createlc.py '[("stats.txt.calcs",[62500])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' stats.txt.calcs.png

# chart for query.txt.queries
echo "getting chart for query.txt.queries"
./createlc.py '[("query.txt.queries",[13000])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' query.txt.queries.png

# chart for recorder.csv.writes
echo "getting chart for recorder.csv.writes"
./createlc.py '[("recorder.csv.writes",[40000])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' recorder.csv.writes.png

# chart for recorder.txt.writes
echo "getting chart for recorder.txt.writes"
./createlc.py '[("recorder.txt.writes",[40000])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' recorder.txt.writes.png

# chart for events.local
echo "getting chart for events.local"
./createlc.py '[("events.local",[13495.277])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' events.local.png

# chart for events.remote
echo "getting chart for events.remote"
./createlc.py '[("events.remote",[116.098])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' events.remote.png

# chart for arithmetic.abs
echo "getting chart for arithmetic.abs"
./createlc.py '[("arithmetic.abs",[14697.237])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' arithmetic.abs.png

# chart for arithmetic.mul
echo "getting chart for arithmetic.mul"
./createlc.py '[("arithmetic.mul",[16030.779])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' arithmetic.mul.png

# chart for arithmetic.div
echo "getting chart for arithmetic.div"
./createlc.py '[("arithmetic.div",[15542.431])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' arithmetic.div.png

# chart for arithmetic.sub
echo "getting chart for arithmetic.sub"
./createlc.py '[("arithmetic.sub",[15146.925])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' arithmetic.sub.png

# chart for arithmetic.mod
echo "getting chart for arithmetic.mod"
./createlc.py '[("arithmetic.mod",[15752.993])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' arithmetic.mod.png

# chart for arithmetic.add
echo "getting chart for arithmetic.add"
./createlc.py '[("arithmetic.add",[13724.952])]' '["113753c8465aa4d4813a0dd7d873ac5f64b65df9_08012011024317"]' arithmetic.add.png
