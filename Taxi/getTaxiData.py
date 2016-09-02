# coding: utf-8
import os
import os.path

rootdir = "taxidata"

for parent,dirnames,filenames in os.walk(rootdir):
	for filename in filenames:
		print("the full name of the file is:" + os.path.join(parent,filename))

		with open(os.path.join(parent,filename),'r') as file:
			file.readline()
			line = file.readline()

			while line:
				data = line.split(',')
				if data[9] == "0" or data[10] == "0":
					data.clear()
					line = file.readline()
					continue

				with open('taxi.txt', 'a+') as refile:
					refile.writelines(data[9] +  " " + data[10])
					refile.write('\n')

				line = file.readline()
				data.clear()
