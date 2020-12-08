
# Use official "Oracle 12c standard edition" image
FROM container-registry.oracle.com/database/standard

# Set runtime environment variable values
ENV DB_SID=OraDoc
ENV DB_PASSWD=MyPasswd123
ENV DB_BUNDLE=basic
ENV DB_DOMAIN=oraldr

# Volumes mounted from the host O/S
VOLUME /home/oracle/inbox
VOLUME /home/oracle/outbox

# Ports exposed to the host O/S
EXPOSE 5500/tcp
EXPOSE 1521/tcp
EXPOSE 8000/tcp

# Extend the image...
WORKDIR /home/oracle
COPY --chown=oracle:oinstall startup.sh .
COPY --chown=oracle:oinstall scripts scripts/
COPY --chown=oracle:oinstall webserver webserver/
USER oracle
RUN mkdir extbin
RUN cp /usr/bin/zcat extbin
RUN chmod +x startup.sh
RUN chmod +x webserver/cgi-bin/*

# initialize as root
USER root
