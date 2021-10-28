#!/bin/bash
set -eo pipefail
ARTIFACT_BUCKET=convertimage-artifacts
TEMPLATE=template.yml
gradle -q packageLibs
mv build/distributions/convertImage-1.0.zip build/convertImageLib.zip
gradle build -i
aws cloudformation package --template-file $TEMPLATE --s3-bucket $ARTIFACT_BUCKET --output-template-file out.yml
aws cloudformation deploy --template-file out.yml --stack-name convertimage-java --capabilities CAPABILITY_NAMED_IAM