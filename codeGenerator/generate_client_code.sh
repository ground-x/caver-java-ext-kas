#!/bin/bash

# ./generate_client_code.sh {service name} {version} {directory name} -> This case `master` branch will be used as a default
# ./generate_client_code.sh {service name} {version} {directory name} {branch}
# ./generate_client_code.sh anchor v1 anchor

# You need to fill variable KAS_REF_DOCS_PATH and PROJECT_API_PATH
KAS_REF_DOCS_PATH=~/git/kas-ref-docs
PROJECT_API_PATH=~/git/caver-java-ext-kas

API_SRC_PATH=$PROJECT_API_PATH/src/main/java/xyz/groundx/caver_ext_kas/rest_client/io/swagger/client/api

branch=$4

if [ -z "$branch" ]; then
   branch="master"
fi

echo ${KAS_REF_DOCS_PATH}
echo ${PROJECT_API_PATH}
echo ${API_SRC_PATH}
echo "Rest client code is generated from the yaml file in ${branch} branch."

# Make rest-client code.
cd $KAS_REF_DOCS_PATH
make clean
git checkout $branch
git fetch upstream $branch
git reset --hard upstream/$branch
make .build/sdk/java/$1/$2

# Copy the rest-client code
#cd ../src/main/java/xyz/groundx/caver_ext_kas/rest_client/io/swagger/client/api
cd $API_SRC_PATH
rm -rf ./$3/api ./$3/model
mkdir ./$3/api ./$3/model
#.build/sdk/java/anchor/v1/src/main/java/xyz/groundx/caver_ext_kas/rest_client/io/swagger/client/api/anchor
cp $KAS_REF_DOCS_PATH/.build/sdk/java/$1/$2/src/main/java/xyz/groundx/caver_ext_kas/rest_client/io/swagger/client/api/$1/api/* ./$3/api
cp $KAS_REF_DOCS_PATH/.build/sdk/java/$1/$2/src/main/java/xyz/groundx/caver_ext_kas/rest_client/io/swagger/client/api/$1/model/* ./$3/model
