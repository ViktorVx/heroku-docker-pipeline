pipeline {
    agent {
        label 'java-11-docker-worker'
    }
    stages {
        stage("Check environment") {
            steps {
                sh(script:'docker version')
                sh(script:'heroku -v')
                sh(script:'echo ${HEROKU_USER_NAME}')
                withCredentials([string(credentialsId: 'HEROKU_TOKEN', variable: 'SECRET')]) {
                    echo "My secret text is '${SECRET}'"
                }
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
                    sh(script:'docker build -t ${HEROKU_APP} .')
                    withCredentials([string(credentialsId: 'HEROKU_TOKEN', variable: 'SECRET')]) {
                        sh(script:'docker login --username=${HEROKU_USER_NAME} --password=${SECRET} registry.heroku.com')
                    }
                    sh(script:'docker push ${HEROKU_APP}')
                }
            }
        }
    }
}