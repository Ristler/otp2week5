pipeline {
    agent any
    tools {
        maven 'Maven 3.9.11'
    }

    environment {
        PATH = "/usr/local/bin:${env.PATH}"
        JAVA_HOME = "/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home"
        SONARQUBE_SERVER = 'SonarQubeServer'  // The name of the SonarQube server configured in Jenkins
        SONAR_TOKEN = 'sqa_872af17eef7a4ae4a7f116ab8fad6652cb4bc888' // Store the token securely
        DOCKERHUB_CREDENTIALS_ID = 'docker_hub'
        DOCKERHUB_REPO = 'ristler/sep2_week5'
        DOCKER_IMAGE_TAG = 'latest'

    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/Ristler/otp2week5.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    script {
                        def scanner = tool 'SonarScanner'
                        if (isUnix()) {
                            sh """
                                ${scanner}/bin/sonar-scanner \
                                -Dsonar.projectKey=devops-demo \
                                -Dsonar.sources=src \
                                -Dsonar.projectName=DevOps-Demo \
                                -Dsonar.host.url=http://localhost:9000 \
                                -Dsonar.login=${env.SONAR_TOKEN} \
                                -Dsonar.java.binaries=target/classes
                            """
                        } else {
                            bat """
                                ${scanner}\\bin\\sonar-scanner ^
                                -Dsonar.projectKey=devops-demo ^
                                -Dsonar.sources=src ^
                                -Dsonar.projectName=DevOps-Demo ^
                                -Dsonar.host.url=http://localhost:9000 ^
                                -Dsonar.login=${env.SONAR_TOKEN} ^
                                -Dsonar.java.binaries=target/classes
                            """
                        }
                    }
                }
            }
        }



        stage('Build Docker Image') {
                    steps {
                        script {
                            docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                            // Or specify Dockerfile path explicitly if needed
                            // docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}", "-f ./Dockerfile .")
                        }
                    }
                }

                stage('Push Docker Image to Docker Hub') {
                    steps {
                        script {
                            docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
                                docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                            }
                        }
                    }
                }
    }
}
