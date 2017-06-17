SCRIPT_PATH=$(cd $(dirname ${BASH_SOURCE:-$0}); pwd)
if [ ! -z "${SCRIPT_PATH// }" ]; then
    rm -rf -- $SCRIPT_PATH/../classes/*/ *.class
    echo "rm -rf -- ${SCRIPT_PATH}/../classes/*/ *.class"
fi
