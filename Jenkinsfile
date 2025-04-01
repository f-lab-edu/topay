pipeline {
    agent any
    environment {
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
                sh './gradlew clean build -x test'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }
        stage('Prepare Deploy Artifacts') {
            steps {
                sh '''
                    mkdir -p deploy

                    cp build/libs/*SNAPSHOT.jar deploy/
                    cp application-prd.yml deploy/

                    echo $(date +"%Y-%m-%d %H:%M:%S") > deploy/deploy.txt

                    cp Dockerfile deploy/
                    cp deploy.sh deploy/
                '''
            }
        }
        stage('Deploy to App Server') {
            steps {
                sshagent(['app-server-ssh-cred']) {
                    sh """
                      scp -o StrictHostKeyChecking=no -r deploy ubuntu@${APP_SERVER_PUBLIC_IP}:/home/ubuntu/

                      ssh -o StrictHostKeyChecking=no ubuntu@${APP_SERVER_PUBLIC_IP} \\
                        "cd /home/ubuntu/deploy && \\
                         echo 'DB_URL: ${DB_URL}' && \\
                         echo 'DB_USERNAME: ${DB_USERNAME}' && \\
                         echo 'DB_PASSWORD: ${DB_PASSWORD}' && \\
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
