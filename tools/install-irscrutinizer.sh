#!/bin/sh

# Installs IrScrutinizer in a normal posix manner in PREFIX.

# This script should be run with the rights required to write
# at the desired places. I.e. root a priori not necessary.

MYPROG=IrScrutinizer


if [ $# = 1 ] ; then
    PREFIX=$1
else
    PREFIX=/usr/local
fi

MYPROG_LOWER=$(echo ${MYPROG} | tr A-Z a-z)
MYPROG_UPPER=$(echo ${MYPROG} | tr a-z A-Z)
MYPROG_HOME=${PREFIX}/share/${MYPROG_LOWER}

# Determine the LIB to use
if [ $(uname -m) = 'x86_64' ] ; then
    LIB=${PREFIX}/lib64
    FROMLIB=../native/Linux-amd64
elif [ $(uname -m) = 'x86' ] ; then
    LIB=${PREFIX}/lib
    FROMLIB=../native/Linux-i386
else
    echo "Not programmed for this machine ($(uname -m)) yet, please extend me."
    exit 1
fi

# Check that we are in grouo "lock", complain otherwise
id -Gn | grep -q lock  || {
    if [ "$EUID" != "0" ]; then
        echo "Warning: current user $USER not in group lock"
        echo "run e.g. 'sudo usermod -aG lock $USER' to fix"
    fi
}

# Copy stuff to MYPROG_HOME
install -d ${MYPROG_HOME}
install --mode=444 target/${MYPROG}-jar-with-dependencies.jar ${MYPROG_HOME}
install --mode=444 target/${MYPROG}.png ${MYPROG_HOME}/${MYPROG_LOWER}.png
install --mode=444 target/*.ini ${MYPROG_HOME}
install --mode=444 target/exportformats.xml ${MYPROG_HOME}
install --mode=444 target/doc/README* target/doc/LICENSE* ${MYPROG_HOME}
install -d ${MYPROG_HOME}/arduino
install --mode=444 arduino/* ${MYPROG_HOME}/arduino

# Create trivial wrappers
echo "#!/bin/sh" > ${MYPROG_HOME}/${MYPROG_LOWER}.sh
echo "java -Djava.library.path=${LIB} -jar ${MYPROG_HOME}/${MYPROG}-jar-with-dependencies.jar \"\$@\"" >> ${MYPROG_HOME}/${MYPROG_LOWER}.sh
chmod +x ${MYPROG_HOME}/${MYPROG_LOWER}.sh
ln -sf ${MYPROG_HOME}/${MYPROG_LOWER}.sh ${PREFIX}/bin/${MYPROG_LOWER}

echo "#!/bin/sh" > ${MYPROG_HOME}/irpmaster.sh
echo "java -Djava.library.path=${LIB} -classpath ${MYPROG_HOME}/${MYPROG}-jar-with-dependencies.jar org.harctoolbox.IrpMaster.IrpMaster \"\$@\"" >> ${MYPROG_HOME}/irpmaster.sh
chmod +x ${MYPROG_HOME}/irpmaster.sh
ln -sf ${MYPROG_HOME}/irpmaster.sh ${PREFIX}/bin/irpmaster

# Install documentation
install -d ${PREFIX}/share/doc/${MYPROG_LOWER}
install --mode=444 target/doc/* ${PREFIX}/share/doc/${MYPROG_LOWER}
ln -sf ../doc/${MYPROG_LOWER} ${MYPROG_HOME}/doc

# Install schemas
install -d ${PREFIX}/share/xml/harctoolbox
install --mode=444 ../schemas/*.xsd ${PREFIX}/share/xml/harctoolbox
ln -sf ../xml/harctoolbox ${MYPROG_HOME}/schemas

# Install DecodeIR
install -d ${LIB}
install --mode=444 ${FROMLIB}/libDecodeIR.* ${LIB}

# Install RXTX serial
if [ $(uname -m) = 'x86_64' -a -f /usr/lib64/rxtx/librxtxSerial.so ] ; then
    echo "System librxtxSerial.so found, linking to that instead of installing ours."
    ln -sf /usr/lib64/rxtx/librxtxSerial.so ${LIB}
elif [ $(uname -m) = 'x86' -a -f /usr/lib/rxtx/librxtxSerial.so ] ; then
    echo "System librxtxSerial.so found, linking to that instead of installing ours."
    ln -sf /usr/lib/rxtx/librxtxSerial.so ${LIB}
else
    echo "System librxtxSerial.so NOT found, installing ours."
    install --mode=444 ${FROMLIB}/librxtxSerial* ${LIB}
fi

# Install desktop file
install -d ${PREFIX}/share/applications
sed -e "s|Exec=.*|Exec=/bin/sh \"${PREFIX}/bin/${MYPROG_LOWER}\"|" \
    -e "s|Icon=.*|Icon=${MYPROG_HOME}/${MYPROG_LOWER}.png|" target/${MYPROG_LOWER}.desktop \
  > ${PREFIX}/share/applications/${MYPROG_LOWER}.desktop

echo "Consider deleting old properties with the command "
echo "irscrutinizer --nuke-properties"
