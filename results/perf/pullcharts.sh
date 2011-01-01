#!/bin/bash


# chart for xml.xpath.complex_xpath.access
echo "getting chart for xml.xpath.complex_xpath.access"
./createlc.py '[("xml.xpath.complex_xpath.access",[666])]' '["01-01-2011.09:02:58"]' xml.xpath.complex_xpath.access.png

# chart for xml.xpath.simple_xpath.access
echo "getting chart for xml.xpath.simple_xpath.access"
./createlc.py '[("xml.xpath.simple_xpath.access",[3125])]' '["01-01-2011.09:02:58"]' xml.xpath.simple_xpath.access.png

# chart for http.delete
echo "getting chart for http.delete"
./createlc.py '[("http.delete",[2051.703])]' '["01-01-2011.09:02:58"]' http.delete.png

# chart for http.head
echo "getting chart for http.head"
./createlc.py '[("http.head",[2097.755])]' '["01-01-2011.09:02:58"]' http.head.png

# chart for http.get
echo "getting chart for http.get"
./createlc.py '[("http.get",[2085.506])]' '["01-01-2011.09:02:58"]' http.get.png

# chart for http.put
echo "getting chart for http.put"
./createlc.py '[("http.put",[1258.020])]' '["01-01-2011.09:02:58"]' http.put.png

# chart for http.post
echo "getting chart for http.post"
./createlc.py '[("http.post",[1103.144])]' '["01-01-2011.09:02:58"]' http.post.png

# chart for rendezvous.remote.visits
echo "getting chart for rendezvous.remote.visits"
./createlc.py '[("rendezvous.remote.visits",[370])]' '["01-01-2011.09:02:58"]' rendezvous.remote.visits.png

# chart for rendezvous.local.visits
echo "getting chart for rendezvous.local.visits"
./createlc.py '[("rendezvous.local.visits",[14285])]' '["01-01-2011.09:02:58"]' rendezvous.local.visits.png

# chart for share.cumulative.local.syncs
echo "getting chart for share.cumulative.local.syncs"
./createlc.py '[("share.cumulative.local.syncs",[5000])]' '["01-01-2011.09:02:58"]' share.cumulative.local.syncs.png

# chart for share.cumulative.remote.syncs
echo "getting chart for share.cumulative.remote.syncs"
./createlc.py '[("share.cumulative.remote.syncs",[357])]' '["01-01-2011.09:02:58"]' share.cumulative.remote.syncs.png

# chart for share.queue.local.syncs
echo "getting chart for share.queue.local.syncs"
./createlc.py '[("share.queue.local.syncs",[4545])]' '["01-01-2011.09:02:58"]' share.queue.local.syncs.png

# chart for share.queue.remote.syncs
echo "getting chart for share.queue.remote.syncs"
./createlc.py '[("share.queue.remote.syncs",[370])]' '["01-01-2011.09:02:58"]' share.queue.remote.syncs.png

# chart for share.single.local.syncs
echo "getting chart for share.single.local.syncs"
./createlc.py '[("share.single.local.syncs",[5000])]' '["01-01-2011.09:02:58"]' share.single.local.syncs.png

# chart for share.single.remote.syncs
echo "getting chart for share.single.remote.syncs"
./createlc.py '[("share.single.remote.syncs",[333])]' '["01-01-2011.09:02:58"]' share.single.remote.syncs.png

# chart for range.access
echo "getting chart for range.access"
./createlc.py '[("range.access",[55555])]' '["01-01-2011.09:02:58"]' range.access.png

# chart for random.gaussianLong.generator
echo "getting chart for random.gaussianLong.generator"
./createlc.py '[("random.gaussianLong.generator",[33333])]' '["01-01-2011.09:02:58"]' random.gaussianLong.generator.png

# chart for random.randomDouble.generator
echo "getting chart for random.randomDouble.generator"
./createlc.py '[("random.randomDouble.generator",[26315])]' '["01-01-2011.09:02:58"]' random.randomDouble.generator.png

# chart for random.randomLong.generator
echo "getting chart for random.randomLong.generator"
./createlc.py '[("random.randomLong.generator",[33333])]' '["01-01-2011.09:02:58"]' random.randomLong.generator.png

# chart for random.randomInt.generator
echo "getting chart for random.randomInt.generator"
./createlc.py '[("random.randomInt.generator",[33333])]' '["01-01-2011.09:02:58"]' random.randomInt.generator.png

# chart for property.resolution
echo "getting chart for property.resolution"
./createlc.py '[("property.resolution",[50000])]' '["01-01-2011.09:02:58"]' property.resolution.png

# chart for function.calls
echo "getting chart for function.calls"
./createlc.py '[("function.calls",[23661.918])]' '["01-01-2011.09:02:58"]' function.calls.png

# chart for choices
echo "getting chart for choices"
./createlc.py '[("choices",[34195.049])]' '["01-01-2011.09:02:58"]' choices.png

# chart for stats.txt.calcs
echo "getting chart for stats.txt.calcs"
./createlc.py '[("stats.txt.calcs",[62500])]' '["01-01-2011.09:02:58"]' stats.txt.calcs.png

# chart for query.txt.queries
echo "getting chart for query.txt.queries"
./createlc.py '[("query.txt.queries",[14000])]' '["01-01-2011.09:02:58"]' query.txt.queries.png

# chart for recorder.csv.writes
echo "getting chart for recorder.csv.writes"
./createlc.py '[("recorder.csv.writes",[50000])]' '["01-01-2011.09:02:58"]' recorder.csv.writes.png

# chart for recorder.txt.writes
echo "getting chart for recorder.txt.writes"
./createlc.py '[("recorder.txt.writes",[50000])]' '["01-01-2011.09:02:58"]' recorder.txt.writes.png

# chart for events.local
echo "getting chart for events.local"
./createlc.py '[("events.local",[156.104])]' '["01-01-2011.09:02:58"]' events.local.png

# chart for events.remote
echo "getting chart for events.remote"
./createlc.py '[("events.remote",[14556.041])]' '["01-01-2011.09:02:58"]' events.remote.png

# chart for arithmetic.abs
echo "getting chart for arithmetic.abs"
./createlc.py '[("arithmetic.abs",[15923.566])]' '["01-01-2011.09:02:58"]' arithmetic.abs.png

# chart for arithmetic.mul
echo "getting chart for arithmetic.mul"
./createlc.py '[("arithmetic.mul",[17193.948])]' '["01-01-2011.09:02:58"]' arithmetic.mul.png

# chart for arithmetic.div
echo "getting chart for arithmetic.div"
./createlc.py '[("arithmetic.div",[16778.523])]' '["01-01-2011.09:02:58"]' arithmetic.div.png

# chart for arithmetic.sub
echo "getting chart for arithmetic.sub"
./createlc.py '[("arithmetic.sub",[15964.240])]' '["01-01-2011.09:02:58"]' arithmetic.sub.png

# chart for arithmetic.mod
echo "getting chart for arithmetic.mod"
./createlc.py '[("arithmetic.mod",[16772.895])]' '["01-01-2011.09:02:58"]' arithmetic.mod.png

# chart for arithmetic.add
echo "getting chart for arithmetic.add"
./createlc.py '[("arithmetic.add",[14854.427])]' '["01-01-2011.09:02:58"]' arithmetic.add.png
