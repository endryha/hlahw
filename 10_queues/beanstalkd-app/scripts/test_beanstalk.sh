#!/bin/bash
siege --content-type 'application/json' -b -c1 -t1m -f "urls.txt"