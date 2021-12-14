PROJECT_NAME=modi
BUILD_JAR=$(ls /home/ubuntu/action/build/libs/*.jar)
JAR_NAME=$(ls -tr $BUILD_JAR | tail -n 1)

source /etc/profile.d/codedeploy.sh
echo ${RDS_URL} >>/home/ubuntu/action/deploy.log
echo ${RDS_USERNAME} >>/home/ubuntu/action/deploy.log
echo ${RDS_PASSWORD} >>/home/ubuntu/action/deploy.log

echo "> Build 파일명: $JAR_NAME" >>/home/ubuntu/action/deploy.log

echo "> 현재 구동 중인 애플리케이션 pid 확인" >>/home/ubuntu/action/deploy.log
CURRENT_PID=$(ps -e | grep java | awk '{print $1}')

echo "> 현재 구동 중인 애플리케이션 pid: $CURRENT_PID" >>/home/ubuntu/action/deploy.log

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >>/home/ubuntu/action/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 어플리케이션 배포" >>/home/ubuntu/action/deploy.log

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME에 실행권한 추가" >>/home/ubuntu/action/deploy.log

chmod +x $JAR_NAME

pwd >>/home/ubuntu/action/deploy.log

echo "> $JAR_NAME 실행" >>/home/ubuntu/action/deploy.log

nohup java -jar -Dspring.profiles.active=prod $JAR_NAME >/home/ubuntu/action/build/libs/nohup.out 2>&1 &

exit 0
