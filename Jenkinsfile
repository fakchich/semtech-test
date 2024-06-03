boolean isRelease
boolean isHotfix

pipeline {
    agent any
    tools {
        maven 'maven-3.8.5'
    }
    environment {
        REGISTRY = "afakchich/semtech-test"
        DOCKER_IMAGE = "semtech-test"
        DATE = new Date().format('yy.M')
        TAG = "${DATE}.${BUILD_NUMBER}"
    }
    parameters {
            choice(name: 'BUILD_TYPE',
                    choices: ['Snapshot',
                              'Release',
                              'Hotfix'],
                    description: 'Pick build type.')
        }
    stages {
        stage('Cloning Git') {
          steps {
            git branch: 'main',
                                url:  'https://github.com/fakchich/semtech-test'
          }
        }

        stage('Prepare release') {
                            when {
                                expression {
                                    isRelease
                                }
                            }
                            steps {
                                // gitflow:release-start updates project.version in pom.xml
                                sh 'mvn gitflow:release-start'
                            }
        }

        stage ('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Unit tests') {
                    steps {
                        // Only launch unit tests
                        sh 'mvn test'
                    }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t afakchich/semtech-test:latest .'
            }
        }
	    stage('Pushing Docker Image to Dockerhub') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker_credential') {
                        docker.image("${REGISTRY}:${TAG}").push()
                        docker.image("${REGISTRY}:${TAG}").push("latest")
                    }
                }
            }
        }
        stage('Deploy'){
            steps {
                sh "docker stop ${DOCKER_IMAGE} | true"
                sh "docker rm ${DOCKER_IMAGE} | true"
                sh "docker run --name ${DOCKER_IMAGE} -t ${REGISTRY}:${TAG}"
            }
        }

        stage('Finish release') {
                    when {
                        expression {
                            isRelease
                        }
                    }
                    steps {
                        sh 'mvn gitflow:release-finish'
                        sh 'git push origin develop:develop'
                        sh 'git push origin main:main'
                        sh 'git push --tags'
                    }
        }

        stage('Remove Unused docker image') {
              steps{
                sh "docker rmi ${REGISTRY}:$BUILD_NUMBER"
              }
        }
    }
}