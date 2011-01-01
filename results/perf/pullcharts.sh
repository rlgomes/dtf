#!/bin/bash


# chart for arithmetic.abs
echo "getting chart for arithmetic.abs"
./createlc.py '[("arithmetic.abs",[4545.455,1315.790,3571.428,1960.784])]' '["92bddc215864b0e6fa7efe7d261df3019831543f11714","92bddc215864b0e6fa7efe7d261df3019831543f30461","92bddc215864b0e6fa7efe7d261df3019831543f29514","92bddc215864b0e6fa7efe7d261df3019831543f14452"]' arithmetic.abs.png

# chart for arithmetic.mul
echo "getting chart for arithmetic.mul"
./createlc.py '[("arithmetic.mul",[5555.556,6250.000,3571.428,1666.667])]' '["92bddc215864b0e6fa7efe7d261df3019831543f11714","92bddc215864b0e6fa7efe7d261df3019831543f30461","92bddc215864b0e6fa7efe7d261df3019831543f29514","92bddc215864b0e6fa7efe7d261df3019831543f14452"]' arithmetic.mul.png

# chart for arithmetic.div
echo "getting chart for arithmetic.div"
./createlc.py '[("arithmetic.div",[3030.303,1818.182,2439.024,9090.909])]' '["92bddc215864b0e6fa7efe7d261df3019831543f11714","92bddc215864b0e6fa7efe7d261df3019831543f30461","92bddc215864b0e6fa7efe7d261df3019831543f29514","92bddc215864b0e6fa7efe7d261df3019831543f14452"]' arithmetic.div.png

# chart for arithmetic.sub
echo "getting chart for arithmetic.sub"
./createlc.py '[("arithmetic.sub",[5263.158,4761.905,6250.000,1639.344])]' '["92bddc215864b0e6fa7efe7d261df3019831543f11714","92bddc215864b0e6fa7efe7d261df3019831543f30461","92bddc215864b0e6fa7efe7d261df3019831543f29514","92bddc215864b0e6fa7efe7d261df3019831543f14452"]' arithmetic.sub.png

# chart for arithmetic.mod
echo "getting chart for arithmetic.mod"
./createlc.py '[("arithmetic.mod",[3846.154,6666.667,20000.000,9090.909])]' '["92bddc215864b0e6fa7efe7d261df3019831543f11714","92bddc215864b0e6fa7efe7d261df3019831543f30461","92bddc215864b0e6fa7efe7d261df3019831543f29514","92bddc215864b0e6fa7efe7d261df3019831543f14452"]' arithmetic.mod.png

# chart for arithmetic.add
echo "getting chart for arithmetic.add"
./createlc.py '[("arithmetic.add",[1408.451,1960.784,1754.386,1612.903])]' '["92bddc215864b0e6fa7efe7d261df3019831543f11714","92bddc215864b0e6fa7efe7d261df3019831543f30461","92bddc215864b0e6fa7efe7d261df3019831543f29514","92bddc215864b0e6fa7efe7d261df3019831543f14452"]' arithmetic.add.png
