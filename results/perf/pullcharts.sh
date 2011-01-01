#!/bin/bash


# chart for xml.xpath.complex_xpath.access
echo "getting chart for xml.xpath.complex_xpath.access"
./createlc.py '[("xml.xpath.complex_xpath.access",[9223372036854775807,9223372036854775807,9223372036854775807])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' xml.xpath.complex_xpath.access.png

# chart for xml.xpath.simple_xpath.access
echo "getting chart for xml.xpath.simple_xpath.access"
./createlc.py '[("xml.xpath.simple_xpath.access",[9223372036854775807,9223372036854775807,9223372036854775807])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' xml.xpath.simple_xpath.access.png

# chart for http.delete
echo "getting chart for http.delete"
./createlc.py '[("http.delete",[869.565,781.250,961.538])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' http.delete.png

# chart for http.head
echo "getting chart for http.head"
./createlc.py '[("http.head",[649.351,740.741,909.091])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' http.head.png

# chart for http.get
echo "getting chart for http.get"
./createlc.py '[("http.get",[900.901,757.576,806.452])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' http.get.png

# chart for http.put
echo "getting chart for http.put"
./createlc.py '[("http.put",[480.769,458.716,632.911])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' http.put.png

# chart for http.post
echo "getting chart for http.post"
./createlc.py '[("http.post",[284.091,253.807,262.467])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' http.post.png

# chart for rendezvous.remote.visits
echo "getting chart for rendezvous.remote.visits"
./createlc.py '[("rendezvous.remote.visits",[100,100,100])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' rendezvous.remote.visits.png

# chart for rendezvous.local.visits
echo "getting chart for rendezvous.local.visits"
./createlc.py '[("rendezvous.local.visits",[100,100,100])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' rendezvous.local.visits.png

# chart for share.cumulative.local.syncs
echo "getting chart for share.cumulative.local.syncs"
./createlc.py '[("share.cumulative.local.syncs",[100,100,100])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' share.cumulative.local.syncs.png

# chart for share.cumulative.remote.syncs
echo "getting chart for share.cumulative.remote.syncs"
./createlc.py '[("share.cumulative.remote.syncs",[100,100,100])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' share.cumulative.remote.syncs.png

# chart for share.queue.local.syncs
echo "getting chart for share.queue.local.syncs"
./createlc.py '[("share.queue.local.syncs",[100,100,100])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' share.queue.local.syncs.png

# chart for share.queue.remote.syncs
echo "getting chart for share.queue.remote.syncs"
./createlc.py '[("share.queue.remote.syncs",[100,100,100])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' share.queue.remote.syncs.png

# chart for share.single.local.syncs
echo "getting chart for share.single.local.syncs"
./createlc.py '[("share.single.local.syncs",[100,100,100])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' share.single.local.syncs.png

# chart for share.single.remote.syncs
echo "getting chart for share.single.remote.syncs"
./createlc.py '[("share.single.remote.syncs",[100,100,100])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' share.single.remote.syncs.png

# chart for range.access
echo "getting chart for range.access"
./createlc.py '[("range.access",[9223372036854775807,9223372036854775807,9223372036854775807])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' range.access.png

# chart for random.gaussianLong.generator
echo "getting chart for random.gaussianLong.generator"
./createlc.py '[("random.gaussianLong.generator",[9223372036854775807,9223372036854775807,9223372036854775807])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' random.gaussianLong.generator.png

# chart for random.randomDouble.generator
echo "getting chart for random.randomDouble.generator"
./createlc.py '[("random.randomDouble.generator",[9223372036854775807,9223372036854775807,9223372036854775807])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' random.randomDouble.generator.png

# chart for random.randomLong.generator
echo "getting chart for random.randomLong.generator"
./createlc.py '[("random.randomLong.generator",[9223372036854775807,9223372036854775807,9223372036854775807])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' random.randomLong.generator.png

# chart for random.randomInt.generator
echo "getting chart for random.randomInt.generator"
./createlc.py '[("random.randomInt.generator",[9223372036854775807,9223372036854775807,9223372036854775807])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' random.randomInt.generator.png

# chart for property.resolution
echo "getting chart for property.resolution"
./createlc.py '[("property.resolution",[9223372036854775807,9223372036854775807,9223372036854775807])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' property.resolution.png

# chart for function.calls
echo "getting chart for function.calls"
./createlc.py '[("function.calls",[33333.333,33333.333,2564.103])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' function.calls.png

# chart for choices
echo "getting chart for choices"
./createlc.py '[("choices",[49999.998,2380.952,49999.998])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' choices.png

# chart for stats.txt.calcs
echo "getting chart for stats.txt.calcs"
./createlc.py '[("stats.txt.calcs",[9223372036854775807,9223372036854775807,9223372036854775807])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' stats.txt.calcs.png

# chart for query.txt.queries
echo "getting chart for query.txt.queries"
./createlc.py '[("query.txt.queries",[3000,12000,12000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' query.txt.queries.png

# chart for recorder.csv.writes
echo "getting chart for recorder.csv.writes"
./createlc.py '[("recorder.csv.writes",[100,100,100])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' recorder.csv.writes.png

# chart for recorder.txt.writes
echo "getting chart for recorder.txt.writes"
./createlc.py '[("recorder.txt.writes",[100,100,100])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' recorder.txt.writes.png

# chart for events.local
echo "getting chart for events.local"
./createlc.py '[("events.local",[124.285,130.753,127.779])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' events.local.png

# chart for events.remote
echo "getting chart for events.remote"
./createlc.py '[("events.remote",[1388.889,1282.051,2000.000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' events.remote.png

# chart for arithmetic.abs
echo "getting chart for arithmetic.abs"
./createlc.py '[("arithmetic.abs",[1538.462,813.008,943.396])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' arithmetic.abs.png

# chart for arithmetic.mul
echo "getting chart for arithmetic.mul"
./createlc.py '[("arithmetic.mul",[9090.909,2000.000,4545.455])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' arithmetic.mul.png

# chart for arithmetic.div
echo "getting chart for arithmetic.div"
./createlc.py '[("arithmetic.div",[1818.182,1960.784,1754.386])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' arithmetic.div.png

# chart for arithmetic.sub
echo "getting chart for arithmetic.sub"
./createlc.py '[("arithmetic.sub",[9090.909,9090.909,4761.905])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' arithmetic.sub.png

# chart for arithmetic.mod
echo "getting chart for arithmetic.mod"
./createlc.py '[("arithmetic.mod",[1724.138,16666.667,1960.784])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' arithmetic.mod.png

# chart for arithmetic.add
echo "getting chart for arithmetic.add"
./createlc.py '[("arithmetic.add",[1612.903,2000.000,1666.667])]' '["92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f","92bddc215864b0e6fa7efe7d261df3019831543f32551"]' arithmetic.add.png
