#!/bin/bash
#set -eo pipefail

STACK=convertimage-java
ARTIFACT_BUCKET=convertimage-artifacts

if [[ $# -eq 1 ]] ; then
    STACK=$1
    echo "Deleting stack $STACK"
fi
APP_BUCKET=$(aws cloudformation describe-stack-resource --stack-name "$STACK" --logical-resource-id bucket --query 'StackResourceDetail.PhysicalResourceId' --output text)
FUNCTION=$(aws cloudformation describe-stack-resource --stack-name "$STACK" --logical-resource-id function --query 'StackResourceDetail.PhysicalResourceId' --output text)

echo "Stack: $STACK"
echo "Artifacts bucket: $ARTIFACT_BUCKET"
echo "App Bucket: $APP_BUCKET"
echo "Lambda: $FUNCTION"

aws logs delete-log-group --log-group-name /aws/lambda/$FUNCTION;

aws s3 rm --recursive s3://$APP_BUCKET
aws s3 rb --force s3://$APP_BUCKET;

aws cloudformation delete-stack --stack-name "$STACK"
echo "Deleted $STACK stack."

while true; do
    read -p "Delete artifacts bucket ($ARTIFACT_BUCKET)? (y/n)" response
    case $response in
        [Yy]* ) aws s3 rb --force s3://$ARTIFACT_BUCKET; break;;
        [Nn]* ) break;;
        * ) echo "Response must start with y or n.";;
    esac
done

rm -f out.yml out.json event.json
rm -rf build .gradle target
