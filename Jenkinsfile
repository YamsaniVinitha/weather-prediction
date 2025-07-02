pipeline {
	agent any
	tools {
		maven 'my-maven-tool'
    }
    stages {
		stage('Checkout') {
			steps {
				git branch: 'master', url: 'https://github.com/YamsaniVinitha/weather-prediction'
			}
		}
		stage('Build and Test') {
			steps {
				sh 'mvn clean install'
            }
        }
        stage('Run Sonarqube') {
			environment {
				scannerHome = tool 'my-sonarqube-scanner';
            }
            steps {
				withSonarQubeEnv(credentialsId: 'sonar-token-for-jenkins', installationName: 'my-sonar-server') {
					sh "${scannerHome}/bin/sonar-scanner"
              }
            }
        }
        stage('Build Docker Image') {
			steps {
				script {
					docker.build("weather-prediction:latest")
                }
			}
		}
		stage('Publish Docker Image') {
			steps {
				script {
					docker.withRegistry('https://hub.docker.com/repositories/dockervinitha', 'dockervinitha') {
						docker.image("weather-prediction:latest").push()
                    }
                }
            }
        }
    }
}