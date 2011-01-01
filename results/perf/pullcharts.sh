#!/bin/bash


# chart for xml.xpath.complex_xpath.access
echo "getting chart for xml.xpath.complex_xpath.access"
./createlc.py '[("xml.xpath.complex_xpath.access",[9223372036854775807,625])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' xml.xpath.complex_xpath.access.png

# chart for xml.xpath.simple_xpath.access
echo "getting chart for xml.xpath.simple_xpath.access"
./createlc.py '[("xml.xpath.simple_xpath.access",[9223372036854775807,3125])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' xml.xpath.simple_xpath.access.png

# chart for http.delete
echo "getting chart for http.delete"
./createlc.py '[("http.delete",[684.932,2089.428])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' http.delete.png

# chart for http.head
echo "getting chart for http.head"
./createlc.py '[("http.head",[558.659,2112.378])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' http.head.png

# chart for http.get
echo "getting chart for http.get"
./createlc.py '[("http.get",[704.225,2085.506])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' http.get.png

# chart for http.put
echo "getting chart for http.put"
./createlc.py '[("http.put",[490.196,1286.339])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' http.put.png

# chart for http.post
echo "getting chart for http.post"
./createlc.py '[("http.post",[248.139,1133.787])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' http.post.png

# chart for rendezvous.remote.visits
echo "getting chart for rendezvous.remote.visits"
./createlc.py '[("rendezvous.remote.visits",[100,384])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' rendezvous.remote.visits.png

# chart for rendezvous.local.visits
echo "getting chart for rendezvous.local.visits"
./createlc.py '[("rendezvous.local.visits",[100,14285])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' rendezvous.local.visits.png

# chart for share.cumulative.local.syncs
echo "getting chart for share.cumulative.local.syncs"
./createlc.py '[("share.cumulative.local.syncs",[100,5000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' share.cumulative.local.syncs.png

# chart for share.cumulative.remote.syncs
echo "getting chart for share.cumulative.remote.syncs"
./createlc.py '[("share.cumulative.remote.syncs",[100,370])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' share.cumulative.remote.syncs.png

# chart for share.queue.local.syncs
echo "getting chart for share.queue.local.syncs"
./createlc.py '[("share.queue.local.syncs",[100,5000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' share.queue.local.syncs.png

# chart for share.queue.remote.syncs
echo "getting chart for share.queue.remote.syncs"
./createlc.py '[("share.queue.remote.syncs",[100,370])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' share.queue.remote.syncs.png

# chart for share.single.local.syncs
echo "getting chart for share.single.local.syncs"
./createlc.py '[("share.single.local.syncs",[100,5000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' share.single.local.syncs.png

# chart for share.single.remote.syncs
echo "getting chart for share.single.remote.syncs"
./createlc.py '[("share.single.remote.syncs",[100,333])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' share.single.remote.syncs.png

# chart for range.access
echo "getting chart for range.access"
./createlc.py '[("range.access",[9223372036854775807,55555])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' range.access.png

# chart for random.gaussianLong.generator
echo "getting chart for random.gaussianLong.generator"
./createlc.py '[("random.gaussianLong.generator",[9223372036854775807,33333])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' random.gaussianLong.generator.png

# chart for random.randomDouble.generator
echo "getting chart for random.randomDouble.generator"
./createlc.py '[("random.randomDouble.generator",[9223372036854775807,26315])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' random.randomDouble.generator.png

# chart for random.randomLong.generator
echo "getting chart for random.randomLong.generator"
./createlc.py '[("random.randomLong.generator",[9223372036854775807,33333])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' random.randomLong.generator.png

# chart for random.randomInt.generator
echo "getting chart for random.randomInt.generator"
./createlc.py '[("random.randomInt.generator",[9223372036854775807,35714])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' random.randomInt.generator.png

# chart for property.resolution
echo "getting chart for property.resolution"
./createlc.py '[("property.resolution",[9223372036854775807,50000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' property.resolution.png

# chart for function.calls
echo "getting chart for function.calls"
./createlc.py '[("function.calls",[33333.333,24277.738])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' function.calls.png

# chart for choices
echo "getting chart for choices"
./createlc.py '[("choices",[49999.998,34797.132])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' choices.png

# chart for stats.txt.calcs
echo "getting chart for stats.txt.calcs"
./createlc.py '[("stats.txt.calcs",[9223372036854775807,62500])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' stats.txt.calcs.png

# chart for query.txt.queries
echo "getting chart for query.txt.queries"
./createlc.py '[("query.txt.queries",[11000,14000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' query.txt.queries.png

# chart for recorder.csv.writes
echo "getting chart for recorder.csv.writes"
./createlc.py '[("recorder.csv.writes",[100,50000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' recorder.csv.writes.png

# chart for recorder.txt.writes
echo "getting chart for recorder.txt.writes"
./createlc.py '[("recorder.txt.writes",[100,50000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' recorder.txt.writes.png

# chart for events.local
echo "getting chart for events.local"
./createlc.py '[("events.local",[128.634,156.838])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' events.local.png

# chart for events.remote
echo "getting chart for events.remote"
./createlc.py '[("events.remote",[5263.158,14645.577])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' events.remote.png

# chart for arithmetic.abs
echo "getting chart for arithmetic.abs"
./createlc.py '[("arithmetic.abs",[16666.667,16281.341])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' arithmetic.abs.png

# chart for arithmetic.mul
echo "getting chart for arithmetic.mul"
./createlc.py '[("arithmetic.mul",[1724.138,17070.673])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' arithmetic.mul.png

# chart for arithmetic.div
echo "getting chart for arithmetic.div"
./createlc.py '[("arithmetic.div",[9090.909,17094.017])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' arithmetic.div.png

# chart for arithmetic.sub
echo "getting chart for arithmetic.sub"
./createlc.py '[("arithmetic.sub",[10000.000,16567.264])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' arithmetic.sub.png

# chart for arithmetic.mod
echo "getting chart for arithmetic.mod"
./createlc.py '[("arithmetic.mod",[4545.455,17135.024])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' arithmetic.mod.png

# chart for arithmetic.add
echo "getting chart for arithmetic.add"
./createlc.py '[("arithmetic.add",[4545.455,15375.153])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f"]' arithmetic.add.png
