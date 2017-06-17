SCRIPT_PATH=$(cd $(dirname ${BASH_SOURCE:-$0}); pwd)

echo "wafflecore/*.java -------------------------"
javac -J-Dfile.encoding=UTF-8 -d $SCRIPT_PATH/../classes $SCRIPT_PATH/../src/wafflecore/*.java -cp $SCRIPT_PATH/../src
rc=$?; if [ $rc != 0 ]; then exit $rc; else echo "OK"; fi
echo "wafflecore/constants/*.java ---------------"
javac -J-Dfile.encoding=UTF-8 -d $SCRIPT_PATH/../classes $SCRIPT_PATH/../src/wafflecore/constants/*.java -cp $SCRIPT_PATH/../src
rc=$?; if [ $rc != 0 ]; then exit $rc; else echo "OK"; fi
echo "wafflecore/message/*.java -----------------"
javac -J-Dfile.encoding=UTF-8 -d $SCRIPT_PATH/../classes $SCRIPT_PATH/../src/wafflecore/message/*.java -cp $SCRIPT_PATH/../src
rc=$?; if [ $rc != 0 ]; then exit $rc; else echo "OK"; fi
echo "wafflecore/model/*.java -------------------"
javac -J-Dfile.encoding=UTF-8 -d $SCRIPT_PATH/../classes $SCRIPT_PATH/../src/wafflecore/model/*.java -cp $SCRIPT_PATH/../src
rc=$?; if [ $rc != 0 ]; then exit $rc; else echo "OK"; fi
echo "wafflecore/tool/*.java --------------------"
javac -J-Dfile.encoding=UTF-8 -d $SCRIPT_PATH/../classes $SCRIPT_PATH/../src/wafflecore/tool/*.java -cp $SCRIPT_PATH/../src
rc=$?; if [ $rc != 0 ]; then exit $rc; else echo "OK"; fi
echo "wafflecore/util/*.java --------------------"
javac -J-Dfile.encoding=UTF-8 -d $SCRIPT_PATH/../classes $SCRIPT_PATH/../src/wafflecore/util/*.java -cp $SCRIPT_PATH/../src
rc=$?; if [ $rc != 0 ]; then exit $rc; else echo "OK"; fi
