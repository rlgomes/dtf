#!/bin/bash


# chart for xml.xpath.complex_xpath.access
echo "getting chart for xml.xpath.complex_xpath.access"
./createlc.py '[("xml.xpath.complex_xpath.access",[555])]' '[1293775474576]' xml.xpath.complex_xpath.access.png

# chart for xml.xpath.simple_xpath.access
echo "getting chart for xml.xpath.simple_xpath.access"
./createlc.py '[("xml.xpath.simple_xpath.access",[2777])]' '[1293775474576]' xml.xpath.simple_xpath.access.png

# chart for http.delete
echo "getting chart for http.delete"
./createlc.py '[("http.delete",[2057.190])]' '[1293775474576]' http.delete.png

# chart for http.head
echo "getting chart for http.head"
./createlc.py '[("http.head",[2131.287])]' '[1293775474576]' http.head.png

# chart for http.get
echo "getting chart for http.get"
./createlc.py '[("http.get",[1990.842])]' '[1293775474576]' http.get.png

# chart for http.put
echo "getting chart for http.put"
./createlc.py '[("http.put",[1117.194])]' '[1293775474576]' http.put.png

# chart for http.post
echo "getting chart for http.post"
./createlc.py '[("http.post",[918.274])]' '[1293775474576]' http.post.png

# chart for rendezvous.remote.visits
echo "getting chart for rendezvous.remote.visits"
./createlc.py '[("rendezvous.remote.visits",[285])]' '[1293775474576]' rendezvous.remote.visits.png

# chart for rendezvous.local.visits
echo "getting chart for rendezvous.local.visits"
./createlc.py '[("rendezvous.local.visits",[10526])]' '[1293775474576]' rendezvous.local.visits.png

# chart for share.cumulative.local.syncs
echo "getting chart for share.cumulative.local.syncs"
./createlc.py '[("share.cumulative.local.syncs",[4166])]' '[1293775474576]' share.cumulative.local.syncs.png

# chart for share.cumulative.remote.syncs
echo "getting chart for share.cumulative.remote.syncs"
./createlc.py '[("share.cumulative.remote.syncs",[277])]' '[1293775474576]' share.cumulative.remote.syncs.png

# chart for share.queue.local.syncs
echo "getting chart for share.queue.local.syncs"
./createlc.py '[("share.queue.local.syncs",[4166])]' '[1293775474576]' share.queue.local.syncs.png

# chart for share.queue.remote.syncs
echo "getting chart for share.queue.remote.syncs"
./createlc.py '[("share.queue.remote.syncs",[285])]' '[1293775474576]' share.queue.remote.syncs.png

# chart for share.single.local.syncs
echo "getting chart for share.single.local.syncs"
./createlc.py '[("share.single.local.syncs",[4166])]' '[1293775474576]' share.single.local.syncs.png

# chart for share.single.remote.syncs
echo "getting chart for share.single.remote.syncs"
./createlc.py '[("share.single.remote.syncs",[250])]' '[1293775474576]' share.single.remote.syncs.png

# chart for range.access
echo "getting chart for range.access"
./createlc.py '[("range.access",[45454])]' '[1293775474576]' range.access.png

# chart for random.gaussianLong.generator
echo "getting chart for random.gaussianLong.generator"
./createlc.py '[("random.gaussianLong.generator",[25000])]' '[1293775474576]' random.gaussianLong.generator.png

# chart for random.randomDouble.generator
echo "getting chart for random.randomDouble.generator"
./createlc.py '[("random.randomDouble.generator",[21739])]' '[1293775474576]' random.randomDouble.generator.png

# chart for random.randomLong.generator
echo "getting chart for random.randomLong.generator"
./createlc.py '[("random.randomLong.generator",[26315])]' '[1293775474576]' random.randomLong.generator.png

# chart for random.randomInt.generator
echo "getting chart for random.randomInt.generator"
./createlc.py '[("random.randomInt.generator",[26315])]' '[1293775474576]' random.randomInt.generator.png

# chart for property.resolution
echo "getting chart for property.resolution"
./createlc.py '[("property.resolution",[38461])]' '[1293775474576]' property.resolution.png

# chart for function.calls
echo "getting chart for function.calls"
./createlc.py '[("function.calls",[22612.157])]' '[1293775474576]' function.calls.png

# chart for choices
echo "getting chart for choices"
./createlc.py '[("choices",[32364.554])]' '[1293775474576]' choices.png

# chart for stats.txt.calcs
echo "getting chart for stats.txt.calcs"
./createlc.py '[("stats.txt.calcs",[55555])]' '[1293775474576]' stats.txt.calcs.png

# chart for query.txt.queries
echo "getting chart for query.txt.queries"
./createlc.py '[("query.txt.queries",[13000])]' '[1293775474576]' query.txt.queries.png

# chart for recorder.csv.writes
echo "getting chart for recorder.csv.writes"
./createlc.py '[("recorder.csv.writes",[40000])]' '[1293775474576]' recorder.csv.writes.png

# chart for recorder.txt.writes
echo "getting chart for recorder.txt.writes"
./createlc.py '[("recorder.txt.writes",[40000])]' '[1293775474576]' recorder.txt.writes.png

# chart for events.local
echo "getting chart for events.local"
./createlc.py '[("events.local",[93.093])]' '[1293775474576]' events.local.png

# chart for events.remote
echo "getting chart for events.remote"
./createlc.py '[("events.remote",[13743.815])]' '[1293775474576]' events.remote.png

# chart for arithmetic.abs
echo "getting chart for arithmetic.abs"
./createlc.py '[("arithmetic.abs",[14488.554])]' '[1293775474576]' arithmetic.abs.png

# chart for arithmetic.mul
echo "getting chart for arithmetic.mul"
./createlc.py '[("arithmetic.mul",[15688.735])]' '[1293775474576]' arithmetic.mul.png

# chart for arithmetic.div
echo "getting chart for arithmetic.div"
./createlc.py '[("arithmetic.div",[15728.217])]' '[1293775474576]' arithmetic.div.png

# chart for arithmetic.sub
echo "getting chart for arithmetic.sub"
./createlc.py '[("arithmetic.sub",[15644.555])]' '[1293775474576]' arithmetic.sub.png

# chart for arithmetic.mod
echo "getting chart for arithmetic.mod"
./createlc.py '[("arithmetic.mod",[15644.555])]' '[1293775474576]' arithmetic.mod.png

# chart for arithmetic.add
echo "getting chart for arithmetic.add"
./createlc.py '[("arithmetic.add",[13564.840])]' '[1293775474576]' arithmetic.add.png
