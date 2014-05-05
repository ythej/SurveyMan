smversion := 1.5
projectdir = $(shell pwd)
pythonpath := $(projectdir)/src/python
npmargs := -g --prefix ./src/javascript
jslib := src/javascript/lib/node_modules
mvnargs := -Dpackaging=jar -DgroupId=com.amazonaws -Dversion=1.6.2 #-DlocalRepositoryPath=local-mvn -Durl=file:$(projectdir)/local-mvn
travisTests := CSVTest MetricTest RandomRespondentTest SystemTest
lein := $(shell if [[ -z `which lein2` ]]; then echo "lein"; else echo "lein2"; fi)

# this line clears ridiculous number of default rules
.SUFFIXES:
.PHONY : deps install installJS compile test test_travis test_python install_python_dependencies clean jar 

deps: lib/java-aws-mturk.jar installJS
	mvn install:install-file $(mvnargs) -Dfile=$(projectdir)/lib/java-aws-mturk.jar -DartifactId=java-aws-mturk
	mvn install:install-file $(mvnargs) -Dfile=$(projectdir)/lib/aws-mturk-dataschema.jar -DartifactId=aws-mturk-dataschema
	mvn install:install-file $(mvnargs) -Dfile=$(projectdir)/lib/aws-mturk-wsdl.jar -DartifactId=aws-mturk-wsdl
	$(lein) deps

lib/java-aws-mturk.jar:
	./scripts/setup.sh

installJS:
	mkdir -p $(jslib)
	npm install underscore $(npmargs)
	npm install jquery $(npmargs)
	npm install seedrandom $(npmargs)

compile : deps installJS
	$(lein) with-profile stage1 javac
	$(lein) with-profile stage2 compile
	$(lein) with-profile stage3 javac
	$(lein) with-profile stage4 compile

test : compile
	$(lein) junit
	$(lein) test 


test_travis : 
	$(lein) junit $(travisTests)
	$(lein) test testAnalyses


clean : 
	rm -rf ~/surveyman/.metadata
	rm -rf $(jslib)
	rm -rf lib
	rm -rf ~/.m2
	$(lein) clean

package : 
	mvn clean
	mvn package -DskipTests
	git checkout -- params.properties 
	cp -r target/appassembler/bin .
	cp -r target/appassembler/lib .
	cp scripts/setup.py .
	chmod +x setup.py
	chmod +x bin/*
	zip surveyman${smversion}.zip bin/* lib/* params.properties data/samples/* setup.py src/javascript/* /$(jslib)/jquery/dist/jquery.js /$(jslib)/underscore/underscore.js /$(jslib)/seedrandom/seedrandom.js
	rm setup.py
	rm -rf setup.py deploy
	mkdir deploy
	mv bin lib *.zip deploy

