#!/bin/bash


# chart for xml.xpath.complex_xpath.access
echo "getting chart for xml.xpath.complex_xpath.access"
./createlc.py '[("xml.xpath.complex_xpath.access",[625,588])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' xml.xpath.complex_xpath.access.png

# chart for xml.xpath.simple_xpath.access
echo "getting chart for xml.xpath.simple_xpath.access"
./createlc.py '[("xml.xpath.simple_xpath.access",[2941,2777])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' xml.xpath.simple_xpath.access.png

# chart for http.delete
echo "getting chart for http.delete"
./createlc.py '[("http.delete",[2065.689,2086.376])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' http.delete.png

# chart for http.head
echo "getting chart for http.head"
./createlc.py '[("http.head",[2120.441,2119.992])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' http.head.png

# chart for http.get
echo "getting chart for http.get"
./createlc.py '[("http.get",[2093.802,2115.059])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' http.get.png

# chart for http.put
echo "getting chart for http.put"
./createlc.py '[("http.put",[1268.231,1126.888])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' http.put.png

# chart for http.post
echo "getting chart for http.post"
./createlc.py '[("http.post",[1099.505,910.581])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' http.post.png

# chart for rendezvous.remote.visits
echo "getting chart for rendezvous.remote.visits"
./createlc.py '[("rendezvous.remote.visits",[370,285])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' rendezvous.remote.visits.png

# chart for rendezvous.local.visits
echo "getting chart for rendezvous.local.visits"
./createlc.py '[("rendezvous.local.visits",[15384,10526])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' rendezvous.local.visits.png

# chart for share.cumulative.local.syncs
echo "getting chart for share.cumulative.local.syncs"
./createlc.py '[("share.cumulative.local.syncs",[5000,3846])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' share.cumulative.local.syncs.png

# chart for share.cumulative.remote.syncs
echo "getting chart for share.cumulative.remote.syncs"
./createlc.py '[("share.cumulative.remote.syncs",[357,303])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' share.cumulative.remote.syncs.png

# chart for share.queue.local.syncs
echo "getting chart for share.queue.local.syncs"
./createlc.py '[("share.queue.local.syncs",[4545,3846])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' share.queue.local.syncs.png

# chart for share.queue.remote.syncs
echo "getting chart for share.queue.remote.syncs"
./createlc.py '[("share.queue.remote.syncs",[370,285])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' share.queue.remote.syncs.png

# chart for share.single.local.syncs
echo "getting chart for share.single.local.syncs"
./createlc.py '[("share.single.local.syncs",[4545,4166])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' share.single.local.syncs.png

# chart for share.single.remote.syncs
echo "getting chart for share.single.remote.syncs"
./createlc.py '[("share.single.remote.syncs",[322,250])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' share.single.remote.syncs.png

# chart for range.access
echo "getting chart for range.access"
./createlc.py '[("range.access",[55555,45454])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' range.access.png

# chart for random.gaussianLong.generator
echo "getting chart for random.gaussianLong.generator"
./createlc.py '[("random.gaussianLong.generator",[33333,25000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' random.gaussianLong.generator.png

# chart for random.randomDouble.generator
echo "getting chart for random.randomDouble.generator"
./createlc.py '[("random.randomDouble.generator",[26315,20833])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' random.randomDouble.generator.png

# chart for random.randomLong.generator
echo "getting chart for random.randomLong.generator"
./createlc.py '[("random.randomLong.generator",[33333,26315])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' random.randomLong.generator.png

# chart for random.randomInt.generator
echo "getting chart for random.randomInt.generator"
./createlc.py '[("random.randomInt.generator",[35714,26315])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' random.randomInt.generator.png

# chart for property.resolution
echo "getting chart for property.resolution"
./createlc.py '[("property.resolution",[50000,38461])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' property.resolution.png

# chart for function.calls
echo "getting chart for function.calls"
./createlc.py '[("function.calls",[23801.590,22528.611])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' function.calls.png

# chart for choices
echo "getting chart for choices"
./createlc.py '[("choices",[34848.063,32024.595])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' choices.png

# chart for stats.txt.calcs
echo "getting chart for stats.txt.calcs"
./createlc.py '[("stats.txt.calcs",[62500,62500])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' stats.txt.calcs.png

# chart for query.txt.queries
echo "getting chart for query.txt.queries"
./createlc.py '[("query.txt.queries",[14000,13000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' query.txt.queries.png

# chart for recorder.csv.writes
echo "getting chart for recorder.csv.writes"
./createlc.py '[("recorder.csv.writes",[50000,40000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' recorder.csv.writes.png

# chart for recorder.txt.writes
echo "getting chart for recorder.txt.writes"
./createlc.py '[("recorder.txt.writes",[50000,40000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' recorder.txt.writes.png

# chart for events.local
echo "getting chart for events.local"
./createlc.py '[("events.local",[151.286,87.199])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' events.local.png

# chart for events.remote
echo "getting chart for events.remote"
./createlc.py '[("events.remote",[14710.209,12976.901])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' events.remote.png

# chart for arithmetic.abs
echo "getting chart for arithmetic.abs"
./createlc.py '[("arithmetic.abs",[15832.806,14228.799])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' arithmetic.abs.png

# chart for arithmetic.mul
echo "getting chart for arithmetic.mul"
./createlc.py '[("arithmetic.mul",[16728.002,15285.846])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' arithmetic.mul.png

# chart for arithmetic.div
echo "getting chart for arithmetic.div"
./createlc.py '[("arithmetic.div",[16756.032,15220.700])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' arithmetic.div.png

# chart for arithmetic.sub
echo "getting chart for arithmetic.sub"
./createlc.py '[("arithmetic.sub",[17018.380,15370.427])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' arithmetic.sub.png

# chart for arithmetic.mod
echo "getting chart for arithmetic.mod"
./createlc.py '[("arithmetic.mod",[16857.720,15001.500])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' arithmetic.mod.png

# chart for arithmetic.add
echo "getting chart for arithmetic.add"
./createlc.py '[("arithmetic.add",[14637.003,13717.421])]' '["92bddc215864b0e6fa7efe7d261df3019831543f_03012011045900","3e7d8ea8eb212d3ee02eebfc59b07845c3f4e89e_04012011052238"]' arithmetic.add.png
