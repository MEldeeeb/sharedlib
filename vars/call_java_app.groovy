
def call() {

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
                    sh "docker build -t meldeeeb/java-app:v${BUILD_NUMBER} ."
                }
            }
            stage("Login to Docker Hub"){
                steps{
                    sh "docker login -u ${DOCKER_CREDS_USR} -p ${DOCKER_CREDS_PSW}"
                }
            }
            stage("push java app image"){
                steps{
                    sh "docker push meldeeeb/java-app:v${BUILD_NUMBER}"
                }
            }
        }
    }
}