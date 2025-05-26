package org.iti;

def login(USERNAME, PASSWORD){
    sh "docker login -u ${USERNAME} -p ${PASSWORD}"
}

def build(IMAGE_NAME, IMAGE_TAG){
    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
}

def push(IMAGE_NAME, IMAGE_TAG){
    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
}


def call_python_app(){
    pipeline{
        agent{
            label "bash"
        }
        environment {
            DOCKER_CREDS = credentials('dockerhub-user')
        }


        stages{
            stage("build Docker image using python"){
                steps{
                    script{
                        def dockerx = new org.iti.docker()
                        dockerx.build("meldeeeb/python-app", "${BUILD_NUMBER}")
                    }
                
                }
            }
            stage("push Docker image using python"){
                steps{
                    script{
                        def dockerx = new org.iti.docker()
                        dockerx.login(env.DOCKER_CREDS_USR, env.DOCKER_CREDS_PSW)
                        dockerx.push("meldeeeb/python-app", "${BUILD_NUMBER}")

                    }
                }
            }
        }
    }
}


def call_java_app(){
    pipeline{
        agent {
            label 'bash'
        }

        environment {
            DOCKER_CREDS = credentials('dockerhub-user')
        }

        stages{
            stage("Build java app"){
                steps{
                    sh "mvn clean package install"
                }
            }
            stage("build java app image"){
                steps{
                    script{
                        def dockerx = new org.iti.docker()
                        dockerx.build("meldeeeb/java-app", "${BUILD_NUMBER}")
                    }
                }
            }
            stage("push java app image"){
                steps{
                    script{
                        def dockerx = new org.iti.docker()
                        dockerx.login(env.DOCKER_CREDS_USR, env.DOCKER_CREDS_PSW)
                        dockerx.push("meldeeeb/java-app", "${BUILD_NUMBER}")
                    }
                }
            }
        }
    }
}