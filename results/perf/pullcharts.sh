#!/bin/bash


# chart for xml.xpath.complex_xpath.access
echo "getting chart for xml.xpath.complex_xpath.access"
./createlc.py '[("xml.xpath.complex_xpath.access",[555,555,555,666,625,555,714,666])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' xml.xpath.complex_xpath.access.png

# chart for xml.xpath.simple_xpath.access
echo "getting chart for xml.xpath.simple_xpath.access"
./createlc.py '[("xml.xpath.simple_xpath.access",[2777,2777,2941,3125,3125,2777,3333,3333])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' xml.xpath.simple_xpath.access.png

# chart for http.delete
echo "getting chart for http.delete"
./createlc.py '[("http.delete",[2057.190,2041.650,2050.441,2113.271,2069.537,2023.063,2272.727,2304.147])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' http.delete.png

# chart for http.head
echo "getting chart for http.head"
./createlc.py '[("http.head",[2131.287,2121.791,2069.965,2129.472,2140.869,2054.232,2340.824,2220.742])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' http.head.png

# chart for http.get
echo "getting chart for http.get"
./createlc.py '[("http.get",[1990.842,2111.932,2085.506,2124.496,2034.174,2066.116,2247.696,2310.002])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' http.get.png

# chart for http.put
echo "getting chart for http.put"
./createlc.py '[("http.put",[1117.194,1125.999,1099.868,1322.052,1270.648,1088.495,1383.317,1392.758])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' http.put.png

# chart for http.post
echo "getting chart for http.post"
./createlc.py '[("http.post",[918.274,932.227,931.099,1131.990,1106.317,922.339,1221.300,1221.300])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' http.post.png

# chart for rendezvous.remote.visits
echo "getting chart for rendezvous.remote.visits"
./createlc.py '[("rendezvous.remote.visits",[285,285,285,384,384,285,400,400])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' rendezvous.remote.visits.png

# chart for rendezvous.local.visits
echo "getting chart for rendezvous.local.visits"
./createlc.py '[("rendezvous.local.visits",[10526,10526,11111,14285,13333,10526,13333,13333])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' rendezvous.local.visits.png

# chart for share.cumulative.local.syncs
echo "getting chart for share.cumulative.local.syncs"
./createlc.py '[("share.cumulative.local.syncs",[4166,3846,4166,5000,5000,3846,5000,5000])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' share.cumulative.local.syncs.png

# chart for share.cumulative.remote.syncs
echo "getting chart for share.cumulative.remote.syncs"
./createlc.py '[("share.cumulative.remote.syncs",[277,294,285,370,370,294,384,370])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' share.cumulative.remote.syncs.png

# chart for share.queue.local.syncs
echo "getting chart for share.queue.local.syncs"
./createlc.py '[("share.queue.local.syncs",[4166,3846,4166,5000,5000,4166,5000,5000])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' share.queue.local.syncs.png

# chart for share.queue.remote.syncs
echo "getting chart for share.queue.remote.syncs"
./createlc.py '[("share.queue.remote.syncs",[285,294,294,384,370,294,370,400])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' share.queue.remote.syncs.png

# chart for share.single.local.syncs
echo "getting chart for share.single.local.syncs"
./createlc.py '[("share.single.local.syncs",[4166,4166,4166,5000,5000,3846,5000,4545])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' share.single.local.syncs.png

# chart for share.single.remote.syncs
echo "getting chart for share.single.remote.syncs"
./createlc.py '[("share.single.remote.syncs",[250,250,250,333,333,256,344,333])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' share.single.remote.syncs.png

# chart for range.access
echo "getting chart for range.access"
./createlc.py '[("range.access",[45454,45454,45454,55555,55555,45454,62500,62500])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' range.access.png

# chart for random.gaussianLong.generator
echo "getting chart for random.gaussianLong.generator"
./createlc.py '[("random.gaussianLong.generator",[25000,26315,25000,33333,31250,26315,35714,35714])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' random.gaussianLong.generator.png

# chart for random.randomDouble.generator
echo "getting chart for random.randomDouble.generator"
./createlc.py '[("random.randomDouble.generator",[21739,21739,21739,26315,25000,21739,27777,27777])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' random.randomDouble.generator.png

# chart for random.randomLong.generator
echo "getting chart for random.randomLong.generator"
./createlc.py '[("random.randomLong.generator",[26315,26315,26315,33333,31250,26315,35714,35714])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' random.randomLong.generator.png

# chart for random.randomInt.generator
echo "getting chart for random.randomInt.generator"
./createlc.py '[("random.randomInt.generator",[26315,27777,27777,33333,33333,27777,38461,35714])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' random.randomInt.generator.png

# chart for property.resolution
echo "getting chart for property.resolution"
./createlc.py '[("property.resolution",[38461,41666,41666,50000,50000,41666,55555,55555])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' property.resolution.png

# chart for function.calls
echo "getting chart for function.calls"
./createlc.py '[("function.calls",[22612.157,22641.851,22257.834,23622.791,24275.380,22765.561,25101.661,25702.978])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' function.calls.png

# chart for choices
echo "getting chart for choices"
./createlc.py '[("choices",[32364.554,32492.853,32847.195,34935.718,34652.436,32617.913,36840.555,37285.608])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' choices.png

# chart for stats.txt.calcs
echo "getting chart for stats.txt.calcs"
./createlc.py '[("stats.txt.calcs",[55555,62500,62500,62500,62500,55555,71428,71428])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' stats.txt.calcs.png

# chart for query.txt.queries
echo "getting chart for query.txt.queries"
./createlc.py '[("query.txt.queries",[13000,13000,13000,14000,14000,13000,15000,15000])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' query.txt.queries.png

# chart for recorder.csv.writes
echo "getting chart for recorder.csv.writes"
./createlc.py '[("recorder.csv.writes",[40000,40000,40000,50000,50000,40000,50000,50000])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' recorder.csv.writes.png

# chart for recorder.txt.writes
echo "getting chart for recorder.txt.writes"
./createlc.py '[("recorder.txt.writes",[40000,40000,40000,50000,50000,40000,50000,50000])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' recorder.txt.writes.png

# chart for events.local
echo "getting chart for events.local"
./createlc.py '[("events.local",[93.093,93.058,90.302,158.730,159.744,87.689,168.976,163.345])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' events.local.png

# chart for events.remote
echo "getting chart for events.remote"
./createlc.py '[("events.remote",[13743.815,13312.034,13192.612,15051.174,14025.245,12886.598,15239.257,15518.312])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' events.remote.png

# chart for arithmetic.abs
echo "getting chart for arithmetic.abs"
./createlc.py '[("arithmetic.abs",[14488.554,14641.289,14894.250,16108.247,16233.767,14723.204,17724.211,17636.684])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' arithmetic.abs.png

# chart for arithmetic.mul
echo "getting chart for arithmetic.mul"
./createlc.py '[("arithmetic.mul",[15688.735,15513.497,16382.700,16638.934,16784.155,15908.368,17655.367,18635.855])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' arithmetic.mul.png

# chart for arithmetic.div
echo "getting chart for arithmetic.div"
./createlc.py '[("arithmetic.div",[15728.217,14480.162,15762.926,16795.432,17433.752,15772.870,18989.746,17743.080])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' arithmetic.div.png

# chart for arithmetic.sub
echo "getting chart for arithmetic.sub"
./createlc.py '[("arithmetic.sub",[15644.555,15812.777,16103.059,16966.406,17059.024,15669.069,16818.029,17818.959])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' arithmetic.sub.png

# chart for arithmetic.mod
echo "getting chart for arithmetic.mod"
./createlc.py '[("arithmetic.mod",[15644.555,15379.883,15964.240,17123.287,17111.568,16196.956,17825.312,18208.303])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' arithmetic.mod.png

# chart for arithmetic.add
echo "getting chart for arithmetic.add"
./createlc.py '[("arithmetic.add",[13564.840,13109.597,12771.392,14983.519,14671.362,13646.288,15323.322,15683.814])]' '[1293775474576,"1293777655458","1293779471283","1293781202149","1293785086721","1293828716776","6bfe1e8fd372b270fa5cfe3d0603f17e4ab0b8f0","fd0d3d254ee6e75dd4c97a44343c3a6409837da1"]' arithmetic.add.png
