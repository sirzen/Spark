#!/bin/bash
bin="bin"
gen="gen"
echo "copying latest apk binary to apk_dir..."
cp HelloGridView/bin/HelloGridView.apk apk_dir/HelloGridView.apk
cp BTCntroller/bin/BTController.apk apk_dir/BTController.apk

echo "cleaning binary and generated file from git tracking..."
for d in */; do
    echo "Removing *** ${d}${bin} *** from git tracking ..."
    git rm -rf ${d}${bin} 2>/dev/null
    echo "Deleting *** ${d}${bin} *** ..."
    rm -rf ${d}${bin}
    echo "Removing *** ${d}${gen} *** from git tracking ..."
    git rm -rf ${d}${bin} 2>/dev/null
    echo "Deleting *** ${d}${gen} *** ..."
    rm -rf ${d}${gen}
done
