FROM nginx:1.23

RUN apt-get -y update && apt-get -y upgrade && \
   DEBIAN_FRONTEND=noninteractive apt-get install -y \
   htop \
   sysstat \
   && apt-get autoremove -y && apt-get clean
