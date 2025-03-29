pipeline {
	agent any
    environment {
		APP_SERVER_PUBLIC_IP = '3.36.82.140'
    }
    stages {
		stage('Checkout') {
			steps {
				// Git Credentials를 사용해 코드 가져오기
                checkout scm
            }
        }
        stage('Build') {
			steps {
				// gradlew에 실행 권한 부여
                sh 'chmod +x gradlew'
                // 로컬에서 Gradle 빌드 (테스트 제외)
                sh './gradlew clean build -x test'
            }
        }
        stage('Test') {
			steps {
				// 테스트
                sh './gradlew test'
            }
        }
        stage('Prepare Deploy Artifacts') {
			steps {
				// 배포에 필요한 파일을 모아서 deploy 폴더 생성
                sh '''
                    mkdir -p deploy
                    # 빌드된 JAR 복사
                    cp build/libs/*SNAPSHOT.jar deploy/
                    # 날짜정보 기록
                    echo $(date +"%Y-%m-%d %H:%M:%S") > deploy/deploy.txt
                    # Dockerfile과 deploy.sh도 함께 복사
                    cp Dockerfile deploy/
                    cp deploy.sh deploy/
                '''
            }
        }
        stage('Deploy to App Server') {
			steps {
				// SSH 자격 증명 사용
                sshagent(['app-server-ssh-cred']) {
					// deploy 폴더를 Application 서버로 전송 후, deploy.sh 실행
                    sh """
                      scp -o StrictHostKeyChecking=no -r deploy ubuntu@${APP_SERVER_PUBLIC_IP}:/home/ubuntu/
                      ssh -o StrictHostKeyChecking=no ubuntu@${APP_SERVER_PUBLIC_IP} "cd /home/ubuntu/deploy && bash deploy.sh"
                    """
                }
            }
        }
    }
    post {
		always {
			// 작업공간 정리
            cleanWs()
        }
    }
}
