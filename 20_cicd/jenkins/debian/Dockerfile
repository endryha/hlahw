FROM jenkins/jenkins:latest

USER root

#install docker
RUN apt-get update && \
       apt-get -y install apt-transport-https \
       ca-certificates \
       curl \
       gnupg2 \
       software-properties-common && \
       curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg > /tmp/dkey; apt-key add /tmp/dkey && \
       add-apt-repository \
       "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") \
       $(lsb_release -cs) \
       stable" && \
       apt-get update && \
       apt-get -y install docker-ce

RUN touch /var/run/docker.sock
RUN chmod 777 /var/run/docker.sock
RUN usermod -a -G docker jenkins

USER jenkins

COPY plugins.txt /usr/share/jenkins/ref/
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
ENV JENKINS_USER admin
ENV JENKINS_PASS P@ssw0rd

COPY default-user.groovy /usr/share/jenkins/ref/init.groovy.d/