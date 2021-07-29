pipeline {
    agent {
        label 'java-11-docker-worker'
    }
    stages {
        stage("Hello world stage") {
            steps {
                echo "Hello world!"
            }
        }
    }
}