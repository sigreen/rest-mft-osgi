Example Managed File Transfer (MFT) with Fuse Project
========================================================

To run this example project build the project and deploy to ServiceMix  
according to the steps below. 

Setup
==============================

- Install JBoss Developer Studio 8.1.0 [https://www.jboss.org/products/devstudio.html]
- Install Apache Maven 3+ [http://maven.apache.org]
- Install JBoss Fuse  6.2.x [https://www.jboss.org/products/fuse.html]

Build & Run
==============================

1) Build this project so bundles are deployed into your local maven repo

<project home> $ mvn clean install

2) Start JBoss Fuse

<JBoss Fuse home>  $ bin/fuse

3) Add this projects features.xml config to Fuse from the Console
   (makes it easier to install bundles with all required dependencies)

JBossFuse:karaf@root>  features:addUrl mvn:org.fusesource.examples/rest-mft-ws/4.0-SNAPSHOT/xml/features

4) Install the project.

JBossFuse:karaf@root>  features:install rest-mft-osgi

5) To calculate a digital signature required for sending test files, there are existing routes in the
   rest-mft-file module.

<project home> $ cp <yourTestFile> <JBoss Fuse home>/target/calculateSignature

   To see what happened look at the log file, either from the console

JBossFuse:karaf@root>  log:display

   or from the command line

<JBoss Fuse home> $ tail -f data/log/fuse.log

For your test file, you will see an LAU value printed.  Copy this value as you'll need it later.

6) To test the WS, use apache-jmeter with the JMX project included in rest-mft-ws/src/test/jmeter.  Update
the JMS project to reference your test files, using the LAU calculated for each test file in step 5.

Getting Help
============================

If you hit any problems please let the Fuse team know on the forums
  [https://community.jboss.org/en/jbossfuse]
