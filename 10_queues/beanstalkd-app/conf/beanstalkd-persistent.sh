#!/sbin/openrc-run
# Copyright 1999-2014 Gentoo Foundation
# Distributed under the terms of MIT
# $Header: /var/cvsroot/gentoo-x86/app-misc/beanstalkd/files/init-1.9,v 1.1 2014/03/31 06:34:28 patrick Exp $

depend() {
	need net
}

start() {
	ebegin "Starting beanstalkd"
	/sbin/start-stop-daemon --start \
        --background \
		--pidfile ${PIDFILE} --make-pidfile \
		--exec ${BEANSTALKD_BINARY} \
		-- -b ${DATADIR} -f1000 -p ${PORT} -l ${ADDR} -u ${USER} -z ${JOB_SIZE}
	eend $?
}

stop() {
	ebegin "Stopping beanstalkd"
	start-stop-daemon --stop --quiet \
		--pidfile ${PIDFILE} \
		--exec ${BEANSTALKD_BINARY}
	eend $?
}