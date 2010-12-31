#!/usr/bin/env python
"""
Simple utility to generate a line graph from command line 
"""
from pygooglechart import Chart
from pygooglechart import SimpleLineChart
from pygooglechart import Axis
import sys

data = eval(sys.argv[1])
ydata = eval(sys.argv[2])
filename = sys.argv[3]

# replace ydata with labels and add those to the legend

lstart = ord('A')
elegends = [""] 
lydata = []

for yd in ydata:
    c = chr(lstart)
    elegends.append(c + " = " + str(yd))
    lydata.append(c)
    lstart = lstart + 1

# 25% larger than the biggest value in any of the series
max_y = int(round(max([max(d)*1.25 for (_,d) in data]),-2))
chart = SimpleLineChart(640, 320, y_range=[0, max_y])

# Add the chart data
for (_,d) in data:
    chart.add_data(d)

# set line colors per data set

mastercolors = ['FF0000', # RED
                '00FF00', # GREEN
                '0000FF', # BLUE
                '00FFFF', # AQUA
                'FFFF00', # YELLOW
                'FF00FF', # PURPLE
               ] 
colors = mastercolors[0:len(data)]

colors.append('FFFFFF')
for _ in elegends:
    colors.append('000000');

chart.set_colours(colors)

legends = [n for (n,_) in data]
for l in elegends:
    legends.append(l);
    
chart.set_legend(legends)

# a 10 markers tops and no marker at 0 to avoid Ovalerlapping with X's 0
steps = int(round(max_y/10,-1))
left_axis = range(0, max_y + 1, steps)
left_axis[0] = ''
chart.set_axis_labels(Axis.LEFT, left_axis)

# X axis Labels
chart.set_axis_labels(Axis.BOTTOM, lydata)
chart.download(filename)
