pipeline {
    // 모든 에이전트에서 실행
    agent any

    environment {
        // 앱 서버 IP 및 DB 정보 (Jenkins Credential을 통해 주입)
        APP_SERVER_PUBLIC_IP = '3.36.82.140'
        DB_URL = credentials('jenkins-secret-db-url')
        DB_USERNAME = credentials('jenkins-secret-db-username')
        DB_PASSWORD = credentials('jenkins-secret-db-password')
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh 'chmod +x gradlew'

                // 테스트 제외하고 빌드
                sh './gradlew clean build -x test'
            }
        }
        stage('Test') {
            steps {
                // 테스트 실행
                sh './gradlew test'
            }
        }
        stage('Prepare Deploy Artifacts') {
            steps {
                sh '''
                    // 배포 폴더 생성
                    mkdir -p deploy

                    // 빌드된 JAR 파일 복사
                    cp build/libs/*SNAPSHOT.jar deploy/

                    // 프로덕션 설정 파일 복사
                    cp src/main/resources/application-prd.yml deploy/

                    // 배포 시간 기록
                    echo $(date +"%Y-%m-%d %H:%M:%S") > deploy/deploy.txt

                    // Dockerfile 복사
                    cp Dockerfile deploy/

                    // 배포 스크립트 복사
                    cp deploy.sh deploy/
                '''
            }
        }
        stage('Deploy to App Server') {
            steps {
                // 앱 서버 SSH 접속 자격증명 사용
                sshagent(['app-server-ssh-cred']) {
                    sh """
                      // 배포 폴더 전송
                      scp -o StrictHostKeyChecking=no -r deploy ubuntu@${APP_SERVER_PUBLIC_IP}:/home/ubuntu/

                      ssh -o StrictHostKeyChecking=no ubuntu@${APP_SERVER_PUBLIC_IP} \\
                        "cd /home/ubuntu/deploy && \\
                         echo 'DB_URL: ${DB_URL}' && \\
                         echo 'DB_USERNAME: ${DB_USERNAME}' && \\
                         echo 'DB_PASSWORD: ${DB_PASSWORD}' && \\

                         // 원격 서버에서 배포 스크립트 실행
                         bash deploy.sh"
                    """
                }
            }
        }
    }
    post {
        always {
            // 빌드 후 워크스페이스 정리
            cleanWs()
        }
    }
}
