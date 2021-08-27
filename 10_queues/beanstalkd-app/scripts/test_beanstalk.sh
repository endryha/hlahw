#!/bin/bash
siege --content-type 'application/json' -b -c1 -t3m -f "urls.txt"