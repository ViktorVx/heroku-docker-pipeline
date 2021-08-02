pipeline {
    agent {
        label 'java-11-docker-worker'
    }
    stages {
        stage("Clone repository") {
//            steps {
//                checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'jenkins-user-github', url: 'https://github.com/ViktorVx/wordsFromWordBruteForce.git']]])
//
//            }
        }
        stage("Docker build") {
            steps {
                script {
                    sh(script:'whoami')
                    sh(script:'docker version')
                }
            }
        }
    }
}