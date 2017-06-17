SCRIPT_PATH=$(cd $(dirname ${BASH_SOURCE:-$0}); pwd)
java -Dfile.encoding=UTF-8 -cp $SCRIPT_PATH/../classes wafflecore/WaffleCore
