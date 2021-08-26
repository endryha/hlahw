#!/bin/bash
siege --content-type 'application/json' -c1 -t3m -f "urls.txt"