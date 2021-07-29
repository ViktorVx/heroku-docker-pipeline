pipeline {
    agent {
        label 'java-11-docker-worker'
    }
    stages {
        stage("Clone repository") {
            steps {
                git url: 'https://github.com/ViktorVx/wordsFromWordBruteForce.git'
            }
        }
        stage("Package jar") {
            steps {
                sh "ls -la"
            }
        }
    }
}