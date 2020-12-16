#!/bin/bash

PACT_BROKER_URL="http://ip172-18-0-60-bvd3717p2ffg00f6oca0-9292.direct.labs.play-with-docker.com/"
GIT_VERSION=$(eval "git rev-parse HEAD")

docker run --rm \
   -w ${PWD} \
   -v ${PWD}:${PWD} \
    pactfoundation/pact-cli:latest \
    publish \
    ${PWD}/target/pacts \
    --broker-base-url $PACT_BROKER_URL \
    --consumer-app-version $GIT_VERSION \
    --tag-with-git-branch
