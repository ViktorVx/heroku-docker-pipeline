pipeline {
    agent {
        label 'java-11-docker-worker'
    }
    stages {
        stage("Check environment") {
            steps {
                sh(script:'docker version')
                sh(script:'heroku -v')
            }
        }
        stage("Clone app repo") {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'jenkins-user-github', url: 'https://github.com/ViktorVx/wordsFromWordBruteForce.git']]])

            }
        }
        stage("Docker build&push to Dockerhub") {
            steps {
                script {
                    docker.withRegistry('https://registry-1.docker.io', 'dockerhub-creds') {
                        def image = docker.build("${DOCKER_REPO_NAME}:latest")
                        image.push()
                    }
                }
            }
        }
        stage("Deploy to Heroku") {
            steps {
                script {
                    sh(script:'docker build -t registry.heroku.com/wfwbf-docker/web .')
                    sh(script:'docker push registry.heroku.com/wfwbf-docker/web')
                }
            }
        }
    }
}