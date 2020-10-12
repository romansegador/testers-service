#!/bin/bash

PACT_BROKER_URL="http://ip172-18-0-32-bu22dl7p2ffg00blhnvg-9292.direct.labs.play-with-docker.com/"
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
