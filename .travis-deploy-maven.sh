#!/bin/bash

if [[ $TRAVIS_BRANCH == 'master' ]]
then
	cp ./keys/pubring.gpg $HOME/.gnupg/pubring.gpg && cp ./keys/secring.gpg $HOME/.gnupg/secring.gpg && cp ./keys/trustdb.gpg $HOME/.gnupg/trustdb.gpg && mvn -s ./.travis.settings.xml deploy -DskipTests && mvn site;	
fi 
