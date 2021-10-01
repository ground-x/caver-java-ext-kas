#!/bin/bash

cd ./src/main/java/xyz/groundx/caver_ext_kas/rest_client/io/swagger/client/api
 
DIRS=`ls -d ./*/`
 
for i in ${DIRS};do
      echo "Entering directory=${i}model";
      cd ${i}model;

	  FILES=`ls`

		for j in ${FILES};do

			sed -i "s/&lt;p&gt;&lt;\/p&gt;/<p><\/p>/g" $j
			sed -i "s/&lt;br&gt;/<br>/g" $j
		done

      cd ../../;

      echo "Entering directory=${i}api";
	  cd ${i}api;

	  FILES=`ls`

		for j in ${FILES};do
			sed -i "s/&lt;p&gt;&lt;\/p&gt;/<p><\/p>/g" $j
			sed -i "s/&lt;br&gt;/<br>/g" $j
		done
	  cd ../../;

done