pipeline {
    agent any

    tools {
        jdk 'corretto-21'
        maven 'Maven-3.9.11'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
                // Alternatif: URL sabitlemek istersen, aşağıdakini kullanırsın:
                // checkout scmGit(
                //    branches: [[name: '*/master']],
                //    userRemoteConfigs: [[url: 'https://github.com/TolgaGunes/DEPO_ADI_GELECEK']]
                // )
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn -B clean test'
                    } else {
                        bat 'mvn -B clean test'
                    }
                }
            }
        }

    }

    post {
        success {
            echo 'Build und Tests erfolgreich abgeschlossen.'
        }
        failure {
            echo 'Build oder Tests sind fehlgeschlagen.'
        }
    }
}
