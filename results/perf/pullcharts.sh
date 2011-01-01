#!/bin/bash


# chart for arithmetic.abs
echo "getting chart for arithmetic.abs"
./createlc.py '[("arithmetic.abs",[4545.455,1315.790])]' '["92bddc215864b0e6fa7efe7d261df3019831543f11714","92bddc215864b0e6fa7efe7d261df3019831543f30461"]' arithmetic.abs.png

# chart for arithmetic.mul
echo "getting chart for arithmetic.mul"
./createlc.py '[("arithmetic.mul",[5555.556,6250.000])]' '["92bddc215864b0e6fa7efe7d261df3019831543f11714","92bddc215864b0e6fa7efe7d261df3019831543f30461"]' arithmetic.mul.png

# chart for arithmetic.div
echo "getting chart for arithmetic.div"
./createlc.py '[("arithmetic.div",[3030.303,1818.182])]' '["92bddc215864b0e6fa7efe7d261df3019831543f11714","92bddc215864b0e6fa7efe7d261df3019831543f30461"]' arithmetic.div.png

# chart for arithmetic.sub
echo "getting chart for arithmetic.sub"
./createlc.py '[("arithmetic.sub",[5263.158,4761.905])]' '["92bddc215864b0e6fa7efe7d261df3019831543f11714","92bddc215864b0e6fa7efe7d261df3019831543f30461"]' arithmetic.sub.png

# chart for arithmetic.mod
echo "getting chart for arithmetic.mod"
./createlc.py '[("arithmetic.mod",[3846.154,6666.667])]' '["92bddc215864b0e6fa7efe7d261df3019831543f11714","92bddc215864b0e6fa7efe7d261df3019831543f30461"]' arithmetic.mod.png

# chart for arithmetic.add
echo "getting chart for arithmetic.add"
./createlc.py '[("arithmetic.add",[1408.451,1960.784])]' '["92bddc215864b0e6fa7efe7d261df3019831543f11714","92bddc215864b0e6fa7efe7d261df3019831543f30461"]' arithmetic.add.png
