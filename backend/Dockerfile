FROM        hub.wuchuangroup.com/common/docker/jbuilder:lastest as builder

ARG         STAGE_DIR=/opt/stage

# Run Mode: dev or prod
ARG         MODE
ARG         REV

# 待优化: 区分易变的和稳定的目录
COPY        ./ ${STAGE_DIR}/

RUN         cd ${STAGE_DIR} && mkdir -p mvncache \
            && mvn -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn \
                -Dmaven.repo.local=./mvncache -DskipTests=true package -B -U

FROM        openjdk:8-jre-alpine

ARG         PREFIX=/opt/java

ARG         REV
ENV         REV=$REV

COPY        --from=builder /opt/stage/target/. ${PREFIX}/
COPY        --from=builder /opt/stage/run.sh  ${PREFIX}/
COPY        --from=builder /opt/stage/migrate  ${PREFIX}/migrate
COPY        --from=builder /opt/stage/.env  ${PREFIX}/
COPY        --from=builder /opt/stage/db.sh  ${PREFIX}/
COPY        --from=builder /opt/stage/version ${PREFIX}/

RUN         chmod 755 ${PREFIX}/migrate/bin/migrate ${PREFIX}/run.sh ${PREFIX}/db.sh

RUN         sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories \
            && apk add --no-cache perl bash

WORKDIR     ${PREFIX}
CMD         ["sh", "./run.sh"]


EXPOSE      3000
