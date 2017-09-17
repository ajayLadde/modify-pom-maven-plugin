# modify-pom-maven-plugin [![Build Status](https://travis-ci.org/amanganiello90/modify-pom-maven-plugin.svg)](https://travis-ci.org/amanganiello90/modify-pom-maven-plugin)

<a href="http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22modify-pom-maven-plugin%22"><img src="https://img.shields.io/maven-central/v/com.github.amanganiello90/modify-pom-maven-plugin.svg">
</a>&nbsp;<img src="https://img.shields.io/github/forks/amanganiello90/modify-pom-maven-plugin.svg">&nbsp;
<img src="https://img.shields.io/github/stars/amanganiello90/modify-pom-maven-plugin.svg">&nbsp;<a href="https://github.com/amanganiello90/modify-pom-maven-plugin/issues"><img src="https://img.shields.io/github/issues/amanganiello90/modify-pom-maven-plugin.svg">
</a>&nbsp;<img src="https://img.shields.io/github/license/amanganiello90/modify-pom-maven-plugin.svg">&nbsp;<img src="https://img.shields.io/github/downloads/amanganiello90/modify-pom-maven-plugin/total.svg">&nbsp;


This is a maven plugin implemented ad hoc to manage pom versions and properties.
It is a github reference maven plugin automatized with **travis continuous integration, deploy on maven central, publish site and JUnit tests for plugins**.


|Please donate whether you wish support us to give more time to app's growth | [![](https://www.paypal.com/en_US/IT/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=XTC895QYD28TC) |
|:------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------|

* build information

The build and the deploy on maven central are automated with **travis** and **nexus-staging-maven-plugin** that releases the artifacts.
For this the jars are before signed with the _maven-gpg-plugin_ that use the gpg keys under the **keys** folder in the maven deploy phase.
The maven deploy is run only on the master branch with the _.travis-deploy-maven_ script called from the _.travis.yml_ together the publishing with the maven site on github **gh-pages** branch.

[Plugin Usage Guide](https://amanganiello90.github.io/modify-pom-maven-plugin)

### News ###

September 17,2017  | **Release 1.0.1** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Ccom.github.amanganiello90%7Cmodify-pom-maven-plugin%7C1.0.1%7Cmaven-plugin)**  |
---- | ---- | ---- |

* The same of 1.0 but with name modify from **manage-pom-maven-plugin to modify-pom-maven-plugin** and improvements for code quality. Some classes added for the next release to use the future property set goal.


August 31,2017  | **Release 1.0** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Ccom.github.amanganiello90%7Cmanage-pom-maven-plugin%7C1.0%7Cmaven-plugin)**  |
---- | ---- | ---- |

* First release with only check-snapshots goal tested.


