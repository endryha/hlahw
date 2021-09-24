#!/bin/bash
./exec_sql.sh "show slave status \G" | grep "mysql_slave_\|Log_File\|Log_Pos\|State\|Seconds_Behind_Master\|Error"