export appHome=$(pwd)

project=MN-Demo-1
group=com.example
myJars=$appHome/postgresql-42.2.9.jar

export src=$appHome/src/main/java/$group/micronaut
export cls=$appHome/build/classes/java/main/$group/micronaut
export jar=$appHome/build/lib
export CLASSPATH=./:$appHome:$jar:$src:$cls:$CLASSPATH

alias h="cd $appHome;clear"
alias s="cd $src;clear"
alias ga="clear;gradlew assemble"
alias ni="clear;native-image -cp $myJars:build/libs/$project-0.1-all.jar:$CLASSPATH"
alias ms="clear;microservice -Xmx136m &"

