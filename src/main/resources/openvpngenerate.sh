#!/bin/bash

cd ~/easy-rsa
echo "cd ~/easy-rsa"
yes "" | ./easyrsa gen-req $1 nopass
echo "./easyrsa gen-req $1 nopass"
cp pki/private/$1.key ~/client-configs/keys/
echo "cp pki/private/$1.key ~/client-configs/keys/"
cp pki/reqs/$1.req /tmp
echo "cp pki/reqs/$1.req /tmp"
cd ../easy-rsa2
echo "cd ../easy-rsa2"
./easyrsa import-req /tmp/$1.req $1
echo "./easyrsa import-req /tmp/$1.req $1"
cat <<-EOF |  ./easyrsa sign-req client $1
yes
EOF
echo "./easyrsa sign-req client $1"
cp pki/issued/$1.crt /tmp
echo "cp pki/issued/$1.crt /tmp"
cp pki/issued/$1.crt ~/client-configs/keys/
echo "cp pki/issued/$1.crt ~/client-configs/keys/"
cd ~/client-configs
echo "cd ~/client-configs"
./make_config.sh $1
echo "./make_config.sh $1"
ls ~/client-configs/files
