pipeline {
    agent any

    environment {
        IMAGE_NAME = "spring-login"
        IMAGE_TAG = "0.0.1"
        PATH = "/opt/homebrew/bin:/usr/local/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh '''
                    docker compose down
                    docker compose up -d
                '''
            }
        }

        stage('Verify') {
            steps {
                sh 'docker compose ps'
                sh 'sleep 15 && curl -f http://localhost:8081 || echo "App not responding yet, check logs"'
            }
        }
    }

    post {
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Pipeline failed — check logs above.'
            sh 'docker compose logs --tail=50'
        }
        always {
            sh 'docker image prune -f'
        }
    }
}
