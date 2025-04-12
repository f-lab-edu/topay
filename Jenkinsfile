pipeline {
	agent any

    environment {
		// 민감한 정보 (Jenkins Credential을 통해 주입)
        APP_SERVER_PUBLIC_IP = credentials('app-server-public-ip')

        DB_URL = credentials('jenkins-secret-db-url')
        DB_USERNAME = credentials('jenkins-secret-db-username')
        DB_PASSWORD = credentials('jenkins-secret-db-password')

        SESSION_HOST = credentials('redis-secret-session-host')
        SESSION_PORT = credentials('redis-secret-session-port')
        CACHE_HOST = credentials('redis-secret-cache-host')
        CACHE_PORT = credentials('redis-secret-cache-port')
    }

    stages {
		stage('Checkout ✨✨✨✨✨') {
			steps {
				checkout scm
            }
        }
        stage('Build ✨✨✨✨✨') {
			steps {
				sh 'chmod +x gradlew'
                sh './gradlew clean build -x test'
            }
        }
        stage('Test ✨✨✨✨✨') {
			steps {
				sh './gradlew test'
            }
        }
        stage('Prepare Deploy Artifacts ✨✨✨✨✨') {
			steps {
				sh '''
                    # 배포 폴더 생성
                    mkdir -p deploy

                    # 빌드된 JAR 파일 복사
                    cp build/libs/*SNAPSHOT.jar deploy/

                    # 프로덕션 설정 파일 복사
                    cp src/main/resources/application-prd.yml deploy/

                    # 배포 시간 기록
                    echo $(date +"%Y-%m-%d %H:%M:%S") > deploy/deploy.txt

                    # Dockerfile 복사
                    cp Dockerfile deploy/

                    # 배포 스크립트 복사
                    cp deploy.sh deploy/
                '''
            }
        }
        stage('Deploy to App Server ✨✨✨✨✨') {
			steps {
				sshagent(['app-server-ssh-cred']) {
					sh """
                      # 1) 배포 폴더 전송
                      scp -o StrictHostKeyChecking=no -r deploy ubuntu@${APP_SERVER_PUBLIC_IP}:/home/ubuntu/

                      # 2) 원격 서버에서 DB 및 Redis 변수들을 설정한 뒤 deploy.sh 실행
                      ssh -o StrictHostKeyChecking=no ubuntu@${APP_SERVER_PUBLIC_IP} \\
                        "cd /home/ubuntu/deploy && \\
                         DB_URL='${DB_URL}' DB_USERNAME='${DB_USERNAME}' DB_PASSWORD='${DB_PASSWORD}' \\
                         SESSION_HOST='${SESSION_HOST}' SESSION_PORT='${SESSION_PORT}' \\
                         CACHE_HOST='${CACHE_HOST}' CACHE_PORT='${CACHE_PORT}' \\
                         bash deploy.sh"
                    """
                }
            }
        }
    }
    post {
		always {
			cleanWs()
        }
    }
}