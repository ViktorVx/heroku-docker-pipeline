pipeline {
    agent {
        label 'java-11-docker-worker'
    }
    stages {
        stage("Clone repository") {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'jenkins-user-github', url: 'https://github.com/ViktorVx/wordsFromWordBruteForce.git']]])

            }
        }
        stage("Docker build") {
            steps {
                script {
                    sh(script:'docker version')
                    docker.withRegistry('https://hub.docker.com/', 'dockerhub-creds') {
                        def image = docker.build("${DOCKER_REPO_NAME}:latest")
                        image.push()
                    }
                }
            }
        }
    }
}